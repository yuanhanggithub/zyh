package com.mirror.view.ad;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.mirror.entity.DataBean;

public class ADInfo implements Parcelable {

    public static final Parcelable.Creator<ADInfo> CREATOR = new Parcelable.Creator() {
        public ADInfo createFromParcel(Parcel paramAnonymousParcel) {
            return new ADInfo(paramAnonymousParcel);
        }

        public ADInfo[] newArray(int paramAnonymousInt) {
            return new ADInfo[paramAnonymousInt];
        }
    };
    public static final int STATE_FAILED = 2;
    public static final int STATE_SUCCESS = 1;
    private DataBean data;
    private int state;

    public ADInfo() {
    }

    public ADInfo(int paramInt) {
        this.state = paramInt;
    }

    protected ADInfo(Parcel paramParcel) {
        this.state = paramParcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public DataBean getData() {
        return this.data;
    }

    public int getState() {
        return this.state;
    }

    public void setData(DataBean paramDataBean) {
        this.data = paramDataBean;
    }

    public void setState(int paramInt) {
        this.state = paramInt;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeInt(this.state);
    }
}
