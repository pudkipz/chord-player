package com.pudkipz.chordplayer.util;

import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;

import java.util.ArrayList;
import java.util.List;

/**
 * Acts as the middleman between the UI and implementation.
 */
public class MidiHandler {

    private final int DEFAULT_VELOCITY = 60;

    private MidiTrack midiTrack;
    private MidiAdapter adapter;
    private List<Chord> chordTrack; // TODO: replace this and midiTrack with your own implementation of a track.
    private ArrayList<MidiHandlerListener> listeners;

    /**
     * Initializes with Track track.
     */
    public MidiHandler() {
        init();
    }

    /**
     * Register listener to be notified when the track is changed.
     *
     * @param listener listener to be notified.
     */
    public void register(MidiHandlerListener listener) {
        listeners.add(listener);
    }

    /**
     * Returns a String representation of the track. Should consider changing the name.
     *
     * @return a String representation of the track.
     */
    public String getVisualTrack() {
        StringBuilder chords = new StringBuilder();

        for (Chord c : chordTrack) {
            chords.append(c.toString() + " ");
        }

        return chords.toString();
    }

    /**
     * Plays the current track.
     */
    public void playButtonPressed() {
        if (adapter.isPlaying()) {
            stop();
        } else {
            playTrack();
        }
    }

    /**
     * Removes the most recently added chord.
     */
    public void removeButtonPressed() {
        adapter.stop();

        if (!chordTrack.isEmpty()) {
            for (MidiEvent e : chordTrack.get(chordTrack.size() - 1).getMidiEvents()) {
                midiTrack.removeEvent(e);
            }

            chordTrack.remove(chordTrack.size() - 1);
            notifyUpdateTrack();
        }
    }

    public int getSize() {
        return midiTrack.getSize();
    }

    private void playTrack() {
        adapter.playTrack(midiTrack);
    }

    public void stop() {
        adapter.stop();
    }

    /**
     * @param n midi value for the note to be added
     * @param t when to play the note
     * @param l length of the note
     */
    public void insertNote(int n, long t, long l) {
        adapter.stop();

        //int channel, int pitch, int velocity, long tick, long duration
        midiTrack.insertNote(1, n, DEFAULT_VELOCITY, t, l);
    }

    /**
     * Inserts events into midiTrack and listTrack.
     *
     * @param root  midi value for the root note of the chord
     * @param t     when to play the chord
     * @param l     how long to play the chord
     * @param chord adds notes at the given intervals, counted from root.
     */
    public void insertChord(int root, long t, long l, int[] chord) {
        adapter.stop();
        Chord c = new Chord(root, chord);

        for (int i : chord) {
            NoteOn on = new NoteOn(t, 0, root + i, DEFAULT_VELOCITY);
            NoteOn off = new NoteOn(t + l, 0, root + i, 0);

            c.addEvent(on);
            c.addEvent(off);

            midiTrack.insertEvent(on);
            midiTrack.insertEvent(off);
        }

        chordTrack.add(c);
        notifyUpdateTrack();
    }

    public void insertChord(int root, long l, int[] chord) {
        long t = midiTrack.getLengthInTicks();
        insertChord(root, t, l, chord);
    }

    public void insertChord(int root, int[] chord) {
        insertChord(root, 1000, chord);
    }

    public void insertChord(Note root, int[] chord) {
        insertChord(root.getMidiValue(), 1000, chord);
    }

    public void changeRoot(Chord c, Note n) {
        c.changeRoot(n);
        notifyUpdateTrack();
    }

    public void changeRoot(Note n) {
        if (!chordTrack.isEmpty())
            changeRoot(chordTrack.get(chordTrack.size() - 1), n);
    }

    private void notifyUpdateTrack() {
        for (MidiHandlerListener l : listeners) {
            l.onUpdateTrack();
        }
    }

    public List<Chord> getChordTrack() {
        return chordTrack;
    }

    /**
     * Initializes the track.
     */
    private void init() {
        midiTrack = new MidiTrack();
        adapter = new MidiAdapter();
        chordTrack = new ArrayList<>();
        listeners = new ArrayList<>();

        insertChord(Note.C.getMidiValue(), Chord.MAJOR);
        insertChord(Note.G.getMidiValue(), Chord.MAJOR);
        insertChord(Note.A.getMidiValue(), Chord.MINOR);
        insertChord(Note.F.getMidiValue(), Chord.MAJOR);


        //midiTrack.insertNote(0, Notes.C, 60, 480, 120);
        //midiTrack.insertNote(0, Notes.G, 60, 480 * 2, 120);
    }
}
