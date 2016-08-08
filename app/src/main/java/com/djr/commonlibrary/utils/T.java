package com.djr.commonlibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast管理工具类
 */
public class T {

    private static Toast toast = null;
    private T() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showShort(Context context, CharSequence message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showShort(Context context, int message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showLong(Context context, CharSequence message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showLong(Context context, int message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void show(Context context, CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, message, duration);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
    
    public static void show(Context context, int message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, message, duration);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}
