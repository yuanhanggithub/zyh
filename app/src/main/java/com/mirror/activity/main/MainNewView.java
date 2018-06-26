package com.mirror.activity.main;

import android.widget.TextView;

import cn.cdl.library.CycleViewPager;


public interface MainNewView {

    CycleViewPager getBottomView();

    TextView getCurrentTime();

    CycleViewPager getCycleViewPager();

    TextView getTvDeviceId();

    TextView getTvWifiName();

    void paperUpdateSystem();

    void showToast(String paramString);

    void showWaitDialog(boolean paramBoolean);
}
