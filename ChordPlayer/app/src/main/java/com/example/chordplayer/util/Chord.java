package com.example.chordplayer.util;

public class Chord {

    public static final int[] MAJOR = {0, 4, 7};
    public static final int[] MINOR = {0, 3, 7};
    private final Note root;
    private final int color; // 0 = major, 1 = minor

    public Chord(Note root, int color) {
        this.root = root;
        this.color = color;
    }

    public Chord(Note root, int[] chord) {
        this.root = root;
        if (chord.equals(MAJOR)) {
            color = 0;
        } else if (chord.equals(MINOR)) {
            color = 1;
        } else {
            color = -1;
        }
    }

    public Chord(int root, int[] chord) {
        this(Note.getNote(root), chord);
    }

    public String getName() {
        if (color == 0) {
            return (root.name());
        } else if (color == 1) {
            return (root.name() + "m");
        }

        return "?";
    }
}
