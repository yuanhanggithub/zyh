package com.mirror.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.util.FileUtil;

public class GuideMobDownfragment extends Fragment {
    ImageView iv_bind_er_code;
    TextView tv_ercode_desc;


    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = View.inflate(getActivity(), R.layout.fragment_guide_bind, null);
        FileUtil.creatDirPathNoExists();
        initView(view);
        return view;
    }

    private void initView(View paramView) {
        this.tv_ercode_desc = ((TextView) paramView.findViewById(R.id.tv_ercode_desc));
        this.tv_ercode_desc.setText("请使用微信扫一扫\n 下载 《尽善镜美》 ");
        this.iv_bind_er_code = ((ImageView) paramView.findViewById(R.id.iv_bind_er_code));
        this.iv_bind_er_code.setBackgroundResource(R.mipmap.mob_down_url);
    }

    public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
        if (paramBoolean) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_enter);
        }
        return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_exit);
    }


}
