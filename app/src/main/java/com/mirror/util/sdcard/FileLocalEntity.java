package com.mirror.util.sdcard;

public class FileLocalEntity {
    int currentProgress;
    boolean isSDCard;
    String lastSize;
    int panImageId;
    String panName;
    String path;
    String titalSize;

    public FileLocalEntity() {
    }

    public FileLocalEntity(String paramString1, String paramString2, String paramString3, int paramInt) {
        this.lastSize = paramString1;
        this.titalSize = paramString2;
        this.panName = paramString3;
        this.panImageId = paramInt;
    }

    public FileLocalEntity(boolean isSDCard, String path, String lastSize, String titalSize, int currentProgress, String panName, int panImageId) {
        this.isSDCard = isSDCard;
        this.path = path;
        this.currentProgress = currentProgress;
        this.lastSize = lastSize;
        this.titalSize = titalSize;
        this.panName = panName;
        this.panImageId = panImageId;
    }

    public int getCurrentProgress() {
        return this.currentProgress;
    }

    public String getLastSize() {
        return this.lastSize;
    }

    public int getPanImageId() {
        return this.panImageId;
    }

    public String getPanName() {
        return this.panName;
    }

    public String getPath() {
        return this.path;
    }

    public String getTitalSize() {
        return this.titalSize;
    }

    public boolean isSDCard() {
        return this.isSDCard;
    }

    public void setCurrentProgress(int paramInt) {
        this.currentProgress = paramInt;
    }

    public void setLastSize(String paramString) {
        this.lastSize = paramString;
    }

    public void setPanImageId(int paramInt) {
        this.panImageId = paramInt;
    }

    public void setPanName(String paramString) {
        this.panName = paramString;
    }

    public void setPath(String paramString) {
        this.path = paramString;
    }

    public void setSDCard(boolean paramBoolean) {
        this.isSDCard = paramBoolean;
    }

    public void setTitalSize(String paramString) {
        this.titalSize = paramString;
    }

    public String toString() {
        return "FileLocalEntity{lastSize='" + this.lastSize + '\'' + ", titalSize='" + this.titalSize + '\'' + ", panName='" + this.panName + '\'' + ", panImageId=" + this.panImageId + ", path='" + this.path + '\'' + ", currentProgress=" + this.currentProgress + '}';
    }
}
