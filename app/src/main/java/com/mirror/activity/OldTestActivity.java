package com.mirror.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;

import com.mirror.R;
import com.mirror.config.AppConfig;
import com.mirror.util.VoiceManager;
import com.mirror.util.video.PlayUtilNew;

public class OldTestActivity extends BaseActivity implements PlayUtilNew.MyMediaPlayerListener {
    public static final String TAG = "SplashActivity";
    private Handler handler = new Handler();
    private PlayUtilNew playUtilNew;
    SurfaceView surfaceView;
    private VoiceManager voiceManager;

    private void initView() {
        surfaceView = ((SurfaceView) findViewById(R.id.surfaceView));
        playUtilNew = new PlayUtilNew(this, this.surfaceView);
        playUtilNew.setOnMediaPlayListener(this);
        pleyRawFile();
        voiceManager = new VoiceManager(this);
        voiceManager.setVideoMax();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_old_test);
        initView();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (playUtilNew != null) {
            playUtilNew.stop();
            playUtilNew = null;
        }
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    @Override
    public void playeronCompletion() {
        pleyRawFile();
    }

    @Override
    public void playeronError(String paramString) {

    }

    public void pleyRawFile() {
        try {
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (playUtilNew == null) {
                        playUtilNew = new PlayUtilNew(OldTestActivity.this, surfaceView);
                    }
                    playUtilNew.playRawScreenFile();
                }
            }, 1000);
        } catch (Exception e) {
            Log.e("SplashActivity", "========播放异常");
        }
    }
}
