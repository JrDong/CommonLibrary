package com.djr.commonlibrary.step.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.djr.commonlibrary.CLApplication;
import com.djr.commonlibrary.step.StepService;

public class TimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {

        Intent i = new Intent();
        i.setClass(CLApplication.getAppContext(), StepService.class);
        CLApplication.getAppContext().startService(i);
        //PollingUtils.getIns().startPollingService(context, 5, StepService.class, StepService.ACTION);

    }
}
