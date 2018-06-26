package com.mirror.util.parsener;
import com.google.gson.Gson;
import com.mirror.entity.ADVideoInfo;
import com.mirror.entity.DefaultAdVide;
import com.mirror.view.ad.ADInfo;


public class GsonParse {
    public static final String TAG = "MirrorService";
    private static Gson instance;

    public static Gson getInstance() {
        if (instance == null) {
            instance = new Gson();
        }
        return instance;
    }
    public static ADInfo parseADInfo(String paramString) {

        return (ADInfo) new Gson().fromJson(paramString, ADInfo.class);
    }

    public static DefaultAdVide parserDefauleAd(String paramString) {
        return (DefaultAdVide) new Gson().fromJson(paramString, DefaultAdVide.class);
    }

    public static ADVideoInfo parserVideoInfo(String paramString) {
        return (ADVideoInfo) new Gson().fromJson(paramString, ADVideoInfo.class);
    }
}
