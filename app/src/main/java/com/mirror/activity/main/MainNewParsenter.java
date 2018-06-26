package com.mirror.activity.main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mirror.MirrorApplication;
import com.mirror.activity.MainNewActivity;
import com.mirror.config.AppConfig;
import com.mirror.config.AppInfo;
import com.mirror.entity.SystemUpdateInfo;
import com.mirror.entity.TopPopEntity;
import com.mirror.entity.UpdateInfo;
import com.mirror.http.GetHttpRequestRunnable;
import com.mirror.http.RequestBackListener;
import com.mirror.update.MirrorUpdateUtil;
import com.mirror.update.MirrorUpdateView;
import com.mirror.util.APKUtil;
import com.mirror.util.CodeUtil;
import com.mirror.util.CurrentRunUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;
import com.mirror.util.SimpleDateUtil;
import com.mirror.util.down.AppDownInstallUtil;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownStateListener;
import com.mirror.util.down.MirrorDownUtil;
import com.mirror.util.eshare.EShareUtil;
import com.mirror.util.parsener.GsonParse;
import com.mirror.view.ad.ADBottomView;
import com.mirror.view.ad.ADInfo;
import com.mirror.view.ad.ADRightView;
import com.mirror.view.ad.PopviewEntity;
import com.mirror.view.ad.Pos1Bean;
import com.mirror.view.ad.Pos4Entity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.cdl.library.CycleViewPager;

public class MainNewParsenter implements MainNewModelListener {

    private static final int SCREEN_ADV = 21;
    private static final String TAG = "MirrorService";
    public static int currentFragmentPosition = 0;
    ADBottomView adBottomView;
    ADRightView adRightView;
    Activity context;
    CycleViewPager cycleViewPager;
    CycleViewPager main_bottom_adview;
    ExecutorService executor = Executors.newFixedThreadPool(10);
    AppDownInstallUtil appDownInstallUtil;

    MainNewView mainNewView;
    TimerTask task;
    private Timer timer;
    TextView tv_current_time;
    TextView tv_ip_address;
    TextView tv_wifi_name;
    MainNewModel mainNewModel;

    public MainNewParsenter(Activity context, MainNewView mainNewView) {
        this.context = context;
        this.mainNewView = mainNewView;
        mainNewModel = new MainNewMidelImpl();
        getView();
    }

