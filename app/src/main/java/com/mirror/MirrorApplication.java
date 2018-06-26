package com.mirror;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mirror.config.AppInfo;
import com.mirror.entity.TectUpdateEntity;
import com.mirror.entity.TopPopEntity;
import com.mirror.entity.VideoEntity;
import com.mirror.util.CrashExceptionHandler;
import com.mirror.util.FileUtil;
import com.mirror.util.SharedPerManager;
import com.mirror.view.ad.Pos1Bean;
import com.mirror.view.ad.Pos4Entity;
import com.mirror.view.ad.VideoLocaEntity;

import org.litepal.LitePalApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsjm on 2018/5/4.
 */

public class MirrorApplication extends Application {
    public static String USER_INFO = "userInfo";
    public static MirrorApplication instance;
    private static SharedPreferences mSharedPreferences;
    private Context context;
    List<VideoLocaEntity> listsMedia;
    private List<Pos4Entity> pos_4;
    public List<Pos1Bean> infos;
    public List<TopPopEntity.Pos13Entity> list_pop;
    public List<VideoEntity> list_hair;  //美发美体，美妆
    List<TectUpdateEntity.TectDetailEntity> list_tect_update;


    public static MirrorApplication getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        mSharedPreferences = getSharedPreferences(USER_INFO, 0);
        initOther();
    }

    private void initOther() {
        FileUtil.creatDirPathNoExists();
//        CrashExceptionHandler.getCrashInstance().init();
        LitePalApplication.initialize(this);
        listsMedia = new ArrayList();
        pos_4 = new ArrayList<>();
        infos = new ArrayList();
        list_pop = new ArrayList();
        list_hair = new ArrayList<>();
        list_tect_update = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }


    public void setList_tect_update(List<TectUpdateEntity.TectDetailEntity> list_tect_update) {
        this.list_tect_update = list_tect_update;
    }

    public List<TectUpdateEntity.TectDetailEntity> getList_tect_update() {
        return list_tect_update;
    }

    public List<VideoEntity> getList_hair() {
        return list_hair;
    }

    public void setList_hair(ArrayList localArrayList) {
        this.list_hair = localArrayList;
    }

    public void setList_pop(List<TopPopEntity.Pos13Entity> list_pop) {
        this.list_pop = list_pop;
    }

    public List<TopPopEntity.Pos13Entity> getList_pop() {
        if (list_pop.size() < 1) {
            list_pop.add(new TopPopEntity.Pos13Entity(AppInfo.DEFAULT_TOP_IMAGE));
        }
        return list_pop;
    }


    /***
     * 获取侧边栏广告
     * @return
     */
    public List<Pos1Bean> getInfos() {
        if (infos.size() < 1) {
            File file = new File(AppInfo.DEFAULT_RIGHT_IMAGE_DISONLINE);
            if (file.exists()) {
                infos.add(new Pos1Bean(AppInfo.DEFAULT_RIGHT_IMAGE_DISONLINE));
                infos.add(new Pos1Bean(AppInfo.DEFAULT_RIGHT_IMAGE_DISONLINE));
            }
        }
        return infos;
    }


    public void setInfos(List<Pos1Bean> paramList) {
        this.infos = paramList;
        Log.i("cdl", "====本地数据==" + paramList.size());
    }

    public void setPos_4(List<Pos4Entity> pos_4) {
        this.pos_4 = pos_4;
    }

    public List<Pos4Entity> getPos_4() {
        if (pos_4.size() < 1) {
            File file = new File(AppInfo.DEFAULT_BOTTOM_IMAGE_DIAONLINE);
            if (file.exists()) {
                pos_4.add(new Pos4Entity(AppInfo.DEFAULT_BOTTOM_IMAGE_DIAONLINE));
                pos_4.add(new Pos4Entity(AppInfo.DEFAULT_BOTTOM_IMAGE_DIAONLINE));
            }
        }
        return pos_4;
    }

    public List<VideoLocaEntity> getListsMedia() {
        return listsMedia;
    }

    public void setListsMedia(List<VideoLocaEntity> paramList) {
        listsMedia.clear();
        listsMedia = paramList;
    }


    //====================================================================

    public void saveData(String key, Object data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            Log.i("SharedPreferences", "设置的tag =" + key + "   //date = " + data);
            if (data instanceof Integer) {
                editor.putInt(key, (Integer) data);
            } else if (data instanceof Boolean) {
                editor.putBoolean(key, (Boolean) data);
            } else if (data instanceof String) {
                editor.putString(key, (String) data);
            } else if (data instanceof Float) {
                editor.putFloat(key, (Float) data);
            } else if (data instanceof Long) {
                editor.putLong(key, (Long) data);
            }
        } catch (Exception e) {
            Log.i("SharedPreferences", "获取的的tag =" + key + "   //date = " + e.toString());
        }
        editor.commit();
    }

    public Object getData(String key, Object defaultObject) {
        try {
            Log.i("SharedPreferences", "获取的的tag =" + key + "   //date = " + defaultObject.toString());
            if (defaultObject instanceof String) {
                return mSharedPreferences.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return mSharedPreferences.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return mSharedPreferences.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return mSharedPreferences.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return mSharedPreferences.getLong(key, (Long) defaultObject);
            }
        } catch (Exception e) {
            Log.i("SharedPreferences", "获取的的tag =" + key + "   //date = " + e.toString());
            return null;
        }
        return null;
    }


}
