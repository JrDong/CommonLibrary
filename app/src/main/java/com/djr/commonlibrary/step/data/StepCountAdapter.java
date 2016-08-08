package com.djr.commonlibrary.step.data;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.djr.commonlibrary.R;
import com.djr.commonlibrary.base.viewpager.RecyclingPagerAdapter;
import com.djr.commonlibrary.step.StepDBModel;
import com.djr.commonlibrary.step.view.CircleView;
import com.djr.commonlibrary.step.view.DateTextView;
import com.djr.commonlibrary.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by DongJr on 2016/5/30.
 */
public class StepCountAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<StepDBModel> list;
    private ViewPager vpStepCount;

    private float DIS = (float) StepConstant.everyStepKm;
    private float CALORIES = (float) StepConstant.everyStepCalorie;

    /*1500步需要15分钟,换算成小数单位时间*/
    private float TIME = (float) StepConstant.everyStepHour;

    private DecimalFormat dfDis = new DecimalFormat("#0.00");
    private DecimalFormat dfTime = new DecimalFormat("#0.0");
    private DecimalFormat c_df = new DecimalFormat("######0");

    public StepCountAdapter(Context context, List<StepDBModel> list, ViewPager vpStepCount) {
        this.context = context;
        this.list = list;
        this.vpStepCount = vpStepCount;
    }

    public void setData(List<StepDBModel> list) {
        this.list = list;
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    //1km=1500步=15分钟=35卡
    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_step_count, container, false);
            viewHolder = new ViewHolder();
            viewHolder.dateTextView = (DateTextView) convertView.findViewById(R.id.tv_date);
            viewHolder.circleView = (CircleView) convertView.findViewById(R.id.step_view);
            viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            viewHolder.tvCalories = (TextView) convertView.findViewById(R.id.tv_calories);
            viewHolder.tvSportHour = (TextView) convertView.findViewById(R.id.tv_sport_hour);
            viewHolder.tvSportMinute = (TextView) convertView.findViewById(R.id.tv_sport_minute);
            viewHolder.tvHour = (TextView) convertView.findViewById(R.id.tv_hour);
            viewHolder.tvMinute = (TextView) convertView.findViewById(R.id.tv_minute);
            viewHolder.ibtToday = (ImageButton) convertView.findViewById(R.id.ibt_back_today);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StepDBModel model = list.get(position);
        initData(viewHolder, model);
        if (position == list.size() - 1) {
            viewHolder.ibtToday.setVisibility(View.GONE);
        } else {
            viewHolder.ibtToday.setVisibility(View.VISIBLE);
        }
        viewHolder.ibtToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vpStepCount != null && list.size() > 0) {
                    vpStepCount.setCurrentItem(list.size() - 1);
                }
            }
        });
        return convertView;
    }

    private void initData(ViewHolder viewHolder, StepDBModel model) {
        viewHolder.dateTextView.setDate(model.getDate(),
                DateUtils.getWeek(model.getDate()));
        viewHolder.circleView.setCurrentStep(model.getStepCount());
        //运动时间
        double totleTime = model.getStepCount() * StepConstant.everyStepMinute;
        int hour = (int) (totleTime / 60);
        String minute = c_df.format(totleTime - hour * 60);
        if (hour == 0) {
            viewHolder.tvSportHour.setVisibility(View.GONE);
            viewHolder.tvHour.setVisibility(View.GONE);
            viewHolder.tvSportMinute.setText(minute);
        } else {
            viewHolder.tvSportHour.setVisibility(View.VISIBLE);
            viewHolder.tvHour.setVisibility(View.VISIBLE);
            viewHolder.tvSportHour.setText(hour + "");
            viewHolder.tvSportMinute.setText(minute);
        }
        viewHolder.tvDistance.setText(
                dfDis.format(model.getStepCount() * DIS) + "");

        viewHolder.tvCalories.setText(
                (int) (model.getStepCount() * CALORIES) + "");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    class ViewHolder {

        DateTextView dateTextView;
        CircleView circleView;
        TextView tvDistance;
        TextView tvCalories;
        TextView tvSportHour;
        TextView tvSportMinute;
        TextView tvHour;
        TextView tvMinute;
        ImageButton ibtToday;
    }
}

