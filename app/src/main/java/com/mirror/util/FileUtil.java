package com.mirror.util;

import android.content.Context;

import com.mirror.config.AppInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

public class FileUtil {
    public static final String TAG = "FileUtil";

    public static String FormetFileSize(long paramLong) {
        Object localObject = new DecimalFormat("#.00");
        if (paramLong == 0L) {
            return "0B";
        }
        if (paramLong < 1024L) {
            localObject = ((DecimalFormat) localObject).format(paramLong) + "B";
        }
        if (paramLong < 1048576L) {
            localObject = ((DecimalFormat) localObject).format(paramLong / 1024.0D) + "KB";
        } else if (paramLong < 1073741824L) {
            localObject = ((DecimalFormat) localObject).format(paramLong / 1048576.0D) + "MB";
        } else {
            localObject = ((DecimalFormat) localObject).format(paramLong / 1073741824.0D) + "GB";
        }
        return (String) localObject;
    }

    //删除目录或者文件
    public static boolean deleteDirOrFile(String Path) {
        return deleteDirOrFile(new File(Path) );
    }
    public static boolean deleteDirOrFile(File file){
        if(file.isFile()){
            file.delete();
            return false;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return false;
            }
            for(File f : childFile){
                deleteDirOrFile(f);
            }
            file.delete();
        }
        return false;
    }
//    public static boolean deleteDirOrFile(File dir) {
//        if (!dir.exists()) {
//            return true;
//        }
//        if (dir.isDirectory()) {
//            String[] children = dir.list();
//            if (children == null || children.length == 0) {
//                return true;
//            }
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDirOrFile(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//        return dir.delete();
//    }

    public static void creatDirPathNoExists() {
        try {
            File localFile = new File(AppInfo.BASE_LOCAL_URL);
            boolean bool;
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件夹返回值 ===" + bool);
            }
            localFile = new File(AppInfo.BASE_VIDEO_PATH);
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            localFile = new File(AppInfo.BASE_MUSIC_PATH);
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            localFile = new File(AppInfo.BASE_APK_PATH);
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            if (!new File(AppInfo.BASE_APK_PATH + "/file").exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            localFile = new File(AppInfo.BASE_QR_PATH);
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            localFile = new File(AppInfo.BASE_VIDEO_CACHE);
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            localFile = new File(AppInfo.FILE_RECEIVER_PATH);  //filereceive
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            localFile = new File(AppInfo.FILE_RECEIVE_IMAGE);  //filereceive/image
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            localFile = new File(AppInfo.AD_IMAGE_RIGHT);  //filereceive/image/right
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            localFile = new File(AppInfo.AD_IMAGE_BOTTOM);  //filereceive/image/bottom
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            localFile = new File(AppInfo.VIDEO_SPLASH_PATH);  //splash
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                MyLog.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
            return;
        } catch (Exception localException) {
            MyLog.i("FileUtil", "====创建异常 ===" + localException.toString());
        }
    }

    /***
     *
     * @param context
     * @param rawFile
     * @param savePath
     */
    public static void saveRawToSDCard(Context context, int rawFile, String savePath) {
        MyLog.i("FileUtil", "====开始写入===" + savePath);
        try {
            InputStream inStream = context.getResources().openRawResource(rawFile);
            File file = new File(savePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
            byte[] buffer = new byte[10];
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] bs = outStream.toByteArray();
            fileOutputStream.write(bs);
            outStream.close();
            inStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            MyLog.i("FileUtil", "====写入===");
        } catch (Exception e) {
            MyLog.i("FileUtil", "====写入异常===" + e.toString());
        }
    }
}
