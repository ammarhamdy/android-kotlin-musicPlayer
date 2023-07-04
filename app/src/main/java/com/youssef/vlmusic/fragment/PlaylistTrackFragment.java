package com.youssef.vlmusic.fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResult;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.youssef.vlmusic.R;
import com.youssef.vlmusic.activity.MainActivity;
import com.youssef.vlmusic.model.PlaylistMill.FavoritePlayListManager;
import com.youssef.vlmusic.model.TrackMill.Track;
import com.youssef.vlmusic.model.TrackMill.TrackManager;
import com.youssef.vlmusic.model.TrackMill.TrackRecyclerAdapter;
import java.util.ArrayList;


public class PlaylistTrackFragment extends Fragment implements CanCalledBack, ClickableTrackItem {

    private MainActivity parentActivity;
    private TrackRecyclerAdapter adapter;
    private ArrayList<Track> tracksList;

    public PlaylistTrackFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_playlist_track, container, false);
        initVars();
        setRecyclerAdapter(parentView);
        return parentView;
    }

    private void initVars(){
        FavoritePlayListManager favoriteManager = new FavoritePlayListManager(this.requireActivity());
        tracksList = favoriteManager.getFavorites(TrackManager.tracksList);
        Track.targetList = tracksList;
        parentActivity = (MainActivity) requireActivity();
        parentActivity.canCalledBack = this;
    }

    private void setRecyclerAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.playlist_track_recyclerview);
        adapter = new TrackRecyclerAdapter(
                getContext(),
                this,
                Track.targetList
        );
        adapter.clickableTrackItem = this;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTrackItemClicked() {
        // launch controller activity.
        parentActivity.launchControllerActivity();
    }

    @Override
    public void onCalledBack(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK){
            Intent intent = result.getData();
            if (intent != null)
                removeTrackItem(intent.getLongExtra("id", -1));
        }
    }

    private void removeTrackItem(long id){
        for(int i = 0; i < Track.targetList.size(); i++)
            if (tracksList.get(i).id == id && !tracksList.get(i).isFavorite){
                tracksList.get(i).isFavorite = false;
                tracksList.remove(i);
                adapter.notifyItemRemoved(i);
                adapter.notifyItemRangeChanged(i, tracksList.size());
                break;
        }
    }

}