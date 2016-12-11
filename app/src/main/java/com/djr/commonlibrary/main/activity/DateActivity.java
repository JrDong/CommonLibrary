package com.djr.commonlibrary.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.djr.commonlibrary.R;
import com.djr.commonlibrary.utils.T;
import com.djr.commonlibrary.view.date.WeekView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DateActivity extends AppCompatActivity {
    @Bind(R.id.weekView)
    WeekView weekView;

    public static void startActivity(Activity activity) {
        Intent i = new Intent();
        i.setClass(activity, DateActivity.class);
        activity.startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        ButterKnife.bind(this);

        weekView.setOnWeekClickListener(new WeekView.onWeekClickListener() {
            @Override
            public void onWeekClick(int week) {
                T.show(week + "");
            }
        });
    }
}
