package com.mirror.update;

import android.content.Context;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import com.mirror.entity.SystemUpdateInfo;
import com.mirror.update.UpdateView;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownRunnable;
import com.mirror.util.down.DownStateListener;
import com.mirror.view.MyToastView;
import com.mirror.view.progress.RopeProgressBar;

public class UpdateParsrer implements DownStateListener {

    Button btn_system_update;
    Context context;
    DownRunnable downRunnable;
    RopeProgressBar ropeProgressBar;
    TextView tv_current_systen_code;
    TextView tv_down_state;
    TextView tv_speed;
    TextView tv_system_web_code;
    TextView tv_system_web_desc;
    UpdateView updateView;

    public UpdateParsrer(Context paramContext, UpdateView paramUpdateView) {
        this.updateView = paramUpdateView;
        this.context = paramContext;
        getView();
    }

    private void getView() {
        tv_system_web_code = this.updateView.getSystemWebCode();
        tv_system_web_desc = this.updateView.getSystemWebDesc();
        tv_current_systen_code = this.updateView.getSystemCurrentCode();
        btn_system_update = this.updateView.getBtnSysUpdate();
        btn_system_update.setClickable(false);
        ropeProgressBar = this.updateView.getProgressBar();
        tv_down_state = this.updateView.getDownStateTv();
        tv_speed = this.updateView.getSpeedTv();
    }

    private void updateState(DownFileEntity entity) {
        switch (entity.getDownState()) {
            case DownFileEntity.DOWN_STATE_START:
                btn_system_update.setClickable(false);
                tv_down_state.setText("开始下载");
                return;
            case DownFileEntity.DOWN_STATE_PROGRESS:
                btn_system_update.setClickable(false);
                tv_down_state.setText("下载中");
                return;
            case DownFileEntity.DOWN_STATE_SUCCESS:
                btn_system_update.setClickable(true);
                tv_down_state.setText("下载成功");
                updateView.showToastMainView(entity.getSavePath());
                return;
            case DownFileEntity.DOWN_STATE_FAIED:
                btn_system_update.setClickable(true);
                tv_down_state.setText("下载失败,请重试，重复多次出现请重启设备");
                return;
            case DownFileEntity.DOWN_STATE_CACLE:
                btn_system_update.setClickable(true);
                tv_down_state.setText("未获取到文件，请点击重试 ! 重复多次出现请重启设备");
                break;
        }
    }

    public void downFileStart(String downUrl, String savePath) {
        if (!NetWorkUtils.isNetworkConnected(this.context)) {
            MyToastView.getInstance().Toast(context, "网络异常，请检查");
            return;
        }
        downRunnable = new DownRunnable(downUrl, savePath, this);
        new Thread(this.downRunnable).start();
    }

    public void downStateInfo(DownFileEntity paramDownFileEntity) {
        updateState(paramDownFileEntity);
        tv_speed.setText(paramDownFileEntity.getDownSpeed() + " KB");
        if (Math.abs(paramDownFileEntity.getProgress()) > 100) {
            downStop();
            MyToastView.getInstance().Toast(this.context, "未获取到文件，请点击重试 ，多次出现请重启 。");
        }
        ropeProgressBar.setProgress(paramDownFileEntity.getProgress());
    }

    public void downStop() {
        if (this.downRunnable != null) {
            downRunnable.stopDown();
        }
    }

    public String getSystemCode() {
        return Build.VERSION.INCREMENTAL.trim();
    }

    public void updateSystemView(SystemUpdateInfo paramSystemUpdateInfo) {
        btn_system_update.setClickable(false);
        String str1 = paramSystemUpdateInfo.getSystemcode();
        String str2 = paramSystemUpdateInfo.getUpdatedesc();
        paramSystemUpdateInfo.getApkdown();
        tv_current_systen_code.setText(Build.VERSION.INCREMENTAL.trim().trim());
        if ((str1 == null) || (str1.length() < 5)) {
            tv_current_systen_code.setText("网络请求异常，请检查网络");
            MyToastView.getInstance().Toast(this.context, paramSystemUpdateInfo.getUpdatedesc());
            return;
        }
        tv_system_web_code.setText(str1);
        tv_system_web_desc.setText(str2);
        if (getSystemCode().equals(str1)) {
            btn_system_update.setText("(系统升级)当前是最新版本");
            return;
        }
        btn_system_update.setClickable(true);
        btn_system_update.setText("系统升级");
    }
}
