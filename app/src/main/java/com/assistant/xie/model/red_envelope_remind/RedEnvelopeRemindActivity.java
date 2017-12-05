package com.assistant.xie.model.red_envelope_remind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import com.assistant.xie.R;

public class RedEnvelopeRemindActivity extends AppCompatActivity {
    private Switch sw_auto_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_grab_red_envelope);
        initView();
        //打开系统设置中辅助功能
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "找到抢红包，然后开启服务即可", Toast.LENGTH_LONG).show();
    }

    private void initView() {
        sw_auto_click = findViewById(R.id.sw_auto_click);

    }
}
