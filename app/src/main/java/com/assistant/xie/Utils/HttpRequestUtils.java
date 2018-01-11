package com.assistant.xie.Utils;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XIE on 2017/12/28.
 */

public class HttpRequestUtils {
    private static HttpRequestUtils httpRequestUtils;
    private OkHttpClient mOkHttpClient;
    public final static int TYPE_REQUEST_GET = 1;
    public final static int TYPE_REQUEST_POST = 2;
    private Handler handler;

    private HttpRequestUtils() {
        okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder();
        ClientBuilder.readTimeout(30, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(8, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(60, TimeUnit.SECONDS);//写入超时
        mOkHttpClient = ClientBuilder.build();
        handler = new Handler();
    }

    public synchronized static HttpRequestUtils getInstance() {
        if (httpRequestUtils == null) {
            httpRequestUtils = new HttpRequestUtils();
        }
        return httpRequestUtils;
    }

    /**
     * 请求URL
     *
     * @param url                 url
     * @param params              参数，没有传null
     * @param requestType         请求方式
     * @param httpWhat            httpWhat
     * @param httpRequestCallback 回调
     * @see #TYPE_REQUEST_GET
     * @see #TYPE_REQUEST_POST
     */
    public void request(@NonNull String url, int requestType, Map<String, String> params, final int httpWhat, final HttpRequestCallback httpRequestCallback) {
        Request.Builder builder = new Request.Builder();
        StringBuilder requestBody = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                requestBody.append(entry.getKey()).append("=");
                requestBody.append(entry.getValue()).append("&");
            }
            requestBody.delete(requestBody.length() - 1, requestBody.length());
        }
        Log.i("HttpRequest", "url-->" + url);
        switch (requestType) {
            default:
            case TYPE_REQUEST_GET:
                StringBuilder urlBuilder = new StringBuilder(url);
                //在url输入参数
                if (requestBody.length() != 0) {
                    urlBuilder.append("?");
                    urlBuilder.append(requestBody.toString());
                    url = urlBuilder.toString();
                }
                builder = builder.get();
                break;
            case TYPE_REQUEST_POST:
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                if (params != null) {
                    //输入参数
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        formBodyBuilder.add(entry.getKey(), entry.getValue());
                    }
                }
                builder = builder.post(formBodyBuilder.build());
                if (requestBody.length() != 0) {
                    Log.i("HttpRequest", "requestBody-->" + requestBody.toString());
                }
                break;
        }
        builder = builder.url(url);
        Call mcall = mOkHttpClient.newCall(builder.build());
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpRequestCallback.onFailure(e, httpWhat);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseStr = getResponseBodyString(response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpRequestCallback.onResponse(responseStr, httpWhat);
                    }
                });
            }
        });
    }

    public interface HttpRequestCallback {

        void onResponse(@Nullable String responseStr, int httpWhat);

        void onFailure(IOException e, int httpWhat);
    }

    /**
     * 获取接口返回的信息
     *
     * @param response response
     * @return 接口返回信息。若接口无返回或者解析错误则返回null。
     */
    public @Nullable
    String getResponseBodyString(Response response) {
        String result = null;
        if (response.body() != null) {
            try {
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
