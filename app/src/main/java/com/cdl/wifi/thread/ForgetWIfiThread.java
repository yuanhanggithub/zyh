package com.cdl.wifi.thread;

import android.net.wifi.ScanResult;
import android.os.Handler;

import com.cdl.wifi.activity.WifiFragmentActivity;
import com.cdl.wifi.listener.WifiDealListener;
import com.cdl.wifi.util.WifiAdminUtils;

public class ForgetWIfiThread implements Runnable {
    ScanResult scanResult;
    WifiAdminUtils mWifiAdmin;
    WifiDealListener listener;
    private Handler handler = new Handler();

    public ForgetWIfiThread(WifiAdminUtils mWifiAdmin, ScanResult scanResult, WifiDealListener listener) {
        this.mWifiAdmin = mWifiAdmin;
        this.scanResult = scanResult;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            int netId = mWifiAdmin.getConnNetId();
            boolean isDisConnec = mWifiAdmin.disConnectionWifi(netId);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isDisConnec) {
                baclLineState(true, "操作成功");
            } else {
                baclLineState(true, "操作失败");
            }
        } catch (Exception e) {
            baclLineState(false, e.toString());
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