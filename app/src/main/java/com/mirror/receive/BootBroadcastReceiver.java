package com.mirror.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mirror.activity.StartActivity;
import com.mirror.config.AppConfig;
import com.mirror.config.AppInfo;
import com.mirror.service.MirrorService;
import com.mirror.service.ScreenViewService;
import com.mirror.util.DisPlayUtil;
import com.mirror.util.SharedPerManager;
import com.mirror.util.popwindow.FloatViewService;
import com.mirror.util.system.PowerManagerUtil;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    private static final String TAG = "BootBroadcastReceiver";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("BootBroadcastReceiver", "开机广播接收的广播==" + action);
        if (action.equals("com.ys.keyevent_home")) {
            DisPlayUtil.gotoHomeActivity(context);
            context.startService(new Intent(context, ScreenViewService.class));
            context.sendBroadcast(new Intent("BROAD_SCREEN_CLOSE"));
            SharedPerManager.setScreenAdTime(300000);
        } else if (action.equals(AppInfo.ESAHRE_APP_OPEN)) {
            context.startService(new Intent(context, ScreenViewService.class));
            context.sendBroadcast(new Intent("BROAD_SCREEN_OPEN"));
            SharedPerManager.setScreenAdTime(7200000);
            return;
        } else if (action.equals(AppInfo.ESAHRE_APP_CLOSE)) {
            context.startService(new Intent(context, ScreenViewService.class));
            context.sendBroadcast(new Intent("BROAD_SCREEN_CLOSE"));
            SharedPerManager.setScreenAdTime(300000);
        } else if (action.equals(ACTION_BOOT)) {
            Log.i("BootBroadcastReceiver", "设备开机广播==" + System.currentTimeMillis());
            if (AppConfig.EQUIP_TYPE==AppConfig.EQUIP_TYPE_ONE){
                Intent intentStaet = new Intent(context, StartActivity.class);
                intentStaet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentStaet);
                return;
            }
            PowerManagerUtil.writeFile(1);
        } else if (action.equals("com.mirror.service.mirrorService.kill")) {
            try {
                context.startService(new Intent(context, MirrorService.class));
                context.startService(new Intent(context, FloatViewService.class));
            } catch (Exception e) {
            }
        }
        Log.i("BootBroadcastReceiver", "====service被杀死了。。。准备复活中");
    }
}
