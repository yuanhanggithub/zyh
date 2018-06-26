package com.mirror.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.mirror.activity.beautynew.BeautyNewActivity;
import com.mirror.entity.TectUpdateEntity;
import com.mirror.fragment.adapter.TectUpdateAdapter;
import com.mirror.fragment.util.TectUpdateParsener;
import com.mirror.fragment.view.TectUpdateView;
import com.mirror.util.SharedPerManager;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;

import java.util.ArrayList;
import java.util.List;

public class TectUpdateFragment extends Fragment implements TectUpdateView {
    public static int lastSelectGridPosition = 0;
    TectUpdateAdapter adapter;
    private GridView grid_content;
    List<TectUpdateEntity.TectDetailEntity> list_content = new ArrayList();
    TectUpdateParsener tectUpdateParsener;
    WaitDialogUtil waitDialogUtil;

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

    private void initView(View paramView) {
        waitDialogUtil = new WaitDialogUtil(getActivity());
        tectUpdateParsener = new TectUpdateParsener(getActivity(), this);
        list_content = MirrorApplication.getInstance().getList_tect_update();
        jujleListSizeUpdateView(list_content);
        grid_content = (GridView) paramView.findViewById(R.id.grid_fathion);
        grid_content.setNumColumns(5);
        adapter = new TectUpdateAdapter(getActivity(), list_content);
        grid_content.setAdapter(adapter);
        grid_content.requestFocus();
    }

    private void initListener() {
        grid_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View view, int position, long paramAnonymousLong) {
                lastSelectGridPosition = position;
                adapter.setCurrentFoufus(position);
                ((BeautyNewActivity) getActivity()).setTabSelection(4);
            }
        });
        grid_content.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View view, int position, long paramAnonymousLong) {
                lastSelectGridPosition = position;
                adapter.setCurrentFoufus(position);
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
    }

    private void jujleListSizeUpdateView(List<TectUpdateEntity.TectDetailEntity> paramList) {
        if (!SharedPerManager.isOnline()) {
            showToast("拖网模式不获取内容");
            return;
        }
        if (paramList.size() > 0) {
            return;
        }
        showWaitDialog(true);
        tectUpdateParsener.getTectVideoInfo();
    }

    public void backTectRequestBack(List<TectUpdateEntity.TectDetailEntity> paramList) {
        list_content = paramList;
        adapter.setList(list_content);
    }


    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
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
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (grid_content.isFocused()) {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (grid_content.isFocused()) {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    public void showToast(String paramString) {
        MyToastView.getInstance().Toast(getActivity(), paramString);
    }

    public void showWaitDialog(boolean paramBoolean) {
        if (paramBoolean) {
            waitDialogUtil.show("加载中");
        } else {
            waitDialogUtil.dismiss();
        }
    }
}
