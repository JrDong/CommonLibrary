package com.djr.commonlibrary.view.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.djr.commonlibrary.R;
import com.djr.commonlibrary.utils.DensityUtils;


/**
 * Created by DongJr on 2016/10/13.
 * 单个日期的View,背景是圆,中间是星期
 */
public class DateView extends View {
    //0-6分别代表周日~周六
    private int mWeek;

    private float mCircleRadius = 40;

    private int mCenterX;

    private int mCenterY;

    private Paint mCirclePaint;

    private Paint mTextPaint;

    private String[] mWeekArray = {"日", "一", "二", "三", "四", "五", "六"};


    public DateView(Context context, int week) {
        super(context);
        this.mWeek = week;
        init(context, null);
    }

    public DateView(Context context) {
        super(context);
        init(context, null);
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(DensityUtils.dp2px(1));
        mCirclePaint.setColor(getResources().getColor(R.color.black));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(getResources().getColor(R.color.black));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(DensityUtils.sp2px(12));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            int windowWidth = manager.getDefaultDisplay().getWidth();
            widthSize = windowWidth / 7;
            heightSize = windowWidth / 7;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mCirclePaint);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseLine = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mWeekArray[mWeek], mCenterX, baseLine, mTextPaint);
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mTextPaint.setColor(getResources().getColor(R.color.white));
        } else {
            mCirclePaint.setStyle(Paint.Style.STROKE);
            mTextPaint.setColor(getResources().getColor(R.color.black));
        }
        invalidate();
    }

    public int getWeek() {
        return mWeek;
    }

    public void setWeek(int week) {
        this.mWeek = week;
        invalidate();
    }
}
