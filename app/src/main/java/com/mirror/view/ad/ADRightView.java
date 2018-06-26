package com.mirror.view.ad;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mirror.service.MirrorService;

import java.util.ArrayList;
import java.util.List;

import cn.cdl.library.CycleViewPager;
import cn.cdl.library.ViewFactory;
import cn.cdl.library.listener.ViewStateListener;
import cn.cdl.library.util.AdView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ADRightView implements ViewStateListener {
    private static final String TAG = "adv";
    Context context;
    List<Pos1Bean> infos = null;
    private List<String> picUrls = new ArrayList();
    AdView adView;

    public ADRightView(Context context) {
        this.context = context;
        adView = new AdView(context);
    }

    public void setAdInfo(CycleViewPager cycleViewPager, List<Pos1Bean> infos) {
        Log.e("image", "=========准备显示的图片数量==" + infos.size());
        try {
            this.infos = infos;
            picUrls.clear();
            for (int i = 0; i < infos.size(); i++) {
                picUrls.add(infos.get(i).getPicpath());
            }
            adView.initAdInfo(cycleViewPager, picUrls);
            adView.setViewStateChangeListener(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onImageClick(String info, int postion) {

    }

    @Override
    public void onItemSecet(int position) {
        try {
            Pos1Bean pos1Bean = infos.get(position);
            MirrorService.getInstance().addPicCountRquest(pos1Bean);
//            String picUrl = pos1Bean.getPicpath();
//            Log.e("glideImage", "======文件存在侧边栏图片的位置==" + picUrl);
//            Glide.with(context)
//                    .load(picUrl)
//                    .bitmapTransform(new RoundedCornersTransformation(context, 15, 0, RoundedCornersTransformation.CornerType.ALL))
//                    .into(views.get(position));
        } catch (Exception e) {
            Log.e("glideImage", "catch异常捕捉==" + e.toString());
        }
    }
}
