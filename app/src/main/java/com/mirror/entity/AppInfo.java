//package com.mirror.entity;
//
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.mirror.util.cache.MemoryUtil;
//
//
//public class AppInfo implements Parcelable {
//
//    public String appName;
//    public String packageName;
//    public Drawable drawable;
//    String downUrl; // 网络获取缩略图
//    int imageId;
//    boolean isChoice;
//
//    /***
//     * 系统应用
//     *
//     * @param apkName
//     * @param imageId
//     * @param downUrl
//     */
//    public AppInfo(String apkName, int imageId, String packageName, String downUrl) {
//        this.setAppName(apkName);
//        this.setDownUrl(downUrl);
//        this.setImageId(imageId);
//        this.setPackageName(packageName);
//        this.setChoice(isChoice);
//    }
//
//    public AppInfo(Parcel source) {
//        appName = source.readString();
//        packageName = source.readString();
//        Bitmap bitmap = source.readParcelable(Bitmap.class.getClassLoader());
//        drawable = new BitmapDrawable(bitmap);
//        downUrl = source.readString();
//        imageId = source.readInt();
//        int choice = source.readInt();
//        isChoice = choice == 0 ? false : true;
//    }
//
//
//    public AppInfo(String appName, int imageId) {
//        this.appName = appName;
//        this.imageId = imageId;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeString(appName);
//        out.writeString(packageName);
//        out.writeParcelable(MemoryUtil.drawableToBitmap(drawable), Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
//        out.writeString(downUrl);
//        out.writeInt(imageId);
//        out.writeInt(isChoice ? 1 : 0);
//    }
//
//    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
//        @Override
//        public AppInfo[] newArray(int size) {
//            return new AppInfo[size];
//        }
//
//        @Override
//        public AppInfo createFromParcel(Parcel source) {
//            return new AppInfo(source);
//        }
//    };
//
//    /***
//     * 我的应用
//     *
//     * @param appName
//     * @param packageName
//     * @param drawable
//     */
//    public AppInfo(String appName, String packageName, Drawable drawable) {
//        this.appName = appName;
//        this.packageName = packageName;
//        this.drawable = drawable;
//    }
//
//    public boolean isChoice() {
//        return isChoice;
//    }
//
//    public void setChoice(boolean isChoice) {
//        this.isChoice = isChoice;
//    }
//
//    public String getDownUrl() {
//        return downUrl;
//    }
//
//    public void setDownUrl(String downUrl) {
//        this.downUrl = downUrl;
//    }
//
//    public int getImageId() {
//        return imageId;
//    }
//
//    public void setImageId(int imageId) {
//        this.imageId = imageId;
//    }
//
//    public AppInfo() {
//    }
//
//    public AppInfo(String appName) {
//        this.appName = appName;
//    }
//
//    public AppInfo(String appName, String packageName) {
//        this.appName = appName;
//        this.packageName = packageName;
//    }
//
//    public AppInfo(String string, Drawable drawable2) {
//        this.appName = string;
//        this.drawable = drawable2;
//    }
//
//    public String getAppName() {
//        if (null == appName)
//            return "";
//        else
//            return appName;
//    }
//
//    public void setAppName(String appName) {
//        this.appName = appName;
//    }
//
//    public String getPackageName() {
//        if (null == packageName)
//            return "";
//        else
//            return packageName;
//    }
//
//    public void setPackageName(String packageName) {
//        this.packageName = packageName;
//    }
//
//    public Drawable getDrawable() {
//        return drawable;
//    }
//
//    public void setDrawable(Drawable drawable) {
//        this.drawable = drawable;
//    }
//
//    @Override
//    public String toString() {
//        return "AppInfo [appName=" + appName + ", packageName=" + packageName + "]";
//    }
//}
