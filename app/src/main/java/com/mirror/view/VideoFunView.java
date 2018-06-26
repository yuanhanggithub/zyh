package com.mirror.view;

import android.widget.TextView;

import com.mirror.entity.CartonAdEntity;

import cn.cdl.library.CycleViewPager;

/**
 * Created by jsjm on 2018/5/14.
 */

public interface VideoFunView {

    void requestCartonAdState(boolean paramBoolean, CartonAdEntity paramCartonAdEntity, String paramString);

    TextView getTvTime();

    TextView getTvDevId();

    TextView getWifiName();

    CycleViewPager getRightRecycleView();

    CycleViewPager getBottomRecycleView();
}
