package com.m2team.library.utils;

import android.content.Context;
import android.content.res.Resources;

public class Initializer {

    private static Context sAppContext;
    private static String sHostToPing;

    public static void init(final Context context){
        sAppContext = context.getApplicationContext();
    }

    public static void init(final Context context, final String _hostToPing){
        sAppContext = context.getApplicationContext();
        sHostToPing = _hostToPing;
    }

    public static Context getsAppContext(){
        if (sAppContext==null)
            Log.e("You should initialize Initializer class with Context first. Otherwise NPE will be thrown");
        return sAppContext;
    }

    public static String getsHostToPing(){
        return sHostToPing;
    }

    public static Resources getResources() {
        return sAppContext.getResources();
    }
}
