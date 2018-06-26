package com.mirror.util.down;

import android.os.Handler;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

public class DownRunnable implements Runnable {

    private HttpHandler<File> httHhandler;
    String downUrl;
    String saveUrl;
    DownStateListener listener;
    HttpUtils httpUtils;
    long downSum = 0;
    boolean isFalse = false;


    public void setIsDelFile(boolean isDelFile) {
        this.isFalse = isDelFile;
    }


    public DownRunnable(String downUrl, String saveUrl, DownStateListener listener) {
        Log.e("down", "======下载地址= " + downUrl + "\n保存的地址==" + saveUrl);
        this.downUrl = downUrl;
        this.saveUrl = saveUrl;
        this.listener = listener;
    }

    @Override
    public void run() {
        //第一个参数:下载地址
        //第二个参数:文件存储路径
        //第三个参数:是否断点续传
        //第四个参数:是否重命名
        //第五个参数:请求回调
        try {
            if (httpUtils == null) {
                httpUtils = new HttpUtils();
            }
            File fileDir = new File(saveUrl);
            //如果不需要断点续传，这里可以删除
            if (isFalse) {
                if (fileDir.exists()) {
                    Log.i("down", "======文件存在，删除文件");
                    fileDir.delete();
                }
            }
            if (!fileDir.exists()) {
                fileDir.createNewFile();
            }
            httpUtils.configRequestThreadPoolSize(5);//设置由几条线程进行下载
            httHhandler = httpUtils.download(downUrl, saveUrl, true, true, new RequestCallBack<File>() {
                @Override
                public void onStart() {
                    super.onStart();
                    Log.e("down", "======开始下载");
                    downSum = 0;
                    backState("开始下载", DownFileEntity.DOWN_STATE_START, 0, true, downUrl, saveUrl, 1000);
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    int progress = (int) (current * 100 / total);
                    int speed = (int) ((current - downSum) / 1024);
                    Log.e("down", "======下载进度==progress=" + progress + "   current=" + current + "/" + total + "    /speed = " + speed);
                    backState("下载中", DownFileEntity.DOWN_STATE_PROGRESS, progress, true, downUrl, saveUrl, speed);
                    downSum = current;
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.e("down", "===下载成功===" + saveUrl);
                    backState("下载成功", DownFileEntity.DOWN_STATE_SUCCESS, 100, false, downUrl, saveUrl, 0);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("down", "===下载失败==" + s + "   /" + e.toString());
                    backState(s, DownFileEntity.DOWN_STATE_FAIED, 0, false, downUrl, saveUrl, -1);
                }
            });
        } catch (Exception e) {
            Log.i("down", "下载异常==" + e.toString());
            backState(e.toString(), DownFileEntity.DOWN_STATE_FAIED, 0, false, downUrl, saveUrl, -1);
        }
    }

    public void stopDown() {
        backState("未获取到文件，请点击重试!", DownFileEntity.DOWN_STATE_CACLE, 0, false, downUrl, saveUrl, -1);
        try {
            if (httHhandler == null) {
                return;
            }
            httHhandler.cancel();
        } catch (Exception e) {
            Log.e("down", "停止下载异常==" + e.toString());
        }
    }

    private Handler handler = new Handler();

    private void backState(final String state, final int downState, final int progress, final boolean b, final String downUrl, final String saveUrl, final int speed) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                DownFileEntity entity = new DownFileEntity();
                entity.setDesc(state);
                entity.setDownState(downState);
                entity.setDownSpeed(speed);
                entity.setProgress(progress);
                entity.setDown(b);
                entity.setDownPath(downUrl);
                entity.setSavePath(saveUrl);
                listener.downStateInfo(entity);
            }
        });

    }
}
