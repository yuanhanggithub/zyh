package com.mirror.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.mirror.R;
import com.mirror.util.anim.AnimalUtil;
import com.mirror.view.GlideCircleTransform;
import com.mirror.view.ViewSizeUtil;

public class DetectionActivity extends BaseActivity {
    AnimalUtil animalUtil;
    private ImageView iv_scan_skin;
    private ImageView iv_skin_image;
    private RelativeLayout rela_animal_layout;
    private RelativeLayout rela_detetion_bottom_layout;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_detect);
        initView();
    }

    private void initView() {
        animalUtil = new AnimalUtil(this);
        iv_scan_skin = ((ImageView) findViewById(R.id.iv_scan_skin));
        animalUtil.startTranslateAnimation(iv_scan_skin);
        rela_detetion_bottom_layout = ((RelativeLayout) findViewById(R.id.rela_detetion_bottom_layout));
        ViewSizeUtil.setSkinRelaBottomSize(rela_detetion_bottom_layout);
        rela_animal_layout = ((RelativeLayout) findViewById(R.id.rela_animal_layout));
        ViewSizeUtil.setSkinRelaSize(rela_animal_layout);
        iv_skin_image = ((ImageView) findViewById(R.id.iv_skin_image));
        ViewSizeUtil.setSkinImageSize(iv_skin_image);
        Glide.with(this)
                .load("http://am.zdmimg.com/201308/11/5206f61f001a0.jpg_e600.jpg")
                .transform(new BitmapTransformation[]{new GlideCircleTransform(this)}).into(iv_skin_image);
    }

    protected void onDestroy() {
        super.onDestroy();
        animalUtil.stopAnimal();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }
}
