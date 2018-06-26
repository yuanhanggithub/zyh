package com.mirror.util.clear;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.math.BigDecimal;

public class SdcardManagerUtil {
    public static long getAvailableExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            StatFs localStatFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long l = localStatFs.getBlockSize();
            return localStatFs.getAvailableBlocks() * l;
        }
        return -1L;
    }

    public static long getAvailableInternalMemorySize() {
        StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
        long l = localStatFs.getBlockSize();
        return localStatFs.getAvailableBlocks() * l;
    }

    public static String getFormatSize(double paramDouble) {
        double d = paramDouble / 1024.0D;
        if (d < 1.0D) {
            return paramDouble + "Byte";
        }
        paramDouble = d / 1024.0D;
        if (paramDouble < 1.0D) {
            BigDecimal localBigDecimal = new BigDecimal(Double.toString(d));
            return localBigDecimal.setScale(1, 4).toPlainString() + "KB";
        }
        d = paramDouble / 1024.0D;
        if (d < 1.0D) {
            BigDecimal localBigDecimal = new BigDecimal(Double.toString(paramDouble));
            return localBigDecimal.setScale(1, 4).toPlainString() + "MB";
        }
        paramDouble = d / 1024.0D;
        if (paramDouble < 1.0D) {
            BigDecimal localBigDecimal = new BigDecimal(Double.toString(d));
            return localBigDecimal.setScale(1, 4).toPlainString() + "GB";
        }
        BigDecimal localBigDecimal = new BigDecimal(paramDouble);
        return localBigDecimal.setScale(1, 4).toPlainString() + "TB";
    }

    public static SdcardEntity getSdcardSize() {
        Object localObject = new SdcardEntity(0.0D, 0.0D, 0.0D, 0.0D);
        if ("mounted".equals(Environment.getExternalStorageState())) {
            localObject = new StatFs(Environment.getExternalStorageDirectory().getPath());
            double d1 = ((StatFs) localObject).getBlockSize();
            double d2 = ((StatFs) localObject).getBlockCount();
            double d3 = ((StatFs) localObject).getAvailableBlocks();
            localObject = new SdcardEntity(d2, d1 * d2, d3, d3 * d1);
            Log.d("", "===========block大小:" + d1 + ",block数目:" + d2 + ",总大小:" + d1 * d2 / 1024.0D + "KB");
            Log.d("", "==============可用的block数目：:" + d3 + ",剩余空间:" + d3 * d1 / 1024.0D + "KB");
        }
        return (SdcardEntity) localObject;
    }

    public static long getTotalExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            StatFs localStatFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long l = localStatFs.getBlockSize();
            return localStatFs.getBlockCount() * l;
        }
        return -1L;
    }

    public static long getTotalInternalMemorySize() {
        StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
        long l = localStatFs.getBlockSize();
        return localStatFs.getBlockCount() * l;
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }
}
