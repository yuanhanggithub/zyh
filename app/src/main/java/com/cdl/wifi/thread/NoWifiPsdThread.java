package com.cdl.wifi.thread;

import android.net.wifi.ScanResult;
import android.os.Handler;

import com.cdl.wifi.listener.WifiDealListener;
import com.cdl.wifi.util.WifiAdminUtils;


public class NoWifiPsdThread extends Thread {
    ScanResult scanResult;
    WifiAdminUtils mWifiAdmin;
    WifiDealListener listener;

    public NoWifiPsdThread(ScanResult scanResult, WifiAdminUtils mWifiAdmin, WifiDealListener listener) {
        this.scanResult = scanResult;
        this.mWifiAdmin = mWifiAdmin;
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        boolean bool = false;
        if (mWifiAdmin.isConnect(scanResult)) {
            listener.dealWifiSuccess();
        } else {
            bool = mWifiAdmin.connectSpecificAP(this.scanResult);
        }
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
        }
        if (bool) {
            baclLineState(true, "连接成功");
        } else {
            baclLineState(false, "连接失败");
        }
    }


    private Handler handler = new Handler();

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

