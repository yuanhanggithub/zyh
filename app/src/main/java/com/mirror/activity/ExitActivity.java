package com.mirror.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.mirror.config.AppConfig;
import com.mirror.config.AppInfo;
import com.mirror.service.MirrorService;
import com.mirror.R;
import com.mirror.adapter.ExitAdapter;
import com.mirror.entity.ExitEntity;
import com.mirror.entity.SystemUpdateInfo;
import com.mirror.entity.UpdateInfo;
import com.mirror.service.ScreenViewService;
import com.mirror.update.UpdateAppActiivty;
import com.mirror.util.APKUtil;
import com.mirror.util.CodeUtil;
import com.mirror.util.DisPlayUtil;
import com.mirror.util.FileUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;
import com.mirror.util.popwindow.FloatViewService;
import com.mirror.update.MirrorUpdateUtil;
import com.mirror.update.MirrorUpdateView;
import com.mirror.util.system.SysPullServer;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;
import com.mirror.view.dialog.OridinryDialog;

import java.util.ArrayList;
import java.util.List;

public class ExitActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    ExitAdapter adapter;
    GridView grid_exit;
    List<ExitEntity> lists = new ArrayList();
    OridinryDialog oridinryDialog;
    TextView tvVersion;
    TextView tv_version_system;
    WaitDialogUtil waitDialogUtil;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_exit);
        getListData();
        initView();
        checkEshareServer();
    }

    private void initView() {
        waitDialogUtil = new WaitDialogUtil(this);
        grid_exit = (GridView) findViewById(R.id.grid_exit);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tv_version_system = (TextView) findViewById(R.id.tv_version_system);
        grid_exit.setNumColumns(4);
        adapter = new ExitAdapter(this, this.lists);
        grid_exit.setAdapter(this.adapter);
        grid_exit.setOnItemClickListener(this);
        oridinryDialog = new OridinryDialog(ExitActivity.this, SharedPerManager.getScreenWidth(), SharedPerManager.getScreenHeight());
        tvVersion.setText("版本 ：" + CodeUtil.getVersion(this) + "_" + CodeUtil.getLocalVersion(this));
        tv_version_system.setText("系统版本 : " + getSystemCode());
    }


    private void checkEshareServer() {
        String serverPackageName = AppInfo.ESHARE_SERVER_PACKAGE;
        if (!NetWorkUtils.isNetworkConnected(this)) {
            return;
        }
        if (!APKUtil.ApkState(ExitActivity.this, serverPackageName)) {
            return;
        }
        if (!SharedPerManager.isOpenShareServer() && AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_TWO) {
            oridinryDialog.show("检测到设备投屏未激活，是否去激活?\n(打开界面，直接关闭即可)", "去激活", "下次把");
        }
        oridinryDialog.setOnDialogClickListener(new OridinryDialog.OridinryDialogClick() {
            public void noSure() {
                MyToastView.getInstance().Toast(ExitActivity.this, "不激活投屏，可能手机检索不到设备ID");
            }

            public void sure() {
                SharedPerManager.setOpenShareServer(true);
                DisPlayUtil.startApp(ExitActivity.this, AppInfo.ESHARE_SERVER_PACKAGE);
            }
        });
    }

    private void getListData() {
        lists.add(new ExitEntity(R.mipmap.ic_reboot_sys, "一键重启", false));
        lists.add(new ExitEntity(R.mipmap.icon_update, "软件升级", false));
        lists.add(new ExitEntity(R.mipmap.icon_setting, "快捷设置", false));
        lists.add(new ExitEntity(R.mipmap.ic_exit_shop, "设备信息", false));
    }

    private void reStartEquip() {
        if (AppConfig.EQUIP_TYPE == AppConfig.EQUIP_TYPE_ONE) {  //一代设备重启
            Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        oridinryDialog.show("确定要重启?", "重启", "取消");
        oridinryDialog.setOnDialogClickListener(new OridinryDialog.OridinryDialogClick() {
            public void noSure() {

            }

            public void sure() {
                restartAppInfo();
            }
        });
    }

    public void restartAppInfo() {
        Intent localIntent = new Intent("android.intent.action.REBOOT");
        localIntent.putExtra("nowait", 1);
        localIntent.putExtra("interval", 1);
        localIntent.putExtra("window", 0);
        sendBroadcast(localIntent);
    }

    public String getSystemCode() {
        return Build.VERSION.INCREMENTAL.trim().trim();
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
        switch (position) {
            case 0:   //重启设备
                reStartEquip();
                return;
            case 1:  //软件升级
                checkAppVersion();
                break;
            case 2:  //系统设置
                startActivity(new Intent(this, SettingActivity.class));
                return;
            case 3:  //设备信息
                startActivity(new Intent(this, EquipInfoActivity.class));
                break;
        }
    }

    private void checkAppVersion() {
        if (!NetWorkUtils.isNetworkConnected(ExitActivity.this)) {
            showToast("网络异常，检测升级失败");
            return;
        }
        MirrorUpdateUtil mirrorUpdateUtil = new MirrorUpdateUtil(ExitActivity.this, new MirrorUpdateView() {
            public void requestAppUpdate(boolean isTrue, UpdateInfo updateInfo, String errorDesc) {
                int localVersion = CodeUtil.getVersion(ExitActivity.this);
                if (isTrue) {
                    int versionWeb = updateInfo.getAppversion();
                    if (versionWeb >= localVersion) {
                        goToAppUpdate();
                    } else {
                        showToast("当前为最新版本");
                    }
                } else {
                    goToAppUpdate();
                }
            }

            public void requestSystemState(boolean isTrue, SystemUpdateInfo systemUpdateInfo, String errorDesc) {
            }

            public void showWaitDialog(boolean isTrue) {
            }
        });
        mirrorUpdateUtil.checkAppUpdate(false);
    }

    public void goToAppUpdate() {
        startActivity(new Intent(this, UpdateAppActiivty.class));
    }


    private void showToast(String s) {
        MyToastView.getInstance().Toast(ExitActivity.this, s);
    }

    int clickNum = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            clickNum++;
            if (clickNum > 5) {
                showDebugModel();
                clickNum = 0;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void onResume() {
        super.onResume();
        MainNewActivity.isForst = true;
        startService(new Intent(this, MirrorService.class));
        startService(new Intent(this, FloatViewService.class));
        startService(new Intent(this, ScreenViewService.class));
        try {
            FloatViewService.getInstance().hideFloat();
            FileUtil.creatDirPathNoExists();
            SysPullServer.openEshareServer(this);
        } catch (Exception e) {
        }
    }

    private void showDebugModel() {
        boolean isAppDebug = AppConfig.isDebug;
        String showContent = "";
        if (isAppDebug) {
            showContent = "当前为测试模式，请选择当前版本 ？";
        } else {
            showContent = "当前为上线模式，请选择当前版本 ？";
        }
        oridinryDialog.show(showContent, "正式版本", "测试版本");
        oridinryDialog.setOnDialogClickListener(new OridinryDialog.OridinryDialogClick() {

            public void noSure() {
                SharedPerManager.setDebugModel(true);
                MyToastView.getInstance().Toast(ExitActivity.this, "设置软件为测试版本，重启设备");
                reStartEquip();
            }

            public void sure() {
                SharedPerManager.setDebugModel(false);
                MyToastView.getInstance().Toast(ExitActivity.this, "设置软件为正式版本，重启设备");
                reStartEquip();
            }
        });
        return;
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainNewActivity.isForst = false;
    }
}
