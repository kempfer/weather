package com.z_project.weather.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;


public class CheckPermission {

    private AppCompatActivity activity;

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final int LOCATION_REQUEST = 1;

    public CheckPermission(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void checkPermission() {
        if (!canAccessLocation()) {
            if (Build.VERSION.SDK_INT > 22) {
                activity.requestPermissions(INITIAL_PERMS, LOCATION_REQUEST);
            }
        }
    }

    public boolean canAccessLocation() {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, perm));
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
