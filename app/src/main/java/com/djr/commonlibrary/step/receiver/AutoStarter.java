package com.djr.commonlibrary.step.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.djr.commonlibrary.base.CLApplication;
import com.djr.commonlibrary.step.StepService;


public class AutoStarter extends BroadcastReceiver {

    private static final String TAG = "AutoStarterLog";
    SharedPreferences prefs;
    private static final String ACTIVITY_PREF_KEY = "auto_start_activity_version";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "@onReceive");
        Intent i= new Intent();
        i.setClass(CLApplication.getAppContext(),StepService.class);
        CLApplication.getAppContext().startService(i);

        //PollingUtils.getIns().startPollingService(CLApplication.getAppContext(), 5, StepService.class, StepService.ACTION);




    }


}