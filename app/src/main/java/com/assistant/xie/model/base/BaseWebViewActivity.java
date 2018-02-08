package com.assistant.xie.model.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.assistant.xie.R;
import com.assistant.xie.Utils.LogUtils;

import static android.view.KeyEvent.KEYCODE_BACK;

public class BaseWebViewActivity extends BaseActivity {
    private BaseWebView webview;
    private ProgressBar pb_webview;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web_view);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        webview = findViewById(R.id.webview);
        pb_webview = findViewById(R.id.pb_webview);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        //取代原本的actionbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LogUtils.getInstance().v(BaseWebViewActivity.class.getSimpleName(),"url-->" + url);
        webview.loadUrl(url);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pb_webview.setVisibility(View.GONE);
                } else {
                    if (pb_webview.getVisibility() == View.GONE)
                        pb_webview.setVisibility(View.VISIBLE);
                    pb_webview.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

