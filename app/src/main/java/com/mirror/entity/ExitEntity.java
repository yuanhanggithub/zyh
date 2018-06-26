package com.mirror.entity;

public class ExitEntity {
    String desc;
    public int imageId;
    boolean isCheck;

    public ExitEntity(int paramInt, String paramString, boolean paramBoolean) {
        this.imageId = paramInt;
        this.desc = paramString;
        this.isCheck = paramBoolean;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getImageId() {
        return this.imageId;
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public void setCheck(boolean paramBoolean) {
        this.isCheck = paramBoolean;
    }

    public void setDesc(String paramString) {
        this.desc = paramString;
    }

    public void setImageId(int paramInt) {
        this.imageId = paramInt;
    }

    public String toString() {
        return "ExitEntity{imageId=" + this.imageId + ", desc='" + this.desc + '\'' + ", isCheck=" + this.isCheck + '}';
    }
}
