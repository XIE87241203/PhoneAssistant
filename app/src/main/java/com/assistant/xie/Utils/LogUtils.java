package com.assistant.xie.Utils;

import android.util.Log;

/**
 * Created by XIE on 2018/2/8.
 * Log工具类
 */

public class LogUtils {
    private boolean isEnable = false;
    private static LogUtils instance;

    public synchronized static LogUtils getInstance() {
        if (instance == null) {
            instance = new LogUtils();
        }
        return instance;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void v(String tag, String msg) {
        Log.v(tag, msg);
    }
}
