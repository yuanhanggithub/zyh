package com.mirror.entity;

/***
 * 系统升级实体类
 * cdl
 */
public class SystemUpdateInfo {
    private String apkdown;
    private String systemcode;
    private String updatedesc;

    public SystemUpdateInfo(String paramString1, String paramString2, String paramString3) {
        this.systemcode = paramString1;
        this.updatedesc = paramString2;
        this.apkdown = paramString3;
    }

    public String getApkdown() {
        return this.apkdown;
    }

    public String getSystemcode() {
        return this.systemcode;
    }

    public String getUpdatedesc() {
        return this.updatedesc;
    }

    public void setApkdown(String paramString) {
        this.apkdown = paramString;
    }

    public void setSystemcode(String paramString) {
        this.systemcode = paramString;
    }

    public void setUpdatedesc(String paramString) {
        this.updatedesc = paramString;
    }

    public String toString() {
        return "SystemUpdateInfo{systemcode='" + this.systemcode + '\'' + ", updatedesc='" + this.updatedesc + '\'' + ", apkdown='" + this.apkdown + '\'' + '}';
    }
}
