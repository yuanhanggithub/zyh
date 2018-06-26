package com.mirror.util.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimalUtil {
    Context context;
    TranslateAnimation translateAnimation;

    public AnimalUtil(Context paramContext) {
        this.context = paramContext;
    }

    public void startTranslateAnimation(View view) {
        translateAnimation = new TranslateAnimation(40, 200, 0, 0);
        translateAnimation.setRepeatMode(2);
        translateAnimation.setDuration(3000L);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(translateAnimation);
    }

    public void stopAnimal() {
        try {
            if (this.translateAnimation != null) {
                translateAnimation.cancel();
            }
            return;
        } catch (Exception localException) {
        }
    }
}
