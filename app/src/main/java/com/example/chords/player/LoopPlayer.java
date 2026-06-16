package com.example.chords.player;

import android.os.Handler;

import android.util.Log;
import com.example.chords.model.Chord;
import com.example.chords.model.ChordProgression;

public class LoopPlayer {

    private Handler handler = new Handler();

    private boolean isPlaying = false;

    private int currentIndex = 0;

    private ChordPlayer chordPlayer;

    private int bpm = 120;
    private int beatsPerChord = 4;

    private Metronome metronome;
    private boolean metronomeEnabled = false;

    public void setMetronome(
            Metronome metronome) {

        this.metronome = metronome;
    }

    public void setMetronomeEnabled(
            boolean enabled) {

        this.metronomeEnabled = enabled;
    }

    public LoopPlayer(ChordPlayer chordPlayer) {
        this.chordPlayer = chordPlayer;
    }

    public void start(ChordProgression progression) {

        stop(); // 先停止旧循环

        isPlaying = true;
        currentIndex = 0;

        if (metronomeEnabled &&
                metronome != null) {
            metronome.start();
        }
        handler.postDelayed(
                () -> playNext(progression),
                0
        );

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
                getChordDuration()
        );
    }

    public void stop() {

        isPlaying = false;

        currentIndex = 0;   // ★重置和弦位置

        handler.removeCallbacksAndMessages(null);

        if (metronome != null) {
            metronome.stop();
        }

        Log.d("LoopPlayer", "停止播放");
    }

    public void setBpm(int bpm) {

        this.bpm = bpm;

        if (metronome != null) {
            metronome.setBpm(bpm);
        }
    }

    public void setBeatsPerChord(int beats) {
        this.beatsPerChord = beats;
    }

    private long getChordDuration() {
        return (60000 / bpm) * beatsPerChord;
    }
}