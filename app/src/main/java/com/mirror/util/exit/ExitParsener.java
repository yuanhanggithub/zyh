package com.mirror.util.exit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mirror.config.AppInfo;
import com.mirror.http.GetHttpRequestRunnable;
import com.mirror.http.RequestBackListener;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;

import org.json.JSONObject;

public class ExitParsener {
    Context context;
    ExitView exitView;

    public ExitParsener(ExitView exitView, Context context) {
        this.context = context;
        this.exitView = exitView;
    }

    public void autoLogintoWeb() {
        if (!NetWorkUtils.isNetworkConnected(this.context)) {
            exitView.showTaost("获取店铺信息,需要网络连接");
            return;
        }
        exitView.showWaitDialog(true);
        String userName = SharedPerManager.getUserName();
        String password = SharedPerManager.getPassword();
        if ((TextUtils.isEmpty(userName)) || (TextUtils.isEmpty(password))) {
            exitView.showTaost("账号信息错误，请联网，绑定设备，重启设备");
            return;
        }
        String loginUrl = AppInfo.getLoginUrl(userName, password);
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(loginUrl, 10, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                exitView.showWaitDialog(false);
                Log.i("login", "=====登陆请求成功===" + json);
                parserLoginJson(json);
            }

            @Override
            public void requestFailed(String errorDesc) {
                exitView.showWaitDialog(false);
                exitView.showTaost("网络请求失败:" + errorDesc);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void parserLoginJson(String json) {
        try {
            JSONObject localObject1 = new JSONObject(json);
            int state = localObject1.getInt("state");
            if (state == 1) {
                String data = localObject1.getString("data");
                JSONObject jeonData = new JSONObject(data);
                String token = jeonData.getString("token");
                String shopName = jeonData.getString("name");
                String username = jeonData.getString("username");
                String admin = jeonData.getString("admin");
                SharedPerManager.setToken(token);
                SharedPerManager.setLogin(true);
                exitView.loginSuccess(shopName, username, admin);
                Log.e("login", "===========登陆成功了");
            } else {
                exitView.showTaost("登录失败:" + json);
            }
        } catch (Exception e) {
            exitView.showTaost("登录解析异常:" + e.toString());
            Log.i("login", "======解析异常 = " + e.toString());
        }
    }

}
