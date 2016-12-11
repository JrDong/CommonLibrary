package com.djr.commonlibrary.view.bezier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.djr.commonlibrary.R;

public class BezierActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private MetaballView metaballView;
    private MetaballDebugView debugMetaballView;
    private SeekBar seekBar, seekBar2, seekBar3;
    private Button button, button2;

    public static void startActivity(Activity activity) {
        Intent i = new Intent();
        i.setClass(activity, BezierActivity.class);
        activity.startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_1);
        metaballView = (MetaballView) this.findViewById(R.id.metaball);
        debugMetaballView = (MetaballDebugView) findViewById(R.id.debug_metaball);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);

        mToolbar.setTitle("Bezier");
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bezier, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fill) {
            metaballView.setPaintMode(1);
            debugMetaballView.setPaintMode(1);
            return true;
        } else if (id == R.id.action_strock) {
            metaballView.setPaintMode(0);
            debugMetaballView.setPaintMode(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBar:
                debugMetaballView.setMaxDistance(progress);
                break;
            case R.id.seekBar2:
                debugMetaballView.setMv(progress / 100f);
                break;
            case R.id.seekBar3:
                debugMetaballView.setHandleLenRate(progress / 100f);
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivity(new Intent(this, SecondActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(this, ThirdActivity.class));
                break;
        }
    }
}
