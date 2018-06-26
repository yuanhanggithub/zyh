package com.mirror.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mirror.R;

public class GlideImageUtil {
    public static void loadADWidthCahe(Context paramContext, String paramString, ImageView paramImageView) {
        Glide.with(paramContext).load(paramString).asBitmap().placeholder(R.mipmap.ad_default).error(R.mipmap.ad_default).into(paramImageView);
    }

    public static void loadGifWidthCahe(Context paramContext, String paramString, ImageView paramImageView) {
        Glide.with(paramContext).load(paramString).asGif().error(R.mipmap.ad_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(paramImageView);
    }

    public static void loadImage(Context paramContext, String paramString, ImageView paramImageView) {
        Glide.with(paramContext).load(paramString).asBitmap().placeholder(R.mipmap.ad_default).error(R.mipmap.ad_default).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(paramImageView);
    }

    public static void loadImagePeace(Context paramContext, String paramString, ImageView paramImageView, int paramInt) {
        Glide.with(paramContext).load(paramString).asBitmap().placeholder(paramInt).error(paramInt).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(paramImageView);
    }

    public static void loadImageWidthCahe(Context paramContext, String paramString, ImageView paramImageView) {
        Glide.with(paramContext).load(paramString).asBitmap().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(paramImageView);
    }

    public static void loadImageWidthCahePlaceLoader(Context paramContext, String paramString, ImageView paramImageView, int paramInt) {
        Glide.with(paramContext).load(paramString).asBitmap().placeholder(paramInt).error(paramInt).diskCacheStrategy(DiskCacheStrategy.ALL).into(paramImageView);
    }

    public static void loadImageWithListener(Context paramContext, String paramString, ImageView paramImageView) {
        RequestListener listener = new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        };
        Glide.with(paramContext)
                .load(paramString)
                .asBitmap()
                .placeholder(R.mipmap.ad_default)
                .error(R.mipmap.ad_default).diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(listener).into(paramImageView);
    }

    public static void preloadImage(Context context, String picPath) {
        try {
            Glide.with(context).load(picPath).diskCacheStrategy(DiskCacheStrategy.SOURCE).preload();
            return;
        } catch (Exception e) {
        }
    }
}
