package com.example.chordplayer;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.chordplayer.util.MidiHandler;
import com.example.chordplayer.util.MidiHandlerListener;

public class FirstFragment extends Fragment  implements MidiHandlerListener {

    private MidiHandler midiHandler;

    private LinearLayout linearLayout_chords;
    private TextView chordBoxText;

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
                midiHandler.playButtonPressed();
            }
        });

        linearLayout_chords = view.findViewById(R.id.linearLayout_chords);
        chordBoxText = new TextView(this.getContext());
        // chordBoxText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // TODO: ensure that chordBoxText is the size of the parent scrollview. the above apparently doesn'chordBoxText work?
        chordBoxText.setSingleLine(true);
        chordBoxText.setTextSize(24);
        chordBoxText.setGravity(Gravity.CENTER);
        setChordBoxText();

    }

    private void setChordBoxText() {
        chordBoxText.setText(midiHandler.getVisualTrack());
        linearLayout_chords.addView(chordBoxText);
    }

    @Override
    public void onUpdateTrack() {
        setChordBoxText();
    }
}
