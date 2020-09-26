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

        //final int NOTE_COUNT = 80;

        /*for(int i = 0; i < NOTE_COUNT; i++)
        {
            int channel = 0;
            int pitch = 1 + i;
            int velocity = 100;
            long tick = i * 480;
            long duration = 120;

            midiTrack.insertNote(channel, pitch, velocity, tick, duration);
        }*/
    }

    public int getSize() {
        return midiTrack.getSize();
    }

    public void playTrack() {
        adapter.playTrack(midiTrack);
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
    public void playChord(int root, long t, long l, int[] chord) {
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


        midiTrack.insertNote(0, 50, 60, 480, 120);
        midiTrack.insertNote(0, 51, 60, 480 * 2, 120);
    }
}
