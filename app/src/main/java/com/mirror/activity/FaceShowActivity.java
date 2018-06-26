package com.mirror.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.mirror.R;

/**
 * Created by ZOUYUANHANG on 2018-05-19 .
 */

public class FaceShowActivity extends Activity{
    TextView show_tx;
    Handler mHandler = new Handler();
              Runnable r = new Runnable() {

                    @Override
                    public void run() {
                              //每隔1s循环执行run方法
                        // mHandler.postDelayed(this, 3000);
                        finish();
                        }
              };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faceshow_activity);
        init();
    }

    private void init() {
        show_tx = findViewById(R.id.show_tx);
        Intent intent = getIntent();
        if (intent != null) {
            String value = intent.getStringExtra("test");
            show_tx.setText(value);
        }
        mHandler.postDelayed(r, 3000);
    }
}
