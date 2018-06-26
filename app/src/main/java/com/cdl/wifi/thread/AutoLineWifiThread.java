package com.cdl.wifi.thread;

import android.content.Context;
import android.util.Log;

import com.cdl.wifi.util.WifiAdminUtils;
import com.cdl.wifi.util.WifiConnectUtils;

public class AutoLineWifiThread extends Thread {

    String wifiName;
    String password;
    Context context;
    String wifiType;

    public AutoLineWifiThread(Context context, String wifiName, String password, String wifiType) {
        Log.d("ServiceWifi", "=====准备去连接wifi==" + wifiName + "/" + password + "/" + wifiType);
        this.context = context;
        this.wifiName = wifiName;
        this.password = password;
        this.wifiType = wifiType;
    }

    @Override
    public void run() {
        super.run();
        try {
            WifiConnectUtils.WifiCipherType type = null;
            if (wifiType.contains("WPA")) {
                type = WifiConnectUtils.WifiCipherType.WIFICIPHER_WPA;
            } else if (wifiType.contains("WEP")) {
                type = WifiConnectUtils.WifiCipherType.WIFICIPHER_WEP;
            } else if (wifiType.contains("WEP") && wifiType.contains("WPA")) {
                type = WifiConnectUtils.WifiCipherType.WIFICIPHER_WPA;
            } else {
                type = WifiConnectUtils.WifiCipherType.WIFICIPHER_NOPASS;
            }
            // 去连接网络
            WifiAdminUtils mWifiAdmin = new WifiAdminUtils(context);
            boolean isConnect = mWifiAdmin.connect(wifiName, password, type);
            Log.d("ServiceWifi", "====判斷是否去聯網了 ===" + isConnect + "是否去连接的值");
            if (isConnect) {
                Log.d("ServiceWifi", "去连接wifi了");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("ServiceWifi", "====网络自动连接成功");
            } else {
                Log.i("ServiceWifi", "====网络自动连接失败");
            }
        } catch (Exception e) {
            Log.i("ServiceWifi", "====网络自动连接异常" + e.toString());
        }
    }
}