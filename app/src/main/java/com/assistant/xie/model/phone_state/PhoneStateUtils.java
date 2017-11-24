package com.assistant.xie.model.phone_state;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by XIE on 2017/11/24.
 * 获取手机状态工具类
 */

class PhoneStateUtils {
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
            return "获取失败";
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
            return "获取失败";
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
            return "获取失败";
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
            return "获取失败";
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
