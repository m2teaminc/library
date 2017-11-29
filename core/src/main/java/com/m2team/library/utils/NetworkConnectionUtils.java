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

    public static final int TYPE_NO = 0;

    public static final int TYPE_MOBILE_CMNET = 1;

    public static final int TYPE_MOBILE_CMWAP = 2;

    public static final int TYPE_MOBILE_UNIWAP = 3;

    public static final int TYPE_MOBILE_3GWAP = 4;

    public static final int TYPE_MOBLIE_3GNET = 5;

    public static final int TYPE_MOBILE_UNINET = 6;

    public static final int TYPE_MOBILE_CTWAP = 7;

    public static final int TYPE_MOBILE_CTNET = 8;

    //WIFI??
    public static final int TYPE_WIFI = 10;

    public static int getNetworkState(Context context) {
        //??ConnectivityManager??
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //????????
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            //??????
            int currentNetWork = networkInfo.getType();
            //??????
            if (currentNetWork == ConnectivityManager.TYPE_MOBILE) {
                if(networkInfo.getExtraInfo() != null){
                    if (networkInfo.getExtraInfo().equals("cmnet")) {
                        AppLogMessageMgr.i("AppNetworkMgr", "?????????CMNET??");
                        return TYPE_MOBILE_CMNET;
                    }
                    if (networkInfo.getExtraInfo().equals("cmwap")) {
                        AppLogMessageMgr.i("AppNetworkMgr", "?????????CMWAP??");
                        return TYPE_MOBILE_CMWAP;
                    }
                    if(networkInfo.getExtraInfo().equals("uniwap")) {
                        AppLogMessageMgr.i("AppNetworkMgr", "?????????UNIWAP??");
                        return TYPE_MOBILE_UNIWAP;
                    }
                    if(networkInfo.getExtraInfo().equals("3gwap")) {
                        AppLogMessageMgr.i("AppNetworkMgr", "?????????3GWAP??");
                        return TYPE_MOBILE_3GWAP;
                    }
                    if(networkInfo.getExtraInfo().equals("3gnet")) {
                        AppLogMessageMgr.i("AppNetworkMgr", "?????????3GNET??");
                        return TYPE_MOBLIE_3GNET;
                    }
                    if(networkInfo.getExtraInfo().equals("uninet")) {
                        AppLogMessageMgr.i("AppNetworkMgr", "?????????UNINET??");
                        return TYPE_MOBILE_UNINET;
                    }
                    if(networkInfo.getExtraInfo().equals("ctwap")) {
                        AppLogMessageMgr.i("AppNetworkMgr", "?????????CTWAP??");
                        return TYPE_MOBILE_UNINET;
                    }
                    if(networkInfo.getExtraInfo().equals("ctnet")) {
                        AppLogMessageMgr.i("AppNetworkMgr", "?????????CTNET??");
                        return TYPE_MOBILE_UNINET;
                    }
                }
                //WIFI????
            }else if (currentNetWork == ConnectivityManager.TYPE_WIFI) {
                AppLogMessageMgr.i("AppNetworkMgr", "?????WIFI??");
                return TYPE_WIFI;
            }
        }
        AppLogMessageMgr.i("AppNetworkMgr-->>NetworkUtils", "??????????????");
        return TYPE_NO;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            //??????
            if (mNetworkInfo != null) {
                //???TYPE_MOBILE??
                if(ConnectivityManager.TYPE_MOBILE == mNetworkInfo.getType()){
                    AppLogMessageMgr.i("AppNetworkMgr", "????????TYPE_MOBILE");
                    //??????????
                    NetworkInfo.State STATE_MOBILE = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                    if (STATE_MOBILE == NetworkInfo.State.CONNECTED) {
                        AppLogMessageMgr.i("AppNetworkMgrd", "????????TYPE_MOBILE, ??????CONNECTED???");
                        return mNetworkInfo.isAvailable();
                    }
                }
                //???TYPE_WIFI??
                if(ConnectivityManager.TYPE_WIFI == mNetworkInfo.getType()){
                    AppLogMessageMgr.i("AppNetworkMgr", "????????TYPE_WIFI");
                    //??WIFI????
                    NetworkInfo.State STATE_WIFI = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                    if (STATE_WIFI == NetworkInfo.State.CONNECTED) {
                        AppLogMessageMgr.i("AppNetworkMgr", "????????TYPE_WIFI, ??????CONNECTED???");
                        return mNetworkInfo.isAvailable();
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNetworkConnected(Activity activity){
        boolean falg = false;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) activity.getApplicationContext().getSystemService("connectivity");
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
