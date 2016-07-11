package com.z_project.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PlaceListFragment extends Fragment {

    private static final String TAG = "PlaceListFragment";

    private RecyclerView mPlaceRecyclerView;

    private PlaceAdapter mPlaceAdaper;

    public static PlaceListFragment newInstance () {

        return new PlaceListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    private class PlaceHolder extends RecyclerView.ViewHolder {

        private Place mPlace;

        private TextView mNameTextView;

        public PlaceHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_place_name);

        }

        private void bindPlace (Place place) {
            mPlace = place;
            mNameTextView.setText(mPlace.getName());
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
