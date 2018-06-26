package com.mirror.update;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.activity.BaseActivity;
import com.mirror.config.AppInfo;
import com.mirror.entity.SystemUpdateInfo;
import com.mirror.entity.UpdateInfo;
import com.mirror.util.FileUtil;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;
import com.mirror.view.progress.RopeProgressBar;

public class UpdateActiivty extends BaseActivity implements UpdateView, MirrorUpdateView {

    MirrorUpdateUtil mirrorUpdateUtil;
    Button btn_system_update;
    LinearLayout lin_progress_layout;
    SystemUpdateInfo systemUpdateInfo = null;
    TextView speed;
    TextView tv_current_systen_code;
    TextView tv_down_state;
    TextView tv_system_web_code;
    TextView tv_system_web_desc;
    UpdateParsrer updateParsrer;
    RopeProgressBar updateProgress;
    private WaitDialogUtil waitDialog;


    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_update_sys);
        initView();
        updateView();
    }

    private void initView() {
        btn_system_update = (Button) findViewById(R.id.btn_system_update);
        lin_progress_layout = (LinearLayout) findViewById(R.id.lin_progress_layout);
        speed = (TextView) findViewById(R.id.speed);
        tv_current_systen_code = (TextView) findViewById(R.id.tv_current_systen_code);
        tv_down_state = (TextView) findViewById(R.id.tv_down_state);
        tv_system_web_code = (TextView) findViewById(R.id.tv_system_web_code);
        tv_system_web_desc = (TextView) findViewById(R.id.tv_system_web_desc);
        updateProgress = (RopeProgressBar) findViewById(R.id.updateProgress);
        btn_system_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downSysFile();
            }
        });
    }

    private void downSysFile() {
        if (systemUpdateInfo == null) {
            MyToastView.getInstance().Toast(UpdateActiivty.this, "没有获取到数据，请重新进入界面");
            return;
        }
        String downUrl = systemUpdateInfo.getApkdown();
        String savePath = AppInfo.FIREWARE_DOWNLOAD_SAVE_APK;
        updateParsrer.downFileStart(downUrl, savePath);
    }


    public void requestAppUpdate(boolean paramBoolean, UpdateInfo paramUpdateInfo, String paramString) {
    }

    public void requestSystemState(boolean isTrue, SystemUpdateInfo sysUpdateInfo, String errorDesc) {
        if (isTrue) {
            this.systemUpdateInfo = sysUpdateInfo;
            updateParsrer.updateSystemView(sysUpdateInfo);
        }
    }

    /***
     * 发广播给系统
     * @param toast
     */
    public void showToastMainView(String toast) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.YS_UPDATE_FIRMWARE");
        intent.putExtra("img_path", AppInfo.FIREWARE_DOWNLOAD_SAVE_APK);
        sendBroadcast(intent);
    }

    public void showWaitDialog(boolean isShow) {
        if (isShow) {
            waitDialog.show("加载中");
        } else {
            waitDialog.dismiss();
        }
    }

    private void updateView() {
        FileUtil.creatDirPathNoExists();
        waitDialog = new WaitDialogUtil(this);
        updateParsrer = new UpdateParsrer(this, this);
        mirrorUpdateUtil = new MirrorUpdateUtil(this, this);
        mirrorUpdateUtil.cheeckSystemUpdate();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == KeyEvent.KEYCODE_BACK) {
            MyToastView.getInstance().Toast(this, "退出界面会暂停下载，请知悉 ！ ");
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }


    protected void onDestroy() {
        super.onDestroy();
        if (this.updateParsrer != null) {
            this.updateParsrer.downStop();
        }
    }


    public Button getBtnSysUpdate() {
        return btn_system_update;
    }

    public TextView getDownStateTv() {
        return tv_down_state;
    }

    public RopeProgressBar getProgressBar() {

        return updateProgress;
    }

    public TextView getSpeedTv() {
        return speed;
    }

    public TextView getSystemCurrentCode() {
        return tv_current_systen_code;
    }

    public TextView getSystemWebCode() {
        return tv_system_web_code;
    }

    public TextView getSystemWebDesc() {
        return tv_system_web_desc;
    }

}
