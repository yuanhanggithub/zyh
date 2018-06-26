package com.mirror.util.image;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

public class GlideCache implements GlideModule {

    private void setCacheSize(Context context, GlideBuilder paramGlideBuilder) {
        MemorySizeCalculator memorySizeCalculator = new MemorySizeCalculator(context);
        int cacheSize = memorySizeCalculator.getMemoryCacheSize();
        int bitmapPoolSize = memorySizeCalculator.getBitmapPoolSize();
        cacheSize = (int) (cacheSize * 1.2D);
        bitmapPoolSize = (int) (bitmapPoolSize * 1.2D);
        paramGlideBuilder.setMemoryCache(new LruResourceCache(cacheSize));
        paramGlideBuilder.setBitmapPool(new LruBitmapPool(bitmapPoolSize));
    }

    private void setDiskCacheSize(Context context, GlideBuilder paramGlideBuilder) {
        GlideFileUtil.createGlidePath();
        paramGlideBuilder.setDiskCache(new DiskLruCacheFactory(GlideConfig.RIGHT_PATH, 100000000));
        Log.e("==", "========================333333333");
    }

    public void applyOptions(Context paramContext, GlideBuilder paramGlideBuilder) {
        GlideFileUtil.createGlidePath();
        paramGlideBuilder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        setDiskCacheSize(paramContext, paramGlideBuilder);
        setCacheSize(paramContext, paramGlideBuilder);
    }

    public void registerComponents(Context paramContext, Glide paramGlide) {
    }
}
