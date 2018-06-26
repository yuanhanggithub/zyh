package com.mirror.activity.main;


/***
 * 业务层监听接口
 */
public interface MainNewModelListener {

    /***
     * 获取侧边栏广告状态监听
     * @param isTrue
     */
    void getAdRightState(boolean isTrue);

    void getAdTopState(boolean isTrue);

    void getAdBottomState(boolean isTrue);

}
