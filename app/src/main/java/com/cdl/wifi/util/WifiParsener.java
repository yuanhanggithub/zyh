package com.cdl.wifi.util;

import android.content.Context;
import android.net.wifi.ScanResult;

import com.cdl.wifi.db.DBWifiEntity;
import com.cdl.wifi.db.DBWifiUtil;
import com.cdl.wifi.dialog.WifiConNoPassDialog;
import com.cdl.wifi.dialog.WifiConnectionDialog;
import com.cdl.wifi.dialog.WifiHiddleDialog;
import com.cdl.wifi.dialog.WifiStateAlertDialog;
import com.cdl.wifi.listener.WifChangeListener;
import com.cdl.wifi.listener.WifiDealListener;
import com.cdl.wifi.thread.ForgetWIfiThread;
import com.cdl.wifi.thread.HiddleWifiThread;
import com.cdl.wifi.thread.LineWifiThread;
import com.cdl.wifi.thread.NoWifiPsdThread;
import com.cdl.wifi.view.WifiView;

public class WifiParsener {

    Context context;
    WifiView wifiView;
    DBWifiUtil dbWifiUtil;
    WifiConNoPassDialog wifiConNoPassDialog;
    WifiConnectionDialog wifiConnectionDialog;
    WifiHiddleDialog wifiHiddleDialog;
    WifiStateAlertDialog wifiStateAlertDialog;
    WifiAdminUtils wifiAdminUtils;

    public WifiParsener(Context context, WifiView wifiView, WifiAdminUtils wifiAdminUtils) {
        this.context = context;
        this.wifiView = wifiView;
        this.wifiAdminUtils = wifiAdminUtils;
        dbWifiUtil = new DBWifiUtil(context);
    }


    /***
     * 查看已经连接WIFI的状态DIALOG
     * @param scanResult
     */
    public void showSatetWifiDialog(ScanResult scanResult) {
        if (wifiStateAlertDialog == null) {
            wifiStateAlertDialog = new WifiStateAlertDialog(context);
        }
        wifiStateAlertDialog.show(scanResult);
        wifiStateAlertDialog.setOnWifiSateChangeListener(new WifChangeListener() {
            @Override
            public void lineHiddleWifi(String wifiName, String password, WifiConnectUtils.WifiCipherType type) {

            }

            @Override
            public void lineWifi(String wifiName, String password, ScanResult scanResult) {

            }

            @Override
            public void lineNopasswordWifi(ScanResult scanResult) {

            }

            @Override
            public void forget(ScanResult scanResult) {
                forgetWfiInfo(scanResult);
            }
        });
    }

    /***
     * 显示隐藏WIFI  dialog
     */
    public void showHiddleDialog() {
        if (wifiHiddleDialog == null) {
            wifiHiddleDialog = new WifiHiddleDialog(context);
        }
        wifiHiddleDialog.show();
        wifiHiddleDialog.setOnWifiSateChangeListener(new WifChangeListener() {
            @Override
            public void lineHiddleWifi(String wifiName, String password, WifiConnectUtils.WifiCipherType type) {
                lineHiddleWifi(wifiName, password, type);
            }

            @Override
            public void lineWifi(String wifiName, String password, ScanResult scanResult) {

            }

            @Override
            public void lineNopasswordWifi(ScanResult scanResult) {

            }

            @Override
            public void forget(ScanResult scanResult) {

            }
        });
    }


    /***
     * 显示需要输入账号i密码的wifi_dialog
     * @param scanResult
     */
    public void showConnectDialog(ScanResult scanResult) {
        if (wifiConnectionDialog == null) {
            wifiConnectionDialog = new WifiConnectionDialog(context);
        }
        wifiConnectionDialog.show(scanResult);
        wifiConnectionDialog.setOnWifiSateChangeListener(new WifChangeListener() {
            @Override
            public void lineHiddleWifi(String wifiName, String password, WifiConnectUtils.WifiCipherType type) {

            }

            @Override
            public void lineWifi(String wifiName, String password, ScanResult scanResult) {
                lineUsePasWifi(wifiName, password, scanResult);
            }

            @Override
            public void lineNopasswordWifi(ScanResult scanResult) {

            }

            @Override
            public void forget(ScanResult scanResult) {

            }
        });
    }

