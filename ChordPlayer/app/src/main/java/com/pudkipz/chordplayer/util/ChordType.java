package com.pudkipz.chordplayer.util;

import java.util.Arrays;

public enum ChordType {
    Major(new int[]{0, 4, 7}),
    Minor(new int[]{0, 3, 7});

    private final int[] intervals;

    ChordType(int[] intervals) {
        this.intervals = intervals;
    }

    public int[] getIntervals() {
        return intervals;
    }

    public String getSuffix() {
        switch (this) {
            case Major:
                return "";
            case Minor:
                return "m";
        }

        return "?";
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
