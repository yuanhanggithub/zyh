package com.mirror.activity.beautynew;

import android.content.Context;
import android.widget.TextView;

import com.mirror.MirrorApplication;
import com.mirror.util.CodeUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SimpleDateUtil;
import com.mirror.view.ad.ADBottomView;
import com.mirror.view.ad.ADRightView;

import java.util.List;

import cn.cdl.library.CycleViewPager;

public class BeautyNewParsenter {
    ADBottomView adBottomView;
    ADRightView adRightView;
    Context context;
    CycleViewPager cycleViewPager;
    BeautyNewView mainNewView;
    CycleViewPager main_bottom_adview;
    TextView tv_current_time;
    TextView tv_ip_address;
    TextView tv_wifi_name;

    public BeautyNewParsenter(Context paramContext, BeautyNewView paramBeautyNewView) {
        context = paramContext;
        mainNewView = paramBeautyNewView;
        getView();
        getRightAdInfo();
        getBottomView();
    }

    private void getBottomView() {
        List localList = MirrorApplication.getInstance().getPos_4();
        if ((localList != null) && (localList.size() > 0)) {
            adBottomView = new ADBottomView(context);
            adBottomView.setBottomAdInfo(main_bottom_adview, localList);
        }
    }

    private void getView() {
        main_bottom_adview = mainNewView.getBottomView();
        cycleViewPager = mainNewView.getCycleViewPager();
        tv_wifi_name = mainNewView.getTvWifiName();
        tv_ip_address = mainNewView.getTvDeviceId();
        tv_current_time = mainNewView.getCurrentTime();
    }

    public void getRightAdInfo() {
        List localList = MirrorApplication.getInstance().getInfos();
        if ((localList != null) && (localList.size() > 0)) {
            if (adRightView == null) {
                adRightView = new ADRightView(context);
            }
            adRightView.setAdInfo(cycleViewPager, localList);
        }
    }

    public void updateMainViewInfo() {
        try {
            String deviceId = CodeUtil.getBlueToothCode();
            deviceId = deviceId.substring(deviceId.length() - 4, deviceId.length());
            deviceId = "尽善镜美-" + deviceId;
            tv_ip_address.setText(deviceId);
            String curentTime = SimpleDateUtil.getCurrentDateTime();
            tv_current_time.setText(curentTime);
            if (NetWorkUtils.isNetworkConnected(context)) {
                String wifiName = NetWorkUtils.getConnectName(context);
                if (wifiName.length() > 12) {
                    tv_wifi_name.setTextSize(20);
                } else {
                    tv_wifi_name.setTextSize(30);
                }
                tv_wifi_name.setText(wifiName);
            } else {
                tv_wifi_name.setText("网络已经断开");
            }
        } catch (Exception e) {
            mainNewView.showToast(e.toString());
        }
    }
}
