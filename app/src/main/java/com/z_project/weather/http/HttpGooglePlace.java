package com.z_project.weather.http;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HttpGooglePlace extends Http {

    private static final String TAG = "HttpGooglePlace";

    private static final String API_KEY = "AIzaSyBUj-DX_ypKkJ7Spp2JAraJUt-25EolfpE";

    private static final Uri ENDPOINT_AUTOCOMPLETE = Uri.parse("https://maps.googleapis.com/maps/api/place/autocomplete/json")
            .buildUpon()
            .appendQueryParameter("key", API_KEY)
            .appendQueryParameter("types", "(cities)")
            .build();

    private static final Uri ENDPOINT_DETAILS = Uri.parse("https://maps.googleapis.com/maps/api/place/details/json")
            .buildUpon()
            .appendQueryParameter("key", API_KEY)
            .build();

    public List<Place> getPlaces(String query)  {
        String urlString = ENDPOINT_AUTOCOMPLETE.buildUpon().appendQueryParameter("input", query).toString();
        List<HttpGooglePlace.Place> places = new ArrayList<>();
        try {
            String response = getUrlString(urlString);
            places = parsePlaces(response);
        } catch (IOException e) {
            Log.e(TAG, "ERROR autocomplete place" + e);
        }

        return places;
    }

    public PlaceGeometry getPlaceGeometry(String placeId) {
        String urlString = ENDPOINT_DETAILS.buildUpon().appendQueryParameter("placeid", placeId).toString();
        PlaceGeometry placeGeometry = null;
        try {
            String response = getUrlString(urlString);

            placeGeometry = parsePlaceDetails(response);
        } catch (IOException e) {
            Log.e(TAG, "ERROR details place" + e);
        }

        return  placeGeometry;

    }


    private List<Place> parsePlaces (String jsonBody) {
        List<Place> places = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(jsonBody);
            JSONArray predictionJsonArray = jsonObject.getJSONArray("predictions");
            for(int i = 0; i < predictionJsonArray.length(); i++) {
                JSONObject predictionJsonObject = predictionJsonArray.getJSONObject(i);
                Place place = new Place();
                place.setDescription(predictionJsonObject.getString("description"));
                place.setPlaceId(predictionJsonObject.getString("place_id"));
                JSONArray termJsonArray =  predictionJsonObject.getJSONArray("terms");
                place.setCity(termJsonArray);
                place.setCountry(termJsonArray);
                place.setRegion(termJsonArray);
                places.add(place);

            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to fetch places", e);
        }

        return places;

    }

    private PlaceGeometry  parsePlaceDetails (String jsonBody) {
        PlaceGeometry placeGeometry = null;
        try {

            JSONObject jsonObject = new JSONObject(jsonBody);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject geometry = result.getJSONObject("geometry");
            JSONObject geometryLocation = geometry.getJSONObject("location");
            Location location = new Location("dummyprovider");
            location.setLatitude( geometryLocation.getDouble("lat"));
            location.setLongitude(geometryLocation.getDouble("lng"));
            placeGeometry = new PlaceGeometry(location);

        } catch (JSONException e) {
            Log.e(TAG, "Failed to fetch place details", e);
        }

        return  placeGeometry;
    }



    public class Place {

        private String mPlaceId;

        private String mDescription;

        private String mCity;

        private String mCountry;

        private String mRegion;


        public String getPlaceId() {
            return mPlaceId;
        }

        public void setPlaceId(String placeId) {
            mPlaceId = placeId;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public String getCity() {
            return mCity;
        }

        public void setCity(String city) {
            mCity = city;
        }

        public String getCountry() {
            return mCountry;
        }

        public void setCountry(String country) {
            mCountry = country;
        }

        public void setCity (JSONArray terms) {
            try {
                JSONObject termJsonObject = terms.getJSONObject(0);
                mCity = termJsonObject.getString("value");
            } catch (JSONException e) {
                Log.e(TAG, "Failed to fetch terms", e);
            }
        }

        public void setCountry (JSONArray terms) {
            try {
                JSONObject termJsonObject;
                if(terms.length() == 3) {
                    termJsonObject = terms.getJSONObject(2);
                } else {
                    termJsonObject = terms.getJSONObject(1);
                }
                mCountry = termJsonObject.getString("value");

            } catch (JSONException e) {
                Log.e(TAG, "Failed to fetch terms", e);
            }
        }

        public String getRegion() {
            return mRegion;
        }

        public void setRegion(String region) {
            mRegion = region;
        }

        public void setRegion(JSONArray terms) {
            try {
                JSONObject termJsonObject;
                if(terms.length() == 3) {
                    termJsonObject = terms.getJSONObject(1);
                    mRegion = termJsonObject.getString("value");
                }

            } catch (JSONException e) {
                Log.e(TAG, "Failed to fetch terms", e);
            }
        }
    }

    public class PlaceGeometry {

        private Location mLocation;

        public PlaceGeometry(Location location) {
            mLocation = location;
        }

        public Location getLocation() {
            return mLocation;
        }
    }

}
