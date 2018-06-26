package com.mirror.util.system;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class SysPullServer {

    public static void openEshareServer(Context paramContext) {
        try {
            ComponentName localComponentName = new ComponentName("com.ecloud.eshare.server", "com.ecloud.eshare.server.CifsServer");
            Intent localIntent = new Intent();
            localIntent.setComponent(localComponentName);
            localIntent.setAction("android.intent.action.MAIN");
            paramContext.startService(localIntent);
            return;
        } catch (Exception e) {
        }
    }

    private void openMonitor(Context paramContext) {
        Intent localIntent = new Intent();
        localIntent.setAction("com.mirror.mobile.action.START_SERVICE_MONITOR");
        localIntent.setPackage("com.mirror.mobile");
        paramContext.startService(localIntent);
    }
}
