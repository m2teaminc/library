package com.m2team.library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;

@SuppressLint("SdCardPath")
public class CleanUtils {

    public static void cleanInternalCache(Context context) {
        FileManager.deleteFilesByDirectory(context.getCacheDir());
    }


    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileManager.deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }


    public static void cleanDatabases(Context context) {
        FileManager.deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }


    public static void cleanSharedPreference(Context context) {
        FileManager.deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }


    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }


    public static void cleanFiles(Context context) {
        FileManager.deleteFilesByDirectory(context.getFilesDir());
    }


    public static int cleanApplicationData(Context context) {
        //???????????
        cleanInternalCache(context);
        //???????????
        cleanExternalCache(context);
        //?????SharedPreference
        cleanSharedPreference(context);
        //?????files??
        cleanFiles(context);
        return 1;
    }


    public static String getAppClearSize(Context context) {
        long clearSize = 0;
        String fileSizeStr = "";
        DecimalFormat df = new DecimalFormat("0.00");
        //??????????
        clearSize = FileManager.getFileSize(context.getCacheDir());
        //????SharedPreference??????
        clearSize += FileManager.getFileSize(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
        //????data/data/com.xxx.xxx/files????????
        clearSize += FileManager.getFileSize(context.getFilesDir());
        //??????????
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            clearSize += FileManager.getFileSize(context.getExternalCacheDir());
        }
        if (clearSize > 5000) {
            //??????Byte?MB
            fileSizeStr = df.format((double) clearSize / 1048576) + "MB";
        }
        return fileSizeStr;
    }


}
