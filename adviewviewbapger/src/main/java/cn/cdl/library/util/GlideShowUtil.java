package cn.cdl.library.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import cn.cdl.library.R;

public class GlideShowUtil {


    /***
     * 图片预加载
     * @param context
     * @param url
     */
    public static void preloadImage(Context context, String url) {
        try {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存网络源文件
                    .preload();   //预加载
        } catch (Exception e) {
        }
    }


    /***
     * 加载图片不加缓存
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()             //这里只允许加载静态图片
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    public static void loadImagePeace(Context context, String url, ImageView imageView, int default_image) {
        Glide.with(context)
                .load(url)
                .asBitmap()             //这里只允许加载静态图片
                .placeholder(default_image)
                .error(default_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }


    /***
     * 图片加载硬盘缓存
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImageWidthCahe(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()             //这里只允许加载静态图片
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    /***
     * 图片加载硬盘缓存，制定默认图片
     * @param context
     * @param url
     * @param imageView
     * @param defaulr_image
     */
    public static void loadImageWidthCahePlaceLoader(Context context, String url, ImageView imageView, int defaulr_image) {
        Glide.with(context)
                .load(url)
                .asBitmap()             //这里只允许加载静态图片
                .placeholder(defaulr_image)
                .error(defaulr_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    public static void loadADWidthCahe(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()             //这里只允许加载静态图片
                .placeholder(R.drawable.ad_default)
                .error(R.drawable.ad_default)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImageWithListener(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()             //这里只允许加载静态图片
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    /***
     * gif图片 加载硬盘缓存
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadGifWidthCahe(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asGif()             //这里只允许加载GIf图片
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
}
