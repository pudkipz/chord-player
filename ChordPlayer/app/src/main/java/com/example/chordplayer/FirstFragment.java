package com.example.chordplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.chordplayer.util.ChordTrack;

import org.billthefarmer.mididriver.MidiDriver;

public class FirstFragment extends Fragment implements MidiDriver.OnMidiStartListener {

    //ChordTrack track = new ChordTrack();
    MidiDriver midiDriver;

    /**
     * Command value for Note Off message (0x80, or 128).
     */
    public static final byte NOTE_OFF = (byte) 0x80;  // 128

    /**
     * Command value for Note On message (0x90, or 144).
     */
    public static final byte NOTE_ON = (byte) 0x90;  // 144

    public static final int DEFAULT_VELOCITY = 0x60;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        midiDriver = new MidiDriver();
        midiDriver.setOnMidiStartListener(this);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote(0x3C, 1, 6);
            }
        });
    }

    private void noteSequence() {

    }

    private void noteOnMessage(int n, int t, int l) {
        byte[] event = new byte[4];
        event[0] = (byte) (NOTE_ON | 0x00);  // 0x90 = note On, 0x00 = channel 1
        event[1] = (byte) n;
        event[2] = (byte) DEFAULT_VELOCITY;  // 0x7F = the maximum velocity (127)
        event[3] = (byte) l; // hopefully time?

        // Internally this just calls write() and can be considered obsoleted:
        //midiDriver.queueEvent(event);

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);
    }

    private void noteOffMessage(int n, int t, int l) {
        byte[] event = new byte[4];
        event[0] = (byte) (NOTE_OFF | 0x00);  // 0x90 = note On, 0x00 = channel 1
        event[1] = (byte) n;
        event[2] = (byte) DEFAULT_VELOCITY;  // 0x7F = the maximum velocity (127)
        event[3] = (byte) (t + l);

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

        //noteOffMessage(n, t, l);

    }

    @Override
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
    }

    @Override
    public void onPause() {
        super.onPause();
        midiDriver.stop();
    }

    @Override
    public void onMidiStart() {
        Log.d(this.getClass().getName(), "onMidiStart()");
    }
}
