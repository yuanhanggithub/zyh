package com.mirror.view.ad;

import android.os.Parcel;
import android.os.Parcelable;

public class Pos4Entity implements Parcelable {
    /**
     * id : 74
     * title :
     * type : 2
     * position : 1
     * text :
     * href :
     * viewcount : 2923843
     * videopath : null
     * picpath : http://cdn.magicmirrormedia.cn/img/00ce03585e0b59ae91689adb563dcd5a.jpg
     * schedule_weight : 1
     * timelength : 10
     */

    private String id;
    private String title;
    private String type;
    private String position;
    private String text;
    private String href;
    private String viewcount;
    private Object videopath;
    private String picpath;
    private String schedule_weight;
    private String timelength;

    public Pos4Entity() {

    }

    public Pos4Entity(String picpath) {
        this.picpath = picpath;
    }

    public Pos4Entity(String id, String title, String type, String position, String text, String href, String viewcount, Object videopath, String picpath, String schedule_weight, String timelength) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.position = position;
        this.text = text;
        this.href = href;
        this.viewcount = viewcount;
        this.videopath = videopath;
        this.picpath = picpath;
        this.schedule_weight = schedule_weight;
        this.timelength = timelength;
    }

    protected Pos4Entity(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.type = in.readString();
        this.position = in.readString();
        this.text = in.readString();
        this.href = in.readString();
        this.viewcount = in.readString();
        this.picpath = in.readString();
        this.schedule_weight = in.readString();
        this.timelength = in.readString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getViewcount() {
        return viewcount;
    }

    public void setViewcount(String viewcount) {
        this.viewcount = viewcount;
    }

    public Object getVideopath() {
        return videopath;
    }

    public void setVideopath(Object videopath) {
        this.videopath = videopath;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getSchedule_weight() {
        return schedule_weight;
    }

    public void setSchedule_weight(String schedule_weight) {
        this.schedule_weight = schedule_weight;
    }

    public String getTimelength() {
        return timelength;
    }

    public void setTimelength(String timelength) {
        this.timelength = timelength;
    }

    @Override
    public String toString() {
        return "Pos1Bean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", position='" + position + '\'' +
                ", text='" + text + '\'' +
                ", href='" + href + '\'' +
                ", viewcount='" + viewcount + '\'' +
                ", videopath=" + videopath +
                ", picpath='" + picpath + '\'' +
                ", schedule_weight='" + schedule_weight + '\'' +
                ", timelength='" + timelength + '\'' +
                '}';
    }


    public static final Creator<Pos4Entity> CREATOR = new Creator<Pos4Entity>() {
        @Override
        public Pos4Entity createFromParcel(Parcel in) {
            return new Pos4Entity(in);
        }

        @Override
        public Pos4Entity[] newArray(int size) {
            return new Pos4Entity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(position);
        dest.writeString(text);
        dest.writeString(href);
        dest.writeString(viewcount);
        dest.writeString(picpath);
        dest.writeString(schedule_weight);
        dest.writeString(timelength);
    }
}