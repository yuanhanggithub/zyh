package cn.cdl.library.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.cdl.library.CycleViewPager;
import cn.cdl.library.R;
import cn.cdl.library.listener.ViewStateListener;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by jsjm on 2018/3/7.
 */

public class AdView implements CycleViewPager.ImageCycleViewListener {

    private List<ImageView> views = new ArrayList<ImageView>();
    List<String> infos;

    Context context;

    public AdView(Context context) {
        this.context = context;
    }

    @SuppressLint("NewApi")
    public void initAdInfo(CycleViewPager cycleViewPager, List<String> infos) {
        this.infos = infos;
        views.clear();
        // 将最后一个ImageView添加进来
        views.add(getImageView(context, infos.get(infos.size() - 1)));
        for (int i = 0; i < infos.size(); i++) {
            views.add(getImageView(context, infos.get(i)));
        }
        // 将第一个ImageView添加进来
        views.add(getImageView(context, infos.get(0)));
        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, this);
        // 设置轮播
        cycleViewPager.setWheel(true);
        cycleViewPager.setTime(30 * 1000);
        // 设置圆点指示图标组居中显示，默认靠右
//        cycleViewPager.setIndicatorCenter();
    }

    public ImageView getImageView(Context context, String url) {
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);
        Glide.with(context)
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(context, 15, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);

        return imageView;
    }

    @Override
    public void onImageClick(String imageUrl, int postion, View imageView) {
        if (postion >= views.size() - 1) {
            postion = 0;
        }
        postion = postion - 1;
        if (postion < 0) {
            return;
        }
        if (listener != null) {
            listener.onImageClick(imageUrl, postion);
        }
    }

    @Override
    public void onItemSecet(int position) {
        if (views == null) {
            return;
        }
        if (views.size() < 1 || infos.size() < 1) {
            return;
        }
        if (position >= views.size() - 1) {
            position = 0;
        }
        position = position - 1;
        if (position < 0) {
            return;
        }
        if (listener != null) {
            listener.onItemSecet(position);
        }
    }

    ViewStateListener listener = null;

    public void setViewStateChangeListener(ViewStateListener listener) {
        this.listener = listener;
    }
}
