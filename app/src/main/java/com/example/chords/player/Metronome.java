package com.example.chords.player;

import android.util.Log;
import android.content.Context;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;

import com.example.chords.R;
public class Metronome {

    private int bpm = 120;
    private boolean isPlaying = false;
    private int currentBeat = 1;

    private Context context;

    private SoundPool soundPool;

    private int highTick;
    private int lowTick;

    private Handler handler = new Handler();

    public Metronome(Context context) {

        this.context = context;

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.LOLLIPOP) {

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .build();

        } else {

            soundPool = new SoundPool(
                    2,
                    AudioManager.STREAM_MUSIC,
                    0
            );
        }

        highTick = soundPool.load(
                context,
                R.raw.tick_high,
                1
        );

        lowTick = soundPool.load(
                context,
                R.raw.tick_low,
                1
        );
    }
    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public void start() {

        if (isPlaying)
            return;

        isPlaying = true;

        currentBeat = 1;

        playBeat();
    }

    public void stop() {

        isPlaying = false;
        currentBeat = 0;
        handler.removeCallbacksAndMessages(null);

        Log.d(
                "Metronome",
                "节拍器停止"
        );
    }
    private void playBeat() {

        if (!isPlaying)
            return;

        Log.d("Metronome",
                "Beat = " + currentBeat);

        if (currentBeat == 1) {

            soundPool.play(
                    highTick,
                    1,1,0,0,1
            );

        } else {

            soundPool.play(
                    lowTick,
                    1,1,0,0,1
            );
        }

        currentBeat++;

        if (currentBeat > 4)
            currentBeat = 1;

        handler.postDelayed(
                this::playBeat,
                60000 / bpm
        );
    }

    public void release() {

        soundPool.release();
    }

}