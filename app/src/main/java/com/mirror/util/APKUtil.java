package com.mirror.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.mirror.view.MyToastView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class APKUtil {
    Context context;

    public APKUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取所有进程包名
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getAllProcess(Context context) {
        ArrayList<String> list = new ArrayList<String>();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo runningApp : appProcesses) {
            list.add(runningApp.processName);
        }
        return list;
    }

    /***
     * 返回当前前台运行的app
     *
     * @return
     */
    public static String appIsRunForset(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        String packName = appProcesses.get(0).processName;
        return packName;
    }

    /**
     * 安装APK文件
     */
    public void installApk(String outpath) {
        try {
            File apkfile = new File(outpath);
            if (!apkfile.exists()) {
                MyToastView.getInstance().Toast(context, "安装文件不存在,请先下载");
                return;
            }
            // 通过Intent安装APK文件
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                    "application/vnd.android.package-archive");
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 判断APK有没有安装
     * @return
     */
    public static boolean ApkState(final Context context, String packageName) {
        boolean isInstall = false;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            if (packageInfo != null) {
                isInstall = true;
            } else {
                isInstall = false;
            }
        } catch (Exception e) {
            isInstall = false;
        }
        return isInstall;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static void writeFile(String str) throws IOException, InterruptedException {

        File file = new File("/sys/devices/fb.11/graphics/fb0/pwr_bl");
        file.setExecutable(true);
        file.setReadable(true);//设置可读权限
        file.setWritable(true);//设置可写权限
        if (str.equals("0")) {
            do_exec("busybox echo 0 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
        } else {
            do_exec("busybox echo 1 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
        }
    }

    public static void do_exec(String cmd) {
        try {
            /* Missing read/write permission, trying to chmod the file */
            Process su;
            su = Runtime.getRuntime().exec("su");
            String str = cmd + "\n" + "exit\n";
            su.getOutputStream().write(str.getBytes());

            if ((su.waitFor() != 0)) {
                System.out.println("cmd=" + cmd + " error!");
                throw new SecurityException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openOrClose(String cmd) {
        try {
            APKUtil.writeFile(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
