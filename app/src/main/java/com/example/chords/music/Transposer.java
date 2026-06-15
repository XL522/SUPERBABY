package com.example.chords.music;

import com.example.chords.model.Chord;
import com.example.chords.model.ChordProgression;

public class Transposer {
    private static final String[] NOTES = {

            "C",
            "C#",
            "D",
            "D#",
            "E",
            "F",
            "F#",
            "G",
            "G#",
            "A",
            "A#",
            "B"
    };

    public static Chord transpose(
            Chord chord,
            int semitone) {

        String root = chord.getRoot();

        int index = -1;

        for (int i = 0;
             i < NOTES.length;
             i++) {

            if (NOTES[i].equals(root)) {
                index = i;
                break;
            }
        }

        if (index == -1)
            return chord;

        int newIndex =
                (index + semitone + 12)
                        % 12;

        return new Chord(
                NOTES[newIndex],
                chord.getType()
        );
    }

    public static ChordProgression transpose(
            ChordProgression progression,
            int semitone) {

        ChordProgression result =
                new ChordProgression();

        for (Chord chord :
                progression.getChords()) {

            result.addChord(
                    transpose(
                            chord,
                            semitone
                    )
            );
        }

        return result;
    }

}