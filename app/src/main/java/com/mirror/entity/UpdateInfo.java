package com.mirror.entity;

/**
 * Created by jsjm on 2018/5/5.
 * app升级实体类
 */

public class UpdateInfo {
    private String apkurl;
    private int appversion;
    private String updatedesc;
    private String webversion;

    public UpdateInfo(int appversion, String updatedesc, String apkurl, String webversion) {
        this.appversion = appversion;
        this.updatedesc = updatedesc;
        this.apkurl = apkurl;
        this.webversion = webversion;
    }

    public String getApkurl() {
        return this.apkurl;
    }

    public int getAppversion() {
        return this.appversion;
    }

    public String getUpdatedesc() {
        return this.updatedesc;
    }

    public String getVersionName() {
        return this.webversion;
    }

    public String getWebversion() {
        return this.webversion;
    }

    public void setApkurl(String paramString) {
        this.apkurl = paramString;
    }

    public void setAppversion(int paramInt) {
        this.appversion = paramInt;
    }

    public void setUpdatedesc(String paramString) {
        this.updatedesc = paramString;
    }

    public void setVersionName(String paramString) {
        this.webversion = paramString;
    }

    public void setWebversion(String paramString) {
        this.webversion = paramString;
    }

    public String toString() {
        return "UpdateInfo{appversion=" + this.appversion + ", updatedesc='" + this.updatedesc + '\'' + ", apkurl='" + this.apkurl + '\'' + ", webversion='" + this.webversion + '\'' + '}';
    }
}