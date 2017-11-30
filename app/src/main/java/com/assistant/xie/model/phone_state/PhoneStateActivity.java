package com.assistant.xie.model.phone_state;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.assistant.xie.R;
import com.assistant.xie.Utils.CommonMethods;
import com.assistant.xie.Utils.SharePreferenceUtils;
import com.assistant.xie.service.FloatWindowService;

import java.util.HashMap;
import java.util.Map;

public class PhoneStateActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private Switch sw_float_window, sw_upload_speed, sw_download_speed, sw_ram_static, sw_battery_static, sw_battery_capacity, sw_battery_voltage,
            sw_battery_temperature, sw_rom_state, sw_sdcard_rom_state;
    private RefreshPhoneStateReceiver receiver;
    private static final String REQUEST_DATA_ING = "获取中...";
    private Map<String, Switch> switchMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_state);
        requestSDCardPermission();
        initView();
        initData();
        initListener();
        //注册广播接收器
        receiver = new RefreshPhoneStateReceiver();
        registerReceiver(receiver, new IntentFilter("com.assistant.xie.REFRESH_PHONE_STATE"));
    }

    private void initView() {
        sw_float_window = findViewById(R.id.sw_float_window);
        sw_upload_speed = findViewById(R.id.sw_upload_speed);
        sw_download_speed = findViewById(R.id.sw_download_speed);
        sw_ram_static = findViewById(R.id.sw_ram_static);
        sw_battery_static = findViewById(R.id.sw_battery_static);
        sw_battery_capacity = findViewById(R.id.sw_battery_capacity);
        sw_battery_voltage = findViewById(R.id.sw_battery_voltage);
        sw_battery_temperature = findViewById(R.id.sw_battery_temperature);
        sw_rom_state = findViewById(R.id.sw_rom_state);
        sw_sdcard_rom_state = findViewById(R.id.sw_sdcard_rom_state);

        switchMap = new HashMap<>();
        switchMap.put(PhoneStateStaticConstants.SAVE_KEY_2_UPLOAD_SPEED, sw_upload_speed);
        switchMap.put(PhoneStateStaticConstants.SAVE_KEY_3_DOWNLOAD_SPEED, sw_download_speed);
        switchMap.put(PhoneStateStaticConstants.SAVE_KEY_1_RAM_STATIC, sw_ram_static);
        switchMap.put(PhoneStateStaticConstants.SAVE_KEY_4_BATTERY_STATIC, sw_battery_static);
        switchMap.put(PhoneStateStaticConstants.SAVE_KEY_5_BATTERY_CAPACITY, sw_battery_capacity);
        switchMap.put(PhoneStateStaticConstants.SAVE_KEY_6_BATTERY_VOLTAGE, sw_battery_voltage);
        switchMap.put(PhoneStateStaticConstants.SAVE_KEY_7_BATTERY_TEMPERATURE, sw_battery_temperature);
        switchMap.put(PhoneStateStaticConstants.SAVE_KEY_8_ROM_STATE, sw_rom_state);
        switchMap.put(PhoneStateStaticConstants.SAVE_KEY_9_SDCARD_ROM_STATE, sw_sdcard_rom_state);

    }

    private void initData() {
        //注册电池广播监听
        PhoneStateUtils.getInstance().registerBatteryBroadcast(this);
        //读取缓存数据
        Map<String, Boolean> switchData = PhoneStateUtils.getInstance().getSaveData(this);
        for (Map.Entry<String, Boolean> entry : switchData.entrySet()) {
            if (entry.getValue()) {
                switchMap.get(entry.getKey()).setChecked(true);
            }
        }
        //全选
        ViewGroup parent = (ViewGroup) sw_float_window.getParent();
        sw_float_window.setChecked(true);
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i) instanceof Switch && !((Switch) parent.getChildAt(i)).isChecked()) {
                sw_float_window.setChecked(false);
                break;
            }
        }
    }

    private void initListener(){
        sw_float_window.setOnCheckedChangeListener(this);
        //设置tag
        for (Map.Entry<String, Switch> entry : switchMap.entrySet()) {
            entry.getValue().setTag(entry.getKey());
            entry.getValue().setOnCheckedChangeListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开启悬浮窗服务
        startFloatService();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //如果没有权限的话，设置为未开启
        //全选
        ViewGroup parent = (ViewGroup) sw_float_window.getParent();
        switch (buttonView.getId()) {
            case R.id.sw_float_window:
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (parent.getChildAt(i) instanceof Switch && parent.getChildAt(i).getId() != R.id.sw_float_window) {
                        ((Switch) parent.getChildAt(i)).setChecked(isChecked);
                    }
                }
                break;
            default:
                SharePreferenceUtils.saveBooleanData(this, SharePreferenceUtils.SAVE_NAME_PHONE_STATE, (String) buttonView.getTag(), isChecked);
                if(!isChecked) {
                    //关闭全选
                    sw_float_window.setOnCheckedChangeListener(null);
                    sw_float_window.setChecked(false);
                    sw_float_window.setOnCheckedChangeListener(this);
                }else{
                    //判断开启全选
                    //全选
                    boolean isAllTrue = true;
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        if (parent.getChildAt(i) instanceof Switch && parent.getChildAt(i).getId() != R.id.sw_float_window) {
                            if(!((Switch) parent.getChildAt(i)).isChecked()){
                                isAllTrue = false;
                                break;
                            }
                        }
                    }
                    if(isAllTrue){
                        sw_float_window.setOnCheckedChangeListener(null);
                        sw_float_window.setChecked(true);
                        sw_float_window.setOnCheckedChangeListener(this);
                    }
                }
                break;
        }

        //发送广播更新悬浮窗
        Intent counterIntent = new Intent();
        counterIntent.setAction("com.assistant.xie.REFRESH_FLOAT_VIEW");
        sendBroadcast(counterIntent);
    }

    public class RefreshPhoneStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshData(intent);
        }
    }

    private void refreshData(Intent intent) {
        if (intent != null) {
            sw_upload_speed.setText(String.format(getResources().getString(R.string.phone_state_upload_speed), intent.getStringExtra("upload_speed")));
            sw_download_speed.setText(String.format(getResources().getString(R.string.phone_state_download_speed), intent.getStringExtra("download_speed")));
            sw_ram_static.setText(String.format(getResources().getString(R.string.phone_state_ram_static), intent.getStringExtra("ram_static")));
            sw_battery_static.setText(String.format(getResources().getString(R.string.phone_state_battery_static), intent.getStringExtra("battery_static")));
            sw_battery_capacity.setText(String.format(getResources().getString(R.string.phone_state_battery_capacity), intent.getStringExtra("battery_capacity")));
            sw_battery_voltage.setText(String.format(getResources().getString(R.string.phone_state_battery_voltage), intent.getStringExtra("battery_voltage")));
            sw_battery_temperature.setText(String.format(getResources().getString(R.string.phone_state_battery_temperature), intent.getStringExtra("battery_temperature")));
            sw_rom_state.setText(String.format(getResources().getString(R.string.phone_state_rom_state), intent.getStringExtra("rom_state")));
            sw_sdcard_rom_state.setText(String.format(getResources().getString(R.string.phone_state_sdcard_rom_state), intent.getStringExtra("sdcard_rom_state")));
        } else {
            sw_upload_speed.setText(String.format(getResources().getString(R.string.phone_state_upload_speed), REQUEST_DATA_ING));
            sw_download_speed.setText(String.format(getResources().getString(R.string.phone_state_download_speed), REQUEST_DATA_ING));
            sw_ram_static.setText(String.format(getResources().getString(R.string.phone_state_ram_static), REQUEST_DATA_ING));
            sw_battery_static.setText(String.format(getResources().getString(R.string.phone_state_battery_static), REQUEST_DATA_ING));
            sw_battery_capacity.setText(String.format(getResources().getString(R.string.phone_state_battery_capacity), REQUEST_DATA_ING));
            sw_battery_voltage.setText(String.format(getResources().getString(R.string.phone_state_battery_voltage), REQUEST_DATA_ING));
            sw_battery_temperature.setText(String.format(getResources().getString(R.string.phone_state_battery_temperature), REQUEST_DATA_ING));
            sw_rom_state.setText(String.format(getResources().getString(R.string.phone_state_rom_state), REQUEST_DATA_ING));
            sw_sdcard_rom_state.setText(String.format(getResources().getString(R.string.phone_state_sdcard_rom_state), REQUEST_DATA_ING));
        }

    }

    /**
     * 开启悬浮窗服务
     */
    private void startFloatService() {
        //判断是否已经开启
        if (CommonMethods.isServiceWork(this, FloatWindowService.class.getName())) return;
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showRequestFloatPermissionDialog();
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
     * 获取悬浮窗权限弹窗
     */
    private void showRequestFloatPermissionDialog() {
        //显示提示
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("提示");
        alertDialogBuilder.setMessage("请允许'" + getString(R.string.app_name) + "'显示悬浮窗");
        alertDialogBuilder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                @SuppressLint("InlinedApi")
                Intent settingIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(settingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        //设置对话框是不可取消的
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();//将dialog显示出来
    }

    /**
     * 请求sd卡授权
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
