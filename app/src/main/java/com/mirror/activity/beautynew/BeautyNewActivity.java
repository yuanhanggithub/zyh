package com.mirror.activity.beautynew;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mirror.activity.MainNewActivity;
import com.mirror.service.MirrorService;
import com.mirror.R;
import com.mirror.adapter.TitleAdapter;
import com.mirror.fragment.FationTowdFragment;
import com.mirror.fragment.LocalVideoFragment;
import com.mirror.fragment.SellUpdateFragment;
import com.mirror.fragment.TectDetailFragment;
import com.mirror.fragment.TectUpdateFragment;
import com.mirror.fragment.video.VideoFunData;
import com.mirror.service.ScreenViewService;
import com.mirror.util.popwindow.FloatViewService;
import com.mirror.util.system.SysPullServer;
import com.mirror.view.MyToastView;
import com.mirror.view.ViewSizeUtil;
import com.mirror.view.WaitDialogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.cdl.library.CycleViewPager;

public class BeautyNewActivity extends FragmentActivity implements BeautyNewView {
    public static final int FRAGMENT_FATION_TOWORD = 0;
    public static final int FRAGMENT_TECT_UPDATE = 1;
    public static final int FRAGMENT_SELL_UPDATE = 2;
    public static final int FRAGMENT_LOCAL_VIDEO = 3;
    public static final int FRAGMENT_TECT_DETAIL = 4;

    private static final int GRID_NUM = 4;
    public static int currentFragmentPosition = 0;
    FrameLayout beauty_content;

