package com.mirror.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.mirror.config.AppInfo;


public class NetBroadCastReciver extends BroadcastReceiver {
    public static final String APK_ADD = "com.reeman.app.AppConfig.APK_ADD";
    public static final String APK_REMOVE = "com.reeman.app.AppConfig.APK_REMOVE";
    public static final String WIFI_STATE_CHANGE = "android.net.wifi.WIFI_STATE_CHANGED";
    public static final String TAG = "NetBroadCastReciver";
    public static final int WIFI_STATE_LINEING = 1;
    public static final int WIFI_STATE_OPENED = 2;
    public static final int WIFI_STATE_CLOSEED = 3;
    public static final int WIFI_STATE_CLOSING = 4;
    public static final int WIFI_STATE_UNKNOW = 5;
    public static int WIFI_STATE = 5;
    public static boolean isConnection = true;

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WIFI_STATE_CHANGE)) {
            int mWifiState = intent.getIntExtra("wifi_state", 0);
            switch (mWifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    //已打开
                    WIFI_STATE = WIFI_STATE_OPENED;
                    Log.i(TAG, "wifi狀態————已经打开");
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    WIFI_STATE = WIFI_STATE_LINEING;
                    Log.i(TAG, "wifi狀態————打开中");
                    //打开中
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    WIFI_STATE = WIFI_STATE_CLOSEED;

                    //已关闭       Log.i(TAG,"wifi狀態————已經關閉");
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    //关闭中
                    WIFI_STATE = WIFI_STATE_CLOSING;
                    Log.i(TAG, "wifi狀態————关闭中");
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    WIFI_STATE = WIFI_STATE_UNKNOW;
                    Log.i(TAG, "wifi狀態————未知狀態");
                    break;
            }
        } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {  //网络发生变化
            dealNetChange(context, intent);
        } else if (action.equals(APK_ADD)) {
            Log.i(TAG, "===程序安装成功");
        } else if (action.equals(APK_REMOVE)) {
            Log.i(TAG, "===程序安装成功");
        }
    }

    /***
     * 处理网络变化
     * @param context
     * @param intent
     */
    private void dealNetChange(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (info == null) {
            Log.i(TAG, "====获取网络对象===null。检测不熬网络变化了");
            return;
        }
        if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                isConnection = true;
                Log.i(TAG, "===NetBroadCastReciver网络连接");
                context.sendBroadcast(new Intent(AppInfo.NET_ONLINE));
            }
        } else {
            isConnection = false;
            Log.i(TAG, "===NetBroadCastReciver网络断开");
            context.sendBroadcast(new Intent(AppInfo.NET_DISONLINE));
        }
    }
}


