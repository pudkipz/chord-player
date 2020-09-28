package com.example.chordplayer.util;

import android.util.Log;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.util.MidiEventListener;
import com.leff.midi.util.MidiProcessor;

import org.billthefarmer.mididriver.MidiDriver;

public class MidiAdapter implements MidiEventListener, MidiDriver.OnMidiStartListener {

    private static final byte NOTE_ON = (byte) 0x90;
    private static final byte NOTE_OFF = (byte) 0x80;

    private MidiDriver midiDriver;
    private MidiProcessor midiProcessor;
    //private MidiTrack track;

    public MidiAdapter() {

        midiDriver = new MidiDriver();
        midiProcessor = new MidiProcessor(new MidiFile());

        midiDriver.setVolume(80);
        //midiDriver.setOnMidiStartListener(this);
    }


    /**
     * Plays the provided track.
     * @param track
     */
    public void playTrack(MidiTrack track) {

        midiDriver.start();

        System.out.println(track.getEvents().toString());
        MidiFile midiFile = new MidiFile();
        midiFile.addTrack(track);

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

    private byte[] eventToByteArray(MidiEvent event) {
        byte[] b = new byte[3];;

        if (event.getClass() == NoteOn.class) {
            NoteOn e = (NoteOn) event;

            b[0] = NOTE_ON;
            b[1] = (byte) e.getNoteValue();
            b[2] = (byte) e.getVelocity();
        } else if (event.getClass() == NoteOff.class) {
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
