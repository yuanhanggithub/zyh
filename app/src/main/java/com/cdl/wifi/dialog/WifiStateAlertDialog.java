package com.cdl.wifi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cdl.wifi.listener.WifChangeListener;
import com.cdl.wifi.util.MyLog;
import com.cdl.wifi.util.WifiAdmin;
import com.mirror.R;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;

public class WifiStateAlertDialog implements View.OnClickListener {

    Context context;
    Dialog mDialog;

    public WifiStateAlertDialog(final Context context) {
        this.context = context;
        mDialog = new Dialog(context, R.style.MyDialog);
        final View layout = View.inflate(context, R.layout.dialog_state_alert, null);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(layout, new ViewGroup.LayoutParams(800, 300));
        initView(layout);
    }

    TextView txtWifiName, tv_wifi_safe, tv_wifi_level, tv_wifi_ip;
    Button BtnCancel, btn_forgot;

    private void initView(View layout) {
        tv_wifi_ip = (TextView) layout.findViewById(R.id.tv_wifi_ip);
        tv_wifi_safe = (TextView) layout.findViewById(R.id.tv_wifi_safe);
        tv_wifi_level = (TextView) layout.findViewById(R.id.tv_wifi_level);
        txtWifiName = (TextView) layout.findViewById(R.id.txt_wifi_name);
        btn_forgot = (Button) layout.findViewById(R.id.btn_forgot);
        BtnCancel = (Button) layout.findViewById(R.id.btn_cancel);
        btn_forgot.setOnClickListener(this);
        BtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                MyLog.e("mian", "=========点击了取消按钮");
                dissmiss();
                break;
            case R.id.btn_forgot:
                MyLog.e("mian", "=========点击了忘记按钮");
                if (listener != null) {
                    listener.forget(scanResult);
                }
                dissmiss();
                break;
        }

    }

    ScanResult scanResult;

    public void show(ScanResult scanResult) {
        MyLog.i("main", "======状态栏dialog显示=====");
        this.scanResult = scanResult;
        int level = scanResult.level;
        tv_wifi_ip.setText(NetWorkUtils.getIP(context));
        txtWifiName.setText("WiFiName : " + scanResult.SSID);
        tv_wifi_safe.setText(scanResult.capabilities.toUpperCase());
        tv_wifi_level.setText(WifiAdmin.singlLevToStr(level));
        mDialog.show();
    }

    public void dissmiss() {
        if (mDialog == null) {
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public WifChangeListener listener;

    public void setOnWifiSateChangeListener(WifChangeListener listener) {
        this.listener = listener;
    }
}
