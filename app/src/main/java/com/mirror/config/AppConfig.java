package com.mirror.config;

import android.os.Environment;

import com.mirror.util.SharedPerManager;

public class AppConfig {

    public static final boolean isDebug = SharedPerManager.isDebugModel();

    public static final int EQUIP_TYPE_ONE = 1;  //旧设备
    public static final int EQUIP_TYPE_TWO = 2;  //新设备
    public static final int EQUIP_TYPE = EQUIP_TYPE_TWO;

    public static final int PROJECT_AP_TIME = SharedPerManager.getScreenAdTime();
    public static final String BASE_SD_PATH = Environment.getExternalStorageDirectory().getPath();
}
