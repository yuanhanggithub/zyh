package com.mirror.view.ad;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoLocaEntity implements Parcelable {

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    String fileName;
    String filePath;
    long fileLength;
    int fileType;

    public VideoLocaEntity(String fileName, String filePath, long fileLength, int fileType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileLength = fileLength;
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    protected VideoLocaEntity(Parcel in) {
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.fileLength = in.readLong();
        this.fileType = in.readInt();
    }


    public static final Creator<VideoLocaEntity> CREATOR = new Creator<VideoLocaEntity>() {
        @Override
        public VideoLocaEntity createFromParcel(Parcel in) {
            return new VideoLocaEntity(in);
        }

        @Override
        public VideoLocaEntity[] newArray(int size) {
            return new VideoLocaEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(filePath);
        dest.writeLong(fileLength);
        dest.writeInt(fileType);
    }
}