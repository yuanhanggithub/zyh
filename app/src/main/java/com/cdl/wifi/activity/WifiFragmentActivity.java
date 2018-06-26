package com.cdl.wifi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.cdl.wifi.adapter.WifiLinkRecyAdapter;
import com.cdl.wifi.adapter.WifiListRecycleAdapter;
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
import com.cdl.wifi.util.MyLog;
import com.cdl.wifi.util.WifiAdminUtils;
import com.cdl.wifi.util.WifiConnectUtils;
import com.cdl.wifi.view.WifiView;
import com.mirror.R;
import com.mirror.activity.BaseActivity;
import com.mirror.config.AppInfo;
import com.mirror.util.Biantai;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;
import com.mirror.view.recycle.SWRecyclerView;
import com.mirror.wifi.ObjectAdapterClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WifiFragmentActivity extends BaseActivity implements View.OnClickListener, WifiView, WifChangeListener, WifiDealListener {
    //已經連接的wifi
    public List<ScanResult> linkList = new ArrayList<ScanResult>();
    private static final int REFRESH_CONN = 100;
    // Wifi管理类
    private WifiAdminUtils mWifiAdmin;
    // 扫描结果列表
    private List<ScanResult> list = new ArrayList<ScanResult>();
    // 显示列表
    WifiLinkRecyAdapter wifiLinkAdapter;
    WifiListRecycleAdapter wifiListRecycleAdapter;
    private WifiReceiver mReceiver;
    DBWifiUtil dbWifiUtil;
    WaitDialogUtil waitDialogUtil;
    ScanResult currentChooiceScanResult;
    private Button btn_answer;

    @Override
    public void dealWifiSuccess() {
        showWaitDialig(false);
        getWifiListInfo();
    }

    @Override
    public void dealWifiFailed(String wifiErrordesc) {
        showWaitDialig(false);
        getWifiListInfo();
        showToast(wifiErrordesc);
    }

    private class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {// wifi發生改變
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                MyLog.e("wifi", "=========wifi状态===" + info.getState());
                MyLog.e("wifi", "=========wifi action===" + action);
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    //網絡斷開
                    wifiLinkAdapter.notifyDataSetChanged();
                } else if (info.getState().equals(NetworkInfo.State.CONNECTING)) {
                    //正在连接
                    wifiLinkAdapter.notifyDataSetChanged();
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    //连接成功
                    wifiLinkAdapter.notifyDataSetChanged();
                }
            } else if (action.equals(AppInfo.NET_ONLINE)) {
                wifiLinkAdapter.notifyDataSetChanged();
            } else if (action.equals(AppInfo.NET_DISONLINE)) {
                wifiLinkAdapter.notifyDataSetChanged();
            } else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                //密碼驗證失敗，這裡會彈窗
                int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
                switch (error) {
                    case WifiManager.ERROR_AUTHENTICATING:
                        Log.d(TAG, "密码认证错误Code为：" + error);
                        MyToastView.getInstance().Toast(WifiFragmentActivity.this, "wifi密码认证错误！");
                        break;
                    default:
                        break;
                }
            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                getWifiListInfo();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_wifi_list);
        initView();
        getWifiListInfo();
        setListener();
        registerReceiver();
        getWifiListOnTime();
    }

    Button refresh_list_btn, btn_add_wifi;
    WifiManager mWifiManager;
    ConnectivityManager cm;
    SWRecyclerView recycle_link_alread;
    SWRecyclerView listView;

    private void initView() {
        dbWifiUtil = new DBWifiUtil(WifiFragmentActivity.this);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiAdmin = new WifiAdminUtils(WifiFragmentActivity.this);
        //=======================================================
        btn_add_wifi = (Button) findViewById(R.id.btn_add_wifi);
        btn_add_wifi.setOnClickListener(this);
        refresh_list_btn = (Button) findViewById(R.id.refresh_list_btn);
        refresh_list_btn.setOnClickListener(this);
        btn_answer = (Button) findViewById(R.id.btn_answer);
        btn_answer.setOnClickListener(this);
        //========================已经连接的设备=============================================
        recycle_link_alread = (SWRecyclerView) findViewById(R.id.recycle_link_alread);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_link_alread.setLayoutManager(layoutManager);
        wifiLinkAdapter = new WifiLinkRecyAdapter(WifiFragmentActivity.this, linkList);
        recycle_link_alread.setAdapter(wifiLinkAdapter);
        //====================显示的设备=============================================
        listView = (SWRecyclerView) findViewById(R.id.wifi_list);
        LinearLayoutManager layoutManager_list = new LinearLayoutManager(this);
        layoutManager_list.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager_list);
        wifiListRecycleAdapter = new WifiListRecycleAdapter(WifiFragmentActivity.this, list);
        listView.setAdapter(wifiListRecycleAdapter);
