package com.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.util.CodeUtil;
import com.mirror.view.WaitDialogUtil;
import com.mirror.wifi.ApMgr;
import com.mirror.wifi.WifiMgr;

public class WifiHotSetActvity extends BaseActivity implements View.OnClickListener {

    WaitDialogUtil waitDialogUtil;
    TextView tv_ap_state;
    Handler handler = new Handler();
    ImageView iv_check;
    Button btn_open_ap;
    String wifiHostName;
    TextView tv_name;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_wifi_hot);
        initView();
    }

    private void initView() {
        waitDialogUtil = new WaitDialogUtil(WifiHotSetActvity.this);
        iv_check = (ImageView) findViewById(R.id.iv_check);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_ap_state = (TextView) findViewById(R.id.tv_ap_state);
        btn_open_ap = (Button) findViewById(R.id.btn_open_ap);
        btn_open_ap.setOnClickListener(this);
        wifiHostName = CodeUtil.getBlueToothCode();
        wifiHostName = wifiHostName.substring(wifiHostName.length() - 4, wifiHostName.length());
        wifiHostName = "MIRROR_MABIC_" + wifiHostName;
        tv_name.setText("WIFI 信息 ：" + wifiHostName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_ap:
                boolean isOpenState = ApMgr.isApOn(WifiHotSetActvity.this);
                if (isOpenState) {
                    ApMgr.disableAp(WifiHotSetActvity.this);
                    Log.e("apState", "===========关闭热点0000：");
                } else {
                    boolean isStat = ApMgr.configApState(WifiHotSetActvity.this, wifiHostName);
                    Log.e("apState", "===========打开热点：" + isStat);
                }
                updateView();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {
        waitDialogUtil.show("加載中");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                waitDialogUtil.dismiss();
                boolean isApOpen = ApMgr.isApOn(WifiHotSetActvity.this);
                Log.e("apState", "===========当前设备IP打开状态：" + isApOpen);
                if (isApOpen) {
                    tv_ap_state.setText("打开");
                    iv_check.setBackgroundResource(R.mipmap.check_yes);
                } else {
                    tv_ap_state.setText("关闭");
                    iv_check.setBackgroundResource(R.mipmap.check_no);
                }
            }
        }, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean isApOpen = ApMgr.isApOn(WifiHotSetActvity.this);
        if (!isApOpen) {
            WifiMgr.getInstance(WifiHotSetActvity.this).openWifi();
        }
    }

    int clickNum = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            clickNum++;
            if (clickNum > 5) {
                startActivity(new Intent(WifiHotSetActvity.this, AdbWifiActivity.class));
                clickNum = 0;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
