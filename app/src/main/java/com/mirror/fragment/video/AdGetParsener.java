package com.mirror.fragment.video;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mirror.entity.ADVideoInfo;
import com.mirror.entity.CartonAdEntity;
import com.mirror.entity.DefaultAdVide;
import com.mirror.fragment.video.AdFirstView;
import com.mirror.http.GetHttpRequestRunnable;
import com.mirror.http.RequestBackListener;
import com.mirror.util.parsener.GsonParse;

import org.json.JSONObject;

public class AdGetParsener {
    private static final String TAG = "adv";
    AdFirstView adFirstView;
    Context context;

    public AdGetParsener(Context paramContext, AdFirstView paramAdFirstView) {
        this.context = paramContext;
        this.adFirstView = paramAdFirstView;
    }

    private void parsener(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int state = jsonObject.getInt("state");
            if (state == 1) {
                Log.e(TAG, "======获取广告成功=====" + json);
                ADVideoInfo adVideoInfo = GsonParse.parserVideoInfo(json);
                adFirstView.requestAdState(true, adVideoInfo, "获取数据成功");
            } else {
                adFirstView.requestAdState(false, null, json);
            }

        } catch (Exception e) {
            Log.e(TAG, "======解析广告异常=====" + e.toString());
            adFirstView.requestAdState(false, null, e.toString());
        }
    }

    private void personCartonJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String pos_11 = jsonObject.getString("pos_11");
            pos_11 = "{ pos_11 :" + pos_11 + "}";
            Gson gson = new Gson();
            CartonAdEntity cartonAdEntity = gson.fromJson(pos_11, CartonAdEntity.class);
            adFirstView.requestCartonAdState(false, cartonAdEntity, "请求成功");
            Log.e(TAG, "=======解析完成，保存到数据==" + cartonAdEntity.getPos_11().size());
        } catch (Exception e) {
            Log.e(TAG, "=====解析异常==" + e.toString());
            adFirstView.requestCartonAdState(false, null, e.toString());
        }
    }

    public void getAdFrirst(String requestUrl) {
        adFirstView.showWaitDialog(true);
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(requestUrl, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                Log.e(TAG, "=====获取片头广告success==" + json);
                adFirstView.showWaitDialog(false);
                parsener(json);
            }

            @Override
            public void requestFailed(String errorDesc) {
                Log.e(TAG, "=====获取片头广告failed==" + errorDesc);
                adFirstView.showWaitDialog(false);
                adFirstView.requestAdState(false, null, errorDesc);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void getCartonFiestAdRequest(String requestUrl) {
        Log.e(TAG, "=====获取卡通默===" + requestUrl);
        adFirstView.showWaitDialog(true);
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(requestUrl, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                adFirstView.showWaitDialog(false);
                Log.e(TAG, "=====获取卡通默认广告成功===" + json);
                try {
                    JSONObject localJSONObject = new JSONObject(json);
                    int state = localJSONObject.getInt("state");
                    if (state == 1) {
                        String data = localJSONObject.getString("data");
                        personCartonJson(data);
                    } else {
                        Log.e(TAG, "=====请求错误==" + json);
                        adFirstView.requestCartonAdState(false, null, json);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "=====解析异常==" + e.toString());
                    adFirstView.requestCartonAdState(false, null, e.toString());
                }
            }

            @Override
            public void requestFailed(String errorDesc) {
                adFirstView.showWaitDialog(false);
                adFirstView.requestCartonAdState(false, null, errorDesc);
                Log.e(TAG, "=====获取卡通默认广告失败==" + errorDesc);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void getDefaultAdInfo(String requestUrl) {
        adFirstView.showWaitDialog(true);
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(requestUrl, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                adFirstView.showWaitDialog(false);
                Log.e(TAG, "=====获取默认广告成功===" + json);
                try {
                    DefaultAdVide defaultAdVide = GsonParse.parserDefauleAd(json);
                    adFirstView.getDefaultAdState(true, defaultAdVide, "获取成功");
                } catch (Exception e) {
                    adFirstView.getDefaultAdState(false, null, e.toString());
                }
            }

            @Override
            public void requestFailed(String errorDesc) {
                adFirstView.showWaitDialog(false);
                Log.e(TAG, "=====获取默认广告失败==" + errorDesc);
                adFirstView.getDefaultAdState(false, null, errorDesc);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
