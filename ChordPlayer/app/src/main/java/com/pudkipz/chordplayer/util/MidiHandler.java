package com.pudkipz.chordplayer.util;

import android.nfc.tech.Ndef;

import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import java.util.ArrayList;
import java.util.List;

/**
 * Acts as the middleman between the UI and implementation.
 */
public class MidiHandler {

    private static int DEFAULT_RESOLUTION = 480; // Constant from MidiFile.
    private static int DEFAULT_BPM = 120;

    private MidiAdapter adapter;
    private List<Chord> chordTrack; // TODO: replace this and midiTrack with your own implementation of a track.
    private ArrayList<MidiHandlerListener> listeners;
    private int bpm;

    /**
     * Initializes with Track track.
     */
    public MidiHandler() {
        init();
    }

    /**
     * Register listener to be notified when the track is changed.
     *
     * @param listener listener to be notified.
     */
    public void register(MidiHandlerListener listener) {
        listeners.add(listener);
    }

    /**
     * Returns a String representation of the track. Should consider changing the name.
     *
     * @return a String representation of the track.
     */
    public String getVisualTrack() {
        StringBuilder chords = new StringBuilder();

        for (Chord c : chordTrack) {
            chords.append(c.toString() + " ");
        }

        return chords.toString();
    }

    private MidiTrack getMidiTrack() {
        MidiTrack midiTrack = new MidiTrack();

        for (Chord c : chordTrack) {
            for (MidiEvent e : c.getMidiEvents()) {
                midiTrack.insertEvent(e);
            }
        }

        return midiTrack;
    }

    private MidiTrack getTempoTrack() {
        MidiTrack tempoTrack = new MidiTrack();
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
        Tempo tempo = new Tempo();
        tempo.setBpm(bpm);
        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

        return tempoTrack;
    }

    /**
     * Plays the current track.
     */
    public void playButtonPressed() {
        if (adapter.isPlaying()) {
            stop();
        } else {
            playTrack();
        }
    }

    /**
     * Nullifies the root of the provided Chord. Effectively, makes it quiet.
     * @param c Chord to be silenced.
     */
    public void removeButtonPressed(Chord c) {
        adapter.stop();

        c.setQuiet();
        notifyUpdateTrack();
    }

    /**
     * Removes the most recently added chord.
     */
    public void removeButtonPressed() {
        adapter.stop();

        if (!chordTrack.isEmpty()) {
            chordTrack.remove(chordTrack.size() - 1);
            notifyUpdateTrack();
        }
    }

    private void playTrack() {
        if (adapter.isPlaying()) {
            adapter.stop();
        } else {
            adapter.setTracks(getMidiTrack(), getTempoTrack());
            adapter.playTrack();
        }
    }

    private void stop() {
        adapter.stop();
    }

    /**
     * Currently obsolete.
     *
     * @param n midi value for the note to be added
     * @param t when to play the note
     * @param l length of the note
     */
    public void insertNote(int n, long t, long l) {
        adapter.stop();

        // int channel, int pitch, int velocity, long tick, long duration
        // midiTrack.insertNote(1, n, DEFAULT_VELOCITY, t, l);
    }

    /**
     * Inserts events into midiTrack and listTrack.
     *
     * @param root  midi value for the root note of the chord
     * @param t     when to play the chord
     * @param l     how long to play the chord
     * @param chord adds notes at the given intervals, counted from root.
     */
    public void insertChord(int root, long t, long l, int[] chord) {
        adapter.stop();
        chordTrack.add(new Chord(root, chord, t, l));
        notifyUpdateTrack();
    }

    public void insertChord(int root, long l, int[] chord) {
        long t = getMidiTrack().getLengthInTicks();
        insertChord(root, t, l, chord);
    }

    /**
     * DEFAULT_RESOLUTION is the length of one beat, so *4 means hold for one bar.
     * @param root r
     * @param chord c
     */
    public void insertChord(int root, int[] chord) {
        insertChord(root, DEFAULT_RESOLUTION * 4, chord);
    }

    public void insertChord(Note root, int[] chord) {
        insertChord(root.getMidiValue(), DEFAULT_RESOLUTION * 4, chord);
    }

    public void editChordButtonPressed(Chord c, Note n, int[] intervals) {
        c.changeRoot(n);
        c.changeIntervals(intervals);
        notifyUpdateTrack();
    }

    private void notifyUpdateTrack() {
        for (MidiHandlerListener l : listeners) {
            l.onUpdateTrack();
        }
    }

    public List<Chord> getChordTrack() {
        return chordTrack;
    }

    public void setBPM(int bpm) {
        this.bpm = bpm;
    }

    public int getBPM() {
        return bpm;
    }

    /**
     * Initializes the track.
     */
    private void init() {
        adapter = new MidiAdapter();
        chordTrack = new ArrayList<>();
        listeners = new ArrayList<>();

        bpm = DEFAULT_BPM;

        insertChord(Note.C.getMidiValue(), Chord.MAJOR);
        insertChord(Note.G.getMidiValue(), Chord.MAJOR);
        insertChord(Note.A.getMidiValue(), Chord.MINOR);
        insertChord(Note.F.getMidiValue(), Chord.MAJOR);
    }
}
