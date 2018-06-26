package com.mirror.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mirror.MirrorApplication;
import com.mirror.R;
import com.mirror.config.AppInfo;
import com.mirror.entity.ADVideoInfo;
import com.mirror.entity.CartonAdEntity;
import com.mirror.entity.DefaultAdVide;
import com.mirror.entity.VideoEntity;
import com.mirror.fragment.adapter.FationTowordAdapter;
import com.mirror.fragment.parsener.FationTowordParsener;
import com.mirror.fragment.video.AdFirstView;
import com.mirror.fragment.video.AdGetParsener;
import com.mirror.fragment.view.FationTowordView;
import com.mirror.util.JumpAppUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;

import java.util.ArrayList;
import java.util.List;

public class FationTowdFragment extends Fragment implements AdFirstView, FationTowordView {
    private static final String TAG = "AdGetParsener";
    private static int current_play_point = 0;
    AdGetParsener adGetParsener;
    private FationTowordAdapter adapter;
    FationTowordParsener fationTowordParsener;
    GridView grid_content;
    JumpAppUtil jumpAppUtil;
    int lastSelectGridPosition = 0;
    List<VideoEntity> list_content = new ArrayList();
    List<ADVideoInfo.DataEntity.Pos11Entity> lists = new ArrayList();
    private VideoEntity mVideoEntity;
    WaitDialogUtil mWaitDialogUtil;

    public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
        if (paramBoolean) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_enter);
        }
        return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_exit);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = View.inflate(getActivity(), R.layout.fragment_fathion, null);
        initView(view);
        initListener();
        return view;
    }

    private void initView(View view) {
        adGetParsener = new AdGetParsener(getActivity(), this);
        jumpAppUtil = new JumpAppUtil(getActivity());
        mWaitDialogUtil = new WaitDialogUtil(getActivity());
        list_content = MirrorApplication.getInstance().getList_hair();
        Log.e("====", "=======application获取的集合====" + list_content.size());
        jujleListContent(list_content);
        grid_content = (GridView) view.findViewById(R.id.grid_fathion);
        grid_content.setNumColumns(5);
        adapter = new FationTowordAdapter(getActivity(), list_content);
        grid_content.setAdapter(adapter);
        grid_content.requestFocus();
    }


    private void initListener() {
        grid_content.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                lastSelectGridPosition = paramAnonymousInt;
                adapter.setCurrentFoufus(paramAnonymousInt);
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
        grid_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View view, int position, long id_long) {
                lastSelectGridPosition = position;
                adapter.setCurrentFoufus(position);
                if (!NetWorkUtils.isNetworkConnected(getActivity())) {
                    showToast("网络异常，请检查");
                    return;
                }
                showWaitDialog(true);
                VideoEntity entity = list_content.get(position);
                mVideoEntity = entity;
                int subChannelId = entity.getSubchannel_id();
                int id = entity.getId();
                String token = SharedPerManager.getToken();
                String requestUrl = "adv/list/sub_channel_id/" + subChannelId + "/position/" + "11" + "/adv_id/" + id + "/token/" + token;
                requestUrl = "http://api.magicmirrormedia.cn/mirr/apiv1/" + requestUrl;
                Log.i("HairFragment", "====您点击了RecycleView====" + mVideoEntity.toString());
                Log.e("HairFragment", "====您点击了RecycleView====" + paramAnonymousAdapterView);
                adGetParsener.getAdFrirst(requestUrl);
            }
        });
    }

    private void jujleListContent(List<VideoEntity> paramList) {
        if (!SharedPerManager.isOnline()) {
            showToast("脱网模式不获取栏目");
            return;
        }
        if (paramList.size() > 1) {
            return;
        }
        Log.i("=====", "======检车数据是为空,联网查询");
        if (fationTowordParsener == null) {
            fationTowordParsener = new FationTowordParsener(getActivity(), this);
        }
        fationTowordParsener.jujleListContent();
    }

    private void startPlayHairVideo(VideoEntity entity, String ad_video_path, int ad_id, int paramInt2, String ad_title) {
        jumpAppUtil.jumpShortActivity(ad_id, entity.getId(), entity.getVideopath(), ad_video_path, entity.getTitle(), ad_title, 0);
    }

    public void getDefaultAdState(boolean paramBoolean, DefaultAdVide paramDefaultAdVide, String paramString) {
        Log.e("adVideoFirst", "=========界面接受视屏前广告==");
        showWaitDialog(false);
        if (paramBoolean) {
            paramString = paramDefaultAdVide.getAdurl();
            int i = Integer.parseInt(paramDefaultAdVide.getId());
            int j = Integer.parseInt(paramDefaultAdVide.getTimelength());
            startPlayHairVideo(mVideoEntity, paramString, i, j, paramDefaultAdVide.getAdtitle());
            return;
        }
        if (mVideoEntity == null) {
            showToast("错误信息: " + paramString);
            return;
        }
        startPlayHairVideo(mVideoEntity, "http://cdn.magicmirrormedia.cn/video/d86ae6c331dbe71c34360d6eae748dfc.mp4", 0, 15, "尽善镜美官方招商视频-媒体篇");
    }

    public void getVideoFirstAd(ADVideoInfo paramADVideoInfo) {
        showWaitDialog(false);
        lists = paramADVideoInfo.getData().getPos_11();
        if (lists.size() < 1) {
            Log.i("AdGetParsener", "没有播放列表，去请求默认的广告");
            adGetParsener.getDefaultAdInfo("http://cdn.magicmirrormedia.cn/mirrorprojector/interface/tvadinfo.txt");
            return;
        }
        if (current_play_point > lists.size() - 1) {
            current_play_point = 0;
        }
        Log.e("AdGetParsener", "=========界面接受视屏前广告,广告集合得个数==" + lists.size() + "/当前播放==" + current_play_point);
        ADVideoInfo.DataEntity.Pos11Entity poslEntity = lists.get(current_play_point);
        String videoPath = poslEntity.getVideopath();
        int timeLength = Integer.parseInt(poslEntity.getTimelength().trim());
        String videoTitle = poslEntity.getTitle();
        int video_id = Integer.parseInt(poslEntity.getId().trim());
        startPlayHairVideo(mVideoEntity, videoPath, video_id, timeLength, videoTitle);
        current_play_point += 1;
    }


    public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (grid_content.isFocused()) {
                return true;
            }
            grid_content.requestFocus();
            if (list_content.size() > 2) {
                adapter.setCurrentFoufus(lastSelectGridPosition);
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (grid_content.isFocused()) {
                adapter.setCurrentFoufus(-1);
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (grid_content.isFocused()) {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    public void queryVideoInfos(List<VideoEntity> paramList) {
        this.list_content = paramList;
        adapter.setList(list_content);
    }

    public void requestAdState(boolean isSuccess, ADVideoInfo paramADVideoInfo, String paramString) {
        if (isSuccess) {
            getVideoFirstAd(paramADVideoInfo);
        } else {
            adGetParsener.getDefaultAdInfo(AppInfo.DEFAULT_AD_REQUEST);
        }
    }

    public void requestCartonAdState(boolean paramBoolean, CartonAdEntity paramCartonAdEntity, String paramString) {
    }

    public void showToast(String paramString) {
        MyToastView.getInstance().Toast(getActivity(), paramString);
    }

    public void showWaitDialog(boolean paramBoolean) {
        if (paramBoolean) {
            mWaitDialogUtil.show("数据获取中");
        } else {
            mWaitDialogUtil.dismiss();
        }
    }
}
