package com.djr.commonlibrary.picture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.djr.commonlibrary.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PictureDemoActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_picture)
    RecyclerView rvPicture;

    PopupWindow mPW;
    @Bind(R.id.activity_picture_demo)
    LinearLayout llRoot;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PictureDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_demo);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_picture) {
                    showPopup();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    private void showPopup() {
        if (mPW == null) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.popupwinow_select_picture, null);
            TextView takePhoto = (TextView) view.findViewById(R.id.takephoto);
            TextView fromAlbum = (TextView) view.findViewById(R.id.album);
            TextView cancel = (TextView) view.findViewById(R.id.cancel);

            takePhoto.setOnClickListener(this);
            fromAlbum.setOnClickListener(this);
            cancel.setOnClickListener(this);

            mPW = new PopupWindow(view,
                    ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            mPW.setFocusable(true);
            mPW.setTouchable(true);
            //点击空白消失
            mPW.setBackgroundDrawable(new BitmapDrawable());
            mPW.setAnimationStyle(R.style.anim_menu_bottombar);
        }

        mPW.showAtLocation(llRoot, Gravity.BOTTOM,0,0);
        mPW.update();

    }

    private void hidePopup(){
        if (mPW != null){
            mPW.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.takephoto:

                break;
            case R.id.album:

                break;
            case R.id.cancel:
                hidePopup();
                break;
            default:
                break;
        }
    }
}
