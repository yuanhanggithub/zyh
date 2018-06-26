//package com.cdl.wifi.dialog;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.cdl.wifi.listener.WifiStateListener;
//import com.cdl.wifi.util.MyLog;
//import com.cdl.wifi.util.WifiAdmin;
//import com.mirror.R;
//
//public class WifiStateDialog {
//
//    Context context;
//    Dialog mDialog;
//
//
//    public WifiStateDialog(final Context context) {
//        this.context = context;
//        mDialog = new Dialog(context, R.style.MyDialog);
//        final View layout = View.inflate(context, R.layout.view_wifi_status, null);
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.setContentView(layout, new ViewGroup.LayoutParams(800, 400));
//        initView(layout);
//        initData();
//    }
//
//    TextView txtWifiName, txtConnStatus, txtSinglStrength, txtSecurityLevel, txtIpAddress;
//    Button btn_cancel, btn_disconnnection;
//
//    private void initView(View layout) {
//        txtWifiName = (TextView) layout.findViewById(R.id.txt_wifi_name);
//        txtConnStatus = (TextView) layout.findViewById(R.id.txt_conn_status);
//        txtSinglStrength = (TextView) layout.findViewById(R.id.txt_signal_strength);
//        txtSecurityLevel = (TextView) layout.findViewById(R.id.txt_security_level);
//        txtIpAddress = (TextView) layout.findViewById(R.id.txt_ip_address);
//        btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
//        btn_disconnnection = (Button) layout.findViewById(R.id.btn_disconnnection);
//    }
//
//    private void initData() {
//        btn_disconnnection.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                MyLog.i("TAG", "======點擊了斷開連接===1111");
//                listener.disConnection(wifiName);
//                dissmiss();
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dissmiss();
//            }
//        });
//    }
//
//    String wifiName;
//
//    public void show(String wifiName, String wapp, int level, String ipAddress) {
//        this.wifiName = wifiName;
//        txtWifiName.setText(wifiName);
//        txtSecurityLevel.setText(wapp);
//        txtSinglStrength.setText(WifiAdmin.singlLevToStr(level));
//        txtIpAddress.setText(ipAddress);
//        mDialog.show();
//    }
//
//    public void dissmiss() {
//        if (mDialog == null) {
//            return;
//        }
//        if (mDialog.isShowing()) {
//            mDialog.dismiss();
//        }
//    }
//
//    public WifiStateListener listener;
//
//    public void setOnWifiSateChangeListener(WifiStateListener listener) {
//        this.listener = listener;
//    }
//}
