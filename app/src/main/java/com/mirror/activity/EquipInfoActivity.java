package com.mirror.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.config.AppInfo;
import com.mirror.util.CodeUtil;
import com.mirror.util.QRCodeUtil;
import com.mirror.util.exit.ExitParsener;
import com.mirror.util.exit.ExitView;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;

import java.io.File;

public class EquipInfoActivity extends BaseActivity implements ExitView {
    private static final int CREATE_FAILED = 1;
    private static final int CREATE_SUCCESS = 0;
    ExitParsener exitParsener;
    ImageView iv_bind_code;
    TextView tv_admin_mob;
    TextView tv_bind_desc;
    TextView tv_device_id;
    TextView tv_shop_name;
    TextView tv_user_name;
    WaitDialogUtil waitDialogUtil;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_equip_info);
        initView();
        updateBindErCodeImage();
    }

    private void initView() {
        this.waitDialogUtil = new WaitDialogUtil(this);
        this.iv_bind_code = ((ImageView) findViewById(R.id.iv_bind_code));
        this.tv_bind_desc = ((TextView) findViewById(R.id.tv_bind_desc));
        this.tv_device_id = ((TextView) findViewById(R.id.tv_device_id));
        this.tv_shop_name = ((TextView) findViewById(R.id.tv_shop_name));
        this.tv_user_name = ((TextView) findViewById(R.id.tv_user_name));
        this.tv_admin_mob = ((TextView) findViewById(R.id.tv_admin_mob));
        this.exitParsener = new ExitParsener(this, this);
        this.exitParsener.autoLogintoWeb();
    }


    private void updateBindErCodeImage() {
        String blueToothCode = CodeUtil.getBlueToothCode();
        tv_bind_desc.setText("设备ID：" + blueToothCode);
        String bindCodeDesc = "{\"bind_code\":" + blueToothCode + "}";
        QRCodeUtil.createErCode(bindCodeDesc, AppInfo.EQUIP_BIND_CODE, new QRCodeUtil.ErCodeListener() {
            @Override
            public void createSuccess(String path) {
                Message localMessage = new Message();
                localMessage.what = CREATE_SUCCESS;
                localMessage.obj = path;
                EquipInfoActivity.this.handler.sendMessage(localMessage);
            }

            @Override
            public void createFailed(String error) {
                Message localMessage = new Message();
                localMessage.what = CREATE_FAILED;
                localMessage.obj = error;
                EquipInfoActivity.this.handler.sendMessage(localMessage);
            }
        });
    }

    public void loginSuccess(String shopName, String username, String adminMob) {
        String devicdId = CodeUtil.getBlueToothCode();
        String shopMob = adminMob;
        if (TextUtils.isEmpty(adminMob)) {
            shopMob = "没有店长";
        }
        this.tv_device_id.setText("设备ID:  " + devicdId);
        this.tv_shop_name.setText("店铺名字:  " + shopName);
        this.tv_user_name.setText("账号信息:  " + username);
        this.tv_admin_mob.setText("管理员手机:  " + shopMob);
    }


    public void showTaost(String paramString) {
        MyToastView.getInstance().Toast(this, paramString);
    }

    public void showWaitDialog(boolean paramBoolean) {
        if (paramBoolean) {
            this.waitDialogUtil.show("加载中");
            return;
        }
        this.waitDialogUtil.dismiss();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CREATE_SUCCESS:
                    String picPath = (String) msg.obj;
                    if (new File(picPath).exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                        iv_bind_code.setImageBitmap(bitmap);
                        return;
                    }
                    MyToastView.getInstance().Toast(EquipInfoActivity.this, "二维码不存在");
                    return;
                case CREATE_FAILED:
                    String errorDesc = (String) msg.obj;
                    MyToastView.getInstance().Toast(EquipInfoActivity.this, errorDesc);
                    break;
            }

        }
    };
}
