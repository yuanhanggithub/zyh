package com.mirror.util.popwindow;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mirror.MirrorApplication;
import com.mirror.R;
import com.mirror.config.AppInfo;
import com.mirror.entity.TopPopEntity;
import com.mirror.util.APKUtil;
import com.mirror.util.CurrentRunUtil;
import com.mirror.util.image.GlideImageUtil;

import java.util.List;

public class FloatView extends FrameLayout {
    private static final int GIF_SHOW_BIG = 0;
    private static final int GIF_SHOW_SMALL = 1;
    private static final String TAG = "POP";
    int currentPosition = 0;
    private Context mContext;
    private ImageView mIvFloatLogo;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams;
    RelativeLayout rela_pop_layout;
    TextView tv_timer_desc;
    View view_pop;

    public FloatView(Context paramContext) {
        super(paramContext);
        this.mContext = paramContext;
        createFloatView(paramContext);
    }

    private void createFloatView(Context mContextt) {
        mWmParams = new WindowManager.LayoutParams();
        mWindowManager = ((WindowManager) mContextt.getSystemService(Context.WINDOW_SERVICE));
        mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWmParams.format = WindowManager.LayoutParams.LAYOUT_CHANGED;
        mWmParams.flags = WindowManager.LayoutParams.FORMAT_CHANGED;
        mWmParams.gravity = 51;
        mWmParams.x = 0;
        mWmParams.y = 0;
        mWmParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mWmParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        view_pop = LayoutInflater.from(mContextt).inflate(R.layout.pj_widget_float_view, null);
        mWindowManager.addView(view_pop, mWmParams);
        tv_timer_desc = ((TextView) view_pop.findViewById(R.id.tv_timer_desc));
        mIvFloatLogo = ((ImageView) view_pop.findViewById(R.id.mIvFloatLogo));
        mIvFloatLogo.setScaleType(ImageView.ScaleType.FIT_XY);
        rela_pop_layout = ((RelativeLayout) view_pop.findViewById(R.id.rela_pop_layout));
        view_pop.measure(View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        hide();
    }

    private void removeFloatView() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImageAsGIf() {
        tv_timer_desc.setText("5");
        List localList = MirrorApplication.getInstance().getList_pop();
        if (currentPosition > localList.size() - 1) {
            currentPosition = 0;
        }
        String str = ((TopPopEntity.Pos13Entity) localList.get(currentPosition)).getPicpath();
        Log.i("POP", "======加载的位置====" + currentPosition + "/" + localList.size());
        Log.i("POP", "需要加载的图片url=" + str);
        if ((str.endsWith(".png")) || (str.endsWith(".jpg"))) {
            if (mIvFloatLogo != null) {
                Log.i("POP", "加载图片去了=");
                Glide.with(mContext).load(str).crossFade(1500).diskCacheStrategy(DiskCacheStrategy.ALL).into(mIvFloatLogo);
            } else {
                currentPosition += 1;
                return;
            }
        }
        if ((str.endsWith(".gif")) && (mIvFloatLogo != null)) {
            Log.i("POP", "加载GIF图片去了=");
            GlideImageUtil.loadGifWidthCahe(mContext, str, mIvFloatLogo);
        }
    }

    public void destroy() {
        hide();
        removeFloatView();
    }

    public void hide() {
        if (rela_pop_layout != null) {
            Log.e("POP", "=========隐藏POP最终执行====");
            rela_pop_layout.setVisibility(View.GONE);
        }
    }

    public void show() {
        String packageName = APKUtil.appIsRunForset(MirrorApplication.getInstance());
        Log.e("POP", "=========当前运行前台的包名====" + packageName);
        if (CurrentRunUtil.isRunForstPopJujle(packageName)) {
            Log.e("POP", "=========第三方APK在运行显示大图====" + packageName);
            showImagelayoutParams(GIF_SHOW_BIG);
        } else if (packageName.contains("com.mirror.videoplayer")) {
            Log.e("POP", "=========ShortVideoPlayer显示小图====");
        } else if (packageName.contains(AppInfo.APP_PACKAGE_NEW)
                || packageName.contains(AppInfo.APP_PACKAGE_OLD)) {
            Log.e("POP", "========系统运行在前台，不现实弹屏广告====");
        }
    }

    public void showImagelayoutParams(int paramInt) {
        if (rela_pop_layout == null) {
            Log.i("POP", "控件为null，不显示了");
            return;
        }
        rela_pop_layout.setVisibility(View.VISIBLE);
        Log.i("POP", "==========加载显示控件=====");
        showImageAsGIf();
    }
}
