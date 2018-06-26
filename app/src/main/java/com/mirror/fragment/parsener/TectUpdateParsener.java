package com.mirror.fragment.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mirror.MirrorApplication;
import com.mirror.config.AppInfo;
import com.mirror.entity.TectUpdateEntity;
import com.mirror.fragment.view.TectUpdateView;
import com.mirror.http.GetHttpRequestRunnable;
import com.mirror.http.RequestBackListener;
import com.mirror.util.NetWorkUtils;

import org.json.JSONObject;

import java.util.List;

public class TectUpdateParsener {
    private static final String TAG = "TectUpdateParsener";
    Context context;
    TectUpdateView tectUpdateView;

    public TectUpdateParsener(Context paramContext, TectUpdateView paramTectUpdateView) {
        context = paramContext;
        tectUpdateView = paramTectUpdateView;
    }

    public void getTectVideoInfo() {
        if (!NetWorkUtils.isNetworkConnected(context)) {
            tectUpdateView.showToast("网络不可用，请检查");
            return;
        }
        tectUpdateView.showWaitDialog(true);
        String requestUrl = AppInfo.TECH_UPDATE_URL;
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(requestUrl, 10, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                Log.e("TectUpdateParsener", "======json=" + json);
                tectUpdateView.showWaitDialog(false);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int state = jsonObject.getInt("state");
                    if (state == 1) {
                        String data = jsonObject.getString("data");
                        data = "{ \"data\":" + data + "}";
                        TectUpdateEntity entity = new Gson().fromJson(data, TectUpdateEntity.class);
                        List<TectUpdateEntity.TectDetailEntity> textEntitys = entity.getData();
                        MirrorApplication.getInstance().setList_tect_update(textEntitys);
                        tectUpdateView.backTectRequestBack(textEntitys);
                    }
                } catch (Exception e) {
                    TectUpdateParsener.this.tectUpdateView.showToast(e.toString());
                }
            }

            @Override
            public void requestFailed(String errorDesc) {
                tectUpdateView.showWaitDialog(false);
                tectUpdateView.showToast("请求失败:" + tectUpdateView);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
