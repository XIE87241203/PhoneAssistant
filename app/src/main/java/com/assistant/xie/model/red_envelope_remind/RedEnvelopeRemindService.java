package com.assistant.xie.model.red_envelope_remind;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.assistant.xie.R;
import com.assistant.xie.Utils.ScreenListener;
import com.assistant.xie.Utils.SharePreferenceUtils;
import com.assistant.xie.service.FloatWindowService;

import java.util.List;

/**
 * Created by XIE on 2017/12/5.
 * 抢红包服务
 */

public class RedEnvelopeRemindService extends AccessibilityService {
    private boolean isScreenOff = false;//判断是否处于锁屏状态
    private boolean isAutoClick = false;//判断是否自动进入会话
    private ScreenListener screenListener;
    private SoundPool soundPool;
    private int red_envelope_id;//音频id
    private RedEnvelopeRemindReceiver remindReceiver;
    private boolean isReminding = false;//打开红包中标记，防止重复打开


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        //指定声音池的最大音频流数目为10，声音品质为5
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        //载入音频流，返回在池中的id
        red_envelope_id = soundPool.load(this, R.raw.red_envelope, 0);
        screenListener = new ScreenListener(getApplicationContext());
        screenListener.register(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenOff() {
                isScreenOff = true;
            }

            @Override
            public void onUserPresent() {
                isScreenOff = false;
            }
        });
        //注册广播接收器
        remindReceiver = new RedEnvelopeRemindReceiver();
        registerReceiver(remindReceiver, new IntentFilter("com.assistant.xie.RED_ENVELOPE_REMIND_REFRESH_SETTING"));
        loadSetting();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            //第一步：监听通知栏消息
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (content.contains("[微信红包]") && !isReminding) {
                            if (isAutoClick) {
                                //模拟打开通知栏消息
                                if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                                    Notification notification = (Notification) event.getParcelableData();
                                    PendingIntent pendingIntent = notification.contentIntent;
                                    try {
                                        pendingIntent.send();
                                        isReminding = true;
                                    } catch (PendingIntent.CanceledException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (isScreenOff || !isAutoClick) {
                                //锁屏和不点击情况下播放声音
                                soundPool.play(red_envelope_id, 1, 1, 0, 0, 1);
                            }
                        }
                    }
                }
                break;
            //第二步：监听是否进入微信红包消息界面
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                String className = event.getClassName().toString();
                if (className.equals("com.tencent.mm.ui.LauncherUI") && isReminding) {
                    //打开聊天页面
                    getPacket();
                    isReminding = false;
                }
//                else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
//                    //开始打开红包
//                    openPacket();
//                }
                break;
        }

    }

    private void getPacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        recycle(rootNode);
    }

    /**
     * 打印一个节点的结构
     *
     * @param info info
     */
    public boolean recycle(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            if (info.getText() != null) {
                if ("领取红包".equals(info.getText().toString())) {
                    //这里有一个问题需要注意，就是需要找到一个可以点击的View
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    AccessibilityNodeInfo parent = info.getParent();
                    while (parent != null) {
                        //点击领取红包
                        if (parent.isClickable()) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            return true;
                        }
                        parent = parent.getParent();
                    }

                }
            }
            return false;
        } else {
            //循环获取领取红包的View
            for (int i = info.getChildCount() - 1; i >= 0; i--) {
                if (info.getChild(i) != null && recycle(info.getChild(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 查找到
     */
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo
                    .findAccessibilityNodeInfosByText("抢红包");
            for (AccessibilityNodeInfo n : list) {
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

    }

    public class RedEnvelopeRemindReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadSetting();
        }
    }

    private void loadSetting() {
        isAutoClick = SharePreferenceUtils.loadBooleanData(getApplicationContext(), SharePreferenceUtils.SAVE_NAME_RED_ENVELOPE_REMIND, RedEnvelopeRemindStaticConstants.SAVE_KEY_1_AUTO_CLICK, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        screenListener.unregister();
        unregisterReceiver(remindReceiver);
    }

    @Override
    public void onInterrupt() {

    }

}
