package com.mirror.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.config.AppInfo;
import com.mirror.entity.CartonAdEntity;
import com.mirror.parsener.VideoFunParsener;
import com.mirror.view.VideoFunView;

import cn.cdl.library.CycleViewPager;

public class VrPhotoActivity extends BaseActivity implements VideoFunView {
    TextView tv_current_time;
    TextView tv_ip_address;
    TextView tv_wifi_name;
    CycleViewPager cycleViewPager;
    CycleViewPager main_bottom_adview;
    VideoFunParsener videoFunParsener;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.TIME_TICK")) {
                videoFunParsener.updateMainViewInfo();
            } else if (action.equals(AppInfo.NET_ONLINE)) {
                videoFunParsener.updateMainViewInfo();
            } else if (action.equals(AppInfo.NET_DISONLINE)) {
                videoFunParsener.updateMainViewInfo();
            }
        }
    };

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_vr_photo);
        initView();
        initReceiver();
    }

    private void initView() {
        tv_current_time = ((TextView) findViewById(R.id.tv_show_time));
        tv_wifi_name = ((TextView) findViewById(R.id.tv_wifi_name));
        tv_ip_address = ((TextView) findViewById(R.id.tv_ip_address));
        main_bottom_adview = (CycleViewPager) getFragmentManager().findFragmentById(R.id.main_bottom_adview);
        cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.cycleViewPager);
        videoFunParsener = new VideoFunParsener(VrPhotoActivity.this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoFunParsener.updateMainViewInfo();
    }

    private void initReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.reeman.receiver.NET_DISONLINE");
        localIntentFilter.addAction("android.intent.action.TIME_TICK");
        localIntentFilter.addAction("com.reeman.receiver.NET_ONLINE");
        registerReceiver(receiver, localIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    public void requestCartonAdState(boolean paramBoolean, CartonAdEntity paramCartonAdEntity, String paramString) {

    }

    @Override
    public TextView getTvTime() {
        return tv_current_time;
    }

    @Override
    public TextView getTvDevId() {
        return tv_ip_address;
    }

    @Override
    public TextView getWifiName() {
        return tv_wifi_name;
    }

    @Override
    public CycleViewPager getRightRecycleView() {
        return cycleViewPager;
    }

    @Override
    public CycleViewPager getBottomRecycleView() {
        return main_bottom_adview;
    }
}
