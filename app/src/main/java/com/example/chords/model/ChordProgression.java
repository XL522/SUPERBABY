package com.example.chords.model;

import java.util.ArrayList;
import java.util.List;

public class ChordProgression {

    private List<Chord> chords = new ArrayList<>();

    public void addChord(Chord chord) {
        chords.add(chord);
    }

    public void removeChord(int index) {
        chords.remove(index);
    }

    public List<Chord> getChords() {
        return chords;
    }
}