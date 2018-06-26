package com.mirror.view.recycle;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.mirror.R;


public class SWFrameLayout extends FrameLayout implements View.OnFocusChangeListener, View.OnClickListener {

    private static final String TAG = SWFrameLayout.class.getSimpleName();
    protected static final int ANIMATION_DURATION = 300;
    protected static final float FOCUS_SCALE = 1.1f;
    protected static final float NORMAL_SCALE = 1.0f;
    private boolean mFocus = true;

    public SWFrameLayout(Context context) {
        this(context, null, 0);
    }

    public SWFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SWFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        setOnClickListener(this);
        setOnFocusChangeListener(this);
        setBackgroundResource(R.drawable.rect_circle_app);
        setFocusable(mFocus);
        setFocusableInTouchMode(mFocus);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        Log.i(TAG, "onFocusChanged: gainFocus = " + gainFocus + ", direction = " + direction);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (null == v) {
            Log.i(TAG, "onFocusChange: v == null");
            return;
        }
        Log.i(TAG, "onFocusChange: hasFocus = " + hasFocus);
        if (hasFocus) {
            v.bringToFront();
            v.animate().scaleX(FOCUS_SCALE).scaleY(FOCUS_SCALE).setDuration(ANIMATION_DURATION).start();
            v.setSelected(true);
        } else {
            v.animate().scaleX(NORMAL_SCALE).scaleY(NORMAL_SCALE).setDuration(ANIMATION_DURATION).start();
            v.setSelected(false);
        }
    }
}