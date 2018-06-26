package com.mirror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.config.AppConfig;
import com.mirror.util.SharedPerManager;
import com.mirror.view.MyToastView;
import com.mirror.view.dialog.OridinryDialog;

public class DisOnlineActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_set_model;
    TextView tv_equip_model;
    private Handler handler = new Handler();
    boolean isRestart = false;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_disline_model);
        initView();
    }

    private void initView() {
        tv_equip_model = (TextView) findViewById(R.id.tv_equip_model);
        btn_set_model = (Button) findViewById(R.id.btn_set_model);
        btn_set_model.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {
        boolean isOnline = SharedPerManager.isOnline();
        if (isOnline) {
            tv_equip_model.setTextColor(getResources().getColor(R.color.color_app));
            tv_equip_model.setText("网络模式");
        } else {
            tv_equip_model.setTextColor(getResources().getColor(R.color.red));
            tv_equip_model.setText("离线模式");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_model:
                boolean isOnline = SharedPerManager.isOnline();
                SharedPerManager.setOnline(!isOnline);
                MyToastView.getInstance().Toast(DisOnlineActivity.this, "设置模式成功.2秒后自动重启软件");
                updateView();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        restrtApp();
                    }
                }, 2000);
                break;
        }
    }

    /***
     * 重启设备
     */
    public void restrtApp() {
        if (isRestart) {  //防止多次执行
            return;
        }
        isRestart = true;
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
