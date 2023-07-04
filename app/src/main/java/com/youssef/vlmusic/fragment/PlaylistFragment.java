package com.youssef.vlmusic.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.youssef.vlmusic.R;
import com.youssef.vlmusic.activity.MainActivity;
import com.youssef.vlmusic.model.PlaylistMill.PlaylistManager;
import com.youssef.vlmusic.model.PlaylistMill.PlaylistRecyclerAdapter;


public class PlaylistFragment extends Fragment {

    private FragmentActivity activity;
    private PlaylistManager manager;
    private TextView favoriteTextView;
    private PlaylistTrackFragment playlistTrackFragment;
    public PlaylistRecyclerAdapter adapter;


    public PlaylistFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_playlist, container, false);
        initVars(parentView);
        setRecyclerAdapter(parentView);
        setActions();
        return parentView;
    }

    private void  initVars(View view){
        playlistTrackFragment = new PlaylistTrackFragment();
        activity = this.requireActivity();
        manager = new PlaylistManager(activity);
        favoriteTextView = view.findViewById(R.id.favorite_image_textview);
    }

    private void setRecyclerAdapter(View view){
        adapter = new PlaylistRecyclerAdapter(manager.getTitles());
        RecyclerView recyclerView = view.findViewById(R.id.playlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setActions(){
        favoriteTextView.setOnClickListener(l -> showPlaylistTrackFragment());
    }

    private void showPlaylistTrackFragment(){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, playlistTrackFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        MainActivity.pageNumber = 21;
    }

}