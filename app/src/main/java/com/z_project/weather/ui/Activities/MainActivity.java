package com.z_project.weather.ui.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


import com.z_project.weather.Models.DBHelper;
import com.z_project.weather.Models.PlaceModel;
import com.z_project.weather.Models.Storage.PlaceStorage;
import com.z_project.weather.R;
import com.z_project.weather.ui.Fragments.PlaceFragment;
import com.z_project.weather.ui.Fragments.WeatherToDayFragment;
import com.z_project.weather.utils.GPSLocation;




public class MainActivity extends BaseActivity {


    protected ViewPager pager;

    protected PagerAdapter pagerAdapter;

    protected int PAGE_COUNT = 2;

    protected PlaceModel placeModel;

    protected GPSLocation gpsLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (ViewPager) findViewById(R.id.pager);

        placeModel = (new PlaceStorage(DBHelper.getInstance(this, 1))).findCurrent();
        if (placeModel == null) {
            placeModel = new PlaceModel(0, 47.8556673, 35.1053143, "Запорожье", "", "");
        }

        pagerAdapter = new PageFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        if(checkPermission.canAccessLocation()) {
            gpsLocation = new GPSLocation(this);
        }
        if(placeModel == null) {
            pager.setCurrentItem(1);
        }

    }

    private class PageFragmentPagerAdapter extends FragmentPagerAdapter {

        public PageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    WeatherToDayFragment weatherToDayFragment = WeatherToDayFragment.newInstance();
                    weatherToDayFragment.setPlace(placeModel);
                    return weatherToDayFragment;
                case 1:
                    boolean gpsEnabled = (gpsLocation != null && gpsLocation.checkServices());
                    return PlaceFragment.newInstance(gpsEnabled);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }

    public void toWeatherToDay(PlaceModel placeModel, boolean switchToday) {

        this.placeModel = placeModel;
        if (switchToday) {
            pager.setCurrentItem(0);
        }
        WeatherToDayFragment weatherToDayFragment = (WeatherToDayFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":0");
        weatherToDayFragment.setPlace(placeModel);
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onChangeLocation(PlaceModel placeModel) {
        if(placeModel !=null) {
            PlaceFragment placeFragment  = (PlaceFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":1" );
            placeFragment.setCurrentPlace(placeModel);
        }
    }

}
