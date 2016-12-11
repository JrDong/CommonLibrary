package com.djr.commonlibrary.view.hexagon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.djr.commonlibrary.R;


/**
 * Created by DongJr on 2016/10/10.
 * 顶部导航栏六边形View
 * <p/>
 * 可以自定义的属性有:l
 * 边长,边宽,颜色
 */
public class HexagonView extends View {

    private Path mPath;
    private Path mOuterPath;
    //六边形的画笔
    private Paint mHexagonPaint;
    private Paint mHexagonPaint1;
    //分割线画笔
    private Paint mTransPaint;
    //文字的画笔
    private Paint mTextPaint;
    private Paint mLevelPaint;

    //边长
    private int mSideLength = 120;
    //中心点x坐标
    private int mCenterX;
    //中心点y坐标
    private int mCenterY;
    //边数
    private static final int NUM_OF_SIDES = 6;
    //弧度,一个圆为2PI;
    private float mRadian = (float) (2 * Math.PI / NUM_OF_SIDES);
    //圆角
    private float mCorner = 10;
    //分割线长度
    private int mSplitLine = 15;
    //边框宽度
    private int mSideWith = 15;
    private int mTextSize = 90;
    private int mTextColor = getResources().getColor(R.color.hexagon_d);

    //基本颜色
    private int mBaseColor = getResources().getColor(R.color.hexagon_base);

    private int mOutPaintColor = getResources().getColor(R.color.hexagon_d);

    private String mGradeStr = "";

    private String mLevelStr = "+";

    /**
     * 等级,SABCD ,每个等级下又分三个小等级;
     */
    public static final int GRADE_S = 0;
    public static final int GRADE_A = 1;
    public static final int GRADE_B = 2;
    public static final int GRADE_C = 3;
    public static final int GRADE_D = 4;

    public static final int GRADE_HIGH = 5;
    public static final int GRADE_NORMAL = 6;
    public static final int GRADE_LOW = 7;

    private int mCurrentLevel;
    private int mCurrentGrade;

    public HexagonView(Context context) {
        super(context);
        init(context, null);
    }

