// import java.io.File;

//import javax.sound.midi.*;

/*public class Main {

    private static Sequence sequence;

    public static void main(String[] args) throws Exception {


        System.out.println("midifile begin ");

        try {
            //****  Create a new MIDI sequence with 24 ticks per beat  ****
            sequence = new Sequence(javax.sound.midi.Sequence.PPQ, 24);

            //****  Obtain a MIDI track from the sequence  ****
            ChordTrack track = new ChordTrack(sequence.createTrack());


            // ***  set notes on  ****

            *//*playNote(track, Note.C, 1, 120);
            playNote(track, Note.E, 25, 120);
            playNote(track, Note.G, 20, 120);
            playNote(track, Note.B, 5, 120);*//*

            track.playChord(Notes.C, 1, 60, Chord.MAJOR);
            track.playChord(Notes.E, 61, 60, Chord.MINOR);

            playSequence();


            //****  write the MIDI sequence to a MIDI file  ****
            //File f = new File("midifile.mid");
            //MidiSystem.write(s,1,f);
        } //try
        catch (Exception e) {
            System.out.println("Exception caught " + e.toString());
        } //catch

        System.out.println("midifile end ");
    }

    public static void playSequence() {
        // Play sequence

        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);
            sequencer.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}*/
