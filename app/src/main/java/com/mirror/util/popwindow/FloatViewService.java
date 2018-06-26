package com.mirror.util.popwindow;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.mirror.config.AppConfig;

import java.util.Timer;
import java.util.TimerTask;

public class FloatViewService extends Service {
    private static final int DISSMISS_GIF_TIMETASK = 125654;
    private static final int SHOW_GIF_TIMETASK = 125645;
    private static final String TAG = "POP";
    public static final int TIME_SHOW = 8000;
    public static FloatViewService instance;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DISSMISS_GIF_TIMETASK:
                    FloatViewService.this.hideFloat();
                    return;
                case SHOW_GIF_TIMETASK:
                    showFloat();
                    clockDissmiss();
                    break;
            }
        }
    };
    private FloatView mFloatView;
    DissTask taskDismiss;
    ShowTask taskShow;
    Timer timerDismiss;
    Timer timerShow;

    private void clockDisplay() {
        try {
            if (this.timerShow != null) {
                this.timerShow.cancel();
            }
            if (this.taskShow != null) {
                this.taskShow.cancel();
            }
            this.timerShow = new Timer(true);
            this.taskShow = new ShowTask();
            if (AppConfig.isDebug) {
                timerShow.schedule(this.taskShow, 1000, 10000);
            } else {
                this.timerShow.schedule(this.taskShow, 1000, 120000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clockDissmiss() {
        try {
            if (this.timerDismiss != null) {
                this.timerDismiss.cancel();
            }
            if (this.taskDismiss != null) {
                this.taskDismiss.cancel();
            }
            this.timerDismiss = new Timer(true);
            this.taskDismiss = new DissTask();
            this.timerDismiss.schedule(this.taskDismiss, TIME_SHOW);
            return;
        } catch (Exception e) {
        }
    }

    public static FloatViewService getInstance() {
        if (instance == null) {
            synchronized (FloatViewService.class) {
                if (instance == null) {
                    instance = new FloatViewService();
                }
            }
        }
        return instance;
    }

    public void destroyFloat() {
        if (mFloatView != null) {
            mFloatView.destroy();
        }
        mFloatView = null;
    }

    public void hideFloat() {
        if (mFloatView != null) {
            Log.i("POP", "====================隐藏gif图片");
            mFloatView.hide();
            if ((timerShow == null) || (taskShow == null)) {
                clockDisplay();
            }
        }
    }

    public IBinder onBind(Intent paramIntent) {
        return new FloatViewServiceBinder();
    }

    public void onCreate() {
        super.onCreate();
        Log.i("POP", "=====================FloService.start()");
        instance = this;
        mFloatView = new FloatView(this);
        clockDisplay();
    }

    public void onDestroy() {
        super.onDestroy();
        destroyFloat();
        if (timerDismiss != null) {
            timerDismiss.cancel();
        }
        if (taskDismiss != null) {
            taskDismiss.cancel();
        }
        if (timerShow != null) {
            timerShow.cancel();
        }
        if (taskShow != null) {
            taskShow.cancel();
        }
        sendBroadcast(new Intent("com.mirror.service.mirrorService.kill"));
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        return START_STICKY;
    }

    public void showFloat() {
        Log.i("POP", "====================显示gif图片");
        if (mFloatView != null) {
            mFloatView.show();
        }
    }

    class DissTask extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(DISSMISS_GIF_TIMETASK);
        }
    }

    public class FloatViewServiceBinder extends Binder {
        public FloatViewServiceBinder() {
        }

        public FloatViewService getService() {
            return FloatViewService.this;
        }
    }

    class ShowTask extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(SHOW_GIF_TIMETASK);
        }
    }
}
