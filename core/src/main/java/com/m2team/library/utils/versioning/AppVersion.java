package com.m2team.library.utils.versioning;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.m2team.library.utils.LogUtils;

public class AppVersion {


    public static int getVersionCode(final Context context) {
        int version = 0;
        try {
            final String packageName = context.getPackageName();
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            version = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(e.getMessage());
        }
        return version;
    }

    public static String getVersionName(final Context context) {
        String version = "";
        try {
            final String packageName = context.getPackageName();
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(e.getMessage());
        }
        return version;
    }

    public static AppVersionInfo getVersionInfo(final Context context) {
        return new AppVersionInfo(context);
    }

}
