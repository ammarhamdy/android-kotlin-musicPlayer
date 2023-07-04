package com.youssef.vlmusic.model.PlaylistMill;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import java.util.ArrayList;


public class PlaylistManager {

    // titles
    private final SharedPreferences titleStore;
    private final Editor titleEditor;


    public  PlaylistManager(Activity activity){
        // Preference of preference's titles.
        titleStore = activity.getSharedPreferences("playlist_titles", Context.MODE_PRIVATE);
        titleEditor = titleStore.edit();
    }

    public void addPlayList(String title){
        if (!titleStore.getBoolean(title, false))
            titleEditor.putBoolean(title, true);
    }

    public void removePlayList(String title){
        titleEditor.remove(title);
        // loop for each shared preference for each title and delete it
    }

    public void removePlayList(String... titles){
        for (String title: titles)
            titleEditor.remove(title);
    }

    public void removeAll(){
        titleEditor.clear();
    }

    public void commit(){
        titleEditor.commit();
    }

    public ArrayList<String> getTitles(){
        return new ArrayList<>(titleStore.getAll().keySet());
    }



}
