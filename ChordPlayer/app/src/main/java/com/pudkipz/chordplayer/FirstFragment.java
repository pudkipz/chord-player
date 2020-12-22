package com.pudkipz.chordplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pudkipz.chordplayer.util.Chord;
import com.pudkipz.chordplayer.util.ChordType;
import com.pudkipz.chordplayer.util.Meter;
import com.pudkipz.chordplayer.util.MidiHandler;
import com.pudkipz.chordplayer.util.MidiHandlerListener;
import com.pudkipz.chordplayer.util.Note;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements MidiHandlerListener, AdapterView.OnItemSelectedListener, OnChordButtonPressedListener {

    private MidiHandler midiHandler;

    private Note selectedNote;
    private ChordType selectedChordType;
    private int selectedLength;
    private int selectedChordDenominator;

    private RecyclerView recyclerView;
    private ChordViewAdapter mAdapter;
    private List<BooleanChord> mDataset;

    private Spinner chordSpinner;
    private Spinner colourSpinner;
    private List<BooleanChord> selectedChords;
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
                selectedChords.clear();
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
                for (BooleanChord selectedChord : selectedChords) {
                    midiHandler.removeButtonPressed(selectedChord.getChord());
                }
                selectedChords.clear();
            }
        });

        view.findViewById(R.id.button_change_chord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (BooleanChord selectedChord : selectedChords) {
                    midiHandler.editChordButtonPressed(selectedChord.getChord(), Note.getNote(selectedNote.getMidiValue()), selectedChordType, selectedLength, selectedChordDenominator);
                }
                selectedChords.clear();
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
        ArrayAdapter<ChordType> colourSpinnerAdapter = new ChordTypeSpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, ChordType.getArray());
        colourSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colourSpinner.setAdapter(colourSpinnerAdapter);
        colourSpinner.setOnItemSelectedListener(this);
        selectedChordType = (ChordType) colourSpinner.getItemAtPosition(0);

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

        //linearLayout_chords = view.findViewById(R.id.linearLayout_chords);

        recyclerView = view.findViewById(R.id.chord_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mDataset = new ArrayList<>();
        mAdapter = new ChordViewAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter.listen(this);

        selectedChords = new ArrayList<>();
        initDataset();
    }

    private void initDataset() {


        for (final Chord c : midiHandler.getChordTrack()) {
            mDataset.add(new BooleanChord(c));
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateTrack() {
        mDataset.clear();

        for (Chord c : midiHandler.getChordTrack()) {
            mDataset.add(new BooleanChord(c));
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_colour:
                selectedChordType = (ChordType) parent.getSelectedItem();
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

    @Override
    public void onChordButtonPressed(BooleanChord cb) {

            if (cb.isSelected()) {
                selectedChords.remove(cb);
            }
            else
                selectedChords.add(cb);
    }
}
