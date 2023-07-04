package com.youssef.vlmusic.model.TrackMill;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Track {

    // static properties.
    public static Track targetTrack = null;
    public static CanCreateMedia canCreateMedia;
    private static int targetIndex = -1;
    private static MediaPlayer mediaPlayer;
    private  static boolean playedBefore;
    private static final Random random = new Random(1663501273);
    public static boolean isShuffleOn;
    public static boolean volumeIsOn = true;
    // list.
    public static ArrayList<Track> targetList =  null;
    // track properties.
    public final long id;
    public final int index;
    private final String name;
    private final String artist;
    public final int duration;
    private final Uri contentUri;
    public boolean isFavorite;


    public Track(long id, int index, String name, String artist, int duration, Uri contentUri) {
        this.id = id;
        this.index = index;
        this.name = name;
        this.duration = duration;
        this.contentUri = contentUri;
        this.artist = artist;
    }

    public static void setTargetTrack(int index) {
        /*
        set new target track to play it.
        point to previous media was played to stop it before start new one.
        */
        targetIndex = index;
        targetTrack = targetList.get(index);
    }

    public static void play(Context context, int seekBarPosition) {
        if (playedBefore) { // if the first played track played and paused and played again.
                mediaPlayer.start();  // resume the same track.
        } else {
            playNewTrack(context, seekBarPosition);  // first time play a track.
            playedBefore = true;
        }
    }

    private static void playNewTrack(Context context, int startFomTime) {
        newMediaPlayer(context);
        if (startFomTime != 0)
            mediaPlayer.seekTo(startFomTime);
        mediaPlayer.start();
    }

    private static void newMediaPlayer(Context context) {
        if (mediaPlayer != null)
            mediaPlayer.release();
        if (targetTrack.contentUri.toString().startsWith("android"))
            mediaPlayer = MediaPlayer.create(context, (int) targetTrack.id);
        else
            mediaPlayer = MediaPlayer.create(context, targetTrack.contentUri);
        if (!volumeIsOn)
            mediaPlayer.setVolume(0, 0);  // set volume off
        mediaPlayer.setOnCompletionListener(
                (mediaPlayer)-> {
                    playNext(context, true);
                    canCreateMedia.onTrackCompletion();   // show track details.
                }
        );
    }

    public static void pause() {
        mediaPlayer.pause();
    }

    public static void stop(){
        mediaPlayer.stop();
        playedBefore = false;
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playNext(Context context, boolean thePreviousWasPlaying) {
        /*
         if the current track is the last track play first track.
         else play next track
         */
        if (isShuffleOn)
            setTargetTrack(random.nextInt(targetList.size()));
        else
            setTargetTrack(targetIndex + 1 == targetList.size() ? 0 : targetIndex + 1);
        stop(); // stop playing the previous media instance
        newMediaPlayer(context);
        if (thePreviousWasPlaying)
            mediaPlayer.start();
    }

    public static void playPrevious(Context context, boolean thePreviousWasPlaying) {
        /*
        if the current track in the first on play last track.
        else play previous track.
        */
        if (isShuffleOn)
            setTargetTrack(random.nextInt(targetList.size()));
        else
            setTargetTrack(targetIndex == 0 ? targetList.size() - 1 : targetIndex - 1);
        stop(); // stop playing the previous media instance
        newMediaPlayer(context);
        if (thePreviousWasPlaying)
            mediaPlayer.start();
    }

    public static void setVolumeOn() {
        volumeIsOn = true;
        mediaPlayer.setVolume(1, 1);
    }

    public static void setVolumeOff() {
        volumeIsOn = false;
        mediaPlayer.setVolume(0, 0);
    }

    public String getName() {
        // remove the extensions from file name.
        int i = name.length() - 1;
        for (; i > 0; i--)
            if (name.charAt(i) == '.')
                break;
        return name.substring(0, i);
    }

    public String getArtist() {
        if (artist.length() == 0)
            return "Unknown";
        if (artist.charAt(0) == '<' && artist.charAt(artist.length() - 1) == '>')
            return "Unknown";
        return artist;
    }

    public int getCurrentTime(){
        return mediaPlayer.getCurrentPosition();
    }

    public static void setCurrentTime(int time){
        mediaPlayer.seekTo(time);
    }

    public Bitmap getPicture(Context context){
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(context, contentUri);
        if (metadataRetriever.getEmbeddedPicture() != null) {
            Bitmap picture = BitmapFactory.decodeStream(
                    new ByteArrayInputStream(metadataRetriever.getEmbeddedPicture())
            );
            metadataRetriever.release();
            metadataRetriever.close();
            return picture;
        }
        return null;
    }


}
