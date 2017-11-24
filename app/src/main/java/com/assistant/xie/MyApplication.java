package com.assistant.xie;

import android.app.Application;

import com.assistant.xie.crash_msg_save.CrashHandler;

/**
 * Created by XIE on 2017/11/23.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
