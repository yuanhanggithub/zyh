package com.mirror.activity;

import android.content.Intent;
import android.os.Bundle;

import com.cdl.wifi.activity.WifiFragmentActivity;
import com.cdl.wifi.activity.WifiTestActivity;
import com.mirror.guide.GuideActivity;
import com.mirror.test.TestActivity;
import com.mirror.util.SharedPerManager;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        initState();
    }

    private void initState() {
        SharedPerManager.setToken("");
        SharedPerManager.setLogin(false);
        boolean isFirstLogin = SharedPerManager.isFirstOpenDevice();
        if (isFirstLogin) {
            startActivity(new Intent(this, GuideActivity.class));
        } else {
            startActivity(new Intent(this, SplashActivity.class));
//            startActivity(new Intent(this, WifiFragmentActivity.class));
        }
        finish();
    }
}
