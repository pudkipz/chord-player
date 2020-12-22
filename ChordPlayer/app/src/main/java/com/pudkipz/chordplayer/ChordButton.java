package com.pudkipz.chordplayer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.pudkipz.chordplayer.util.Chord;

public class ChordButton extends androidx.appcompat.widget.AppCompatButton {

    private BooleanChord chord; // TODO: this is a stupid and not constructive name.

    public ChordButton(Context context) {
        super(context);
        init();
    }

    public ChordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChordButton(Context context, BooleanChord c) {
        this(context);
        this.chord = c;
        init();
    }

    public Chord getChord() {
        return chord.getChord();
    }

    private void init() {
        chord = new BooleanChord();
        chord.deselect();
        updateText();
    }

    public void setChord(Chord chord) {
        this.chord.setChord(chord);
        updateText();
    }

    public void toggle() {
        this.chord.toggle();
    }

    public void setSelectedColor() {
        setBackgroundColor(Color.CYAN);
    }

    public void setDeselectedColor() {
        setBackgroundColor(Color.LTGRAY);
    }

    public boolean isSelectedMaybe() {
        return chord.isSelected();
    }

    private void updateText() {
        if (chord.getChord() == null) {
            setText("\n");
        } else {
            setText(chord.getChord().toString() + "\n" + chord.getChord().getNumerator() + "/" + chord.getChord().getDenominator());
        }
    }
}
