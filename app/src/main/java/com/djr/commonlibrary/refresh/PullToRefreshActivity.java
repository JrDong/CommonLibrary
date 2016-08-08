package com.djr.commonlibrary.refresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.djr.commonlibrary.R;
import com.djr.commonlibrary.refresh.view.PullToRefreshView;

public class PullToRefreshActivity extends AppCompatActivity {

    private static final int REFRESH_DELAY = 4000;
    private PullToRefreshView mRefresh;

    public static void startActivity(Activity activity) {
        Intent i = new Intent();
        i.setClass(activity, PullToRefreshActivity.class);
        activity.startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);
        mRefresh = (PullToRefreshView) findViewById(R.id.refresh_view);
        mRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefresh.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }
}
