package com.example.chords.model;

public class ChordViewModel {

    public String key;        // C
    public String degree;     // I
    public String name;       // Am

    public Chord chord;

    public ChordViewModel(
            String key,
            String degree,
            Chord chord) {

        this.key = key;
        this.degree = degree;
        this.chord = chord;

        this.name = chord.getDisplayName();
    }

    public String getDisplayText() {

        return key
                + "   "
                + degree
                + "   "
                + name;
    }
}