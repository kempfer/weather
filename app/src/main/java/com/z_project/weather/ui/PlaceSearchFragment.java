package com.z_project.weather.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.z_project.weather.HttpGooglePlace;
import com.z_project.weather.R;

import java.util.ArrayList;
import java.util.List;

public class PlaceSearchFragment extends Fragment {

    private static final String TAG = "PlaceSearchFragment";

    private RecyclerView mPlaceRecyclerView;

    private List<HttpGooglePlace.Place> mPlaces = new ArrayList<>();

    public static PlaceSearchFragment newInstance() {
        return new PlaceSearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_search, container, false);
        mPlaceRecyclerView = (RecyclerView) view.findViewById(R.id.place_search_recycler_view);
        mPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_place_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 2) {
                    new PlaceSearchTask(newText).execute();
                }

                return true;
            }
        });

    }

    private void setupAdapter () {
        if(isAdded()) {
            mPlaceRecyclerView.setAdapter(new PlaceAdapter(mPlaces));
        }
    }

    private class PlaceSearchTask extends AsyncTask<Void,Void, List<HttpGooglePlace.Place>> {

        private String mQuery;

        public PlaceSearchTask(String query) {
            mQuery = query;
        }

        @Override
        protected List<HttpGooglePlace.Place> doInBackground(Void... params) {
            return new HttpGooglePlace().getPlaces(mQuery);
        }

        @Override
        protected void onPostExecute(List<HttpGooglePlace.Place> places) {
            mPlaces = places;
            setupAdapter();
        }
    }

    private class PlaceDetailsTask extends  AsyncTask<Void,Void, HttpGooglePlace.PlaceGeometry> {

        private String mPlaceId;

        public PlaceDetailsTask(String placeId) {
            mPlaceId = placeId;
        }

        @Override
        protected HttpGooglePlace.PlaceGeometry doInBackground(Void... params) {
            return new HttpGooglePlace().getPlaceGeometry(mPlaceId);

        }

        @Override
        protected void onPostExecute(HttpGooglePlace.PlaceGeometry placeGeometry) {
            Log.i(TAG, placeGeometry.getLocation().toString());
        }
    }

    private class PlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private HttpGooglePlace.Place mPlace;

        private TextView mDescriptionTextView;

        public PlaceHolder(View itemView) {
            super(itemView);
            mDescriptionTextView = (TextView) itemView;
            itemView.setOnClickListener(this);
        }

        private void bindPlace (HttpGooglePlace.Place place) {
            mPlace = place;
            mDescriptionTextView.setText(mPlace.getDescription());
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "Selected place id" + mPlace.getPlaceId());
            new PlaceDetailsTask(mPlace.getPlaceId()).execute();
        }
    }

    private class PlaceAdapter extends RecyclerView.Adapter<PlaceHolder> {
        private List<HttpGooglePlace.Place> mPlaces;

        public PlaceAdapter(List<HttpGooglePlace.Place> places) {
            mPlaces = places;
        }

        @Override
        public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new PlaceHolder(view);
        }

        @Override
        public void onBindViewHolder(PlaceHolder holder, int position) {
            HttpGooglePlace.Place place = mPlaces.get(position);
            holder.bindPlace(place);
        }

        @Override
        public int getItemCount() {
            return mPlaces.size();
        }
    }


}
