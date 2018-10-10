package com.djr.commonlibrary.dou;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.djr.commonlibrary.R;
import com.djr.commonlibrary.utils.DensityUtils;


/**
 * @author DongJr
 * @date 2018/10/09
 * 闪动头像
 */
public class TwinkleHeadView extends RelativeLayout {

    private static final float DEFAULT_HEAD_SIZE = 114f;
    private static final float DEFAULT_CIRCLE_WIDTH = 5f;
    private static final int   DEFAULT_HEAD_COLOR = Color.parseColor("#EA22FE");
    private static final int   DEFAULT_HEAD_END_COLOR = Color.parseColor("#FD36AC");
    private static final int   DEFAULT_ANIMATION_DURATION = 1000;
    //固定圆到头像间距
    private static final int   DEFAULT_SPACING = DensityUtils.dp2px(2);
    //固定圆扩散区域
    private static final int   DEFAULT_SPREAD_SPACING = DensityUtils.dp2px(5);
    private static final int   DEFAULT_TAG_WIDTH = DensityUtils.dp2px(28);
    private static final int   DEFAULT_TAG_HEIGHT = DensityUtils.dp2px(10);
    private static final int   DEFAULT_TEXT_SIZE = DensityUtils.dp2px(8);


    //头像大小
    private float mTwinkleHeadSize;
    //圆圈的宽度
    private float mCircleWidth;
    //初始颜色(如果没有结束颜色，则不使用渐变)
    private int mTwinkleColor;
    //结束颜色，使用渐变
    private int mTwinkleEndColor;
    //圆圈画笔
    private Paint mCirclePaint;
    //标签画笔
    private Paint mTagPaint;
    //扩散圆圈动画的画笔
    private Paint mSpreadPaint;
    //文字画笔
    private Paint mTextPaint;
    private RectF mTagRectF;
    private ValueAnimator mValueAnimator;

    private float mCircleRadius;
    private float mSpreadRadius;
    private String mText;

    public TwinkleHeadView(@NonNull Context context) {
        this(context, null);
    }

    public TwinkleHeadView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwinkleHeadView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        initAttrs(context, attrs);
        initPaint();
        addImageView();
    }

    /**
     * 设置标签文字
     */
    public void setText(String text){
        mText = text;
    }

    /**
     * 开启闪动动画
     */
    public void startSpreadAnimation(){
        mValueAnimator = ValueAnimator.ofFloat(1f, 0f);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mSpreadPaint.setAlpha((int) (value * 255));
                mSpreadRadius = (int) (mCircleRadius + DEFAULT_SPREAD_SPACING * (1 - value));
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    /**
     * 取消闪动动画
     */
    public void cancleAnimation(){
        if (mValueAnimator != null && mValueAnimator.isRunning()){
            mValueAnimator.cancel();
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TwinkleHeadView);
        mTwinkleHeadSize = a.getDimension(R.styleable.TwinkleHeadView_twinkle_head_size, DEFAULT_HEAD_SIZE);
        mTwinkleColor = a.getColor(R.styleable.TwinkleHeadView_twinkle_color, DEFAULT_HEAD_COLOR);
        mTwinkleEndColor = a.getColor(R.styleable.TwinkleHeadView_twinkle_end_color, DEFAULT_HEAD_END_COLOR);
        mCircleWidth = a.getDimension(R.styleable.TwinkleHeadView_twinkle_circle_width, DEFAULT_CIRCLE_WIDTH);
        a.recycle();
        mCircleRadius = mTwinkleHeadSize / 2 + DEFAULT_SPACING;
    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleWidth);
        mCirclePaint.setColor(Color.BLUE);

        mTagPaint = new Paint();
        mTagPaint.setAntiAlias(true);
        mTagPaint.setStyle(Paint.Style.FILL);
        mTagPaint.setColor(mTwinkleColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DEFAULT_TEXT_SIZE);
        mTextPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = (int) (mTwinkleHeadSize + DEFAULT_SPACING * 2
                + DEFAULT_SPREAD_SPACING * 2 + mCircleWidth * 2);
        int measureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(measureSpec, measureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //圆圈渐变
        LinearGradient linearGradient = new LinearGradient(0, h / 2, w,
                getMeasuredHeight() / 2, mTwinkleColor, mTwinkleEndColor, Shader.TileMode.MIRROR);
        Matrix circleMatrix = new Matrix();
        circleMatrix.setRotate(45f, w / 2, h / 2);
        linearGradient.setLocalMatrix(circleMatrix);
        mCirclePaint.setShader(linearGradient);
        //扩散圆圈画笔
        mSpreadPaint = new Paint(mCirclePaint);
        //标签矩形区域
        mTagRectF = new RectF();
        mTagRectF.left = (w - DEFAULT_TAG_WIDTH) / 2;
        mTagRectF.right = (w + DEFAULT_TAG_WIDTH) / 2;
        mTagRectF.top = (h + mTwinkleHeadSize - DEFAULT_TAG_HEIGHT ) / 2 - DEFAULT_SPACING;
        mTagRectF.bottom = (h + mTwinkleHeadSize + DEFAULT_TAG_HEIGHT) / 2 - DEFAULT_SPACING;
        //标签矩形渐变
        LinearGradient tagLinearGradient = new LinearGradient(mTagRectF.left, mTagRectF.top, mTagRectF.right,
                mTagRectF.bottom, mTwinkleColor, mTwinkleEndColor, Shader.TileMode.MIRROR);
        Matrix tagMatrix = new Matrix();
        tagMatrix.setRotate(15f, w / 2, h / 2);
        tagLinearGradient.setLocalMatrix(tagMatrix);

        mTagPaint.setShader(tagLinearGradient);
        mTagPaint.setStrokeWidth(mCircleWidth / 2);
        startSpreadAnimation();
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        int cx = canvas.getWidth() / 2;
        int cy = canvas.getHeight() / 2;

        if (mValueAnimator != null){
            canvas.drawCircle(cx, cy, mCircleRadius, mCirclePaint);
            canvas.drawCircle(cx, cy, mSpreadRadius, mSpreadPaint);
        }

        if (!TextUtils.isEmpty(mText)){
            canvas.drawRoundRect(mTagRectF, 5, 5, mTagPaint);

            float textWidth = mTextPaint.measureText(mText);
            float baseLineY = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
            canvas.drawText(mText, cx - textWidth / 2,
                    mTagRectF.top + DEFAULT_TAG_HEIGHT / 2 + baseLineY, mTextPaint);
        }

    }

    private void addImageView() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams((int) mTwinkleHeadSize, (int) mTwinkleHeadSize);
        layoutParams.addRule(CENTER_IN_PARENT, TRUE);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.avatar);
        addView(imageView);
    }



}
