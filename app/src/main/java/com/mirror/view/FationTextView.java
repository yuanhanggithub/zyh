package com.mirror.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint({"AppCompatCustomView"})
public class FationTextView extends TextView {
    public FationTextView(Context paramContext) {
        this(paramContext, null);
    }

    public FationTextView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public FationTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        setTypeface(paramContext);
    }

    private void setTypeface(Context paramContext) {
        setTypeface(Typeface.createFromAsset(paramContext.getAssets(), "fonts/fationchina.TTF"));
    }
}
