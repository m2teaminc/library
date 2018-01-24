package com.m2team.library.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectionUtils {

    private NetworkConnectionUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isNetworkConnected(Context activity){
        boolean falg = false;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null){
            return falg;
        }
        NetworkInfo[] arrayOfNetworkInfo = mConnectivityManager.getAllNetworkInfo();
        if (arrayOfNetworkInfo != null){
            for (int j = 0; j < arrayOfNetworkInfo.length; j++){
                if (arrayOfNetworkInfo[j].getState() == NetworkInfo.State.CONNECTED){
                    falg = true;
                    break;
                }
            }
        }
        return falg;
    }

    public static void openNetSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    public static boolean is3gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null == cm) {
            return false;
        }

        NetworkInfo info = cm.getActiveNetworkInfo();
        if (null != info && info.isConnected()) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null == cm) {
            return false;
        }

        NetworkInfo info = cm.getActiveNetworkInfo();
        if (null != info) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;

    }

    public static void openSetting(Activity activity, int requestCode) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction(Intent.ACTION_VIEW);
        activity.startActivityForResult(intent, requestCode);
    }

}
