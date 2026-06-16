package com.example.chords.model;

public class ScaleRule {

    // 12个半音系统（简化版）
    public static final String[] NATURAL =
            {"c","d","e","f","g","a","b"};

    // 升降号简化映射
    public static String normalize(String key) {

        switch (key) {

            case "C#": return "cs";
            case "D#": return "ds";
            case "F#": return "fs";
            case "G#": return "gs";
            case "A#": return "as";

            default: return key.toLowerCase();
        }
    }

    // Major 级数（音名映射）
    public static String getNote(String key, int degree) {

        // ⚠️ 简化模型：直接用 C 调模板
        // 后续可以升级为完整十二调循环

        String[] cMajor =
                {"c","d","e","f","g","a","b"};

        return cMajor[degree - 1];
    }
}