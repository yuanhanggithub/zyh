package com.mirror.test;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.cdl.library.CycleViewPager;
import cn.cdl.library.listener.ViewStateListener;
import cn.cdl.library.util.AdView;

public class MainUtil {

    public MainUtil(Context context, CycleViewPager cycleViewPager, List<String> infos) {
        AdView adView = new AdView(context);
        adView.initAdInfo(cycleViewPager, infos);
        adView.setViewStateChangeListener(new ViewStateListener() {
            @Override
            public void onImageClick(String info, int postion) {
                Log.e("main", "========点击的位置=======" + postion + "  /" + info);
            }

            @Override
            public void onItemSecet(int position) {
                Log.e("main", "======dangqian=========" + position);
            }
        });
    }
}
