package com.mirror.util.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.mirror.service.MirrorService;
import com.mirror.view.ad.Pos1Bean;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.util.List;

public class BannerAdRightUtil implements ViewPager.OnPageChangeListener {
    Banner banner;
    Context context;
    List<?> images;

    public BannerAdRightUtil(Context paramContext, Banner paramBanner, List<?> paramList) {
        this.context = paramContext;
        this.banner = paramBanner;
        this.images = paramList;
        paramBanner.setBannerStyle(2);
        paramBanner.setImageLoader(new GlideImageLoader());
        paramBanner.setBannerAnimation(Transformer.Accordion);
        paramBanner.setImages(paramList);
        paramBanner.isAutoPlay(true);
        paramBanner.setDelayTime(5000);
        paramBanner.setIndicatorGravity(7);
        paramBanner.start();
    }

    public void onPageScrollStateChanged(int paramInt) {
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    }

    public void onPageSelected(int paramInt) {
        try {
            Object localObject = (Pos1Bean) this.images.get(paramInt);
            MirrorService.getInstance().addPicCountRquest((Pos1Bean) localObject);
            localObject = ((Pos1Bean) localObject).getPicpath();
            Log.e("glideImage", "======文件存在侧边栏图片的位置==" + (String) localObject);
            return;
        } catch (Exception localException) {
            Log.e("glideImage", "catch异常捕捉==" + localException.toString());
        }
    }

    public void startPlay() {
        this.banner.startAutoPlay();
    }

    public void stopPlay() {
        this.banner.stopAutoPlay();
    }
}
