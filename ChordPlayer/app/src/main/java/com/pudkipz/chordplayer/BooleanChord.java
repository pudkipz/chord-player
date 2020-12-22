package com.pudkipz.chordplayer;

import com.pudkipz.chordplayer.util.Chord;

/**
 * The sole purpose of this class is to contain a chord and a boolean to tell the recycler view
 * whether a data value is selected (and should be highlighted) or not. This prevents strange
 * dependencies.
 */
public class BooleanChord {
    private Chord chord;
    private boolean isSelected;

    public BooleanChord(Chord c) {
        chord = c;
    }

    public BooleanChord() {

    }

    public void select() {
        isSelected = true;
    }

    public void deselect() {
        isSelected = false;
    }

    public void toggle() {
        isSelected = !isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Chord getChord() {
        return chord;
    }

    public void setChord(Chord c) {
        chord = c;
    }
}
