package com.mirror.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.mirror.view.ad.Pos1Bean;

import java.util.List;

public class DataBean implements Parcelable {
    public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator() {
        public DataBean createFromParcel(Parcel paramAnonymousParcel) {
            return new DataBean(paramAnonymousParcel);
        }

        public DataBean[] newArray(int paramAnonymousInt) {
            return new DataBean[paramAnonymousInt];
        }
    };
    private List<Pos1Bean> pos_1;

    protected DataBean(Parcel paramParcel) {
        this.pos_1 = paramParcel.createTypedArrayList(Pos1Bean.CREATOR);
    }

    public int describeContents() {
        return 0;
    }

    public List<Pos1Bean> getPos_1() {
        return this.pos_1;
    }

    public void setPos_1(List<Pos1Bean> paramList) {
        this.pos_1 = paramList;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeTypedList(this.pos_1);
    }
}
