package com.mirror.view.ad;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mirror.MirrorApplication;
import com.mirror.util.SharedPerManager;

import java.util.ArrayList;
import java.util.List;

public class AidlService extends Service {

    public static List<Pos1Bean> list = new ArrayList();
    public static List<VideoLocaEntity> list_local = new ArrayList();

    private IBinder iBinder = new GetAdList.Stub() {

        public List<Pos4Entity> getBottomList() throws RemoteException {
            return MirrorApplication.getInstance().getPos_4();
        }

        public List<Pos1Bean> getListInf()
                throws RemoteException {
            AidlService.list = MirrorApplication.getInstance().getInfos();
            Log.i("cdl", "=====远程服务器接收到数据===" + AidlService.list.size());
            return AidlService.list;
        }

        public String getUserToken()
                throws RemoteException {
            return SharedPerManager.getToken();
        }

        public List<VideoLocaEntity> getVideoLocalList()
                throws RemoteException {
            AidlService.list_local = MirrorApplication.getInstance().getListsMedia();
            return AidlService.list_local;
        }
    };

    @Nullable
    public IBinder onBind(Intent paramIntent) {
        return this.iBinder;
    }

    public void onCreate() {
        super.onCreate();
    }
}
