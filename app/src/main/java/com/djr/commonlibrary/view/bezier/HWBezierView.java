package com.djr.commonlibrary.view.bezier;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by jerry on 2018/6/22.
 */
public class HWBezierView extends View{

    private int mRadius = 200;
    private float mCenterX;
    private float mCenterY;
    private double mAngle;

    private float[] mInitialPoint;
    private float[] mCPoint;
    private float[] mBPoint;
    private float[] mAPoint;

    private Paint mCirclePaint;
    private Paint mBezierPaint;
    private Paint mAuxiliaryLinePaint;
    private Path mPath;

    private ValueAnimator mValueAnimator;

    public HWBezierView(Context context) {
        this(context, null);
    }

    public HWBezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HWBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        mInitialPoint =  new float[2];
        mCPoint = new float[2];
        mBPoint = new float[2];
        mAPoint = new float[2];
        mPath = new Path();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(8f);
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setAntiAlias(true);

        mBezierPaint = new Paint();
        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setColor(Color.BLACK);
        mBezierPaint.setStrokeWidth(8f);
        mBezierPaint.setAntiAlias(true);

        mAuxiliaryLinePaint = new Paint();
        mAuxiliaryLinePaint.setStyle(Paint.Style.FILL);
        mAuxiliaryLinePaint.setStrokeWidth(3f);
        mAuxiliaryLinePaint.setColor(Color.RED);
        mAuxiliaryLinePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mCenterX, mCenterY);
        canvas.scale(1, -1);
        mPath.reset();

        float h = (float) ((4 / 3) * ((1 - Math.cos(Math.toRadians(mAngle / 2)) / Math.sin(Math.toRadians(mAngle / 2)))));
        mInitialPoint[0] = mRadius;
        mInitialPoint[1] = 0;

        mCPoint[0] = mRadius;
        mCPoint[1] = (h * mRadius);

        mBPoint[0] = (float) ((Math.cos(Math.toRadians(mAngle / 2)) + h * Math.sin(Math.toRadians(mAngle / 2))) * mRadius);
        mBPoint[1] = (float) ((Math.sin(Math.toRadians(mAngle / 2)) - h * Math.cos(Math.toRadians(mAngle / 2))) * mRadius);

        mAPoint[0] = (float) (Math.cos(Math.toRadians(mAngle / 2)) * mRadius);
        mAPoint[1] = (float) (Math.sin(Math.toRadians(mAngle / 2)) * mRadius);

        drawAuxiliaryLine(canvas);

        mPath.moveTo(mRadius, 0);
        mPath.cubicTo(mCPoint[0], mCPoint[1], mBPoint[0], mBPoint[1], mAPoint[0], mAPoint[1]);
        canvas.drawPath(mPath, mBezierPaint);
        canvas.drawCircle(0, 0, mRadius, mCirclePaint);
    }

    private void drawAuxiliaryLine(Canvas canvas) {
        canvas.drawLine(mInitialPoint[0], mInitialPoint[1], mCPoint[0], mCPoint[1], mAuxiliaryLinePaint);
        canvas.drawLine(mCPoint[0], mCPoint[1], mBPoint[0], mBPoint[1], mAuxiliaryLinePaint);
        canvas.drawLine(mBPoint[0], mBPoint[1], mAPoint[0], mAPoint[1], mAuxiliaryLinePaint);
    }

    public void start() {
        mValueAnimator = ValueAnimator.ofInt(30, 270, 30);
        mValueAnimator.setDuration(10000);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.start();
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                mAngle = animatedValue;
                invalidate();
            }
        });
    }

}
