package com.example.chords.model;

public class ChordFactory {

    public static Chord create(
            String key,
            String mode,
            int degree) {

        String normalizedKey =
                ScaleRule.normalize(key);

        String note =
                ScaleRule.getNote(
                        key,
                        degree
                );

        String audioName =
                buildAudioName(
                        mode,
                        normalizedKey,
                        note
                );

        String displayName =
                buildDisplayName(
                        key,
                        mode,
                        degree
                );

        return new Chord(
                displayName,
                audioName
        );
    }

    private static String buildAudioName(
            String mode,
            String key,
            String note) {

        return mode
                + "_"
                + key
                + "_"
                + note;
    }

    private static String buildDisplayName(
            String key,
            String mode,
            int degree) {

        String[] roman =
                {"I","II","III","IV","V","VI","VII"};

        if ("min".equals(mode)) {
            return key + "m " + roman[degree - 1];
        }

        return key + " " + roman[degree - 1];
    }
}