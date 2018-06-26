package com.mirror.update;

import android.widget.Button;
import android.widget.TextView;

import com.mirror.view.progress.RopeProgressBar;


public interface UpdateView {
    Button getBtnSysUpdate();

    TextView getDownStateTv();

    RopeProgressBar getProgressBar();

    TextView getSpeedTv();

    TextView getSystemCurrentCode();

    TextView getSystemWebCode();

    TextView getSystemWebDesc();

    void showToastMainView(String paramString);
}
