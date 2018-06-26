package com.mirror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;

import com.cdl.wifi.util.MyLog;
import com.mirror.config.AppConfig;
import com.mirror.service.MirrorService;
import com.mirror.R;
import com.mirror.config.AppInfo;
import com.mirror.util.CursorMediaUtil;
import com.mirror.util.FileUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;
import com.mirror.util.autologin.AutoLoginParsener;
import com.mirror.util.autologin.AutoLoginView;
import com.mirror.util.clear.SdcardManagerUtil;
import com.mirror.util.video.PlayUtilNew;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;
import com.mirror.view.ad.VideoLocaEntity;
import com.mirror.wifi.AutoLineUtil;

import java.util.List;

public class SplashActivity extends BaseActivity implements PlayUtilNew.MyMediaPlayerListener, AutoLoginView {

    SurfaceView sef_view;
    PlayUtilNew playUtilView;
    WaitDialogUtil waitDialogUtil;
    int playNum = 0;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        waitDialogUtil = new WaitDialogUtil(SplashActivity.this);
        waitDialogUtil.show("程序启动中");
        sef_view = findViewById(R.id.sef_view);
        playUtilView = new PlayUtilNew(SplashActivity.this, sef_view);
        playUtilView.setOnMediaPlayListener(this);
        //=========================自动登陆============================
        if (SharedPerManager.isOnline()) {  //在线模式，才登陆设备
            AutoLoginParsener autoLoginParsener = new AutoLoginParsener(this, this);
            autoLoginParsener.autoLoginToWeb();
        } else {
            MyToastView.getInstance().Toast(SplashActivity.this, "当前为拖网模式,广告从本地获取");
        }
        //===============1.5秒后开启播放程序==================================
        Message message = new Message();
        message.what = START_PLAY_START;
        handler.sendMessageDelayed(message, 1500);
    }

    private static final int START_PLAY_START = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_PLAY_START:
                    playDefaultVideo();
                    break;
            }
        }
    };

    public void playDefaultVideo() {
        waitDialogUtil.dismiss();
        playUtilView.playRawSplashFile();
        playNum++;
    }

    /***
     * 播放完成回掉
     */
    @Override
    public void playeronCompletion() {
        if (!SharedPerManager.isOnline()) {  //脱机模式 ，直接去主界面
            startActivityMain();
            return;
        }
        if (SharedPerManager.isLogin()) { //登陆成功，直接去主界面
            startActivityMain();
            return;
        }
        //没有登陆成功
        if (playNum > 1) {
            startActivityMain();
        } else {
            MyToastView.getInstance().Toast(this, "自动登陆失败，再次自动登陆");
            playDefaultVideo();
        }
    }

    public void startActivityMain() {
        startService(new Intent(SplashActivity.this, MirrorService.class));
        startActivity(new Intent(SplashActivity.this, MainNewActivity.class));
        finish();
    }

    @Override
    public void playeronError(String paramString) {

    }

    protected void onResume() {
        super.onResume();
        if (playUtilView != null) {
            playUtilView.onresume();
        }
        autoLineWifi();
        Log.i("ServiceWifi", "===准备去链接新的wifi====");
        FileUtil.deleteDirOrFile(AppInfo.FIREWARE_DOWNLOAD_SAVE_APK);
        FileUtil.deleteDirOrFile(Environment.getExternalStorageDirectory().getPath() + "/update.img");
        clearCahche();
        getVideoImageData();
        writeFileToSd();  //离线模式，先把图片写入到设备SD卡中 ，方便调用
    }

    /**
     * 将默认得图片写入到SD卡
     */
    private void writeFileToSd() {
        MyLog.i("FileUtil", "====onresumr 写入文件到sd卡===");
        FileUtil.creatDirPathNoExists();
        FileUtil.saveRawToSDCard(SplashActivity.this, R.raw.ad_default, AppInfo.DEFAULT_RIGHT_IMAGE_DISONLINE);
        FileUtil.saveRawToSDCard(SplashActivity.this, R.raw.menu_bottom_default, AppInfo.DEFAULT_BOTTOM_IMAGE_DIAONLINE);
    }


    private void clearCahche() {
        double d = SdcardManagerUtil.getSdcardSize().getCurrentSize();
        Log.i("splash", "剩余可用内存==" + d / 1024 + "  kb   /" + SdcardManagerUtil.getFormatSize(d));
        if (d / 1024 < 1048576) {
            Log.e("splash", "内存不够用，清理内存");
            FileUtil.deleteDirOrFile(AppInfo.BASE_LOCAL_URL);
        }
        FileUtil.creatDirPathNoExists();
    }

    private void autoLineWifi() {
        boolean isOnlineType = SharedPerManager.isOnline();
        if (!isOnlineType) {  //脱机模式，不用连接网络
            return;
        }
        if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_TWO) {  //二代设备会自动联网，这里不用了
            return;
        }
        if (!NetWorkUtils.isNetworkConnected(this)) {
            Log.i("ServiceWifi", "===网络未连接，去连接");
            new AutoLineUtil(SplashActivity.this).lineWifi();
            return;
        }
        Log.i("ServiceWifi", "===网络连接成功");
    }


    /***
     * 获取本地视频文件
     */
    private void getVideoImageData() {
        new CursorMediaUtil(this).refreshFileList(AppInfo.FILE_RECEIVER_PATH, new CursorMediaUtil.FileSearchBackListener() {
            public void backError(String paramAnonymousString) {
            }

            public void backFile(List<VideoLocaEntity> paramAnonymousList) {
            }
        });
    }

    protected void onPause() {
        super.onPause();
        if (playUtilView != null) {
            playUtilView.pause();
        }
        if (waitDialogUtil != null) {
            waitDialogUtil.dismiss();
        }
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    @Override
    public void autoLoginSuccess() {
        Log.e("login", "==============登录成功，界面回调");
    }

    @Override
    public void showTaost(String paramString) {
        MyToastView.getInstance().Toast(this, paramString);
    }

    @Override
    public void showWaitDialog(boolean paramBoolean) {

    }
}