    private void getView() {
        main_bottom_adview = mainNewView.getBottomView();
        cycleViewPager = mainNewView.getCycleViewPager();
        tv_wifi_name = mainNewView.getTvWifiName();
        tv_ip_address = mainNewView.getTvDeviceId();
        tv_current_time = mainNewView.getCurrentTime();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCREEN_ADV:
                    try {
                        String currentRunPackageName = APKUtil.appIsRunForset(context);
                        Log.i("MirrorService", "=========当前程序运行包名==" + currentRunPackageName);
                        if (CurrentRunUtil.isRunForstProjectJujle(currentRunPackageName)) {
                            Log.e("MirrorService", "======第三方程序运行在前台，不去屏保");
                            return;
                        }
                        if ((currentRunPackageName.contains("com.ecloud.eairplay")) ||
                                (currentRunPackageName.contains("com.ecloud.emedia"))) {
                            return;
                        }
                        if (currentRunPackageName.contains("com.mirror.videoplayer")) {
                            return;
                        }
                        if (currentRunPackageName.contains(AppInfo.APP_PACKAGE_NEW)
                                || currentRunPackageName.contains(AppInfo.APP_PACKAGE_OLD)) {
                            Log.e("MirrorService", "=====是否在前台==" + MainNewActivity.isForst);
                            if (MainNewActivity.isForst) {
                                jujleGsyApp();
                            }
                        }
                    } catch (Exception e) {
                        Log.e("MirrorService", "===屏保异常===" + e.toString());
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /***
     * 去判断屏保APP在不在
     */
    private void jujleGsyApp() {
        String packageName = AppInfo.GSY_PLAYER_PACKAGENAME;
        if (APKUtil.ApkState(context, packageName)) { //存在，直接打开
            Log.e("MirrorService", "=====魔镜软件已经安装，去打开");
            String action = "com.example.gsyvideoplayer.activity.ScreenProjectorActivity";
            ComponentName componentName = new ComponentName(AppInfo.GSY_PLAYER_PACKAGENAME, action);
            Intent localIntent = new Intent();
            localIntent.setComponent(componentName);
            localIntent.setAction(action);
            context.startActivity(localIntent);
        } else {//不存在，去下载
            Log.e("MirrorService", "=====魔镜软件没有安装，去安装");
            downMirrorPlayer(packageName);
        }
    }

    private void downMirrorPlayer(String packageName) {
        if (appDownInstallUtil == null) {
            appDownInstallUtil = new AppDownInstallUtil(context);
        }
        appDownInstallUtil.downAppInstall(packageName);
    }

    public void getAdTopBottomRight() {
        getAdRightList();
        getBottomPopAdList();
        getTopPopAdList();
    }

    public void checkUpdate() {
        handler.postDelayed(new Runnable() {
            public void run() {
                checkAppUpdate();
                checkSystemUpdate();
            }
        }, 3000L);
    }

    /***
     * 刷新侧边栏广告
     */
    public void updateRightAdInfo() {
        List localList = MirrorApplication.getInstance().getInfos();
        if ((localList != null) && (localList.size() > 0)) {
            if (adRightView == null) {
                adRightView = new ADRightView(context);
            }
            adRightView.setAdInfo(cycleViewPager, localList);
        }
    }

    /***
     * 刷新底部菜单烂广告
     */
    private void updateBottomView() {
        List<Pos4Entity> localList = MirrorApplication.getInstance().getPos_4();
        if ((localList != null) && (localList.size() > 0)) {
            adBottomView = new ADBottomView(context);
            adBottomView.setBottomAdInfo(main_bottom_adview, localList);
        }
    }

    public void checkAppUpdate() {
        if (!SharedPerManager.isOnline()) { //拖网模式不检测软件升级
            return;
        }
        if (!NetWorkUtils.isNetworkConnected(this.context)) {
            mainNewView.showToast("网络异常，检测升级失败");
            return;
        }
        new MirrorUpdateUtil(this.context, new MirrorUpdateView() {
            public void requestAppUpdate(boolean paramAnonymousBoolean, UpdateInfo paramAnonymousUpdateInfo, String paramAnonymousString) {
            }

            public void requestSystemState(boolean paramAnonymousBoolean, SystemUpdateInfo paramAnonymousSystemUpdateInfo, String paramAnonymousString) {
            }

            public void showWaitDialog(boolean paramAnonymousBoolean) {
            }
        }).checkAppUpdate(true);
    }

    public void checkSystemUpdate() {
        if (!SharedPerManager.isOnline()) { //拖网模式不检测系统升级
            return;
        }
        if (AppConfig.isDebug) {
            return;
        }
        if (!NetWorkUtils.isNetworkConnected(this.context)) {
            mainNewView.showToast("网络异常，检测系统升级失败");
            return;
        }
        if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {
            //一代设备不升级
            return;
        }
        MirrorUpdateUtil mirrorUpdateUtil = new MirrorUpdateUtil(context, new MirrorUpdateView() {

            @Override
            public void requestAppUpdate(boolean isTrue, UpdateInfo updateInfo, String errordesc) {

            }

            @Override
            public void requestSystemState(boolean isTrue, SystemUpdateInfo systemUpdateInfo, String errordesc) {
                if (!isTrue) {
                    return;
                }
                String localVersion = Build.VERSION.INCREMENTAL.trim().trim();
                String webVersion = systemUpdateInfo.getSystemcode().trim();
                if (!TextUtils.isEmpty(webVersion) && !localVersion.equals(webVersion)) {
                    mainNewView.paperUpdateSystem();
                }
            }

            @Override
            public void showWaitDialog(boolean isShow) {

            }
        });
        mirrorUpdateUtil.cheeckSystemUpdate();
    }


    /***
     * 获取广告列表
     */

    public void getAdRightList() {
        if (!SharedPerManager.isOnline()) { //脱机模式
            getLocalAdRightImage();
            return;
        }
        mainNewModel.getAdRightImages(context, this);  //在线模式
    }

    /***
     * 网络请求_获取侧边栏广告返回状态
     * @param isTrue
     */
    @Override
    public void getAdRightState(boolean isTrue) {
        if (isTrue) {
            updateRightAdInfo();
        } else {  //网络获取失败，获取本地的广告
            getLocalAdRightImage();
        }
    }

    @Override
    public void getAdTopState(boolean isTrue) {

    }

    @Override
    public void getAdBottomState(boolean isTrue) {
        if (isTrue) {
            updateBottomView();
        } else {
            getLocalBottomAdImage();
        }
    }

    private void getLocalAdRightImage() {
        File file = new File(AppInfo.AD_IMAGE_RIGHT);
        File[] fileList = file.listFiles();
        if (fileList.length < 1) {
            updateRightAdInfo();
            return;
        }
        List<Pos1Bean> listPostBean = new ArrayList<>();
        for (int i = 0; i < fileList.length; i++) {
            listPostBean.add(new Pos1Bean(fileList[i].getPath()));
        }
        MirrorApplication.getInstance().setInfos(listPostBean);
        updateRightAdInfo();
    }

    /***
     * 获取弹屏广告
     */

    public void getTopPopAdList() {
        if (!SharedPerManager.isOnline()) { //拖网模式
            return;
        }  //联网获取弹屏广告
        mainNewModel.getTopRightImages(context, this);
    }

    public void getBottomPopAdList() {
        if (!SharedPerManager.isOnline()) { //拖网模式
            getLocalBottomAdImage();
            return;
        }  //联网获取底部菜单广告
        mainNewModel.getBottomAdImages(context, this);
    }

    /***
     * 获取本地得底部弹屏广告
     */
    private void getLocalBottomAdImage() {
        File file = new File(AppInfo.AD_IMAGE_BOTTOM);
        File[] fileList = file.listFiles();
        Log.e("bottom", "========本地底部广告得个数： " + fileList.length);
        if (fileList.length < 1) {
            Log.e("bottom", "========本地底部广告得个数==0 ");
            updateBottomView();
            return;
        }
        List<Pos4Entity> localList = new ArrayList<>();
        for (int i = 0; i < fileList.length; i++) {
            localList.add(new Pos4Entity(fileList[i].getPath()));
        }
        Log.e("bottom", "========add application == " + localList.size());
        MirrorApplication.getInstance().setPos_4(localList);
        updateBottomView();
    }

    public void startAnimalMain() {
        stopAnimalMain();
    }

    public void stopAnimalMain() {
    }

    //统计进入界面的次数
    int view_first = 0;

    public void updateMainViewInfo() {
        try {
            view_first += 1;
            if (NetWorkUtils.isNetworkConnected(context)) {
                String wifiName = NetWorkUtils.getWifiName(context);
                if (wifiName.length() > 12) {
                    tv_wifi_name.setTextSize(20);
                } else {
                    tv_wifi_name.setTextSize(30);
                }
                tv_wifi_name.setText(wifiName);
            } else {
                tv_wifi_name.setText("网络已经断开");
            }
            String bluthCode = CodeUtil.getBlueToothCode();
            bluthCode = bluthCode.substring(bluthCode.length() - 4, bluthCode.length());
            bluthCode = "尽善镜美-" + bluthCode;
            if (view_first < 2) {
                EShareUtil.setDeviceName(context, bluthCode);
                Intent localIntent = new Intent("com.eshare.action.DEVICE_NAME_CHANGED");
                context.sendBroadcast(localIntent);
            }
            tv_ip_address.setText(bluthCode);
            String currentDateTime = SimpleDateUtil.getCurrentDateTime();
            tv_current_time.setText(currentDateTime);
            startAnimalMain();
        } catch (Exception e) {
            mainNewView.showToast(e.toString());
        }
    }

    public void startTimerProject() {
        cacelTimer();
        Log.i("MirrorService", "===开始屏保程序" + MainNewActivity.isForst);
        timer = new Timer(true);
        task = new MyTask();
        if (AppConfig.isDebug) {
            timer.schedule(task, 10000);
        } else {
            timer.schedule(task, AppConfig.PROJECT_AP_TIME);
        }
    }

    public void cacelTimer() {
        Log.i(TAG, "======quxiao");
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
    }


    private class MyTask extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(SCREEN_ADV);
        }
    }
}
