package com.cdl.wifi.listener;

import android.net.wifi.ScanResult;

import com.cdl.wifi.util.WifiConnectUtils;

public interface WifChangeListener {

    void lineHiddleWifi(String wifiName, String password, WifiConnectUtils.WifiCipherType type);

    /***
     * 去連接wifi
     * @param wifiName
     * @param password
     * @param scanResult
     */
    void lineWifi(String wifiName, String password, ScanResult scanResult);

    /***
     * 无密码廉价
     * @param scanResult
     */
    void lineNopasswordWifi(ScanResult scanResult);


    /***
     * 忘记密码
     * @param scanResult
     */
    void forget(ScanResult scanResult);
}
