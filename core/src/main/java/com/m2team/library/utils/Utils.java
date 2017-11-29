package com.m2team.library.utils;

import android.content.Context;

public class Utils {

    private static  Context mContext;

    public static void initialize(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
}
