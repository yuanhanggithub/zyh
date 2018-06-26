package com.cdl.wifi.view;

/**
 * Created by jsjm on 2018/5/9.
 */

public interface WifiView {
    void showToast(String toast);

    void showWaitDialig(boolean isShow);

    void dealWifiState(boolean isTrue);
}
