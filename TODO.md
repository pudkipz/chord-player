# TODO

The contents of this file are my own personal records of what I'm thinking about while I'm working on this project.

## Things that should be done:

- ~~Move chord arrays into a constants file.~~
- ~~Move notes into a constants file, thus removing the Note enum. This should also remove the need for overloading.~~
- ~~Research how removing MidiEvents from a Track works.~~
- Refactor things from FirstActivity into some kind of custom MidiDriver with scheduling capabilities. Also refactor
    constants to as it were before Android stuff was implemented.
  - ~~Step 1: I found a suitable library to help me with this.~~
  - ~~Step 2: I have created MidiHandler to act a bit like ChordTrack did.~~
  - Step 3: I have started to create MidiAdapter to act as a middle thing between the midi libraries.
    - To do this, I seem to need to create a midi file, so that MidiProcessor can dispatch events to MidiAdapter.
        This seems stupid. I will look into it.
  - Step 4: Make sure that the timing works as it should.
  - Step 5: Also make this work for chords and not only single notes.
- Add some kind of UI.
    - Step 1: being able to play a predetermined track.
    - Step 2: being able to add and remove predetermined chords.
    - Step 3: being able to change the root of the chord.
    - Step 4: being able to change the colour of the chord.
    
    At this point, a new plan should be made.

## Things that should be considered:

- ~~While researching UI and Android compatibility, I realized that there doesn't seem to be an equivalent of the javax
      midi library. I have decided to instead use Midi Driver, found here: https://github.com/billthefarmer/mididriver.
      However, it seems to only focus on the playing capabilities, and not the ability to create a midi file structure
      thingy (which the other library could). So it seems that I will have to do that myself. I should consider finding
      a more appropriate library for this.~~

- I have found another library to aid me in my quest: https://github.com/LeffelMania/android-midi-lib.
      
- Instead of having chords as an array of intervals, we could have a chord be an array with, for example, every scale
    degree and an indicator telling whether it is sharp (#), flat (b), unchanged (1), or quiet (0). Cmin7 could then
    look like
        
        [1, 0, b, 0, 1, 0, b]
    
    One problem with this approach is if we want to have, for instance, all of b2, 2 and b3. On the other hand, this is
    probably not going to happen.

- How should inversions and playing in different octaves be treated?

- ~~Think of better names for playNote(...) and playChord(...) (since playing isn't what we're doing here).~~