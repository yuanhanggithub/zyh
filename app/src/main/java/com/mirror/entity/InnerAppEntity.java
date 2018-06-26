package com.mirror.entity;

import java.io.Serializable;

public class InnerAppEntity
        implements Serializable {

    public static final int APP_ALL_TAG = 0;
    public static final int APP_TV_TAG = 1;
    public static final int APP_CARTON_TAG = 2;
    public static final int APP_GAME_TAG = 3;
    public static final int APP_ADD_TAG = 4;
    private int app_tag;
    private String downLoadUrl;
    private int imgeUrl;
    private String launchName;
    private String liveName;
    private String packageName;
    private int videoTag;

    public InnerAppEntity() {
    }

    public InnerAppEntity(String liveName, int imgeUrl, String downLoadUrl, String packageName, int videoTag, int app_tag) {
        this.videoTag = videoTag;
        this.imgeUrl = imgeUrl;
        this.liveName = liveName;
        this.app_tag = app_tag;
        this.downLoadUrl = downLoadUrl;
        this.packageName = packageName;
    }

    public int getApp_tag() {
        return this.app_tag;
    }

    public String getDownLoadUrl() {
        return this.downLoadUrl;
    }

    public int getImgeUrl() {
        return this.imgeUrl;
    }

    public String getLaunchName() {
        return this.launchName;
    }

    public String getLiveName() {
        return this.liveName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public int getVideoTag() {
        return this.videoTag;
    }

    public void setApp_tag(int paramInt) {
        this.app_tag = paramInt;
    }

    public void setDownLoadUrl(String paramString) {
        this.downLoadUrl = paramString;
    }

    public void setImgeUrl(int paramInt) {
        this.imgeUrl = paramInt;
    }

    public void setLaunchName(String paramString) {
        this.launchName = paramString;
    }

    public void setLiveName(String paramString) {
        this.liveName = paramString;
    }

    public void setPackageName(String paramString) {
        this.packageName = paramString;
    }

    public void setVideoTag(int paramInt) {
        this.videoTag = paramInt;
    }
}
