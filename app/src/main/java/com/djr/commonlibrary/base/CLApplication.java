package com.djr.commonlibrary.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.djr.commonlibrary.step.StepService;

/**
 * Created by DongJr on 2016/8/5.
 */
public class CLApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        startService(new Intent(this, StepService.class));

    }

    public static Context getAppContext() {
        return mContext;
    }


}
