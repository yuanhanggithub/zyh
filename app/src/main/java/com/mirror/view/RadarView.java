package com.mirror.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.mirror.R;
import com.mirror.util.SharedPerManager;

/**
 * 雷达搜索
 *
 * @author LGL
 */
public class RadarView extends View {

    private Paint mPaintLine, mPaintCircle;
    private int w, h;
    // 动画
    private Matrix matrix;
    // 旋转角度
    private int start;
    // Handler定时动画
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            start = start + 1;
            matrix = new Matrix();
            // 参数：旋转角度，围绕点坐标的x,y坐标点
            matrix.postRotate(start, w / 2, h / 2);
            // 刷新重绘
            RadarView.this.invalidate();
            // 继续循环
            handler.postDelayed(run, 60);
        }
    };

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        w = SharedPerManager.getScreenWidth() * 7 / 10;
        h = SharedPerManager.getScreenHeight();
        handler.post(run);
    }

    private void initView() {
        mPaintLine = new Paint();
        mPaintLine.setColor(0xff00CCFF);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Style.STROKE);
        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.RED);
        mPaintCircle.setAntiAlias(true);
        matrix = new Matrix();
    }

    /**
     * 测量
     *
     * @author LGL
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置铺满
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画四个圆形
        canvas.drawCircle(w / 2, h / 2, w / 2, mPaintLine);
        canvas.drawCircle(w / 2, h / 2, w / 3, mPaintLine);
        canvas.drawCircle(w / 2, h / 2, w * 7 / 10, mPaintLine);
        canvas.drawCircle(w / 2, h / 2, w / 4, mPaintLine);
        // 绘制渐变圆
        Shader mShader = new SweepGradient(w / 2, h / 2, Color.TRANSPARENT, Color.parseColor("#AAAAAAAA"));
        // 绘制时渐变
        mPaintCircle.setShader(mShader);
        // 增加旋转动画，使用矩阵实现
        canvas.concat(matrix); // 前置动画
        canvas.drawCircle(w / 2, h / 2, w * 7 / 10, mPaintCircle);
    }
}