    FationTowdFragment fashionTowordFragment;
    private android.support.v4.app.FragmentManager fm;
    GridView grid_title_beauty;
    private ImageView img_title_icon;
    ImageView iv_eshare_time;
    ImageView iv_eshare_tv;
    ImageView iv_eshare_wifi;
    LinearLayout lin_time_eshare;
    private List<String> list_title = new ArrayList();
    LocalVideoFragment localVideoFragment;
    BeautyNewParsenter mainNewParsener;
    CycleViewPager main_bottom_adview;
    CycleViewPager cycleViewPager;
    private RelativeLayout rela_video_layout;
    SellUpdateFragment sellUpdateFragment;
    TectDetailFragment tectDetailFragment;
    TectUpdateFragment tectUpdateFragment;
    private TitleAdapter titleAdapter;
    TextView tv_current_time;
    TextView tv_ip_address;
    TextView tv_wifi_name;
    WaitDialogUtil waitDialogUtil;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_beauty_news);
        intiView();
        listener();
    }

    private void intiView() {
        fm = getSupportFragmentManager();
        waitDialogUtil = new WaitDialogUtil(this);
        img_title_icon = (ImageView) findViewById(R.id.img_title_icon);
        ViewSizeUtil.setTitleViewSize(img_title_icon);
        rela_video_layout = ((RelativeLayout) findViewById(R.id.rela_video_layout));
        ViewSizeUtil.setVideoRelaLayoutSize(rela_video_layout);
        iv_eshare_time = ((ImageView) findViewById(R.id.iv_eshare_time));
        iv_eshare_wifi = ((ImageView) findViewById(R.id.iv_eshare_wifi));
        iv_eshare_tv = ((ImageView) findViewById(R.id.iv_eshare_tv));
        ViewSizeUtil.setEshareImageIconSize(iv_eshare_time);
        ViewSizeUtil.setEshareImageIconSize(iv_eshare_wifi);
        ViewSizeUtil.setEshareImageIconSize(iv_eshare_tv);
        tv_current_time = ((TextView) findViewById(R.id.tv_show_time));
        tv_wifi_name = ((TextView) findViewById(R.id.tv_wifi_name));
        tv_ip_address = ((TextView) findViewById(R.id.tv_ip_address));
        lin_time_eshare = ((LinearLayout) findViewById(R.id.lin_time_eshare));
        ViewSizeUtil.setEshareSize(lin_time_eshare);
        main_bottom_adview = ((CycleViewPager) getFragmentManager().findFragmentById(R.id.main_bottom_adview));
        cycleViewPager = ((CycleViewPager) getFragmentManager().findFragmentById(R.id.cycleViewPager));
        beauty_content = ((FrameLayout) findViewById(R.id.fragment_beauty_content));
        grid_title_beauty = ((GridView) findViewById(R.id.grid_title_beauty));
        grid_title_beauty.setNumColumns(GRID_NUM);
        list_title = VideoFunData.getBeautyTitleDate();
        titleAdapter = new TitleAdapter(this, list_title);
        grid_title_beauty.setAdapter(titleAdapter);
        mainNewParsener = new BeautyNewParsenter(this, this);
        setTabSelection(0);
    }

    private void listener() {
        grid_title_beauty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int position, long paramAnonymousLong) {
                setTabSelection(position);
                titleAdapter.updateSelectionPoaition(position);
            }
        });
        grid_title_beauty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int position, long paramAnonymousLong) {
                setTabSelection(position);
                titleAdapter.updateSelectionPoaition(position);
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (currentFragmentPosition == 3) {
            return localVideoFragment.onKeyDown(paramInt, paramKeyEvent);
        }
        if (currentFragmentPosition == 0) {
            return fashionTowordFragment.onKeyDown(paramInt, paramKeyEvent);
        }
        if (currentFragmentPosition == 1) {
            return tectUpdateFragment.onKeyDown(paramInt, paramKeyEvent);
        }
        if (currentFragmentPosition == 4) {
            Log.e("keycode", "=============技术提升点击返回===");
            return tectDetailFragment.onKeyDown(paramInt, paramKeyEvent);
        }
        if (paramInt == 4) {
            finish();
            return true;
        }
        return false;
    }

    protected void onResume() {
        super.onResume();
        try {
            MainNewActivity.isForst = true;
            mainNewParsener.updateMainViewInfo();
            startService(new Intent(this, MirrorService.class));
            startService(new Intent(this, FloatViewService.class));
            startService(new Intent(this, ScreenViewService.class));
            SysPullServer.openEshareServer(this);
        } catch (Exception localException) {
        }
    }

    @SuppressLint({"CommitTransaction"})
    public void setTabSelection(int position) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        currentFragmentPosition = position;
        Log.i("postion", position + "");
        switch (position) {
            case FRAGMENT_FATION_TOWORD:
                if (fashionTowordFragment == null) {
                    fashionTowordFragment = new FationTowdFragment();
                }
                fragmentTransaction.replace(R.id.fragment_beauty_content, fashionTowordFragment);
                break;
            case FRAGMENT_SELL_UPDATE:
                if (sellUpdateFragment == null) {
                    sellUpdateFragment = new SellUpdateFragment();
                }
                fragmentTransaction.replace(R.id.fragment_beauty_content, sellUpdateFragment);
                break;
            case FRAGMENT_TECT_UPDATE:
                if (tectUpdateFragment == null) {
                    tectUpdateFragment = new TectUpdateFragment();
                }
                fragmentTransaction.replace(R.id.fragment_beauty_content, tectUpdateFragment);
                break;
            case FRAGMENT_TECT_DETAIL:
                if (tectDetailFragment == null) {
                    tectDetailFragment = new TectDetailFragment();
                }
                fragmentTransaction.replace(R.id.fragment_beauty_content, tectDetailFragment);
                break;
            case FRAGMENT_LOCAL_VIDEO:
                if (localVideoFragment == null) {
                    localVideoFragment = new LocalVideoFragment();
                }
                fragmentTransaction.replace(R.id.fragment_beauty_content, localVideoFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    public void showToast(String paramString) {
        MyToastView.getInstance().Toast(this, paramString);
    }

    public void showWaitDialog(boolean paramBoolean) {
        if (waitDialogUtil == null) {
            waitDialogUtil = new WaitDialogUtil(this);
        }
        if (paramBoolean) {
            waitDialogUtil.show("处理中");
            return;
        }
        waitDialogUtil.dismiss();
    }

    public CycleViewPager getBottomView() {
        return main_bottom_adview;
    }

    public TextView getCurrentTime() {
        return tv_current_time;
    }

    public CycleViewPager getCycleViewPager() {
        return cycleViewPager;
    }

    public TextView getTvDeviceId() {
        return tv_ip_address;
    }

    public TextView getTvWifiName() {
        return tv_wifi_name;
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainNewActivity.isForst = false;
    }
}
