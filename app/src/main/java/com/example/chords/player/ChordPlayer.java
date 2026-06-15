package com.example.chords.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.chords.R;
import com.example.chords.model.Chord;

import java.util.HashMap;
import android.util.Log;
public class ChordPlayer {
    private boolean loaded = false;
    private SoundPool soundPool;

    private HashMap<String, Integer> sounds =
            new HashMap<>();

    public ChordPlayer(Context context) {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.LOLLIPOP) {

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

        sounds.put("C",
                soundPool.load(
                        context,
                        R.raw.maj_c_c,
                        1));

        sounds.put("Dm",
                soundPool.load(
                        context,
                        R.raw.maj_c_d,
                        1));

        sounds.put("Em",
                soundPool.load(
                        context,
                        R.raw.maj_c_e,
                        1));

        sounds.put("F",
                soundPool.load(
                        context,
                        R.raw.maj_c_f,
                        1));

        sounds.put("G",
                soundPool.load(
                        context,
                        R.raw.maj_c_g,
                        1));

        sounds.put("Am",
                soundPool.load(
                        context,
                        R.raw.maj_c_a,
                        1));

        soundPool.setOnLoadCompleteListener(
                (soundPool, sampleId, status) -> {

                    Log.d("ChordPlayer",
                            "加载完成：" + sampleId);

                    loaded = true;
                }
        );
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void playChord(Chord chord) {

        String name = chord.getDisplayName();

        Log.d("ChordPlayer", "播放：" + name);

        Integer soundId = sounds.get(name);

        if (soundId != null) {

            soundPool.play(
                    soundId,
                    1,
                    1,
                    0,
                    0,
                    1
            );

        } else {

            Log.e("ChordPlayer",
                    "找不到音频：" + name);
        }
    }

    public void stop() {
    }

    public void release() {
        soundPool.release();
    }
}