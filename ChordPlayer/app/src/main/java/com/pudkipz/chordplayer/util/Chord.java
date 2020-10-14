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

    private static final int DEFAULT_VELOCITY = 60;

    private Note root;
    private ChordType chordType;
    private final List<MidiEvent> midiEvents;

    private long t;
    private long l;

    public Chord(Note root, int color) {
        this.root = root;
        midiEvents = new ArrayList<>();
    }

    public Chord(Note root, ChordType chord, long t, long l) {
        this.t = t;
        this.l = l;
        this.root = root;
        chordType = chord;
        midiEvents = createAndGetEvents();
    }

    private List<MidiEvent> createAndGetEvents() {
        List<MidiEvent> events = new ArrayList<>();

        if (root != null) {
            for (int i : chordType.getIntervals()) {
                NoteOn on = new NoteOn(t, 0, root.getMidiValue() + i, DEFAULT_VELOCITY);
                NoteOn off = new NoteOn(t + l, 0, root.getMidiValue() + i, 0);

                events.add(on);
                events.add(off);
            }
        }

        return events;
    }

    /**
     * Returns root note + suffix.
     * @return a string representation of the chord.
     */
    @Override
    public String toString() {
        return root.toString() + chordType.getSuffix();
    }

    /**
     * Changes the root note to Note n.
     * @param n note to change root to.
     */
    public void changeRoot(Note n) {
        this.root = n;
        midiEvents.clear();
        midiEvents.addAll(createAndGetEvents());
    }

    public void changeChordType(ChordType chordType) {
        this.chordType = chordType;
        midiEvents.clear();
        midiEvents.addAll(createAndGetEvents());
    }

    public void setQuiet() {
        root = null;
        midiEvents.clear();
    }

    public Note getRoot() {
        return root;
    }

    public List<MidiEvent> getMidiEvents() {
        return midiEvents;
    }
}
