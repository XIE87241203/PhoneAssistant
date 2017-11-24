package com.assistant.xie.model.phone_state;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.xie.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PhoneStateActivity extends AppCompatActivity {
    private ListView listview;
    private List<PhoneStateInfo> data;
    private boolean threadSwitch;
    private PhoneStateAdapter adapter;
    private UIHandler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_state);
        requestSDCardPermission();
        initView();
        initData();
    }

    private void initView() {
        listview = findViewById(R.id.listview);
    }

    private void initData() {
        //注册电池广播监听
        PhoneStateUtils.getInstance().registerBatteryBroadcast(this);

        data = new ArrayList<>();
        data.add(new PhoneStateInfo("总内存", PhoneStateUtils.getInstance().getTotalMemory(getApplicationContext())));
        data.add(new PhoneStateInfo("可用内存"));
        data.add(new PhoneStateInfo("电池状态"));
        data.add(new PhoneStateInfo("剩余电量"));
        data.add(new PhoneStateInfo("当前电压"));
        data.add(new PhoneStateInfo("电池温度"));
        data.add(new PhoneStateInfo("手机存储状态"));
        data.add(new PhoneStateInfo("内存卡存储状态"));
        adapter = new PhoneStateAdapter(this, data, R.layout.item_phone_state);
        listview.setAdapter(adapter);
        uiHandler = new UIHandler(this);
        threadSwitch = true;
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (threadSwitch) {
                    data.get(1).setValue(PhoneStateUtils.getInstance().getAvailMemory(getApplicationContext()));
                    data.get(2).setValue(PhoneStateUtils.getInstance().getBatteryStatus());
                    data.get(3).setValue(PhoneStateUtils.getInstance().getBattery());
                    data.get(4).setValue(PhoneStateUtils.getInstance().getBatteryV());
                    data.get(5).setValue(PhoneStateUtils.getInstance().getBatteryT());
                    data.get(6).setValue(PhoneStateUtils.getInstance().getROMUsageStatus(PhoneStateActivity.this));
                    data.get(7).setValue(PhoneStateUtils.getInstance().getSDUsageStatus(PhoneStateActivity.this));
                    //刷新listview
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
     * 请求授权
     */
    private void requestSDCardPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
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
                    theActivity.adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
