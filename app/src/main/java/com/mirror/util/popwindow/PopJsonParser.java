package com.mirror.util.popwindow;

import android.util.Log;

import com.google.gson.Gson;
import com.mirror.MirrorApplication;
import com.mirror.view.ad.PopviewEntity;
import com.mirror.view.ad.Pos4Entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class PopJsonParser {
    public static void addDefaultInfo() {
        ArrayList localArrayList = new ArrayList();
        Pos4Entity localPos4Entity = new Pos4Entity();
        localPos4Entity.setPicpath("http://cdn.magicmirrormedia.cn/img/menu_bottom_default.png");
        localArrayList.add(localPos4Entity);
        MirrorApplication.getInstance().setPos_4(localArrayList);
    }

    public static void parserPop(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            int state = jsonObject.getInt("state");
            if (state == 1) {
                String data = jsonObject.getString("data");
                PopviewEntity entity = new Gson().fromJson(data, PopviewEntity.class);
                List<Pos4Entity> pos_4 = entity.getPos_4();
                MirrorApplication.getInstance().setPos_4(pos_4);
                Log.e("gif", "====" + ((Pos4Entity) pos_4.get(0)).getPicpath());
            } else {
                addDefaultInfo();
            }
        } catch (Exception e) {
            addDefaultInfo();
        }
    }
}
