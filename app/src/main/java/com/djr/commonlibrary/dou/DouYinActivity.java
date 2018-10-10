package com.djr.commonlibrary.dou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.djr.commonlibrary.R;


public class DouYinActivity extends AppCompatActivity {


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DouYinActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyin);
        final ShowLayout showLayout = (ShowLayout) findViewById(R.id.douyin);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showLayout.startAnimation();
            }
        }, 2000);

    }

}
