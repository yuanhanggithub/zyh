package com.mirror.util;

import android.text.TextUtils;
import android.util.Log;

import com.mirror.MirrorApplication;
import com.mirror.config.AppConfig;

public class SharedPerManager {

    /***
     * 判断是否是上线模式
     * true   是网络模式
     * false  离线模式
     * @return
     */
    public static boolean isOnline() {
        return ((boolean) MirrorApplication.getInstance().getData("online", true));
    }

    public static void setOnline(boolean online) {
        MirrorApplication.getInstance().saveData("online", online);
    }

    public static int getCloseEqiopMin() {
        return ((Integer) MirrorApplication.getInstance().getData("closeEqiopMin", 30));
    }

    public static int getCloseEquipHour() {
        return ((Integer) MirrorApplication.getInstance().getData("closeEquipHour", 2));
    }

    public static int getOpenEquipHour() {
        return ((Integer) MirrorApplication.getInstance().getData("openEquipHour", 8));
    }

    public static int getOpenEquipMin() {
        return ((Integer) MirrorApplication.getInstance().getData("openEquipMin", 30));
    }

    public static String getPassword() {
        return (String) MirrorApplication.getInstance().getData("password", "");
    }

    public static int getScreenAdTime() {
        if (AppConfig.isDebug) {
            return ((Integer) MirrorApplication.getInstance().getData("distanceTime", 10000));
        }
        return ((Integer) MirrorApplication.getInstance().getData("distanceTime", 300000));
    }

    public static int getScreenHeight() {
        return ((Integer) MirrorApplication.getInstance().getData("screenHeight", 768));
    }

    public static int getScreenWidth() {
        return ((Integer) MirrorApplication.getInstance().getData("screenWidth", 1366));
    }

    public static String getToken() {
        return (String) MirrorApplication.getInstance().getData("token", "888888");
    }

    public static String getUserName() {
        return (String) MirrorApplication.getInstance().getData("username", "tam");
    }

    public static boolean isDebugModel() {
        return ((Boolean) MirrorApplication.getInstance().getData("debugModel", false));
    }

    public static boolean isFirstOpenDevice() {
        return ((Boolean) MirrorApplication.getInstance().getData("firstOpenDevice", true));
    }

    public static boolean isLogin() {
        return ((Boolean) MirrorApplication.getInstance().getData("login", false));
    }

    public static boolean isOpenPowerNotify() {
        return ((Boolean) MirrorApplication.getInstance().getData("openPowerNotify", false));
    }

    public static boolean isOpenShareServer() {
        return ((Boolean) MirrorApplication.getInstance().getData("isOpenShareServer", false));
    }

    public static void setCloseEqiopMin(int closeEqiopMin) {
        MirrorApplication.getInstance().saveData("closeEqiopMin", closeEqiopMin);
    }

    public static void setCloseEquipHour(int closeEquipHour) {
        MirrorApplication.getInstance().saveData("closeEquipHour", closeEquipHour);
    }

    public static void setDebugModel(boolean debugModel) {
        MirrorApplication.getInstance().saveData("debugModel", debugModel);
    }

    public static void setFirstOpenDevice(boolean firstOpenDevice) {
        MirrorApplication.getInstance().saveData("firstOpenDevice", firstOpenDevice);
    }

    public static void setLogin(boolean login) {
        MirrorApplication.getInstance().saveData("login", login);
    }

    public static void setOpenEquipHour(int openEquipHour) {
        MirrorApplication.getInstance().saveData("openEquipHour", openEquipHour);
    }

    public static void setOpenEquipMin(int openEquipMin) {
        MirrorApplication.getInstance().saveData("openEquipMin", openEquipMin);
    }

    public static void setOpenPowerNotify(boolean openPowerNotify) {
        MirrorApplication.getInstance().saveData("openPowerNotify", openPowerNotify);
    }

    public static void setOpenShareServer(boolean isOpenShareServer) {
        MirrorApplication.getInstance().saveData("isOpenShareServer", isOpenShareServer);
    }

    public static void setPassword(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return;
        }
        MirrorApplication.getInstance().saveData("password", paramString);
    }

    public static void setScreenAdTime(int distanceTime) {
        MirrorApplication.getInstance().saveData("distanceTime", distanceTime);
    }

    public static void setScreenHeight(int screenHeight) {
        MirrorApplication.getInstance().saveData("screenHeight", screenHeight);
    }

    public static void setScreenWidth(int screenWidth) {
        Log.e("width", "====setScreenWidth====" + screenWidth);
        MirrorApplication.getInstance().saveData("screenWidth", screenWidth);
    }

    public static void setToken(String paramString) {
        MirrorApplication.getInstance().saveData("token", paramString);
    }

    public static void setUserName(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return;
        }
        MirrorApplication.getInstance().saveData("username", paramString);
    }
}
