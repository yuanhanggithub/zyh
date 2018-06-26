package com.mirror.fragment.video;

import android.content.Context;
import android.util.Log;

import com.mirror.config.AppInfo;
import com.mirror.entity.ADVideoInfo;
import com.mirror.entity.CartonAdEntity;
import com.mirror.entity.DefaultAdVide;
import com.mirror.entity.InnerAppEntity;
import com.mirror.util.APKUtil;
import com.mirror.util.SharedPerManager;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownStateListener;
import com.mirror.util.down.MirrorDownUtil;

public class TvLiveParsener implements AdFirstView {
    private AdGetParsener adGetParsener;
    APKUtil apkUtil;
    Context context;
    MirrorDownUtil downManagerXutil;
    TvLiveView tvLiveView;

    public TvLiveParsener(Context paramContext, TvLiveView paramTvLiveView) {
        this.tvLiveView = paramTvLiveView;
        this.context = paramContext;
        getView();
    }

    private void getView() {
        this.downManagerXutil = new MirrorDownUtil(this.context);
    }

    public void getDefaultAdState(boolean paramBoolean, DefaultAdVide paramDefaultAdVide, String paramString) {
    }

    public void getTvAdVideo() {
        if (!SharedPerManager.isLogin()) {
            Log.e("adv", "============设备没有登陆，不获取广告");
            return;
        }
        String str = SharedPerManager.getToken();
        str = "adv/list/main_channel_id/4/token/" + str + "/position/" + 11 + "/sub_channel_id/0";
        str = "http://api.magicmirrormedia.cn/mirr/apiv1/" + str;
        Log.e("adv", "===请求的url==" + str);
        adGetParsener = new AdGetParsener(this.context, this);
        adGetParsener.getCartonFiestAdRequest(str);
    }

    public void requestAdState(boolean paramBoolean, ADVideoInfo paramADVideoInfo, String paramString) {
    }

    public void requestCartonAdState(boolean paramBoolean, CartonAdEntity cartonAdEntity, String paramString) {
        tvLiveView.requestCartonAdState(paramBoolean, cartonAdEntity, paramString);
    }

    public void showWaitDialog(boolean isShow) {
        tvLiveView.showWaitDialog(isShow);
    }

    public void toDownApp(InnerAppEntity entity) {
        String doanDesc = "是否下载 <" + entity.getLiveName() + "> 软件";
        String savePath = entity.getDownLoadUrl();
        savePath = savePath.substring(savePath.lastIndexOf("/") + 1);
        savePath = AppInfo.BASE_APK_PATH + "/" + savePath;
        Log.i("url-----", entity.getDownLoadUrl() + "\n" + doanDesc + "\n" + entity.getLiveName());
        if (downManagerXutil == null) {
            downManagerXutil = new MirrorDownUtil(context);
        }
        downManagerXutil.downFileStart(entity.getDownLoadUrl(), savePath, doanDesc, true, new DownStateListener() {
            @Override
            public void downStateInfo(DownFileEntity entity) {
                if (entity.getDownState() == DownFileEntity.DOWN_STATE_SUCCESS) {
                    if (apkUtil == null) {
                        apkUtil = new APKUtil(context);
                    }
                    apkUtil.installApk(entity.getSavePath());
                }
            }
        });
    }
}
