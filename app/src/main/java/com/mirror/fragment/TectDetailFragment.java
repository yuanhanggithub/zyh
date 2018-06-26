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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mirror.MirrorApplication;
import com.mirror.R;
import com.mirror.activity.beautynew.BeautyNewActivity;
import com.mirror.entity.TectUpdateEntity;
import com.mirror.fragment.util.PlayStateListener;
import com.mirror.util.JumpAppUtil;
import com.mirror.view.MyToastView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TectDetailFragment
        extends Fragment
        implements View.OnClickListener {
    Button btn_play_next;
    Button btn_play_proe;
    Button btn_play_video;
    TectUpdateEntity.TectDetailEntity currentShowEntity;
    ImageView iv_cover;
    ImageView iv_scan_er_code;
    JumpAppUtil jumpAppUtil;
    List<TectUpdateEntity.TectDetailEntity> list_content = new ArrayList();
    TextView tv_video_introduce;

    public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
        if (paramBoolean) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_enter);
        }
        return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_enter);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = View.inflate(getActivity(), R.layout.fragment_tect_detail, null);
        initView(view);
        return view;
    }

    private void initView(View paramView) {
        jumpAppUtil = new JumpAppUtil(getActivity());
        tv_video_introduce = ((TextView) paramView.findViewById(R.id.tv_video_introduce));
        iv_cover = ((ImageView) paramView.findViewById(R.id.iv_cover));
        iv_scan_er_code = ((ImageView) paramView.findViewById(R.id.iv_scan_er_code));
        btn_play_video = ((Button) paramView.findViewById(R.id.btn_play_video));
        btn_play_proe = ((Button) paramView.findViewById(R.id.btn_play_proe));
        btn_play_next = ((Button) paramView.findViewById(R.id.btn_play_next));
        btn_play_video.setOnClickListener(this);
        btn_play_proe.setOnClickListener(this);
        btn_play_next.setOnClickListener(this);
        btn_play_video.requestFocus();
        list_content = MirrorApplication.getInstance().getList_tect_update();
        currentShowEntity = list_content.get(TectUpdateFragment.lastSelectGridPosition);
        updateViewFragment(currentShowEntity);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_video:
                String playUrl = currentShowEntity.getVideo_url();
                Log.e("cdl", "==================点击播放===" + playUrl);
                String videoTitle = currentShowEntity.getTitle();
                jumpAppUtil.jumpShortActivity(-1, -1, playUrl, "", videoTitle, "默认广告", PlayStateListener.TAG_AD);
                return;
            case R.id.btn_play_proe:
                updateMainView(false);
                return;
            case R.id.btn_play_next:
                updateMainView(true);
                return;
        }
    }


    private void updateMainView(boolean paramBoolean) {
        int currentShowPosition = TectUpdateFragment.lastSelectGridPosition;
        if (list_content.size() < 1) {
            MyToastView.getInstance().Toast(getActivity(), "没有更多了哦");
            return;
        }
        if (paramBoolean) {
            currentShowPosition++;
            if (currentShowPosition > list_content.size() - 1) {
                currentShowPosition = 0;
            }
        } else {
            currentShowPosition--;
            if (currentShowPosition < 0) {
                currentShowPosition = list_content.size() - 1;
            }
        }
        TectUpdateFragment.lastSelectGridPosition = currentShowPosition;
        currentShowEntity = list_content.get(currentShowPosition);
        updateViewFragment(currentShowEntity);
    }

    private void updateViewFragment(TectUpdateEntity.TectDetailEntity entity) {
        Log.e("haha", "===============界面刷新了一次================" + entity.getTitle());
        Glide.with(getActivity()).load(entity.getCover_url()).diskCacheStrategy(DiskCacheStrategy.RESULT).thumbnail(0.5F).priority(Priority.HIGH).placeholder(R.mipmap.ic_launcher).bitmapTransform(new Transformation[]{new RoundedCornersTransformation(getActivity(), 15, 15, RoundedCornersTransformation.CornerType.ALL)}).crossFade(1000).fallback(R.mipmap.ic_launcher).into(iv_cover);
        Glide.with(getActivity()).load(entity.getQr_code_url()).diskCacheStrategy(DiskCacheStrategy.RESULT).thumbnail(0.5F).priority(Priority.HIGH).placeholder(R.mipmap.ic_launcher).bitmapTransform(new Transformation[]{new RoundedCornersTransformation(getActivity(), 15, 15, RoundedCornersTransformation.CornerType.ALL)}).crossFade(1000).fallback(R.mipmap.ic_launcher).into(iv_scan_er_code);
        String introduceDesc = entity.getIntroduction();
        tv_video_introduce.setText(introduceDesc + ".");
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        Log.e("keycode", "=============详细===" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ((BeautyNewActivity) getActivity()).setTabSelection(1);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (btn_play_next.isFocused()) {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (btn_play_video.isFocused()) {
                MyToastView.getInstance().Toast(getActivity(), "请点击返回按钮，返回");
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            return true;
        }
        return false;
    }
}
