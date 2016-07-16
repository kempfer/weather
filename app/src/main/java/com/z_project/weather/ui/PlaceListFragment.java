package com.z_project.weather.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.z_project.weather.Place;
import com.z_project.weather.PlaceLab;
import com.z_project.weather.R;
import com.z_project.weather.Weather;
import com.z_project.weather.WeatherLab;
import com.z_project.weather.http.HttpOpenWeatherMap;
import com.z_project.weather.services.PollService;

import java.util.List;

public class PlaceListFragment extends Fragment {

    private static final String TAG = "PlaceListFragment";

    private RecyclerView mPlaceRecyclerView;

    private PlaceAdapter mPlaceAdaper;

    private WeatherLab mWeatherLab;

    public static PlaceListFragment newInstance () {

        return new PlaceListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherLab = WeatherLab.getInstance(getActivity());
        PollService.setServiceAlarm(getActivity());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_list, container, false);

        mPlaceRecyclerView = (RecyclerView) view.findViewById(R.id.place_recycler_view);
        mPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Place> places = PlaceLab.getInstance(getActivity()).getPlaces();

        Log.i(TAG, "place count" + places.size());

        mPlaceAdaper = new PlaceAdapter(places);
        mPlaceRecyclerView.setAdapter(mPlaceAdaper);


        return  view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_place_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_item_add_place) {
            Intent intent = new Intent(getActivity(), PlaceSearchActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class WeatherTask extends AsyncTask<Void,Void,Void> {

        private Place mPlace;

        public WeatherTask(Place place) {
            mPlace = place;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Weather weather = new HttpOpenWeatherMap().getToDay(mPlace);
            mWeatherLab.updateToDay(weather);
            Log.i(TAG, "weather "  + weather.getDescription());
            return null;

        }
    }


    private class PlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Place mPlace;

        private TextView mNameTextView;

        public PlaceHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_place_name);
            itemView.setOnClickListener(this);
        }

        private void bindPlace (Place place) {
            mPlace = place;
            mNameTextView.setText(mPlace.getDescription());
        }

        @Override
        public void onClick(View v) {
            //new WeatherTask(mPlace).execute();
            Log.i(TAG, mWeatherLab.getToDay(mPlace).getDescription());
        }
    }

    private class PlaceAdapter extends RecyclerView.Adapter<PlaceHolder> {

        private List<Place> mPlaces;

        public PlaceAdapter(List<Place> places) {
            mPlaces = places;
        }

        @Override
        public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_place, parent, false);
            return new PlaceHolder(view);
        }

        @Override
        public void onBindViewHolder(PlaceHolder holder, int position) {
            Place place = mPlaces.get(position);
            holder.bindPlace(place);
        }

        @Override
        public int getItemCount() {
            return mPlaces.size();
        }
    }

}
