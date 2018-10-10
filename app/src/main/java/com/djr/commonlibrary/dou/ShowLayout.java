package com.djr.commonlibrary.dou;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.djr.commonlibrary.utils.DensityUtils;

/**
 * @author DongJr
 *
 * @date 2018/08/06
 */
public class ShowLayout extends RelativeLayout{

    private static final int PARENT_SIZE = DensityUtils.dp2px(52);
    private MusicalNoteView mDouyinView;
    private ShowHeadView mShowHeadView;

    public ShowLayout(Context context) {
        this(context, null);
    }

    public ShowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutParams musicNodeParams = new LayoutParams(PARENT_SIZE / 2, PARENT_SIZE);
        mDouyinView = new MusicalNoteView(getContext());
        mDouyinView.setLayoutParams(musicNodeParams);
        addView(mDouyinView);

        LayoutParams showHeadViewParams = new LayoutParams(PARENT_SIZE, PARENT_SIZE);
        mShowHeadView = new ShowHeadView(getContext());
        showHeadViewParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        mShowHeadView.setLayoutParams(showHeadViewParams);
        addView(mShowHeadView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentHeight = PARENT_SIZE;
        //默认整体宽高比应为3 ： 2；
        int parentWidth = parentHeight * 3 / 2;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void startAnimation(){
//        mDouyinView.startAnimation();
//        mShowHeadView.startAnimation();
    }

}
