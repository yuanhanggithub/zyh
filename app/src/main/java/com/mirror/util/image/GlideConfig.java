package com.mirror.util.image;

import android.os.Environment;

import java.io.File;

public class GlideConfig {
    public static final String GLIDE_BASE_URL = Environment.getExternalStorageDirectory().getPath();
    public static final String GLIDE_CACHE_PATH = GLIDE_BASE_URL + "/mirror";
    public static final String RIGHT_PATH = GLIDE_CACHE_PATH + "/right";
}
