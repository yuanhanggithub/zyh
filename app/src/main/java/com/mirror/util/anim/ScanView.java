package com.mirror.util.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ScanView extends FrameLayout {
    public static final int ANTI_CLOCK_WISE = -1;
    public static final int CLOCK_WISE = 1;
    private static final int DEFAULT_DIERCTION = 1;
    private int direction = 1;
    public boolean isstart = false;
    private Context mContext;
    private Paint mPaintCircle;
    private Paint mPaintLine;
    private Paint mPaintPoint;
    private Paint mPaintSector;
    private Shader mShader;
    private ScanThread mThread;
    private Matrix matrix;
    private int start = 0;
    private boolean threadRunning = true;
    private int viewSize = 800;

    public ScanView(Context paramContext) {
        super(paramContext);
        this.mContext = paramContext;
        initPaint();
    }

    public ScanView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.mContext = paramContext;
        initPaint();
    }

    private void initPaint() {
        setBackgroundColor(0);
        this.mPaintLine = new Paint();
        this.mPaintLine.setStrokeWidth(3.0F);
        this.mPaintLine.setAntiAlias(true);
        this.mPaintLine.setStyle(Paint.Style.STROKE);
        this.mPaintLine.setColor(-1);
        this.mPaintCircle = new Paint();
        this.mPaintCircle.setStrokeWidth(5.0F);
        this.mPaintCircle.setAntiAlias(true);
        this.mPaintCircle.setStyle(Paint.Style.FILL);
        this.mPaintCircle.setColor(0xffB3B3B3);
        this.mPaintSector = new Paint();
        this.mPaintSector.setColor(0xffB3B3B3);
        this.mPaintSector.setAntiAlias(true);
        this.mShader = new SweepGradient(this.viewSize / 2, this.viewSize / 2, 0, -16711936);
        this.mPaintSector.setShader(this.mShader);
        this.mPaintPoint = new Paint();
        this.mPaintPoint.setColor(-1);
        this.mPaintPoint.setStyle(Paint.Style.FILL);
    }

    protected void onDraw(Canvas paramCanvas) {
        paramCanvas.drawCircle(this.viewSize / 2, this.viewSize / 2, 150.0F, this.mPaintCircle);
        paramCanvas.drawCircle(this.viewSize / 2, this.viewSize / 2, 100.0F, this.mPaintLine);
        paramCanvas.drawCircle(this.viewSize / 2, this.viewSize / 2, 50.0F, this.mPaintLine);
        paramCanvas.drawCircle(this.viewSize / 2, this.viewSize / 2, 150.0F, this.mPaintLine);
        paramCanvas.drawLine(this.viewSize / 2, 0.0F, this.viewSize / 2, this.viewSize, this.mPaintLine);
        paramCanvas.drawLine(0.0F, this.viewSize / 2, this.viewSize, this.viewSize / 2, this.mPaintLine);
        paramCanvas.concat(this.matrix);
        paramCanvas.drawCircle(this.viewSize / 2, this.viewSize / 2, 150.0F, this.mPaintSector);
        super.onDraw(paramCanvas);
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        setMeasuredDimension(this.viewSize, this.viewSize);
    }

    public void setDirection(@RADAR_DIRECTION int paramInt) {
        if ((paramInt != 1) && (paramInt != -1)) {
            throw new IllegalArgumentException("Use @RADAR_DIRECTION constants only!");
        }
        this.direction = paramInt;
    }

    void setViewSize(int paramInt) {
        this.viewSize = paramInt;
        setMeasuredDimension(this.viewSize, this.viewSize);
    }

    public void start() {
        this.mThread = new ScanThread(this);
        this.mThread.setName("radar");
        this.mThread.start();
        this.threadRunning = true;
        this.isstart = true;
    }

    public void stop() {
        if (this.isstart) {
            this.threadRunning = false;
            this.isstart = false;
        }
    }

    public static @interface RADAR_DIRECTION {
    }

    protected class ScanThread
            extends Thread {
        private ScanView view;

        public ScanThread(ScanView paramScanView) {
            this.view = paramScanView;
        }

        public void run() {
            while (ScanView.this.threadRunning) {
                if (ScanView.this.isstart) {
                    this.view.post(new Runnable() {
                        public void run() {
                            matrix.preRotate(ScanView.this.direction * ScanView.this.start, ScanView.this.viewSize / 2, ScanView.this.viewSize / 2);
                            invalidate();
                        }
                    });
                    try {
                        Thread.sleep(5L);
                    } catch (InterruptedException localInterruptedException) {
                        localInterruptedException.printStackTrace();
                    }
                }
            }
        }
    }
}
