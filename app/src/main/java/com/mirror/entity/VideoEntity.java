package com.mirror.entity;

public class VideoEntity {
    String createtime;
    String description;
    int id;
    String picpath;
    int subchannel_id;
    int time;
    String title;
    String videopath;
    int viewcount;

    public VideoEntity(int id, int subchannel_id, String title, String description, String videopath,
                       String picpath, int time, int viewcount, String createtime) {
        this.id = id;
        this.subchannel_id = subchannel_id;
        this.title = title;
        this.description = description;
        this.videopath = videopath;
        this.picpath = picpath;
        this.time = time;
        this.viewcount = viewcount;
        this.createtime = createtime;
    }

    public VideoEntity(String paramString1, String paramString2) {
        this.title = paramString1;
        this.picpath = paramString2;
    }

    public String getCreatetime() {
        return this.createtime;
    }

    public String getDescription() {
        return this.description;
    }

    public int getId() {
        return this.id;
    }

    public String getPicpath() {
        return this.picpath;
    }

    public int getSubchannel_id() {
        return this.subchannel_id;
    }

    public int getTime() {
        return this.time;
    }

    public String getTitle() {
        return this.title;
    }

    public String getVideopath() {
        return this.videopath;
    }

    public int getViewcount() {
        return this.viewcount;
    }

    public void setCreatetime(String paramString) {
        this.createtime = paramString;
    }

    public void setDescription(String paramString) {
        this.description = paramString;
    }

    public void setId(int paramInt) {
        this.id = paramInt;
    }

    public void setPicpath(String paramString) {
        this.picpath = paramString;
    }

    public void setSubchannel_id(int paramInt) {
        this.subchannel_id = paramInt;
    }

    public void setTime(int paramInt) {
        this.time = paramInt;
    }

    public void setTitle(String paramString) {
        this.title = paramString;
    }

    public void setVideopath(String paramString) {
        this.videopath = paramString;
    }

    public void setViewcount(int paramInt) {
        this.viewcount = paramInt;
    }

    public String toString() {
        return "VideoEntity{id=" + this.id + ", subchannel_id=" + this.subchannel_id + ", title='" + this.title + '\'' + ", description='" + this.description + '\'' + ", videopath='" + this.videopath + '\'' + ", picpath='" + this.picpath + '\'' + ", time=" + this.time + ", viewcount=" + this.viewcount + ", createtime='" + this.createtime + '\'' + '}';
    }
}
