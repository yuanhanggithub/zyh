package com.mirror.guide;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

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
import com.mirror.config.AppInfo;
import com.mirror.guide.adapter.WifiListLittleAdapter;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;
import com.mirror.view.recycle.SWRecyclerView;
import com.mirror.wifi.ObjectAdapterClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GuideWifiFragment extends Fragment implements ObjectAdapterClickListener, WifiView, WifChangeListener, WifiDealListener {
    WaitDialogUtil waitDialogUtil;
    public static final String TAG = "WifiFragmentFragment";
    private Button btn_line_hiddle;
    private Button btn_refresh_net;
    ConnectivityManager cm;
    private List<ScanResult> list = new ArrayList();
    private WifiListLittleAdapter mAdapter;
    private WifiAdminUtils mWifiAdmin;
    WifiManager mWifiManager;
    private SWRecyclerView recyview_video_content;
    TextView tv_wifi_line_state;
    DBWifiUtil dbWifiUtil;

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {// wifi發生改變
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                MyLog.e("wifi", "=========wifi状态===" + info.getState());
                MyLog.e("wifi", "=========wifi action===" + action);
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    //網絡斷開
                    updateTextView("网络断开");
                } else if (info.getState().equals(NetworkInfo.State.CONNECTING)) {
                    //正在连接
                    updateTextView("正在连接");
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    //连接成功
                    updateTextView("连接成功");
                }
            } else if (action.equals(AppInfo.NET_ONLINE)) {
                getWifiListInfo();
            } else if (action.equals(AppInfo.NET_DISONLINE)) {
                getWifiListInfo();
            } else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                //密碼驗證失敗，這裡會彈窗
                int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
                switch (error) {
                    case WifiManager.ERROR_AUTHENTICATING:
                        Log.d(TAG, "密码认证错误Code为：" + error);
                        MyToastView.getInstance().Toast(getActivity(), "wifi密码认证错误！");
                        break;
                    default:
                        break;
                }
            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                getWifiListInfo();
            }
        }
    };

    public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
        if (paramBoolean) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_enter);
        }
        return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_exit);
    }

    @Nullable
    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View view = View.inflate(getActivity(), R.layout.guide_wifi_list, null);
        initView(view);
        initDialog();
        initListener();
        getWifiListInfo();
        return view;
    }


    WifiConNoPassDialog wifiConNoPassDialog;  //不需要密码的wifi
    WifiStateAlertDialog wifiStateAlertDialog; //网络连接状态dialog
    WifiHiddleDialog wifiHiddleDialog;
    WifiConnectionDialog wifiConnectDialog;

    private void initDialog() {
        wifiConNoPassDialog = new WifiConNoPassDialog(getActivity());
        wifiConNoPassDialog.setOnWifiSateChangeListener(this);
        wifiStateAlertDialog = new WifiStateAlertDialog(getActivity());
        wifiStateAlertDialog.setOnWifiSateChangeListener(this);
        wifiHiddleDialog = new WifiHiddleDialog(getActivity());
        wifiHiddleDialog.setOnWifiSateChangeListener(this);
        wifiConnectDialog = new WifiConnectionDialog(getActivity());
        wifiConnectDialog.setOnWifiSateChangeListener(this);
    }


    /**
     * 得到wifi的列表信息
     */
    private void getWifiListInfo() {
        showWaitDialig(true);
        btn_refresh_net.setText("刷新 ( ... )");
        list.clear();
        mWifiAdmin.startScan();
        try {
            List<ScanResult> cacheList = mWifiAdmin.getWifiList();
            for (int i = 0; i < cacheList.size(); i++) {
                String wifiName = cacheList.get(i).SSID.trim();
                if (!TextUtils.isEmpty(wifiName) || wifiName.length() > 2) {
                    ScanResult scanResult = cacheList.get(i);
                    if (mWifiAdmin.isExsits(scanResult.SSID) != null && mWifiAdmin.isExsits(scanResult.SSID).networkId == mWifiManager.getConnectionInfo().getNetworkId()) {
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
            public int compare(ScanResult scanResultold, ScanResult scanResultnew) {
                return scanResultnew.level - scanResultold.level;
            }
        });
        btn_refresh_net.setText("刷新 (" + list.size() + ")");
        mAdapter.setDatas(list);
        showWaitDialig(false);
    }

    private void initView(View view) {
        dbWifiUtil = new DBWifiUtil(getActivity());
        mWifiManager = ((WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        cm = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
        mWifiAdmin = new WifiAdminUtils(getActivity());
        tv_wifi_line_state = (TextView) view.findViewById(R.id.tv_wifi_line_state);
        recyview_video_content = (SWRecyclerView) view.findViewById(R.id.recyview_video_content);
        LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(getActivity());
        localLinearLayoutManager.setOrientation(1);
        recyview_video_content.setLayoutManager(localLinearLayoutManager);
        mAdapter = new WifiListLittleAdapter(getActivity(), list);
        mAdapter.setOnAdapterClickListener(this);
        recyview_video_content.setAdapter(mAdapter);
        btn_refresh_net = (Button) view.findViewById(R.id.btn_refresh_net);
        btn_line_hiddle = (Button) view.findViewById(R.id.btn_line_hiddle);
    }


    private void initListener() {
        btn_refresh_net.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                getWifiListInfo();
            }
        });
        btn_line_hiddle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                wifiHiddleDialog.show();
            }
        });
    }

    private void updateTextView(String paramString) {
        if (this.tv_wifi_line_state != null) {
            tv_wifi_line_state.setText(paramString);
        }
    }

    public void OnLongViewClick(Object paramObject) {
    }

    public void addLinkWifi(ScanResult scanResult) {
        String wifiName = scanResult.SSID;
        updateTextView(wifiName + "  已连接");
    }

    public void clickItem(Object object, int paramInt) {
        try {
            ScanResult localScanResult = (ScanResult) object;
            MyLog.i("main", "============您点击的wifi = " + localScanResult.SSID);
            String wifiDesc = "";
            String capabilities = localScanResult.capabilities;
            if (capabilities.toUpperCase().contains("WPA-PSK")) {
                wifiDesc = "WPA";
            } else if (capabilities.toUpperCase().contains("WPA2-PSK")) {
                wifiDesc = "WPA2";
            } else if (capabilities.toUpperCase().contains("WPA-PSK")) {
                wifiDesc = "WPA-PSK";
            } else if (capabilities.toUpperCase().contains("WPA2-PSK")) {
                wifiDesc = "WPA/WPA2";
            }
            if (TextUtils.isEmpty(wifiDesc)) {
                wifiConNoPassDialog.show(localScanResult);
                return;
            }
            wifiConnectDialog.show(localScanResult);
        } catch (Exception e) {
            startActivity(new Intent("android.settings.WIFI_SETTINGS"));
        }
    }

    public void onAttach(Activity paramActivity) {
        super.onAttach(paramActivity);
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
        localIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        localIntentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        localIntentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        localIntentFilter.addAction("com.reeman.receiver.NET_DISONLINE");
        localIntentFilter.addAction("com.reeman.receiver.NET_ONLINE");
        paramActivity.registerReceiver(this.receiver, localIntentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDestroyView() {
        getActivity().unregisterReceiver(this.receiver);
        super.onDestroyView();
    }

    public void onSelectionItem(Object paramObject) {
    }

    public void startSetting() {
        MyToastView.getInstance().Toast(getActivity(), "WIFI连接异常，跳转至系统WIFI");
        startActivity(new Intent("android.settings.WIFI_SETTINGS"));
    }

    @Override
    public void showToast(String toast) {
        MyToastView.getInstance().Toast(getActivity(), toast);
    }


    @Override
    public void showWaitDialig(boolean isShow) {
        if (waitDialogUtil == null) {
            waitDialogUtil = new WaitDialogUtil(getActivity());
        }
        if (isShow) {
            waitDialogUtil.show("加载中");
        } else {
            waitDialogUtil.dismiss();
        }
    }

    @Override
    public void dealWifiState(boolean isTrue) {
        getWifiListInfo();
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
        Runnable runnable = new HiddleWifiThread(getActivity(), wifiName, password, type, this);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void lineWifi(String wifiName, String password, ScanResult scanResult) {
        Runnable runnable = new LineWifiThread(getActivity(), wifiName, password, scanResult, this);
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
        dbWifiUtil.deleteByName(scanResult.SSID);
        Runnable runnable = new ForgetWIfiThread(mWifiAdmin, scanResult, this);
        Thread thread = new Thread(runnable);
        thread.start();
    }


    @Override
    public void dealWifiSuccess() {
        getWifiListInfo();
    }

    @Override
    public void dealWifiFailed(String wifiErrordesc) {
        showToast(wifiErrordesc);
        getWifiListInfo();
    }
}
