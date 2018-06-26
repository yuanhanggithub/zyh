package com.mirror.util.down;

import android.content.Context;
import android.util.Log;

import com.mirror.config.AppConfig;
import com.mirror.config.AppInfo;
import com.mirror.util.APKUtil;

/**
 * 这个类只是用来下载，直接安装流程的app
 */

public class AppDownInstallUtil {

    Context context;
    MirrorDownUtil mirrorDownUtil;
    APKUtil apkUtil;

    public AppDownInstallUtil(Context context) {
        this.context = context;
        mirrorDownUtil = new MirrorDownUtil(context);
        apkUtil = new APKUtil(context);
    }

    /***
     * 不需要用户点击确定
     * @param packageName
     */
    public void downAppInstall(String packageName) {
        downAppInstall(packageName, false);
    }

    /***
     * 重载方法 ，需要用户确定
     * @param packageName
     * @param isNeedClick
     * true 需要用户确定
     * false 不需要用户确定
     */
    public void downAppInstall(String packageName, boolean isNeedClick) {
        String downUrl = "";
        String savePath = "";
        String descTitle = "";
        if (packageName.equals(AppInfo.GSY_PLAYER_PACKAGENAME)) {  //mirrorPlayer 下载安装
            if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {
                downUrl = AppInfo.GSY_DOWN_OLD;
            } else if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_TWO) {
                downUrl = AppInfo.GSY_DOWN_NEW;
            }
            savePath = AppInfo.GSY_SAVE;
            descTitle = "是否下载《播放器》软件 ?";
        } else if (packageName.equals(AppInfo.SOCKET_APK_PACKAGENAME)) { //sock文件传输
            downUrl = AppInfo.SOCKET_FILE_DOWN_URL;
            savePath = AppInfo.SOCKET_SAVE_URL;
            descTitle = "是否下载《闪传》软件 ?";
        }
        toDoDownApp(downUrl, savePath, descTitle, isNeedClick);
    }


    private void toDoDownApp(String downUrl, String savePath, String desc, boolean isNeedClick) {
        mirrorDownUtil.downFileStart(downUrl, savePath, desc, true, new DownStateListener() {
            @Override
            public void downStateInfo(DownFileEntity entity) {
                int state = entity.getDownState();
                if (state == DownFileEntity.DOWN_STATE_SUCCESS) {
                    intstalApp(entity.getSavePath());
                } else {
                    Log.e("down", "===下载失败 ： " + entity.getDesc());
                }
            }
        });
        if (!isNeedClick) {  //不需要用户确定
            mirrorDownUtil.clickOk();
        }
    }

    /***
     * 安装APK方法
     * @param savePath
     */
    private void intstalApp(String savePath) {
        if (apkUtil == null) {
            apkUtil = new APKUtil(context);
        }
        apkUtil.installApk(savePath);
    }
}
