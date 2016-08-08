package com.djr.commonlibrary.utils;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by dongbo on 2015/11/11.
 */
public class ColorUtils {
    public static boolean isColor(String color){
        Pattern pattern = Pattern.compile("#" + "[0-f]" + "[0-f]" + "[0-f]" + "[0-f]" + "[0-f]" + "[0-f]");
        Matcher m = pattern.matcher(color);

        return m.matches();
    }
    public static String stringFilter(String str)throws PatternSyntaxException {
        // 只允许字母和数字
        String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }


    // 成新的颜色值
    public static int getNewColorByStartEndColor(Context context, float fraction, int startValue, int endValue) {
        return evaluate(fraction, context.getResources().getColor(startValue), context.getResources().getColor(endValue));
    }
    /**
     * 成新的颜色值
     * @param fraction 颜色取值的级别 (0.0f ~ 1.0f)
     * @param startValue 开始显示的颜色
     * @param endValue 结束显示的颜色
     * @return 返回生成新的颜色值
     */
    public static int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }
}
