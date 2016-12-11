package com.djr.commonlibrary.step.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.djr.commonlibrary.base.CLApplication;
import com.djr.commonlibrary.step.StepService;


public class SystemReceiver extends BroadcastReceiver {

    private static final String TAG = "StepService";
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG,"SystemReceiver   "+intent.getAction());
        Intent i= new Intent();
        i.setClass(CLApplication.getAppContext(),StepService.class);
        CLApplication.getAppContext().startService(i);
        //PollingUtils.getIns().startPollingService(context, 5, StepService.class, StepService.ACTION);

    }
}
