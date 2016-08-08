package com.djr.commonlibrary.refresh.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by DongJr on 2016/7/11.
 */
public class RotateRefreshView extends View {

    private PullToRefreshView mParent;
    private Context mContext;
    private Paint circlePaint;
    private ImageView mRefreshView;


    private float mCurrentTop;
    private float mPercent = 0f;
    float scaleFloat = 0.8f;
    float rotateFloat = 0f;
    ValueAnimator scaleAnim;
    boolean mIsRreshing = false;


    public RotateRefreshView(Context context) {
        super(context);
        init(context);
    }

    public RotateRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RotateRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RotateRefreshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


//    public RefreshDrawable(XMPullToRefreshView layout, ImageView refreshView) {
//        mParent = layout;
//        mContext = layout.getContext();
//        mRefreshView = refreshView;
//        init();
//    }

    private void init(Context context) {
        mContext = context;
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getMeasuredWidth() / 2;
        float y = mCurrentTop / 2;
        float radius = dip2px(5);
//        float rotateFloat = scaleFloat;

        canvas.save();
        canvas.translate(x, y - (y * mPercent));
        canvas.scale(scaleFloat, scaleFloat);
//        if (mIsRreshing) {
//            canvas.rotate(rotateFloat * 360, 0, y + radius / 2);
//        }
        canvas.drawCircle(0, 0, radius, circlePaint);
        canvas.restore();

        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scaleFloat, scaleFloat);
        canvas.drawCircle(0, 0, radius, circlePaint);
        canvas.restore();

        canvas.save();
        canvas.translate(x, y + (y * mPercent));
        canvas.scale(scaleFloat, scaleFloat);
//        if (mIsRreshing) {
//            canvas.rotate(rotateFloat * 360, 0, -(y + radius / 2));
//        }
        canvas.drawCircle(0, 0, radius, circlePaint);
        canvas.restore();

    }

    public void setPercent(float currentTop, float percent, boolean invalidate) {
        mCurrentTop = currentTop;
        setPercent(percent);
        setVariable(percent);
    }

    private void setPercent(float percent) {
        if (percent < 0.49f) percent = 0.49f;
        if (percent > 0.51f) percent = 0.51f;
        mPercent = percent;
    }

    public void offsetTopAndBottom(int offset) {
        invalidate();
    }


    private float setVariable(float value) {
        invalidate();
        return value;
    }

    public void start() {
        mIsRreshing = true;
        scaleAnim = ValueAnimator.ofFloat(0, 1, 0);
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.start();
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                scaleFloat = animatedValue <= 0.5f ? 0.5f : animatedValue;
                rotateFloat = animatedValue;
                invalidate();
            }
        });

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, getMeasuredWidth() / 2, mCurrentTop / 2);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(-1);
        this.startAnimation(rotateAnimation);

    }

    public void stop() {
        mIsRreshing = false;
        if (scaleAnim != null) {
            scaleAnim.end();
        }
        scaleFloat = 0.8f;
    }

    /**
     * dp转px
     *
     * @param dpValue
     * @return
     */
    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
