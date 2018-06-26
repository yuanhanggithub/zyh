package com.cdl.wifi.thread;

import android.content.Context;
import android.util.Log;

import com.cdl.wifi.util.WifiAdmin;

/**
 * 自动连接没有密码的wifi
 */

public class NoWifiThread extends Thread {

    String wifiName;
    Context context;
    WifiAdmin mWifiAdmin;

    public NoWifiThread(Context context, String wifiName) {
        this.context = context;
        this.wifiName = wifiName;
    }

    @Override
    public void run() {
        super.run();
        try {
            if (mWifiAdmin == null) {
                mWifiAdmin = new WifiAdmin(context);
            }
            boolean iswifi = mWifiAdmin.connectNoWifi(wifiName);
            Thread.sleep(1000);
            if (iswifi) {
                Log.i("ServiceWifi", "自动连接不要wifi密码的，网络连接成功");
            } else {
                Log.i("ServiceWifi", "自动连接不要wifi密码的，网络连接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}