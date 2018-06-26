package com.cdl.wifi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cdl.wifi.db.DBWifiEntity;
import com.cdl.wifi.db.DBWifiUtil;
import com.cdl.wifi.listener.WifChangeListener;
import com.cdl.wifi.util.WifiAdmin;
import com.mirror.R;
import com.mirror.view.MyToastView;

public class WifiConnectionDialog {

    Context context;
    Dialog mDialog;
    String TAG = "wifi";
    DBWifiUtil dbWifiUtil;

    public WifiConnectionDialog(final Context context) {
        dbWifiUtil = new DBWifiUtil(context);
        this.context = context;
        mDialog = new Dialog(context, R.style.MyDialog);
        final View layout = View.inflate(context, R.layout.view_wifi_conn, null);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(layout, new ViewGroup.LayoutParams(800, 400));
        initView(layout);
        initData();
    }

    private EditText edtPassword;
    TextView txtWifiName, txtSinglStrength, txtSecurityLevel, tv_wifi_safe;
    Button BtnCancel, BtnConn;

    private void initView(View layout) {
        edtPassword = (EditText) layout.findViewById(R.id.edt_password);
        //======================================
        tv_wifi_safe = (TextView) layout.findViewById(R.id.tv_wifi_safe);
        txtWifiName = (TextView) layout.findViewById(R.id.txt_wifi_name);
        txtSinglStrength = (TextView) layout.findViewById(R.id.txt_signal_strength);
        txtSecurityLevel = (TextView) layout.findViewById(R.id.txt_security_level);
        BtnCancel = (Button) layout.findViewById(R.id.btn_cancel);
        BtnConn = (Button) layout.findViewById(R.id.btn_connect);
        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyBord(v);
                }
            }
        });
    }

    public void showKeyBord(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    private void initData() {
        BtnConn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String password = getPassword();
                if (TextUtils.isEmpty(password) || password.length() < 8) {
                    MyToastView.getInstance().Toast(context, "请输入大于8位数的密码");
                    return;
                }
                try {
                    DBWifiEntity entity = new DBWifiEntity(scanResult.SSID, getPassword(), scanResult.capabilities.toUpperCase());
                    dbWifiUtil.addWifiInfo(entity);
                    listener.lineWifi(scanResult.SSID, password, scanResult);
                } catch (Exception e) {

                }
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
        String savePasswird = "";
        DBWifiEntity entity = dbWifiUtil.queryByName(scanResult.SSID);
        if (entity != null) {
            savePasswird = entity.getWifipsd();
        }
        edtPassword.setText(savePasswird);

        //========機智的分割線===================
        this.scanResult = scanResult;
        txtWifiName.setText("  " + scanResult.SSID);
        txtSinglStrength.setText("  " + WifiAdmin.singlLevToStr(scanResult.level));
        txtSecurityLevel.setText("  " + scanResult.capabilities.toUpperCase());
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

    public String getPassword() {
        return edtPassword.getText().toString().trim();
    }


    public WifChangeListener listener;

    public void setOnWifiSateChangeListener(WifChangeListener listener) {
        this.listener = listener;
    }
}
