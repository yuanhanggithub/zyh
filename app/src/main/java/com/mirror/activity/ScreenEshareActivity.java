package com.mirror.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.config.AppConfig;
import com.mirror.config.AppInfo;
import com.mirror.util.CodeUtil;
import com.mirror.util.DisPlayUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.QRCodeUtil;
import com.mirror.util.eshare.EShareUtil;
import com.mirror.view.MyToastView;
import com.mirror.wifi.WifiMgr;

import java.io.File;

public class ScreenEshareActivity extends BaseActivity {
    private static final int ERCODE_CREATE_FRAILED = 5462;
    private static final int ERCODE_CREATE_SUCCESS = 5461;
    Button btn_go_introduct;
    private Button btn_open_esahres_app;
    TextView connWifi;
    TextView deviID;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_screen);
        intiView();
        initTouPingView();
        initListener();
    }

    private void intiView() {
        touping_er = (ImageView) findViewById(R.id.touping_er);
        tv_ercode_desc = (TextView) findViewById(R.id.tv_ercode_desc);
        btn_go_introduct = (Button) findViewById(R.id.btn_go_introduct);
        btn_open_esahres_app = (Button) findViewById(R.id.btn_open_esahres_app);
        connWifi = (TextView) findViewById(R.id.connWifi);
        deviID = (TextView) findViewById(R.id.deviID);
        btn_open_esahres_app = ((Button) findViewById(R.id.btn_open_esahres_app));
    }

    private void initListener() {
        btn_go_introduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (!NetWorkUtils.isNetworkConnected(ScreenEshareActivity.this)) {
                    MyToastView.getInstance().Toast(ScreenEshareActivity.this, "请链接网络后重试");
                    return;
                }
                if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {
                    MyToastView.getInstance().Toast(ScreenEshareActivity.this, "一代设备无法使用该功能");
                    return;
                }
                Intent intent = new Intent(ScreenEshareActivity.this, WifiHotSetActvity.class);
                startActivity(intent);
            }
        });

        this.btn_open_esahres_app.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (!NetWorkUtils.isNetworkConnected(ScreenEshareActivity.this)) {
                    MyToastView.getInstance().Toast(ScreenEshareActivity.this, "请链接网络后重试");
                    return;
                }
                if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {
                    MyToastView.getInstance().Toast(ScreenEshareActivity.this, "一代设备无法使用该功能");
                    return;
                }
                DisPlayUtil.startApp(ScreenEshareActivity.this, AppInfo.ESHARE_SERVER_PACKAGE);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateErCode();
    }

    private void updateErCode() {
        String bindCodeInfo = "http://" + NetWorkUtils.getIP(this) + ":8000";
        QRCodeUtil.createErCode(bindCodeInfo, AppInfo.TOU_PING_ER_CODE_PATH, new QRCodeUtil.ErCodeListener() {
            @Override
            public void createSuccess(String path) {
                Message localMessage = new Message();
                localMessage.what = ERCODE_CREATE_SUCCESS;
                localMessage.obj = path;
                ScreenEshareActivity.this.handler.sendMessage(localMessage);
            }

            @Override
            public void createFailed(String error) {
                Message localMessage = new Message();
                localMessage.what = ERCODE_CREATE_FRAILED;
                localMessage.obj = error;
                ScreenEshareActivity.this.handler.sendMessage(localMessage);
            }
        });

    }

    ImageView touping_er;
    TextView tv_ercode_desc;

    private void initTouPingView() {
        try {
            String wifiName = NetWorkUtils.getConnectName(this);
            this.connWifi.setText("WIFI : " + wifiName);
            String bleethCode = CodeUtil.getBlueToothCode();
            bleethCode = bleethCode.substring(bleethCode.length() - 4, bleethCode.length());
            bleethCode = "尽善镜美-" + bleethCode;
            deviID.setText("设备ID : " + bleethCode);
            EShareUtil.setDeviceName(this, bleethCode);
            sendBroadcast(new Intent("com.eshare.action.DEVICE_NAME_CHANGED"));
        } catch (Exception e) {

        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ERCODE_CREATE_SUCCESS:
                    String picPath = (String) msg.obj;
                    if (new File(picPath).exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                        touping_er.setImageBitmap(bitmap);
                        tv_ercode_desc.setText("扫码下载APK,更多体验");
                    } else {
                        touping_er.setImageResource(R.mipmap.cion_no_install);
                        tv_ercode_desc.setText("二维码不存在 ,请重启设备刷新");
                    }
                    break;
                case ERCODE_CREATE_FRAILED:
                    String errorDesc = (String) msg.obj;
                    touping_er.setImageResource(R.mipmap.cion_no_install);
                    tv_ercode_desc.setText(errorDesc);
                    break;
            }
        }
    };


}
