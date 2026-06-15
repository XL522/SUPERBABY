package com.example.chords.player;

import android.util.Log;

public class Metronome {

    private int bpm = 120;

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public void start() {
        Log.d("Metronome",
                "节拍器开始：" + bpm);
    }

    public void stop() {
        Log.d("Metronome",
                "节拍器停止");
    }
}