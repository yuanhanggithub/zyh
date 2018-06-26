package com.cdl.wifi.listener;

import android.net.wifi.ScanResult;

/**
 * Created by reeman on 2017/5/9.
 */

public interface WifiStateListener {

    /***
     * 不需要密碼連接
     */
    void wifiConnection(ScanResult scanResult);

    /***
     * 账号密码连接
     * @param scanResult
     * @param password
     */
    void wifiConnection(ScanResult scanResult, String password);

    /***
     * 网络監聽回掉
     * @param wifiName
     * @param password
     */
    void wifiConnection(String wifiName, String password, String wifiDesc);

    /***
     * 忘记密码
     * @param scanResult
     */
    void forget(ScanResult scanResult);

    /**
     * 斷開制定的wifi
     */
    void disConnection(ScanResult scanResult);

    /***
     * 断开连接
     * @param wifiName
     */
    void disConnection(String wifiName);

    /***
     * 連接隱藏的wifi
     * @param wifiName
     * @param password
     * @param type
     */
    void connHiddleWifi(String wifiName, String password, String type);


}
