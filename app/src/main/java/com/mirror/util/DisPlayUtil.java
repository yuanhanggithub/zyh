package com.mirror.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.mirror.activity.MainNewActivity;
import com.mirror.activity.SplashActivity;
import com.mirror.guide.GuideActivity;
import com.mirror.view.MyToastView;

public class DisPlayUtil {

    public static void gotoHomeActivity(Context context) {
        try {
            if (ActivityCollector.isForeground(context, SplashActivity.class.getName())) {
                return;
            }
            if (!SharedPerManager.isLogin()) {
                return;
            }
            if (ActivityCollector.isForeground(context, MainNewActivity.class.getName())) {
                return;
            }
            if (MainNewActivity.isForst) {
                return;
            }
            if (ActivityCollector.isForeground(context, GuideActivity.class.getName())) {
                return;
            }
            Intent localIntent = new Intent();
            localIntent.setClass(context, MainNewActivity.class);
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(localIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startApp(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gotoSysSdPath(Context context) {
        if (APKUtil.ApkState(context, "com.android.rockchip")) {
            Intent param = new Intent("android.intent.action.MAIN");
            param.addCategory("android.intent.category.LAUNCHER");
            param.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            param.setComponent(new ComponentName("com.android.rockchip", "com.android.rockchip.RockExplorer"));
            context.startActivity(param);
            return;
        } else if (APKUtil.ApkState(context, "com.softwinner.TvdFileManager")) {
            Intent param = new Intent("android.intent.action.MAIN");
            param.addCategory("android.intent.category.LAUNCHER");
            param.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            param.setComponent(new ComponentName("com.softwinner.TvdFileManager", "com.softwinner.TvdFileManager.MainUI"));
            context.startActivity(param);
            return;
        }
        MyToastView.getInstance().Toast(context, "没有找到合适的文件管理器，请联系售后");
    }


}
