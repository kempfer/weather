package com.z_project.weather.ui;

import android.support.v4.app.Fragment;


public class PlaceListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PlaceListFragment.newInstance();
    }

}
