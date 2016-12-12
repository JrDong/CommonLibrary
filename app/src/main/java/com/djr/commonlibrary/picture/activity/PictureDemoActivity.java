package com.djr.commonlibrary.picture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.djr.commonlibrary.picture.adapter.PictureAdapter;
import com.djr.commonlibrary.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PictureDemoActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_picture)
    RecyclerView rvPicture;
    @Bind(R.id.activity_picture_demo)
    LinearLayout llRoot;

    PopupWindow mPW;

    private static final int TAKE_PHOTO_REQUEST_CODE = 1;

    private static final int ALBUM_REQUEST_CODE = 2;

    private List<Bitmap> mList = new ArrayList<>();

    private PictureAdapter mAdapter;

    private String FILE_PATH = "temp.jpg";

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
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPW.setFocusable(true);
            mPW.setTouchable(true);
            //点击空白消失
            mPW.setBackgroundDrawable(new BitmapDrawable());
            mPW.setAnimationStyle(R.style.anim_menu_bottombar);
        }

        mPW.showAtLocation(llRoot, Gravity.BOTTOM, 0, 0);
        mPW.update();

    }

    private void hidePopup() {
        if (mPW != null) {
            mPW.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takephoto:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = Uri.fromFile(FileUtils.getFile(FILE_PATH));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
                hidePopup();
                break;
            case R.id.album:
                Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                albumIntent.setType("image/*");
                startActivityForResult(albumIntent, ALBUM_REQUEST_CODE);
                hidePopup();
                break;
            case R.id.cancel:
                hidePopup();
                break;
            default:
                break;
        }
    }

    private void addBitmap(Bitmap bitmap) {
        mList.clear();
        mList.add(bitmap);
        if (mAdapter == null) {
            mAdapter = new PictureAdapter(this, mList);
            rvPicture.setLayoutManager(new GridLayoutManager(this, 3));
            rvPicture.setAdapter(mAdapter);
        } else {
            mAdapter.setData(mList);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("dong", "系统相机拍照完成，resultCode=" + resultCode);
        switch (requestCode) {
            case TAKE_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bitmap bmp = BitmapFactory.decodeFile(FileUtils.getFile(FILE_PATH).getPath());
                    addBitmap(bmp);
                }
                break;
            case ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {



                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
