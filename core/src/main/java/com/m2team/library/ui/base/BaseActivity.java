package com.m2team.library.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.m2team.library.R;

public class BaseActivity extends AppCompatActivity {

    private boolean isPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (isPress) {
            finish();
            super.onBackPressed();
            return;
        }
        isPress = true;

        Toast.makeText(this, R.string.back_again_to_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> isPress = false, 3000);
    }
}
