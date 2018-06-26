package com.mirror.http;

import android.os.Handler;
import android.util.Log;

import okhttp3.RequestBody;

public class PostRequestRunnable implements Runnable, RequeatListener {

    private static final String TAG = "http";
    String requestUrl;
    PostHttpRequest postHttpRequest;
    RequestBackListener listener;
    private Handler handler = new Handler();

    public PostRequestRunnable(String requestUrl, RequestBody requestBodyPost, RequestBackListener listener) {
        this.requestUrl = requestUrl;
        this.listener = listener;
        postHttpRequest = new PostHttpRequest(requestUrl, requestBodyPost);
    }

    @Override
    public void run() {
        postHttpRequest.querybindInfo(this);
    }

    @Override
    public void requestSuccess(final String json) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.requestSuccess(json);
            }
        });

    }

    @Override
    public void requestFailed(final String errorDesc) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.requestFailed(errorDesc);
                Log.e(TAG, "===获取房间详情=====" + errorDesc);
            }
        });
    }
}
