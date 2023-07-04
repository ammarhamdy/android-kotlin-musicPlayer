package com.youssef.vlmusic.model.PlaylistMill;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.youssef.vlmusic.model.TrackMill.Track;
import java.util.ArrayList;


public class FavoritePlayListManager {

    // favorites
    private final SharedPreferences favoriteStore;
    private final SharedPreferences.Editor favoriteEditor;

    public FavoritePlayListManager(Activity activity){
        // Preference of favorites tracks.
        favoriteStore = activity.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        favoriteEditor = favoriteStore.edit();
    }

    public void addToFavorite(Track track){
        favoriteEditor.putLong(Integer.toString(track.index), track.id);
        favoriteEditor.commit();
    }

    public void removeFromFavorite(Track track){
        favoriteEditor.remove(Integer.toString(track.index));
        favoriteEditor.commit();
    }

    public ArrayList<Track> getFavorites(ArrayList<Track> tracks){
        ArrayList<Track> favoriteTracks = new ArrayList<>();
        for (String key: favoriteStore.getAll().keySet()){
            int index = Integer.parseInt(key);
            if (index < tracks.size())
                if (tracks.get(index).id == favoriteStore.getLong(key, -1))
                    favoriteTracks.add(tracks.get(index));
        }
        return favoriteTracks;
    }

    public void notifyFavoriteTracks(ArrayList<Track> tracks){
        // mark each track that saved as favorite (favorite = true)
        for (String key: favoriteStore.getAll().keySet()){
            int index = Integer.parseInt(key);
            if (index < tracks.size())
                if (tracks.get(index).id == favoriteStore.getLong(key, -1))
                    tracks.get(index).isFavorite = true;
        }
    }

}
/*private Track searchForTrack(long id){
        return null;
    }*/
