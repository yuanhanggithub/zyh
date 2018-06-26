package com.mirror.util.exit;

public interface ExitView {

    void loginSuccess(String shopName, String username, String adminMob);

    void showTaost(String paramString);

    void showWaitDialog(boolean paramBoolean);
}
