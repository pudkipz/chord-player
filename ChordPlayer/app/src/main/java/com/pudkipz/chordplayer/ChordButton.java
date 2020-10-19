package com.pudkipz.chordplayer;

import android.content.Context;

import com.pudkipz.chordplayer.util.Chord;

public class ChordButton extends androidx.appcompat.widget.AppCompatButton {

    private Chord chord;

    public ChordButton(Context context) {
        super(context);
    }

    public ChordButton(Context context, Chord c) {
        this(context);
        this.chord = c;
        if (c.getRoot() == null) {
            setText("\n");
        } else {
            setText(c.toString() + "\n" + c.getNumerator() + "/" + c.getDenominator());
        }
    }

    public Chord getChord() {
        return chord;
    }
}
