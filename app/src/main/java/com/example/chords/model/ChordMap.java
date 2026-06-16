package com.example.chords.model;

import java.util.HashMap;

public class ChordMap {

    private static final HashMap<String, String> map =
            new HashMap<>();

    static {

        // =========================
        // C Major
        // =========================
        map.put("C:maj:1", "maj_c_c");
        map.put("C:maj:2", "maj_c_d");
        map.put("C:maj:3", "maj_c_e");
        map.put("C:maj:4", "maj_c_f");
        map.put("C:maj:5", "maj_c_g");
        map.put("C:maj:6", "maj_c_a");
        map.put("C:maj:7", "maj_c_b");

        // =========================
        // G Major
        // =========================
        map.put("G:maj:1", "maj_g_g");
        map.put("G:maj:2", "maj_g_a");
        map.put("G:maj:3", "maj_g_b");
        map.put("G:maj:4", "maj_g_c");
        map.put("G:maj:5", "maj_g_d");
        map.put("G:maj:6", "maj_g_e");
        map.put("G:maj:7", "maj_g_fs");

        // =========================
        // A Minor
        // =========================
        map.put("A:min:1", "min_a_a");
        map.put("A:min:2", "min_a_b");
        map.put("A:min:3", "min_a_c");
        map.put("A:min:4", "min_a_d");
        map.put("A:min:5", "min_a_e");
        map.put("A:min:6", "min_a_f");
        map.put("A:min:7", "min_a_g");
    }

    public static String get(
            String key,
            String mode,
            int degree) {

        return map.get(
                key + ":" + mode + ":" + degree
        );
    }
}