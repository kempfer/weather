package com.z_project.weather;

import android.support.v4.app.Fragment;

public class WeatherActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return WeatherFragment.newInstance();
    }
}
