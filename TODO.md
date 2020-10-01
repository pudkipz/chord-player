# TODO

The contents of this file are my own personal records of what I'm thinking about while I'm working on this project.
It can also be seen as a messy scrum-wannabe thing.

## The point at which I am currently:

(This is so that I can quickly recall what I was working on last.)

I solved the problem of removing chords by letting Chord hold a list of NoteOn events. It doesn't feel like a
great solution, and I don't know if it's appropriate that Chord is dependent on the library, but maybe that's fine.

## Things that should be done:

- Add some kind of UI.
    - ~~Step 1: being able to play a predetermined track.~~
    - ~~Step 2: being able to add and remove predetermined chords.~~
    - ~~Step 3: having a visual representation of the current track (see another point).~~
    - Step 4: being able to change the root of the chord that's being added.
    - Step 5: being able to change the colour of the chord that's being added.
    - Step 6: being able to pick which chord to remove.
    - Step 7: being able to decide where to insert a new chord.
    - Step 8: being able to change the root and colour of an already existing chord.
    
    At this point, a new plan should be made.

## Things that should be considered:

- Instead of having chords as an array of intervals, we could have a chord be an array with, for example, every scale
    degree and an indicator telling whether it is sharp (#), flat (b), unchanged (1), or quiet (0). Cmin7 could then
    look like
        
        [1, 0, b, 0, 1, 0, b]
    
    One problem with this approach is if we want to have, for instance, all of b2, 2 and b3. On the other hand, this is
    probably not going to happen.

- How should inversions and playing in different octaves be treated?





## Things put on ice:

## Things that aren't relevant anymore:

- ~~Move chord arrays into a constants file.~~

- ~~Move notes into a constants file, thus removing the Note enum. This should also remove the need for overloading.~~

- ~~Research how removing MidiEvents from a Track works.~~

- I might need a custom class to hold the MidiTrack, so that I can easily access both the visual and internal
    representations of the track, since it's inefficient to have to go through the events and look for NoteOn and NoteOff
    events. This should also make tempo related things easier, although I should also look into how the library treats
    such things. Although I'm still unsure of how to remove chords.

## Things that are finished:

- Refactor things from FirstActivity into some kind of custom MidiDriver with scheduling capabilities. Also refactor
    constants to as it were before Android stuff was implemented.
  - Step 1: I found a suitable library to help me with this.
  - Step 2: I have created MidiHandler to act a bit like ChordTrack did.
  - Step 3: I have started to create MidiAdapter to act as a middle thing between the midi libraries.
    - To do this, I seem to need to create a midi file, so that MidiProcessor can dispatch events to MidiAdapter.
        This seems stupid. I will look into it.
  - Step 4: Make sure that the timing works as it should.
  - Step 5: Also make this work for chords and not only single notes.

  - ~~While researching UI and Android compatibility, I realized that there doesn't seem to be an equivalent of the javax
        midi library. I have decided to instead use Midi Driver, found here: https://github.com/billthefarmer/mididriver.
        However, it seems to only focus on the playing capabilities, and not the ability to create a midi file structure
        thingy (which the other library could). So it seems that I will have to do that myself. I should consider finding
        a more appropriate library for this.~~
        - I have found another library to aid me in my quest: https://github.com/LeffelMania/android-midi-lib.

- ~~Think of better names for playNote(...) and playChord(...) (since playing isn't what we're doing here).~~

- Creating a visual representation:
    - Create a layout for this.
    - Find a way to convert from MidiTrack to actual chord names etc.