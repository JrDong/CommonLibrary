package com.djr.commonlibrary.dou;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.djr.commonlibrary.R;

import java.util.Random;

/**
 * @author DongJr
 *
 * 抖音效果的音符运动轨迹
 */
public class MusicalNoteView extends RelativeLayout {

    private Paint mBezierPaint;
    private PointF mControl;
    private PointF mStartPoint;
    private PointF mEndPoint;
    private Drawable[] mDrawableArr;
    private Random mRandom;
    private Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            addMusicNodeView();
            mHander.sendEmptyMessageDelayed(0, 1000);
        }
    };

    public MusicalNoteView(Context context) {
        this(context, null);
    }

    public MusicalNoteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicalNoteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBezierPaint = new Paint();
        mBezierPaint.setColor(Color.RED);
        mBezierPaint.setStrokeWidth(10);
        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setAntiAlias(true);

        mDrawableArr = new Drawable[]{
                getResources().getDrawable(R.drawable.icon_music_node1),
                getResources().getDrawable(R.drawable.icon_music_node2)};
        mRandom = new Random();
    }

    private void addMusicNodeView() {
        final ImageView musicNodeView = new ImageView(getContext());
        musicNodeView.setImageDrawable(mDrawableArr[mRandom.nextInt(2)]);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        addView(musicNodeView ,lp);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(musicNodeView);
        bezierValueAnimator.setInterpolator(new LinearInterpolator());
        bezierValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(musicNodeView);
            }
        });
        bezierValueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartPoint = new PointF(w, h);
        mEndPoint = new PointF(w / 2, 0);
        mControl = new PointF(0, h * 0.6f);
        startAnimation();
    }

    public void startAnimation(){
        mHander.removeCallbacksAndMessages(null);
        mHander.sendEmptyMessageDelayed(0, 1000);
    }

    private ValueAnimator getBezierValueAnimator(final View target) {
        // 初始化贝塞尔估值器
        BezierEvaluator evaluator = new BezierEvaluator(mControl);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, mStartPoint, mEndPoint);
        animator.setTarget(target);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                float animatedFraction = valueAnimator.getAnimatedFraction();
                target.setX(pointF.x);
                target.setY(pointF.y);
                if (animatedFraction <= 0.5f){
                    //0 - 1f
                    target.setAlpha(animatedFraction * 2f);
                    //1 - 1.5f
                    target.setScaleX(1f + animatedFraction);
                    target.setScaleY(1f + animatedFraction);
                } else {
                    //1f - 0f
                    target.setAlpha(2 - (animatedFraction * 2));
                    //1.5f - 1
                    target.setScaleX(2f - animatedFraction);
                    target.setScaleY(2f - animatedFraction);
                }
            }
        });

        animator.setDuration(3000);
        return animator;
    }



}
