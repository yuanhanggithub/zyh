package com.cdl.wifi.listener;

/**
 * wifi操作接口
 */

public interface WifiDealListener {

    void dealWifiSuccess();

    void dealWifiFailed(String wifiErrordesc);
}
