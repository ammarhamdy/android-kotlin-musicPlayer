package com.youssef.vlmusic.model.TrackMill;


import static androidx.core.content.ContextCompat.getDrawable;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.youssef.vlmusic.R;
import com.youssef.vlmusic.fragment.ClickableTrackItem;

import java.util.ArrayList;


public class TrackRecyclerAdapter
        extends RecyclerView.Adapter<TrackRecyclerAdapter.TrackViewHolder>{


    private final Context context;
    public ClickableTrackItem clickableTrackItem;
    private final FragmentActivity fActivity;
    private final ArrayList<Track> list;


    public TrackRecyclerAdapter(Context context, Fragment fragment, ArrayList<Track> targetList) {
        this.context = context;
        fActivity = fragment.requireActivity();
        list = targetList;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrackViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.track_view_item,
                        parent,
                        false
                )
        );
    }


    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        holder.titleTextView.setText(list.get(position).getName());
        holder.artistTextView.setText(list.get(position).getArtist());
        setPicture(position, holder.pictureImageview);
        // track item view on press actions.
        holder.trackItemView.setOnClickListener((view->{
            Track.setTargetTrack(position);
            markSelectedTrackItem(holder.trackItemView);
            clickableTrackItem.onTrackItemClicked();
            unMarkSelectedTrackItem(holder.trackItemView);
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    private void removeExtension(String name){}

    private void markSelectedTrackItem(CardView selectedCardView){
        selectedCardView.getChildAt(0).setBackground(
                getDrawable(context, R.drawable.round_green_stroke)
        );
    }

    private void unMarkSelectedTrackItem(CardView selectedCardView){
        new Thread(()->{
            try {Thread.sleep(250);} catch (Exception ignored) {}
            fActivity.runOnUiThread(()->selectedCardView.getChildAt(0).setBackground(getDefaultBackground()));
        }).start();
    }

    public void setPicture(int trackIndex, ImageView trackImageview){
        new Thread(()->{
            Bitmap picture = list.get(trackIndex).getPicture(context);
            if (picture != null) {
                fActivity.runOnUiThread(() ->
                        trackImageview.setImageBitmap(picture));
                trackImageview.setTag(true);
            }else {
                if ((boolean)trackImageview.getTag()){
                    fActivity.runOnUiThread(() ->
                            trackImageview.setImageDrawable(getDrawable(context, R.drawable.ic_round_music_note_64_light)));
                    trackImageview.setTag(false);
                }
            }
        }).start();
    }


    public Drawable getDefaultBackground(){
        return ((context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES)?
                getDrawable(context, R.drawable.round_quite_dark_gray)
                :
                getDrawable(context, R.drawable.round_gray);
    }


    // ......................my holder class...................... \\
    public static class TrackViewHolder extends RecyclerView.ViewHolder{
        /* receive item view from "onCreateViewHolder" from adapter. */

        public TextView titleTextView;
        public TextView artistTextView;
        public ImageView pictureImageview;
        public CardView trackItemView;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_textview);
            artistTextView = itemView.findViewById(R.id.artist_textview);
            pictureImageview = itemView.findViewById(R.id.picture_imageview);
            // tag false mean that the image view hold default picture (the green tone picture).
            // mean that the image view picture  not changed.
            pictureImageview.setTag(false);
            trackItemView = itemView.findViewById(R.id.my_song_card_view);
        }

    }

}
