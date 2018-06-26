package com.mirror.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.cdl.wifi.util.MyLog;
import com.mirror.config.AppInfo;
import com.mirror.entity.AppInfomation;

import java.util.ArrayList;
import java.util.List;

public class PackgeUtil {

    /***
     * 获取手机中所有已安装的应用，并判断是否系统应用
     *
     * @param context
     * @return 非系统应用
     */
    public static void getPackage(Context context, PackageListener listener) {
        try {
            ArrayList<AppInfomation> appList = new ArrayList<AppInfomation>();
            List<PackageInfo> packages = context.getPackageManager()
                    .getInstalledPackages(0);
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);
                AppInfomation tmpInfo = new AppInfomation();
                String name = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString(); //app名字
                String packageName = packageInfo.packageName;                                                //包名
                tmpInfo.appName = name;
                tmpInfo.packageName = packageName;
                tmpInfo.drawable = packageInfo.applicationInfo.loadIcon(context
                        .getPackageManager());
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    if ((!packageName.contains(AppInfo.APP_PACKAGE_NEW)) && (!packageName.contains(AppInfo.APP_PACKAGE_OLD))) {
                        MyLog.i("PackgeUtil", "====" + name + " /" + AppInfo.APP_PACKAGE_NEW + "  /" + AppInfo.APP_PACKAGE_OLD);
                        appList.add(tmpInfo);
                    }
                } else if (packageName.contains(AppInfo.ESHARE_I_MIRRIR_PACKAGE)) {
                    MyLog.i("PackgeUtil", "======imirror=" + AppInfo.ESHARE_I_MIRRIR_PACKAGE);
                    appList.add(tmpInfo);
                }
            }
            listener.getSuccess(appList);
        } catch (Exception e) {
            String desc = e.toString();
            listener.getFail(desc);
        }
    }

    public interface PackageListener {
        void getSuccess(ArrayList<AppInfomation> appList);

        void getFail(String error);
    }

}
