package com.example.chords.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundManager {

    private SoundPool soundPool;

    public SoundManager() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            soundPool = new SoundPool(
                    10,
                    AudioManager.STREAM_MUSIC,
                    0
            );
        }
    }

    public int load(Context context, int resId) {
        return soundPool.load(context, resId, 1);
    }

    public void play(int soundId) {
        soundPool.play(
                soundId,
                1,
                1,
                0,
                0,
                1
        );
    }

    public void release() {
        soundPool.release();
    }
}