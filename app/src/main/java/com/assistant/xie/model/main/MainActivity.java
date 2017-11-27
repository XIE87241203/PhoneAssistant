package com.assistant.xie.model.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.assistant.xie.R;
import com.assistant.xie.model.base.BaseActivity;
import com.assistant.xie.model.phone_state.PhoneStateActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private ListView listView;
    private List<MainActivityInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        listView = findViewById(R.id.listview);
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new MainActivityInfo("手机状态", "查看手机cpu使用，内存使用，温度等状态", PhoneStateActivity.class));
        listView.setAdapter(new MainActivityAdapter(this,data,R.layout.item_main));
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, data.get(position).getCls());
                startActivity(intent);
            }
        });
    }
}
