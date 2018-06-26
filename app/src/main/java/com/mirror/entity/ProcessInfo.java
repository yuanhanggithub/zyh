package com.mirror.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ProcessInfo implements Parcelable {

    public static final Parcelable.Creator<ProcessInfo> CREATOR = new Parcelable.Creator() {
        public ProcessInfo createFromParcel(Parcel parcel) {
            return new ProcessInfo(parcel);
        }

        public ProcessInfo[] newArray(int paramAnonymousInt) {
            return new ProcessInfo[paramAnonymousInt];
        }
    };
    public String appName;
    public Bitmap icon;
    public String pgName;
    public int pid;

    public ProcessInfo(Bitmap paramBitmap, String paramString1, int paramInt, String paramString2) {
        this.icon = paramBitmap;
        this.pgName = paramString1;
        this.pid = paramInt;
        this.appName = paramString2;
    }

    public ProcessInfo(Parcel paramParcel) {
        this.icon = ((Bitmap) paramParcel.readParcelable(Bitmap.class.getClassLoader()));
        this.appName = paramParcel.readString();
        this.pgName = paramParcel.readString();
        this.pid = paramParcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ProcessInfo{icon=" + this.icon + ", appName='" + this.appName + '\'' + ", pgName='" + this.pgName + '\'' + ", pid=" + this.pid + '}';
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeParcelable(this.icon, 1);
        paramParcel.writeString(this.appName);
        paramParcel.writeString(this.pgName);
        paramParcel.writeInt(this.pid);
    }
}
