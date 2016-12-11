package com.djr.commonlibrary.view.refresh.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DongJr on 2016/7/19.
 * 用贝塞尔曲线画圆
 */
public class BezierRefreshView extends View {

    private static final float C = 0.551915024494f;     // 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置
    private float mCurrentTop;
    private float mCircleRadius = 50;                  // 圆的半径
    private float mDifference = mCircleRadius * C;        // 圆形的控制点与数据点的差值

    private float[] mData = new float[8];               // 顺时针记录绘制圆形的四个数据点
    private float[] mCtrl = new float[16];              // 顺时针记录绘制圆形的八个控制点

    private float mDuration = 500;                     // 变化总时长
    private float mCurrent = 0;                         // 当前已进行时长
    private float mCount = 50;                         // 将时长总共划分多少份
    private float mPiece = mDuration / mCount;            // 每一份的时长
    private Paint mPaint;
    private float mPercent;
    ValueAnimator scaleAnim;
    float scaleFloat = 0.8f;

    public BezierRefreshView(Context context) {
        super(context);
        init();
    }

    public BezierRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);

        // 初始化数据点
        mData[0] = 0;
        mData[1] = mCircleRadius;

        mData[2] = mCircleRadius;
        mData[3] = 0;

        mData[4] = 0;
        mData[5] = -mCircleRadius;

        mData[6] = -mCircleRadius;
        mData[7] = 0;

        // 初始化控制点
        mCtrl[0] = mData[0] + mDifference;
        mCtrl[1] = mData[1];

        mCtrl[2] = mData[2];
        mCtrl[3] = mData[3] + mDifference;

        mCtrl[4] = mData[2];
        mCtrl[5] = mData[3] - mDifference;

        mCtrl[6] = mData[4] + mDifference;
        mCtrl[7] = mData[5];

        mCtrl[8] = mData[4] - mDifference;
        mCtrl[9] = mData[5];

        mCtrl[10] = mData[6];
        mCtrl[11] = mData[7] - mDifference;

        mCtrl[12] = mData[6];
        mCtrl[13] = mData[7] + mDifference;

        mCtrl[14] = mData[0] - mDifference;
        mCtrl[15] = mData[1];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getMeasuredWidth() / 2;
        float y = mCurrentTop / 2;
        canvas.translate(x, y); // 将坐标系移动到画布中央
        canvas.scale(1, -1);                 // 翻转Y轴

        canvas.scale(scaleFloat, scaleFloat);

        Path path = new Path();
        path.moveTo(mData[0], mData[1]);
        path.cubicTo(mCtrl[0], mCtrl[1], mCtrl[2], mCtrl[3], mData[2], mData[3]);
        path.cubicTo(mCtrl[4], mCtrl[5], mCtrl[6], mCtrl[7], mData[4], mData[5]);
        path.cubicTo(mCtrl[8], mCtrl[9], mCtrl[10], mCtrl[11], mData[6], mData[7]);
        path.cubicTo(mCtrl[12], mCtrl[13], mCtrl[14], mCtrl[15], mData[0], mData[1]);
        canvas.drawPath(path, mPaint);

        mCurrent += mPiece;
        if (mCurrent < mDuration) {
            mData[1] -= 30 / mCount;
            mCtrl[7] += 20 / mCount;
            mCtrl[9] += 20 / mCount;

            mCtrl[4] -= 5 / mCount;
            mCtrl[10] += 5 / mCount;
//            postInvalidateDelayed((long) mPiece);
        }
    }

    public void offsetTopAndBottom(int offset) {
        invalidate();
    }

    public void start() {
        scaleAnim = ValueAnimator.ofFloat(0.8f, 1, 0.8f);
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.start();
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                scaleFloat = animatedValue;
                invalidate();
            }
        });
    }

    public void setPercent(float currentTop, float percent, boolean invalidate) {
        mCurrentTop = currentTop;
        mPercent = percent;
        invalidate();
    }

    public void stop() {
        init();
        if (scaleAnim != null) {
            scaleAnim.end();
        }
        scaleFloat = 0.8f;
        mCurrent = 0;
    }

}
