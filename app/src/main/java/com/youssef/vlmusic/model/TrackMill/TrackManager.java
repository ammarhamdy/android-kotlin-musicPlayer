package com.youssef.vlmusic.model.TrackMill;



import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import com.youssef.vlmusic.R;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class TrackManager {
    /*
     * use the MediaStore class to scanning for any audio files from stored in the device.
     * save all this audio information (id, name, duration, uri) in our list .
     * */


    private Cursor cursor;
    private Context context;
    public static final ArrayList<Track> tracksList = new ArrayList<>();
//    public static final ArrayList<Track> selfTracksList = new ArrayList<>();


    public TrackManager(Context context, boolean forSelfTrack) {
        this.context = context;
        setCursor(context);
//        if (!forSelfTrack)
//            setCursor(context);
    }


    private void setCursor(Context context) {
        cursor = context.getContentResolver().query(
                getUri(),
                getProjection(),
                getSelection(),
                getSelectionArgs(),
                getSortOrder()
        );
    }


    private Uri getUri() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return MediaStore.Audio.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        else
            return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }


    private String[] getProjection() {
        return new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST
        };
    }


    private String getSelection() {
        return MediaStore.Audio.Media.DURATION + " >= ?";
    }


    private String[] getSelectionArgs() {
        return new String[]{
                String.valueOf(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES))
        };
    }


    private String getSortOrder() {
        return MediaStore.Audio.Media.DISPLAY_NAME + " ASC";
    }


    public void loadTracks() {
        // Cache column indices or return -1 if can't catch column.
        int idColumn =
                cursor.getColumnIndex(MediaStore.Audio.Media._ID);
        int nameColumn =
                cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
        int durationColumn =
                cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int artistColumn =
                cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        scanningForAudio(idColumn, nameColumn, durationColumn,  artistColumn);
//        loadSelfTrack(context);
    }

    private void scanningForAudio(int idColumn, int nameColumn, int durationColumn, int artistColumn){
        int counter = 0;
        while (cursor.moveToNext()) {
            // Get values of columns for a given Audio.
            String name = cursor.getString(nameColumn);
            if (!name.endsWith(".mp3"))
                continue;
            long id = cursor.getLong(idColumn);
            Uri contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
            );
            String artist = cursor.getString(artistColumn);
            artist = artist == null? "" : artist;
            int duration = cursor.getInt(durationColumn);
            tracksList.add(new Track(id, counter++, name, artist, duration, contentUri));
        }
    }

    public void loadSelfTrack(Context context){
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        String title, artist, duration;
        Uri contentUri;
        int counter = 0;
        try{
            for(Field field: R.raw.class.getFields()){
                contentUri = Uri.parse("android.resource://"+context.getPackageName()+"/raw/"+field.getName());
                metadataRetriever.setDataSource(context, contentUri);
                title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                duration  = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                if (duration == null) continue;
                tracksList.add(new Track(
                        field.getInt(null),
                        counter ++,
                        title == null? field.getName()+"." : title,
                        artist == null? "Unknown" : artist,
                        Integer.parseInt(duration),
                        contentUri
                     )
                );
            }
        }catch (Exception ignored){}
    }

}

//    private boolean isMp3(String title){
//        if (title.length() < 5)
//            return false;
//        int i = (title.length() - 1);
//        return title.charAt(i--) == '3' && title.charAt(i--) == 'p' && title.charAt(i) == 'm';
//    }

