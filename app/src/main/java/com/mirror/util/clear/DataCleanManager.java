package com.mirror.util.clear;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

public class DataCleanManager {
    public static void clearAllCache(Context paramContext) {
        deleteDir(paramContext.getCacheDir());
        if (Environment.getExternalStorageState().equals("mounted")) {
            deleteDir(paramContext.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File paramFile) {
        if ((paramFile != null) && (paramFile.isDirectory())) {
            String[] arrayOfString = paramFile.list();
            int i = 0;
            while (i < arrayOfString.length) {
                if (!deleteDir(new File(paramFile, arrayOfString[i]))) {
                    return false;
                }
                i += 1;
            }
        }
        return paramFile.delete();
    }

    public static long getFolderSize(File file) throws Exception {
//        long l1 = 0L;
//        long l2 = l1;
////        for (;;)
////        {
//            int i;
//            long l3;
//            try {
//               File[] param = paramFile.listFiles();
//                i = 0;
//                l2 = l1;
//                l3 = l1;
//                if (i < param.length)
//                {
//                    l2 = l1;
//                    if (param[i].isDirectory())
//                    {
//                        l2 = l1;
//                        l1 += getFolderSize(param[i]);
//                    }
//                    else
//                    {
//                        l2 = l1;
//                        l3 = param[i].length();
//                        l1 += l3;
//                    }
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//                l3 = l2;
//            }
//            return l3;
//            i += 1;
//        }
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFormatSize(double size) {
//        double d = paramDouble / 1024.0D;
//        if (d < 1.0D) {
//            return "0K";
//        }
//        paramDouble = d / 1024.0D;
//        if (paramDouble < 1.0D)
//        {
//            BigDecimal   bigDecimal = new BigDecimal(Double.toString(d));
//            return bigDecimal.setScale(2, 4).toPlainString() + "K";
//        }
//        d = paramDouble / 1024.0D;
//        if (d < 1.0D)
//        {
//            BigDecimal bigDecimald = new BigDecimal(Double.toString(paramDouble));
//            return bigDecimald.setScale(2, 4).toPlainString() + "M";
//        }
//        paramDouble = d / 1024.0D;
//        if (paramDouble < 1.0D)
//        {
//            localBigDecimal = new BigDecimal(Double.toString(d));
//            return localBigDecimal.setScale(2, 4).toPlainString() + "GB";
//        }
//        BigDecimal localBigDecimal = new BigDecimal(paramDouble);
//        return localBigDecimal.setScale(2, 4).toPlainString() + "TB";
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    public static String getTotalCacheSize(Context context) {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getFormatSize(cacheSize);
    }
}
