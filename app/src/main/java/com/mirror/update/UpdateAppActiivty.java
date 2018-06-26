package com.mirror.update;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.mirror.R;
import com.mirror.activity.BaseActivity;
import com.mirror.config.AppInfo;
import com.mirror.entity.SystemUpdateInfo;
import com.mirror.entity.UpdateInfo;
import com.mirror.util.FileUtil;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;

public class UpdateAppActiivty extends BaseActivity implements UpdateAppView, MirrorUpdateView {
    Button btn_update_app;
    TextView tv_current_local_code;
    TextView tv_local_web_code;
    TextView tv_local_web_desc;
    UpdateInfo updateInfo = null;
    UpdateAppParsrer updateParsrer;
    private WaitDialogUtil waitDialog;

    private void updateView() {
        FileUtil.creatDirPathNoExists();
        this.updateParsrer = new UpdateAppParsrer(this, this);
        this.waitDialog = new WaitDialogUtil(this);
        this.waitDialog.show("数据加载中");
        new MirrorUpdateUtil(this, this).checkAppUpdate(false);
    }

    public TextView getLocalCurrenCode()
    {
        if (this.tv_current_local_code == null) {
            this.tv_current_local_code = ((TextView)findViewById(R.id.tv_current_local_code));
        }
        return this.tv_current_local_code;
    }

    public TextView getLocalWebCode()
    {
        if (this.tv_local_web_code == null) {
            this.tv_local_web_code = ((TextView)findViewById(R.id.tv_local_web_code));
        }
        return this.tv_local_web_code;
    }

    public TextView getLocalWebDesc()
    {
        if (this.tv_local_web_desc == null) {
            this.tv_local_web_desc = ((TextView)findViewById(R.id.tv_local_web_desc));
        }
        return this.tv_local_web_desc;
    }

    public Button getUpdateAppBtn()
    {
        if (this.btn_update_app == null) {
            this.btn_update_app = ((Button)findViewById(R.id.btn_update_app));
        }
        return this.btn_update_app;
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_update_app);
        initView();
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(-1, -1);
        updateView();
    }
    private void initView() {
         btn_update_app = (Button)findViewById(R.id.btn_update_app);
         tv_current_local_code = (TextView)findViewById(R.id.tv_current_local_code);
         tv_local_web_code =(TextView) findViewById(R.id.tv_local_web_code);
         tv_local_web_desc = (TextView)findViewById(R.id.tv_local_web_desc);
         btn_update_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String downUrl =  updateInfo.getApkurl();
                String savePath = AppInfo.DOWNLOAD_SAVE_APK;
                 updateParsrer.downFileStart(downUrl, savePath );
            }
        });
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if (this.updateParsrer != null) {
            this.updateParsrer.downStop();
        }
    }

//    public void onViewClicked(View paramView)
//    {
//        switch (paramView.getId())
//        {
//            default:
//                return;
//        }
//        paramView = this.updateInfo.getApkurl();
//        String str = AppInfo.DOWNLOAD_SAVE_APK;
//        this.updateParsrer.downFileStart(paramView, str, 1289);
//    }

    public void requestAppUpdate(boolean paramBoolean, UpdateInfo updateInfo, String paramString)  {
        this.updateInfo = updateInfo;
        Log.e("update", "=======检测软件升级====" + updateInfo.toString());
        if (paramBoolean) {
            if (updateInfo.getAppversion() == -1)  {
                MyToastView.getInstance().Toast(this, "检测失败，请重新进入界面");
                return;
            }
             updateParsrer.updateAppView(updateInfo);
            return;
        }
        MyToastView.getInstance().Toast(this, paramString);
    }

    public void requestSystemState(boolean paramBoolean, SystemUpdateInfo paramSystemUpdateInfo, String paramString) {}

    public void showWaitDialog(boolean paramBoolean)
    {
        if (paramBoolean)
        {
            this.waitDialog.show("检测版本");
            return;
        }
        this.waitDialog.dismiss();
    }
}
