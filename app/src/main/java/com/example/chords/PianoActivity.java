package com.example.chords;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PianoActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private final int[] soundIds = new int[12];
    private PianoView pianoView;

    private static final int[] WHITE_KEY_RES = {
            0,              // C
            R.raw.maj_a_d,  // D
            R.raw.maj_a_e,  // E
            0,              // F
            0,              // G
            R.raw.maj_a_a,  // A
            R.raw.maj_a_b   // B
    };

    private static final int[] BLACK_KEY_RES = {
            R.raw.maj_a_cs, // C#
            0,              // D#
            R.raw.maj_a_fs, // F#
            R.raw.maj_a_gs, // G#
            0               // A#
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piano);

        pianoView = findViewById(R.id.pianoView);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        initSoundPool();
        loadSounds();

        pianoView.setKeyListener(new PianoView.OnKeyListener() {
            @Override
            public void onKeyDown(int keyIndex, boolean isBlack) {
                int soundId = isBlack ? soundIds[7 + keyIndex] : soundIds[keyIndex];
                if (soundId != 0 && soundPool != null) {
                    soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }

            @Override
            public void onKeyUp(int keyIndex, boolean isBlack) {
                // 先不处理松键
            }
        });

        Intent intent = getIntent();
        String type = intent.getStringExtra("CHORD_TYPE");
        if ("A_MAJOR".equals(type)) {
            boolean[] whiteHighlights = new boolean[7];
            boolean[] blackHighlights = new boolean[5];

            whiteHighlights[5] = true; // A
            whiteHighlights[6] = true; // B
            whiteHighlights[1] = true; // D
            whiteHighlights[2] = true; // E

            blackHighlights[0] = true; // C#
            blackHighlights[2] = true; // F#
            blackHighlights[3] = true; // G#

            pianoView.highlightNotes(whiteHighlights, blackHighlights);
        }
    }

    private void initSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            //noinspection deprecation
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
    }

    private void loadSounds() {
        for (int i = 0; i < WHITE_KEY_RES.length; i++) {
            if (WHITE_KEY_RES[i] != 0) {
                soundIds[i] = soundPool.load(this, WHITE_KEY_RES[i], 1);
            }
        }

        for (int i = 0; i < BLACK_KEY_RES.length; i++) {
            if (BLACK_KEY_RES[i] != 0) {
                soundIds[7 + i] = soundPool.load(this, BLACK_KEY_RES[i], 1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}