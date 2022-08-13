package com.pudkipz.chordplayer.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Progression {

    private List<Chord> chordTrack;

    public Progression() {
        chordTrack = new ArrayList<>();
    }

    public Progression(List<Chord> ct) {
        chordTrack = ct;
    }

    public void setChords(List<Chord> ct) {
        chordTrack = new ArrayList<>(ct);
    }

    public void insertChord(Note root, ChordType chordType, int length, int denominator) {
        chordTrack.add(new Chord(root, chordType, length, denominator));
    }

    public void swap(int pos1, int pos2) {
        Collections.swap(chordTrack, pos1, pos2);
    }

    public void removeChord(Chord chordToBeRemoved) {
        chordTrack.remove(chordToBeRemoved);
    }

    /**
     * Removes the most recently added chord.
     */
    public void removeChord() {
        if (!isEmpty()) {
            chordTrack.remove(chordTrack.size() - 1);
        }
    }

    // TODO: is it OK to return the reference?
    public List<Chord> getChordTrack() {
        return chordTrack;
    }

    public boolean isEmpty() {
        return chordTrack.isEmpty();
    }

}
