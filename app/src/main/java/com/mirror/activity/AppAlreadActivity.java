package com.mirror.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mirror.R;
import com.mirror.adapter.AppInfosAdapter;
import com.mirror.entity.AppInfomation;
import com.mirror.util.PackgeUtil;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AppAlreadActivity extends BaseActivity {
    private AppInfosAdapter adapter;
    ArrayList<AppInfomation> appList = new ArrayList();
    private GridView gridView;
    WaitDialogUtil waitDialog;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            getData();
        }
    };

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_alread_app);
        initView();
        regReceiver();
    }

    private void initView() {
        waitDialog = new WaitDialogUtil(this);
        waitDialog.show("获取中");
        appList = new ArrayList();
        gridView = (GridView) findViewById(R.id.my_app_gridview);
        gridView.setNumColumns(6);
        adapter = new AppInfosAdapter(this, appList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(onItemClickListener);
    }

    private void getData() {
        waitDialog.show("加载中");
        appList.clear();
        PackgeUtil.getPackage(this, new PackgeUtil.PackageListener() {

            @Override
            public void getSuccess(ArrayList<AppInfomation> lists) {
                waitDialog.dismiss();
                appList = lists;
                adapter.setAppInfos(appList);
                adapter.notifyDataSetChanged();
            }

            public void getFail(String paramAnonymousString) {
                waitDialog.dismiss();
                MyToastView.getInstance().Toast(AppAlreadActivity.this, "获取已安装失败");
            }
        });
    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View view, int position, long paramAnonymousLong) {
            AppInfomation appInfomation = appList.get(position);
            String appName = appInfomation.getAppName();
            String packName = appInfomation.getPackageName();
            byte[] bytes = new byte[0];
            try {
                Drawable drawable  = appInfomation.getDrawable();
                //drawable转化成bitmap的方法
                int w = drawable.getIntrinsicWidth();
                int h = drawable.getIntrinsicHeight();
                Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                Bitmap bitmap = Bitmap.createBitmap(w,h,config);
                //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0,0,w,h);
                drawable.draw(canvas);
                //bitmap转化成byte数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                bytes = baos.toByteArray();
            }catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(AppAlreadActivity.this, DialogApk.class);
//          intent.putExtra("appinfo", appInfomation);
            intent.putExtra("appIcon",bytes);
            intent.putExtra("appName", appName);
            intent.putExtra("packName", packName);
                startActivity(intent);
        }
    };


    private void regReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        localIntentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        localIntentFilter.addAction("android.intent.action.PACKAGE_NEEDS_VERIFICATION");
        localIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        localIntentFilter.addDataScheme("package");
        registerReceiver(receiver, localIntentFilter);
    }


    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        getData();
    }
}
