package com.example.chordplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.chordplayer.util.MidiHandler;

import org.billthefarmer.mididriver.MidiDriver;

public class FirstFragment extends Fragment {

    /**
     * Command value for Note Off message (0x80, or 128).
     */
    public static final byte NOTE_OFF = (byte) 0x80;  // 128

    /**
     * Command value for Note On message (0x90, or 144).
     */
    public static final byte NOTE_ON = (byte) 0x90;  // 144

    public static final int DEFAULT_VELOCITY = 0x60;

    private MidiHandler midiHandler;

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

        midiHandler = new MidiHandler();

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
                //midiHandler.insertNote(0x3C, 1, 60);
                midiHandler.playTrack();
            }
        });

        /*view.findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                midiHandler.playTrack();
            }
        });*/
    }

    private void noteSequence() {

    }


}
