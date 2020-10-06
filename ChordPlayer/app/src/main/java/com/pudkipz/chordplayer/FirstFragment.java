package com.pudkipz.chordplayer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pudkipz.chordplayer.util.Chord;
import com.pudkipz.chordplayer.util.MidiHandler;
import com.pudkipz.chordplayer.util.MidiHandlerListener;
import com.pudkipz.chordplayer.util.Note;

public class FirstFragment extends Fragment implements MidiHandlerListener, AdapterView.OnItemSelectedListener {

    private MidiHandler midiHandler;

    private LinearLayout linearLayout_chords;
    private Spinner chordSpinner;
    private Note selectedNote;
    private Spinner colourSpinner;
    private int[] selectedColour;
    private ChordButton selectedChordButton;
    private boolean toggleFlat;

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
        midiHandler.register(this);

        view.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleFlat) {
                    midiHandler.insertChord(selectedNote.getMidiValue() - 1, selectedColour);
                } else {
                    midiHandler.insertChord(selectedNote, selectedColour);
                }
            }
        });

        view.findViewById(R.id.button_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                midiHandler.playButtonPressed();
            }
        });

        view.findViewById(R.id.button_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                midiHandler.removeButtonPressed();
            }
        });

        view.findViewById(R.id.toggleButton_flat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFlat = !toggleFlat;
            }
        });

        view.findViewById(R.id.button_change_chord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleFlat) midiHandler.changeRoot(selectedChordButton.getChord(), Note.getNote(selectedNote.getMidiValue() - 1));
                else midiHandler.changeRoot(selectedChordButton.getChord(), Note.getNote(selectedNote.getMidiValue()));
            }
        });

        chordSpinner = view.findViewById(R.id.spinner_root_note);
        ArrayAdapter<CharSequence> chordSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.chord_spinner, android.R.layout.simple_spinner_item);
        chordSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chordSpinner.setAdapter(chordSpinnerAdapter);
        chordSpinner.setOnItemSelectedListener(this);
        selectedNote = Note.C;

        colourSpinner = view.findViewById(R.id.spinner_colour);
        ArrayAdapter<CharSequence> colourSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.colour_spinner, android.R.layout.simple_spinner_item);
        colourSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colourSpinner.setAdapter(colourSpinnerAdapter);
        colourSpinner.setOnItemSelectedListener(this);
        selectedColour = Chord.MAJOR;


        linearLayout_chords = view.findViewById(R.id.linearLayout_chords);
        onUpdateTrack();
    }

    @Override
    public void onUpdateTrack() {
        linearLayout_chords.removeAllViews();

        for (final Chord c : midiHandler.getChordTrack()) {
            final ChordButton b = new ChordButton(this.getContext(), c);

            b.setBackgroundColor(Color.LTGRAY);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedChordButton != null) //selectedChordButton.setBackgroundColor(Color.LTGRAY);
                    selectedChordButton = b;
                    b.setBackgroundColor(Color.CYAN);

                    // TODO: update spinners to have the values of the selected chord.

                }
            });

            linearLayout_chords.addView(b);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_colour:
                switch ((String) parent.getSelectedItem()) {
                    case "Major":
                        selectedColour = Chord.MAJOR;
                        break;
                    case "Minor":
                        selectedColour = Chord.MINOR;
                        break;
            }
            break;
            case R.id.spinner_root_note:
                selectedNote = Note.valueOf((String) parent.getSelectedItem());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
