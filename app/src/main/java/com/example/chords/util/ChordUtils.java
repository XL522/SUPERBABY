package com.example.chords.util;

public class ChordUtils {

    public static String degreeToRoman(int degree) {

        switch (degree) {

            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            default: return "";
        }
    }
}