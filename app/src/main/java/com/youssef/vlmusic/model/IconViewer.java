package com.youssef.vlmusic.model;

import android.content.res.Configuration;

import com.youssef.vlmusic.R;
import com.youssef.vlmusic.model.ControllerView.ControllerImageButton;

public class IconViewer {


    private final ControllerImageButton shuffleIButton;
    private final ControllerImageButton volumeIButton;
    private final ControllerImageButton playIButton;
    private final ControllerImageButton favoriteImage;
    private final boolean isNightModeOn;


    public IconViewer(
            int uiMode,
            ControllerImageButton shuffleIButton,
            ControllerImageButton volumeIButton,
            ControllerImageButton playIButton,
            ControllerImageButton favoriteImage
    ){
        this.shuffleIButton = shuffleIButton;
        this.volumeIButton = volumeIButton;
        this.playIButton = playIButton;
        this.favoriteImage = favoriteImage;
        this.favoriteImage.setTag(false); // initial state is favorite off.
        isNightModeOn =
        (uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    public boolean showShuffleOn(){
        if(isNightModeOn)
            shuffleIButton.setImageResource(R.drawable.ic_round_shuffle_on_night);
        else
            shuffleIButton.setImageResource(R.drawable.ic_round_shuffle_on_light);
        return true;
    }

    public boolean showShuffleOff(){
        if(isNightModeOn)
            shuffleIButton.setImageResource(R.drawable.ic_round_shuffle_off_night);
        else
            shuffleIButton.setImageResource(R.drawable.ic_round_shuffle_off_light);
        return false;
    }

    public boolean showVolumeOn(){
        if(isNightModeOn)
            volumeIButton.setImageResource(R.drawable.ic_round_volume_up_night);
        else
            volumeIButton.setImageResource(R.drawable.ic_round_volume_up_light);
        return true;
    }

    public boolean showVolumeOff(){
        if(isNightModeOn)
            volumeIButton.setImageResource(R.drawable.ic_round_volume_off_night);
        else
            volumeIButton.setImageResource(R.drawable.ic_round_volume_off_light);
        return false;
    }

    public boolean showPlayIcon(){
        if(isNightModeOn)
            playIButton.setImageResource(R.drawable.ic_round_play_arrow_night);
        else
            playIButton.setImageResource(R.drawable.ic_round_play_arrow_light);
        return false;
    }

    public boolean showPauseIcon(){
        if(isNightModeOn)
            playIButton.setImageResource(R.drawable.ic_round_pause_night);
        else
            playIButton.setImageResource(R.drawable.ic_round_pause_light);
        return true;
    }

    public boolean showFavoriteOn(){
        if(isNightModeOn)
            favoriteImage.setImageResource(R.drawable.ic_round_favorite_night);
        else
            favoriteImage.setImageResource(R.drawable.ic_round_favorite_light);
        favoriteImage.setTag(true);
        return true;
    }

    public boolean showFavoriteOff(){
        if(isNightModeOn)
            favoriteImage.setImageResource(R.drawable.ic_round_favorite_border_night);
        else
            favoriteImage.setImageResource(R.drawable.ic_round_favorite_border_light);
        favoriteImage.setTag(false);
        return false;
    }

    public boolean favoriteOnIsShowed(){
        return (boolean) favoriteImage.getTag();
    }

}
