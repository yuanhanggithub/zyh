package com.mirror.entity;

import java.util.List;

public class TectUpdateEntity {
    private List<TectDetailEntity> data;

    public List<TectDetailEntity> getData() {
        return this.data;
    }

    public void setData(List<TectDetailEntity> paramList) {
        this.data = paramList;
    }

    public static class TectDetailEntity {
        private String cover_url;
        private String created_at;
        private String id;
        private String introduction;
        private String qr_code_url;
        private String title;
        private String type;
        private Object updated_at;
        private String video_size;
        private String video_url;

        public String getCover_url() {
            return this.cover_url;
        }

        public String getCreated_at() {
            return this.created_at;
        }

        public String getId() {
            return this.id;
        }

        public String getIntroduction() {
            return this.introduction;
        }

        public String getQr_code_url() {
            return this.qr_code_url;
        }

        public String getTitle() {
            return this.title;
        }

        public String getType() {
            return this.type;
        }

        public Object getUpdated_at() {
            return this.updated_at;
        }

        public String getVideo_size() {
            return this.video_size;
        }

        public String getVideo_url() {
            return this.video_url;
        }

        public void setCover_url(String paramString) {
            this.cover_url = paramString;
        }

        public void setCreated_at(String paramString) {
            this.created_at = paramString;
        }

        public void setId(String paramString) {
            this.id = paramString;
        }

        public void setIntroduction(String paramString) {
            this.introduction = paramString;
        }

        public void setQr_code_url(String paramString) {
            this.qr_code_url = paramString;
        }

        public void setTitle(String paramString) {
            this.title = paramString;
        }

        public void setType(String paramString) {
            this.type = paramString;
        }

        public void setUpdated_at(Object paramObject) {
            this.updated_at = paramObject;
        }

        public void setVideo_size(String paramString) {
            this.video_size = paramString;
        }

        public void setVideo_url(String paramString) {
            this.video_url = paramString;
        }
    }
}
