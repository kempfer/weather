package com.example.zotov.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.zotov.weather.http.openweathermap.Api;
import com.example.zotov.weather.http.openweathermap.Weather;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;

    TextView textLocation;

    TextView textTemperature;

    TextView textHumidity;

    TextView textPressure;



    private static final String[] INITIAL_PERMS={
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

        textLocation = (TextView) findViewById(R.id.location);
        textTemperature = (TextView) findViewById(R.id.temperature);
        textHumidity = (TextView) findViewById(R.id.humidity);
        textPressure = (TextView) findViewById(R.id.pressure);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);





        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(canAccessLocation()) {
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                    locationListener);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(canAccessLocation()) {
            locationManager.removeUpdates(locationListener);
        }

    }

    private void showLocation(Location location) {
        textLocation.setText(formatLocation(location));

        findTemp(location);

        locationManager.removeUpdates(locationListener);
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f",
                location.getLatitude(), location.getLongitude());
    }


    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case LOCATION_REQUEST:
                if (canAccessLocation()) {
                    doLocationThing();
                }
                else {
                    bzzzt();
                }
                break;
        }
    }

    private void bzzzt() {
        Toast.makeText(this, "Bzzzz", Toast.LENGTH_LONG).show();
    }


    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    private void doLocationThing() {
        Toast.makeText(this, "Location", Toast.LENGTH_SHORT).show();
    }


    private void findTemp(Location location) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("lat", Double.toString(location.getLatitude()));
        parameters.put("lon",  Double.toString(location.getLongitude()));
        parameters.put("appid", "b644c4a94e897f47d46d2666d9c42a47");


        Call <Weather> call =  api.getToDay(parameters);

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    String str = "Temperature: " +  Double.toString(response.body().getMain().getTempC());
                    textTemperature.setText(str);
                    textHumidity.setText("Humidity:" + response.body().getMain().getHumidity());
                    textPressure.setText("Pressure:" + response.body().getMain().getPressure());
                } else {
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });
    }
}
