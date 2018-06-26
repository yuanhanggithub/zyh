package com.cdl.wifi.db;

import org.litepal.crud.DataSupport;

public class DBWifiEntity extends DataSupport {

    String wifiname;  //wifi名字
    String wifipsd;   //wifi密码
    String wifitype;  //wifi的加密方式

    public String getWifiType() {
        return wifitype;
    }

    public void setWifiType(String wifiType) {
        this.wifitype = wifiType;
    }

    public String getWifiname() {
        return wifiname;
    }

    public void setWifiname(String wifiname) {
        this.wifiname = wifiname;
    }

    public String getWifipsd() {
        return wifipsd;
    }

    public void setWifipsd(String wifipsd) {
        this.wifipsd = wifipsd;
    }

    public DBWifiEntity(String wifiname, String wifipsd, String wifiType) {
        this.wifiname = wifiname;
        this.wifipsd = wifipsd;
        this.wifitype = wifiType;
    }

    @Override
    public String toString() {
        return "DBWifiEntity{" +
                "wifiname='" + wifiname + '\'' +
                ", wifipsd='" + wifipsd + '\'' +
                '}';
    }
}
