package com.example.chords.player;

import android.os.Handler;

import com.example.chords.model.Chord;
import com.example.chords.model.ChordProgression;

public class LoopPlayer {

    private Handler handler = new Handler();

    private boolean isPlaying = false;

    private int currentIndex = 0;

    private ChordPlayer chordPlayer;

    public LoopPlayer(ChordPlayer chordPlayer) {
        this.chordPlayer = chordPlayer;
    }

    public void start(ChordProgression progression) {

        stop(); // 先停止旧循环

        isPlaying = true;
        currentIndex = 0;

        playNext(progression);
    }

    private void playNext(ChordProgression progression) {

        if (!isPlaying) return;

        if (progression.getChords().size() == 0) return;

        Chord chord =
                progression.getChords().get(currentIndex);

        chordPlayer.playChord(chord);

        currentIndex++;

        if (currentIndex >= progression.getChords().size()) {
            currentIndex = 0;
        }

        handler.postDelayed(
                () -> playNext(progression),
                2000
        );
    }

    public void stop() {
        isPlaying = false;
        handler.removeCallbacksAndMessages(null);
        chordPlayer.stop();
    }
}