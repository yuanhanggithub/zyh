package com.mirror.guide.entity;

public class GuideMobEntity {
    String desc;
    public int imageId;

    public GuideMobEntity(String paramString, int paramInt) {
        this.imageId = paramInt;
        this.desc = paramString;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getImageId() {
        return this.imageId;
    }

    public void setDesc(String paramString) {
        this.desc = paramString;
    }

    public void setImageId(int paramInt) {
        this.imageId = paramInt;
    }

    public String toString() {
        return "ExitEntity{imageId=" + this.imageId + ", desc='" + this.desc + '\'' + '}';
    }
}
