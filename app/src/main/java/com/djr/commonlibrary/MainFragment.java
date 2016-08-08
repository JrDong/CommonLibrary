package com.djr.commonlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.djr.commonlibrary.base.adaper.CommonAdapter;
import com.djr.commonlibrary.base.adaper.ViewHolder;
import com.djr.commonlibrary.bezier.BezierActivity;
import com.djr.commonlibrary.refresh.PullToRefreshActivity;
import com.djr.commonlibrary.step.StepCountActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DongJr on 2016/8/5.
 */
public class MainFragment extends Fragment {

    private ListView mListView;
    private List<String> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mListView = new ListView(getContext());
        mListView.setLayoutParams(params);
        return mListView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mList.add("STEP");
        mList.add("PULL-TO-REFRESH");
        mList.add("BÉZIER");

        MainAdapter adapter = new MainAdapter(getContext(), mList, R.layout.main_item);
        mListView.setAdapter(adapter);
    }

    class MainAdapter extends CommonAdapter<String> {

        public MainAdapter(Context context, List datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(final ViewHolder holder, String s) {
            holder.setText(R.id.tv_item, s);
            holder.setOnClickListener(R.id.tv_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getPosition();
                    switch (position) {
                        case 0:
                            StepCountActivity.startActivity(getActivity());
                            break;
                        case 1:
                            PullToRefreshActivity.startActivity(getActivity());
                            break;
                        case 2:
                            BezierActivity.startActivity(getActivity());
                            break;
                    }
                }
            });
        }
    }
}
