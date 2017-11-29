package com.m2team.library.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.m2team.library.R;

import java.util.ArrayList;

public class DialogUtils {

    public static AlertDialog showDialog(Context context, String title, ArrayList<String> list, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(true)
                .setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list), onClickListener);

        return builder.show();
    }

    public static AlertDialog showDialog(Context context, String title, String msg, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, onClickListener)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setMessage(msg);

        return builder.show();
    }

    public static AlertDialog showMessageDialog(Context context, String title, String message) {
        AlertDialog ad = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .create();
        ad.show();
        return ad;
    }

    public static ProgressDialog showProgressDialog(Context context, String caption) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
        return mProgressDialog;
    }
}
