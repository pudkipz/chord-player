package com.example.chordplayer.util;

import com.leff.midi.MidiTrack;

/**
 * Adds features to Track through delegation.
 */
public class MidiHandler {

    private final int DEFAULT_VELOCITY = 60;

    private MidiTrack midiTrack;
    private MidiAdapter adapter;

    /**
     * Initializes with Track track.
     */
    public MidiHandler() {
        init();
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
    }

    /**
     * Initializes the track.
     */
    private void init() {
        midiTrack = new MidiTrack();
        adapter = new MidiAdapter();

        insertChord(Notes.C, 0, 1000, Chords.MAJOR);
        insertChord(Notes.G, 1000, 1000, Chords.MAJOR);
        insertChord(Notes.A, 2000, 1000, Chords.MINOR);
        insertChord(Notes.F, 3000, 1000, Chords.MAJOR);


        //midiTrack.insertNote(0, Notes.C, 60, 480, 120);
        //midiTrack.insertNote(0, Notes.G, 60, 480 * 2, 120);
    }
}
