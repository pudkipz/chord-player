package com.pudkipz.chordplayer.util;

import java.util.Arrays;
import java.util.Map;

public class ChordType {

private static Map<String, ChordType> chordTypes;

    private final int[] intervals;
    private final String name;
    private final String suffix;

    public ChordType(int[] intervals, String name, String suffix) {
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
        return chordTypes.get(name);
    }

    public static ChordType getChordType(int[] intervals) {
        for (ChordType ct : chordTypes.values()) {
            if (Arrays.equals(ct.intervals, intervals)) {
                return ct;
            }
        }
        return null;
    }

    public static void setMap(Map<String, ChordType> cts) {
        chordTypes = cts;
    }

    public static ChordType[] getArray() {
        ChordType[] cts = chordTypes.values().toArray(new ChordType[]{});
        return cts;
    }
}
