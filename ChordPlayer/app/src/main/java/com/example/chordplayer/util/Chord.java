package com.example.chordplayer.util;

import com.leff.midi.event.MidiEvent;

import java.util.ArrayList;
import java.util.List;

public class Chord {

    public static final int[] MAJOR = {0, 4, 7};
    public static final int[] MINOR = {0, 3, 7};
    private final Note root;
    private final int color; // 0 = major, 1 = minor
    private final List<MidiEvent> midiEvents;

    public Chord(Note root, int color) {
        this.root = root;
        this.color = color;
        midiEvents = new ArrayList<>();
    }

    public Chord(Note root, int[] chord) {
        this.root = root;
        if (chord.equals(MAJOR)) {
            color = 0;
        } else if (chord.equals(MINOR)) {
            color = 1;
        } else {
            color = -1;
        }
        midiEvents = new ArrayList<>();
    }

    public Chord(int root, int[] chord) {
        this(Note.getNote(root), chord);
    }

    public String getName() {
        if (color == 0) {
            return (root.name());
        } else if (color == 1) {
            return (root.name() + "m");
        } else {
            return "?";
        }
    }

    public void addEvent(MidiEvent e) {
        midiEvents.add(e);
    }

    public List<MidiEvent> getMidiEvents() {
        return midiEvents;
    }
}