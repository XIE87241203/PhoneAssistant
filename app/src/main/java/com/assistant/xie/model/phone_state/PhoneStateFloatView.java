package com.assistant.xie.model.phone_state;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assistant.xie.R;
import com.assistant.xie.Utils.DisplayUtil;
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //注册电池广播
        PhoneStateUtils.getInstance().registerBatteryBroadcast(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //反注册电池广播
        PhoneStateUtils.getInstance().unregisterBatteryBroadcast(getContext());
    }

    /**
     * 刷新状态信息
     */
    public void refreshMsg() {
        if (isAttachedToWindow()) {
            String ram_static = PhoneStateUtils.getInstance().getAvailMemory(getContext()) + "/" + PhoneStateUtils.getInstance().getTotalMemory(getContext());
            String battery_static = PhoneStateUtils.getInstance().getBatteryStatus();
            String battery_capacity = PhoneStateUtils.getInstance().getBattery();
            String battery_voltage = PhoneStateUtils.getInstance().getBatteryV();
            String battery_temperature = PhoneStateUtils.getInstance().getBatteryT();
            String rom_state = PhoneStateUtils.getInstance().getROMUsageStatus(getContext());
            String sdcard_rom_state = PhoneStateUtils.getInstance().getSDUsageStatus(getContext());
            String msg = "";
            msg += String.format(getResources().getString(R.string.phone_state_float_ram_static), ram_static) + "\n";
            msg += String.format(getResources().getString(R.string.phone_state_float_battery_static), battery_static) + "\n";
            msg += String.format(getResources().getString(R.string.phone_state_float_battery_capacity), battery_capacity) + "\n";
            msg += String.format(getResources().getString(R.string.phone_state_float_battery_voltage), battery_voltage) + "\n";
            msg += String.format(getResources().getString(R.string.phone_state_float_battery_temperature), battery_temperature) + "\n";
            msg += String.format(getResources().getString(R.string.phone_state_float_rom_state), rom_state) + "\n";
            msg += String.format(getResources().getString(R.string.phone_state_float_sdcard_rom_state), sdcard_rom_state);
            tv_msg.setText(msg);
            //发送广播更新activity
            Intent counterIntent = new Intent();
            counterIntent.putExtra("ram_static", ram_static);
            counterIntent.putExtra("battery_static", battery_static);
            counterIntent.putExtra("battery_capacity", battery_capacity);
            counterIntent.putExtra("battery_voltage", battery_voltage);
            counterIntent.putExtra("battery_temperature", battery_temperature);
            counterIntent.putExtra("rom_state", rom_state);
            counterIntent.putExtra("sdcard_rom_state", sdcard_rom_state);
            counterIntent.setAction("com.assistant.xie.REFRESH_PHONE_STATE");
            getContext().sendBroadcast(counterIntent);
        }
    }

    public void show() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.TOP | Gravity.START;
        params.y = DisplayUtil.getStarusBarHeight(getContext());
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
