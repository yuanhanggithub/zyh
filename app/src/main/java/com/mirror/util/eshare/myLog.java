package com.mirror.util.eshare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

public final class myLog {

    public static boolean isPrint = true;
    private static String defaultTag = "eshare";

    private myLog() {
    }

    public static void setdefaultTag(String defaultTag) {
        defaultTag = defaultTag;
    }

    public static int i(Object o) {
        return isPrint && o != null ? Log.i(defaultTag,
                o.toString()) : -1;
    }

    public static int e(String msg) {
        return isPrint && msg != null ? Log.e(defaultTag, msg)
                : -1;
    }

    /*********************** Log ***************************/
    public static int v(String msg) {
        return isPrint && msg != null ? Log.v(defaultTag, msg)
                : -1;
    }

    public static int d(String msg) {
        return isPrint && msg != null ? Log.d(defaultTag, msg)
                : -1;
    }

    public static int i(String msg) {
        return isPrint && msg != null ? Log.i(defaultTag, msg)
                : -1;
    }

    public static int w(String msg) {
        return isPrint && msg != null ? Log.w(defaultTag, msg)
                : -1;
    }

    /*********************** Log with object list ***************************/
    public static int v(Object... msg) {
        return isPrint ? Log.v(defaultTag, getLogMessage(msg))
                : -1;
    }

    public static int d(Object... msg) {
        return isPrint ? Log.d(defaultTag, getLogMessage(msg))
                : -1;
    }

    public static int i(Object... msg) {
        return isPrint ? Log.i(defaultTag, getLogMessage(msg))
                : -1;
    }

    public static int w(Object... msg) {
        return isPrint ? Log.w(defaultTag, getLogMessage(msg))
                : -1;
    }

    public static int e(Object... msg) {
        return isPrint ? Log.e(defaultTag, getLogMessage(msg))
                : -1;
    }

    private static String getLogMessage(Object... msg) {
        if (msg != null && msg.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object s : msg) {
                if (msg != null)
                    sb.append(s.toString());
            }
            return sb.toString();
        }
        return "";
    }

    /*********************** Log with Throwable ***************************/
    public static int v(String msg, Throwable tr) {
        return isPrint && msg != null ? Log.v(defaultTag, msg, tr)
                : -1;
    }

    public static int d(String msg, Throwable tr) {
        return isPrint && msg != null ? Log.d(defaultTag, msg, tr)
                : -1;
    }

    public static int i(String msg, Throwable tr) {
        return isPrint && msg != null ? Log.i(defaultTag, msg, tr)
                : -1;
    }

    public static int w(String msg, Throwable tr) {
        return isPrint && msg != null ? Log.w(defaultTag, msg, tr)
                : -1;
    }

    public static int e(String msg, Throwable tr) {
        return isPrint && msg != null ? Log.e(defaultTag, msg, tr)
                : -1;
    }

    /*********************** defaultTag use Object defaultTag ***************************/
    public static int v(Object defaultTag, String msg) {
        return isPrint ? Log.v(defaultTag.getClass()
                .getSimpleName(), msg) : -1;
    }

    public static int d(Object defaultTag, String msg) {
        return isPrint ? Log.d(defaultTag.getClass()
                .getSimpleName(), msg) : -1;
    }

    public static int i(Object defaultTag, String msg) {
        return isPrint ? Log.i(defaultTag.getClass()
                .getSimpleName(), msg) : -1;
    }

    public static int w(Object defaultTag, String msg) {
        return isPrint ? Log.w(defaultTag.getClass()
                .getSimpleName(), msg) : -1;
    }

    public static int e(Object defaultTag, String msg) {
        return isPrint ? Log.e(defaultTag.getClass()
                .getSimpleName(), msg) : -1;
    }

    public static void writeToFile(String file, byte[] conent, int len) {
        FileOutputStream out = null;
        try {
            File filee = new File(file);
            if (!filee.exists())
                filee.createNewFile();
            out = new FileOutputStream(file, true);
            out.write(conent, 0, len);
            ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToFile(String file, byte[] conent) {
        FileOutputStream out = null;
        try {
            File filee = new File(file);
            if (!filee.exists())
                filee.createNewFile();
            out = new FileOutputStream(file, true);
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printHexString(byte[] b, int lenth) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lenth; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            builder.append(" " + hex.toUpperCase());
        }
        Log.e("luoxiangbin", builder.toString());
    }
}