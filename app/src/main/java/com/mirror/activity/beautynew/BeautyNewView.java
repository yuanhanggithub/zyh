package com.mirror.activity.beautynew;

import android.widget.TextView;

import cn.cdl.library.CycleViewPager;


public interface BeautyNewView {

    CycleViewPager getBottomView();

    TextView getCurrentTime();

    CycleViewPager getCycleViewPager();

    TextView getTvDeviceId();

    TextView getTvWifiName();

    void showToast(String paramString);

    void showWaitDialog(boolean paramBoolean);
}
