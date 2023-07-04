package com.youssef.vlmusic.activity;


//...imports....\\
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.youssef.vlmusic.R;
import com.youssef.vlmusic.model.ControllerView.ControllerImageButton;
import com.youssef.vlmusic.model.IconViewer;
import com.youssef.vlmusic.model.PlaylistMill.FavoritePlayListManager;
import com.youssef.vlmusic.model.TimeFormatter;
import com.youssef.vlmusic.model.TrackMill.CanCreateMedia;
import com.youssef.vlmusic.model.TrackMill.Track;


//...track controller activity....\\
public class ControllerActivity extends AppCompatActivity implements CanCreateMedia {


    //....controller properties....\\
    // activity context.
    private Context context;
    // time formatter
    private TimeFormatter timeFormatter;
    // seekbar handler.
    private Handler seekbarHandler;
    // controller views.
    private ControllerImageButton playIButton;
    private ControllerImageButton nextIButton;
    private ControllerImageButton previousIButton;
    private ControllerImageButton shuffleIButton;
    private ControllerImageButton volumeIButton;
    private ControllerImageButton favoriteImage;
    private TextView titleTextview;
    private TextView artistTextview;
    private TextView durationTextView;
    private TextView currentTimeTextView;
    private ImageView pictureImageview;
    private SeekBar seekbar;
    // icon viewer.
    private IconViewer iconViewer;
    private FavoritePlayListManager favoritePlayListManager;
    // playing controller.
    private boolean isPlay;
    private boolean isShuffleOn;
    private boolean isVolumeOn = true;
    private boolean isFavoriteOn;
    private boolean isAlive;
    private boolean defaultPictureIsOn = true;


