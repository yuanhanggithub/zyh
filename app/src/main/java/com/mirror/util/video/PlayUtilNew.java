package com.mirror.util.video;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

import com.mirror.R;

import java.io.IOException;

public class PlayUtilNew
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "PlayUtilNew";
    Context context;
    String currentPlayUrl;
    MyMediaPlayerListener listener;
    private MediaPlayer mediaPlayer;
    SurfaceView surfaceView;

    public PlayUtilNew(Context paramContext, SurfaceView paramSurfaceView) {
        this.context = paramContext;
        this.surfaceView = paramSurfaceView;
        initSurfaceView();
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(3);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setDisplay(surfaceView.getHolder());
    }

    private void initSurfaceView() {
        surfaceView.getHolder().setType(3);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder paramAnonymousSurfaceHolder, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            }

            public void surfaceCreated(SurfaceHolder paramAnonymousSurfaceHolder) {
               initMediaPlayer();
            }

            public void surfaceDestroyed(SurfaceHolder paramAnonymousSurfaceHolder) {
                stop();
            }
        });
    }

    public void onCompletion(MediaPlayer paramMediaPlayer) {
         listener.playeronCompletion();
    }

    public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2) {
        Log.i("PlayUtilNew", "=====播放异常:");
        listener.playeronError(currentPlayUrl);
        return false;
    }

    public void onPrepared(MediaPlayer paramMediaPlayer) {
        Log.i("PlayUtilNew", "=====onPrepared");
        paramMediaPlayer.start();
        paramMediaPlayer.seekTo(0);
    }

    public void pause() {
        if (mediaPlayer==null){
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            return;
        }
        mediaPlayer.start();
    }

    public void playLocalFile(String paramString) {
        currentPlayUrl = paramString;
        if (mediaPlayer == null) {
            Toast.makeText(context, "播放异常，请重新进入界面", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(paramString);
            mediaPlayer.prepareAsync();
            mediaPlayer.prepare();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playRawScreenFile() {
        try {
            if (mediaPlayer == null) {
                Toast.makeText(context, "播放异常，请重新进入界面", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            AssetFileDescriptor localAssetFileDescriptor = context.getResources().openRawResourceFd(R.raw.screen_protect);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(localAssetFileDescriptor.getFileDescriptor(), localAssetFileDescriptor.getStartOffset(), localAssetFileDescriptor.getLength());
            mediaPlayer.prepare();
            localAssetFileDescriptor.close();
            return;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    public void playRawSplashFile() {
        if (mediaPlayer == null) {
            Toast.makeText(context, "播放异常，请重新进入界面", Toast.LENGTH_LONG).show();
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        AssetFileDescriptor localAssetFileDescriptor = context.getResources().openRawResourceFd(R.raw.start);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(localAssetFileDescriptor.getFileDescriptor(), localAssetFileDescriptor.getStartOffset(), localAssetFileDescriptor.getLength());
            mediaPlayer.prepare();
            localAssetFileDescriptor.close();
            return;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    public void setOnMediaPlayListener(MyMediaPlayerListener paramMyMediaPlayerListener) {
        listener = paramMyMediaPlayerListener;
    }

    public void stop() {
        try {
            if (mediaPlayer == null) {
                return;
            }
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mediaPlayer.release();
                return;
            }
        } catch (Exception localException) {
        }
    }

    public void onresume() {
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }

    public interface MyMediaPlayerListener {

        void playeronCompletion();

        void playeronError(String paramString);
    }
}
