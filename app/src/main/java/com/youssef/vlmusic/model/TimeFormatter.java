package com.youssef.vlmusic.model;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeFormatter {

    public String format(int duration){
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        return String.format(
                Locale.getDefault(),
                "%d:%02d",
                minutes,
                (TimeUnit.MILLISECONDS.toSeconds(duration) -TimeUnit.MINUTES.toSeconds(minutes))
        );
    }

}
