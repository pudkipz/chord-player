package com.pudkipz.chordplayer.util;

import android.util.Log;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.util.MetronomeTick;
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
    private static final byte METRONOME_CHANNEL = (byte) 0xA;
    private static final byte PROGRAM_CHANGE = (byte) 0xC0;
    private static final byte METRONOME_INSTRUMENT = (byte) 0x74;
    private static final byte METRONOME_VELOCITY = (byte) 100; // consider moving to MidiHandler
    private static final byte METRONOME_PITCH = (byte) 50; // consider moving to MidiHandler

    private byte metronomeVelocity = 0; // exists so that metronome can be muted by setting this to 0.

    private MidiDriver midiDriver;
    private MidiProcessor midiProcessor;
    private MidiFile midiFile;
    private boolean shouldBePlaying;

    public MidiAdapter() {

        midiDriver = new MidiDriver();

        shouldBePlaying = false;

        midiFile = new MidiFile(MidiFile.DEFAULT_RESOLUTION);

        // The reason for doing the following is because it causes problems when the n.o. tracks in
        // MidiFile changes after creating the MidiProcessor.
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

    public void toggleMetronome() {
        if (metronomeVelocity == METRONOME_VELOCITY) {
            metronomeVelocity = 0;
        } else {
            metronomeVelocity = METRONOME_VELOCITY;
        }
    }

    public void setMetronomeOn() {
        metronomeVelocity = METRONOME_VELOCITY;
    }

    public void setMetronomeOff() {
        metronomeVelocity = 0;
    }


    // TODO: (Consider renaming.qqq

    /**
     * Starts playback of the current MidiFile.
     */
    public void playTrack() {
        midiProcessor.reset();
        shouldBePlaying = true;
        midiDriver.start();
        midiProcessor.start();
    }

    /**
     * Only called automatically when the track is finished.
     */
    private void loopTrack() {
        midiProcessor.reset();
        midiProcessor.start();
    }

    // TODO: (Consider renaming parameters.)

    /**
     * Changes the current tracks of the MidiFile. The parameter order doesn't actually matter.
     *
     * @param noteTrack  n
     * @param tempoTrack t
     */
    public void setTracks(MidiTrack noteTrack, MidiTrack tempoTrack) {
        clearMidiFile();
        midiFile.addTrack(noteTrack);
        midiFile.addTrack(tempoTrack);
        // System.out.println(midiFile.getTrackCount());
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
        System.out.println("event: " + event);
    }

    @Override
    public void onStop(boolean finished) {
        if (shouldBePlaying) loopTrack();
        // shouldn't have to call midiDriver.stop()
    }

    /**
     * Method to convert from a MidiEvent used by the Midi library to a byte array, used by
     * MidiDriver.
     * <p>
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
        } else if (event instanceof MetronomeTick) {
            MetronomeTick e = (MetronomeTick) event;
            b[0] = (byte) (NOTE_ON | METRONOME_CHANNEL);
            ;
            b[1] = (byte) METRONOME_PITCH;
            b[2] = (byte) metronomeVelocity;
        }

        return b;
    }

// midi driver

    @Override
    public void onMidiStart() {
        Log.d(this.getClass().getName(), "onMidiStart()");

        // Set metronome instrument
        byte[] b = new byte[2];
        b[0] = (byte) (PROGRAM_CHANGE | METRONOME_CHANNEL);
        b[1] = (byte) (METRONOME_INSTRUMENT);
        midiDriver.write(b);
    }
}
