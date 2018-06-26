package com.mirror.util.sdcard;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.mirror.config.AppConfig;
import com.mirror.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MySDCard {
    private String tag = this.getClass().getName();
    private       Context  context;
    public static Object[] objArray;
    public static String  path          = null;// SD卡的路径
    public static String  TotalSize     = null;// SD卡的大小
    public static String  AvailableSize = null;// SD卡的大小
    public static Boolean isRemovable   = false;// SD卡是否可移除
    String getVolumeState = null;// SD卡的安装状态

    public static List<FileLocalEntity> mStorageList = new ArrayList<>();

    public boolean searchSd() {
        getSDCardPaths();
        return true;
    }

    public static List<FileLocalEntity> getmStorageList () {
        return mStorageList;
    }

    /**
     * 构造方法
     */
    public MySDCard (Context context) {
        this.context = context;
    }


    public static int getSdkVersion () {
        Log.i("==", "当前手机系统版本号=" + Build.VERSION.SDK_INT);
        return Build.VERSION.SDK_INT;
    }

    public File[] getSDCardPaths () {
        Log.i(tag, "当前手机系统版本号=" + Build.VERSION.SDK_INT);
        switch (Build.VERSION.SDK_INT) {
            case 23:
            case 22:
            case 21:
            case 20:
                return this.getSDCardPaths_20();
            case 19:
            case 18:
            case 17:
            case 16:
            case 15:
            case 14:
                return this.getSDCardPaths_16();
        }
        return null;
    }

    @TargetApi(18)
    public File[] getSDCardPaths_20 () {
        try {
            Log.i(tag, "getExternalStorageDirectory=" + Environment.getExternalStorageDirectory().getPath());
            Class class_StorageManager = StorageManager.class;
            Method method_getVolumeList = class_StorageManager.getMethod("getVolumeList");
            Method method_getVolumeState = class_StorageManager.getMethod("getVolumeState", String.class);
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class class_StorageVolume = Class.forName("android.os.storage.StorageVolume");
            Method method_isRemovable = class_StorageVolume.getMethod("isRemovable");
            Method method_getPath = class_StorageVolume.getMethod("getPath");
            Method method_getId = class_StorageVolume.getMethod("getUserLabel");
            Method method_getPathFile = class_StorageVolume.getMethod("getPathFile");
            objArray = (Object[]) method_getVolumeList.invoke(sm);
            //objArray.length==2时说明插上了sd卡
            List<File> fileList = new ArrayList<File>();
            mStorageList.clear();
            for (Object value : objArray) {
                //sd卡的路径
                path = (String) method_getPath.invoke(value);
                AvailableSize = getAvailableExternalMemorySize(context, path);
                TotalSize = getTotalExternalMemorySize(context, path);
                isRemovable = (Boolean) method_isRemovable.invoke(value);
                String id = (String) method_getId.invoke(value);
                getVolumeState = (String) method_getVolumeState.invoke(sm, path);// 获取挂载状态。
                File file = (File) method_getPathFile.invoke(value);
                fileList.add(file);

                if (Environment.MEDIA_MOUNTED.equals(getVolumeState)) {
                    boolean isSdCard = !file.getAbsolutePath().equals(AppConfig.BASE_SD_PATH);
                    FileLocalEntity fileLocalEntity = new FileLocalEntity(isSdCard, path, AvailableSize, TotalSize,
                            getCurrentProgress(path), id, 0);
                    mStorageList.add(fileLocalEntity);
                }
            }
            File[] files = new File[fileList.size()];
            fileList.toArray(files);
            return files;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @TargetApi(16)
    public File[] getSDCardPaths_16 () {
        try {
            Class class_StorageManager = StorageManager.class;
            Method method_getVolumeList = class_StorageManager.getMethod("getVolumeList");
            Method method_getVolumeState = class_StorageManager.getMethod("getVolumeState", String.class);
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class class_StorageVolume = Class.forName("android.os.storage.StorageVolume");
            Method method_isRemovable = class_StorageVolume.getMethod("isRemovable");
            Method   method_getPath = class_StorageVolume.getMethod("getPath");
            Method method_getId = class_StorageVolume.getMethod("getUserLabel");
            Object[] objArray       = (Object[]) method_getVolumeList.invoke(sm);
            List<File> fileList = new ArrayList<File>();
            mStorageList.clear();
            for (Object value : objArray) {
                String path = (String) method_getPath.invoke(value);
                String getVolumeState = (String) method_getVolumeState.invoke(sm, path);// 获取挂载状态。
                String  id = (String) method_getId.invoke(value);
                AvailableSize =getLastSpace(1,path) ;
                TotalSize  =getLastSpace(2,path) ;
                Log.i(TAG,"==检索存储设备的路径==="+path+"  /="+getVolumeState+"  /="+TotalSize ) ;
                fileList.add(new File((String) method_getPath.invoke(value)));
                if (Environment.MEDIA_MOUNTED.equals(getVolumeState)) {
                    boolean isSdCard = !path.equals(AppConfig.BASE_SD_PATH);
                    if (id==null||id.length()<1){
                        if (path.contains("external_")){
                            id="SD卡";
                        }else if (path.contains("usb_")){
                            if (getSpaceLong(2,path)>1024*1024*10){
                                id="USB存储";
                                path = path.substring(0,path.lastIndexOf("/"));
                                AvailableSize = "不可" ;
                                TotalSize = "查看";
                            }
                        }
                    }
                    FileLocalEntity fileLocalEntity = new FileLocalEntity(isSdCard, path, AvailableSize, TotalSize,
                            getCurrentProgress(path), id, 0);
                    mStorageList.add(fileLocalEntity);
                }
            }
            File[] files = new File[fileList.size()];
            fileList.toArray(files);
            return files;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取SDCARD剩余存储空间 带单位
     *
     * @return
     */
    public String getAvailableExternalMemorySize (Context context, String path1) {
        File   path            = new File(path1);
        if (path1 == null || !path.exists()) {
            return 0 +  "B";
        }
        StatFs stat            = new StatFs(path.getPath());
        long   blockSize       = stat.getBlockSize();
        long   availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, availableBlocks * blockSize);
    }

    /**
     * 获取SDCARD总的存储空间 不带单位
     *
     * @return
     */
    public String getTotalExternalMemorySize (Context context, String path2) {
        File   path        = new File(path2);
        StatFs stat        = new StatFs(path.getPath());
        long   blockSize   = stat.getBlockSize();
        long   totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, totalBlocks * blockSize);
    }

    public static final String TAG = "MySDCard";

    /***
     * 获取内存占用百分比
     * @param path2
     * @return
     */
    public int getCurrentProgress (String path2) {
        File path        = new File(path2);
        long usableSpace = path.getUsableSpace();
        long totalSpace  = path.getTotalSpace();
        int  progress    = (int) ((totalSpace - usableSpace) * 100 / totalSpace);
        return progress;
    }

    /***
     * 获取可用空间
     * type
     * 1:剩余空间
     * 2:总共空间
     * @param path2
     * @return
     */
    public static String getLastSpace (int type, String path2) {
        String space_desc = "";
        File  path  = new File(path2);
        long   usableSpace;
        if (type == 1) {
            usableSpace = path.getUsableSpace();
        } else {
            usableSpace = path.getTotalSpace();
        }
        space_desc = FileUtil.FormetFileSize(usableSpace);
        return space_desc;
    }

    public static long getSpaceLong (int type, String path2) {
        File  path  = new File(path2);
        long   usableSpace;
        if (type == 1) {
            usableSpace = path.getUsableSpace();
        } else {
            usableSpace = path.getTotalSpace();
        }
        return usableSpace;
    }


    public static List<String> getMountPathList() {
        List<String> pathList = new ArrayList<String>();
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();//取得当前JVM的运行时环境
        try {
            Process p = run.exec(cmd);//执行命令
            BufferedInputStream inputStream = new BufferedInputStream(p.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                //输出信息内容：  /data/media /storage/emulated/0 sdcardfs rw,nosuid,nodev,relatime,uid=1023,gid=1023 0 0
                String[] temp = TextUtils.split(line, "" );
                //分析内容可看出第二个空格后面是路径
                String result = temp[1];
                File file = new File(result);
                //类型为目录、可读、可写，就算是一条挂载路径
                if (file.isDirectory() && file.canRead() && file.canWrite()) {
                    pathList.add(result);
                }

                // 检查命令是否执行失败
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                }
            }
            bufferedReader.close();
            inputStream.close();
        } catch (Exception e) {
            pathList.add(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        return pathList;
    }

}