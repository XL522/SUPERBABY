package com.example.chords.model;

public class Chord {

    private String root;
    private String type;

    public Chord(String root, String type) {
        this.root = root;
        this.type = type;
    }

    public String getRoot() {
        return root;
    }

    public String getType() {
        return type;
    }

    public String getDisplayName() {

        if ("maj".equals(type))
            return root;

        if ("min".equals(type))
            return root + "m";

        return root + type;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}