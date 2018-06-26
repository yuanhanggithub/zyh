package com.mirror.guide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.mirror.R;
import com.mirror.activity.SplashActivity;
import com.mirror.util.SharedPerManager;
import com.mirror.view.MyToastView;
import com.mirror.view.dialog.OridinryDialog;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends FragmentActivity implements View.OnClickListener {
    public static final String TAG_DEFAULT = "TAG_DEFAULT";
    public int currentFragmentPosition = 0;
    Button btn_next;
    Button btn_pre;
    private FragmentManager fm;
    List<Fragment> listFragment = new ArrayList();
    OridinryDialog oridinryDialog;
    int tag = -1;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_guide);
        getFragmentDate();
        initView();
    }


    private void initView() {
        Intent localIntent = getIntent();
        if (localIntent != null) {
            this.tag = localIntent.getIntExtra("TAG_DEFAULT", -1);
        }
        oridinryDialog = new OridinryDialog(this, SharedPerManager.getScreenWidth(), SharedPerManager.getScreenHeight());
        fm = getSupportFragmentManager();
        btn_pre = ((Button) findViewById(R.id.btn_pre));
        btn_next = ((Button) findViewById(R.id.btn_next));
        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        setTabSelection(0);
    }

    private void getFragmentDate() {
        listFragment.add(new GuideIntoduceFragment());
        listFragment.add(new GuideWifiFragment());
        listFragment.add(new GuideMobDownfragment());
        listFragment.add(new GuideMobIntroducefragment());
        listFragment.add(new GuideBindFragment());
    }

    private void updateButtonView(int paramInt) {
        if (paramInt == 0) {
            btn_pre.setVisibility(View.GONE);
        } else {
            btn_pre.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_pre:
                currentFragmentPosition -= 1;
                if (currentFragmentPosition < 0) {
                    currentFragmentPosition = listFragment.size() - 1;
                }
                setTabSelection(currentFragmentPosition);
                return;
            case R.id.btn_next:
                currentFragmentPosition = currentFragmentPosition + 1;
                Log.i("position", "=======当前的位置==" + currentFragmentPosition + "  /" + this.listFragment.size());
                if (currentFragmentPosition > listFragment.size() - 1) {
                    if (tag != -1) {
                        finish();
                        return;
                    }
                    MyToastView.getInstance().Toast(this, "恭喜您，设备注册完成啦 ");
                    SharedPerManager.setFirstOpenDevice(false);
                    startActivity(new Intent(this, SplashActivity.class));
                    finish();
                    break;
                }
                setTabSelection(currentFragmentPosition);
        }
    }


    protected void onDestroy() {
        super.onDestroy();
    }

    int menu_add;

    public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            menu_add += 1;
            if (menu_add == 3) {
                startSetting();
                menu_add = 0;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (tag == -1) {
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, paramKeyEvent);
    }

    @SuppressLint({"CommitTransaction"})
    public void setTabSelection(int paramInt) {
        currentFragmentPosition = paramInt;
        updateButtonView(paramInt);
        FragmentTransaction localFragmentTransaction = fm.beginTransaction();
        localFragmentTransaction.replace(R.id.fragment_guide, listFragment.get(paramInt));
        localFragmentTransaction.commit();
    }

    public void startSetting() {
        MyToastView.getInstance().Toast(this, "WIFI连接异常，跳转至系统WIFI");
        startActivity(new Intent("android.settings.WIFI_SETTINGS"));
    }
}