    /***
     * 连接正常wifi
     * @param wifiName
     * @param password
     * @param scanResult
     */
    private void lineUsePasWifi(String wifiName, String password, ScanResult scanResult) {
//        wifiView.showWaitDialig(true);
//        Thread thread = new LineWifiThread(context, wifiName, password, scanResult, new WifiDealListener() {
//            @Override
//            public void dealWifiSuccess() {
//                wifiView.showWaitDialig(false);
//                wifiView.dealWifiState(true);
//            }
//
//            @Override
//            public void dealWifiFailed(String wifiErrordesc) {
//                wifiView.showWaitDialig(false);
//                wifiView.dealWifiState(false);
//            }
//        });
//        thread.start();
    }


    /***
     * 显示不需要密码的WIFI
     */
    public void showNoWifiPasswordDialog() {
        if (wifiConNoPassDialog == null) {
            wifiConNoPassDialog = new WifiConNoPassDialog(context);
        }
        wifiConNoPassDialog.setOnWifiSateChangeListener(new WifChangeListener() {
            @Override
            public void lineHiddleWifi(String wifiName, String password, WifiConnectUtils.WifiCipherType type) {
                lineHiddleWifi(wifiName, password, type);
            }

            @Override
            public void lineWifi(String wifiName, String password, ScanResult scanResult) {

            }

            @Override
            public void lineNopasswordWifi(ScanResult scanResult) {

            }

            @Override
            public void forget(ScanResult scanResult) {
                forgetWfiInfo(scanResult);
            }
        });

    }

    /***
     * 忘记账号
     * @param paramScanResult
     */
    public void forgetWfiInfo(ScanResult paramScanResult) {
        wifiView.showWaitDialig(true);
        try {
            dbWifiUtil.deleteByName(paramScanResult.SSID);
            Runnable runnable = new ForgetWIfiThread(wifiAdminUtils, paramScanResult, new WifiDealListener() {
                @Override
                public void dealWifiSuccess() {
                    wifiView.showWaitDialig(false);
                    wifiView.showToast("忘记密码成功");
                    wifiView.dealWifiState(true);
                }

                @Override
                public void dealWifiFailed(String wifiErrordesc) {
                    wifiView.showWaitDialig(false);
                    wifiView.showToast("忘记密码失败：" + wifiErrordesc);
                    wifiView.dealWifiState(false);
                }
            });
            Thread thread = new Thread(runnable);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void lineHiddleWifi(String wifiName, String password, WifiConnectUtils.WifiCipherType type) {
//        wifiView.showWaitDialig(true);
//        Thread thread = new HiddleWifiThread(context, wifiName, password, type, new WifiDealListener() {
//            @Override
//            public void dealWifiSuccess() {
//                wifiView.showWaitDialig(false);
//                wifiView.dealWifiState(true);
//            }
//
//            @Override
//            public void dealWifiFailed(String wifiErrordesc) {
//                wifiView.showWaitDialig(false);
//                wifiView.showToast(wifiErrordesc);
//            }
//        });
//        thread.start();
    }


    /***
     * 连接不需要wifi密码的
     * @param paramScanResult
     * @param mWifiAdmin
     */
    public void lineNoPadWifi(ScanResult paramScanResult, WifiAdminUtils mWifiAdmin) {
        wifiView.showWaitDialig(true);
        dbWifiUtil.addWifiInfo(new DBWifiEntity(paramScanResult.SSID, "", ""));
        Thread thread = new NoWifiPsdThread(paramScanResult, mWifiAdmin, new WifiDealListener() {
            @Override
            public void dealWifiSuccess() {
                wifiView.showWaitDialig(false);
                wifiView.dealWifiState(true);
            }

            @Override
            public void dealWifiFailed(String wifiErrordesc) {
                wifiView.showWaitDialig(false);
                wifiView.dealWifiState(false);
            }
        });

    }


}
