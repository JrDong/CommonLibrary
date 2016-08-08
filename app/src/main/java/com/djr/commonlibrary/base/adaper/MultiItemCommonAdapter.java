package com.djr.commonlibrary.base.adaper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T>
{

	protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

	public MultiItemCommonAdapter(Context context, List<T> datas,
			MultiItemTypeSupport<T> multiItemTypeSupport)
	{
		super(context, datas, -1);
		mMultiItemTypeSupport = multiItemTypeSupport;
	}

	@Override
	public int getViewTypeCount()
	{
		if (mMultiItemTypeSupport != null)
			return mMultiItemTypeSupport.getViewTypeCount();
		return super.getViewTypeCount();
	}

	@Override
	public int getItemViewType(int position)
	{
		if (mMultiItemTypeSupport != null)
			return mMultiItemTypeSupport.getItemViewType(position,
					mDatas.get(position));
		return super.getItemViewType(position);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (mMultiItemTypeSupport == null)
			return super.getView(position, convertView, parent);
		
		int layoutId = mMultiItemTypeSupport.getLayoutId(position,
				getItem(position));
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				layoutId, position);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();
	}

}
