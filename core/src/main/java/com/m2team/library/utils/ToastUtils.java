package com.m2team.library.utils;

import android.content.Context;

import com.sdsmdg.tastytoast.TastyToast;

public class ToastUtils {

    public static void showToast(Context context, int resId) {
        TastyToast.makeText(context, context.getString(resId), TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
    }

    public static void showToast(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
    }

    public static void showToastSuccess(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
    }

    public static void showToastSuccess(Context context, int resId) {
        TastyToast.makeText(context, context.getString(resId), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
    }

    public static void showToastError(Context context, int resId) {
        TastyToast.makeText(context, context.getString(resId), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
    }

    public static void showToastError(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
    }
}
