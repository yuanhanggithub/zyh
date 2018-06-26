package com.mirror.util.system;

import java.io.File;
import java.io.FileReader;

public class PowerManagerUtil {
    private static final String TAG = "PowerManagerUtil";

    public static void do_exec(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            String str = paramString + "\nexit\n";
            localProcess.getOutputStream().write(str.getBytes());
            if (localProcess.waitFor() != 0) {
                System.out.println("cmd=" + paramString + " error!");
                throw new SecurityException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String isOpenLight() {
        String str = "";
        try {
            File file = new File("/sys/devices/fb.11/graphics/fb0/pwr_bl");
            FileReader reader = new FileReader(file);
            char[] bb = new char[1024];
            int n;
            while ((n = reader.read(bb)) != -1) {
                str += new String(bb, 0, n);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void openOrClose(int paramInt) {
        try {
            writeFile(paramInt);
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static void writeFile(int paramInt) {
        try {
            File localFile = new File("/sys/devices/fb.11/graphics/fb0/pwr_bl");
            localFile.setExecutable(true);
            localFile.setReadable(true);
            localFile.setWritable(true);
            if (paramInt == 0) {
                do_exec("busybox echo 0 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
                return;
            }
            if (paramInt == 1) {
                do_exec("busybox echo 1 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
                return;
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }


}
