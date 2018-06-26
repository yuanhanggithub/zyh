package com.mirror.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.mirror.activity.ExitActivity;


public class CodeUtil {

    public static String getBlueToothCode() {
        String address = "";
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            String address1 = bluetoothAdapter.getAddress();
            if (address1 != null) {
                if (!address1.equals("") && address1.length() > 3)
                    address = address1.replace(":", "").trim();
            }
        } catch (Exception e) {
            Log.e("catch", "====或去蓝牙地址异常 =" + e.toString());
        }
        Log.i("catch", "==== 蓝牙地址  =" + address);
        return address;
    }


    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersion(Context mContext) {
        int versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /***
     * 版本号
     * @param context
     * @return
     */
    public static String getLocalVersion(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

}
