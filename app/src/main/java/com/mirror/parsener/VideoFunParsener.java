package com.mirror.parsener;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.mirror.MirrorApplication;
import com.mirror.activity.VideoFunActivity;
import com.mirror.config.AppInfo;
import com.mirror.entity.ADVideoInfo;
import com.mirror.entity.CartonAdEntity;
import com.mirror.entity.DefaultAdVide;
import com.mirror.entity.InnerAppEntity;
import com.mirror.fragment.video.AdFirstView;
import com.mirror.fragment.video.AdGetParsener;
import com.mirror.util.APKUtil;
import com.mirror.util.CodeUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;
import com.mirror.util.SimpleDateUtil;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownStateListener;
import com.mirror.util.down.MirrorDownUtil;
import com.mirror.view.VideoFunView;
import com.mirror.view.ad.ADBottomView;
import com.mirror.view.ad.ADRightView;
import com.mirror.view.ad.Pos4Entity;

import java.util.List;

import cn.cdl.library.CycleViewPager;

/**
 * Created by jsjm on 2018/5/14.
 */

public class VideoFunParsener implements AdFirstView {

    TextView tv_wifi_name;
    TextView tv_ip_address;
    TextView tv_current_time;
    VideoFunView videoFunView;
    Context context;
    CycleViewPager cycleViewPager;
    CycleViewPager main_bottom_adview;

    ADBottomView adbottomView;
    ADRightView adRightView;

    public VideoFunParsener(Context context, VideoFunView videoFunView) {
        this.context = context;
        this.videoFunView = videoFunView;
        getView();
        getRightAdInfo();
        getBottomView();
    }

    private void getView() {
        tv_wifi_name = videoFunView.getWifiName();
        tv_ip_address = videoFunView.getTvDevId();
        tv_current_time = videoFunView.getTvTime();
        cycleViewPager = videoFunView.getRightRecycleView();
        main_bottom_adview = videoFunView.getBottomRecycleView();
    }

    public void getTvAdVideo() {
        if (!SharedPerManager.isLogin()) {
            return;
        }
        String token = SharedPerManager.getToken();
        String requestUrl = "adv/list/main_channel_id/4/token/" + token + "/position/" + 11 + "/sub_channel_id/0";
        requestUrl = "http://api.magicmirrormedia.cn/mirr/apiv1/" + requestUrl;
        Log.e("request", "===请求的url==" + requestUrl);
        AdGetParsener adGetParsener = new AdGetParsener(context, this);
        adGetParsener.getCartonFiestAdRequest(requestUrl);
    }

    MirrorDownUtil downManagerXutil;
    APKUtil apkUtil;
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


    public void getRightAdInfo() {
        List localList = MirrorApplication.getInstance().getInfos();
        adRightView = new ADRightView(context);
        adRightView.setAdInfo(cycleViewPager, localList);
    }

    private void getBottomView() {
        List<Pos4Entity> localList = MirrorApplication.getInstance().getPos_4();
        adbottomView = new ADBottomView(context);
        adbottomView.setBottomAdInfo(main_bottom_adview, localList);
    }


    public void updateMainViewInfo() {
        try {
            if (NetWorkUtils.isNetworkConnected(context)) {
                String wifiName = NetWorkUtils.getWifiName(context);
                if (wifiName.length() > 12) {
                    tv_wifi_name.setTextSize(20);
                } else {
                    tv_wifi_name.setTextSize(30);
                }
                tv_wifi_name.setText(wifiName);
            } else {
                tv_wifi_name.setText("网络已经断开");
            }
            String bluthCode = CodeUtil.getBlueToothCode();
            bluthCode = bluthCode.substring(bluthCode.length() - 4, bluthCode.length());
            bluthCode = "尽善镜美-" + bluthCode;
            tv_ip_address.setText(bluthCode);
            String currentDateTime = SimpleDateUtil.getCurrentDateTime();
            tv_current_time.setText(currentDateTime);
        } catch (Exception e) {
        }
    }


    @Override
    public void getDefaultAdState(boolean isTrue, DefaultAdVide defaultAdVide, String errorDesc) {

    }

    @Override
    public void requestAdState(boolean isTrue, ADVideoInfo adVideoInfo, String errorDesc) {

    }

    @Override
    public void requestCartonAdState(boolean isTrue, CartonAdEntity cartonAdEntity, String errorDesc) {
        videoFunView.requestCartonAdState(isTrue, cartonAdEntity, errorDesc);
    }

    @Override
    public void showWaitDialog(boolean isTrue) {

    }
}
