package com.djr.commonlibrary.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.djr.commonlibrary.R;
import com.djr.commonlibrary.view.hexagon.HexagonCheckBox;
import com.djr.commonlibrary.view.hexagon.HexagonView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HexagonActivity extends AppCompatActivity {

    @Bind(R.id.hexagon1)
    HexagonView hexagon1;
    @Bind(R.id.hexagon2)
    HexagonView hexagon2;
    @Bind(R.id.hexagon3)
    HexagonView hexagon3;
    @Bind(R.id.HexagonCheckBox1)
    HexagonCheckBox HexagonCheckBox1;
    @Bind(R.id.HexagonCheckBox2)
    HexagonCheckBox HexagonCheckBox2;
    @Bind(R.id.HexagonCheckBox3)
    HexagonCheckBox HexagonCheckBox3;

    public static void startActivity(Activity activity) {
        Intent i = new Intent();
        i.setClass(activity, HexagonActivity.class);
        activity.startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexagon);
        ButterKnife.bind(this);

        hexagon1.setGrade(HexagonView.GRADE_A, HexagonView.GRADE_HIGH);
        HexagonCheckBox1.setGrade(HexagonView.GRADE_A, HexagonView.GRADE_HIGH);
        hexagon2.setGrade(HexagonView.GRADE_B, HexagonView.GRADE_NORMAL);
        HexagonCheckBox2.setGrade(HexagonView.GRADE_B, HexagonView.GRADE_NORMAL);
        hexagon3.setGrade(HexagonView.GRADE_D, HexagonView.GRADE_LOW);
        HexagonCheckBox3.setGrade(HexagonView.GRADE_C, HexagonView.GRADE_LOW);

    }
}
