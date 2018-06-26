package com.mirror.fragment.video;


import com.mirror.entity.CartonAdEntity;

public interface TvLiveView {
    void requestCartonAdState(boolean paramBoolean, CartonAdEntity paramCartonAdEntity, String paramString);

    void showToastView(String paramString);

    void showWaitDialog(boolean paramBoolean);
}
