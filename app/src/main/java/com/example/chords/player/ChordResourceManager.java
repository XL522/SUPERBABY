package com.example.chords.player;

import com.example.chords.model.Chord;

public class ChordResourceManager {
    private static String toFileName(String root) {

        switch (root) {
            case "C#":
                return "cs";

            case "D#":
                return "ds";

            case "F#":
                return "fs";

            case "G#":
                return "gs";

            case "A#":
                return "as";

            default:
                return root.toLowerCase();
        }
    }
    public static String getResourceName(
            Chord chord) {

        String root =
                toFileName(
                        chord.getRoot());

        if ("maj".equals(
                chord.getType())) {

            return "maj_"
                    + root
                    + "_"
                    + root;
        }

        if ("min".equals(
                chord.getType())) {

            return "min_"
                    + root
                    + "_"
                    + root;
        }

        return null;
    }

}
