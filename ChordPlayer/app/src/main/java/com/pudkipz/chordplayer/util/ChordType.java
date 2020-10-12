package com.pudkipz.chordplayer.util;

import java.util.Arrays;

public enum ChordType {
    Major(new int[]{0, 4, 7}, "Major", ""),
    Minor(new int[]{0, 3, 7}, "Minor", "m"),
    Major7(new int[]{0, 4, 7, 11}, "Major 7", "maj7"),
    Minor7(new int[]{0, 3, 7, 10}, "Minor 7", "m7"),
    Dominant7(new int[]{0, 4, 7, 10}, "Dominant 7", "7"),
    Diminished7(new int[]{0, 3, 6, 9}, "Diminished 7", "dim7"),
    HalfDim7(new int[]{0, 3, 6, 10}, "Half dim 7", "m7b5"),
    Augmented7(new int[]{0, 4, 8, 10}, "Augmented 7", "aug7");

    private final int[] intervals;
    private final String name;
    private final String suffix;

    ChordType(int[] intervals, String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
        this.intervals = intervals;
    }

    public int[] getIntervals() {
        return intervals;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getName() {
        return name;
    }

    public static ChordType getChordType(String name) {
        for (ChordType ct : ChordType.values()) {
            if (ct.name.equals(name)) {
                return ct;
            }
        }
        return null;
    }

    public static ChordType getChordType(int[] intervals) {
        for (ChordType ct : ChordType.values()) {
            if (Arrays.equals(ct.intervals, intervals)) {
                return ct;
            }
        }
        return null;
    }
}
