package com.youssef.vlmusic.model.PlaylistMill;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.youssef.vlmusic.R;
import java.util.ArrayList;


public class PlaylistRecyclerAdapter
        extends RecyclerView.Adapter<PlaylistRecyclerAdapter.playlistViewHolder>{

    private final ArrayList<String> titles;

    public PlaylistRecyclerAdapter(ArrayList<String> titles){
        this.titles = titles;
    }

    @NonNull
    @Override
    public playlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new playlistViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.playlist_view_item,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull playlistViewHolder holder, int position) {
        holder.playlistTitleTextView.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    // ......................my holder class...................... \\
    public static class playlistViewHolder extends RecyclerView.ViewHolder {
        /* receive item view from "onCreateViewHolder" from adapter. */

        public TextView playlistTitleTextView;
        public CardView playlistItemView;

        public playlistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistTitleTextView = itemView.findViewById(R.id.playlist_title_textview);
            playlistItemView = itemView.findViewById(R.id.playlist_item_view);
        }

    }
}
