package com.mirror.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mirror.R;

public class ButtonAnimalView extends RelativeLayout {
    int DEFAULT_IMAGE = 2130903074;
    FationTextView btn_buttom;
    int btn_image;
    Context context;
    ImageView iv_button_animal;
    ButtomAnimalViewListener listener;
    RelativeLayout rela_botton_view;
    ObjectAnimator retationAnimal;
    String textShow;
    int textSize;
    View view;

    public ButtonAnimalView(Context context) {
        this(context, null);
    }

    public ButtonAnimalView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ButtonAnimalView(Context context, @Nullable AttributeSet attrs, int paramInt) {
        super(context, attrs, paramInt);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BUTTOM_ANIMAL);
        btn_image = a.getResourceId(R.styleable.BUTTOM_ANIMAL_btn_image, DEFAULT_IMAGE);
        textShow = a.getString(R.styleable.BUTTOM_ANIMAL_textdesc);
        textSize = a.getDimensionPixelSize(R.styleable.BUTTOM_ANIMAL_textsize, 20);
        view = LayoutInflater.from(context).inflate(R.layout.view_bottom_anim, null);
        rela_botton_view = ((RelativeLayout) view.findViewById(R.id.rela_botton_view));
        iv_button_animal = ((ImageView) view.findViewById(R.id.iv_button_animal));
        btn_buttom = ((FationTextView) view.findViewById(R.id.btn_buttom));
        btn_buttom.setText(textShow);
        btn_buttom.setTextSize(textSize);
        iv_button_animal.setVisibility(GONE);
        a.recycle();
        addView(view);
        initViewListener();
    }

    private void initViewListener() {
        btn_buttom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Log.e("click", "==============点击了事件");
                if (listener != null) {
                    stopAnimal();
                    listener.btnAnumalClick(ButtonAnimalView.this);
                }
            }
        });
        btn_buttom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean isChooice) {
                Log.e("main", "======button=获取焦点=");
                changeView(isChooice);
            }
        });
        btn_buttom.setOnHoverListener(new OnHoverListener() {
            @Override
            public boolean onHover(View view, MotionEvent motionEvent) {
                int what = motionEvent.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER:  //鼠标进入view
                        changeView(true);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:  //鼠标离开view
                        changeView(false);
                        break;
                }
                return false;
            }
        });
    }

    private void stopAnimal() {
        try {
            iv_button_animal.setVisibility(GONE);
            if (this.retationAnimal != null) {
                retationAnimal.end();
            }
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public void changeView(boolean isChooice) {
        if (isChooice) {
            btn_buttom.setTextColor(getResources().getColor(R.color.yellow));
            startPropertyAnim();
            ViewCompat.animate(this.rela_botton_view).scaleX(1.3F).scaleY(1.3F).translationZ(1.0F).setDuration(100L).start();
        } else {
            btn_buttom.setTextColor(getResources().getColor(R.color.white));
            stopAnimal();
            ViewCompat.animate(this.rela_botton_view).scaleX(1.0F).scaleY(1.0F).translationZ(1.0F).setDuration(100L).start();
        }
    }

    public void setOnClickBtnListener(ButtomAnimalViewListener paramButtomAnimalViewListener) {
        listener = paramButtomAnimalViewListener;
    }

    public void startPropertyAnim() {
        stopAnimal();
        try {
            iv_button_animal.setVisibility(VISIBLE);
            if (this.retationAnimal == null) {
                retationAnimal = ObjectAnimator.ofFloat(this.iv_button_animal, "rotation", new float[]{0.0F, 360.0F});
                retationAnimal.setDuration(2000);
                LinearInterpolator localLinearInterpolator = new LinearInterpolator();
                retationAnimal.setInterpolator(localLinearInterpolator);
                retationAnimal.setRepeatCount(-1);
                retationAnimal.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator) {
                        ((Float) paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
                    }
                });
            }
            retationAnimal.start();
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public interface ButtomAnimalViewListener {
        void btnAnumalClick(View paramView);
    }
}
