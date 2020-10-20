package com.pudkipz.chordplayer.util;

import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import java.util.ArrayList;
import java.util.List;

/**
 * Acts as the middleman between the UI and implementation.
 */
public class MidiHandler {

    private static final int DEFAULT_VELOCITY = 60;
    private static int DEFAULT_RESOLUTION = 480; // Constant from MidiFile.
    private static int DEFAULT_BPM = 100;

    private MidiAdapter adapter;
    private List<Chord> chordTrack; // TODO: replace this and midiTrack with your own implementation of a track.
    private MidiTrack midiTrack;
    private ArrayList<MidiHandlerListener> listeners;
    private TimeSignature timeSignature;
    private MidiTrack tempoTrack;
    private Tempo tempo;

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

    private void clearMidiTrack() {
        midiTrack.getEvents().clear();
    }

    private void buildMidiTrack() {
        if (midiTrack == null) {
            midiTrack = new MidiTrack();
        }

        clearMidiTrack();

        for (Chord c : chordTrack) {
            for (MidiEvent e : buildChord(c)) {
                midiTrack.insertEvent(e);
            }
        }
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
     *
     * @param removeChord Chord to be removed.
     */
    public void removeButtonPressed(Chord removeChord) {
        adapter.stop();
        chordTrack.remove(removeChord);
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
            buildMidiTrack();
            adapter.setTracks(midiTrack, tempoTrack);
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
     * @param root        midi value for the root note of the chord
     * @param chordType   adds notes at the given intervals, counted from root.
     * @param length      how many denominators to play the chord
     * @param denominator the division of the chord
     */
    public void insertChord(Note root, ChordType chordType, int length, int denominator) {
        // TODO: fix descriptions of length and denominator in javadoc.
        adapter.stop();
        chordTrack.add(new Chord(root, chordType, length, denominator));
        buildMidiTrack();
        notifyUpdateTrack();
    }

    /**
     * Holds a chord for 1 bar.
     *
     * @param root  r
     * @param chord c
     */
    public void insertChord(Note root, ChordType chord) {
        insertChord(root, chord, timeSignature.getNumerator(), timeSignature.getRealDenominator());
    }

    private List<MidiEvent> buildChord(Chord c) {
        List<MidiEvent> events = new ArrayList<>();
        long tick = midiTrack.getLengthInTicks();
        long length = (long) (DEFAULT_RESOLUTION * (c.getLength()) * 4);
        // *4 to compensate for DEFAULT_RESOLUTION being a 1/4 note.

        if (c.getRoot() != null) {
            for (int i : c.getChordType().getIntervals()) {
                NoteOn on = new NoteOn(tick, 0, c.getRoot().getMidiValue() + i, DEFAULT_VELOCITY);
                NoteOn off = new NoteOn(tick + length, 0, c.getRoot().getMidiValue() + i, 0);

                events.add(on);
                events.add(off);
            }
        }

        return events;
    }

    public void editChordButtonPressed(Chord c, Note n, ChordType chordType, int length, int denominator) {
        adapter.stop();
        c.changeRoot(n);
        c.changeChordType(chordType);
        c.setLength(length, denominator);
        buildMidiTrack();
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
        tempo.setBpm(bpm);
    }

    public int getBPM() {
        return (int) tempo.getBpm();
    }

    /**
     * Initializes the track.
     */
    private void init() {
        adapter = new MidiAdapter();
        chordTrack = new ArrayList<>();
        listeners = new ArrayList<>();

        timeSignature = new TimeSignature();
        timeSignature.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
        // System.out.println("NUMERATOR " + timeSignature.getNumerator() + " DENOMINATOR " + timeSignature.getDenominatorValue());

        tempoTrack = new MidiTrack();
        tempo = new Tempo();
        tempo.setBpm(DEFAULT_BPM);
        tempoTrack.insertEvent(timeSignature);
        tempoTrack.insertEvent(tempo);

        insertChord(Note.C, ChordType.Major);
        insertChord(Note.G, ChordType.Major);
        insertChord(Note.A, ChordType.Minor);
        insertChord(Note.F, ChordType.Major);

        buildMidiTrack();
    }
}
