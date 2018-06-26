package com.mirror.update;


import android.content.Context;
import android.util.Log;

import com.mirror.config.AppConfig;
import com.mirror.config.AppInfo;
import com.mirror.entity.SystemUpdateInfo;
import com.mirror.entity.UpdateInfo;
import com.mirror.http.GetHttpRequestRunnable;
import com.mirror.http.RequestBackListener;
import com.mirror.util.APKUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownStateListener;
import com.mirror.util.down.MirrorDownUtil;
import com.mirror.view.MyToastView;

import org.json.JSONObject;

public class MirrorUpdateUtil {
    private APKUtil apkutil;
    Context context;
    boolean isAutoDown = false;
    MirrorDownUtil mirrorDownUtil;
    MirrorUpdateView updateView;

    public MirrorUpdateUtil(Context context, MirrorUpdateView updateView) {
        this.updateView = updateView;
        this.context = context;
        this.mirrorDownUtil = new MirrorDownUtil(context);
    }

    private void jujleUpdate(UpdateInfo updateInfo) {
        try {
            int webVersion = updateInfo.getAppversion();
            int localVersion = APKUtil.getVersionCode(this.context);
            String apkUrl = updateInfo.getApkurl();
            Log.i("down", "===服务器版本版本===" + webVersion + "/ 本地软件版本==" + localVersion);
            if (webVersion == -1) {
                this.updateView.requestAppUpdate(false, null, "请求失败，请重试");
                return;
            }
            if (webVersion > localVersion) {
                Log.i("down", "===全部设备升级===");
                String savePath = AppInfo.DOWNLOAD_SAVE_APK;
                this.mirrorDownUtil.downFileStart(apkUrl, savePath, "是否下载最新软件", true, new DownStateListener() {
                    public void downStateInfo(DownFileEntity entity) {
                        if (entity.getDownState() == DownFileEntity.DOWN_STATE_SUCCESS) {
                            if (apkutil == null) {
                                apkutil = new APKUtil(context);
                            }
                            apkutil.installApk(entity.getSavePath());
                        }
                    }
                });
                mirrorDownUtil.clickOk();
            }
        } catch (Exception e) {
            updateView.requestAppUpdate(false, null, e.toString());
        }
    }

    private void parserJson(String json) {
        Log.e("HttpGetRunnable", "===json:" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            int appversion = jsonObject.getInt("appversion");
            String updatedesc = jsonObject.getString("updatedesc");
            String webversion = jsonObject.getString("webversion");
            String apkurl = jsonObject.getString("apkurl");
            UpdateInfo updateInfo = new UpdateInfo(appversion, updatedesc, apkurl, webversion);
            Log.e("HttpGetRunnable", "===updateInfo.toString:" + updateInfo.toString());
            if (isAutoDown) {
                jujleUpdate(updateInfo);
                return;
            }
            updateView.requestAppUpdate(true, updateInfo, "检测app成功");
            return;
        } catch (Exception paramString) {
            this.updateView.requestAppUpdate(false, null, paramString.toString());
        }
    }

    private void parserJsonSystem(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String systemcode = jsonObject.getString("systemcode");
            String updatedesc = jsonObject.getString("updatedesc");
            String apkdown = jsonObject.getString("apkdown");
            SystemUpdateInfo systemUpdateInfo = new SystemUpdateInfo(systemcode, updatedesc, apkdown);
            Log.e("HttpGetRunnable", "===============检测系统锁success==" + systemUpdateInfo.toString());
            updateView.requestSystemState(true, systemUpdateInfo, "获取数据成功");
            return;
        } catch (Exception e) {
            updateView.requestSystemState(false, null, e.toString());
        }
    }

    public void checkAppUpdate(boolean isAutoDown) {
        if (!NetWorkUtils.isNetworkConnected(context)) {
            MyToastView.getInstance().Toast(context, "网络异常，请检查");
            return;
        }
        this.isAutoDown = isAutoDown;
        updateView.showWaitDialog(true);
        String checkUpdateUrl = AppInfo.CHECK_APP_UPDARE;
        if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {
            checkUpdateUrl = AppInfo.CHECK_APP_UPDARE_OLD;
        } else if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_TWO) {
            checkUpdateUrl = AppInfo.CHECK_APP_UPDARE;
        }
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(checkUpdateUrl, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                updateView.showWaitDialog(false);
                parserJson(json);
            }

            @Override
            public void requestFailed(String errorDesc) {
                updateView.showWaitDialog(false);
                updateView.requestAppUpdate(false, null, errorDesc);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void cheeckSystemUpdate() {
        if (!NetWorkUtils.isNetworkConnected(context)) {
            MyToastView.getInstance().Toast(context, "当前没有网络");
        }
        updateView.showWaitDialog(true);
        String requestUrl = AppInfo.CHECK_SYSTEM_URL;
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(requestUrl, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                updateView.showWaitDialog(false);
                parserJsonSystem(json);
            }

            @Override
            public void requestFailed(String errorDesc) {
                updateView.showWaitDialog(false);
                updateView.requestSystemState(false, null, errorDesc);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }
}