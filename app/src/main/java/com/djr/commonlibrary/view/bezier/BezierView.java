package com.djr.commonlibrary.view.bezier;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DongJr on 2016/7/19.
 */
public class BezierView extends View {

    Paint mPaint;
    Paint mLinePaint;
    float mSupX = 200;
    float mSupX2 = 200;
    float mSupY = 300;
    float mSupY2 = 300;
    boolean moveFirst;
    boolean moveSecond;

    public BezierView(Context context) {
        super(context);
        init();
    }


    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BezierView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.BLUE);
    }

    public void setFirstPoint() {
        moveFirst = true;
        moveSecond = false;
    }

    public void setSecondPoint() {
        moveFirst = false;
        moveSecond = true;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Path path = new Path();
        path.moveTo(200, 200);
        path.cubicTo(mSupX, mSupY, mSupX2, mSupY2, 400, 200);
        canvas.drawPath(path, mPaint);
        canvas.drawPoint(mSupX, mSupY, mPaint);
        canvas.drawPoint(mSupX2, mSupY2, mPaint);
        canvas.drawLine(200, 200, mSupX, mSupY, mLinePaint);
        canvas.drawLine(400, 200, mSupX2, mSupY2, mLinePaint);
        canvas.drawLine(mSupX, mSupY, mSupX2, mSupY2, mLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (moveFirst) {
                    mSupX = event.getX();
                    mSupY = event.getY();
                }
                if (moveSecond) {
                    mSupX2 = event.getX();
                    mSupY2 = event.getY();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }
}