    public HexagonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HexagonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);

        mCurrentLevel = GRADE_NORMAL;
        mPath = new Path();
        mOuterPath = new Path();
        CornerPathEffect effect = new CornerPathEffect(mCorner);
        mHexagonPaint = new Paint();
        mHexagonPaint.setStyle(Paint.Style.STROKE);
        mHexagonPaint.setAntiAlias(true);
        mHexagonPaint.setPathEffect(effect);
        mHexagonPaint.setColor(mBaseColor);
        mHexagonPaint.setStrokeWidth(mSideWith);

        mHexagonPaint1 = new Paint();
        mHexagonPaint1.setStyle(Paint.Style.STROKE);
        mHexagonPaint1.setAntiAlias(true);
        mHexagonPaint1.setPathEffect(effect);
        mHexagonPaint1.setColor(mOutPaintColor);
        mHexagonPaint1.setStrokeWidth(mSideWith);

        mTransPaint = new Paint();
        mTransPaint.setStyle(Paint.Style.FILL);
        mTransPaint.setAntiAlias(true);
        mTransPaint.setColor(getResources().getColor(R.color.white));
        mTransPaint.setStrokeWidth(mSideWith / 3);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mLevelPaint = new Paint();
        mLevelPaint.setStyle(Paint.Style.FILL);
        mLevelPaint.setAntiAlias(true);
        mLevelPaint.setTextSize(mTextSize * 2 / 3);
        mLevelPaint.setTextAlign(Paint.Align.LEFT);
        mLevelPaint.setColor(mTextColor);
        mLevelPaint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HexagonView);
            mSideLength = (int) array.getDimension(R.styleable.HexagonView_sideLength, 120);
            mSideWith = (int) array.getDimension(R.styleable.HexagonView_sideWidth, 15);
            mSplitLine = mSideWith;
            mTextSize = (int) array.getDimension(R.styleable.HexagonView_hexTextSize, 90);
            mTextColor = array.getColor(R.styleable.HexagonView_hexTextColor, ContextCompat.getColor(context, R.color.hexagon_d));
            mBaseColor = array.getColor(R.styleable.HexagonView_baseColor, ContextCompat.getColor(context, R.color.hexagon_base));
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            widthSize = mSideLength * 2 + getPaddingLeft() + getPaddingRight();
            heightSize = mSideLength * 2 + getPaddingTop() + getPaddingBottom() + mSideLength / 3;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float sinSideLength = (float) Math.sin(mRadian) * mSideLength;

        float cosSideLength = (float) Math.cos(mRadian) * mSideLength;

        drawPath(canvas, sinSideLength, cosSideLength);

        if (mCurrentLevel == GRADE_LOW) {
            drawOutPathLow(canvas, sinSideLength, cosSideLength);
        } else if (mCurrentLevel == GRADE_NORMAL) {
            drawOutPathNormal(canvas, sinSideLength, cosSideLength);
        }

        drawText(canvas);

    }

    private void drawText(Canvas canvas) {
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseLine = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mGradeStr, mCenterX, baseLine, mTextPaint);
        canvas.drawText(mLevelStr, mCenterX + mCenterX / 5
                , mCenterY, mLevelPaint);
    }

    /**
     * 绘制中等级外层path
     */
    private void drawOutPathNormal(Canvas canvas, float sinSideLength, float cosSideLength) {

        float sinSplitLine = (float) Math.sin(mRadian) * mSplitLine;
        float cosSplitLine = (float) Math.cos(mRadian) * mSplitLine;

        mOuterPath.moveTo(mCenterX - sinSideLength, mCenterY - cosSideLength);
        mOuterPath.lineTo(mCenterX - sinSideLength, mCenterY + cosSideLength);
        mOuterPath.lineTo(mCenterX, mCenterY + mSideLength);
        mOuterPath.lineTo(mCenterX + sinSideLength, mCenterY + cosSideLength);
        mOuterPath.lineTo(mCenterX + sinSideLength, mCenterY - cosSideLength);
        canvas.drawPath(mOuterPath, mHexagonPaint1);

        //绘制分隔线
        canvas.drawLine(mCenterX - sinSideLength - sinSplitLine / 2,
                mCenterY - cosSideLength - cosSplitLine / 2,
                mCenterX - sinSideLength + sinSplitLine,
                mCenterY - cosSideLength + cosSplitLine, mTransPaint
        );
        canvas.drawLine(mCenterX + sinSideLength - sinSplitLine,
                mCenterY - cosSideLength + cosSplitLine,
                mCenterX + sinSideLength + sinSplitLine / 2,
                mCenterY - cosSideLength - cosSplitLine / 2, mTransPaint
        );
    }

    /**
     * 绘制低等级外层path
     */
    private void drawOutPathLow(Canvas canvas, float sinSideLength, float cosSideLength) {
        float sinSplitLine = (float) Math.sin(mRadian) * mSplitLine;
        float cosSplitLine = (float) Math.cos(mRadian) * mSplitLine;

        mOuterPath.moveTo(mCenterX - sinSideLength, mCenterY + cosSideLength);
        mOuterPath.lineTo(mCenterX, mCenterY + mSideLength);
        mOuterPath.lineTo(mCenterX + sinSideLength, mCenterY + cosSideLength);
        canvas.drawPath(mOuterPath, mHexagonPaint1);

        //绘制分割线
        canvas.drawLine(mCenterX - sinSideLength - sinSplitLine / 2,
                mCenterY + cosSideLength + cosSplitLine / 2,
                mCenterX - sinSideLength + sinSplitLine,
                mCenterY + cosSideLength - cosSplitLine, mTransPaint
        );
        canvas.drawLine(mCenterX + sinSideLength - sinSplitLine,
                mCenterY + cosSideLength - cosSplitLine,
                mCenterX + sinSideLength + sinSplitLine / 2,
                mCenterY + cosSideLength + cosSplitLine / 2, mTransPaint
        );

    }

    /**
     * 绘制底层六边形
     */
    private void drawPath(Canvas canvas, float sinSideLength, float cosSideLength) {

        //把顶点作为起点
        mPath.moveTo(mCenterX, mCenterY - mSideLength);
        mPath.lineTo(mCenterX + sinSideLength, mCenterY - cosSideLength);
        mPath.lineTo(mCenterX + sinSideLength, mCenterY + cosSideLength);
        mPath.lineTo(mCenterX, mCenterY + mSideLength);
        mPath.lineTo(mCenterX - sinSideLength, mCenterY + cosSideLength);
        mPath.lineTo(mCenterX - sinSideLength, mCenterY - cosSideLength);

        mPath.close();

        canvas.drawPath(mPath, mHexagonPaint);
    }

    /**
     * 按压效果
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setEnabled(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setEnabled(false);
                break;
        }
        return true;
    }

    /**
     * 设置交互效果
     *
     * @param enable true 选中,false 未选中
     */
    public void setEnabled(boolean enable) {
        if (enable) {
            mHexagonPaint.setColor(mOutPaintColor);
            mHexagonPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mHexagonPaint1.setColor(getResources().getColor(R.color.color_transparent));
            mTransPaint.setColor(getResources().getColor(R.color.color_transparent));
        } else {
            mHexagonPaint.setColor(mCurrentLevel == GRADE_HIGH ? mOutPaintColor : mBaseColor);
            mHexagonPaint.setStyle(Paint.Style.STROKE);
            mHexagonPaint1.setColor(mOutPaintColor);
            mTransPaint.setColor(getResources().getColor(R.color.white));
        }
        invalidate();
    }


    /**
     * 设置View级别
     *
     * @param grade 等级:sabcd
     * @param level 层级:高中低
     */
    public void setGrade(int grade, int level) {
        mCurrentGrade = grade;
        mCurrentLevel = level;
        dealWithGrade(grade);
        dealWithLevel(level);
        mHexagonPaint.setColor(mCurrentLevel == GRADE_HIGH ? mOutPaintColor : mBaseColor);
        mHexagonPaint1.setColor(mOutPaintColor);
        invalidate();
    }

    private void dealWithLevel(int level) {
        switch (level) {
            case GRADE_HIGH:
                mLevelStr = "";
                break;
            case GRADE_NORMAL:
                mLevelStr = "+";
                break;
            case GRADE_LOW:
                mLevelStr = " -";
                break;
            default:
                break;
        }
    }

    private void dealWithGrade(int grade) {
        switch (grade) {
            case GRADE_S:
                mGradeStr = "S";
                mOutPaintColor = getResources().getColor(R.color.hexagon_s);
                break;
            case GRADE_A:
                mGradeStr = "A";
                mOutPaintColor = getResources().getColor(R.color.hexagon_a);
                break;
            case GRADE_B:
                mGradeStr = "B";
                mOutPaintColor = getResources().getColor(R.color.hexagon_b);
                break;
            case GRADE_C:
                mGradeStr = "C";
                mOutPaintColor = getResources().getColor(R.color.hexagon_c);
                break;
            case GRADE_D:
                mGradeStr = "D";
                mOutPaintColor = getResources().getColor(R.color.hexagon_d);
                break;
            default:
                break;
        }

    }
}
