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
import android.view.View;
import android.widget.Checkable;

import com.djr.commonlibrary.R;


/**
 * Created by DongJr on 2016/10/9.
 * 六边形的CheckBox,暂不支持任意多边形,可以自定义;
 */
public class HexagonCheckBox extends View implements Checkable {

    //中心点x坐标
    private int mCenterX;
    //中心点y坐标
    private int mCenterY;
    //边长
    private int mSideLength = 30;
    //路径
    private Path mPath;
    //画笔
    private Paint mPaint;
    //选中时的画笔
    private Paint mCheckedPaint;
    //未选中时的画笔
    private Paint mUnCheckedPaint;
    //对勾画笔
    private Paint mRightPaint;
    //边数
    private static final int NUM_OF_SIDES = 6;
    //弧度,一个圆为2PI;
    private float mRadian = (float) (2 * Math.PI / NUM_OF_SIDES);
    //圆角
    private int mCorner = 6;
    //边框宽度
    private int mSideWith = 4;
    //基本颜色
    private int mBaseColor = getResources().getColor(R.color.hexagon_base);
    private boolean mChecked;
    private OnCheckedListener mCheckedListener;

    private String mGradeStr = "B";

    private String mLevelStr = "-";
    private Paint mLevelPaint;
    private Paint mTextPaint;
    private float mTextSize = 30f;
    private int mTextColor = getResources().getColor(R.color.hexagon_base);

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

    public HexagonCheckBox(Context context) {
        super(context);
        init(context, null);
    }

    public HexagonCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HexagonCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);

        CornerPathEffect effect = new CornerPathEffect(mCorner);

        mCheckedPaint = new Paint();
        mCheckedPaint.setAntiAlias(true);
        mCheckedPaint.setStyle(Paint.Style.FILL);
        mCheckedPaint.setPathEffect(effect);
        mCheckedPaint.setColor(mBaseColor);

        mRightPaint = new Paint();
        mRightPaint.setAntiAlias(true);
        mRightPaint.setStyle(Paint.Style.FILL);
        mRightPaint.setStrokeWidth(mSideWith);
        mRightPaint.setColor(getResources().getColor(R.color.white));

        mUnCheckedPaint = new Paint();
        mUnCheckedPaint.setAntiAlias(true);
        mUnCheckedPaint.setStyle(Paint.Style.STROKE);
        mUnCheckedPaint.setStrokeWidth(mSideWith);
        mUnCheckedPaint.setPathEffect(effect);
        mUnCheckedPaint.setColor(mBaseColor);

        mPaint = mUnCheckedPaint;

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


        mPath = new Path();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HexagonView);
            mSideLength = (int) array.getDimension(R.styleable.HexagonView_sideLength, 30);
            mSideWith = (int) array.getDimension(R.styleable.HexagonView_sideWidth, 4);
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

        drawHexagon(canvas, sinSideLength, cosSideLength);

        if (mChecked) {
            //如果勾选住,绘制对勾
            drawRight(canvas, sinSideLength, cosSideLength);
        } else {
            //未勾选住绘制文字
            drawText(canvas);
        }
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseLine = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mGradeStr, mCenterX, baseLine, mTextPaint);
        canvas.drawText(mLevelStr, mCenterX + mCenterX / 3
                , mCenterY, mLevelPaint);
    }

    private void drawRight(Canvas canvas, float sinSideLength, float cosSideLength) {
        canvas.drawLine(mCenterX, mCenterY + sinSideLength / 2,
                mCenterX - cosSideLength, mCenterY, mRightPaint);
        canvas.drawLine(mCenterX, mCenterY + sinSideLength / 2,
                mCenterX + cosSideLength, mCenterY - sinSideLength / 2, mRightPaint);
    }

    private void drawHexagon(Canvas canvas, float sinSideLength, float cosSideLength) {
        //把顶点作为起点
        mPath.moveTo(mCenterX, mCenterY - mSideLength);
        mPath.lineTo(mCenterX + sinSideLength, mCenterY - cosSideLength);
        mPath.lineTo(mCenterX + sinSideLength, mCenterY + cosSideLength);
        mPath.lineTo(mCenterX, mCenterY + mSideLength);
        mPath.lineTo(mCenterX - sinSideLength, mCenterY + cosSideLength);
        mPath.lineTo(mCenterX - sinSideLength, mCenterY - cosSideLength);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            if (mCheckedListener != null) {
                mCheckedListener.onCheckedChanged(mChecked);
            }
            mPaint = mChecked ? mCheckedPaint : mUnCheckedPaint;
            invalidate();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {

    }

    public interface OnCheckedListener {
        void onCheckedChanged(boolean isChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedListener onCheckedListener) {
        mCheckedListener = onCheckedListener;
    }

    /**
     * 设置View级别
     *
     * @param grade 等级:sabcd
     * @param level 层级:高中低
     */
    public void setGrade(int grade, int level) {
        dealWithGrade(grade);
        dealWithLevel(level);
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
                mLevelStr = "-";
                break;
            default:
                break;
        }
    }

    private void dealWithGrade(int grade) {
        switch (grade) {
            case GRADE_S:
                mGradeStr = "S";
                break;
            case GRADE_A:
                mGradeStr = "A";
                break;
            case GRADE_B:
                mGradeStr = "B";
                break;
            case GRADE_C:
                mGradeStr = "C";
                break;
            case GRADE_D:
                mGradeStr = "D";
                break;
            default:
                break;
        }
    }
}
