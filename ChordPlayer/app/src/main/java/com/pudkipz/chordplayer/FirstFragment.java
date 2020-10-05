package com.pudkipz.chordplayer;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pudkipz.chordplayer.util.Chord;
import com.pudkipz.chordplayer.util.MidiHandler;
import com.pudkipz.chordplayer.util.MidiHandlerListener;
import com.pudkipz.chordplayer.util.Note;

public class FirstFragment extends Fragment implements MidiHandlerListener, AdapterView.OnItemSelectedListener {

    private MidiHandler midiHandler;

    private LinearLayout linearLayout_chords;
    private TextView chordBoxText;
    private Spinner chordSpinner;
    private Note selectedNote;
    private Spinner colourSpinner;
    private int[] selectedColour;
    // private ToggleButton toggleFlat;
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
        chordBoxText = new TextView(this.getContext());
        // chordBoxText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // TODO: ensure that chordBoxText is the size of the parent scrollview. the above apparently doesn't work?
        chordBoxText.setSingleLine(true);
        chordBoxText.setTextSize(24);
        chordBoxText.setGravity(Gravity.CENTER);
        linearLayout_chords.addView(chordBoxText);
        setChordBoxText();

    }

    private void setChordBoxText() {
        chordBoxText.setText(midiHandler.getVisualTrack());
    }

    @Override
    public void onUpdateTrack() {
        System.out.println(midiHandler.getVisualTrack());
        setChordBoxText();
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
                // selectedNote = Note.valueOf((String) parent.getItemAtPosition(position));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
