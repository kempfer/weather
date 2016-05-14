package com.z_project.weather.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.z_project.weather.Models.PlaceModel;
import com.z_project.weather.ui.Activities.MainActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class GPSLocation  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private Context context;

    public GPSLocation(Context context) {
        this.context = context;
        if (checkServices() ) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        changeLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        changeLocation();
    }


    /**
     * Method to verify google play services on the device
     * */
    public boolean checkServices() {
        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS || !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            return false;
        }
        return true;
    }

    /**
     * Creating google api client object
     * */
    protected  void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private String getAddress(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        String finalAddress = "";
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> addresses = geoCoder.getFromLocation(lat, lng, 1);

            for (Address adrs : addresses) {
                if (adrs != null) {
                    finalAddress = adrs.getLocality();
                    System.out.println(adrs.getLocality());
                    if (finalAddress != null && !finalAddress.equals("")) {
                        break;
                    }
                }
            }


        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }

        return  finalAddress;
    }

    private void changeLocation() {

        Location lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if(lastLocation != null) {
            PlaceModel placeModel = new PlaceModel(
                    0,
                    lastLocation.getLatitude(),
                    lastLocation.getLongitude(),
                    getAddress(lastLocation),
                    "",
                    ""
            );
            ((MainActivity) context).onChangeLocation(placeModel);
        }

    }

}
