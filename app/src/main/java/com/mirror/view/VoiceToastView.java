package com.mirror.view;


import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mirror.R;

public class VoiceToastView {

    static VoiceToastView instance;

    public static VoiceToastView getInstance() {
        if (instance == null) {
            synchronized (VoiceToastView.class) {
                if (instance == null) {
                    instance = new VoiceToastView();
                }
            }
        }
        return instance;
    }

    Toast toast;
    private TextView tv_voice;
    private ProgressBar pro_voice;

    public void ToastShow(Context context, int voiceNum) {
        if (toast != null) {
            toast.cancel();
        }
        View localView = LayoutInflater.from(context).inflate(R.layout.toast_voice, null);
        voiceNum = voiceNum * 100 / 15;
        pro_voice = ((ProgressBar) localView.findViewById(R.id.pro_voice));
        tv_voice = ((TextView) localView.findViewById(R.id.tv_voice));
        pro_voice.setProgress(voiceNum);
        tv_voice.setText(voiceNum + "");
        toast = Toast.makeText(context, "自定义位置Toast", Toast.LENGTH_SHORT);
        toast.setGravity(50, 0, 50);
        toast.setView(localView);
        toast.show();
    }
}