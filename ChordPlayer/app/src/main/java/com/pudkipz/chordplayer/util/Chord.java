package com.pudkipz.chordplayer.util;

/**
 * Contains information about its root and whether it's a major or minor chord. Also contains a
 * reference to its corresponding MidiEvents.
 */
public class Chord {

    private Note root;
    private ChordType chordType;

    private int numerator;
    private int denominator;

    public Chord(Note root, ChordType chord, int num, int den) {
        this.numerator = num;
        this.denominator = den;
        this.root = root;
        chordType = chord;
    }

    /**
     * Returns root note + suffix.
     *
     * @return a string representation of the chord.
     */
    @Override
    public String toString() {
        return root.toString() + chordType.getSuffix();
    }

    /**
     * Changes the root note to Note n.
     *
     * @param n note to change root to.
     */
    public void changeRoot(Note n) {
        this.root = n;
    }

    public void changeChordType(ChordType chordType) {
        this.chordType = chordType;
    }

    public void setQuiet() {
        root = null;
    }

    public ChordType getChordType() {
        return chordType; //TODO: defensive copy
    }

    public Note getRoot() {
        return root;
    }

    public void setLength(int num, int den) {
        this.numerator = num;
        this.denominator = den;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public float getLength() {
        return (float) numerator/(float) denominator;
    }
}
