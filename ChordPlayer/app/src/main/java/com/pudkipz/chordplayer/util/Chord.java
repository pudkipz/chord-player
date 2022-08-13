package com.pudkipz.chordplayer.util;

/**
 * Contains information about its root and whether it's a major or minor chord. Also contains a
 * reference to its corresponding MidiEvents.
 */
public class Chord {

    private Note root;
    private ChordType chordType;

    private Meter meter;

    public Chord(Note root, ChordType chord, int num, int den) {
        meter = new Meter(num, den);
        this.root = root;
        chordType = chord;
    }

    public Chord(int root, ChordType chord, int num, int den) {
        new Chord(Note.getNote(root), chord, num, den);
    }

    public Chord(Note root, ChordType chord, Meter m) {
        meter = m;
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
        meter.setMeter(num, den);
    }

    public int getNumerator() {
        return meter.getNumerator();
    }

    public int getDenominator() {
        return meter.denominator;
    }

    public float getLength() {
        return meter.getValue();
    }

    public String getSuffix() {
        return chordType.getSuffix();
    }

    public int[] getIntervals() {
        return chordType.getIntervals().clone();
    }
}
