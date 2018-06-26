package com.mirror.util.cache;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import com.cdl.wifi.util.MyLog;
import com.mirror.entity.ProcessInfo;
import com.mirror.http.GetAppBackRunnable;
import com.mirror.listener.CacheClearListener;
import com.mirror.util.APKUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MemoryUtil {

    public static final String MEMORY_CLEAN_PROCESS = "com.reemanye.cleanprocess";
    private static final String TAG = MemoryUtil.class.getSimpleName();
    private static Context mContext;
    private ActivityManager am;
    private Handler handler = new Handler();

    public MemoryUtil(Context paramContext) {
        mContext = paramContext;
        this.am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @SuppressLint({"DefaultLocale"})
    public static String byte2FitMemorySize(long paramLong) {
        if (paramLong < 0L) {
            return "0.00 B";
        }
        if (paramLong < 1024L) {
            return String.format("%.2fB", new Object[]{Double.valueOf(paramLong + 0.0005D)});
        }
        if (paramLong < 1048576L) {
            return String.format("%.2fKB", new Object[]{Double.valueOf(paramLong / 1024.0D + 0.0005D)});
        }
        if (paramLong < 1073741824L) {
            return String.format("%.2fMB", new Object[]{Double.valueOf(paramLong / 1048576.0D + 0.0005D)});
        }
        return String.format("%.2fGB", new Object[]{Double.valueOf(paramLong / 1073741824.0D + 0.0005D)});
    }


    /**
     * 描述：获取可用内存.
     *
     * @param context
     * @return
     */
    public static long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        // 当前系统可用内存 ,将获得的内存大小规格化
        return memoryInfo.availMem;
    }

    /**
     * 描述：总内存.
     *
     * @param context
     * @return
     */
    public static long getTotalMemory(Context context) {
        // 系统内存信息文件
        String file = "/proc/meminfo";
        String memInfo;
        String[] strs;
        long memory = 0;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
            // 读取meminfo第一行，系统内存大小
            memInfo = bufferedReader.readLine();
            strs = memInfo.split("\\s+");
            // 获得系统总内存，单位KB
            memory = Integer.valueOf(strs[1]).intValue();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Byte转位KB或MB
        return memory * 1024;
    }

    /**
     * 根据包名结束进程
     *
     * @param context
     * @param packageName
     */
    public static void killApkForPackageName(Context context, String packageName) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理所有非可见的进程
     */
    public void cleanMemory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
                long beforeMem = getAvailMemorySize(mContext);
                int count = 0;
                String forstName = APKUtil.appIsRunForset(mContext);
                if (infoList != null) {
                    for (int i = 0; i < infoList.size(); ++i) {
                        ActivityManager.RunningAppProcessInfo appProcessInfo = infoList.get(i);
                        if (appProcessInfo.pid == android.os.Process.myPid()
                                || appProcessInfo.processName.contains("com.android")
                                || appProcessInfo.processName.equals("system")
                                || appProcessInfo.processName.equals("com.cghs.stresstest")) // 压力测试
                            continue;
                        String[] pkgList = appProcessInfo.pkgList;
                        for (int j = 0; j < pkgList.length; ++j) {
                            if (pkgList[j].contains(forstName) || forstName.contains(pkgList[j]))
                                continue;
                            killApkForPackageName(mContext, pkgList[j]);
                            count++;
                        }
                    }
                }
                long afterMem = getAvailMemorySize(mContext);
                Intent intent = new Intent(MEMORY_CLEAN_PROCESS);
                intent.putExtra("count", count);
                intent.putExtra("size", byte2FitMemorySize(afterMem - beforeMem));
                mContext.sendBroadcast(intent);
            }
        }).start();
    }

    /**
     * 获取可用内存
     *
     * @param context
     * @return
     */
    public long getAvailMemorySize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    /**
     * 获取不可用内存占比
     *
     * @param context
     * @return
     */
    public int getDisAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // Formatter.formatFileSize(context, mi.availMem);
        return (int) ((mi.totalMem - mi.availMem) * 100 / (mi.totalMem == 0 ? 1 : mi.totalMem));
    }


    /**
     * 获取所有应用
     */
    public void getRunApp(CacheClearListener listener) {
        Runnable runnable = new GetAppBackRunnable(mContext, listener);
        Thread thread = new Thread(runnable);
        thread.start();
    }

}
