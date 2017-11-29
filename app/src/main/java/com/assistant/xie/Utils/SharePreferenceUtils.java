package com.assistant.xie.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XIE on 2017/11/29.
 * 缓存工具类
 */

public class SharePreferenceUtils {
    public final static String SAVE_NAME_PHONE_STATE = "share_preference_phone_state";

    /**
     * 缓存字符串数据
     *
     * @param context context
     * @param name    模块名
     * @param key     key
     * @param value   value
     */
    public static void saveStringData(Context context, String name, String key, String value) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 读取字符串数据
     *
     * @param context      context
     * @param name         模块名
     * @param key          key
     * @param defaultValue defaultValue
     * @return String
     */
    public static String loadStringData(Context context, String name, String key, String defaultValue) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * 批量缓存字符串数据
     *
     * @param context context
     * @param name    模块名
     * @param data    Map<key, value>
     */
    public static void saveStringData(Context context, String name, Map<String, String> data) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.apply();
    }

    /**
     * 批量获取字符串数据
     *
     * @param context   context
     * @param name      模块名
     * @param queryData Map<key, defaultValue>
     * @return Map<key, value>
     */
    public static Map<String, String> loadStringData(Context context, String name, Map<String, String> queryData) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        HashMap<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : queryData.entrySet()) {
            result.put(entry.getKey(), mSharedPreferences.getString(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    /**
     * 缓存整型数据
     *
     * @param context context
     * @param name    模块名
     * @param key     key
     * @param value   value
     */
    public static void saveIntData(Context context, String name, String key, int value) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 批量缓存整型数据
     *
     * @param context context
     * @param name    模块名
     * @param data    Map<key, value>
     */
    public static void saveIntData(Context context, String name, Map<String, Integer> data) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            editor.putInt(entry.getKey(), entry.getValue());
        }
        editor.apply();
    }


    /**
     * 批量读取整型数据
     *
     * @param context   context
     * @param name      模块名
     * @param queryData Map<key, defaultValue>
     * @return Map<key, value>
     */
    public static Map<String, Integer> loadIntData(Context context, String name, Map<String, Integer> queryData) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        HashMap<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : queryData.entrySet()) {
            result.put(entry.getKey(), mSharedPreferences.getInt(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    /**
     * 读取整型数据
     *
     * @param context      context
     * @param name         模块名
     * @param key          key
     * @param defaultValue defaultValue
     * @return String
     */
    public static int loadIntData(Context context, String name, String key, int defaultValue) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 缓存布尔数据
     *
     * @param context context
     * @param name    模块名
     * @param key     key
     * @param value   value
     */
    public static void saveBooleanData(Context context, String name, String key, boolean value) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 批量缓存布尔数据
     *
     * @param context context
     * @param name    模块名
     * @param data    Map<key, value>
     */
    public static void saveBooleanData(Context context, String name, Map<String, Boolean> data) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (Map.Entry<String, Boolean> entry : data.entrySet()) {
            editor.putBoolean(entry.getKey(), entry.getValue());
        }
        editor.apply();
    }

    /**
     * 批量读取布尔数据
     *
     * @param context   context
     * @param name      模块名
     * @param queryData Map<key, defaultValue>
     * @return Map<key, value>
     */
    public static Map<String, Boolean> loadBooleanData(Context context, String name, Map<String, Boolean> queryData) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        HashMap<String, Boolean> result = new HashMap<>();
        for (Map.Entry<String, Boolean> entry : queryData.entrySet()) {
            result.put(entry.getKey(), mSharedPreferences.getBoolean(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    /**
     * 读取布尔数据
     *
     * @param context      context
     * @param name         模块名
     * @param key          key
     * @param defaultValue defaultValue
     * @return String
     */
    public static Boolean loadBooleanData(Context context, String name, String key, Boolean defaultValue) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, defaultValue);
    }
}
