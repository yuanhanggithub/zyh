package com.mirror.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.mirror.R;
import com.mirror.util.image.GlideImageUtil;


public class BackActivityDialog extends BaseActivity implements View.OnClickListener {
    Button btn_cancle;
    Button btn_ok;
    ImageView iv_content;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_back_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initView();
    }

    private void initView() {
        iv_content = ((ImageView) findViewById(R.id.iv_content));
        btn_cancle = ((Button) findViewById(R.id.btn_cancle));
        btn_ok = ((Button) findViewById(R.id.btn_ok));
        btn_ok.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_ok:
                startActivity(new Intent(this, ExitActivity.class));
                finish();
                break;
            case R.id.btn_cancle:
                finish();
                break;
        }
    }


    protected void onResume() {
        super.onResume();
        GlideImageUtil.loadImagePeace(this, "http://cdn.magicmirrormedia.cn/mirrorprojector/appimage/backimage/back_image.png", iv_content, 2130903043);
    }
}
