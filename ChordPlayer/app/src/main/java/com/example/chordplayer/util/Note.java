package com.example.chordplayer.util;

/**
 * Enum for easy access to the midi value of notes and vice versa.
 */

public enum Note {

    Cb(59),
    C(60),
    Db(61),
    D(62),
    Eb(63),
    E(64),
    F(65),
    Gb(66),
    G(67),
    Ab(68),
    A(69),
    Bb(70),
    B(71);

    private final int midiValue;

    Note(int midiValue) {
        this.midiValue = midiValue;
    }

    public int getMidiValue() {
        return midiValue;
    }

    public static Note getNote(int midiValue) {
        for (Note n : Note.values()) {
            if (n.midiValue == midiValue) {
                return n;
            }
        }

        return getNote(midiValue % 12 + 60);
    }
}
