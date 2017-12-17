package com.assistant.xie.model.phone_state;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assistant.xie.R;
import com.assistant.xie.Utils.DisplayUtil;
import com.assistant.xie.Utils.FloatingManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by XIE on 2017/11/27.
 * 手机状态悬浮窗
 */

public class PhoneStateFloatView extends LinearLayout implements View.OnClickListener {
    private TextView tv_msg;
    private FloatingManager mWindowManager;
    private boolean isOpen = false;
    private boolean isHide = false;
    private List<String> switchList;
    private WindowManager.LayoutParams params;
    private boolean isActivityOpen = true;//判断手机状态页面是否开启

    public PhoneStateFloatView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_phone_state, this);
        mWindowManager = FloatingManager.getInstance(context);
        tv_msg = findViewById(R.id.tv_msg);
        switchList = new ArrayList<>();
        setOnClickListener(this);
        refreshView();
        //注册电池广播
        PhoneStateUtils.getInstance().registerBatteryBroadcast(getContext());
    }

    public void unregisterBatteryBroadcast() {
        //反注册电池广播
        PhoneStateUtils.getInstance().unregisterBatteryBroadcast(getContext());
    }

    public void refreshView() {
        //从缓存中读取开关信息
        Map<String, Boolean> switchMap = PhoneStateUtils.getInstance().getSaveData(getContext());
        //获取打开的tag
        List<String> saveTag = PhoneStateUtils.getInstance().getSaveTagList();
        switchList.clear();
        for (String tag : saveTag) {
            if (switchMap.get(tag)) {
                switchList.add(tag);
            }
        }
        if (isHide) isHide = false;
        if (switchList.isEmpty()) {
            close();
        } else {
            open();
        }
        refreshMsg();
    }

    /**
     * 刷新状态信息
     */
    public void refreshMsg() {
        String upload_speed = null;
        String download_speed = null;
        String ram_static = null;
        String battery_static = null;
        String battery_capacity = null;
        String battery_voltage = null;
        String battery_temperature = null;
        String rom_state = null;
        String sdcard_rom_state = null;
        if (isActivityOpen) {
            upload_speed = PhoneStateUtils.getInstance().getTxNetSpeed(getContext(), FloatWindowService.REFRESH_TIME);
            download_speed = PhoneStateUtils.getInstance().getRxNetSpeed(getContext(), FloatWindowService.REFRESH_TIME);
            ram_static = PhoneStateUtils.getInstance().getAvailMemory(getContext()) + "/" + PhoneStateUtils.getInstance().getTotalMemory(getContext());
            battery_static = PhoneStateUtils.getInstance().getBatteryStatus();
            battery_capacity = PhoneStateUtils.getInstance().getBattery();
            battery_voltage = PhoneStateUtils.getInstance().getBatteryV();
            battery_temperature = PhoneStateUtils.getInstance().getBatteryT();
            rom_state = PhoneStateUtils.getInstance().getROMUsageStatus(getContext());
            sdcard_rom_state = PhoneStateUtils.getInstance().getSDUsageStatus(getContext());
            //发送广播更新activity
            Intent counterIntent = new Intent();
            counterIntent.putExtra("upload_speed", upload_speed);
            counterIntent.putExtra("download_speed", download_speed);
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
        if (isAttachedToWindow()) {
            //隐藏时保留框大小
            if (!switchList.isEmpty() && isOpen && !isHide) {
                StringBuilder msg = new StringBuilder();
                for (String tag : switchList) {
                    switch (tag) {
                        case PhoneStateStaticConstants.SAVE_KEY_2_UPLOAD_SPEED:
                            if (upload_speed == null) {
                                upload_speed = PhoneStateUtils.getInstance().getTxNetSpeed(getContext(), FloatWindowService.REFRESH_TIME);
                            }
                            msg.append(String.format(getResources().getString(R.string.phone_state_float_upload_speed), upload_speed));
                            break;
                        case PhoneStateStaticConstants.SAVE_KEY_3_DOWNLOAD_SPEED:
                            if (download_speed == null) {
                                download_speed = PhoneStateUtils.getInstance().getRxNetSpeed(getContext(), FloatWindowService.REFRESH_TIME);
                            }
                            msg.append(String.format(getResources().getString(R.string.phone_state_float_download_speed), download_speed));
                            break;
                        case PhoneStateStaticConstants.SAVE_KEY_1_RAM_STATIC:
                            if (ram_static == null) {
                                ram_static = PhoneStateUtils.getInstance().getAvailMemory(getContext()) + "/" + PhoneStateUtils.getInstance().getTotalMemory(getContext());
                            }
                            msg.append(String.format(getResources().getString(R.string.phone_state_float_ram_static), ram_static));
                            break;
                        case PhoneStateStaticConstants.SAVE_KEY_4_BATTERY_STATIC:
                            if (battery_static == null) {
                                battery_static = PhoneStateUtils.getInstance().getBatteryStatus();
                            }
                            msg.append(String.format(getResources().getString(R.string.phone_state_float_battery_static), battery_static));
                            break;
                        case PhoneStateStaticConstants.SAVE_KEY_5_BATTERY_CAPACITY:
                            if (battery_capacity == null) {
                                battery_capacity = PhoneStateUtils.getInstance().getBattery();
                            }
                            msg.append(String.format(getResources().getString(R.string.phone_state_float_battery_capacity), battery_capacity));
                            break;
                        case PhoneStateStaticConstants.SAVE_KEY_6_BATTERY_VOLTAGE:
                            if (battery_voltage == null) {
                                battery_voltage = PhoneStateUtils.getInstance().getBatteryV();
                            }
                            msg.append(String.format(getResources().getString(R.string.phone_state_float_battery_voltage), battery_voltage));
                            break;
                        case PhoneStateStaticConstants.SAVE_KEY_7_BATTERY_TEMPERATURE:
                            if (battery_temperature == null) {
                                battery_temperature = PhoneStateUtils.getInstance().getBatteryT();
                            }
                            msg.append(String.format(getResources().getString(R.string.phone_state_float_battery_temperature), battery_temperature));
                            break;
                        case PhoneStateStaticConstants.SAVE_KEY_8_ROM_STATE:
                            if (rom_state == null) {
                                rom_state = PhoneStateUtils.getInstance().getROMUsageStatus(getContext());
                            }
                            msg.append(String.format(getResources().getString(R.string.phone_state_float_rom_state), rom_state));
                            break;
                        case PhoneStateStaticConstants.SAVE_KEY_9_SDCARD_ROM_STATE:
                            if (sdcard_rom_state == null) {
                                sdcard_rom_state = PhoneStateUtils.getInstance().getSDUsageStatus(getContext());
                            }
                            msg.append(String.format(getResources().getString(R.string.phone_state_float_sdcard_rom_state), sdcard_rom_state));
                            break;
                    }
                    msg.append("\n");
                }
                msg.delete(msg.length() - 1, msg.length());
                tv_msg.setText(msg.toString());
            }
            if (isHide) {
                tv_msg.setText("显示");
            }
        }
    }

    private void open() {
        if (isOpen) return;
        params = new WindowManager.LayoutParams();
        params.gravity = Gravity.TOP | Gravity.START;
        params.y = DisplayUtil.getInstance().getStarusBarHeight(getContext());
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
        isOpen = true;
    }

    public void close() {
        if (!isOpen) return;
        tv_msg.setText("");
        mWindowManager.removeView(this);
        isOpen = false;
    }

    @Override
    public void onClick(View v) {
        isHide = !isHide;
        refreshMsg();
        //刷新悬浮窗贴边位置
        moveEnd();
    }

    /**
     * 移动悬浮窗
     *
     * @param offsetX offsetX
     * @param offsetY offsetY
     */
    private void move(float offsetX, float offsetY) {
        int[] location = new int[2];
        getLocationOnScreen(location);
        int y = (int) (location[1] + offsetY);
        if (y < DisplayUtil.getInstance().getStarusBarHeight(getContext())) {
            y = DisplayUtil.getInstance().getStarusBarHeight(getContext());
        }
        params.x = (int) (location[0] + offsetX);
        params.y = y;
        mWindowManager.updateView(this, params);
    }

    /**
     * 移动悬浮窗结束自动贴边
     */
    private void moveEnd() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        int screenWidth = DisplayUtil.getInstance().getScreenWidth(getContext());
        if (location[0] + getWidth() <= screenWidth / 2) {
            params.x = 0;
        } else {
            params.x = screenWidth;
        }
        mWindowManager.updateView(this, params);
    }

    private float lastTouchX, lastTouchY;
    private boolean isDrag = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX, touchY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getRawX();
                lastTouchY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                touchX = event.getRawX();
                touchY = event.getRawY();
                float offsetX = touchX - lastTouchX;
                float offsetY = touchY - lastTouchY;
                if (!isDrag && (Math.abs(offsetX) >= DisplayUtil.getInstance().dip2px(getContext(), 3) || Math.abs(offsetY) >= DisplayUtil.getInstance().dip2px(getContext(), 3))) {
                    //是拖动事件
                    isDrag = true;
                }
                if (isDrag) {
                    move(offsetX, offsetY);
                    lastTouchX = touchX;
                    lastTouchY = touchY;
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    moveEnd();
                    isDrag = false;
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
        }
        return super.onTouchEvent(event);
    }

    public void setActivityOpen(boolean activityOpen) {
        isActivityOpen = activityOpen;
    }
}
