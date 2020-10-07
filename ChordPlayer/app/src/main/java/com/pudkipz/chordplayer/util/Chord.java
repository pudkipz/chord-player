package com.pudkipz.chordplayer.util;

import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about its root and whether it's a major or minor chord. Also contains a
 * reference to its corresponding MidiEvents.
 */
public class Chord {

    public static final int[] MAJOR = {0, 4, 7};
    public static final int[] MINOR = {0, 3, 7};

    private static final int DEFAULT_VELOCITY = 60;

    private Note root;
    private int color; // 0 = major, 1 = minor
    private final List<MidiEvent> midiEvents;

    public Chord(Note root, int color) {
        this.root = root;
        this.color = color;
        midiEvents = new ArrayList<>();
    }

    public Chord(Note root, int[] chord, long t, long l) {
        this.root = root;
        if (chord.equals(MAJOR)) {
            color = 0;
        } else if (chord.equals(MINOR)) {
            color = 1;
        } else {
            color = -1;
        }
        midiEvents = new ArrayList<>();

        for (int i : chord) {
            NoteOn on = new NoteOn(t, 0, root.getMidiValue() + i, DEFAULT_VELOCITY);
            NoteOn off = new NoteOn(t + l, 0, root.getMidiValue() + i, 0);

            midiEvents.add(on);
            midiEvents.add(off);
        }
    }

    public Chord(int root, int[] chord, long t, long l) {
        this(Note.getNote(root), chord, t, l);
    }

    /**
     * Returns a String representation of the chord.
     * @return a string representation of the chord.
     */
    @Override
    public String toString() {
        if (color == 0) {
            return (root.name());
        } else if (color == 1) {
            return (root.name() + "m");
        } else {
            return "?";
        }
    }

    /**
     * Changes the root note to Note n.
     * @param n note to change root to.
     */
    public void changeRoot(Note n) {
        int dN = n.getMidiValue() - root.getMidiValue();
        root = n;

        for (MidiEvent e : midiEvents) {
            ((NoteOn) e).setNoteValue(((NoteOn) e).getNoteValue() + dN);
        }
    }

    public Note getRoot() {
        return root;
    }

    public int[] getColour() {
        if (color == 0) return MAJOR;
        else return MINOR;
    }

    public List<MidiEvent> getMidiEvents() {
        return midiEvents;
    }
}
