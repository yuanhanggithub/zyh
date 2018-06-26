package com.mirror.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.mirror.util.ActivityCollector;
import com.mirror.util.FileUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;
import com.mirror.view.MyToastView;

public class BaseActivity extends Activity {

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityCollector.addActivity(this);
        FileUtil.creatDirPathNoExists();
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    protected void onResume() {
        super.onResume();
        if (!NetWorkUtils.isNetworkConnected(this)) {
            if (SharedPerManager.isOnline()) {  //网络模式弹框，离线模式不弹
                MyToastView.getInstance().Toast(this, "网络异常，请检查");
            }
        }
        getScreenSize();
    }

    private void getScreenSize() {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int i = localDisplayMetrics.widthPixels;
        int j = localDisplayMetrics.heightPixels;
        SharedPerManager.setScreenWidth(i);
        SharedPerManager.setScreenHeight(j);
    }
}
