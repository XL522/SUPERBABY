package com.example.chords.music;

import com.example.chords.model.Chord;
import com.example.chords.model.ChordFactory;

public class Transposer {

    private static final String[] KEYS = {

            "C","C#","D","D#","E","F",
            "F#","G","G#","A","A#","B"
    };

    public static Chord transpose(
            Chord chord,
            int semitone,
            String mode,
            int degree) {

        int index = findKeyIndex(
                chord.getDisplayName()
        );

        if (index == -1) {
            return chord;
        }

        int newIndex =
                (index + semitone + 12)
                        % 12;

        return ChordFactory.create(
                KEYS[newIndex],
                mode,
                degree
        );
    }

    private static int findKeyIndex(String key) {

        for (int i = 0;
             i < KEYS.length;
             i++) {

            if (key.contains(KEYS[i])) {
                return i;
            }
        }

        return -1;
    }
}