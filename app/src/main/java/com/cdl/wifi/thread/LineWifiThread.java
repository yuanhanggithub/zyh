package com.cdl.wifi.thread;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.util.Log;

import com.cdl.wifi.activity.WifiFragmentActivity;
import com.cdl.wifi.listener.WifiDealListener;
import com.cdl.wifi.util.WifiAdminUtils;
import com.cdl.wifi.util.WifiConnectUtils;

public class LineWifiThread implements Runnable {
    ScanResult scanResult;
    String wifiName;
    String password;
    Context context;
    WifiDealListener wifiDealListener;

    public LineWifiThread(Context context, String wifiName, String password, ScanResult scanResult, WifiDealListener wifiDealListener) {
        this.context = context;
        this.scanResult = scanResult;
        this.wifiName = wifiName;
        this.password = password;
        this.wifiDealListener = wifiDealListener;
    }

    @Override
    public void run() {
        try {
            WifiConnectUtils.WifiCipherType type = null;
            if (scanResult.capabilities.toUpperCase().contains("WPA")) {
                type = WifiConnectUtils.WifiCipherType.WIFICIPHER_WPA;
            } else if (scanResult.capabilities.toUpperCase()
                    .contains("WEP")) {
                type = WifiConnectUtils.WifiCipherType.WIFICIPHER_WEP;
            } else {
                type = WifiConnectUtils.WifiCipherType.WIFICIPHER_NOPASS;
            }
            // 去连接网络
            WifiAdminUtils mWifiAdmin = new WifiAdminUtils(context);
            boolean isConnect = mWifiAdmin.connect(wifiName, password, type);
            Log.d("WifiListActivity", "====判斷是否去聯網了 ===" + isConnect + "是否去连接的值");
            if (isConnect) {
                Log.d("WifiListActivity", "去连接wifi了");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                baclLineState(true, "操作成功");
            } else {
                baclLineState(false, "连接失败");
            }
        } catch (Exception e) {
            baclLineState(false, "连接失败:" + e.toString());
        }
    }

    private Handler handler = new Handler();

    public void baclLineState(final boolean isTrue, final String errorDesc) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isTrue) {
                    wifiDealListener.dealWifiSuccess();
                } else {
                    wifiDealListener.dealWifiFailed(errorDesc);
                }
            }
        });
    }

}