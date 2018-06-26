package com.mirror.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mirror.R;
import com.mirror.guide.adapter.GuideMobAdapter;
import com.mirror.guide.entity.GuideMobEntity;
import com.mirror.util.rx.RxRecyclerViewDividerTool;
import com.mirror.view.recycle.SWRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GuideMobIntroducefragment extends Fragment {
    List<GuideMobEntity> list_content = new ArrayList();
    SWRecyclerView sw_media_view;
    GuideMobAdapter videoViewAdapter;

    private void addInfo() {
        this.list_content.clear();
        this.list_content.add(new GuideMobEntity("注册入口", R.mipmap.guide_mob_register));
        this.list_content.add(new GuideMobEntity("注册界面", R.mipmap.guide_registe_re));
        this.list_content.add(new GuideMobEntity("登陆界面", R.mipmap.guide_login));
        this.list_content.add(new GuideMobEntity("主界面", R.mipmap.guide_main));
        this.list_content.add(new GuideMobEntity("设备绑定", R.mipmap.guide_bind));
    }

    private void initView(View paramView) {
        this.sw_media_view = ((SWRecyclerView) paramView.findViewById(R.id.sv_guide_view));
        this.videoViewAdapter = new GuideMobAdapter(getActivity(), list_content);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.sw_media_view.setLayoutManager(linearLayoutManager);
        this.sw_media_view.addItemDecoration(new RxRecyclerViewDividerTool(10));
        this.sw_media_view.setAdapter(this.videoViewAdapter);
    }

    public void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        addInfo();
    }

    public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
        if (paramBoolean) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_enter);
        }
        return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_exit);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = View.inflate(getActivity(), R.layout.fragment_mob_introduce, null);
        initView(view);
        return view;
    }
}
