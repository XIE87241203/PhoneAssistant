package com.assistant.xie.model.base;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.assistant.xie.R;

import static android.view.KeyEvent.KEYCODE_BACK;

public class BaseWebViewActivity extends BaseActivity {
    private BaseWebView webview;
    private ProgressBar pb_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web_view);
        webview = findViewById(R.id.webview);
        pb_webview = findViewById(R.id.pb_webview);
        Log.i("BaseWebView", "load->" + getIntent().getStringExtra("url"));
        webview.loadUrl(getIntent().getStringExtra("url"));
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    pb_webview.setVisibility(View.GONE);
                }else{
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

