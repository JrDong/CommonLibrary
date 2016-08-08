package com.djr.commonlibrary.step;

import android.util.Log;

import com.djr.commonlibrary.step.data.StepDBManager;
import com.djr.commonlibrary.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 加速传感器和计步传感器 获得的计步数都放在这个类，通过get  set进行操作
 */
public class StepCountManager {

    private static StepCountManager mManager;

    private volatile int StepCountFromSensorAcc;//加速传感器得到的计步数据

    private volatile int StepCountFromSensorStep;//计步传感器得到的计步数据

    private volatile int StepCountFromDB = 0;//从数据库中读取出的数据
    private volatile int recordtepCountFromSensorStep = 0;//从传感器读取出的数据

    private String currentDate = "";

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private long currentTime = 0;

    private static final String TAG = "StepCounterManager";


    private StepCountManager() {

    }

    public static StepCountManager getIns() {
        if (mManager == null) {
            mManager = new StepCountManager();
        }

        return mManager;
    }

    public int getStepCountFromSensorAccInMemory() {
        return StepCountFromSensorAcc;
    }

    public void setStepCountFromSensorAcc(final int stepCountFromSensorAcc) {
        StepCountFromSensorAcc = stepCountFromSensorAcc;
    }

    public int getStepCountFromSensorStepInMemory() {
        return StepCountFromSensorStep;
    }

    public void setStepCountFromSensorStep(final int stepCountFromSensorStep) {

        recordtepCountFromSensorStep = stepCountFromSensorStep;

        int ignoreCount = (int) SPUtils.get(StepConstants.restartCount, 0);
        Log.d("StepCounterManager", " 锚点值 " + ignoreCount);

        //如果计步传感器返回的值，小于数据库中存储的值，说明手机重启了，需要把这两个值相加
        if (stepCountFromSensorStep < StepCountFromDB) {

            //如果sp中的锚点大于传感器返回的值，说明这个锚点是在重启手机之前写入的，没有意义，重置
            if (ignoreCount > stepCountFromSensorStep) {
                SPUtils.put(StepConstants.restartCount, 0);
                ignoreCount = 0;
            }


            //sp中存储的锚点值如果大于0.需要做差值，然后在相加，过滤掉未登录状态时，记录下来的那些数据
            if (ignoreCount > 0) {
                StepCountFromSensorStep = StepCountFromDB + stepCountFromSensorStep - ignoreCount;
            } else {
                StepCountFromSensorStep = StepCountFromDB + stepCountFromSensorStep;
            }

        } else {//手机没有重启
            //sp中存储的锚点值如果大于0.需要做差值，然后在相加，过滤掉未登录状态时，记录下来的那些数据
            if (ignoreCount > 0) {
                StepCountFromSensorStep = StepCountFromDB + stepCountFromSensorStep - ignoreCount;
            } else {
                StepCountFromSensorStep = stepCountFromSensorStep;
            }
        }

    }

    //将内存中的sensorStep  传感器返回的值写入sp中，下次步数只记录增加的部分，而不是从头计算，避免重复的取值
    public void writeSensorStepToSp() {
        SPUtils.put(StepConstants.restartCount, recordtepCountFromSensorStep);
        StepCountFromDB = getStepCount();

    }

    //从数据库中读取的数据
    public void setStepCountFromDB(int count) {
        StepCountFromSensorAcc = count;
        StepCountFromSensorStep = count;
        StepCountFromDB = count;
    }


    public void clearStepCount() {
        StepCountFromSensorStep = 0;
        StepCountFromSensorAcc = 0;
        SPUtils.put(StepConstants.restartCount, getStepCountFromSensorStepInMemory());
    }


    public void addStepCountForSensorAcc() {


        //如果手机日期发生变更，清空内存中的数据，重新计步
        if (!currentDate.equals("")) {
            if (!currentDate.equals(format.format(new Date(System.currentTimeMillis())))) {
                this.StepCountFromSensorAcc = 0;
                this.StepCountFromSensorStep = 0;
                this.StepCountFromDB = 0;
                SPUtils.put(StepConstants.restartCount, recordtepCountFromSensorStep);
                currentDate = format.format(new Date(System.currentTimeMillis()));
            }
        } else {
            currentDate = format.format(new Date(System.currentTimeMillis()));
        }

        //阀值比较灵敏,所以再加一个时间的判断,如果时间差距大于一秒,认为是走了一步
        if (currentTime != 0) {
            if (Math.abs(System.currentTimeMillis() - currentTime) >= 500L) {
                Log.e(TAG, "============add==========");
                //Vibrator vibrator = (Vibrator) CLApplication.getAppContext()
                // .getSystemService(CLApplication.getAppContext().VIBRATOR_SERVICE);
                //                        vibrator.vibrate(200);

                this.StepCountFromSensorAcc = StepCountFromSensorAcc + 1;
                currentTime = System.currentTimeMillis();
            }
        } else {
            currentTime = System.currentTimeMillis();
        }
    }


    //两个感应器，哪一个获取的值大 就用哪一个的值
    public int getStepCount() {
        if (StepCountFromSensorAcc == 0 && StepCountFromSensorStep == 0) {
            StepDBManager.getIns()
                    .getStepCountFromDB();
        }

        if (StepCountFromSensorAcc > StepCountFromSensorStep) {
            Log.d(TAG, "acc" + "   StepCountFromSensorAcc " + StepCountFromSensorAcc + "   StepCountFromSensorStep "
                    + StepCountFromSensorStep);
            return StepCountFromSensorAcc;
        }

        if (StepCountFromSensorAcc < StepCountFromSensorStep) {
            Log.d(TAG, "stepSen" + "   StepCountFromSensorAcc " + StepCountFromSensorAcc + "   " +
                    "StepCountFromSensorStep " + StepCountFromSensorStep);
            return StepCountFromSensorStep;
        }

        return StepCountFromSensorAcc;
    }
}


























