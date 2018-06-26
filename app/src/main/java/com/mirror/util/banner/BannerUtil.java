package com.mirror.util.banner;

import android.content.Context;

import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.util.List;

public class BannerUtil {
    Banner banner;
    Context context;

    public BannerUtil(Context paramContext, Banner paramBanner, List<?> paramList) {
        this.context = paramContext;
        this.banner = paramBanner;
        paramBanner.setBannerStyle(2);
        paramBanner.setImageLoader(new GlideImageLoader());
        paramBanner.setBannerAnimation(Transformer.Default);
        paramBanner.setImages(paramList);
        paramBanner.isAutoPlay(true);
        paramBanner.setDelayTime(30 * 1000);
        paramBanner.setIndicatorGravity(7);
        paramBanner.start();
    }

    public void startPlay() {
        try {
            this.banner.startAutoPlay();
            return;
        } catch (Exception localException) {
        }
    }

    public void stopPlay() {
        try {
            this.banner.stopAutoPlay();
            return;
        } catch (Exception localException) {
        }
    }
}
