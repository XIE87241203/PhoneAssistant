package com.assistant.xie.model.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.assistant.xie.R;
import com.assistant.xie.model.news.NewsActivity;
import com.assistant.xie.model.red_envelope_remind.RedEnvelopeRemindActivity;
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
        initView();
        initData();
        initListener();
    }

    private void initView() {
        listView = findViewById(R.id.listview);
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new MainActivityInfo("手机状态", "查看手机下载上传速度、内存状态和电池状态等信息", PhoneStateActivity.class));
        data.add(new MainActivityInfo("红包提醒", "微信红包提醒", RedEnvelopeRemindActivity.class));
        data.add(new MainActivityInfo("新闻热点", "看看新闻都有啥？谁看到了就给他", NewsActivity.class));
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
