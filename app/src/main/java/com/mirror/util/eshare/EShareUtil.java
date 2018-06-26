package com.mirror.util.eshare;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;


/**
 * @author litianji@ee-share.com
 * @time 2016-9-1 下午2:05:19
 */
public final class EShareUtil {

    ////////////////////配置项////////////////////
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    @Deprecated
    /** 配置文件路径 */
    public static final String ESHARE_PATH = SDCARD_PATH + "/.com.eshare.prop";
    @Deprecated
    /** 配置文件名称 */
    public static final String ESHARE_FILE_NAME = ESHARE_PATH + "/eshare.prop";

    /**
     * 设备名称
     */
    public static final String KEY_DEVICE_NAME = "eshare_device_name";
    /**
     * 连接密码
     */
    public static final String KEY_PIN_CODE = "eshare_connect_code";
    /**
     * 是否需要连接密码
     */
    public static final String KEY_CODE_STATE = "eshare_encrypt_state";
    /**
     * 是否自动刷新
     */
    public static final String KEY_AUTO_REFRESH = "eshare_refresh_state";
    /**
     * 是否显示悬浮窗
     */
    public static final String KEY_SHOW_WINDOW = "eshare_show_window";
    /**
     * 是否允许移动端触控
     */
    public static final String KEY_MIRROR_TOUCH = "eshare_mirror_touch";
    /**
     * 是否打开传屏控制
     */
    public static final String KEY_CONTROL_CONFIRM = "eshare_control_confirm";
    /**
     * 是否允许发送端投屏
     */
    public static final String KEY_ALLOW_CAST = "eshare_allow_cast";

    /**
     * 是否开机自启动
     */
    public static final String KEY_BOOT_START = "com.eshare.settings.key.boot_start";

    ////////////////////默认参数////////////////////
    /**
     * 默认参数：是否需要连接密码
     */
    public static final boolean DEFAULT_CODE_STATE = false;
    /**
     * 默认参数：是否自动刷新
     */
    public static final boolean DEFAULT_AUTO_REFRESH = true;
    /**
     * 默认参数：是否显示悬浮窗
     */
    public static final boolean DEFAULT_SHOW_WINDOW = true;
    /**
     * 默认参数：是否显示分割线
     */
    public static final boolean DEFAULT_SHOW_DIVIDER = true;
    /**
     * 默认参数：连接密码位数
     */
    public static final int DEFAULT_CODE_SIZE = 6;
    public static final String DEFAULT_DIGIT_FORMAT = "%06d";
    public static final int DEFAULT_MAX_CODE = 1000000;
    /**
     * 默认参数：是否允许移动端触控
     */
    public static final boolean DEFAULT_MIRROR_TOUCH = true;
    /**
     * 默认参数：是否打开传屏控制
     */
    public static final boolean DEFAULT_CONTROL_CONFIRM = false;
    /**
     * 是否开机自启动
     */
    public static final int BOOT_START_OPEN = 0;
    public static final int BOOT_START_CLOSE = 1;
    /**
     * 默认参数：是否开机自启动
     */
    public static final int DEF_BOOT_START = BOOT_START_OPEN;

    /**
     * 默认参数：是否允许发送端投屏
     */
    public static final boolean DEFAULT_ALLOW_CAST = true;


    ////////////////////其它常数////////////////////
    /**
     * 密码固定时的格式
     */
    public static final String CODE_FORMAT_FIXED = "<i>%s</i>";
    /**
     * 密码自动刷新时的格式
     */
    public static final String CODE_FORMAT_REFRESH = "%s";


    ////////////////////广播////////////////////
    /**
     * 广播：修改设备名称
     */
    public static final String ACTION_DEVICE_NAME_CHANGED = "com.eshare.action.DEVICE_NAME_CHANGED";
    public static final String EXTRA_NEW_DEVICE_NAME = "com.eshare.extra.NEW_DEVICE_NAME";
    /**
     * 广播：修改连接密码
     */
    public static final String ACTION_PIN_CODE_CHANGED = "com.eshare.action.CONNECT_CODE_CHANGED";
    public static final String EXTRA_NEW_PIN_CODE = "com.eshare.extra.NEW_CONNECT_CODE";
    /**
     * 广播：修改连接密码状态
     */
    public static final String ACTION_CODE_STATE_CHANGED = "com.eshare.action.ENCRYPT_STATE_CHANGED";
    public static final String EXTRA_NEW_CODE_STATE = "com.eshare.extra.NEW_ENCRYPT_STATE";
    /**
     * 广播：修改是否允许发送端传屏
     */
    public static final String ACTION_CAST_STATE_CHANGED = "com.eshare.action.CAST_STATE_CHANGED";
    public static final String EXTRA_NEW_CAST_STATE = "com.eshare.extra.NEW_CAST_STATE";

    private final static String KEY_KEEP_BACKGROUND_RUNNING = "key_keep_background_running";

    private EShareUtil() {

    }


    ////////////////////获取配置////////////////////

    public static String getDeviceName(Context context) {
        String result = getSystemSetting(context, KEY_DEVICE_NAME);
        if (TextUtils.isEmpty(result)) {
            result = getProperty(KEY_DEVICE_NAME);
        }
        return result;
    }

    public static String getPinCode(Context context) {
        String result = getSystemSetting(context, KEY_PIN_CODE);
        if (TextUtils.isEmpty(result)) {
            result = getProperty(KEY_PIN_CODE);
        }
        return result;
    }

