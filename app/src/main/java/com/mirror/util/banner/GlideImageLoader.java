package com.mirror.util.banner;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class GlideImageLoader extends ImageLoader {
    public void displayImage(Context paramContext, Object paramObject, ImageView paramImageView) {
        Glide.with(paramContext)
                .load(paramObject)
                .bitmapTransform(new Transformation[]{new RoundedCornersTransformation(paramContext, 25, 0, RoundedCornersTransformation.CornerType.ALL)})
                .crossFade(500)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(paramImageView);
    }
}
