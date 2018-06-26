package com.mirror.entity;

public class ModelMainItem {

    public static final String SETTING_WIFI = "WIFI设置";
    public static final String SETTING_VERSION_DISONLINE = "脱网模式";
    public static final String SETTING_BELLTH = "蓝牙设置";
    public static final String SETTING_NET_SPEED = "网速测试";
    public static final String SETTING_CACHE_CLEAR= "缓存清理";
    public static final String SETTING_VIDEO_DISPLAY= "作品展示";
    public static final String SETTING_APP_MANAGER= "程序管理";
    public static final String SETTING_AP_OPEN= "开启热点";
    public static final String SETTING_SD_MANAGER= "磁盘管理";
    public static final String SETTING_SYSTEM_UPDATE= "系统升级";
    public static final String SETTING_ESHARE_INFO= "投屏信息";
    public static final String SETTING_OLD_TEST= "老化测试";
    public static final String SETTING_SYS_SETTING= "系统设置";
    public static final String SETTING_INTRO_USER= "使用引导";
    public static final String SETTING_CLOSE_TIMER= "定时开关机";

    private int image;
    private String name;


    public ModelMainItem(String paramString, int paramInt) {
        this.name = paramString;
        this.image = paramInt;
    }

    public int getImage() {
        return this.image;
    }

    public String getName() {
        return this.name;
    }

    public void setImage(int paramInt) {
        this.image = paramInt;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }
}
