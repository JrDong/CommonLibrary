package com.djr.commonlibrary.utils;

/**
 * Created by dongbo on 2015/11/16.
 */
public class TimeUtils {
    public static String getTime(){
        Long tsLong = System.currentTimeMillis();
        String s = tsLong+"";
        return s;
    }

    /**
     * 返回的日期的格式   年/月/日
     * @param time 时间戳
     * @return
     */

    public static String getDate_Y_M_D(long time) {

        String date = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date(time));
        return date;
    }

}
