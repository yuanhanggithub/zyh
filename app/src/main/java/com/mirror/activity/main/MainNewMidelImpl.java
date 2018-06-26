package com.mirror.activity.main;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mirror.MirrorApplication;
import com.mirror.config.AppInfo;
import com.mirror.entity.TopPopEntity;
import com.mirror.http.GetHttpRequestRunnable;
import com.mirror.http.RequestBackListener;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;
import com.mirror.util.parsener.GsonParse;
import com.mirror.view.ad.ADInfo;
import com.mirror.view.ad.PopviewEntity;
import com.mirror.view.ad.Pos1Bean;
import com.mirror.view.ad.Pos4Entity;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainNewMidelImpl implements MainNewModel {

    ExecutorService executor = Executors.newFixedThreadPool(10);


    @Override
    public void getBottomAdImages(Context context, final MainNewModelListener listener) {
        Log.e("gif", "======准备获取底部菜单广告===========");
        if (!NetWorkUtils.isNetworkConnected(context)) {
            listener.getAdBottomState(false);
            return;
        }
        String token = SharedPerManager.getToken();
        if (token.length() < 5) {
            listener.getAdBottomState(false);
            return;
        }
        String requestUrl = AppInfo.GET_BOTTOM_URL + token;
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(requestUrl, 10, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                Log.e("gif", "======获取底部菜单成功==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int state = jsonObject.getInt("state");
                    if (state == 1) {
                        String date = jsonObject.getString("data");
                        Gson gson = new Gson();
                        PopviewEntity entity = gson.fromJson(date, PopviewEntity.class);
                        List<Pos4Entity> pos_4 = entity.getPos_4();
                        MirrorApplication.getInstance().setPos_4(pos_4);
                        listener.getAdBottomState(true);
                    }
                } catch (Exception e) {
                    listener.getAdBottomState(false);
                    Log.e("gif", "======获取底部菜单error==" + e.toString());
                }
            }

            @Override
            public void requestFailed(String errorDesc) {
                listener.getAdBottomState(true);
            }
        });
        executor.execute(runnable);
    }


    @Override
    public void getAdRightImages(Context context, final MainNewModelListener listener) {
        if (!NetWorkUtils.isNetworkConnected(context)) {// 没有网络直接获取默认得广告图片
            listener.getAdRightState(false);
            return;
        }
        String token = SharedPerManager.getToken();
        if (token.length() < 5) {
            listener.getAdRightState(false);
            return;
        }
        String requestUrl = AppInfo.GET_RIGHT_AD_IMAGE + token;
        Runnable runnable = new GetHttpRequestRunnable(requestUrl, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    int state = object.getInt("state");
                    if (state == 1) {
                        ADInfo adInfo = GsonParse.parseADInfo(json);
                        List<Pos1Bean> listPostBean = adInfo.getData().getPos_1();
                        MirrorApplication.getInstance().setInfos(listPostBean);
                        listener.getAdRightState(true);
                    } else {
                        listener.getAdRightState(false);
                    }
                } catch (Exception paramAnonymousString) {
                    listener.getAdRightState(false);
                }
            }

            @Override
            public void requestFailed(String errorDesc) {
                listener.getAdRightState(false);
            }
        });
        executor.execute(runnable);
    }


    /***
     * 获取弹屏广告
     * @param context
     * @param listener
     */
    @Override
    public void getTopRightImages(Context context, final MainNewModelListener listener) {
        if (!NetWorkUtils.isNetworkConnected(context)) {
            listener.getAdTopState(false);
            return;
        }
        String token = SharedPerManager.getToken();
        if (token.length() < 5) {
            listener.getAdTopState(false);
            return;
        }
        String requestUrl = AppInfo.GET_POP_URL + token;
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(requestUrl, 10, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                Log.e("pop", "======获取弹屏广告成功==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int state = jsonObject.getInt("state");
                    if (state == 1) {
                        String data = jsonObject.getString("data");
                        TopPopEntity entity = new Gson().fromJson(data, TopPopEntity.class);
                        MirrorApplication.getInstance().setList_pop(entity.getPos_13());
                        listener.getAdTopState(true);
                    }
                } catch (Exception e) {
                    Log.e("gif", "======获取底部菜单error==" + e.toString());
                }
            }

            @Override
            public void requestFailed(String errorDesc) {
                listener.getAdTopState(false);
                Log.e("pop", "======获取弹屏广告error==" + errorDesc);
            }
        });
        executor.execute(runnable);
    }
}
