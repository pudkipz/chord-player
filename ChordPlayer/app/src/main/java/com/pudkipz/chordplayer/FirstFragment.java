package com.pudkipz.chordplayer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private Switch metronomeSwitch;

    // TODO: move METERS into constants class?
    private final static Meter[] METERS = new Meter[]{
            new Meter(1, 1), new Meter(1, 2), new Meter(1, 4), new Meter(1, 8), new Meter(1, 16)};

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setHasOptionsMenu(true);
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

        metronomeSwitch = view.findViewById(R.id.switch_metronome);
        metronomeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) { // enabled
                    midiHandler.setMetronomeOn();
                } else { // disabled
                    midiHandler.setMetronomeOff();
                }
            }
        });

        //linearLayout_chords = view.findViewById(R.id.linearLayout_chords);

        recyclerView = view.findViewById(R.id.chord_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mDataset = new ArrayList<>();
        mAdapter = new ChordViewAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter.listen(this);

        ItemTouchHelper chordViewHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0) {
            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int draggedPosition = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();

                midiHandler.swap(draggedPosition, targetPosition);
                mAdapter.notifyItemMoved(draggedPosition, targetPosition);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        chordViewHelper.attachToRecyclerView(recyclerView);

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
        } else
            selectedChords.add(cb);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case (R.id.action_settings):
                return true;
            case (R.id.action_save):
                onSaveButtonPressed(getView());
                return true;
            case (R.id.action_load):
                onLoadButtonPressed(getView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO: Actually save stuff, using m_text.
    // TODO: Increase padding for the input field? looks a bit stupid when it covers the whole window.

    /**
     * Called when saving a progression. Creates and shows an AlertDialog to enter a name.
     *
     * @param view
     */
    public void onSaveButtonPressed(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.input_save_title);
        builder.setMessage(R.string.input_save_desc);

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
//                ResourceLoader.saveProgression(midiHandler.getProgression(), m_Text, getContext());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // TODO. Possibly turn into a whole fragment? (Manage progressions...)
    public void onLoadButtonPressed(View view) {
        return;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }
}
