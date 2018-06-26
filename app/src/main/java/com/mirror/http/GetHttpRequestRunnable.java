package com.mirror.http;


import android.os.Handler;
import android.util.Log;

/***
 * get网络请求
 */
public class GetHttpRequestRunnable implements Runnable, RequeatListener {

    String requestUrl;
    MyOkHttpUtil myOkHttpUtil;
    final String TAG = "http";
    RequestBackListener listener;
    private Handler handler = new Handler();

    public GetHttpRequestRunnable(String requestUrl, RequestBackListener listener) {
//        Log.e(TAG, "请求的网址===" + requestUrl);
//        this.requestUrl = requestUrl;
//        this.listener = listener;
//        myOkHttpUtil = new MyOkHttpUtil(requestUrl);
        getRequestRunnable(requestUrl, 1, listener);
    }

    public GetHttpRequestRunnable(String requestUrl, int requestNum, RequestBackListener listener) {
        getRequestRunnable(requestUrl, requestNum, listener);
    }

    /***
     *
     * @param requestUrl
     * 请求的网址
     * @param requestNum
     * 请求失败，重复请求的次数
     * @param listener
     */
    public void getRequestRunnable(String requestUrl, int requestNum, RequestBackListener listener) {
        Log.e(TAG, "请求的网址===" + requestUrl);
        this.requestUrl = requestUrl;
        this.listener = listener;
        myOkHttpUtil = new MyOkHttpUtil(requestUrl, requestNum);
    }

    @Override
    public void run() {
        myOkHttpUtil.getRequestInfo(this);
    }

    @Override
    public void requestSuccess(final String json) {
        Log.e(TAG, "===修改密码返回的结果:" + json);
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
            }
        });
    }
}
