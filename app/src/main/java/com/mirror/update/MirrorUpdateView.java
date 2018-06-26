package com.mirror.update;


import com.mirror.entity.SystemUpdateInfo;
import com.mirror.entity.UpdateInfo;

public interface MirrorUpdateView {

    void requestAppUpdate(boolean isTrue, UpdateInfo updateInfo, String errordesc);

    void requestSystemState(boolean isTrue, SystemUpdateInfo systemUpdateInfo, String errordesc);

    void showWaitDialog(boolean isShow);
}
