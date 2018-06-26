package com.mirror.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mirror.R;
import com.mirror.adapter.TitleAdapter;
import com.mirror.adapter.TitleNewAdapter;
import com.mirror.entity.CartonAdEntity;
import com.mirror.entity.InnerAppEntity;
import com.mirror.fragment.adapter.VideoFunAdapter;
import com.mirror.fragment.video.TvLiveParsener;
import com.mirror.fragment.video.TvLiveView;
import com.mirror.fragment.video.VideoFunData;
import com.mirror.util.APKUtil;
import com.mirror.util.DisPlayUtil;
import com.mirror.util.JumpAppUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.rx.RxRecyclerViewDividerTool;
import com.mirror.view.MyToastView;
import com.mirror.view.ViewSizeUtil;
import com.mirror.view.WaitDialogUtil;

import java.util.ArrayList;
import java.util.List;

//VideoFunAdapter.VideoFunItemClickListener,
public class VideoFunFragment extends Fragment implements TvLiveView {
    String advId = "52";
    String advTitle = "尽善镜美官方招商视频-媒体篇";
    private String advUrl = "http://cdn.magicmirrormedia.cn/video/d86ae6c331dbe71c34360d6eae748dfc.mp4";
    List<CartonAdEntity.Pos11Entity> cartonAdList = new ArrayList();
    int currentAdPosition = 0;
    private GridView grid_title_video;
    private ImageView img_title_icon;
    private ImageView iv_video_animal;
    private JumpAppUtil jumpAppUtil;
    private List<InnerAppEntity> list_app = new ArrayList();
    private List<InnerAppEntity> list_show = new ArrayList();
    private List<String> list_title = new ArrayList();
    GridView grid_video_fun;
    private RelativeLayout rela_video_layout;
    private TitleAdapter titleAdapter;
    TvLiveParsener tvLiveParsener;
    private VideoFunAdapter videoTvFunAdapter;
    private WaitDialogUtil waitDialogUtil;

    public void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        list_app = VideoFunData.getListDate();
        list_title = VideoFunData.getTitleDate();
    }

    public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
        if (paramBoolean) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_enter);
        } else {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_exit);
        }
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = View.inflate(getActivity(), R.layout.fragment_video_fun, null);
        initView(view);
        initListener();
        return view;
    }

    private void initView(View paramView) {
        waitDialogUtil = new WaitDialogUtil(getActivity());
        iv_video_animal = ((ImageView) paramView.findViewById(R.id.iv_video_animal));
        grid_title_video = ((GridView) paramView.findViewById(R.id.grid_title_video));
        grid_title_video.setNumColumns(4);
        titleAdapter = new TitleAdapter(getActivity(), list_title);
        grid_title_video.setAdapter(titleAdapter);
        img_title_icon = ((ImageView) paramView.findViewById(R.id.img_title_icon));
        ViewSizeUtil.setTitleViewSize(img_title_icon);
        rela_video_layout = ((RelativeLayout) paramView.findViewById(R.id.rela_video_layout));
        ViewSizeUtil.setVideoRelaLayoutSize(rela_video_layout);
        initRecycle(paramView);
        tvLiveParsener = new TvLiveParsener(getActivity(), this);
        tvLiveParsener.getTvAdVideo();
    }

    private void initRecycle(View view) {
        grid_video_fun = (GridView) view.findViewById(R.id.grid_video_fun);
        grid_video_fun.setNumColumns(4);
        videoTvFunAdapter = new VideoFunAdapter(getContext(), list_show);
        grid_video_fun.setAdapter(videoTvFunAdapter);
        Log.e("grid", "=================设置adapter");
        showCurrentPosition(InnerAppEntity.APP_ALL_TAG);
    }


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
        //==============================================================
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

    public void clickItem(int paramInt) {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            showToastView("当前无网络，请检查");
            return;
        }
        InnerAppEntity localInnerAppEntity = (InnerAppEntity) list_show.get(paramInt);
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

    public void gotoApp(InnerAppEntity entity) {
        String packageName = entity.getPackageName();
        if (APKUtil.ApkState(getActivity(), packageName)) {
            DisPlayUtil.startApp(getActivity(), packageName);
        } else {
            tvLiveParsener.toDownApp(entity);
        }
    }

    private void gotoVideoPlayActivity(InnerAppEntity innerAppEntity) {
        Log.e("===", "=======需要播放的广告==" + innerAppEntity.toString());
        if (jumpAppUtil == null) {
            jumpAppUtil = new JumpAppUtil(getActivity());
        }
        if (cartonAdList.size() < 1) {
            jumpAppUtil.jumpShortActivity(52, 8, "", advUrl, advTitle, advTitle, currentAdPosition);
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
        jumpAppUtil.jumpShortActivity(ad_id, 8, advUrl, advUrl, advTitle, advTitle, currentAdPosition);
        currentAdPosition += 1;
    }

    public void openLive(InnerAppEntity entity) {
        if (APKUtil.ApkState(getActivity(), entity.getPackageName())) {
            gotoVideoPlayActivity(entity);
        } else {
            tvLiveParsener.toDownApp(entity);
        }
    }

    public void requestCartonAdState(boolean paramBoolean, CartonAdEntity paramCartonAdEntity, String paramString) {
        if (paramCartonAdEntity != null) {
            cartonAdList = paramCartonAdEntity.getPos_11();
            Log.e("=====", "======获取的动画资源==" + cartonAdList.size());
        }
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

    public void showToastView(String paramString) {
        MyToastView.getInstance().Toast(getActivity(), paramString);
    }

    public void showWaitDialog(boolean isShow) {
        if (isShow) {
            waitDialogUtil.show("加载中");
        } else {
            waitDialogUtil.dismiss();
        }
    }

    int lastChooicePosition = 0;

    public boolean onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (grid_video_fun.isFocused()) {
                videoTvFunAdapter.updateCurrentPosition(-1);
                grid_title_video.requestFocus();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (grid_title_video.isFocused()) {
                grid_video_fun.requestFocus();
                videoTvFunAdapter.updateCurrentPosition(lastChooicePosition);
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (grid_title_video.isFocused() || grid_video_fun.isFocused())
                return true;
        }
        return false;
    }
}
