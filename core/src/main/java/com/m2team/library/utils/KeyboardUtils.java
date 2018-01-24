package com.m2team.library.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {

    public static void openSoftInput(View et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) return;
        inputMethodManager.showSoftInput(et, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftInput(View et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) return;
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
