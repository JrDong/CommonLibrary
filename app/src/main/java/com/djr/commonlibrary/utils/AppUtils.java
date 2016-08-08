package com.djr.commonlibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.djr.commonlibrary.CLApplication;

import java.util.ArrayList;
import java.util.List;


/**
 * App相关辅助类
 */
public class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkLocationPerssion( ){

        //XYTrainApplication.getContext().checkSelfPermission(perssionName);
        PackageManager pm = CLApplication.getAppContext().getPackageManager();
        boolean permission_fine = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,CLApplication.getAppContext().getPackageName()));
        boolean permission_coarse = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,CLApplication.getAppContext().getPackageName()));
        int a = ActivityCompat.checkSelfPermission(CLApplication.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission_coarse ||permission_fine) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isPackageInstalled(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 隐藏输入法窗口
     */
    public static void hideInputWindow(Activity  activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 显示输入法窗口
     */
    public static void showInputWindow(Activity  activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(activity.getCurrentFocus(), 0); ; //强制隐藏键盘
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param view
     * @param motionEvent
     * @return
     */
    public static boolean isShouldHideInput(View view, MotionEvent motionEvent) {
        if (view != null && (view instanceof EditText)) {
            int[] l = { 0, 0 };
            view.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + view.getHeight(), right = left
                    + view.getWidth();
            if (motionEvent.getX() > left && motionEvent.getX() < right
                    && motionEvent.getY() > top && motionEvent.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }
}
