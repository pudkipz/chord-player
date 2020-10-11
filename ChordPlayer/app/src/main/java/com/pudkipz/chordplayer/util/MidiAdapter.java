package com.pudkipz.chordplayer.util;

import android.util.Log;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.util.MidiEventListener;
import com.leff.midi.util.MidiProcessor;

import org.billthefarmer.mididriver.MidiDriver;

/**
 * Contains functionality to convert between events from the midi library to the mididriver library
 * (making it possible to actually play midi events).
 */
public class MidiAdapter implements MidiEventListener, MidiDriver.OnMidiStartListener {

    private static final byte NOTE_ON = (byte) 0x90;
    private static final byte NOTE_OFF = (byte) 0x80;

    private MidiDriver midiDriver;
    private MidiProcessor midiProcessor;

    public MidiAdapter() {

        midiDriver = new MidiDriver();
        midiProcessor = new MidiProcessor(new MidiFile());

        midiDriver.setVolume(80);
        //midiDriver.setOnMidiStartListener(this);
    }


    /**
     * Plays the provided track.
     *
     * @param midiTrack
     */
    public void playTrack(MidiTrack midiTrack, MidiTrack tempoTrack) {

        midiDriver.start();

        System.out.println(midiTrack.getEvents().toString());
        MidiFile midiFile = new MidiFile(MidiFile.DEFAULT_RESOLUTION);
        midiFile.addTrack(midiTrack);
        midiFile.addTrack(tempoTrack);

        midiProcessor = new MidiProcessor(midiFile);
        midiProcessor.registerEventListener(this, MidiEvent.class);
        midiProcessor.start();
    }

// leff midi

    public boolean isPlaying() {
        return midiProcessor.isRunning();
    }

    public void stop() {
        midiProcessor.stop();
        midiProcessor.reset();
    }

    @Override
    public void onStart(boolean fromBeginning) {

    }

    @Override
    public void onEvent(MidiEvent event, long ms) {
        midiDriver.write(eventToByteArray(event));
    }

    @Override
    public void onStop(boolean finished) {
        midiDriver.stop();
    }

    /**
     * Method to convert from a MidiEvent used by the Midi library to a byte array, used by
     * MidiDriver.
     *
     * Due to how events are treated in MidiDriver, information about the timestamp and length are
     * lost. This is, however, not a problem and can be taken account for in other ways.
     *
     * @param event event to be converted.
     * @return byte array with relevant information.
     */
    private byte[] eventToByteArray(MidiEvent event) {
        byte[] b = new byte[3];
        ;

        if (event instanceof NoteOn) {
            NoteOn e = (NoteOn) event;

            b[0] = NOTE_ON;
            b[1] = (byte) e.getNoteValue();
            b[2] = (byte) e.getVelocity();
        } else if (event instanceof NoteOff) {
            NoteOff e = (NoteOff) event;
            b[0] = NOTE_OFF;
            b[1] = (byte) e.getNoteValue();
            b[2] = (byte) 0x00;
        }

        return b;
    }

// midi driver

    @Override
    public void onMidiStart() {
        Log.d(this.getClass().getName(), "onMidiStart()");
    }
}
