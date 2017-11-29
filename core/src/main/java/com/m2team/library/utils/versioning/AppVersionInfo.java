package com.m2team.library.utils.versioning;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.m2team.library.utils.Log;


public class AppVersionInfo {

    public final int versionCode;
    public final String versionName;

    AppVersionInfo(final Context context) {
        int versionCode = 0;
        String versionName = null;
        try {
            final String packageName = context.getPackageName();
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(e);
        }
        this.versionCode = versionCode;
        this.versionName = versionName;
    }
}