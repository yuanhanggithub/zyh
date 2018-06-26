package com.mirror.util;

public class CurrentRunUtil {

    public static boolean isRunForstPopJujle(String paramString) {
        boolean bool = jujleThirdRunFrost(paramString);
        if (paramString.contains("com.ecloud.eairplay")) {
            bool = false;
        }
        if (paramString.contains("com.ecloud.emedia")) {
            bool = false;
        }
        if (paramString.contains("com.ecloud.eshare.server")) {
            bool = false;
        }
        return bool;
    }

    public static boolean isRunForstProjectJujle(String paramString) {
        boolean bool = jujleThirdRunFrost(paramString);
        if (paramString.contains("com.ecloud.eairplay")) {
            bool = true;
        } else if (paramString.contains("com.ecloud.emedia")) {
            bool = true;
        } else if (paramString.contains("com.ecloud.eshare.server")) {
            bool = true;
        }
        return bool;
    }

    /***
     * 判断第三方程序是否运行在前台
     * @param paramString
     * @return
     */
    public static boolean jujleThirdRunFrost(String paramString) {
        boolean isThirdRunFirst = false;
        if (paramString.contains("com.elinkway.tvlive2")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("com.dianshijia.newlive")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("com.fengmizhibo.live")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("com.bilibili.tv")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("hdpfans.com")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("com.happy.wonderland")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("com.lekan.tv.kids.activity")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("com.hoaix.childplayer.tv")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("air.tv.douyu.android")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("com.douyu.xl.douyutv")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("com.panda.videolivetv")) {
            isThirdRunFirst = true;
        } else if (paramString.contains("com.duowan.kiwitv")) {
            isThirdRunFirst = true;
        }
        return isThirdRunFirst;
    }
}