//        wifiParsener = new WifiParsener(WifiFragmentActivity.this, this, mWifiAdmin);
        initDialog();
    }

    WifiConNoPassDialog wifiConNoPassDialog;  //不需要密码的wifi
    WifiStateAlertDialog wifiStateAlertDialog; //网络连接状态dialog
    WifiHiddleDialog wifiHiddleDialog;
    WifiConnectionDialog wifiConnectDialog;

    private void initDialog() {
        wifiConNoPassDialog = new WifiConNoPassDialog(WifiFragmentActivity.this);
        wifiConNoPassDialog.setOnWifiSateChangeListener(this);
        wifiStateAlertDialog = new WifiStateAlertDialog(WifiFragmentActivity.this);
        wifiStateAlertDialog.setOnWifiSateChangeListener(this);
        wifiHiddleDialog = new WifiHiddleDialog(WifiFragmentActivity.this);
        wifiHiddleDialog.setOnWifiSateChangeListener(this);
        wifiConnectDialog = new WifiConnectionDialog(WifiFragmentActivity.this);
        wifiConnectDialog.setOnWifiSateChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_answer:
                MyToastView.getInstance().Toast(WifiFragmentActivity.this, "点击菜单 3 次,进入系统WIFI设置界面 ");
                break;
            case R.id.btn_add_wifi:
                wifiHiddleDialog.show();
                break;
            case R.id.refresh_list_btn:
                if (Biantai.isThreeClick()) {
                    MyToastView.getInstance().Toast(WifiFragmentActivity.this, "机器还没有反应过来呢 ！");
                    return;
                }
                getWifiListInfo();
                break;
        }
    }

    private void setListener() {
        wifiLinkAdapter.setOnItemClickListener(new ObjectAdapterClickListener() {
            @Override
            public void OnLongViewClick(Object paramObject) {

            }

            @Override
            public void clickItem(Object object, int position) {
                ScanResult scanResult = (ScanResult) object;
                wifiStateAlertDialog.show(scanResult);
            }

            @Override
            public void onSelectionItem(Object paramObject) {

            }
        });

        wifiListRecycleAdapter.setOnItemClickListener(new ObjectAdapterClickListener() {
            @Override
            public void OnLongViewClick(Object object) {

            }

            @Override
            public void clickItem(Object object, int position) {
                try {
                    ScanResult scanResult = (ScanResult) object;
                    currentChooiceScanResult = scanResult;
                    String desc = "";
                    String descOri = scanResult.capabilities;
                    if (descOri.toUpperCase().contains("WPA-PSK")) {
                        desc = "WPA";
                    } else if (descOri.toUpperCase().contains("WPA2-PSK")) {
                        desc = "WPA2";
                    } else if (descOri.toUpperCase().contains("WPA-PSK")
                            && descOri.toUpperCase().contains("WPA2-PSK")) {
                        desc = "WPA/WPA2";
                    }
                    MyLog.i("main", "============您点击的wifi  desc= " + desc + "  /// " + scanResult.SSID);
                    if (desc.equals("")) {  //不需要密码的wifi
                        wifiConNoPassDialog.show(scanResult);
                    } else { //需要密码连接的
                        wifiConnectDialog.show(scanResult);
                    }
                } catch (Exception e) {
                    Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                    startActivity(wifiSettingsIntent);
                }
            }

            @Override
            public void onSelectionItem(Object object) {

            }
        });
    }
    public static final String TAG = "WifiFragmentFragment";

    /**
     * 得到wifi的列表信息
     */
    private void getWifiListInfo() {
        refresh_list_btn.setText("刷新 ( ... )");
        mWifiAdmin.startScan();
        list.clear();
        linkList.clear();
        try {
            List<ScanResult> cacheList = mWifiAdmin.getWifiList();
            for (int i = 0; i < cacheList.size(); i++) {
                String wifiName = cacheList.get(i).SSID.trim();
                if (!TextUtils.isEmpty(wifiName) || wifiName.length() > 2) {
                    ScanResult scanResult = cacheList.get(i);
                    if (mWifiAdmin.isExsits(scanResult.SSID) != null
                            && mWifiAdmin.isExsits(scanResult.SSID).networkId == mWifiManager
                            .getConnectionInfo().getNetworkId()) {
                        addLinkWifi(cacheList.get(i));
                    } else {
                        int wifiLevel = cacheList.get(i).level;
                        MyLog.i(TAG, "====WifiList==" + cacheList.get(i).SSID + "   /wifi强度  :  " + wifiLevel);
                        list.add(cacheList.get(i));
                    }
                }
            }
        } catch (Exception e) {
            startSetting();
        }
        Collections.sort(list, new Comparator<ScanResult>() {

            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return rhs.level - lhs.level;
            }
        });
        refresh_list_btn.setText("刷新 (" + list.size() + ")");
        wifiListRecycleAdapter.setDatas(list);
        wifiLinkAdapter.setDatas(linkList);
    }

    public void addLinkWifi(ScanResult scanResult) {
        if (linkList.size() < 1) {
            linkList.add(scanResult);
        } else {
            for (int i = 0; i < linkList.size(); i++) {
                String wifiSave = linkList.get(i).SSID;
                if (!wifiSave.contains(scanResult.SSID)) {
                    linkList.add(scanResult);
                }
            }
        }
    }


    protected boolean isUpdate = true;
    public static final int GET_WIFI_LIST = 125 + 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_WIFI_LIST:
                    getWifiListInfo();
                    break;
                case REFRESH_CONN:
                    getWifiListInfo();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        isUpdate = false;
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void showToast(String toast) {
        MyToastView.getInstance().Toast(WifiFragmentActivity.this, toast);
    }

    @Override
    public void showWaitDialig(boolean isShow) {
        if (waitDialogUtil == null) {
            waitDialogUtil = new WaitDialogUtil(WifiFragmentActivity.this);
        }
        if (isShow) {
            waitDialogUtil.show("处理中");
        } else {
            waitDialogUtil.dismiss();
        }
    }

    @Override
    public void dealWifiState(boolean isTrue) {
        if (isTrue) {
            getWifiListInfo();
            MyToastView.getInstance().Toast(WifiFragmentActivity.this, "网络连接成功");
        } else {
            MyToastView.getInstance().Toast(WifiFragmentActivity.this, "网络连接失败");
        }
    }


    //================================================================================================

    /***
     * 连接需要账号密码的wifi
     * @param wifiName
     * @param password
     * @param type
     */
    @Override
    public void lineHiddleWifi(String wifiName, String password, WifiConnectUtils.WifiCipherType type) {
        Runnable runnable = new HiddleWifiThread(WifiFragmentActivity.this, wifiName, password, type, this);
        Thread thread = new Thread(runnable);
        thread.start();
}

    @Override
    public void lineWifi(String wifiName, String password, ScanResult scanResult) {
        Runnable runnable = new LineWifiThread(WifiFragmentActivity.this, wifiName, password, scanResult, this);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void lineNopasswordWifi(ScanResult scanResult) {
        Runnable runnable = new NoWifiPsdThread(scanResult, mWifiAdmin, this);
        Thread threate = new Thread(runnable);
        threate.start();
    }

    /***
     * 忘记WIFI
     * @param scanResult
     */
    @Override
    public void forget(ScanResult scanResult) {
        showWaitDialig(true);
        dbWifiUtil.deleteByName(scanResult.SSID);
        Runnable runnable = new ForgetWIfiThread(mWifiAdmin, scanResult, this);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    //================================================================================================

    /***进入设置界面*/
    public void startSetting() {
        MyToastView.getInstance().Toast(WifiFragmentActivity.this, "WIFI连接异常，跳转至系统WIFI");
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        startActivity(wifiSettingsIntent);
    }

    private void registerReceiver() {
        mReceiver = new WifiReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(AppInfo.NET_DISONLINE);
        filter.addAction(AppInfo.NET_ONLINE);
        registerReceiver(mReceiver, filter);
    }

    int menu_add;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            menu_add++;
            if (menu_add == 3) {
                startSetting();
                menu_add = 0;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private Timer timer;
    TimerTask task;

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            MyLog.i("main", "=========计时任务");
            handler.sendEmptyMessage(GET_WIFI_LIST);
        }
    }

    private void getWifiListOnTime() {
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
        timer = new Timer(true);
        task = new MyTask();
        timer.schedule(task, 5000, 15000);
    }
}
