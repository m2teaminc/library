package com.m2team.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.m2team.library.BuildConfig;

import java.util.UUID;

public class DeviceUtils {

    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // ????
        deviceId.append("a");
        try {
            //wifi mac??
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if(!TextUtils.isEmpty(wifiMac)){
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                if (BuildConfig.DEBUG) Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            //IMEI?imei?
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if(!TextUtils.isEmpty(imei)){
                deviceId.append("imei");
                deviceId.append(imei);
                if (BuildConfig.DEBUG) Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            //????sn?
            String sn = tm.getSimSerialNumber();
            if(!TextUtils.isEmpty(sn)){
                deviceId.append("sn");
                deviceId.append(sn);
                if (BuildConfig.DEBUG) Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            //???????? ?????id????
            String uuid = getUUID(context);
            if(!TextUtils.isEmpty(uuid)){
                deviceId.append("id");
                deviceId.append(uuid);
                if (BuildConfig.DEBUG) Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(UUID.randomUUID().toString());
        }
        if (BuildConfig.DEBUG) Log.e("getDeviceId : ", deviceId.toString());
        return deviceId.toString();
    }
    public static String getUUID(Context context){
        String uuid = null;
        SharedPreferences mShare = PreferenceManager.getDefaultSharedPreferences(context);//, "sysCacheMap");
        if(mShare != null){
            uuid = mShare.getString("uuid", "");
        }
        if(TextUtils.isEmpty(uuid)){
            uuid = UUID.randomUUID().toString();
            if (mShare != null) {
                mShare.edit().putString("uuid", uuid).commit();
            }
        }
        if (BuildConfig.DEBUG) Log.d("DeviceUtils", "getUUID : " + uuid);
        return uuid;
    }



}