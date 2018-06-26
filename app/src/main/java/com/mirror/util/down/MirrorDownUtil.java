package com.mirror.util.down;


import android.content.Context;
import android.widget.Toast;


import com.mirror.util.NetWorkUtils;

import java.io.File;

/***
 * 镜端下载工具类
 */
public class MirrorDownUtil implements DownStateListener, DialogDown.DownDialogListener {

    Context context;
    DownRunnable downRunnable;
    DialogDown dialogDown;
    DownStateListener mDownStateListener;

    public MirrorDownUtil(Context context) {
        this.context = context;
        dialogDown = new DialogDown(context);
        dialogDown.setOnDownDialogListener(this);
    }

    public void dialogDissmiss() {
        dialogDown.dismiss();
    }

    @Override
    public void clickOk() {
        downRunnable = new DownRunnable(downUrl, savePath, this);
        downRunnable.setIsDelFile(isDel);
        Thread thread = new Thread(downRunnable);
        thread.start();

        //刷新dialog界面，============================
        DownFileEntity entity = new DownFileEntity();
        entity.setDesc("Start Down");
        entity.setDownState(DownFileEntity.DOWN_STATE_START);
        entity.setDownSpeed(100);
        entity.setProgress(0);
        entity.setDown(true);
        entity.setDownPath(downUrl);
        entity.setSavePath(savePath);
        dialogDown.updateView(entity);
        //=======================================
    }

    @Override
    public void clickCancle() {
        downStop();
        try {
            File file = new File(savePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception E) {
        }
    }

    private String downUrl;
    private String savePath;
    private boolean isDel;

    /***
     * 文件下载方法
     * @param downUrl
     * 文件下载地址
     * @param savePath
     * 下载的文件的保存地址
     * @param desc
     * dialog的显示文字
     * @param isDel
     * 删不删除源文件
     */
    public void downFileStart(String downUrl, String savePath, String desc, boolean isDel, DownStateListener mDownStateListener) {
        this.mDownStateListener = mDownStateListener;
        if (!NetWorkUtils.isNetworkConnected(context)) {
            Toast.makeText(context, "网络异常，请检查", Toast.LENGTH_LONG).show();
            return;
        }
        this.downUrl = downUrl;
        this.isDel = isDel;
        this.savePath = savePath;
        dialogDown.showDialog(desc);
    }

    public void downStop() {
        if (downRunnable != null) {
            downRunnable.stopDown();
        }
    }

    @Override
    public void downStateInfo(final DownFileEntity entity) {
        dialogDown.updateView(entity);
        if (mDownStateListener != null) {
            mDownStateListener.downStateInfo(entity);
        }
    }
}
