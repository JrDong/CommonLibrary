package com.djr.commonlibrary.view.bezier;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.djr.commonlibrary.R;

public class ThirdActivity extends AppCompatActivity {
    HeartBezierView heartBezier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_3);
        heartBezier = (HeartBezierView) findViewById(R.id.bezier);
    }

    public void onClick(View view) {
        heartBezier.start();
    }
}
