package com.mirror.wifi;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cdl.wifi.db.DBWifiEntity;
import com.cdl.wifi.db.DBWifiUtil;
import com.cdl.wifi.thread.AutoLineWifiThread;
import com.cdl.wifi.thread.NoWifiThread;
import com.cdl.wifi.util.MyLog;
import com.cdl.wifi.util.WifiAdminUtils;
import com.mirror.receive.NetBroadCastReciver;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AutoLineUtil {
    private static final String TAG = "ServiceWifi";
    Context context;
    WifiAdminUtils wifiAdminUtils;

    Timer timer;
    TimerTask task;


    public AutoLineUtil(Context context) {
        this.context = context;
        wifiAdminUtils = new WifiAdminUtils(context);
    }

    /***
     * 去连接wifi
     */
    DBWifiUtil dbWifiUtil;
    List<ScanResult> wifiLists = new ArrayList<ScanResult>();
    List<DBWifiEntity> cacheList = new ArrayList<DBWifiEntity>(); //用户缓存共同的wifi信息

    /***
     * 1:获取当前wifiList
     * 2:获取数据库保存的wifi相关的信息
     * 3:便利去匹配,拿到共同的list
     * 4：便利相同的 ，去连接网络信号强的wifi
     */

    public void lineWifi() {
        cacheList.clear();
        Log.i(TAG, "===000开启服务。去联网");
        boolean isOpen = wifiAdminUtils.openWifi();
        Log.i(TAG, "===wifi打開狀態===" + isOpen);
        destroyTimer();
        timer = new Timer(true);
        task = new MyTask();
        timer.schedule(task, 0, 200);
    }


    public void checkWifiState() {
        if (NetBroadCastReciver.WIFI_STATE == NetBroadCastReciver.WIFI_STATE_OPENED) {  //网络已经连接成功
            Log.i(TAG, "===开始扫描===wifiList");
            wifiAdminUtils.startScan();  //打开wifi并且连接wifi
            destroyTimer();

            timer = new Timer(true);
            getWifiTask = new GetWifiTask();
            timer.schedule(getWifiTask, 0, 1000);
        }
    }

    private static final int CHECK_WIFI_STATE = 8907;
    public static final int GET_WIFI_LIST = 8250;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_WIFI_LIST:
                    wifiLists = wifiAdminUtils.getWifiList();
                    if (wifiLists.size() == 0) {
                        wifiAdminUtils.startScan();
                    } else {
                        destroyTimer();
                        Log.i(TAG, "===111扫描网络的个数===" + wifiLists.size());
                        mathList();
                    }
                    break;
                case CHECK_WIFI_STATE:
                    checkWifiState();
                    break;
            }
        }
    };

    TimerTask getWifiTask;

    class GetWifiTask extends TimerTask {
        @Override
        public void run() {
            MyLog.i(TAG, "=====去获取wifi列表======");
            handler.sendEmptyMessage(GET_WIFI_LIST);
        }
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            MyLog.i(TAG, "=====去检测网络状态======");
            handler.sendEmptyMessage(CHECK_WIFI_STATE);
        }
    }

    /***
     * 去兩張表单中匹配相同的数据
     */
    public void mathList() {
        try {
            if (dbWifiUtil == null) {
                dbWifiUtil = new DBWifiUtil(context);
            }
            List<DBWifiEntity> saveList = dbWifiUtil.queryDbInfo();  //保存的wifi相关信息
            if (saveList.size() == 0) {
                Log.i(TAG, "===222当前没有保存的wifi相关的信息，需要手动去联网");
                return;
            }
            Log.i(TAG, "===333数据库保存的相关的信息个数===" + saveList.size());
            for (int i = 0; i < wifiLists.size(); i++) {  // 遍历 wifiList
                for (int j = 0; j < saveList.size(); j++) {
                    String wifiName = wifiLists.get(i).SSID;
                    String saveWifiName = saveList.get(j).getWifiname();
                    if (wifiName.contains(saveWifiName)) {
                        Log.i(TAG, "====================444 same__wifiName===" + wifiName);
                        cacheList.add(saveList.get(j));
                    }
                }
            }
            choiceLineWifi();
        } catch (Exception e) {
            Log.i(TAG, "===777自动连接wifi异常了===" + e.toString());
        }
    }

    /***
     * 从缓存的wifi列表中选择第一个去连接wifi
     */
    public void choiceLineWifi() {
        //如果有保存记录，并且当前wifi列表里面也有当前wifi数据。就去连接
        if (cacheList.size() > 0) {
            String wifiName = cacheList.get(0).getWifiname();
            String password = cacheList.get(0).getWifipsd();
            String wifiType = cacheList.get(0).getWifiType();
            if (wifiType == null || wifiType.length() < 2) {  //连接不要密码的wifi
                MyLog.i(TAG, "===555去连接不要wifi密码的wifi");
                Thread noWifiThread = new NoWifiThread(context, wifiName);
                noWifiThread.start();
            } else {
                MyLog.i(TAG, "===666去连接需要wifi密码的wifi");
                Thread thread = new AutoLineWifiThread(context, wifiName, password, wifiType);
                thread.start();
            }
        }
    }


    public void destroyTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
        if (getWifiTask != null) {
            getWifiTask.cancel();
        }
    }
}
