package com.djr.commonlibrary.picture.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.djr.commonlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DongJr on 2016/12/12.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureHolder> {

    private Context mContext;

    private List<Bitmap> mList = new ArrayList<>();

    public PictureAdapter(Context context , List<Bitmap> list) {
        mContext = context;
        mList.clear();
        mList.addAll(list);
    }

    public void setData(List<Bitmap> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PictureHolder(View.inflate(mContext, R.layout.item_picture_preview, null));
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
        holder.picture.setImageBitmap(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PictureHolder extends RecyclerView.ViewHolder {
        private ImageView picture;

        public PictureHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.iv_picture);
        }
    }
}
