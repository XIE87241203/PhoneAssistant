package com.assistant.xie.model.phone_state;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.xie.R;
import com.assistant.xie.service.FloatWindowService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PhoneStateActivity extends AppCompatActivity {
    private ListView listview;
    private boolean threadSwitch;
    private UIHandler uiHandler;
    private Switch sw_float_window, sw_ram_static, sw_battery_static, sw_battery_capacity, sw_battery_voltage,
            sw_battery_temperature, sw_rom_state, sw_sdcard_rom_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_state);
        requestSDCardPermission();
        initView();
        initData();
//        startFloatService();
    }

    private void initView() {
        listview = findViewById(R.id.listview);
        sw_float_window = findViewById(R.id.sw_float_window);
        sw_ram_static = findViewById(R.id.sw_ram_static);
        sw_battery_static = findViewById(R.id.sw_battery_static);
        sw_battery_capacity = findViewById(R.id.sw_battery_capacity);
        sw_battery_voltage = findViewById(R.id.sw_battery_voltage);
        sw_battery_temperature = findViewById(R.id.sw_battery_temperature);
        sw_rom_state = findViewById(R.id.sw_rom_state);
        sw_sdcard_rom_state = findViewById(R.id.sw_sdcard_rom_state);
    }

    private void initData() {
        //注册电池广播监听
        PhoneStateUtils.getInstance().registerBatteryBroadcast(this);
        uiHandler = new UIHandler(this);
        threadSwitch = true;
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (threadSwitch) {
                    //刷新数据
                    uiHandler.sendEmptyMessage(0);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        threadSwitch = false;
                    }
                }
            }
        };
        thread.start();
    }

    /**
     * 刷新列表数据
     */
    public void refreshData() {
        sw_ram_static.setText(String.format(getResources().getString(R.string.phone_state_ram_static), PhoneStateUtils.getInstance().getAvailMemory(this), PhoneStateUtils.getInstance().getTotalMemory(this)));
        sw_battery_static.setText(String.format(getResources().getString(R.string.phone_state_battery_static),PhoneStateUtils.getInstance().getBatteryStatus()));
        sw_battery_capacity.setText(String.format(getResources().getString(R.string.phone_state_battery_capacity),PhoneStateUtils.getInstance().getBattery()));
        sw_battery_voltage.setText(String.format(getResources().getString(R.string.phone_state_battery_voltage),PhoneStateUtils.getInstance().getBatteryV()));
        sw_battery_temperature.setText(String.format(getResources().getString(R.string.phone_state_battery_temperature),PhoneStateUtils.getInstance().getBatteryT()));
        sw_rom_state.setText(String.format(getResources().getString(R.string.phone_state_rom_state),PhoneStateUtils.getInstance().getROMUsageStatus(this)));
        sw_sdcard_rom_state.setText(String.format(getResources().getString(R.string.phone_state_sdcard_rom_state),PhoneStateUtils.getInstance().getSDUsageStatus(this)));
    }

    private void startFloatService() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent settingIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(settingIntent);
            } else {
                Intent intent = new Intent(this, FloatWindowService.class);
                startService(intent);
            }
        } else {
            Intent intent = new Intent(this, FloatWindowService.class);
            startService(intent);
        }
    }

    /**
     * 请求授权
     */
    private void requestSDCardPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            PhoneStateUtils.getInstance().setSDCardHasPermission(true);
        }
    }

    /**
     * 权限申请返回结果
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 申请结果数组，里面都是int类型的数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                PhoneStateUtils.getInstance().setSDCardHasPermission(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        threadSwitch = false;
        PhoneStateUtils.getInstance().unregisterBatteryBroadcast(this);
        super.onDestroy();
    }

    /**
     * 处理UI更新的Handler
     */
    static class UIHandler extends Handler {
        WeakReference<PhoneStateActivity> mActivity;

        UIHandler(PhoneStateActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PhoneStateActivity theActivity = mActivity.get();
            switch (msg.what) {
                case 0:
                    theActivity.refreshData();
                    break;
            }
        }
    }
}
