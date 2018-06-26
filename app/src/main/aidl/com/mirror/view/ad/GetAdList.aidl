package com.mirror.view.ad;

import com.mirror.view.ad.Pos1Bean;
import com.mirror.view.ad.VideoLocaEntity;
import com.mirror.view.ad.Pos4Entity;

interface GetAdList {
    //获取侧边栏列表
   List<Pos1Bean> getListInf();
   //获取本地视屏列表
   List<VideoLocaEntity> getVideoLocalList();
    //获取设备的TOKEN
   String getUserToken();
    //获取底部广告信息
   List<Pos4Entity> getBottomList();
}
