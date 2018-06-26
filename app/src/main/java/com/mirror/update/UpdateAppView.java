package com.mirror.update;

import android.widget.Button;
import android.widget.TextView;

public abstract interface UpdateAppView
{
    public static final int TAG_APP_UPDATE = 1289;

    public abstract TextView getLocalCurrenCode();

    public abstract TextView getLocalWebCode();

    public abstract TextView getLocalWebDesc();

    public abstract Button getUpdateAppBtn();
}
