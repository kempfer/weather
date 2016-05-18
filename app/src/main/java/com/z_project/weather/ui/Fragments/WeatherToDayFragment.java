package com.z_project.weather.ui.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.z_project.weather.Models.DBHelper;
import com.z_project.weather.Models.Storage.WeatherStorage;
import com.z_project.weather.R;

import com.z_project.weather.Models.PlaceModel;
import com.z_project.weather.Models.WeatherCurrentModel;

import com.z_project.weather.network.openweathermap.Weather;
import com.z_project.weather.network.openweathermap.WeatherApi;

import org.w3c.dom.Text;

import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherToDayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherToDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherToDayFragment extends Fragment {


    private static final String ARG_PLACE = "place";
    private static final String ARG_WEATHER_DATA = "weather_data";

    private PlaceModel place;

    private View view;

    private WeatherApi weatherApi;

    private WeatherCurrentModel weatherModel;


    protected TextView textHumidity;

    protected TextView textPressure;

    protected TextView textTemperatureMain;

    protected TextView textWind;

    protected TextView textLocation;

    protected TextView textCurrentWeacher;

    public WeatherToDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeatherToDayFragment.
     */
    public static WeatherToDayFragment newInstance() {
        WeatherToDayFragment fragment = new WeatherToDayFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        weatherApi = new WeatherApi();
        setRetainInstance(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_weather_to_day, container, false);


        init();
        return  view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView() != null ? super.getView() : view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public PlaceModel getPlace() {
        return place;
    }

    public void setPlace(PlaceModel place) {
        this.place = place;
        if (this.place != null && getView() != null) {
            refresh();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void init () {
        if(getArguments() != null ) {
            weatherModel = new Gson().fromJson(getArguments().getString(ARG_WEATHER_DATA), WeatherCurrentModel.class);

        }
        if (weatherModel != null) {
            fillData(weatherModel);
        } else {
            if (this.place != null) {
                refresh();
            }
        }


    }

    private void refresh () {
        final WeatherStorage weatherStorage = new WeatherStorage(DBHelper.getInstance(getActivity(), 1));
        WeatherCurrentModel weatherCurrentModel = weatherStorage.findByPlaceId(place.getId());

        if(weatherCurrentModel == null) {
            refreshByNetwork();
        } else {
            java.util.Date date= new java.util.Date();
            long currentTime = (new Timestamp(date.getTime())).getTime();
            System.out.println(currentTime);
            System.out.println(weatherCurrentModel.getLastUpdate());
            fillData(weatherCurrentModel);
        }

    }

    private void refreshByNetwork () {
        final WeatherStorage weatherStorage = new WeatherStorage(DBHelper.getInstance(getActivity(), 1));
        if(weatherApi == null) {
            weatherApi = new WeatherApi();
        }
        weatherApi.getToDay(place.getLatitude(), place.getLongitude(), new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                if (response.isSuccessful()) {
                    weatherModel = new WeatherCurrentModel(response.body());
                    weatherStorage.save(place.getId(), weatherModel);
                    fillData(weatherModel);
                } else {
                    // error response, no access to resource?
                    System.out.println("onFailure onResponse");
                }


            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    private void fillData (WeatherCurrentModel weatherModel) {
        findViewElement();
        String wind = Double.valueOf(weatherModel.getWind()).toString() + " м/с";
        String pressure = Double.valueOf(weatherModel.getPressure()).toString() + " hpa";
        String humidity = Double.valueOf(weatherModel.getHumidity()).toString() + " %";
        String temperature = Integer.valueOf(weatherModel.getTempCelsius()).toString() + "°";
        textWind.setText(wind);
        textPressure.setText(pressure);
        textTemperatureMain.setText(temperature);
        textHumidity.setText(humidity);
        textLocation.setText(place.getName());
        textCurrentWeacher.setText(weatherModel.getDescription());
    }

    private void findViewElement() {
        View view = getView();
        textHumidity = (TextView) view.findViewById(R.id.humidity_value);
        textPressure = (TextView) view.findViewById(R.id.pressure_value);
        textTemperatureMain = (TextView) view.findViewById(R.id.temperature_main);
        textWind = (TextView) view.findViewById(R.id.wind_value);
        textLocation = (TextView) view.findViewById(R.id.location_value);
        textCurrentWeacher = (TextView) view.findViewById(R.id.current_weacher);

    }
}
