package com.djr.commonlibrary.utils;

import android.util.Log;

import com.djr.commonlibrary.BuildConfig;


/**
 * Log管理工具类
 */
public class L {

    private static final String TAG = "logTag";
    public static boolean isDebug = BuildConfig.DEBUG;

    private L() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void i(int msg) {
        if (isDebug)
            Log.i(TAG, msg + "");
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void d(int msg) {
        if (isDebug)
            Log.d(TAG, msg + "");
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }
}
