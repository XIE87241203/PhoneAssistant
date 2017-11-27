package com.assistant.xie.model.phone_state;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.util.Log;

import com.assistant.xie.model.main.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by XIE on 2017/11/24.
 * 获取手机状态工具类
 */

class PhoneStateUtils {
    private static final String STRING_GET_BATTERY_STATE_FAIL = "获取失败";
    private static final String STRING_NO_SDCARD = "未检测到内存卡";
    private static final String STRING_NO_SDCARD_PERMISSION = "无内存卡权限，无法获取内存卡状态";
    private boolean sdCardHasPermission = false;//是否有内存卡权限
    private static PhoneStateUtils instance;
    private String batteryState;//电池状态信息
    private MyBroadcastReceiver broadcastReceiver;//电池广播监听

    static PhoneStateUtils getInstance() {
        if (instance == null) {
            instance = new PhoneStateUtils();
        }
        return instance;
    }

    /**
     * 获取可用内存
     *
     * @param context context
     * @return String
     */
    synchronized String getAvailMemory(Context context) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        if (am != null) {
            am.getMemoryInfo(mi);
        }
        //mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    /**
     * 获取总内存
     *
     * @param context context
     * @return String
     */
    synchronized String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Long.parseLong(arrayOfString[1]) * 1024L;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 设置有无访问内存卡权限标记
     *
     * @param isGrant 是否具有权限
     */
    void setSDCardHasPermission(boolean isGrant) {
        sdCardHasPermission = isGrant;
    }

    /**
     * 获取内存卡存储状态
     *
     * @param context context
     * @return 内存卡存储状态
     */
    String getSDUsageStatus(Context context) {
        if (!sdCardHasPermission) return STRING_NO_SDCARD_PERMISSION;
        // 判断是否有插入并挂载存储卡(通过判断设备是否能够移除来判断是否是内存卡)
        if (Environment.isExternalStorageRemovable() && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return getSDAvailableSize(context) + "/" + getSDTotalSize(context);
        } else {
            return STRING_NO_SDCARD;
        }
    }

    /**
     * 获取手机内部存储状态
     *
     * @param context context
     * @return 手机内部存储状态
     */
    String getROMUsageStatus(Context context) {
        return getRomAvailableSize(context) + "/" + getRomTotalSize(context);
    }

    /**
     * 获得SD卡总大小
     *
     * @return SD卡总大小
     */
    private String getSDTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);

    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return sd卡剩余容量
     */
    private String getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        Log.v("testMsg", "getExternalStorageDirectory-->" + path.getPath());
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获得机身存储总大小
     *
     * @return 机身存储总大小
     */
    private String getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用存储容量
     *
     * @return 机身可用存储容量
     */
    private String getRomAvailableSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 注册电池监听广播
     *
     * @param context context
     */
    void registerBatteryBroadcast(Context context) {
        if (broadcastReceiver != null) return;
        broadcastReceiver = new MyBroadcastReceiver();
        // 注册一个系统 BroadcastReceiver，作为访问电池计量之用这个不能直接在AndroidManifest.xml中注册
        context.getApplicationContext().registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    /**
     * 反注册电池监听广播
     *
     * @param context context
     */
    void unregisterBatteryBroadcast(Context context) {
        context.getApplicationContext().unregisterReceiver(broadcastReceiver);
        broadcastReceiver = null;
    }

    /**
     * 获取电量
     * 需要先注册电池广播监听
     *
     * @return 电量百分比
     * @see #registerBatteryBroadcast(Context)
     */
    synchronized String getBattery() {
        if (broadcastReceiver != null) {
            return broadcastReceiver.battery + "%";
        } else {
            return STRING_GET_BATTERY_STATE_FAIL;
        }
    }

    /**
     * 获取当前电压
     * 需要先注册电池广播监听
     *
     * @return 电压
     * @see #registerBatteryBroadcast(Context)
     */
    synchronized String getBatteryV() {
        if (broadcastReceiver != null) {
            return broadcastReceiver.batteryV + "mV";
        } else {
            return STRING_GET_BATTERY_STATE_FAIL;
        }
    }

    /**
     * 获取电池温度
     * 需要先注册电池广播监听
     *
     * @return 电池温度
     * @see #registerBatteryBroadcast(Context)
     */
    synchronized String getBatteryT() {
        if (broadcastReceiver != null) {
            return (broadcastReceiver.batteryT * 0.1) + "℃";
        } else {
            return STRING_GET_BATTERY_STATE_FAIL;
        }
    }

    /**
     * 电池状态
     * 需要先注册电池广播监听
     *
     * @return 电池状态
     * @see #registerBatteryBroadcast(Context)
     */
    synchronized String getBatteryStatus() {
        if (broadcastReceiver != null) {
            return broadcastReceiver.batteryStatus;
        } else {
            return STRING_GET_BATTERY_STATE_FAIL;
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        int battery;//目前电量
        int batteryV;//电池电压
        long batteryT;//电池温度
        String batteryStatus;//电池充电状态
        String batteryTemp;//电池状态

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*
             * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
             */
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                battery = intent.getIntExtra("level", 0);    //目前电量
                batteryV = intent.getIntExtra("voltage", 0);  //电池电压
                batteryT = intent.getIntExtra("temperature", 0);  //电池温度

                switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        batteryStatus = "充电状态";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        batteryStatus = "放电状态";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        batteryStatus = "未充电";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        batteryStatus = "充满电";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        batteryStatus = "未知状态";
                        break;
                }

                switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        batteryTemp = "未知错误";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        batteryTemp = "状态良好";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        batteryTemp = "电池没有电";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        batteryTemp = "电池电压过高";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        batteryTemp = "电池过热";
                        break;
                }

            }
        }


    }
}
