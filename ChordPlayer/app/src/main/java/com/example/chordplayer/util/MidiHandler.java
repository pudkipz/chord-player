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
        midiTrack = new MidiTrack();

        midiTrack.insertNote(1, 1, 60, 1, 60);
        midiTrack.insertNote(1, 5, 60, 60, 60);

        final int NOTE_COUNT = 80;

        for(int i = 0; i < NOTE_COUNT; i++)
        {
            int channel = 0;
            int pitch = 1 + i;
            int velocity = 100;
            long tick = i * 480;
            long duration = 120;

            midiTrack.insertNote(channel, pitch, velocity, tick, duration);
        }

        adapter = new MidiAdapter();

        // init();
    }

    public int getSize() {
        return midiTrack.getSize();
    }

    public void playTrack() {
        adapter.playTrack(midiTrack);
    }

    /*private static MidiEvent noteOnMessage(Note n, long t) throws InvalidMidiDataException {
        try {
            return noteOnMessage(n.midiValue(), t);
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
        return null;
    }*/

    /*private static MidiEvent noteOffMessage(Note n, long t) throws InvalidMidiDataException {
        try {
            return noteOffMessage(n.midiValue(), t);
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
        return null;
    }*/

/*    private MidiEvent noteOnMessage(int n, long t) throws InvalidMidiDataException {
        try {
            ShortMessage midiMessage = new ShortMessage();
            midiMessage.setMessage(ShortMessage.NOTE_ON, n, 0x60);
            return new MidiEvent(midiMessage, t);
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
        return null;
    }*/

/*    private MidiEvent noteOffMessage(int n, long t) throws InvalidMidiDataException {
        try {
            ShortMessage midiMessage = new ShortMessage();
            midiMessage.setMessage(ShortMessage.NOTE_OFF, n, 0x60);
            return new MidiEvent(midiMessage, t);
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
        return null;
    }*/

    /**
     // * @param track add note to this track
     * @param n     which note to add
     * @param t     when to play the note
     * @param l     length of note
     * @throws InvalidMidiDataException if method calls throw exceptions
     */
    /*private static void playNote(Track track, Note n, long t, long l) throws InvalidMidiDataException {
        try {
            track.add(noteOnMessage(n, t));
            track.add(noteOffMessage(n, t + l));
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
    }*/

    /**
     * @param n midi value for the note to be added
     * @param t when to play the note
     * @param l length of the note
     */
    public void insertNote(int n, long t, long l) {

        //int channel, int pitch, int velocity, long tick, long duration
        midiTrack.insertNote(1, n, DEFAULT_VELOCITY, t, l);
        //adapter.playTrack();
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
     * Presumably does meta stuff to set the ending for the track. Should probably be run before playing the track.
     */
    /*public void setEnd() {
        try {
            MetaMessage mt = new MetaMessage();
            byte[] bet = {}; // empty array
            mt.setMessage(0x2F, bet, 0);
            MidiEvent midiEvent = new MidiEvent(mt, (long) track.ticks());
            track.add(midiEvent);
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/

    /**
     * Initializes the track.
     */
    /*private void init() {
        try {
            //****  General MIDI sysex -- turn on General MIDI sound set  ****
            byte[] b = {(byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7};
            SysexMessage sm = new SysexMessage();
            sm.setMessage(b, 6);
            MidiEvent midiEvent = new MidiEvent(sm, (long) 0);
            track.add(midiEvent);

            //****  set tempo (meta event)  ****
            MetaMessage mt = new MetaMessage();
            byte[] bt = {0x02, (byte) 0x00, 0x00};
            mt.setMessage(0x51, bt, 3);
            midiEvent = new MidiEvent(mt, (long) 0);
            track.add(midiEvent);

            //****  set track name (meta event)  ****
            mt = new MetaMessage();
            String TrackName = new String("midifile track");
            mt.setMessage(0x03, TrackName.getBytes(), TrackName.length());
            midiEvent = new MidiEvent(mt, (long) 0);
            track.add(midiEvent);

            //****  set omni on  ****
            ShortMessage midiMessage = new ShortMessage();
            midiMessage.setMessage(0xB0, 0x7D, 0x00);
            midiEvent = new MidiEvent(midiMessage, (long) 0);
            track.add(midiEvent);

            //****  set poly on  ****
            midiMessage = new ShortMessage();
            midiMessage.setMessage(0xB0, 0x7F, 0x00);
            midiEvent = new MidiEvent(midiMessage, (long) 0);
            track.add(midiEvent);

            //****  set instrument to Piano  ****
            midiMessage = new ShortMessage();
            midiMessage.setMessage(0xC0, 0x00, 0x00);
            midiEvent = new MidiEvent(midiMessage, (long) 0);
            track.add(midiEvent);
        } catch (Exception e) {
            System.out.println(e);
        }*/
}
