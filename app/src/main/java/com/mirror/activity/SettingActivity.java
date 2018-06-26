package com.mirror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cdl.wifi.activity.WifiFragmentActivity;
import com.mirror.R;
import com.mirror.adapter.SettingAdapter;
import com.mirror.config.AppConfig;
import com.mirror.config.AppInfo;
import com.mirror.entity.ModelMainItem;
import com.mirror.guide.GuideActivity;
import com.mirror.update.UpdateActiivty;
import com.mirror.util.APKUtil;
import com.mirror.util.Biantai;
import com.mirror.util.DisPlayUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownStateListener;
import com.mirror.util.down.MirrorDownUtil;
import com.mirror.view.MyToastView;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    APKUtil apkutil;
    private List<ModelMainItem> mData;
    MirrorDownUtil mirrorDownUtil;
    GridView grid_setting;
    SettingAdapter adapter;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_setting);
        initData();
        initView();
    }

    private void initView() {
        grid_setting = (GridView) findViewById(R.id.grid_setting);
        grid_setting.setNumColumns(5);
        adapter = new SettingAdapter(SettingActivity.this, mData);
        grid_setting.setAdapter(adapter);
        grid_setting.setOnItemClickListener(this);
    }

    private void downSocketFileApp() {
        if (this.mirrorDownUtil == null) {
            mirrorDownUtil = new MirrorDownUtil(this);
        }
        mirrorDownUtil.downFileStart(AppInfo.SOCKET_FILE_DOWN_URL,
                AppInfo.SOCKET_SAVE_URL,
                "是否下载 <闪传> 软件 ? ",
                true, new DownStateListener() {
                    public void downStateInfo(DownFileEntity entity) {
                        if (entity.getDownState() == DownFileEntity.DOWN_STATE_SUCCESS) {
                            if (apkutil == null) {
                                apkutil = new APKUtil(SettingActivity.this);
                            }
                            apkutil.installApk(entity.getSavePath());
                        } else if (entity.getDownState() == DownFileEntity.DOWN_STATE_FAIED) {
                            MyToastView.getInstance().Toast(SettingActivity.this, entity.getDesc());
                        }
                    }
                });
    }

    private void initData() {
        mData = new ArrayList();
        mData.add(new ModelMainItem(ModelMainItem.SETTING_WIFI, R.mipmap.setting_fiwi));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_VERSION_DISONLINE, R.mipmap.setting_disonline));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_BELLTH, R.mipmap.setting_blue));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_NET_SPEED, R.mipmap.setting_speed));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_CACHE_CLEAR, R.mipmap.setting_clear));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_VIDEO_DISPLAY, R.mipmap.setting_port));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_APP_MANAGER, R.mipmap.setting_app_manager));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_AP_OPEN, R.mipmap.setting_hot_wifi));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_SD_MANAGER, R.mipmap.setting_icon_last));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_SYSTEM_UPDATE, R.mipmap.setting_icon_appupdate));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_ESHARE_INFO, R.mipmap.setting_eshare_icon));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_OLD_TEST, R.mipmap.setting_old_test_icon));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_SYS_SETTING, R.mipmap.setting_setting_icon));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_INTRO_USER, R.mipmap.setting_icon_introduce));
        mData.add(new ModelMainItem(ModelMainItem.SETTING_CLOSE_TIMER, R.mipmap.setting_off_time));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.w("haha", "=====當前的年紀的位置==" + position);
        if (Biantai.isOneClick()) {
            MyToastView.getInstance().Toast(this, "点击太快了");
            return;
        }
        ModelMainItem modelMainItem = mData.get(position);
        switch (modelMainItem.getName()) {
            case ModelMainItem.SETTING_VERSION_DISONLINE:  //拖网模式
                startActivity(new Intent(this, DisOnlineActivity.class));
                break;
            case ModelMainItem.SETTING_WIFI:  //wifi设置
                startActivity(new Intent(this, WifiFragmentActivity.class));
                break;
            case ModelMainItem.SETTING_BELLTH:  //蓝牙设置
                startActivity(new Intent("android.settings.BLUETOOTH_SETTINGS"));
                break;
            case ModelMainItem.SETTING_NET_SPEED:  //网络测试
                if (!NetWorkUtils.isNetworkConnected(this)) {
                    MyToastView.getInstance().Toast(this, "网络异常，请检查");
                    return;
                }
                startActivity(new Intent(this, ActivityNetSpeed.class));
                break;
            case ModelMainItem.SETTING_CACHE_CLEAR: //缓存清理
                startActivity(new Intent(this, SpeedActivity.class));
                break;
            case ModelMainItem.SETTING_VIDEO_DISPLAY: //作品展示
                String socketUrl = AppInfo.SOCKET_APK_PACKAGENAME;
                if (APKUtil.ApkState(this, socketUrl)) {
                    DisPlayUtil.startApp(this, socketUrl);
                    return;
                }
                downSocketFileApp();
                break;
            case ModelMainItem.SETTING_APP_MANAGER:
                startActivity(new Intent(this, AppAlreadActivity.class));
                break;
            case ModelMainItem.SETTING_AP_OPEN:  //开启热点
                if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {
                    MyToastView.getInstance().Toast(this, "老设备没有此功能");
                    return;
                }
                Intent intentHot = new Intent(this, WifiHotSetActvity.class);
                startActivity(intentHot);
                break;
            case ModelMainItem.SETTING_SD_MANAGER:  //SD卡管理
                DisPlayUtil.gotoSysSdPath(SettingActivity.this);
                break;
            case ModelMainItem.SETTING_SYSTEM_UPDATE:  //系统升级
                if (!NetWorkUtils.isNetworkConnected(this)) {
                    MyToastView.getInstance().Toast(this, "网络异常，请检查");
                    return;
                }
                if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {
                    MyToastView.getInstance().Toast(this, "老设备没有此功能");
                    return;
                }
                startActivity(new Intent(SettingActivity.this, UpdateActiivty.class));
                return;
            case ModelMainItem.SETTING_ESHARE_INFO:  //投屏信息
                if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {
                    MyToastView.getInstance().Toast(this, "老设备没有此功能");
                    return;
                }
                startActivity(new Intent(this, ScreenEshareActivity.class));
                break;
            case ModelMainItem.SETTING_OLD_TEST:  //老化测试
                startActivity(new Intent(this, OldTestActivity.class));
                break;
            case ModelMainItem.SETTING_SYS_SETTING: //系统设置
                if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {
                    MyToastView.getInstance().Toast(this, "老设备没有此功能");
                    Toast.makeText(SettingActivity.this, "============================", Toast.LENGTH_SHORT);
                    return;
                }
                startSettingInfo();
                break;
            case ModelMainItem.SETTING_INTRO_USER:  //使用引导
                Intent intent12 = new Intent(this, GuideActivity.class);
                intent12.putExtra("TAG_DEFAULT", 1);
                startActivity(intent12);
                break;
            case ModelMainItem.SETTING_CLOSE_TIMER:  //定时开关机
                startActivity(new Intent(this, ShutOnTimeActivity.class));
                break;
        }
    }

    private void startSettingInfo() {
        Intent intentSetting = new Intent("android.settings.SETTINGS");
        intentSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentSetting);
    }


    int clickNum = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            clickNum++;
            if (clickNum > 5) {
                clickNum = 0;
                startSettingInfo();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
