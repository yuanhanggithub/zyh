package cn.cdl.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

/**
 * Created by jsjm on 2018/5/5.
 */

public class ViewFactory {
    public static ImageView getImageView(Context context, String paramString) {
        ImageView localImageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.view_image_pager, null);
        GlideImageUtil.preloadImage(context, paramString);



        return localImageView;
    }

}
