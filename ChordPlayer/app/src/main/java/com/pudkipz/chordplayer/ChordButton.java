package com.pudkipz.chordplayer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.pudkipz.chordplayer.util.Chord;

public class ChordButton extends androidx.appcompat.widget.AppCompatButton {

    private Chord chord;

    public ChordButton(Context context) {
        super(context);
        init();
    }

    public ChordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChordButton(Context context, Chord c) {
        this(context);
        this.chord = c;
        updateText();
        init();
    }

    public Chord getChord() {
        return chord;
    }

    private void init() {
        setBackgroundColor(Color.LTGRAY);
    }

    public void setChord(Chord chord) {
        this.chord = chord;
        updateText();
    }

    private void updateText() {
        if (chord.getRoot() == null) {
            setText("\n");
        } else {
            setText(chord.toString() + "\n" + chord.getNumerator() + "/" + chord.getDenominator());
        }
    }
}
