package com.mirror.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mirror.activity.beautynew.BeautyNewActivity;
import com.mirror.service.MirrorService;
import com.mirror.R;
import com.mirror.activity.main.MainNewParsenter;
import com.mirror.activity.main.MainNewView;
import com.mirror.config.AppInfo;
import com.mirror.service.ScreenViewService;
import com.mirror.update.UpdateActiivty;
import com.mirror.util.FileUtil;
import com.mirror.util.SharedPerManager;
import com.mirror.util.popwindow.FloatViewService;
import com.mirror.util.system.SysPullServer;
import com.mirror.view.ButtonAnimalView;
import com.mirror.view.MyToastView;
import com.mirror.view.ViewSizeUtil;
import com.mirror.view.WaitDialogUtil;
import com.mirror.view.dialog.OridinryDialog;

import cn.cdl.library.CycleViewPager;

public class MainNewActivity extends FragmentActivity implements MainNewView, View.OnClickListener, ButtonAnimalView.ButtomAnimalViewListener {

    TextView tv_current_time;
    TextView tv_ip_address;
    TextView tv_wifi_name;
    WaitDialogUtil waitDialogUtil;
    ImageView iv_eshare_time;
    ImageView iv_eshare_tv;
    ImageView iv_eshare_wifi;
    LinearLayout lin_time_eshare;
    MainNewParsenter mainNewParsener;
    OridinryDialog oridinryDialog;
    public static boolean isForst = false;
    CycleViewPager cycleViewPager;
    CycleViewPager main_bottom_adview;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.TIME_TICK")) {
                mainNewParsener.updateMainViewInfo();
            } else if (action.equals(AppInfo.NET_ONLINE)) {
                mainNewParsener.updateMainViewInfo();
            } else if (action.equals(AppInfo.NET_DISONLINE)) {
                mainNewParsener.updateMainViewInfo();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();
        initButton();
    }

    ButtonAnimalView btn_beauty_news;
    ButtonAnimalView btn_degital;
    ButtonAnimalView btn_video;
    ButtonAnimalView btn_vr_photo;

    private void initButton() {
        btn_video = ((ButtonAnimalView) findViewById(R.id.btn_video));
        btn_degital = ((ButtonAnimalView) findViewById(R.id.btn_degital));
        btn_beauty_news = ((ButtonAnimalView) findViewById(R.id.btn_beauty_news));
        btn_vr_photo = ((ButtonAnimalView) findViewById(R.id.btn_vr_photo));
        btn_video.setOnClickBtnListener(this);
        btn_degital.setOnClickBtnListener(this);
        btn_beauty_news.setOnClickBtnListener(this);
        btn_vr_photo.setOnClickBtnListener(this);
    }

    private void intiView() {
        waitDialogUtil = new WaitDialogUtil(this);
        iv_eshare_time = (ImageView) findViewById(R.id.iv_eshare_time);
        iv_eshare_wifi = (ImageView) findViewById(R.id.iv_eshare_wifi);
        iv_eshare_tv = (ImageView) findViewById(R.id.iv_eshare_tv);
        ViewSizeUtil.setEshareImageIconSize(iv_eshare_time);
        ViewSizeUtil.setEshareImageIconSize(iv_eshare_wifi);
        ViewSizeUtil.setEshareImageIconSize(iv_eshare_tv);
        tv_current_time = ((TextView) findViewById(R.id.tv_show_time));
        tv_wifi_name = ((TextView) findViewById(R.id.tv_wifi_name));
        tv_ip_address = ((TextView) findViewById(R.id.tv_ip_address));
        lin_time_eshare = ((LinearLayout) findViewById(R.id.lin_time_eshare));
        ViewSizeUtil.setEshareSize(lin_time_eshare);
        main_bottom_adview = (CycleViewPager) getFragmentManager().findFragmentById(R.id.main_bottom_adview);
        cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.cycleViewPager);
        mainNewParsener = new MainNewParsenter(MainNewActivity.this, this);
        initReceiver();
        mainNewParsener.getAdTopBottomRight();
        mainNewParsener.checkUpdate();
    }

    @Override
    public void onClick(View view) {
        view.getId();
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        paramBundle.putInt("position", MainNewParsenter.currentFragmentPosition);
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
    public void paperUpdateSystem() {
        if (oridinryDialog == null) {
            oridinryDialog = new OridinryDialog(MainNewActivity.this, SharedPerManager.getScreenWidth(), SharedPerManager.getScreenHeight());
        }
        oridinryDialog.show("当前检测到新系统软件，是否前去升级 ?", "前去升级", "下次在升级");
        oridinryDialog.setOnDialogClickListener(new OridinryDialog.OridinryDialogClick() {
            public void noSure() {
            }

            public void sure() {
                MainNewActivity.this.startActivity(new Intent(MainNewActivity.this, UpdateActiivty.class));
            }
        });
    }


    @Override
    public void showToast(String paramString) {
        MyToastView.getInstance().Toast(this, paramString);
    }

    @Override
    public void showWaitDialog(boolean paramBoolean) {
        if (this.waitDialogUtil == null) {
            this.waitDialogUtil = new WaitDialogUtil(this);
        }
        if (paramBoolean) {
            waitDialogUtil.show("处理中");
        } else {
            waitDialogUtil.dismiss();
        }
    }

    protected void onResume() {
        super.onResume();
        isForst = true;
        startService(new Intent(this, MirrorService.class));
        startService(new Intent(this, FloatViewService.class));
        startService(new Intent(this, ScreenViewService.class));
        Log.e("POP", "回到主界面,隐藏pop");
        FloatViewService.getInstance().hideFloat();
        try {
            mainNewParsener.updateMainViewInfo();
            mainNewParsener.startTimerProject(); //开始屏保计时
            SysPullServer.openEshareServer(this);
            FileUtil.creatDirPathNoExists();
            updateFoucuse();
        } catch (Exception localException) {
        }
    }

    int clickPosition = 0;

    private void updateFoucuse() {
        if (clickPosition == 0) {
            btn_video.requestFocus();
            btn_video.startPropertyAnim();
        } else if (clickPosition == 1) {
            btn_degital.requestFocus();
            btn_degital.startPropertyAnim();
        } else if (clickPosition == 2) {
            btn_beauty_news.requestFocus();
            btn_beauty_news.startPropertyAnim();
        } else if (clickPosition == 3) {
            btn_vr_photo.requestFocus();
            btn_vr_photo.startPropertyAnim();
        }
    }

    protected void onStop() {
        super.onStop();
        isForst = false;
        mainNewParsener.stopAnimalMain();
        mainNewParsener.cacelTimer(); //取消屏保计时
    }

    public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(MainNewActivity.this, BackActivityDialog.class));
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_RIGHT) {
            if (!btn_video.isFocused() && !btn_degital.isFocused() && !btn_beauty_news.isFocused() && !btn_vr_photo.isFocused()) {
                btn_video.requestFocus();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_LEFT) {
            if (!btn_video.isFocused() && !btn_degital.isFocused() && !btn_beauty_news.isFocused() && !btn_vr_photo.isFocused()) {
                btn_vr_photo.requestFocus();
                return true;
            }
        }
        return false;
    }

    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private void initReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.reeman.receiver.NET_DISONLINE");
        localIntentFilter.addAction("android.intent.action.TIME_TICK");
        localIntentFilter.addAction("com.reeman.receiver.NET_ONLINE");
        registerReceiver(receiver, localIntentFilter);
    }


    public void btnAnumalClick(View view) {
        switch (view.getId()) {
            case R.id.btn_video:
                clickPosition = 0;
                startActivity(new Intent(MainNewActivity.this, VideoFunActivity.class));
                return;
            case R.id.btn_degital:
                clickPosition = 1;
                startActivity(new Intent(MainNewActivity.this, DetectionActivity.class));
                return;
            case R.id.btn_beauty_news:
                clickPosition = 2;
                startActivity(new Intent(MainNewActivity.this, BeautyNewActivity.class));
                return;
            case R.id.btn_vr_photo:
                clickPosition = 3;
                startActivity(new Intent(MainNewActivity.this, VrPhotoActivity.class));
                break;
        }
    }
}