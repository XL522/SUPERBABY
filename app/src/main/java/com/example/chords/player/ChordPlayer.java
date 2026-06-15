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

    private Context context;

    public ChordPlayer(Context context) {

        this.context = context;

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

        String resourceName =
                ChordResourceManager
                        .getResourceName(chord);

        int resId =
                context.getResources()
                        .getIdentifier(
                                resourceName,
                                "raw",
                                context.getPackageName()
                        );

        if (resId == 0) {

            Log.e(
                    "ChordPlayer",
                    "找不到资源："
                            + resourceName
            );

            return;
        }

        int soundId =
                soundPool.load(
                        context,
                        resId,
                        1
                );

        soundPool.setOnLoadCompleteListener(
                (pool, id, status) -> {

                    if (id == soundId) {

                        pool.play(
                                soundId,
                                1,
                                1,
                                0,
                                0,
                                1
                        );
                    }
                }
        );
    }

    public void stop() {
    }

    public void release() {
        soundPool.release();
    }
}