/**
 * Enum for easy access to the midi value of notes.
 */

public enum Note {

    C  (0x3C),
    Db (0x3D),
    D  (0x3E),
    Eb (0x3F),
    E  (0x40),
    F  (0x41),
    Gb (0x42),
    G  (0x43),
    Ab (0x44),
    A  (0x45),
    Bb (0x46),
    B  (0x47);

    private final int midiValue;

    Note(byte midiValue) {
        this.midiValue = midiValue;
    }

    Note(int midiValue) {
        this.midiValue = midiValue;
    }

    public int midiValue() { return midiValue; }

}
