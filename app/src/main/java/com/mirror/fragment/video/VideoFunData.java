package com.mirror.fragment.video;


import com.mirror.R;
import com.mirror.config.AppInfo;
import com.mirror.entity.InnerAppEntity;
import com.mirror.fragment.util.PlayStateListener;

import java.util.ArrayList;
import java.util.List;

public class VideoFunData {
    public static List<String> getBeautyTitleDate() {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("潮流趋势");
        localArrayList.add("技术提升");
        localArrayList.add("营销技巧");
        localArrayList.add("作品展示");
        return localArrayList;
    }

    public static List<InnerAppEntity> getListDate() {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new InnerAppEntity(AppInfo.HDP_NAME, AppInfo.HDP_ICON, AppInfo.HDP_HOME_DOWNURL, AppInfo.HDP_PACKAGE, PlayStateListener.TAG_TV_HDP, InnerAppEntity.APP_TV_TAG));
//        localArrayList.add(new InnerAppEntity(AppInfo.TV_HOME_NAME, AppInfo.TV_HOME_ICON, AppInfo.TV_HOME_DOWNURL, AppInfo.TV_HOME_PACKAGE, PlayStateListener.TAG_TV_HOME, InnerAppEntity.APP_TV_TAG));
        localArrayList.add(new InnerAppEntity(AppInfo.BEE_NAME_APP, AppInfo.BEE_ICON, AppInfo.BEE_DOWNURL, AppInfo.BEE_PACKAGE, PlayStateListener.TAG_TV_BEE, InnerAppEntity.APP_TV_TAG));
        localArrayList.add(new InnerAppEntity(AppInfo.BILIBILI_NAME, AppInfo.BILIBILI_ICON, AppInfo.BILIBILI_DOWNURL, AppInfo.BILIBILI_PACKAGE, PlayStateListener.TAG_TV_BILIBILI, InnerAppEntity.APP_TV_TAG));
        localArrayList.add(new InnerAppEntity(AppInfo.TV_KW_MUSIC_NAME, AppInfo.TV_KW_MUSIC_ICON, AppInfo.TV_KW_MUSIC_DOWNURL, AppInfo.TV_KW_MUSIC_PACKAGE, PlayStateListener.TAG_MUSIC_KUWO, InnerAppEntity.APP_TV_TAG));
        localArrayList.add(new InnerAppEntity(AppInfo.LIUYI_NAME, AppInfo.LIUYI_ICON, AppInfo.LIUYI_DOWNURL, AppInfo.LIUYI_PACKAGE, PlayStateListener.TAG_CARTON_LIUYI, InnerAppEntity.APP_CARTON_TAG));
        localArrayList.add(new InnerAppEntity(AppInfo.LEKAN_NAME, AppInfo.LEKAN_ICON, AppInfo.LEKAN_DOWNURL, AppInfo.LEKAN_PACKAGE, PlayStateListener.TAG_CARTON_LEKAN, InnerAppEntity.APP_CARTON_TAG));
        localArrayList.add(new InnerAppEntity(AppInfo.TV_ANIMAL_NAME, AppInfo.TV_ANIMAL_ICON, AppInfo.TV_ANIMAL_DOWNURL, AppInfo.TV_ANIMAL_PACKAGE, PlayStateListener.TAG_CARTON_ANIMAL, InnerAppEntity.APP_CARTON_TAG));
        localArrayList.add(new InnerAppEntity(AppInfo.DOUYU_TV_NAME_APP, AppInfo.DOUYU_TV_ICON, AppInfo.DOUYU_TV_DOWNURL, AppInfo.DOUYU_TV_PACKAGE, PlayStateListener.TAG_GAME_DOYU, InnerAppEntity.APP_GAME_TAG));
        localArrayList.add(new InnerAppEntity(AppInfo.PANDER_TV_NAME_APP, AppInfo.PANDER_TV_ICON, AppInfo.PANDER_TV_DOWNURL, AppInfo.PANDER_TV_PACKAGE, PlayStateListener.TAG_GAME_PANDA, InnerAppEntity.APP_GAME_TAG));
        localArrayList.add(new InnerAppEntity(AppInfo.HUYA_TV_NAME_APP, AppInfo.HUYA_TV_ICON, AppInfo.HUYA_TV_DOWNURL, AppInfo.HUYA_TV_PACKAGE, PlayStateListener.TAG_GAME_HUYA, InnerAppEntity.APP_GAME_TAG));
        localArrayList.add(new InnerAppEntity("APP STORE", R.mipmap.video_icon_add, "", "", 2566, InnerAppEntity.APP_ADD_TAG));
        return localArrayList;
    }

    public static List<String> getTitleDate() {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("全部应用");
        localArrayList.add("电视直播");
        localArrayList.add("卡通动漫");
        localArrayList.add("游戏直播");
        return localArrayList;
    }
}
