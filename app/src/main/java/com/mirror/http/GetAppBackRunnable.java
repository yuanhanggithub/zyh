package com.mirror.http;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.cdl.wifi.util.MyLog;
import com.mirror.entity.ProcessInfo;
import com.mirror.listener.CacheClearListener;
import com.mirror.util.cache.MemoryUtil;

import java.util.ArrayList;
import java.util.List;

public class GetAppBackRunnable implements Runnable {

    private static final String TAG = "GetAppBackRunnable";
    Context context;
    CacheClearListener listener;
    private Handler handler = new Handler();
    private ActivityManager am;
    ArrayList<ProcessInfo> appList = new ArrayList<ProcessInfo>();

    public GetAppBackRunnable(Context context, CacheClearListener listener) {
        this.context = context;
        this.listener = listener;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public void run() {
        appList.clear();
        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        if (infoList != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppInfo : infoList) {
                String pgName = runningAppInfo.pkgList[0];
                if (runningAppInfo.pid == android.os.Process.myPid()
                        || runningAppInfo.processName.contains("com.android")
                        || runningAppInfo.processName.equals("system")
                        || pgName.equals("android")
                        || runningAppInfo.processName.equals("com.cghs.stresstest")) // 压力测试
                    continue;
                try {
                    Drawable icon = pm.getApplicationIcon(pgName);
                    ApplicationInfo appInfo = pm.getApplicationInfo(pgName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
                    String lable = pm.getApplicationLabel(appInfo) + "";
                    MyLog.i(TAG, "===processInfo===>:" + pgName + "pid:" + runningAppInfo.pid + ", lable:" + lable);
                    appList.add(new ProcessInfo(drawableToBitmap(icon), pgName, runningAppInfo.pid, lable));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        MyLog.i(TAG, "===appList===>" + appList.size());
        appList = singleArray(appList);
        backInfo(appList);
    }

    public void backInfo(final ArrayList<ProcessInfo> appList) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.cacheBack(appList);
            }
        });
    }


    public static ArrayList<ProcessInfo> singleArray(ArrayList<ProcessInfo> oldArray) {
        ArrayList<ProcessInfo> newList = new ArrayList<ProcessInfo>();
        if (oldArray == null && oldArray.size() <= 0)
            return newList;
        for (ProcessInfo oldInfo : oldArray) {
            boolean has = false;
            for (ProcessInfo newInfo : newList) {
                if (newInfo.pgName.equals(oldInfo.pgName)) {
                    has = true;
                    break;
                }
            }
            if (!has)
                newList.add(oldInfo);
        }
        return newList;
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
