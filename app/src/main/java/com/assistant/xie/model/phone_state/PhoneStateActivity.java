package com.assistant.xie.model.phone_state;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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
        initView();
        initData();
    }

    private void initView() {
        listview = findViewById(R.id.listview);
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new PhoneStateInfo("总内存", PhoneStateUtils.getInstance().getTotalMemory(getApplicationContext())));
        data.add(new PhoneStateInfo("可用内存"));
        adapter = new PhoneStateAdapter(this, data ,R.layout.item_phone_state);
        listview.setAdapter(adapter);
        uiHandler = new UIHandler(this);
        threadSwitch = true;
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (threadSwitch) {
                    data.get(1).setValue(PhoneStateUtils.getInstance().getAvailMemory(getApplicationContext()));
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

    @Override
    protected void onDestroy() {
        threadSwitch = false;
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
