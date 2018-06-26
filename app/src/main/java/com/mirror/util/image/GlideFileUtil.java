package com.mirror.util.image;

import java.io.File;

public class GlideFileUtil {
    public static void createGlidePath() {
        File localFile = new File(GlideConfig.GLIDE_CACHE_PATH);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        localFile = new File(GlideConfig.RIGHT_PATH);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
    }
}
