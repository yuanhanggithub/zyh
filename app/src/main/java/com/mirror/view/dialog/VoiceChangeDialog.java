package com.mirror.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mirror.R;

import java.util.Timer;
import java.util.TimerTask;

/***
 * 通用dialog,一句话，两个按钮
 */
public class VoiceChangeDialog {
    private ProgressBar pro_voice;
    private TextView tv_voice;
    private Context context;
    private Dialog dialog;

    public VoiceChangeDialog(Context context, int width, int height) {
        this.context = context;
        dialog = new Dialog(context, R.style.Library_MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        View dialog_view = View.inflate(context, R.layout.toast_voice, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 2, height);
        dialog.setContentView(dialog_view, params);
        dialog.setCancelable(true); // true点击屏幕以外关闭dialog
        pro_voice = (ProgressBar) dialog_view.findViewById(R.id.pro_voice);
        tv_voice = (TextView) dialog_view.findViewById(R.id.tv_voice);
    }

    public void show(int voiceNum) {
        dissmiss();
        voiceNum = voiceNum * 100 / 15;
        pro_voice.setProgress(voiceNum);
        tv_voice.setText(voiceNum + "");
        dialog.show();
        startTimerDissmiss();
    }

    public void dissmiss() {
        if (dialog != null && dialog.isShowing()) {
            cacelTimer();
            dialog.dismiss();
        }
    }


    public void startTimerDissmiss() {
        cacelTimer();
        timer = new Timer(true);
        task = new MyTask();
        timer.schedule(task, 2000);
    }


    public void cacelTimer() {
        if (task != null) {
            task.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    MyTask task;
    Timer timer;

    class MyTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dissmiss();
        }
    };
}
