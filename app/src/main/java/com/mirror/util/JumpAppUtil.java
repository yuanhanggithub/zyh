package com.mirror.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.TableRow;

import com.mirror.service.MirrorService;
import com.mirror.config.AppConfig;
import com.mirror.config.AppInfo;
import com.mirror.util.down.AppDownInstallUtil;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownStateListener;
import com.mirror.util.down.MirrorDownUtil;

public class JumpAppUtil {
    static final String AD_DEFAULT_URL = "AD_DEFAULT_URL";
    static final String PLAY_STATE_TAG = "PLAY_STATE_TAG";
    public static final int TAG_AD = 0;
    static final String VIDEO_ADV_TITLE = "VIDEO_ADV_TITLE";
    static final String VIDEO_DEFAULT_URL = "VIDEO_DEFAULT_URL";
    static final String VIDEO_TITLE = "VIDEO_TITLE";
    AppDownInstallUtil appDownInstallUtil;
    Context context;

    public JumpAppUtil(Context context) {
        this.context = context;
    }


    private void jujleFileVisit(String video_path, String ad_video_path, String video_title, String ad_title, int video_tag) {
        try {
            Intent localIntent = new Intent("android.intent.action.MAIN");
            localIntent.putExtra("VIDEO_DEFAULT_URL", video_path);
            localIntent.putExtra("AD_DEFAULT_URL", ad_video_path);
            localIntent.putExtra("VIDEO_TITLE", video_title);
            localIntent.putExtra("VIDEO_ADV_TITLE", ad_title);
            localIntent.putExtra("PLAY_STATE_TAG", video_tag);
            localIntent.addCategory("android.intent.category.LAUNCHER");
            localIntent.addFlags(268435456);
            localIntent.setComponent(new ComponentName("com.mirror.videoplayer", "com.example.gsyvideoplayer.activity.ShortVideoActivity"));
            this.context.startActivity(localIntent);
            return;
        } catch (Exception e) {
            Log.e("down", "==========跳转视频页面异常====");
        }
    }

    public void jumpShortActivity(int ad_id, int video_id, String video_path, String ad_video_path, String video_title, String ad_title, int video_tag) {
        MirrorService.getInstance().addVideoCountRquest(ad_id, video_id);
        String PLAY_PACKAGE = AppInfo.GSY_PLAYER_PACKAGENAME;
        if (APKUtil.ApkState(context, PLAY_PACKAGE)) {
            jujleFileVisit(video_path, ad_video_path, video_title, ad_title, video_tag);
        } else {
            downMirrorPlayer();
        }
    }

    private void downMirrorPlayer() {
        if (appDownInstallUtil == null) {
            appDownInstallUtil = new AppDownInstallUtil(context);
        }
        appDownInstallUtil.downAppInstall(AppInfo.GSY_PLAYER_PACKAGENAME, true);
    }
}
