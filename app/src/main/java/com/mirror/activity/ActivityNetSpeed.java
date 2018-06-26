package com.mirror.activity;

import android.net.ProxyInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownRunnable;
import com.mirror.util.down.DownStateListener;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityNetSpeed extends BaseActivity {
    int currentSpeed = 0;
    DownRunnable downRunnable;
    TextView tv_down_speed;
    TextView tv_speed_up;
    TextView tv_title_net;
    String url = "http://dlied5.myapp.com/myapp/1104466820/sgame/2017_com.tencent.tmgp.sgame_h161_1.32.1.25_335782.apk";

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_speed_net);
        initView();
    }

    private void initView() {
        tv_title_net = (TextView) findViewById(R.id.tv_title_net);
        tv_down_speed = (TextView) findViewById(R.id.tv_down_speed);
        tv_speed_up = (TextView) findViewById(R.id.tv_speed_up);
        handler.sendEmptyMessageDelayed(START_DOWN_FILE, 2000);
        startTimerNetUpdate();
    }

    private void downFile() {
        try {
            File localFile = new File("/sdcard/test.apk");
            if (localFile.exists()) {
                localFile.delete();
            }
            localFile.createNewFile();
            downRunnable = new DownRunnable(this.url, "/sdcard/test.apk", new DownStateListener() {
                public void downStateInfo(DownFileEntity entity) {
                    if (entity.getDownState() == DownFileEntity.DOWN_STATE_SUCCESS) {
                        downFile();
                    }
                    tv_down_speed.setText(entity.getDownSpeed() + "  KB/S");
                    updateNetView(entity.getDownSpeed());
                }
            });
            new Thread(this.downRunnable).start();
        } catch (Exception e) {
            tv_title_net.setText("测试异常: " + e.toString());
        }
    }

    /***
     * 下行网速测试
     * @param paramInt
     */
    private void updateNetView(int paramInt) {
        String netSpeed = "2 M";
        if (paramInt > currentSpeed) {
            currentSpeed = paramInt;
        }
        if (this.currentSpeed < 256) {
            netSpeed = "2 M";
        } else if ((this.currentSpeed > 256) && (this.currentSpeed < 614)) {
            netSpeed = "4 M";
        } else if ((this.currentSpeed > 614) && (this.currentSpeed < 1024)) {
            netSpeed = "8 M";
        } else if ((this.currentSpeed > 1024) && (this.currentSpeed < 2560)) {
            netSpeed = "20 M";
        } else if ((this.currentSpeed > 2560) && (this.currentSpeed < 6400)) {
            netSpeed = "50 M";
        } else if ((this.currentSpeed > 6400) && (this.currentSpeed < 12800)) {
            netSpeed = "100 M";
        }
        tv_title_net.setText("当前测试网速 : ~~" + netSpeed);
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    protected void onStop() {
        super.onStop();
        cacelTimer();
        if (downRunnable != null) {
            downRunnable.stopDown();
        }
    }

    Timer timer;
    TimerTask task;

    public void startTimerNetUpdate() {
        cacelTimer();
        timer = new Timer(true);
        task = new MyTask();
        timer.schedule(task, 1000, 1000);
    }


    public void cacelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(UPDATE_NET_SPEED);
        }
    }

    private static final int START_DOWN_FILE = 0;
    private static final int UPDATE_NET_SPEED = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_NET_SPEED:
                    netUpdateSpeed();
                    break;
                case START_DOWN_FILE:
                    downFile();
                    break;
            }
        }
    };

    long lastNetSize = 0;

    public void netUpdateSpeed() {
        long allSendVolue = TrafficStats.getTotalTxBytes();
        long showSendVoinu = allSendVolue - lastNetSize;
        showSendVoinu = showSendVoinu / 1024;
        String net_update_speed = showSendVoinu + " KB/S";
        tv_speed_up.setText(net_update_speed);
        lastNetSize = allSendVolue;
    }
}
