package com.m2team.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import java.util.Map;
import java.util.logging.Logger;

import timber.log.Timber;

public class SharedPreferenceUtil {
    public static final String TAG = SharedPreferences.class.getSimpleName();

    public static void putLong(Context context, String fileName, String key, long b) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, b);

        try {
            editor.apply();
        } catch (Exception e) {
            Timber.e(TAG, "putLong(\t" + key + "\t,\t" + b + " \t)\tFail");
            e.printStackTrace();
        }
    }

    public static void putBoolean(Context context, String fileName, String key, boolean b) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, b);

        try {
            editor.apply();
        } catch (Exception e) {
            Timber.e(TAG, "putBoolean(\t" + key + "\t,\t" + b + " \t)\tFail");
            e.printStackTrace();
        }
    }

    public static void putString(Context context, String fileName, String key, String string) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, string);

        try {
            editor.apply();
        } catch (Exception e) {
            Timber.e(TAG, "putString(\t" + key + "\t,\t" + string + " \t)\tFail");
            e.printStackTrace();
        }
    }

    public static void putInt(Context context, String fileName, String key, int value) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);

        try {
            editor.apply();
        } catch (Exception e) {
            Timber.e(TAG, "putInt(\t" + key + "\t,\t" + value + " \t)\tFail");
            e.printStackTrace();
        }
    }

    public static void removeString(Context context, String fileName, String key) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);

        try {
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "remove(\t" + key + "\t)\tFail");
            e.printStackTrace();
        }
    }

    public static boolean getBoolean(Context context, String fileName, String key) {
        return getBoolean(context, fileName, key, false);
    }

    public static boolean getBoolean(Context context, String fileName, String key, boolean defValue) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }

        return prefs.getBoolean(key, defValue);
    }

    public static long getLong(Context context, String fileName, String key) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }

        return prefs.getLong(key, 0);
    }

    public static String getString(Context context, String fileName, String key, String value) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return prefs.getString(key, value);
    }

    public static int getInt(Context context, String fileName, String key) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return prefs.getInt(key, -1);
    }

    public static int getInt(Context context, String fileName, String key, int value) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return prefs.getInt(key, value);
    }

    public static final Map<String, ?> getAll(Context context, String fileName) {
        SharedPreferences prefs;
        if (TextUtils.isEmpty(fileName)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return prefs.getAll();
    }
}