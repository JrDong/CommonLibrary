package com.djr.commonlibrary.utils;

import android.widget.Toast;

import com.djr.commonlibrary.CLApplication;

/**
 * Toast管理工具类
 */
public class T {

    private static Toast toast = null;

    private T() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void show(CharSequence message) {
        if (toast == null) {
            toast = Toast.makeText(CLApplication.getAppContext(), message, Toast.LENGTH_SHORT);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void show(int message) {
        if (toast == null) {
            toast = Toast.makeText(CLApplication.getAppContext(), message, Toast.LENGTH_SHORT);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showLong(CharSequence message) {
        if (toast == null) {
            toast = Toast.makeText(CLApplication.getAppContext(), message, Toast.LENGTH_LONG);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showLong(int message) {
        if (toast == null) {
            toast = Toast.makeText(CLApplication.getAppContext(), message, Toast.LENGTH_LONG);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void show(CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(CLApplication.getAppContext(), message, duration);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void show(int message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(CLApplication.getAppContext(), message, duration);
//			toast.setGravity(Gravity.CENTER, 0, 62);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}
