package com.assistant.xie.model.phone_state;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private Switch sw_float_window, sw_ram_static, sw_battery_static, sw_battery_capacity, sw_battery_voltage,
            sw_battery_temperature, sw_rom_state, sw_sdcard_rom_state;
    private RefreshPhoneStateReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_state);
        requestSDCardPermission();
        initView();
        initData();
        //注册广播接收器
        receiver = new RefreshPhoneStateReceiver();
        registerReceiver(receiver, new IntentFilter("com.assistant.xie.REFRESH_PHONE_STATE"));
    }

    private void initView() {
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
        startFloatService();
    }

    public class RefreshPhoneStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            sw_ram_static.setText(String.format(getResources().getString(R.string.phone_state_ram_static), intent.getStringExtra("ram_static")));
            sw_battery_static.setText(String.format(getResources().getString(R.string.phone_state_battery_static), intent.getStringExtra("battery_static")));
            sw_battery_capacity.setText(String.format(getResources().getString(R.string.phone_state_battery_capacity), intent.getStringExtra("battery_capacity")));
            sw_battery_voltage.setText(String.format(getResources().getString(R.string.phone_state_battery_voltage), intent.getStringExtra("battery_voltage")));
            sw_battery_temperature.setText(String.format(getResources().getString(R.string.phone_state_battery_temperature), intent.getStringExtra("battery_temperature")));
            sw_rom_state.setText(String.format(getResources().getString(R.string.phone_state_rom_state), intent.getStringExtra("rom_state")));
            sw_sdcard_rom_state.setText(String.format(getResources().getString(R.string.phone_state_sdcard_rom_state), intent.getStringExtra("sdcard_rom_state")));
        }
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
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
