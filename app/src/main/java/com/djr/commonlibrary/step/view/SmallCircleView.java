package com.djr.commonlibrary.step.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.djr.commonlibrary.R;


/**
 * Created by DongJr on 2016/6/1.
 */
public class SmallCircleView extends View {

    private Paint backCirclePaint;
    private Paint gradientCirclePaint;
    private Paint percentPaint;
    private Paint completePaint;

    private float circleBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
    private float circleBorderPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
    private float percentTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics());
    private float completeTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
    private String completeText = "完成";

    private float percent = 0f;
    int mCurrentStep = 0;
    int mTargetStep = 5000;

    public SmallCircleView(Context context) {
        super(context);
        init(context, null);
    }


    public SmallCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmallCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initTypeArray(context, attrs);

        backCirclePaint = new Paint();
        backCirclePaint.setAntiAlias(true);
        backCirclePaint.setColor(getResources().getColor(R.color.trans_light_grey));
        backCirclePaint.setStyle(Paint.Style.STROKE);
        backCirclePaint.setStrokeWidth(circleBorderWidth);

        gradientCirclePaint = new Paint();
        gradientCirclePaint.setAntiAlias(true);
        gradientCirclePaint.setColor(getResources().getColor(R.color.ThemeColor));
        gradientCirclePaint.setStyle(Paint.Style.STROKE);
        gradientCirclePaint.setStrokeWidth(circleBorderWidth);
        gradientCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        percentPaint = new Paint();
        percentPaint.setAntiAlias(true);
        percentPaint.setColor(Color.BLACK);
        percentPaint.setTextSize(percentTextSize);
        percentPaint.setTypeface(Typeface.DEFAULT_BOLD);

        completePaint = new Paint();
        completePaint.setAntiAlias(true);
        completePaint.setColor(Color.DKGRAY);
        completePaint.setTextSize(completeTextSize);

    }

    private void initTypeArray(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(measuredWidth, measuredHeight), Math.min(measuredWidth, measuredHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        RectF rectF = new RectF(circleBorderPadding, circleBorderPadding,
                getMeasuredWidth() - circleBorderPadding, getMeasuredHeight() - circleBorderPadding);
        canvas.drawArc(rectF, 0, 360, false, backCirclePaint);
        canvas.drawArc(rectF, -90, 360 * percent, false, gradientCirclePaint);

        String percentText = (int) (percent * 100) + "%";
        int centerX = getMeasuredWidth() / 2;

        float percentTextX = centerX - percentPaint.measureText(percentText) / 2;
        float percentTextY = getMeasuredHeight() / 2;
        canvas.drawText(percentText, percentTextX, percentTextY, percentPaint);

        int completeTextX = (int) (centerX - completePaint.measureText(completeText) / 2);
        int completeTextY = (int) (getMeasuredHeight() - circleBorderWidth -
                (completePaint.descent() - completePaint.ascent()));
        canvas.drawText(completeText, completeTextX, completeTextY, completePaint);
    }

    /**
     * 设置当前步数
     */
    public void setCurrentStep(int step) {

        mCurrentStep = step;

        percent = (float) mCurrentStep / mTargetStep;

        if (percent < 0) {
            percent = 0;
        }
//        else if (percent > 1) {
//            percent = 1;
//        }

        invalidate();
    }

}
