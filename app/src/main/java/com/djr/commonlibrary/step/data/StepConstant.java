package com.djr.commonlibrary.step.data;

/**
 * Created by yuwentao on 16/6/1.
 */
public class StepConstant {

    //1km=1500步=15分钟=35卡
    /**
     * 每步消耗的卡路里
     */
    public static float everyStepCalorie = 35f / 1500;
    /**
     * 每步的千米数
     */
    public static float everyStepKm = 1f / 1500;
    /**
     * 每步多少小时
     */
    public static float everyStepHour = 0.25f / 1500;

    /**
     * 每步多少分钟
     */
    public static double everyStepMinute = 0.01;
    /**
     * 每步多少米
     */
    public static float everyStepDistance = 0.667f;
    /**
     * 目标步数
     */
    public static int targetStep = 5000;


}
