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
    private MidiFile midiFile;
    private boolean shouldBePlaying;

    public MidiAdapter() {

        midiDriver = new MidiDriver();

        shouldBePlaying = false;

        midiFile = new MidiFile(MidiFile.DEFAULT_RESOLUTION);
        midiFile.addTrack(new MidiTrack());
        midiFile.addTrack(new MidiTrack());

        midiProcessor = new MidiProcessor(midiFile);
        midiProcessor.registerEventListener(this, MidiEvent.class);

        midiDriver.setVolume(80);
        midiDriver.setOnMidiStartListener(this); // What is the point of this call?
    }

    private void clearMidiFile() {
        while (!midiFile.getTracks().isEmpty()) {
            midiFile.removeTrack(0);
        }
    }


    /**
     *
     */
    public void playTrack() {
        midiProcessor.reset();
        shouldBePlaying = true;
        midiDriver.start();
        midiProcessor.start();
    }

    public void setTracks(MidiTrack noteTrack, MidiTrack tempoTrack) {
        clearMidiFile();
        midiFile.addTrack(noteTrack);
        midiFile.addTrack(tempoTrack);
        System.out.println(midiFile.getTrackCount());
    }

// leff midi

    public boolean isPlaying() {
        return midiProcessor.isRunning();
    }

    public void stop() {
        shouldBePlaying = false;
        midiProcessor.stop();
        midiProcessor.reset();
        midiDriver.stop();
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
        if (shouldBePlaying) playTrack();
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
