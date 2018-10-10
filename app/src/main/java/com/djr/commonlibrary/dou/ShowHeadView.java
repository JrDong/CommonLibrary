package com.djr.commonlibrary.dou;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.djr.commonlibrary.R;
import com.djr.commonlibrary.utils.DensityUtils;

/**
 * @author DongJr
 *
 * @date 2018/08/06
 */
public class ShowHeadView extends RelativeLayout{

    private Paint mCirclePaint;
    private Paint mCircleFramePaint;
    private int mParentSize ;
    private int mHeadSize ;
    private int mHeadRadius ;
    private static final int ANIMATION_DURATION = 1000;
    private int mFrameRadius;

    public ShowHeadView(Context context) {
        this(context , null);
    }

    public ShowHeadView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public ShowHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor("#6924F2"));
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(2);

        mCircleFramePaint = new Paint();
        mCircleFramePaint.setColor(Color.parseColor("#6924F2"));
        mCircleFramePaint.setAntiAlias(true);
        mCircleFramePaint.setStyle(Paint.Style.STROKE);
        mCircleFramePaint.setStrokeWidth(2);

        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mParentSize = h;
        mHeadSize = (int) (h * 0.8);
        mHeadRadius = (int) (h * 0.45);
        mFrameRadius = mHeadRadius;
        addImageView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mParentSize / 2, mParentSize / 2, mHeadRadius, mCirclePaint);
        canvas.drawCircle(mParentSize / 2, mParentSize / 2, mFrameRadius, mCircleFramePaint);
    }

    private void addImageView(){
        final ImageView headView = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(mHeadSize, mHeadSize);
        lp.addRule(CENTER_IN_PARENT, TRUE);
        headView.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
        addView(headView, lp);
        startFrameAnimation();
        startHeadTwinkleAnimation(headView);
    }

    private void startFrameAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0f);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCircleFramePaint.setAlpha((int) (value * 255));
                mFrameRadius = (int) (mHeadRadius + DensityUtils.dp2px(4) * (1 - value));
                invalidate();
            }
        });
        valueAnimator.setStartDelay(1000);
        valueAnimator.start();
    }

    private void startHeadTwinkleAnimation(View target) {
        //先闪动4次，再缩放切换
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 1f, 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 1f, 0.9f, 1f);
        scaleX.setRepeatCount(4);
        scaleY.setRepeatCount(4);
        AnimatorSet twinkleSet = new AnimatorSet();
        twinkleSet.setInterpolator(new LinearInterpolator());
        twinkleSet.setDuration(ANIMATION_DURATION).playTogether(scaleX, scaleY);


        ObjectAnimator changeScaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 1f, 0.2f, 1f);
        ObjectAnimator changeScaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 1f, 0.2f, 1f);
        AnimatorSet changeSet = new AnimatorSet();
        changeSet.setInterpolator(new AccelerateDecelerateInterpolator());
        changeSet.setDuration(ANIMATION_DURATION).playTogether(changeScaleX, changeScaleY);


        final AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(twinkleSet, changeSet);
        finalSet.setTarget(target);
        finalSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finalSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        finalSet.start();
    }

}
