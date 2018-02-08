package com.assistant.xie.Utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import java.text.DecimalFormat;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by XIE on 2017/11/29.
 * 通用工具类
 */

public class CommonMethods {

    /**
     * 判断字符串是否为null或为""
     * @param str String
     * @return boolean
     */
    public static boolean isEmptyString(String str){
        return str == null || str.equals("");
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext    Context
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceName.equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断AccessibilityService服务是否已经启动
     *
     * @param context context
     * @param name    服务名字
     * @return boolean
     */
    public static boolean isStartAccessibilityService(Context context, String name) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> serviceInfos = null;
        if (am != null) {
            serviceInfos = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
            for (AccessibilityServiceInfo info : serviceInfos) {
                String id = info.getId();
//                Log.v("testMsg", "all -->" + id + "  name-->" + name);
                if (id.contains(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 数据单位自动向上转换，最小为B，最大为GB
     * @param dataSize
     * @return
     */
    public static String dataUnitConversion(long dataSize){
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        double dataSizeD = dataSize;
        String unit = "B";
        if(dataSizeD>500){
            dataSizeD = dataSizeD/1024;
            unit = "KB";
        }
        if(dataSizeD>500){
            dataSizeD = dataSizeD/1024;
            unit = "M";
        }
        if(dataSizeD>500){
            dataSizeD = dataSizeD/1024;
            unit = "G";
        }
        return decimalFormat.format(dataSizeD) + unit;
    }
}
