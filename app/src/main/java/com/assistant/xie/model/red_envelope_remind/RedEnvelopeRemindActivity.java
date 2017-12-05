package com.assistant.xie.model.red_envelope_remind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.assistant.xie.R;
import com.assistant.xie.Utils.CommonMethods;
import com.assistant.xie.Utils.SharePreferenceUtils;

public class RedEnvelopeRemindActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private Switch sw_auto_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_grab_red_envelope);
        requestPermission();
        initView();
        initData();
    }

    private void requestPermission() {
        if(!CommonMethods.isStartAccessibilityService(this,RedEnvelopeRemindService.class.getSimpleName())){
            //打开系统设置中辅助功能
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "找到" + getString(R.string.red_envelope_desc) + "，然后开启服务即可", Toast.LENGTH_LONG).show();
        }
    }

    private void initData() {
        //读取设置
        sw_auto_click.setChecked(SharePreferenceUtils.loadBooleanData(this, SharePreferenceUtils.SAVE_NAME_RED_ENVELOPE_REMIND, RedEnvelopeRemindStaticConstants.SAVE_KEY_1_AUTO_CLICK, false));
    }

    private void initView() {
        sw_auto_click = findViewById(R.id.sw_auto_click);
        sw_auto_click.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_auto_click:
                SharePreferenceUtils.saveBooleanData(this, SharePreferenceUtils.SAVE_NAME_RED_ENVELOPE_REMIND, RedEnvelopeRemindStaticConstants.SAVE_KEY_1_AUTO_CLICK, isChecked);
                //发送广播更新抢红包服务
                Intent counterIntent = new Intent();
                counterIntent.setAction("com.assistant.xie.RED_ENVELOPE_REMIND_REFRESH_SETTING");
                sendBroadcast(counterIntent);
                break;
        }
    }
}
