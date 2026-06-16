package com.example.chords.model;

import java.util.ArrayList;
import java.util.List;

public class ChordDatabase {

    private static final String[] KEYS = {

            "c", "cs", "d", "ds", "e", "f",
            "fs", "g", "gs", "a", "as", "b"
    };

    public static List<Chord> generateAllChords() {

        List<Chord> list = new ArrayList<>();

        // ===== MAJOR =====
        for (String key : KEYS) {

            for (int degree = 1; degree <= 7; degree++) {

                Chord chord =
                        ChordFactory.create(
                                key,
                                "maj",
                                degree
                        );

                if (chord != null) {
                    list.add(chord);
                }
            }
        }

        // ===== MINOR =====
        for (String key : KEYS) {

            for (int degree = 1; degree <= 7; degree++) {

                Chord chord =
                        ChordFactory.create(
                                key,
                                "min",
                                degree
                        );

                if (chord != null) {
                    list.add(chord);
                }
            }
        }

        return list;
    }
}