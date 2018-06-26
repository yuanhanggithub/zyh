package com.cdl.wifi.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cdl.wifi.activity.WifiFragmentActivity;
import com.cdl.wifi.listener.WifiDealListener;
import com.cdl.wifi.util.WifiAdminUtils;
import com.cdl.wifi.util.WifiConnectUtils;

public class HiddleWifiThread implements Runnable {
    String wifiName;
    String password;
    WifiConnectUtils.WifiCipherType type;
    Context context;
    WifiDealListener listener;
    private Handler handler = new Handler();


    public HiddleWifiThread(Context context, String wifiName, String password, WifiConnectUtils.WifiCipherType type, WifiDealListener listener) {
        this.context = context;
        this.wifiName = wifiName;
        this.password = password;
        this.type = type;
        this.listener = listener;
    }

    @Override
    public void run() {
        // 去连接网络
        WifiAdminUtils mWifiAdmin = new WifiAdminUtils(context);
        boolean isConnect = mWifiAdmin.connect(wifiName, password, type);
        Log.d("WifiListActivity", isConnect + "是否去连接的值");
        if (isConnect) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            baclLineState(true, "操作成功");
        } else {
            baclLineState(false, "WIFI连接失败");
        }
    }

    public void baclLineState(final boolean isTrue, final String errorDesc) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isTrue) {
                    listener.dealWifiSuccess();
                } else {
                    listener.dealWifiFailed(errorDesc);
                }
            }
        });
    }

}