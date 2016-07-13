package com.z_project.weather.ui;

import android.support.v4.app.Fragment;


public class PlaceSearchActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PlaceSearchFragment.newInstance();
    }
}
