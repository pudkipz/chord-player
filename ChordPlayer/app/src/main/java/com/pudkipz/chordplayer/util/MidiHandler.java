package com.pudkipz.chordplayer.util;

import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Acts as the middleman between the UI and implementation.
 */
public class MidiHandler {

    private static int DEFAULT_RESOLUTION = 480; // Constant from MidiFile.
    private static int DEFAULT_BPM = 400; // Debug value (very high!).

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
     * Removes provided Chord and moves all subsequent chords.
     * @param removeChord Chord to be removed.
     */
    public void removeButtonPressed(Chord removeChord) {
        adapter.stop();

        List<Chord> copyChordTrack = new ArrayList<>(chordTrack);
        chordTrack.clear();

        for (Chord c : copyChordTrack) {
            System.out.println(Arrays.toString(chordTrack.toArray()));
            if (removeChord != c) {
                insertChord(c);
            }
        }
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
     * @param c c
     */
    public void insertChord(Chord c) {
        adapter.stop();
        long t = getMidiTrack().getLengthInTicks();
        c.setTick(t);
        chordTrack.add(c);
    }

    /**
     * Inserts events into midiTrack and listTrack.
     *
     * @param root  midi value for the root note of the chord
     * @param t     when to play the chord
     * @param l     how long to play the chord
     * @param chordType adds notes at the given intervals, counted from root.
     */
    public void insertChord(Note root, long t, long l, ChordType chordType) {
        adapter.stop();
        chordTrack.add(new Chord(root, chordType, t, l));
        notifyUpdateTrack();
    }

    public void insertChord(Note root, long l, ChordType chordType) {
        long t = getMidiTrack().getLengthInTicks();
        insertChord(root, t, l, chordType);
    }

    /**
     * DEFAULT_RESOLUTION is the length of one beat, so *4 means hold for one bar.
     * @param root r
     * @param chord c
     */
    public void insertChord(Note root, ChordType chord) {
        insertChord(root, DEFAULT_RESOLUTION * 4, chord);
    }

    public void editChordButtonPressed(Chord c, Note n, ChordType chordType) {
        adapter.stop();
        c.changeRoot(n);
        c.changeChordType(chordType);
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

        insertChord(Note.C, ChordType.Major);
        insertChord(Note.G, ChordType.Major);
        insertChord(Note.A, ChordType.Minor);
        insertChord(Note.F, ChordType.Major);
    }
}