    public static boolean isCodeState(Context context) {
        String result = getSystemSetting(context, KEY_CODE_STATE);
        if (TextUtils.isEmpty(result)) {
            result = getProperty(KEY_CODE_STATE);
        }
        if (!TextUtils.isEmpty(result)) {
            try {
                return Boolean.valueOf(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return DEFAULT_CODE_STATE;
    }

    public static boolean isAutoRefresh(Context context) {
        String result = getSystemSetting(context, KEY_AUTO_REFRESH);
        if (TextUtils.isEmpty(result)) {
            result = getProperty(KEY_AUTO_REFRESH);
        }
        if (!TextUtils.isEmpty(result)) {
            try {
                return Boolean.valueOf(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return DEFAULT_AUTO_REFRESH;
    }

    public static boolean isShowWindow(Context context) {
        String result = getSystemSetting(context, KEY_SHOW_WINDOW);
        if (TextUtils.isEmpty(result)) {
            result = getProperty(KEY_SHOW_WINDOW);
        }
        if (!TextUtils.isEmpty(result)) {
            try {
                return Boolean.valueOf(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return DEFAULT_SHOW_WINDOW;
    }

    public static boolean isMirrorTouch(Context context) {
        String result = getSystemSetting(context, KEY_MIRROR_TOUCH);
        if (TextUtils.isEmpty(result)) {
            result = getProperty(KEY_MIRROR_TOUCH);
        }
        if (!TextUtils.isEmpty(result)) {
            try {
                return Boolean.valueOf(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return DEFAULT_MIRROR_TOUCH;
    }

    public static boolean isControlConfirm(Context context) {
        String result = getSystemSetting(context, KEY_CONTROL_CONFIRM);
        if (TextUtils.isEmpty(result)) {
            result = getProperty(KEY_CONTROL_CONFIRM);
        }
        if (!TextUtils.isEmpty(result)) {
            try {
                return Boolean.valueOf(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return DEFAULT_CONTROL_CONFIRM;
    }

    public static boolean isAllowCast(Context context) {
        String result = getSystemSetting(context, KEY_ALLOW_CAST);
        if (TextUtils.isEmpty(result)) {
            result = getProperty(KEY_ALLOW_CAST);
        }
        if (!TextUtils.isEmpty(result)) {
            try {
                return Boolean.valueOf(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return DEFAULT_ALLOW_CAST;
    }

    public static boolean getBootStart(Context context) {
        return getSystemSetting(context, KEY_BOOT_START, DEF_BOOT_START) == BOOT_START_OPEN;
    }

    public static boolean getKeepBackgroundRunning(Context context) {
        return getSystemSetting(context, KEY_KEEP_BACKGROUND_RUNNING, 0) == 1;
    }

    ////////////////////设置配置////////////////////

    public static boolean setDeviceName(Context context, String deviceName) {
        return putSystemSetting(context, KEY_DEVICE_NAME, deviceName);
    }

    public static boolean setPinCode(Context context, String pinCode) {
        return putSystemSetting(context, KEY_PIN_CODE, pinCode);
    }

    public static boolean setCodeState(Context context, boolean isCodeState) {
        return putSystemSetting(context, KEY_CODE_STATE, String.valueOf(isCodeState));
    }

    public static boolean setAutoRefresh(Context context, boolean isAutoRefresh) {
        return putSystemSetting(context, KEY_AUTO_REFRESH, String.valueOf(isAutoRefresh));
    }

    public static boolean setShowWindow(Context context, boolean isShowWindow) {
        return putSystemSetting(context, KEY_SHOW_WINDOW, String.valueOf(isShowWindow));
    }

    public static boolean setMirrorTouch(Context context, boolean isMirrorTouch) {
        return putSystemSetting(context, KEY_MIRROR_TOUCH, String.valueOf(isMirrorTouch));
    }

    public static boolean setControlConfirm(Context context, boolean isControlConfirm) {
        return putSystemSetting(context, KEY_CONTROL_CONFIRM, String.valueOf(isControlConfirm));
    }

    public static boolean setAllowCast(Context context, boolean isAllowCast) {
        return putSystemSetting(context, KEY_ALLOW_CAST, String.valueOf(isAllowCast));
    }

    public static boolean setBootStart(Context context, boolean isBootStart) {
        return putSystemSetting(context, KEY_BOOT_START, isBootStart ? BOOT_START_OPEN : BOOT_START_CLOSE);
    }

    ////////////////////设置与获取配置////////////////////

    public static boolean putSystemSetting(Context context, String name, String value) {
        try {
            Settings.System.putString(context.getContentResolver(), name, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getSystemSetting(Context context, String name) {
        try {
            return Settings.System.getString(context.getContentResolver(), name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean putSystemSetting(Context context, String name, int value) {
        try {
            Settings.System.putInt(context.getContentResolver(), name, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getSystemSetting(Context context, String name, int defValue) {
        try {
            return Settings.System.getInt(context.getContentResolver(), name, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    @Deprecated
    @SuppressWarnings("unused")
    private static synchronized boolean setProperty(String key, String value) {
        File folder = new File(ESHARE_PATH);
        if (!folder.exists())
            folder.mkdirs();
        if (TextUtils.isEmpty(value))
            return false;

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            Properties prop = new Properties();
            try {
                fis = new FileInputStream(ESHARE_FILE_NAME);
                prop.load(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            fos = new FileOutputStream(ESHARE_FILE_NAME, false);
            prop.setProperty(key, value);
            prop.store(fos, null);

            return true;
        } catch (IOException e) {

        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Deprecated
    private static synchronized String getProperty(String key) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(ESHARE_FILE_NAME);
            Properties prop = new Properties();
            prop.load(fis);
            String value = prop.getProperty(key);

            if (!TextUtils.isEmpty(value))
                return value;
        } catch (IOException e) {

        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    ////////////////////其它工具方法////////////////////

    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }


}
