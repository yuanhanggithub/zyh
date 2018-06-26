package com.mirror.entity;

import java.util.List;

public class ADVideoInfo {
    private DataEntity data;
    private int state;

    public DataEntity getData() {
        return this.data;
    }

    public int getState() {
        return this.state;
    }

    public void setData(DataEntity paramDataEntity) {
        this.data = paramDataEntity;
    }

    public void setState(int paramInt) {
        this.state = paramInt;
    }

    public String toString() {
        return "ADVideoInfo{state=" + this.state + ", data=" + this.data + '}';
    }

    public static class DataEntity {
        private List<Pos11Entity> pos_11;

        public List<Pos11Entity> getPos_11() {
            return this.pos_11;
        }

        public void setPos_11(List<Pos11Entity> paramList) {
            this.pos_11 = paramList;
        }

        public static class Pos11Entity {
            private String href;
            private String id;
            private Object picpath;
            private String position;
            private String schedule_weight;
            private String text;
            private String timelength;
            private String title;
            private String type;
            private String videopath;
            private String viewcount;

            public String getHref() {
                return this.href;
            }

            public String getId() {
                return this.id;
            }

            public Object getPicpath() {
                return this.picpath;
            }

            public String getPosition() {
                return this.position;
            }

            public String getSchedule_weight() {
                return this.schedule_weight;
            }

            public String getText() {
                return this.text;
            }

            public String getTimelength() {
                return this.timelength;
            }

            public String getTitle() {
                return this.title;
            }

            public String getType() {
                return this.type;
            }

            public String getVideopath() {
                return this.videopath;
            }

            public String getViewcount() {
                return this.viewcount;
            }

            public void setHref(String paramString) {
                this.href = paramString;
            }

            public void setId(String paramString) {
                this.id = paramString;
            }

            public void setPicpath(Object paramObject) {
                this.picpath = paramObject;
            }

            public void setPosition(String paramString) {
                this.position = paramString;
            }

            public void setSchedule_weight(String paramString) {
                this.schedule_weight = paramString;
            }

            public void setText(String paramString) {
                this.text = paramString;
            }

            public void setTimelength(String paramString) {
                this.timelength = paramString;
            }

            public void setTitle(String paramString) {
                this.title = paramString;
            }

            public void setType(String paramString) {
                this.type = paramString;
            }

            public void setVideopath(String paramString) {
                this.videopath = paramString;
            }

            public void setViewcount(String paramString) {
                this.viewcount = paramString;
            }

            public String toString() {
                return "Pos11Entity{id='" + this.id + '\'' + ", title='" + this.title + '\'' + ", type='" + this.type + '\'' + ", position='" + this.position + '\'' + ", text='" + this.text + '\'' + ", href='" + this.href + '\'' + ", viewcount='" + this.viewcount + '\'' + ", videopath='" + this.videopath + '\'' + ", picpath=" + this.picpath + ", schedule_weight='" + this.schedule_weight + '\'' + ", timelength='" + this.timelength + '\'' + '}';
            }
        }
    }
}
