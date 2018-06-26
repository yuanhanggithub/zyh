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
import com.mirror.R;

public class WifiConNoPassDialog {

    Context context;
    Dialog mDialog;

    public WifiConNoPassDialog(final Context context) {
        this.context = context;
        mDialog = new Dialog(context, R.style.MyDialog);
        final View layout = View.inflate(context, R.layout.dialog_conn_no_password, null);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(layout, new ViewGroup.LayoutParams(800, 400));
        initView(layout);
        initData();
    }

    TextView txtWifiName;
    Button BtnCancel, BtnConn;

    private void initView(View layout) {
        txtWifiName = (TextView) layout.findViewById(R.id.txt_wifi_name);
        BtnCancel = (Button) layout.findViewById(R.id.btn_cancel);
        BtnConn = (Button) layout.findViewById(R.id.btn_connect);
    }

    private void initData() {
        BtnConn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.lineNopasswordWifi(scanResult);
                dissmiss();
            }
        });
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dissmiss();
            }
        });
    }

    ScanResult scanResult;

    public void show(ScanResult scanResult) {
        this.scanResult = scanResult;
        txtWifiName.setText("WiFiName : " + scanResult.SSID);
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
