package com.example.chords.model;

import java.util.ArrayList;
import java.util.List;

public class ChordProgression {

    private List<Chord> chords = new ArrayList<>();

    public void addChord(Chord chord) {
        chords.add(chord);
    }

    public void removeChord(int index) {
        if (index >= 0 && index < chords.size()) {
            chords.remove(index);
        }
    }

    public void moveChord(int from, int to) {

        if (from < 0 || from >= chords.size()
                || to < 0 || to >= chords.size()) {
            return;
        }

        Chord chord = chords.remove(from);
        chords.add(to, chord);
    }

    public List<Chord> getChords() {
        return chords;
    }

    public void clear() {
        chords.clear();
    }
}