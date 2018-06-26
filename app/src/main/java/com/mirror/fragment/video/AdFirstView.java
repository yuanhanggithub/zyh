package com.mirror.fragment.video;

import com.mirror.entity.ADVideoInfo;
import com.mirror.entity.CartonAdEntity;
import com.mirror.entity.DefaultAdVide;

public interface AdFirstView {

    void getDefaultAdState(boolean isTrue, DefaultAdVide defaultAdVide, String errorDesc);

    void requestAdState(boolean isTrue, ADVideoInfo adVideoInfo, String errorDesc);

    void requestCartonAdState(boolean isTrue, CartonAdEntity cartonAdEntity, String errorDesc);

    void showWaitDialog(boolean isTrue);
}
