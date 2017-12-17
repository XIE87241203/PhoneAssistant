package com.assistant.xie.model.phone_state;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by XIE on 2017/11/27.
 * 悬浮窗服务
 */

public class FloatWindowService extends Service {
    //悬浮窗刷新时间
    public static final int REFRESH_TIME = 2000;
    private static final int HANDLER_WHAT_REFRESH_MSG = 1;
    private static final int HANDLER_WHAT_REFRESH_VIEW = 2;
    public static final String ACTION_REFRESH_FLOAT_VIEW = "com.assistant.xie.REFRESH_FLOAT_VIEW";
    public static final String ACTION_PHONE_STATE_ACTIVITY_CLOSE = "com.assistant.xie.PHONE_STATE_ACTIVITY_CLOSE";
    public static final String ACTION_PHONE_STATE_ACTIVITY_OPEN = "com.assistant.xie.PHONE_STATE_ACTIVITY_OPEN";

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;

    //更新UI用Handler
    private UIHandler handler;

    public PhoneStateFloatView phoneStateFloatView;

    private RefreshFloatViewReceiver receiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new UIHandler(this);
        // 开启定时器，每隔1秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshFloatWindowTask(), 0, REFRESH_TIME);
        }
        phoneStateFloatView = new PhoneStateFloatView(getApplicationContext());
        //注册广播接收器
        receiver = new RefreshFloatViewReceiver();
        registerReceiver(receiver, new IntentFilter(ACTION_REFRESH_FLOAT_VIEW));
        registerReceiver(receiver, new IntentFilter(ACTION_PHONE_STATE_ACTIVITY_CLOSE));
        registerReceiver(receiver, new IntentFilter(ACTION_PHONE_STATE_ACTIVITY_OPEN));
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 处理UI更新的Handler
     */
    static class UIHandler extends Handler {
        WeakReference<FloatWindowService> mService;

        UIHandler(FloatWindowService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            FloatWindowService service = mService.get();
            switch (msg.what) {
                case HANDLER_WHAT_REFRESH_MSG:
                    service.phoneStateFloatView.refreshMsg();
                    break;
                case HANDLER_WHAT_REFRESH_VIEW:
                    service.phoneStateFloatView.refreshView();
                    break;
            }
        }
    }


    public class RefreshFloatViewReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case ACTION_REFRESH_FLOAT_VIEW:
                        handler.sendEmptyMessage(HANDLER_WHAT_REFRESH_VIEW);
                        break;
                    case ACTION_PHONE_STATE_ACTIVITY_OPEN:
                        phoneStateFloatView.setActivityOpen(true);
                        break;
                    case ACTION_PHONE_STATE_ACTIVITY_CLOSE:
                        phoneStateFloatView.setActivityOpen(false);
                        break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
        phoneStateFloatView.unregisterBatteryBroadcast();
        phoneStateFloatView.close();
        phoneStateFloatView = null;
        unregisterReceiver(receiver);

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class RefreshFloatWindowTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_WHAT_REFRESH_MSG);
        }
    }

    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti;
        if (mActivityManager != null) {
            rti = mActivityManager.getRunningTasks(1);
            return getHomes().contains(rti.get(0).topActivity.getPackageName());
        } else {
            return false;
        }
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }
}
