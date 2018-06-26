package com.mirror.test;

import android.os.Bundle;

import com.mirror.R;
import com.mirror.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import cn.cdl.library.CycleViewPager;

public class TestActivity extends BaseActivity {


    private List<String> infos = new ArrayList<String>();
    private CycleViewPager cycleViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initialize();
    }

    private void initialize() {
        infos.add("http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg");
        infos.add("http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg");
        infos.add("http://pic18.nipic.com/20111215/577405_080531548148_2.jpg");
        infos.add("http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg");
        infos.add("http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg");
        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);
        MainUtil mainUtil = new MainUtil(TestActivity.this, cycleViewPager, infos);
    }

}
