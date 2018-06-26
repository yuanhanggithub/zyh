package com.mirror.fragment.parsener;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mirror.MirrorApplication;
import com.mirror.config.AppInfo;
import com.mirror.entity.VideoEntity;
import com.mirror.fragment.view.FationTowordView;
import com.mirror.http.GetHttpRequestRunnable;
import com.mirror.http.RequestBackListener;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class FationTowordParsener {
    Context context;
    FationTowordView fationTowordView;

    public FationTowordParsener(Context paramContext, FationTowordView paramFationTowordView) {
        this.context = paramContext;
        this.fationTowordView = paramFationTowordView;
    }

    public void jujleListContent() {
        if (!NetWorkUtils.isNetworkConnected(context)) {
            fationTowordView.showToast("网络未连接");
            return;
        }
        String token = SharedPerManager.getToken();
        if ((!SharedPerManager.isLogin()) || (TextUtils.isEmpty(token))) {
            fationTowordView.showToast("设备未登陆，请联网重启设备");
            return;
        }
        if (token.length() < 5) {
            return;
        }
        fationTowordView.showWaitDialog(true);
        String requestUrl = AppInfo.getFationUrl(token);
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(requestUrl, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                fationTowordView.showWaitDialog(false);
                parsenJsonHair(json);
            }

            @Override
            public void requestFailed(String errorDesc) {
                fationTowordView.showWaitDialog(false);
                fationTowordView.showToast(errorDesc);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static final String TAG = "FATHION";

    private void parsenJsonHair(String json) {
        ArrayList localArrayList = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int state = jsonObject.getInt("state");
            if (state == 1) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectSon = (JSONObject) jsonArray.opt(i);
                    int id = jsonObjectSon.getInt("id");
                    int subchannel_id = jsonObjectSon.getInt("subchannel_id");
                    String title = jsonObjectSon.getString("title");
                    String description = jsonObjectSon.getString("description");
                    String videopath = jsonObjectSon.getString("videopath");
                    String picpath = jsonObjectSon.getString("picpath");
                    int time = jsonObjectSon.getInt("time");
                    int viewcount = jsonObjectSon.getInt("viewcount");
                    String createtime = jsonObjectSon.getString("createtime");
                    VideoEntity entity = new VideoEntity(id, subchannel_id, title, description, videopath, picpath, time, viewcount, createtime);
                    localArrayList.add(entity);
                    Log.e(TAG, "= 解析效果 = " + entity.toString());
                    MirrorApplication.getInstance().setList_hair(localArrayList);
                    fationTowordView.queryVideoInfos(localArrayList);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "= 解析error = " + e.toString());
            fationTowordView.showToast("获取潮流解析异常: " + e.toString());
        }
    }

}
