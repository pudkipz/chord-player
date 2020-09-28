package com.example.chordplayer.util;

import com.leff.midi.MidiTrack;

import java.util.ArrayList;

/**
 * Adds features to Track through delegation.
 */
public class MidiHandler {

    private final int DEFAULT_VELOCITY = 60;

    private MidiTrack midiTrack;
    private MidiAdapter adapter;
    private ArrayList<Chord> listTrack; // TODO: replace this and midiTrack with your own implementation of a track.

    /**
     * Initializes with Track track.
     */
    public MidiHandler() {
        init();
    }

    public String getVisualTrack() {
        StringBuilder chords = new StringBuilder();

        for (Chord c : listTrack) {
            chords.append(c.getName() + " ");
        }

        return chords.toString();
    }

    public void playButtonPressed() {
        if (adapter.isPlaying()) {
            stop();
        } else {
            playTrack();
        }
    }

    public int getSize() {
        return midiTrack.getSize();
    }

    public void playTrack() {
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

        //int channel, int pitch, int velocity, long tick, long duration
        midiTrack.insertNote(1, n, DEFAULT_VELOCITY, t, l);
    }

    /**
     * @param root  midi value for the root note of the chord
     * @param t     when to play the chord
     * @param l     how long to play the chord
     * @param chord adds notes at the given intervals, counted from root.
     */
    public void insertChord(int root, long t, long l, int[] chord) {
        for (int i : chord) {
            insertNote(root + i, t, l);
        }

        listTrack.add(new Chord(root, chord));
    }

    /**
     * Initializes the track.
     */
    private void init() {
        midiTrack = new MidiTrack();
        adapter = new MidiAdapter();
        listTrack = new ArrayList<>();

        insertChord(Note.C.getMidiValue(), 0, 1000, Chord.MAJOR);
        insertChord(Note.G.getMidiValue(), 1000, 1000, Chord.MAJOR);
        insertChord(Note.A.getMidiValue(), 2000, 1000, Chord.MINOR);
        insertChord(Note.F.getMidiValue(), 3000, 1000, Chord.MAJOR);


        //midiTrack.insertNote(0, Notes.C, 60, 480, 120);
        //midiTrack.insertNote(0, Notes.G, 60, 480 * 2, 120);
    }
}
