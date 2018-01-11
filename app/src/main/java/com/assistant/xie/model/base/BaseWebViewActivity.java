package com.assistant.xie.model.base;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.assistant.xie.R;

import static android.view.KeyEvent.KEYCODE_BACK;

public class BaseWebViewActivity extends BaseActivity {
    private BaseWebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web_view);
        ActionBar mActionBar=getSupportActionBar();
        if(mActionBar!=null){
            mActionBar.setTitle(getIntent().getStringExtra("title"));
        }
        webview = findViewById(R.id.webview);
        webview.loadUrl(getIntent().getStringExtra("url"));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

