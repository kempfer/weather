package com.z_project.weather.ui.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.z_project.weather.utils.CheckPermission;


public class BaseActivity extends AppCompatActivity {

    protected CheckPermission checkPermission;

    public BaseActivity() {
        super();
        checkPermission = new CheckPermission(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission.checkPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (checkPermission.canAccessLocation()) {

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        checkPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
