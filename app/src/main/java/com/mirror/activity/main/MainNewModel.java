package com.mirror.activity.main;


import android.content.Context;

/***
 * 主界面业务层工具类
 */
public interface MainNewModel {

    /***
     * 获取侧边栏广告
     */
    void getAdRightImages(Context context, MainNewModelListener listener);

    void getTopRightImages(Context context, MainNewModelListener listener);

    void getBottomAdImages(Context context, MainNewModelListener listener);


}
