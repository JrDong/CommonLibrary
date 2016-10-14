package com.djr.commonlibrary.step;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.djr.commonlibrary.CLApplication;
import com.djr.commonlibrary.R;
import com.djr.commonlibrary.main.UserInfoCenter;
import com.djr.commonlibrary.step.data.StepConstant;
import com.djr.commonlibrary.step.data.StepCountAdapter;
import com.djr.commonlibrary.step.data.StepDBManager;
import com.djr.commonlibrary.step.view.CalibrationView;
import com.djr.commonlibrary.step.view.CustomScrollView;
import com.djr.commonlibrary.step.view.TrendView;
import com.djr.commonlibrary.utils.DateUtils;
import com.djr.commonlibrary.utils.DensityUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StepCountActivity extends AppCompatActivity implements OnPageChangeListener, StepListener, Toolbar.OnMenuItemClickListener {


    @Bind(R.id.ll_week)
    LinearLayout llWeek;

    @Bind(R.id.vp_step_count)
    ViewPager vpStepCount;

    @Bind(R.id.ll_circle)
    LinearLayout llCircle;

    @Bind(R.id.ll_chart)
    LinearLayout llChart;

    @Bind(R.id.stepCount)
    TextView mStepCount;

    @Bind(R.id.stepDistance)
    TextView mStepDistance;

    @Bind(R.id.ConsumptionOfEnergy)
    TextView mConsumptionOfEnergy;

    @Bind(R.id.movementTime)
    TextView mMovementTime;

    @Bind(R.id.scrollView)
    CustomScrollView mScrollView;

    @Bind(R.id.trendviewContainer)
    LinearLayout mTrendviewContainer;

    @Bind(R.id.calibrationView)
    CalibrationView mCalibrationView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private List<StepDBModel> mList;

    private String[] weeks = new String[]{"一", "二", "三", "四", "五", "六", "日"};

    private SparseArray<TextView> textArray;

    private StepCountAdapter stepCountAdapter;

    private int week = 0;

    /*chart*/ int count = 30;


    private TrendView mTrendview;

    private Handler mHandler;

    private TrendView.OnPointClick mOnPointClick;

    private ArrayList<ArrayList<Float>> pointList;

    private final int MSG_CHART = 1;

    private final int MSG_CIRCLE = 2;

    //折线图的最大值
    private int MaxValue = 7500;

    public static void startActivity(Activity activity) {
        Intent i = new Intent();
        i.setClass(activity, StepCountActivity.class);
        activity.startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        ButterKnife.bind(this);
        initTitle();
        initData();

        initChart();
        initListener();

    }

    private void initTitle() {
        mToolbar.setTitle("STEP-COUNT");
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_step, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_circle:
                llCircle.setVisibility(View.VISIBLE);
                llChart.setVisibility(View.GONE);
                break;
            case R.id.action_chart:
                llCircle.setVisibility(View.GONE);
                llChart.setVisibility(View.VISIBLE);
                break;
        }
        return false;
    }

    private void initListener() {
        StepDetector.addStepListener(this);
    }


    private void onPointclick(int index) {
        int count = 0;
        if (pointList.size() > 0) {
            count = pointList.get(0)
                    .get(index)
                    .intValue();
        }

        DecimalFormat df = new DecimalFormat("######0.##");
        DecimalFormat c_df = new DecimalFormat("######0");

        //运动步数
        mStepCount.setText(count + "");

        //运动距离
        double distance = (count * StepConstant.everyStepKm);
        mStepDistance.setText(df.format(distance) + "km");

        //运动时间
        double totleTime = count * StepConstant.everyStepMinute;
        int hour = (int) (totleTime / 60);
        String minute = c_df.format(totleTime - hour * 60);
        if (hour == 0) {
            mMovementTime.setText(minute + "分钟");
        } else {
            mMovementTime.setText(hour + "小时" + minute + "分钟");
        }

        //消耗卡路里
        mConsumptionOfEnergy.setText(c_df.format(count * StepConstant.everyStepCalorie) + "卡");

    }

    private void initChart() {

        mOnPointClick = new TrendView.OnPointClick() {
            @Override
            public void OnPointClickevent(final int index) {

                onPointclick(index);
            }
        };

        ArrayList colorList = new ArrayList();
        colorList.add(Color.parseColor("#FFFFFF"));

        //points.set(0,100);
        pointList = new ArrayList<>();

        final ArrayList<Float> lines = new ArrayList();
        lines.add(5000f);

        mTrendview = new TrendView(StepCountActivity.this, new ArrayList<ArrayList<Float>>());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT);
        mTrendview.setMaxValue(MaxValue);
        mTrendview.setLayoutParams(params);
        mTrendview.setColorList(colorList);
        //左右的空隙
        mTrendview.paddingLeftRightSpace = 16;
        mTrendview.setShowAxisList(lines);

        mTrendviewContainer.addView(mTrendview);
        mTrendview.setVisibility(View.INVISIBLE);
        mTrendview.registListener();
        mTrendview.setOnPointClick(mOnPointClick);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                mTrendview.setVisibility(View.VISIBLE);
                mTrendview.initCalibrationYValue();

                mCalibrationView.setAxisLineValueList(lines);
                mCalibrationView.setShowAxisList(mTrendview.getCalibrationYValue());
                mCalibrationView.invalidate();
            }
        }, 100);

        new Thread() {
            @Override
            public void run() {
                ArrayList<StepDBModel> monthData = StepDBManager.getIns()
                        .getMonthData();

                LinkedHashMap<String, Float> map;
                map = HealthRecordUtil.getWeekDateHashMap();
                Set key = map.keySet();
                for (StepDBModel stepDBModel : monthData) {
                    if (key.contains(stepDBModel.getDate())) {
                        map.put(stepDBModel.getDate(), (float) stepDBModel.getStepCount());
                    }
                }
                ArrayList<Float> floats = new ArrayList<Float>();
                floats.addAll(map.values());

                Collections.reverse(floats);

                pointList.clear();
                pointList.add(floats);

                MaxValue = Collections.max(floats).intValue();
                if (MaxValue <= 5000) {
                    MaxValue = 7500;
                } else {
                    MaxValue = (int) (MaxValue * 1.1);
                }

                mHandler.sendEmptyMessage(MSG_CHART);
            }
        }.start();

    }


    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mList = getData();
                mHandler.sendEmptyMessage(MSG_CIRCLE);
            }
        }).start();

        mHandler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                switch (msg.what) {
                    case MSG_CHART:
                        mTrendview.setMaxValue(MaxValue);
                        mTrendview.scaleTemspace();
                        mTrendview.setPointList(pointList);
                        mTrendview.invalidate();

                        mTrendview.initCalibrationYValue();
                        mCalibrationView.setShowAxisList(mTrendview.getCalibrationYValue());
                        mCalibrationView.invalidate();

                        onPointclick(pointList.get(0).size() - 1);
                        break;
                    case MSG_CIRCLE:
                        initViewPager();
                        initIndicator();
                        break;
                }

            }
        };
    }

    private List<StepDBModel> getData() {
        StepDBManager manager = StepDBManager.getIns();
        //        stepDBManager.writeStepToDb();
        ArrayList<StepDBModel> monthData = manager.getMonthData();
        LinkedHashMap<String, StepDBModel> map = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long currentTime = System.currentTimeMillis();
        int dateCount = manager.getDateCount();
        for (int i = dateCount; i >= 0; i--) {
            Date d = new Date(currentTime - i * (1000L * 60 * 60 * 24));
            StepDBModel model = new StepDBModel();
            model.setUserid(UserInfoCenter.getUserId());
            model.setStepCount(0);
            model.setDate(dateFormat.format(d));
            map.put(dateFormat.format(d), model);
        }
        Set key = map.keySet();
        for (StepDBModel stepDBModel : monthData) {
            if (key.contains(stepDBModel.getDate())) {
                map.put(stepDBModel.getDate(), stepDBModel);
            }
        }
        ArrayList<StepDBModel> newMonthDate = new ArrayList<>();
        newMonthDate.addAll(map.values());
        return newMonthDate;
    }


    private void initViewPager() {
        stepCountAdapter = new StepCountAdapter(CLApplication.getAppContext(), mList, vpStepCount);
        vpStepCount.setAdapter(stepCountAdapter);
        vpStepCount.setCurrentItem(mList.size() - 1);
    }

    private void initIndicator() {
        textArray = new SparseArray<>();
        llWeek.removeAllViews();
        if (mList != null && mList.size() > 0) {
            week = DateUtils.getWeekIndex(mList.get(mList.size() - 1)
                    .getDate());
        }
        for (int i = 0; i < 7; i++) {
            View view = View.inflate(this, R.layout.item_step_count_indicator, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(30), DensityUtils.dp2px(30));
            if (i != 0) {
                lp.leftMargin = DensityUtils.dp2px(20);
            }
            view.setLayoutParams(lp);
            FrameLayout flBg = (FrameLayout) view.findViewById(R.id.fl_bg);
            TextView tvIndicator = (TextView) view.findViewById(R.id.tv_indicator_week);
            tvIndicator.setText(weeks[i]);
            if (i == week) {
                flBg.setEnabled(true);
                tvIndicator.setEnabled(true);
            } else {
                flBg.setEnabled(false);
                tvIndicator.setEnabled(false);
                if (i > week) {
                    tvIndicator.setTextColor(getResources().getColor(R.color.trans_light_grey));
                }
                if (mList.size() - 1 < week && i < week - mList.size() + 1) {
                    tvIndicator.setTextColor(getResources().getColor(R.color.trans_light_grey));
                }
            }
            llWeek.addView(view);
            textArray.put(i, tvIndicator);
        }
        vpStepCount.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mList != null && mList.size() > 0) {
            int newPosition = DateUtils.getWeekIndex(mList.get(position)
                    .getDate());
            for (int i = 0; i < 7; i++) {
                llWeek.getChildAt(i)
                        .setEnabled(false);
                textArray.get(i)
                        .setEnabled(false);
                int firstWeek = DateUtils.getWeekIndex(mList.get(0)
                        .getDate());
                int lastWeek = DateUtils.getWeekIndex(mList.get(mList.size() - 1)
                        .getDate());

                if (0 <= newPosition && newPosition <= lastWeek && (mList.size() - 1 - position) < 7 && i > lastWeek
                        || newPosition >= firstWeek && newPosition <= 6 && (position < 7) && i < firstWeek) {
                    textArray.get(i)
                            .setTextColor(getResources().getColor(R.color.trans_light_grey));
                } else {
                    textArray.get(i)
                            .setTextColor(getResources().getColor(R.color.text_color_999));
                }

                if (i == newPosition) {
                    llWeek.getChildAt(i)
                            .setEnabled(true);
                    textArray.get(i)
                            .setTextColor(getResources().getColor(R.color.white));
                    //                    textArray.get(i).setEnabled(true);
                }
            }

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private boolean hasReceived = false;

    @Override
    public void onStep(int stepCount) {
        if (mList != null && mList.size() > 0) {
            mList.get(mList.size() - 1)
                    .setStepCount(stepCount);
            if (stepCountAdapter != null) {
                stepCountAdapter.setData(mList);
                stepCountAdapter.notifyDataSetChanged();
            }
            //当步数实时变动时,改变领取按钮状态,设置标志位,只改变一次.节约性能
            if (!hasReceived && stepCount >= 5000 && !StepDBManager.getIns()
                    .isUploadPoints()) {

                hasReceived = true;
            }
        }
    }

    @Override
    public void passValue() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        StepDBManager.getIns()
                .writeStepToDb();
        StepDBManager.getIns()
                .getStepCountFromDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (StepDetector.mStepListeners != null) {
            StepDetector.mStepListeners.remove(this);
        }
    }


}
