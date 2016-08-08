package com.djr.commonlibrary.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetReceiver extends BroadcastReceiver {

    /**
     * 枚举网络状态 0：没有网络 2:2g网络 6：3g网络 4：4g网络 1：wifi NET_UNKNOWN：未知网络
     */
    public static final String ISNETWORK="isnetwork";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context) > 0) {
            SPUtils.put(ISNETWORK, true);
        } else {
            SPUtils.put(ISNETWORK, false);
        }

    }

    /**
     * 判断当前是否网络连接
     *
     * @param context
     * @return 状态码
     */
    public int isConnected(Context context) {
        int stateCode = 0;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            switch (ni.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    stateCode = 1;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (ni.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            stateCode = 2;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            stateCode = 3;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            stateCode = 4;
                            break;
                        default:
                            stateCode = 5;
                    }
                    break;
                default:
            }

        }
        return stateCode;
    }

}
