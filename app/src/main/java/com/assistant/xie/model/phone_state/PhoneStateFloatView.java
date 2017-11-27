package com.assistant.xie.model.phone_state;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assistant.xie.R;
import com.assistant.xie.Utils.FloatingManager;

import java.util.zip.Inflater;

/**
 * Created by XIE on 2017/11/27.
 * 手机状态悬浮窗
 */

public class PhoneStateFloatView extends LinearLayout {
    private TextView tv_msg;
    private FloatingManager mWindowManager;

    public PhoneStateFloatView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_phone_state, this);
        mWindowManager = FloatingManager.getInstance(context);
        tv_msg = findViewById(R.id.tv_msg);
    }

    /**
     * 刷新状态信息
     */
    public void refreshMsg() {
        String msg = "";
        msg += "总内存:" + PhoneStateUtils.getInstance().getTotalMemory(getContext());
        msg += "可用内存:" + PhoneStateUtils.getInstance().getAvailMemory(getContext());
        tv_msg.setText(msg);
    }

    public void show() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;
        //总是在窗口之上
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        mWindowManager.addView(this, params);
    }

    public void hide() {
        mWindowManager.removeView(this);
    }
}