    //...controller activity methods...\\
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        initVars();
        updateTrackDetails();
        setActions();
        playOrPause();
    }


    private void initVars(){
        isAlive = true;
        Track.isShuffleOn = false;
        Track.volumeIsOn = true;
        context = getApplicationContext();
        timeFormatter = new TimeFormatter();
        favoriteImage = findViewById(R.id.favorite_image);
        playIButton = findViewById(R.id.play_image);
        nextIButton = findViewById(R.id.next_image);
        previousIButton = findViewById(R.id.previous_image);
        shuffleIButton = findViewById(R.id.shuffle_image);
        volumeIButton = findViewById(R.id.volume_image);
        titleTextview = findViewById(R.id.controller_title_textview);
        artistTextview = findViewById(R.id.controller_artist_textview);
        durationTextView = findViewById(R.id.controller_duration_textview);
        currentTimeTextView = findViewById(R.id.controller_current_time_textview);
        pictureImageview = findViewById(R.id.controller_picture_imageview);
        iconViewer = new IconViewer(
                context.getResources().getConfiguration().uiMode,
                shuffleIButton,
                volumeIButton,
                playIButton,
                favoriteImage
        );
        seekbar = findViewById(R.id.controller_seekbar);
        seekbar.setMax(Track.targetTrack.duration);
        seekbar.setClickable(false);
        seekbarHandler  = new Handler();
        favoritePlayListManager = new FavoritePlayListManager(this);
    }


    private void updateTrackDetails(){
        runOnUiThread(()->{
            // update track picture
            updatePictureImageview();
            // update the favorite status.
            updateFavoriteStatus();
            // show and update the track title, artist, duration.
            titleTextview.setText(Track.targetTrack.getName());
            artistTextview.setText(Track.targetTrack.getArtist());
            durationTextView.setText(timeFormatter.format(Track.targetTrack.duration));
            // update seek bar status.
            seekbar.setMax(Track.targetTrack.duration);
            seekbar.setProgress(0);
            currentTimeTextView.setText(getText(R.string.zero_time));
        });
    }

    private void updateFavoriteStatus(){
        if (Track.targetTrack.isFavorite) {
            if (!iconViewer.favoriteOnIsShowed())
                isFavoriteOn = iconViewer.showFavoriteOn();
        }else{
            if (iconViewer.favoriteOnIsShowed())
                isFavoriteOn = iconViewer.showFavoriteOff();
        }
    }

    private void updatePictureImageview(){
        Bitmap picture = Track.targetTrack.getPicture(context);
        if (picture != null)
            showPicture(picture);
        else if (!defaultPictureIsOn)
            showDefaultPicture();
    }

    private void showPicture(Bitmap picture){
        defaultPictureIsOn = false;
        pictureImageview.setImageBitmap(picture);
        pictureImageview.setBackgroundColor(Color.TRANSPARENT);
    }

    private void showDefaultPicture(){
        defaultPictureIsOn = true;
        pictureImageview.setImageBitmap(null);
        pictureImageview.setBackground(
                AppCompatResources.getDrawable(context, R.drawable.round_green_light)
        );
        pictureImageview.setImageDrawable(
                AppCompatResources.getDrawable(context, R.drawable.ic_round_music_note_256)
        );
    }


    private void setActions(){
        playIButton.setOnPressUpAction(this::playOrPause);
        nextIButton.setOnPressUpAction(this::playNext);
        previousIButton.setOnPressUpAction(this::playPrevious);
        volumeIButton.setOnPressUpAction(this::setVolumeOnOrOff);
        shuffleIButton.setOnPressUpAction(this::setShuffleOnOrOff);
        favoriteImage.setOnPressUpAction(this::setFavoriteOnOrOff);
        seekbarHandler.postDelayed(getUpdateTrackTimeRunner(), 100);
        Track.canCreateMedia = this;
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Track.setCurrentTime(seekBar.getProgress());
            }
        });
    }


    private Runnable getUpdateTrackTimeRunner(){
        return new Runnable() {
            @Override
            public void run() {
                if (isAlive){
                    if (isPlay){
                        int currentTime = Track.targetTrack.getCurrentTime();
                        currentTimeTextView.setText(timeFormatter.format(currentTime));  // update current time.
                        seekbar.setProgress(currentTime);  // update seekbar position.
                    }
                    seekbarHandler.postDelayed(this, 100);
                }
            }
        };
    }

    public void playOrPause() {
        if (isPlay)
            pause();
        else
            play();
    }


    private void play(){
        Track.play(context, seekbar.getProgress());
        isPlay = iconViewer.showPauseIcon();
    }


    private void pause(){
        Track.pause();
        isPlay = iconViewer.showPlayIcon();
    }


    private void setVolumeOnOrOff(){
        if (isVolumeOn)
            setVolumeOff();
        else
            setVolumeOn();
    }


    private void setVolumeOn(){
        Track.setVolumeOn();
        isVolumeOn = iconViewer.showVolumeOn();
    }


    private void setVolumeOff(){
        Track.setVolumeOff();
        isVolumeOn = iconViewer.showVolumeOff();
    }


    private void setShuffleOnOrOff(){
        if(isShuffleOn)
            setShuffleOff();
        else
            setShuffleOn();
    }


    private void setShuffleOn(){
        Track.isShuffleOn = true;
        isShuffleOn = iconViewer.showShuffleOn();
    }


    private void  setShuffleOff(){
        Track.isShuffleOn = false;
        isShuffleOn = iconViewer.showShuffleOff();
    }


    private void playNext(){
        Track.playNext(context, isPlay);
        updateTrackDetails();
    }


    private void playPrevious(){
        Track.playPrevious(context, isPlay);
        updateTrackDetails();
    }

    private void setFavoriteOnOrOff(){
        if (isFavoriteOn)
            setFavoriteOff();
        else
            setFavoriteOn();
    }

    private void setFavoriteOn(){
        isFavoriteOn = iconViewer.showFavoriteOn();
        Track.targetTrack.isFavorite = true;
        favoritePlayListManager.addToFavorite(Track.targetTrack);
    }

    private void setFavoriteOff(){
        isFavoriteOn = iconViewer.showFavoriteOff();
        Track.targetTrack.isFavorite = false;
        favoritePlayListManager.removeFromFavorite(Track.targetTrack);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().putExtra("id", Track.targetTrack.id));
        super.onBackPressed();
        Track.stop();
        isAlive = false;
        finish();
    }

    @Override
    public void onTrackCompletion() {
        // update track details.
        updateTrackDetails();
    }

}