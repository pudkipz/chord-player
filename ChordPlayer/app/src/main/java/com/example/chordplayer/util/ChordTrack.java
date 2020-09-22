package com.example.chordplayer.util;

import android.util.Log;

import org.billthefarmer.mididriver.*;

/**
 * Adds features to Track through delegation.
 */
public class ChordTrack {

    /**
     * Command value for Note Off message (0x80, or 128).
     */
    public static final byte NOTE_OFF = (byte) 0x80;  // 128

    /**
     * Command value for Note On message (0x90, or 144).
     */
    public static final byte NOTE_ON = (byte) 0x90;  // 144

    public static final int DEFAULT_VELOCITY = 0x60;

    private MidiDriver midiDriver;

    /**
     * Initializes with Track track.
     */
    public ChordTrack() {
        // init();
        midiDriver = new MidiDriver();
        midiDriver.start();

        // Get the configuration.
        int[] config = midiDriver.config();

        // Print out the details.
        Log.d(this.getClass().getName(), "maxVoices: " + config[0]);
        Log.d(this.getClass().getName(), "numChannels: " + config[1]);
        Log.d(this.getClass().getName(), "sampleRate: " + config[2]);
        Log.d(this.getClass().getName(), "mixBufferSize: " + config[3]);

    }

    private void noteOnMessage(int n, int t, int l) {
        byte[] event = new byte[3];
        event[0] = (byte) (NOTE_ON | 0x00);  // 0x90 = note On, 0x00 = channel 1
        event[1] = (byte) n;
        event[2] = (byte) DEFAULT_VELOCITY;  // 0x7F = the maximum velocity (127)
        //event[3] = (byte) t; // hopefully time?

        // Internally this just calls write() and can be considered obsoleted:
        //midiDriver.queueEvent(event);

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);
    }

    private void noteOffMessage(int n, int t, int l) {
        byte[] event = new byte[3];
        event[0] = (byte) (NOTE_OFF | 0x00);  // 0x90 = note On, 0x00 = channel 1
        event[1] = (byte) n;
        event[2] = (byte) DEFAULT_VELOCITY;  // 0x7F = the maximum velocity (127)
        //event[3] = (byte) (t + l);

        // Internally this just calls write() and can be considered obsoleted:
        //midiDriver.queueEvent(event);

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);
    }

    /**
     * @param n     midi value for the note to be added
     * @param t     when to play the note
     * @param l     length of the note
     */
    public void playNote(int n, int t, int l) {
        noteOnMessage(n, t, l);
        noteOffMessage(n, t, l);

    }
}
