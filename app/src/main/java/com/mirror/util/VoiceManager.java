package com.mirror.util;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.mirror.view.VoiceToastView;
import com.mirror.view.dialog.VoiceChangeDialog;

/**
 * 音量控制管理器
 */

public class VoiceManager {
    private static final String TAG = "VoiceManager";
    Context context;
    AudioManager mAudioManager;
    VoiceChangeDialog voiceChangeDialog;

    public VoiceManager(Context context) {
        this.context = context;
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        }
        voiceChangeDialog = new VoiceChangeDialog(context, SharedPerManager.getScreenWidth(), SharedPerManager.getScreenHeight());
    }

    public void addMusicVoice() {
        Log.e("keycode", "======= ++音量");
        if (mAudioManager == null) {
            mAudioManager = ((AudioManager) context.getSystemService(Service.AUDIO_SERVICE));
        }
        int maxVoice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVoiceNum = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVoiceNum == maxVoice) {
            currentVoiceNum = maxVoice - 1;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVoiceNum + 1, 0);
//        VoiceToastView.getInstance().ToastShow(context, currentVoiceNum + 1);
        showVoiceDialog(currentVoiceNum + 1);
    }

    public void reduceMusicVoice() {
        Log.e("keycode", "=======  --音量");
        if (mAudioManager == null) {
            mAudioManager = ((AudioManager) context.getSystemService(Service.AUDIO_SERVICE));
        }
        int currentVoiceNum = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVoiceNum == 0) {
            currentVoiceNum = 1;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVoiceNum - 1, 0);
        showVoiceDialog(currentVoiceNum - 1);
//        VoiceToastView.getInstance().ToastShow(context, currentVoiceNum - 1);
    }

    private void showVoiceDialog(int voiceNum) {
        if (voiceChangeDialog == null) {
            voiceChangeDialog = new VoiceChangeDialog(context, SharedPerManager.getScreenWidth(), SharedPerManager.getScreenHeight());
        }
        voiceChangeDialog.show(voiceNum);
    }


    public void setVideoMax() {
        if (mAudioManager == null) {
            mAudioManager = ((AudioManager) context.getSystemService(Service.AUDIO_SERVICE));
        }
        int maxVoice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVoiceNum = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVoiceNum == maxVoice) {
            return;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
    }


    /***
     * 静音
     */
    public void stopMediaVoice() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);  //设置媒体音量为 0
    }

    /***
     * 回复音量
     */
    public void resumeMediaVoice() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 8, 0);  //设置媒体音量为 0
    }

}
