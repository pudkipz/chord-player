package com.example.chordplayer.util;

import android.util.Log;

import com.leff.midi.MidiFile;
import com.leff.midi.event.ChannelEvent;
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

    public MidiAdapter() {

        midiDriver = new MidiDriver();
        midiProcessor = new MidiProcessor();

        midiProcessor.registerEventListener(this, ChannelEvent.class);
    }

// leff midi

    @Override
    public void onStart(boolean fromBeginning) {
        midiDriver.setOnMidiStartListener(this);
    }

    @Override
    public void onEvent(MidiEvent event, long ms) {
        midiDriver.write(eventToByteArray(event));
    }

    @Override
    public void onStop(boolean finished) {

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

/*    @Override
    public void onResume() {
        super.onResume();
        midiDriver.start();

        // Get the configuration.
        int[] config = midiDriver.config();

        // Print out the details.
        Log.d(this.getClass().getName(), "maxVoices: " + config[0]);
        Log.d(this.getClass().getName(), "numChannels: " + config[1]);
        Log.d(this.getClass().getName(), "sampleRate: " + config[2]);
        Log.d(this.getClass().getName(), "mixBufferSize: " + config[3]);
    }*/

/*    @Override
    public void onPause() {
        super.onPause();
        midiDriver.stop();
    }*/


}
