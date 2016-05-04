package com.example.zotov.weather;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.zotov.weather.http.openweathermap.Api;
import com.example.zotov.weather.http.openweathermap.Weather;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;

    TextView textLocation;

    TextView textTemperature;

    TextView textHumidity;

    TextView textPressure;

    TextView textTemperatureMain;

    TextView textWind;

    LinearLayout linearLayoutMain;

    LinearLayout linearLayoutDetails;

    ProgressBar progressBar;

    Button buttonRefresh;

    FloatingActionButton btnOpenAddPlace;

    Location currentLocation;

    Boolean clickRefresh = false;

    private  final int REQUEST_PLACE_PICKER = 12345;


    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int INITIAL_REQUEST = 1337;

    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        this.getSystemService(LOCATION_SERVICE);

        textLocation = (TextView) findViewById(R.id.location_value);
        textTemperature = (TextView) findViewById(R.id.temperature_value);
        textHumidity = (TextView) findViewById(R.id.humidity_value);
        textPressure = (TextView) findViewById(R.id.pressure_value);
        textTemperatureMain = (TextView) findViewById(R.id.temperature_main);
        textWind = (TextView) findViewById(R.id.wind_value);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        linearLayoutMain = (LinearLayout) findViewById(R.id.mail_liner_layout);
        linearLayoutDetails = (LinearLayout) findViewById(R.id.lin_layout_deteils);
        buttonRefresh = (Button) findViewById(R.id.button_refresh);

        btnOpenAddPlace = (FloatingActionButton) findViewById(R.id.btn_open_add_place);

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refresh();
                clickRefresh = true;
            }
        });
        btnOpenAddPlace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFindPlace(v);

            }
        });


        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
    }

    private void openFindPlace (View v) {
        try {

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (canAccessLocation()) {
            locationManager.removeUpdates(locationListener);
        }

    }


    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            findTemp(location);
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
            //showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (canAccessLocation()) {
                    doLocationThing();
                } else {
                    bzzzt();
                }
                break;
        }
    }

    private void bzzzt() {
        Toast.makeText(this, "Bzzzz", Toast.LENGTH_LONG).show();
    }


    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    private void doLocationThing() {
        Toast.makeText(this, "Location", Toast.LENGTH_SHORT).show();
    }

    private void refresh() {
        buttonRefresh.setEnabled(false);
        linearLayoutMain.setVisibility(View.GONE);
        linearLayoutDetails.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (currentLocation == null) {
            if (canAccessLocation()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000 * 5, 10, locationListener);
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 1000 * 5, 10,
                        locationListener);
            }
        } else {
            findTemp(currentLocation);
        }

    }


    private void findTemp(Location location) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("lat", Double.toString(location.getLatitude()));
        parameters.put("lon", Double.toString(location.getLongitude()));
        parameters.put("appid", "b644c4a94e897f47d46d2666d9c42a47");


        Call<Weather> call = api.getToDay(parameters);

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                buttonRefresh.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    String str = Integer.toString(response.body().getMain().getTempCelsius());
                    textTemperature.setText(str);
                    textTemperatureMain.setText(str);
                    textHumidity.setText(response.body().getMain().getHumidity());
                    textPressure.setText(response.body().getMain().getPressure());
                    textLocation.setText(response.body().getCity());
                    textWind.setText(Double.toString(response.body().getWind().getSpeed()));

                    linearLayoutMain.setVisibility(View.VISIBLE);
                    linearLayoutDetails.setVisibility(View.VISIBLE);
                    if (clickRefresh) {
                        Toast.makeText(getBaseContext(), "Данные успешно обновлены", Toast.LENGTH_LONG).show();
                    }
                    clickRefresh = false;

                } else {
                    // error response, no access to resource?
                }


            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                // something went completely south (like no internet connection)
                progressBar.setVisibility(View.GONE);
                linearLayoutMain.setVisibility(View.VISIBLE);
                linearLayoutDetails.setVisibility(View.VISIBLE);
                buttonRefresh.setEnabled(true);
                Toast.makeText(getBaseContext(), "Ошибка при попытки соединиться к серверу", Toast.LENGTH_LONG).show();
                Log.d("Error", t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

           LatLng latLng = place.getLatLng();

            Location location = new Location("Test");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            currentLocation = location;
            refresh();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
