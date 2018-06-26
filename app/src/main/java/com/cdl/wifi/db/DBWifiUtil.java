package com.cdl.wifi.db;


import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mirror.config.AppConfig;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class DBWifiUtil {


    public static final String TAG = "DBWifiUtil";
    Context context;
    List<DBWifiEntity> wifilists = new ArrayList<>();

    public DBWifiUtil(Context context) {
        this.context = context;
    }

    /***
     * 查询数据库
     * @return
     */
    public List<DBWifiEntity> queryDbInfo() {
        wifilists.clear();
        wifilists = DataSupport.findAll(DBWifiEntity.class);

        for (int i = 0; i < wifilists.size(); i++) {
            Log.i(TAG, "查询到的信息 ：" + wifilists.get(i).toString());
        }
        return wifilists;
    }

    /***
     * 添加数据
     * @return
     */
    public boolean addWifiInfo(DBWifiEntity entity) {
        try {
            Log.i(TAG, "====添加数据===" + entity.toString());
            wifilists = queryDbInfo();
            for (int i = 0; i < wifilists.size(); i++) {
                String wifiName = wifilists.get(i).getWifiname();
                if (wifiName.contains(entity.getWifiname())) {
                    //如果表单中有数，就修改数据
                    Log.i(TAG, "====表单中有数据，修改数据");
                    return updateByName(entity);
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "====添加数据异常===" + e.toString());
        }
        return entity.save();
    }


    /***
     * 根据wifiName来更新wifi密码
     * @param entity
     */
    public boolean updateByName(DBWifiEntity entity) {
        try {
            ContentValues values = new ContentValues();
            String modifyNum = entity.getWifipsd();
            String wifiName = entity.getWifiname();
            values.put("wifipsd", modifyNum);
            int updateNum = DataSupport.updateAll(DBWifiEntity.class, values, "wifiname = ?", wifiName);
            Log.e(TAG, "===修改数据state ：" + updateNum);
            if (updateNum == 0) {
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "===修改数据异常：" + e.toString());

        }
        return true;
    }

    /***
     * 查询数据库
     * @return
     */
    public DBWifiEntity queryByName(String wifiname) {
        DBWifiEntity entity = null;
        try {
            List<DBWifiEntity> list = queryDbInfo();
            Log.i(TAG, "===========011 ==========list.size==" + list.size());
            if (list.size() < 1) {
                return entity;
            }
            for (int i = 0; i < list.size(); i++) {
                DBWifiEntity entitySave = list.get(i);
                String name = entitySave.getWifiname().trim();
                String password = entitySave.getWifipsd();
                String wifiType = entitySave.getWifiType();
                Log.i(TAG, "===========1111============" + name + "  /password = " + password + " /" + wifiType);
                if (!TextUtils.isEmpty(name) && name.equals(wifiname)) {
                    entity = new DBWifiEntity(name, password, wifiType);
                    Log.i(TAG, "===========22222===保存数据到entity=========");
                }
            }
            Log.i(TAG, "===========333=========返回数据===" + entity.getWifiname() + "/ " + entity.getWifipsd());
        } catch (Exception e) {
            Log.i(TAG, "===========4444===========" + e.toString());
        }
        return entity;
    }


    // 全部删除
    public boolean deleteAll() {
        try {
            int delNUm = DataSupport.deleteAll(DBWifiEntity.class);
            if (delNUm == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.i(TAG, "==删除数据路失败==" + e.toString());
        }
        return true;
    }


    public boolean deleteByName(String wifiname) {
        boolean isDel = false;
        try {
            wifilists = queryDbInfo();
            Log.i(TAG, "數據查询==wifilists.sizew=" + wifilists.size());
            if (wifilists.size() < 1) {
                Log.i(TAG, "查询列表数据《1 ， 直接返回数据");
                isDel = false;
            } else {
                for (int i = 0; i < wifilists.size(); i++) {
                    String wifiName = wifilists.get(i).getWifiname();
                    if (wifiName.trim().contains(wifiname.trim())) {
                        Log.i(TAG, "检测到有数据，去删除数据");
                        int delNum = DataSupport.deleteAll(DBWifiEntity.class, "wifiname=?", wifiname);
                        Log.i(TAG, "检测到有数据，去删除数据==" + delNum);
                        if (delNum == 0) {
                            isDel = false;
                        } else {
                            isDel = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        Log.i(TAG, "==删除数据结果===" + isDel);
        return isDel;
    }
}
