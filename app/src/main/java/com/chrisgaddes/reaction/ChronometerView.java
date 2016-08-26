package com.chrisgaddes.reaction;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

public class ChronometerView extends TextView implements Runnable {
    private long startTime = 0L;
    long beginTime = 0L;
    long overallDuration = 0L;
    long warningDuration = 0L;
    boolean isRunning = false;
    private long elapsedSeconds;

    public ChronometerView(Context context) {
        super(context);

        reset();
    }

    public ChronometerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        reset();
    }

    @Override
    public void run() {
        isRunning = true;

        elapsedSeconds = (SystemClock.elapsedRealtime() - startTime + beginTime) / 1000;

        if (elapsedSeconds <= overallDuration) {
            long remainingSeconds = elapsedSeconds;
            long minutes = remainingSeconds / 60;
            long seconds = remainingSeconds - (60 * minutes);

            setText(String.format("%d:%02d", minutes, seconds));

            if (elapsedSeconds >= overallDuration - warningDuration) {
                setTextColor(0xFFFF6600); // orange
            } else {
                setTextColor(Color.WHITE);
            }

            postDelayed(this, 1000);
        } else {
            setText("0:00");
            setTextColor(Color.RED);
            isRunning = false;
        }
    }

    public void reset() {
        startTime = SystemClock.elapsedRealtime();
        setText("--:--");
        setTextColor(Color.WHITE);
    }

    public void setBeginTime(long beginTime) {
        startTime = beginTime;
        setText("--:--");
        setTextColor(Color.WHITE);
    }

    public void stop() {
        removeCallbacks(this);
        isRunning = false;
    }

    public long getCurrentTime() {
        return elapsedSeconds;
    }

    public boolean isRunning() {
        return (isRunning);
    }

    public void setOverallDuration(long overallDuration) {
        this.overallDuration = overallDuration;
    }

    public void setWarningDuration(long warningDuration) {
        this.warningDuration = warningDuration;
    }
}