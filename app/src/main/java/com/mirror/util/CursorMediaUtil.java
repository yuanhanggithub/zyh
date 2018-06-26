package com.mirror.util;

import android.content.Context;

import com.mirror.MirrorApplication;
import com.mirror.view.ad.VideoLocaEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CursorMediaUtil {
    Context context;

    public CursorMediaUtil(Context paramContext) {
        this.context = paramContext;
    }

    public void refreshFileList(String filePath, FileSearchBackListener paramFileSearchBackListener) {
        ArrayList localArrayList = new ArrayList();
        try {
            File fileDir = new File(filePath);
            if (!fileDir.exists()) {
                paramFileSearchBackListener.backError("文件夹不存在");
                return;
            }
            File[] files = fileDir.listFiles();
            if (files.length < 1) {
                paramFileSearchBackListener.backError("文件夹下没有内容");
                return;
            }
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) {
                    String fileName = files[i].getName();
                    fileName = fileName.toLowerCase();
                    String path = files[i].getPath();
                    long fileLength = files[i].length();
                    if (fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png")) {
                        localArrayList.add(new VideoLocaEntity(fileName, path, fileLength, VideoLocaEntity.TYPE_IMAGE));
                    } else if (fileName.endsWith("mp4") || fileName.endsWith("avi") || fileName.endsWith("3gp") || fileName.endsWith("rmvb")) {
                        localArrayList.add(new VideoLocaEntity(fileName, path, fileLength, VideoLocaEntity.TYPE_VIDEO));
                    }
                }
            }
            MirrorApplication.getInstance().setListsMedia(localArrayList);
            paramFileSearchBackListener.backFile(localArrayList);
        } catch (Exception e) {
            paramFileSearchBackListener.backError(e.toString());
        }
    }

    public interface FileSearchBackListener {
        void backError(String paramString);

        void backFile(List<VideoLocaEntity> paramList);
    }
}
