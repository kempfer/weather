package com.z_project.weather.ui.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.z_project.weather.Models.DBHelper;
import com.z_project.weather.Models.PlaceModel;
import com.z_project.weather.Models.Storage.PlaceStorage;
import com.z_project.weather.R;
import com.z_project.weather.ui.Activities.MainActivity;
import com.z_project.weather.ui.Adapters.PlaceListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GPS_ENABLE = "gps_enable";

    private PlaceStorage placeStorage;

    private  final int REQUEST_PLACE_PICKER = 12345;

    private boolean gpsEnable = false;

    protected FloatingActionButton btnOpenActivityAddPlace;

    protected ListView placeList;

    protected PlaceListAdapter placeListAdapter;

    protected PlaceModel currentPlace;

    protected TextView textViewcurrentPlace;


    public PlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlaceFragment.
     */
    public static PlaceFragment newInstance(boolean gpsEnabled) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_GPS_ENABLE, gpsEnabled);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeStorage = new PlaceStorage(DBHelper.getInstance(getActivity(), 1));

        if (placeStorage.findCurrent()  == null) {
            clickCurrentLocation(false);
        }
        if (getArguments() != null) {
            gpsEnable = getArguments().getBoolean(ARG_GPS_ENABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        placeList = (ListView) view.findViewById(R.id.place_list_view);
        btnOpenActivityAddPlace = (FloatingActionButton) view.findViewById(R.id.btn_open_add_place);
        btnOpenActivityAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFindPlace();
            }
        });
        textViewcurrentPlace = (TextView) view.findViewById(R.id.current_place);

        textViewcurrentPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCurrentLocation(true);
            }
        });

        placeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onClickItem(id);
            }
        });
        placeListAdapter = getGuideAdapter();
        placeList.setAdapter(placeListAdapter);


        if (gpsEnable == false) {
            TextView textView = (TextView) view.findViewById(R.id.current_place);
            textView.setText("Current Location: disabled");
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (placeStorage.findCurrent()  == null) {
            clickCurrentLocation(false);
        }
    }

    private void openFindPlace () {

        try {

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    public void setCurrentPlace(PlaceModel placeModel) {
        this.currentPlace = placeModel;
        TextView textView = (TextView) getView().findViewById(R.id.current_place);
        textView.setText("Current Location: " + currentPlace.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER && resultCode == Activity.RESULT_OK) {
            addPlace(data);
        }
    }

    private void addPlace(Intent data) {

        // The user has selected a place. Extract the name and address.
        final Place place = PlacePicker.getPlace(getContext(), data);
        LatLng latLng = place.getLatLng();

        long id = placeStorage.addPlace((String) place.getName(),"", latLng.latitude, latLng.longitude, place.getId());

        placeListAdapter.refresh();

    }

    private PlaceListAdapter getGuideAdapter () {
        String[] columns = new String[]{
                DBHelper.COLUMN_NAME,
                DBHelper.COLUMN_CURRENT
        };

        int[] to = new int[]{
                R.id.place_list_place_name
        };


        Cursor cursor = placeStorage.fetchAllPlaces();

        PlaceListAdapter dataAdapter = new PlaceListAdapter(
                getActivity(), R.layout.place_item,
                cursor,
                columns,
                to,
                0,
                this);
        dataAdapter.setDbHelper(placeStorage);

        return dataAdapter;
    }

    private void onClickItem(long id) {
        placeStorage.selectedById(id);
        PlaceModel placeModel = placeStorage.findById(id);

        ((MainActivity)getActivity()).toWeatherToDay(placeModel, true);

        placeListAdapter.refresh();
        TextView textView = (TextView) getView().findViewById(R.id.current_place);
        textView.setTextColor(Color.WHITE);


    }


    private void clickCurrentLocation(boolean switchToday) {
        if (currentPlace !=null) {
            PlaceModel placeModel = currentPlace;

            ((MainActivity)getActivity()).toWeatherToDay(placeModel, switchToday);

            placeStorage.removeSelectedAll();

            TextView textView = (TextView) getView().findViewById(R.id.current_place);
            textView.setTextColor(Color.YELLOW);
            placeListAdapter.refresh();
        }

    }

    public void onPlaceDelete() {
        clickCurrentLocation(false);
    }
}
