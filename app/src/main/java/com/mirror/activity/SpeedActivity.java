package com.mirror.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cdl.wifi.util.MyLog;
import com.mirror.R;
import com.mirror.adapter.ProdessRecyAdapter;
import com.mirror.config.AppInfo;
import com.mirror.entity.ProcessInfo;
import com.mirror.listener.CacheClearListener;
import com.mirror.util.Biantai;
import com.mirror.util.FileUtil;
import com.mirror.util.cache.DataCleanManager;
import com.mirror.util.cache.MemoryUtil;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;
import com.mirror.view.recycle.SWRecyclerView;

import java.io.File;
import java.util.ArrayList;

public class SpeedActivity extends BaseActivity implements View.OnClickListener {
    //    private ProcessAdapter mAdapter;
    ProdessRecyAdapter mAdapter;
    public ArrayList<ProcessInfo> mAppList = new ArrayList();
    private Button mBtnClean;
    private MemoryUtil mMemoryUtil;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MyLog.i("main", "=====Sppend接受到广播====" + action);
            if (action.equals(MemoryUtil.MEMORY_CLEAN_PROCESS)) {
                int count = intent.getIntExtra("count", 0);
                String size = intent.getStringExtra("size");
                MyToastView.getInstance().Toast(SpeedActivity.this, "结束 " + count + " 进程,清理内存： " + size);
                mAppList.clear();
                getDate();
                setMemory();
            }
        }
    };

    private TextView tv_aviable_size;
    WaitDialogUtil waitDialogUtil;
    private TextView tv_all_size;
    SWRecyclerView recy_speed;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_speed_cache);
        initView();
        initReceiver();
    }

    private void initView() {
        waitDialogUtil = new WaitDialogUtil(this);
        tv_all_size = (TextView) findViewById(R.id.tv_all_size);
        tv_aviable_size = ((TextView) findViewById(R.id.tv_aviable_size));
        mMemoryUtil = new MemoryUtil(this);
        mBtnClean = ((Button) findViewById(R.id.mBtnClean));
        mBtnClean.setOnClickListener(this);
        recy_speed = (SWRecyclerView) findViewById(R.id.recy_speed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recy_speed.setLayoutManager(layoutManager);
        mAdapter = new ProdessRecyAdapter(this, mAppList);
        recy_speed.setAdapter(mAdapter);
    }

    public void getDate() {
        waitDialogUtil.show("获取文件中...");
        mAppList.clear();
        mMemoryUtil.getRunApp(new CacheClearListener() {
            public void cacheBack(ArrayList<ProcessInfo> lists) {
                waitDialogUtil.dismiss();
                Log.e("main", "==========获取到的内存-==" + lists.size());
                mAppList = lists;
                mAdapter.setProcessList(mAppList);
                if (mAppList.size() == 0) {
                    MyToastView.getInstance().Toast(SpeedActivity.this, "太干净了，没有可清理的进程");
                }
            }
        });
    }

    private void setMemory() {
        String avible_size = MemoryUtil.byte2FitMemorySize(this.mMemoryUtil.getAvailMemorySize(this));
        tv_aviable_size.setText("可用内存：" + avible_size);
        String allSize = mMemoryUtil.byte2FitMemorySize(mMemoryUtil.getTotalMemory(SpeedActivity.this));
        tv_all_size.setText("总内存:" + allSize);
    }

    public void initReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(MemoryUtil.MEMORY_CLEAN_PROCESS);
        registerReceiver(this.receiver, localIntentFilter);
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.mBtnClean:
                if (Biantai.isThreeClick()) {
                    return;
                }
                DataCleanManager.clearAllCache(SpeedActivity.this);
                waitDialogUtil.show("清理中");
                mMemoryUtil.cleanMemory();
                File file = new File(AppInfo.BASE_LOCAL_URL);
                Log.e("clear", "=========文件夹是否存在===" + file.exists());
                if (file.exists()) {
                    FileUtil.deleteDirOrFile(file);
                }
                waitDialogUtil.dismiss();
                break;
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == 4) {
            finish();
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    protected void onResume() {
        super.onResume();
        getDate();
        setMemory();
    }
}
