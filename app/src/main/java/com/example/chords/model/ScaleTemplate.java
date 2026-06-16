package com.example.chords.model;

import java.util.HashMap;

public class ScaleTemplate {

    // 大调 7级结构（罗马数字 → 音名）
    public static final String[] MAJOR_DEGREES = {
            "c", "d", "e", "f", "g", "a", "b"
    };

    // 升号处理
    private static final HashMap<String, String> SHARP_MAP =
            new HashMap<>();

    static {
        SHARP_MAP.put("cs", "c#");
        SHARP_MAP.put("ds", "d#");
        SHARP_MAP.put("fs", "f#");
        SHARP_MAP.put("gs", "g#");
        SHARP_MAP.put("as", "a#");
    }

    public static String normalize(String note) {
        return SHARP_MAP.getOrDefault(note, note);
    }
}