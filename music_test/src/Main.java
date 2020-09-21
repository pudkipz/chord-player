// import java.io.File;

import javax.sound.midi.*;

public class Main {

    // public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public static final int[] MAJOR_CHORD = {0, 4, 7};
    public static final int[] MINOR_CHORD = {0, 3, 7};


    public static void main(String[] args) throws Exception {

        Sequence sequence;
        System.out.println("midifile begin ");
        try {
            //****  Create a new MIDI sequence with 24 ticks per beat  ****
            sequence = new Sequence(javax.sound.midi.Sequence.PPQ, 24);

            //****  Obtain a MIDI track from the sequence  ****
            Track track = sequence.createTrack();

            //****  General MIDI sysex -- turn on General MIDI sound set  ****
            byte[] b = {(byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7};
            SysexMessage sm = new SysexMessage();
            sm.setMessage(b, 6);
            MidiEvent midiEvent = new MidiEvent(sm, (long) 0);
            track.add(midiEvent);

            //****  set tempo (meta event)  ****
            MetaMessage mt = new MetaMessage();
            byte[] bt = {0x02, (byte) 0x00, 0x00};
            mt.setMessage(0x51, bt, 3);
            midiEvent = new MidiEvent(mt, (long) 0);
            track.add(midiEvent);

            //****  set track name (meta event)  ****
            mt = new MetaMessage();
            String TrackName = new String("midifile track");
            mt.setMessage(0x03, TrackName.getBytes(), TrackName.length());
            midiEvent = new MidiEvent(mt, (long) 0);
            track.add(midiEvent);

            //****  set omni on  ****
            ShortMessage midiMessage = new ShortMessage();
            midiMessage.setMessage(0xB0, 0x7D, 0x00);
            midiEvent = new MidiEvent(midiMessage, (long) 0);
            track.add(midiEvent);

            //****  set poly on  ****
            midiMessage = new ShortMessage();
            midiMessage.setMessage(0xB0, 0x7F, 0x00);
            midiEvent = new MidiEvent(midiMessage, (long) 0);
            track.add(midiEvent);

            //****  set instrument to Piano  ****
            midiMessage = new ShortMessage();
            midiMessage.setMessage(0xC0, 0x00, 0x00);
            midiEvent = new MidiEvent(midiMessage, (long) 0);
            track.add(midiEvent);


            // ***  set notes on  ****

            /*playNote(track, Note.C, 1, 120);
            playNote(track, Note.E, 25, 120);
            playNote(track, Note.G, 20, 120);
            playNote(track, Note.B, 5, 120);*/

            playChord(track, Note.C, 1, 60, MAJOR_CHORD);
            playChord(track, Note.C, 61, 60, MINOR_CHORD);


            //****  set end of track (meta event) 19 ticks later  ****
            mt = new MetaMessage();
            byte[] bet = {}; // empty array
            mt.setMessage(0x2F, bet, 0);
            midiEvent = new MidiEvent(mt, (long) 140);
            track.add(midiEvent);

            // Play sequence
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);
            sequencer.start();

            //****  write the MIDI sequence to a MIDI file  ****
            //File f = new File("midifile.mid");
            //MidiSystem.write(s,1,f);
        } //try
        catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        } //catch

        System.out.println("midifile end ");

        System.out.println();

    }

    private static MidiEvent noteOnMessage(Note n, long t) throws InvalidMidiDataException {
        try {
            return noteOnMessage(n.midiValue(), t);
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
        return null;
    }

    private static MidiEvent noteOffMessage(Note n, long t) throws InvalidMidiDataException {
        try {
            return noteOffMessage(n.midiValue(), t);
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
        return null;
    }

    private static MidiEvent noteOnMessage(int n, long t) throws InvalidMidiDataException {
        try {
            ShortMessage midiMessage = new ShortMessage();
            midiMessage.setMessage(ShortMessage.NOTE_ON, n, 0x60);
            return new MidiEvent(midiMessage, t);
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
        return null;
    }

    private static MidiEvent noteOffMessage(int n, long t) throws InvalidMidiDataException {
        try {
            ShortMessage midiMessage = new ShortMessage();
            midiMessage.setMessage(ShortMessage.NOTE_OFF, n, 0x60);
            return new MidiEvent(midiMessage, t);
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
        return null;
    }

    /**
     * @param track add note to this track
     * @param n     which note to add
     * @param t     when to play the note
     * @param l     length of note
     * @throws InvalidMidiDataException if method calls throw exceptions
     */
    private static void playNote(Track track, Note n, long t, long l) throws InvalidMidiDataException {
        try {
            track.add(noteOnMessage(n, t));
            track.add(noteOffMessage(n, t + l));
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
    }

    private static void playNote(Track track, int n, long t, long l) throws InvalidMidiDataException {
        try {
            track.add(noteOnMessage(n, t));
            track.add(noteOffMessage(n, t + l));
        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
    }

    /**
     *
     * @param track
     * @param root
     * @param t
     * @param l
     * @param chord adds notes at the given intervals, counted from root.
     * @throws InvalidMidiDataException
     */
    private static void playChord(Track track, Note root, long t, long l, int[] chord) throws InvalidMidiDataException {
        try {
            for (int i : chord) {
                playNote(track, root.midiValue() + i, t, l);
            }

        } catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        }
    }

}
