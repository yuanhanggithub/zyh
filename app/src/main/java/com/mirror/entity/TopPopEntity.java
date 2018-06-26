package com.mirror.entity;

import java.util.List;

public class TopPopEntity {
    private List<Pos13Entity> pos_13;

    public List<Pos13Entity> getPos_13() {
        return this.pos_13;
    }

    public void setPos_13(List<Pos13Entity> paramList) {
        this.pos_13 = paramList;
    }

    public static class Pos13Entity {
        private String href;
        private String id;
        private String picpath;
        private String position;
        private String schedule_weight;
        private String text;
        private String timelength;
        private String title;
        private String type;
        private Object videopath;
        private String viewcount;

        public Pos13Entity(String paramString) {
            this.picpath = paramString;
        }

        public String getHref() {
            return this.href;
        }

        public String getId() {
            return this.id;
        }

        public String getPicpath() {
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

        public Object getVideopath() {
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

        public void setPicpath(String paramString) {
            this.picpath = paramString;
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

        public void setVideopath(Object paramObject) {
            this.videopath = paramObject;
        }

        public void setViewcount(String paramString) {
            this.viewcount = paramString;
        }
    }
}
