package com.mirror.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mirror.MirrorApplication;
import com.mirror.R;
import com.mirror.config.AppInfo;
import com.mirror.util.CodeUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SimpleDateUtil;
import com.mirror.util.banner.BannerUtil;
import com.mirror.view.ad.Pos1Bean;
import com.mirror.view.ad.Pos4Entity;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class ScreenViewService extends Service {
    private static final String TAG = "POP";
    Banner banner_bottom_view;
    Banner banner_right_view;
    BannerUtil bannnerBottomUtil;
    BannerUtil bannnerRightUtil;
    LinearLayout lin_time_eshare;
    List<String> lists_bottom;
    List<String> lists_right;
    private RelativeLayout mFloatLayout;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("BootBroadcastReceiver", "===========ScreenViewService=======" + action);
            if (action.equals("BROAD_SCREEN_OPEN")) {
                shoeView();
            } else if (action.equals("BROAD_SCREEN_CLOSE")) {
                hide();
            } else if (action.equals("android.intent.action.TIME_TICK")) {
                updateTimeWifiView();
            } else if (action.equals("com.reeman.receiver.NET_ONLINE")) {
                updateTimeWifiView();
            } else if (action.equals("com.reeman.receiver.NET_DISONLINE")) {
                updateTimeWifiView();
            }
        }
    };
    TextView tv_current_time;
    TextView tv_ip_address;
    TextView tv_wifi_name;

    private void createFloatView() {
        mWmParams = new WindowManager.LayoutParams();
        mWindowManager = ((WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE));
        mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWmParams.format = WindowManager.LayoutParams.LAYOUT_CHANGED;
        mWmParams.flags = WindowManager.LayoutParams.FORMAT_CHANGED;
        mWmParams.gravity = 51;
        mWmParams.x = 0;
        mWmParams.y = 0;
        mWmParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mWmParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mFloatLayout = ((RelativeLayout) LayoutInflater.from(getApplication()).inflate(R.layout.view_screen_pop, null));
        mWindowManager.addView(mFloatLayout, mWmParams);
        intiView(mFloatLayout);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        hide();
    }

    private void initReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("BROAD_SCREEN_CLOSE");
        localIntentFilter.addAction("BROAD_SCREEN_OPEN");
        localIntentFilter.addAction("android.intent.action.TIME_TICK");
        localIntentFilter.addAction(AppInfo.ESAHRE_CLICK_OK);
        registerReceiver(receiver, localIntentFilter);
    }

    private void intiView(RelativeLayout paramRelativeLayout) {
        lists_right = new ArrayList();
        lists_bottom = new ArrayList();
        lin_time_eshare = ((LinearLayout) paramRelativeLayout.findViewById(R.id.lin_time_eshare));
        banner_right_view = ((Banner) paramRelativeLayout.findViewById(R.id.banner_right_view));
        banner_bottom_view = ((Banner) paramRelativeLayout.findViewById(R.id.banner_bottom_view));
        tv_current_time = ((TextView) paramRelativeLayout.findViewById(R.id.tv_show_time));
        tv_wifi_name = ((TextView) paramRelativeLayout.findViewById(R.id.tv_wifi_name));
        tv_ip_address = ((TextView) paramRelativeLayout.findViewById(R.id.tv_ip_address));
    }

    public void hide() {
        try {
            if (mFloatLayout != null) {
                mFloatLayout.setVisibility(View.GONE);
            }
            if (bannnerRightUtil != null) {
                bannnerRightUtil.stopPlay();
            }
            if (bannnerBottomUtil != null) {
                bannnerBottomUtil.stopPlay();
            }
            return;
        } catch (Exception localException) {
        }
    }

    @Nullable
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        createFloatView();
        initReceiver();
    }

    public void shoeView() {
        updateTimeWifiView();
        lin_time_eshare.setVisibility(View.VISIBLE);
        try {
            if (mFloatLayout != null) {
                mFloatLayout.setVisibility(View.VISIBLE);
            }
            List localList;
            int i;
            if (bannnerRightUtil == null) {
                localList = MirrorApplication.getInstance().getInfos();
                i = 0;
                while (i < localList.size()) {
                    lists_right.add(((Pos1Bean) localList.get(i)).getPicpath());
                    i += 1;
                }
                bannnerRightUtil = new BannerUtil(getApplication(), banner_right_view, lists_right);
            }
            bannnerRightUtil.startPlay();
            if (bannnerBottomUtil == null) {
                localList = MirrorApplication.getInstance().getPos_4();
                i = 0;
                while (i < localList.size()) {
                    lists_bottom.add(((Pos4Entity) localList.get(i)).getPicpath());
                    i += 1;
                }
                bannnerBottomUtil = new BannerUtil(getApplication(), banner_bottom_view, lists_bottom);
            }
            bannnerBottomUtil.startPlay();
        } catch (Exception e) {
        }
    }

    public void updateTimeWifiView() {
        try {
            String wifiName = NetWorkUtils.getConnectName(getApplication());
            tv_wifi_name.setText(wifiName);
            String bloothCode = CodeUtil.getBlueToothCode();
            bloothCode = bloothCode.substring(bloothCode.length() - 4, bloothCode.length());
            bloothCode = "尽善镜美-" + bloothCode;
            tv_ip_address.setText(bloothCode);
            String time = SimpleDateUtil.getCurrentDateTime();
            Log.e("BootBroadcastReceiver", "===========取得当前的时间 ： " + time);
            tv_current_time.setText(time);
        } catch (Exception e) {
            Log.e("POP", e.getMessage().toString());
        }
    }
}
