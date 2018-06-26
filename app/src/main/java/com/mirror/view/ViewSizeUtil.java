package com.mirror.view;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mirror.util.SharedPerManager;

public class ViewSizeUtil {
    private static final int DISTANCE_IMAGE = 50;

    public static void setEshareImageIconSize(ImageView paramImageView) {
        int height = SharedPerManager.getScreenHeight() / 10 - 20;
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) paramImageView.getLayoutParams();
        localLayoutParams.height = height;
        localLayoutParams.width = (height * 2);
        paramImageView.setLayoutParams(localLayoutParams);
    }

    public static void setEsharePopSize(LinearLayout paramLinearLayout) {
        int height = SharedPerManager.getScreenHeight() / 10;
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) paramLinearLayout.getLayoutParams();
        localLayoutParams.height = height;
        paramLinearLayout.setLayoutParams(localLayoutParams);
    }

    public static void setEshareSize(LinearLayout paramLinearLayout) {
        int height = SharedPerManager.getScreenHeight() / 10;
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) paramLinearLayout.getLayoutParams();
        localLayoutParams.height = (height - 10);
        paramLinearLayout.setLayoutParams(localLayoutParams);
    }

    public static void setRelaAdapterMenuSize(RelativeLayout paramRelativeLayout) {
        int height = SharedPerManager.getScreenHeight() * 5 / 7 * 3 / 4 / 2 - 10;
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) paramRelativeLayout.getLayoutParams();
        localLayoutParams.height = height;
        localLayoutParams.width = height;
        paramRelativeLayout.setLayoutParams(localLayoutParams);
    }

    public static void setSkinImageSize(ImageView paramImageView) {
        int height = SharedPerManager.getScreenWidth() * 3 / 10;
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) paramImageView.getLayoutParams();
        localLayoutParams.width = (height - 150);
        localLayoutParams.height = (height - 150);
        paramImageView.setLayoutParams(localLayoutParams);
    }

    public static void setSkinRelaBottomSize(RelativeLayout paramRelativeLayout) {
        int height = SharedPerManager.getScreenWidth() * 3 / 10 * 2 / 3;
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) paramRelativeLayout.getLayoutParams();
        localLayoutParams.width = height;
        localLayoutParams.height = (height * 2 / 5);
        paramRelativeLayout.setLayoutParams(localLayoutParams);
    }

    public static void setSkinRelaSize(RelativeLayout paramRelativeLayout) {
        int height = SharedPerManager.getScreenWidth() * 3 / 10;
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) paramRelativeLayout.getLayoutParams();
        localLayoutParams.width = (height - 100);
        localLayoutParams.height = (height - 100);
        paramRelativeLayout.setLayoutParams(localLayoutParams);
    }

    public static void setTitleViewSize(ImageView paramImageView) {
        int height = SharedPerManager.getScreenHeight() / 10;
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) paramImageView.getLayoutParams();
        localLayoutParams.width = (height * 3);
        localLayoutParams.height = height;
        paramImageView.setLayoutParams(localLayoutParams);
    }

    public static void setVideoRelaLayoutSize(RelativeLayout paramRelativeLayout) {
        int height = SharedPerManager.getScreenHeight() / 10 / 2;
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) paramRelativeLayout.getLayoutParams();
        localLayoutParams.topMargin = height;
        paramRelativeLayout.setLayoutParams(localLayoutParams);
    }
}
