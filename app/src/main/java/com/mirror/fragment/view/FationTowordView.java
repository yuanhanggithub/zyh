package com.mirror.fragment.view;


import com.mirror.entity.VideoEntity;

import java.util.List;

public interface FationTowordView {

    void queryVideoInfos(List<VideoEntity> paramList);

    void showToast(String paramString);

    void showWaitDialog(boolean paramBoolean);
}
