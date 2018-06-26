package com.mirror.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.mirror.activity.MainNewActivity;
import com.mirror.config.AppInfo;
import com.mirror.http.PostRequestRunnable;
import com.mirror.http.RequestBackListener;
import com.mirror.util.APKUtil;
import com.mirror.util.CurrentRunUtil;
import com.mirror.util.SharedPerManager;
import com.mirror.util.VoiceManager;
import com.mirror.util.system.PowerManagerUtil;
import com.mirror.view.ad.Pos1Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MirrorService extends Service {

    public static final String TAG = "MirrorService";
    public static MirrorService instance;
    VoiceManager voiceManager;
    ExecutorService executor = Executors.newFixedThreadPool(10);

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("keycode", "======设备接受到广播===" + action);
            if (action.equals(AppInfo.POWER_OPEN_CLOSE_BROAD)) {
                String lightState = PowerManagerUtil.isOpenLight();
                if (lightState.contains("1")) {
                    PowerManagerUtil.openOrClose(0);
                    MirrorService.this.voiceManager.stopMediaVoice();
                    return;
                } else if (lightState.contains("0")) {
                    PowerManagerUtil.openOrClose(1);
                }
            } else if (action.equals(AppInfo.VOICE_TEST_ADD)) {
                if (voiceManager == null) {
                    voiceManager = new VoiceManager(getApplicationContext());
                }
                voiceManager.addMusicVoice();
            } else if (action.equals(AppInfo.VOICE_TEST_REDUCE)) {
                if (voiceManager == null) {
                    voiceManager = new VoiceManager(getApplicationContext());
                }
                voiceManager.reduceMusicVoice();
            }
        }
    };

    public static MirrorService getInstance() {
        if (instance == null) {
            instance = new MirrorService();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        voiceManager = new VoiceManager(getApplicationContext());
        initReceiver();
    }

    public void addPicCountRquest(Pos1Bean paramPos1Bean) {
        if (!SharedPerManager.isOnline()) { //离线模式不提交
            return;
        }
        if (paramPos1Bean == null) {
            return;
        }
        try {
            String tokn = SharedPerManager.getToken();
            String ad_id = paramPos1Bean.getId();
            RequestBody requestBodyPost = new FormBody.Builder()
                    .add("token", tokn)
                    .add("ad_id", ad_id)
                    .build();
            String requestUrl = AppInfo.ADD_COUNT_PIC_VODIO;
            PostRequestRunnable runnable = new PostRequestRunnable(requestUrl, requestBodyPost, new RequestBackListener() {
                @Override
                public void requestSuccess(String json) {

                }

                @Override
                public void requestFailed(String errorDesc) {

                }
            });
            executor.execute(runnable);
        } catch (Exception e) {
        }
    }

    public void addVideoCountRquest(int ad_id, int video_id) {
        if (!SharedPerManager.isOnline()) { //离线模式不提交
            return;
        }
        if ((ad_id < 0) || (video_id < 0)) {
            return;
        }
        try {
            String token = SharedPerManager.getToken();
            RequestBody requestBodyPost = new FormBody.Builder()
                    .add("token", token)
                    .add("ad_id", ad_id + "")
                    .add("video_id", video_id + "")
                    .build();
            String requestUrl = AppInfo.ADD_COUNT_PIC_VODIO;
            PostRequestRunnable runnable = new PostRequestRunnable(requestUrl, requestBodyPost, new RequestBackListener() {
                @Override
                public void requestSuccess(String json) {

                }

                @Override
                public void requestFailed(String errorDesc) {

                }
            });
            executor.execute(runnable);
        } catch (Exception localException) {
        }
    }


    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("com.mirror.service.mirrorService.kill"));
        SharedPerManager.setLogin(false);
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private void initReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(AppInfo.POWER_OPEN_CLOSE_BROAD);
        localIntentFilter.addAction(AppInfo.VOICE_TEST_ADD);
        localIntentFilter.addAction(AppInfo.VOICE_TEST_REDUCE);
        registerReceiver(receiver, localIntentFilter);
    }
}
