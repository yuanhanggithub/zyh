package com.mirror.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirror.MirrorApplication;
import com.mirror.R;
import com.mirror.adapter.TitleAdapter;
import com.mirror.config.AppInfo;
import com.mirror.entity.CartonAdEntity;
import com.mirror.entity.InnerAppEntity;
import com.mirror.fragment.adapter.VideoFunAdapter;
import com.mirror.fragment.video.VideoFunData;
import com.mirror.parsener.VideoFunParsener;
import com.mirror.util.APKUtil;
import com.mirror.util.CodeUtil;
import com.mirror.util.DisPlayUtil;
import com.mirror.util.JumpAppUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SimpleDateUtil;
import com.mirror.util.popwindow.FloatViewService;
import com.mirror.view.MyToastView;
import com.mirror.view.VideoFunView;
import com.mirror.view.ViewSizeUtil;
import com.mirror.view.WaitDialogUtil;
import com.mirror.view.ad.ADBottomView;
import com.mirror.view.ad.ADRightView;
import com.mirror.view.ad.Pos4Entity;

import java.util.ArrayList;
import java.util.List;

import cn.cdl.library.CycleViewPager;

public class VideoFunActivity extends BaseActivity implements VideoFunView {
    TextView tv_current_time;
    TextView tv_ip_address;
    TextView tv_wifi_name;
    CycleViewPager cycleViewPager;
    CycleViewPager main_bottom_adview;
    VideoFunParsener videoFunParsener;
    private ImageView img_title_icon;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.TIME_TICK")) {
                videoFunParsener.updateMainViewInfo();
            } else if (action.equals(AppInfo.NET_ONLINE)) {
                videoFunParsener.updateMainViewInfo();
            } else if (action.equals(AppInfo.NET_DISONLINE)) {
                videoFunParsener.updateMainViewInfo();
            }
        }
    };

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_video_fun);
        getDataInfo();
        initView();
        initListener();
        initReceiver();
    }

    private void getDataInfo() {
        list_app = VideoFunData.getListDate();
        list_title = VideoFunData.getTitleDate();
    }

    private GridView grid_title_video;
    private List<String> list_title = new ArrayList();
    private List<InnerAppEntity> list_app = new ArrayList();
    private List<InnerAppEntity> list_show = new ArrayList();
    private TitleAdapter titleAdapter;
    private VideoFunAdapter videoTvFunAdapter;
    private WaitDialogUtil waitDialogUtil;
    private GridView grid_video_fun;
    String advId = "52";
    String advTitle = "尽善镜美官方招商视频-媒体篇";
    private String advUrl = "http://cdn.magicmirrormedia.cn/video/d86ae6c331dbe71c34360d6eae748dfc.mp4";
    List<CartonAdEntity.Pos11Entity> cartonAdList = new ArrayList();
    int currentAdPosition = 0;
    private ImageView iv_video_animal;

    private void initView() {
        img_title_icon = ((ImageView) findViewById(R.id.img_title_icon));
        ViewSizeUtil.setTitleViewSize(img_title_icon);
        grid_title_video = ((GridView) findViewById(R.id.grid_title_video));
        grid_title_video.setNumColumns(4);
        titleAdapter = new TitleAdapter(VideoFunActivity.this, list_title);
        grid_title_video.setAdapter(titleAdapter);
        //============================================================================
        iv_video_animal = ((ImageView) findViewById(R.id.iv_video_animal));
        grid_video_fun = (GridView) findViewById(R.id.grid_video_fun);
        grid_video_fun.setNumColumns(4);
        videoTvFunAdapter = new VideoFunAdapter(VideoFunActivity.this, list_show);
        grid_video_fun.setAdapter(videoTvFunAdapter);
        Log.e("grid", "=================设置adapter");
        showCurrentPosition(InnerAppEntity.APP_ALL_TAG);
        //===============================================================================
        tv_current_time = ((TextView) findViewById(R.id.tv_show_time));
        tv_wifi_name = ((TextView) findViewById(R.id.tv_wifi_name));
        tv_ip_address = ((TextView) findViewById(R.id.tv_ip_address));
        main_bottom_adview = (CycleViewPager) getFragmentManager().findFragmentById(R.id.main_bottom_adview);
        cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.cycleViewPager);
        videoFunParsener = new VideoFunParsener(VideoFunActivity.this, this);
        videoFunParsener.getTvAdVideo();  //获取片头广告
    }

    int lastChooicePosition = 0;

    private void initListener() {
        grid_video_fun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                lastChooicePosition = position;
                videoTvFunAdapter.updateCurrentPosition(position);
                clickItem(position);
                selectionPosition(position);
            }
        });

        grid_video_fun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                lastChooicePosition = position;
                selectionPosition(position);
                videoTvFunAdapter.updateCurrentPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //================================================================================================
        grid_title_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View view, int position, long id) {
                showCurrentPosition(position);
                titleAdapter.updateSelectionPoaition(position);
            }
        });
        grid_title_video.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                showCurrentPosition(paramAnonymousInt);
                titleAdapter.updateSelectionPoaition(paramAnonymousInt);
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
    }

    public void clickItem(int paramInt) {
        if (!NetWorkUtils.isNetworkConnected(VideoFunActivity.this)) {
            showToastView("当前无网络，请检查");
            return;
        }
        InnerAppEntity localInnerAppEntity = list_show.get(paramInt);
        if (localInnerAppEntity.getApp_tag() == InnerAppEntity.APP_ADD_TAG) {
            showToastView("改功能暂时未开放，请选择其他直播软件 。");
            return;
        }
        if (cartonAdList.size() < 1) {
            gotoApp(localInnerAppEntity);
        } else {
            openLive(list_show.get(paramInt));
        }
    }

    public void openLive(InnerAppEntity entity) {
        if (APKUtil.ApkState(VideoFunActivity.this, entity.getPackageName())) {
            gotoVideoPlayActivity(entity);
        } else {
            videoFunParsener.toDownApp(entity);
        }
    }

    public void gotoApp(InnerAppEntity entity) {
        String packageName = entity.getPackageName();
        if (APKUtil.ApkState(VideoFunActivity.this, packageName)) {
            DisPlayUtil.startApp(VideoFunActivity.this, packageName);
        } else {
            videoFunParsener.toDownApp(entity);
        }
    }

    JumpAppUtil jumpAppUtil;

    private void gotoVideoPlayActivity(InnerAppEntity innerAppEntity) {
        Log.e("===", "=======需要播放的广告==" + innerAppEntity.toString());
        if (jumpAppUtil == null) {
            jumpAppUtil = new JumpAppUtil(VideoFunActivity.this);
        }
        if (cartonAdList.size() < 1) {
            jumpAppUtil.jumpShortActivity(52, 8, "", advUrl, advTitle, advTitle, list_app.get(lastChooicePosition).getVideoTag());
            return;
        }
        if (currentAdPosition > (cartonAdList.size() - 1)) {
            currentAdPosition = 0;
        }
        CartonAdEntity.Pos11Entity entity = cartonAdList.get(currentAdPosition);
        String videoIId = entity.getId();
        int ad_id = Integer.parseInt(videoIId);
        advUrl = entity.getVideopath();
        advTitle = entity.getTitle();
        jumpAppUtil.jumpShortActivity(ad_id, 8, advUrl, advUrl, advTitle, advTitle, list_app.get(lastChooicePosition).getVideoTag());
        currentAdPosition += 1;
    }

    public void selectionPosition(int paramInt) {
        iv_video_animal.setBackgroundResource(R.drawable.animal_shu_1);
        if ((paramInt > 0) && (paramInt < 4)) {
            iv_video_animal.setBackgroundResource(R.drawable.animal_shu_1);
        } else if ((3 < paramInt) && (paramInt < 8)) {
            iv_video_animal.setBackgroundResource(R.drawable.animal_shu_2);
        } else if (paramInt > 7) {
            iv_video_animal.setBackgroundResource(R.drawable.animal_shu_3);
        }
    }

    private void showToastView(String s) {
        MyToastView.getInstance().Toast(VideoFunActivity.this, s);
    }


    private void showCurrentPosition(int position) {
        list_show.clear();
        for (int i = 0; i < list_app.size(); i++) {
            if (position == InnerAppEntity.APP_ALL_TAG) {
                list_show.add(list_app.get(i));
            } else {
                int appTag = list_app.get(i).getApp_tag();
                if (appTag == position) {
                    list_show.add(list_app.get(i));
                }
            }
        }
        videoTvFunAdapter.setListCurrent(list_show);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatViewService.getInstance().hideFloat();
        videoFunParsener.updateMainViewInfo();
    }

    private void initReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.reeman.receiver.NET_DISONLINE");
        localIntentFilter.addAction("android.intent.action.TIME_TICK");
        localIntentFilter.addAction("com.reeman.receiver.NET_ONLINE");
        registerReceiver(receiver, localIntentFilter);
    }

    public void requestCartonAdState(boolean paramBoolean, CartonAdEntity paramCartonAdEntity, String paramString) {
        if (paramCartonAdEntity != null) {
            cartonAdList = paramCartonAdEntity.getPos_11();
            Log.e("=====", "======获取的动画资源==" + cartonAdList.size());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (grid_video_fun.isFocused()) {
                grid_title_video.requestFocus();
                videoTvFunAdapter.updateCurrentPosition(-1);
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (grid_title_video.isFocused()) {
                grid_video_fun.requestFocus();
                videoTvFunAdapter.updateCurrentPosition(lastChooicePosition);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    //==========================================================================================

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    public TextView getTvTime() {
        return tv_current_time;
    }

    @Override
    public TextView getTvDevId() {
        return tv_ip_address;
    }

    @Override
    public TextView getWifiName() {
        return tv_wifi_name;
    }

    @Override
    public CycleViewPager getRightRecycleView() {
        return cycleViewPager;
    }

    @Override
    public CycleViewPager getBottomRecycleView() {
        return main_bottom_adview;
    }
}
