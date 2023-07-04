package com.youssef.vlmusic.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.youssef.vlmusic.R;
import com.youssef.vlmusic.activity.ControllerActivity;
import com.youssef.vlmusic.model.PlaylistMill.FavoritePlayListManager;
import com.youssef.vlmusic.model.TrackMill.Track;
import com.youssef.vlmusic.model.TrackMill.TrackManager;
import com.youssef.vlmusic.model.TrackMill.TrackRecyclerAdapter;


public class TrackFragment extends Fragment implements ClickableTrackItem{


    private Context context;
    private static boolean isTracksLoaded;


    public TrackFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_track, container, false);
        initVars();
        setRecyclerAdapter(view);
        showToastIfListIsEmpty();
        return view;
    }

    private void initVars(){
        context = getContext();
        Track.targetList = TrackManager.tracksList;
    }

    private void setRecyclerAdapter(View view){
        if (!isTracksLoaded) {
             new TrackManager(context, false).loadTracks();
            preparingFavoriteTracks();
            isTracksLoaded = true;
        }
        RecyclerView recyclerView = view.findViewById(R.id.track_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        TrackRecyclerAdapter adapter = new TrackRecyclerAdapter(context, this, Track.targetList);
        adapter.clickableTrackItem = this;
        recyclerView.setAdapter(adapter);
    }

    private void preparingFavoriteTracks(){
        new Thread(
                ()-> new FavoritePlayListManager(
                        requireActivity()
                ).notifyFavoriteTracks(TrackManager.tracksList)
        ).start();
    }

    private void showToastIfListIsEmpty(){
        if (Track.targetList.isEmpty())
            Toast.makeText(context, "not thing to show!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackItemClicked() {
        // go to controller activity
        startActivity(new Intent(context, ControllerActivity.class));
    }

}