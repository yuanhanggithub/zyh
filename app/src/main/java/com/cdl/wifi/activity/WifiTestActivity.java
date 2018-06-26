package com.cdl.wifi.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cdl.wifi.db.DBWifiEntity;
import com.cdl.wifi.db.DBWifiUtil;
import com.mirror.R;
import com.mirror.activity.BaseActivity;

import java.util.List;
import java.util.Random;


public class WifiTestActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_test);
        initView();
    }

    Button btn_add, btn_del, btn_modify, btn_search;
    DBWifiUtil dbUtil;

    private void initView() {
        dbUtil = new DBWifiUtil(WifiTestActivity.this);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_modify = (Button) findViewById(R.id.btn_modify);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_add.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_modify.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                int numRandom = new Random().nextInt(100);
                String wifiName = "wifi" + numRandom;
                DBWifiEntity entity = new DBWifiEntity(wifiName, "123456", "123456");
                dbUtil.addWifiInfo(entity);
                dbUtil.queryDbInfo();
                break;
            case R.id.btn_del:
                String name = "wifi72";
                boolean isDel = dbUtil.deleteByName(name);
                Log.e("DBWifiUtil", "======数据删除状态-==" + isDel);
                break;
            case R.id.btn_modify:
                DBWifiEntity entity_modify = new DBWifiEntity("wifi123", "222222", "123456");
                boolean isUpdate = dbUtil.addWifiInfo(entity_modify);
                Log.e("DBWifiUtil", "======数据跟新成功-==" + isUpdate);
                break;
            case R.id.btn_search:
                dbUtil.queryDbInfo();
                break;
        }
    }
}
