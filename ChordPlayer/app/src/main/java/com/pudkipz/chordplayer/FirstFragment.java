package com.pudkipz.chordplayer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pudkipz.chordplayer.util.Chord;
import com.pudkipz.chordplayer.util.ChordType;
import com.pudkipz.chordplayer.util.Meter;
import com.pudkipz.chordplayer.util.MidiHandler;
import com.pudkipz.chordplayer.util.MidiHandlerListener;
import com.pudkipz.chordplayer.util.Note;

public class FirstFragment extends Fragment implements MidiHandlerListener, AdapterView.OnItemSelectedListener {

    private MidiHandler midiHandler;

    private Note selectedNote;
    private ChordType selectedChordType;
    private int selectedLength;
    private int selectedChordDenominator;

    private LinearLayout linearLayout_chords;
    private Spinner chordSpinner;
    private Spinner colourSpinner;
    private ChordButton selectedChordButton;
    private EditText setBPM;
    private Spinner chordDenominatorSpinner;
    private Spinner chordLengthSpinner;

    // TODO: move METERS into constants class?
    private final static Meter[] METERS = new Meter[]{
            new Meter(1, 1), new Meter(1, 2), new Meter(1, 4), new Meter(1, 8), new Meter(1, 16)};

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
                midiHandler.insertChord(selectedNote, selectedChordType, selectedLength, selectedChordDenominator);
            }
        });

        view.findViewById(R.id.button_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!setBPM.getText().toString().equals("")) {
                    midiHandler.setBPM(Integer.parseInt(setBPM.getText().toString()));
                }
                midiHandler.playButtonPressed();
            }
        });

        view.findViewById(R.id.button_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedChordButton != null)
                    midiHandler.removeButtonPressed(selectedChordButton.getChord());
            }
        });

        view.findViewById(R.id.button_change_chord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedChordButton != null) {
                    midiHandler.editChordButtonPressed(selectedChordButton.getChord(), Note.getNote(selectedNote.getMidiValue()), selectedChordType, selectedLength, selectedChordDenominator);
                }
            }
        });

        setBPM = view.findViewById(R.id.editTextSetBPM);
        setBPM.setText(String.valueOf(midiHandler.getBPM()));

        chordSpinner = view.findViewById(R.id.spinner_root_note);
        ArrayAdapter<CharSequence> chordSpinnerAdapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, Note.stringValues());
        chordSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chordSpinner.setAdapter(chordSpinnerAdapter);
        chordSpinner.setOnItemSelectedListener(this);
        selectedNote = Note.C;

        colourSpinner = view.findViewById(R.id.spinner_colour);
        ArrayAdapter<CharSequence> colourSpinnerAdapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, ChordType.stringValues());
        colourSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colourSpinner.setAdapter(colourSpinnerAdapter);
        colourSpinnerAdapter.addAll(ChordType.stringValues());
        colourSpinner.setOnItemSelectedListener(this);
        selectedChordType = ChordType.Major;

        chordDenominatorSpinner = view.findViewById(R.id.spinner_chord_denominator);
        ArrayAdapter<Meter> chordDenominatorSpinnerAdapter = new MeterSpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, METERS);
        chordSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chordDenominatorSpinner.setAdapter(chordDenominatorSpinnerAdapter);
        chordDenominatorSpinner.setOnItemSelectedListener(this);

        chordLengthSpinner = view.findViewById(R.id.spinner_length);
        ArrayAdapter<CharSequence> chordLengthSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.chord_lengths, android.R.layout.simple_spinner_item);
        chordSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chordLengthSpinner.setAdapter(chordLengthSpinnerAdapter);
        chordLengthSpinner.setOnItemSelectedListener(this);

        linearLayout_chords = view.findViewById(R.id.linearLayout_chords);
        onUpdateTrack();
    }

    @Override
    public void onUpdateTrack() {
        linearLayout_chords.removeAllViews();
        selectedChordButton = null;

        for (final Chord c : midiHandler.getChordTrack()) {
            final ChordButton b = new ChordButton(this.getContext(), c);
            b.setBackgroundColor(Color.LTGRAY);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedChordButton != null)
                        selectedChordButton.setBackgroundColor(Color.LTGRAY);

                    if (selectedChordButton == b) {
                        selectedChordButton = null;
                    } else {
                        selectedChordButton = b;
                        b.setBackgroundColor(Color.CYAN);
                    }

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
                selectedChordType = ChordType.getChordType((String) parent.getSelectedItem());
                break;

            case R.id.spinner_root_note:
                selectedNote = Note.valueOf((String) parent.getSelectedItem());
                break;

            case R.id.spinner_chord_denominator:
                selectedChordDenominator = ((Meter) parent.getSelectedItem()).getDenominator();
                break;

            case R.id.spinner_length:
                selectedLength = Integer.parseInt((String) parent.getSelectedItem());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
