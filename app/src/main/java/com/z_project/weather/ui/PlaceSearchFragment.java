package com.z_project.weather.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.z_project.weather.HttpGooglePlace;
import com.z_project.weather.Place;
import com.z_project.weather.PlaceLab;
import com.z_project.weather.R;

import java.util.ArrayList;
import java.util.List;


public class PlaceSearchFragment extends Fragment {

    private static final String TAG = "PlaceSearchFragment";

    private RecyclerView mPlaceRecyclerView;

    private List<HttpGooglePlace.Place> mPlaces = new ArrayList<>();

    private EditText mSearchEditText;

    private ProgressBar mProgressBar;

    private ImageButton mBackButton;

    private PlaceLab  mPlaceLab;


    public static PlaceSearchFragment newInstance() {
        return new PlaceSearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPlaceLab = PlaceLab.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_search, container, false);
        mPlaceRecyclerView = (RecyclerView) view.findViewById(R.id.place_search_recycler_view);
        mPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearchEditText = (EditText) view.findViewById(R.id.place_search_edit_text);



        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2) {
                    new PlaceSearchTask(s.toString()).execute();
                }
            }
        });

        mBackButton = (ImageButton) view.findViewById(R.id.button_back);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    private void setupAdapter () {
        if(isAdded()) {
            mPlaceRecyclerView.setAdapter(new PlaceAdapter(mPlaces));
        }
    }

    private void addPlace (Place place) {
        mPlaceLab.add(place);
        getActivity().finish();
    }

    private class PlaceSearchTask extends AsyncTask<Void,Void, List<HttpGooglePlace.Place>> {

        private String mQuery;

        public PlaceSearchTask(String query) {
            mProgressBar.setVisibility(View.VISIBLE);
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
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private class PlaceDetailsTask extends  AsyncTask<Void,Void, HttpGooglePlace.PlaceGeometry> {

        private HttpGooglePlace.Place mGooglePlace;

        public PlaceDetailsTask(HttpGooglePlace.Place place) {
            mGooglePlace = place;
        }

        @Override
        protected HttpGooglePlace.PlaceGeometry doInBackground(Void... params) {
            return new HttpGooglePlace().getPlaceGeometry(mGooglePlace.getPlaceId());

        }

        @Override
        protected void onPostExecute(HttpGooglePlace.PlaceGeometry placeGeometry) {
            Place place = new Place();
            place.setName(mGooglePlace.getCity());
            place.setCountry(mGooglePlace.getCountry());
            place.setExternalId(mGooglePlace.getPlaceId());
            place.setLatitude(placeGeometry.getLocation().getLatitude());
            place.setLongitude(placeGeometry.getLocation().getLongitude());
            place.setRegion(mGooglePlace.getRegion());
            addPlace(place);
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
            new PlaceDetailsTask(mPlace).execute();
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
