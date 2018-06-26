package com.mirror.update;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mirror.entity.UpdateInfo;
import com.mirror.util.APKUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownStateListener;
import com.mirror.util.down.MirrorDownUtil;

public class UpdateAppParsrer
        implements DownStateListener
{
    private APKUtil apkUtil;
    Button btn_update_app;
    Context context;
    MirrorDownUtil downManagerXutil;
//    int downType;
    TextView tv_current_local_code;
    TextView tv_local_web_code;
    TextView tv_local_web_desc;
    UpdateAppView updateView;

    public UpdateAppParsrer(Context paramContext, UpdateAppView paramUpdateAppView)
    {
        this.updateView = paramUpdateAppView;
        this.context = paramContext;
        getView();
    }

    private void getView()
    {
        this.downManagerXutil = new MirrorDownUtil(this.context);
        this.tv_current_local_code = this.updateView.getLocalCurrenCode();
        this.tv_local_web_code = this.updateView.getLocalWebCode();
        this.tv_local_web_desc = this.updateView.getLocalWebDesc();
        this.btn_update_app = this.updateView.getUpdateAppBtn();
        this.btn_update_app.setClickable(false);
    }

    public void downFileStart(String downUrl, String savePath )  {
        if (!NetWorkUtils.isNetworkConnected(this.context)) {
            Toast.makeText(this.context, "网络异常，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        this.downManagerXutil.downFileStart(downUrl, savePath,
                "是否更新最新软件", true, new DownStateListener()
        {
            public void downStateInfo(DownFileEntity paramAnonymousDownFileEntity)
            {
                if (paramAnonymousDownFileEntity.getDownState() == 2)
                {
                    if (UpdateAppParsrer.this.apkUtil == null) {
                        apkUtil = new APKUtil(context);
                    }
                    UpdateAppParsrer.this.apkUtil.installApk(paramAnonymousDownFileEntity.getSavePath());
                }
            }
        });
    }

    public void downStateInfo(DownFileEntity paramDownFileEntity) {}

    public void downStop()
    {
        this.downManagerXutil.clickCancle();
    }

    public void updateAppView(UpdateInfo paramUpdateInfo)  {
        String str1 = paramUpdateInfo.getVersionName();
        String str2 = paramUpdateInfo.getUpdatedesc();
        this.tv_current_local_code.setText(APKUtil.getVersionName(this.context));
        this.tv_local_web_code.setText(str1);
        this.tv_local_web_desc.setText(str2);
        this.btn_update_app.setClickable(false);
        int i = APKUtil.getVersionCode(this.context);
        if (paramUpdateInfo.getAppversion() > i)  {
            this.btn_update_app.setClickable(true);
            this.btn_update_app.setText("软件升级");
            return;
        }
        this.btn_update_app.setText("(软件升级)当前为最新版本");
    }
}
