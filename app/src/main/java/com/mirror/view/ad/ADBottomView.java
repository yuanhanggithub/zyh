package com.mirror.view.ad;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.cdl.library.CycleViewPager;
import cn.cdl.library.listener.ViewStateListener;
import cn.cdl.library.util.AdView;

public class ADBottomView implements ViewStateListener {

    Context context;
    List<Pos4Entity> infos = null;
    private List<String> picPaths = new ArrayList();
    AdView adView;

    public ADBottomView(Context context) {
        this.context = context;
        adView = new AdView(context);
    }

    public void setBottomAdInfo(CycleViewPager cycleViewPager, List<Pos4Entity> infos) {
        try {
            this.infos = infos;
            picPaths.clear();
            for (int i = 0; i < infos.size(); i++) {
                picPaths.add(infos.get(i).getPicpath());
            }
            adView.initAdInfo(cycleViewPager, picPaths);
            adView.setViewStateChangeListener(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onImageClick(String info, int postion) {
        Log.e("main", "========点击的位置=======" + postion + "  /" + info);
    }

    @Override
    public void onItemSecet(int position) {
        Log.e("main", "======dangqian=========" + position);
    }
}
