package com.djr.commonlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by DongJr on 2016/10/13.
 * <p/>
 * 展示星期的控件,可点击
 */
public class WeekView extends LinearLayout {

    private onWeekClickListener mWeekClickListener;

    private DateView mCurrentDateView;

    public WeekView(Context context) {
        super(context);
        init(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        addChildView();

        mCurrentDateView = (DateView) getChildAt(getWeekIndex());
        mCurrentDateView.setEnabled(true);
    }

    private int getWeekIndex() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        //获取当前日期星期,周日为1,周一为2
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        return week - 1;
    }

    private void addChildView() {
        removeAllViews();
        for (int i = 0; i < 7; i++) {
            final DateView dateView = new DateView(getContext(), i);
            addView(dateView);
            dateView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWeekClickListener != null) {
                        mWeekClickListener.onWeekClick(dateView.getWeek());
                    }

                    if (mCurrentDateView != null) {
                        mCurrentDateView.setEnabled(false);
                        mCurrentDateView = (DateView) getChildAt(dateView.getWeek());
                        mCurrentDateView.setEnabled(true);
                    }
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = widthSize / 7 + getPaddingTop() + getPaddingBottom();
        }

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View children = getChildAt(i);
            measureChild(children, widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        int posL = l;
        int posR = l + getChildAt(0).getMeasuredWidth();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout(posL, 0, posR, childView.getMeasuredHeight());
            posL += childView.getMeasuredWidth();
            posR = childView.getMeasuredWidth() + posL;
        }
    }

    public interface onWeekClickListener {
        void onWeekClick(int week);
    }

    public void setOnWeekClickListener(onWeekClickListener listener) {
        mWeekClickListener = listener;
    }
}
