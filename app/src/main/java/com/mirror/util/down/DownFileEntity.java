package com.mirror.util.down;


public class DownFileEntity {

    public static final int DOWN_STATE_START = 0;
    public static final int DOWN_STATE_PROGRESS = 1;
    public static final int DOWN_STATE_SUCCESS = 2;
    public static final int DOWN_STATE_FAIED = 3;
    public static final int DOWN_STATE_CACLE = 4;

    int downState;   //用来回掉界面现在的状态
    int progress;    //下载的进度
    boolean isDown;  //当前是否在下载状态
    String desc;   //用来描述下载失败异常的
    String downPath;    //文件的下载地址
    String savePath;     //文件的保存地址
    int downSpeed;       //下载的速度

    public int getDownSpeed() {
        return downSpeed;
    }

    public void setDownSpeed(int downSpeed) {
        this.downSpeed = downSpeed;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDownPath() {
        return downPath;
    }

    public void setDownPath(String downPath) {
        this.downPath = downPath;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public int getDownState() {
        return downState;
    }

    public void setDownState(int downState) {
        this.downState = downState;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isDown() {
        return isDown;
    }

    public void setDown(boolean down) {
        isDown = down;
    }

    @Override
    public String toString() {
        return "DownFileEntity{" +
                "downState=" + downState +
                ", progress=" + progress +
                ", isDown=" + isDown +
                ", desc='" + desc + '\'' +
                ", downPath='" + downPath + '\'' +
                ", savePath='" + savePath + '\'' +
                '}';
    }
}
