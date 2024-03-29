# TODO

The contents of this file are my own personal records of what I'm thinking about while I'm working on this project.
It can also be seen as a messy scrum-wannabe thing.

# The point at which I am currently:

*(This is so that I can quickly recall what I was working on last.)*

I have refactored stuff. I have added code to save and load via json. Saving seems to work. Next, I need to make sure loading works.

## Log of the above:

I have added an interface for saving progressions. Next, I need to actually save stuff and think about loading stuff.

You can now drag and drop the chords!

The metronome is now instead a track with a note-on every quarter note for the duration of the whole track. Seems to
work. The only thing is that I can't seem to make it default to off, but whatever.

Update: the reason seems to be that midiProcessor.reset() doesn't reset the position of the metronome, and I can't
tell whether it's supposed to do that or not. The problem is also that midi processor has a built in metronome, which
I didn't even realize at first, meaning I can't just create a new one every time I restart the playback or something.
I will have to think of another solution.

I added a metronome with a switch button. It's a bit weird that it has reverb, but I didn't manage to turn that off.

Another thing is that the metronome and chords don't seem to be playing in time, but maybe that's just the emulator.
Will have to test on phone.

I attempted to fix some issues that arose with changing to RecyclerView. See commit messages for details.

(This is so I can be more verbose and "session focused" than in my commit messages.)

I decided that a RecycleView was more appropriate and had a tough time implementing it. In the end, though, I think it looks
and works much better, but it needs a bit more polishing. I also feel like I made a lot of bad code design choices. Will have
to look it through. In hindsight, this task was too large and generally badly planned, but to a significant degree, I believe
that this is because I did not know exactly what a RecycleView is and how much time and effort it would take to implement it.

I refactored chord types into a json file. Don't know if it's objectively better... but I like it this way, and it should
make it easier to add more types dynamically.

I refactored things into Meter. Now, the most valuable feature would be having a toggleable metronome. Should be fine to
have it tick every quarter note as a first step. I don't necessarily think that being able to change the time signature
is particularly important, especially not unless you can actually hear it. For example, in terms of a metronome.

# Things that should be done:

- Rename fragment/activity to something more useful.

- Persistence is the key to success.
  - *Operation Saving progressions:*
    - ~~Step 1: Create an appropriate user interface.~~
    - ~~Step 2: Figure out how to convert chord progressions into some saveable format (json?).~~
    ~~- Step 3: Actually save things.~~
  - *Operation Loading (and managing) progressions:*
    - Step 1: Create an appropriate user interface. Probably needs to be more extensive than the saving interface.
      - Step 1a: Create a super simple UI: just enter a filename to load/delete. Don't handle NoSuchFile exceptions.
    - Step 2: Be able to select and load an existing progression from a scrollable list.
    - Step 3: Be able to delete existing progressions.
    - Step 4: Be able to rename existing progressions.
    - Step 5: Be able to copy/duplicate existing progression.
  - *Extracurricular activities:*
    - Exercise 1: Associate attributes such as bpm and instrument with a progression, and save these as well.
    - Exercise 2: Related to ex1, let the user create tags and filter and sort by these.

- Fun with rhythm:
    - ~~Step 1: Create a way to represent the time signature.~~
    - ~~Step 2: Make it possible to divide the bar into beats (only 4/4 for now, so 4 beats.~~
    - ~~Step 3: Make it possible to divide the 1/4 beats into arbitrarily small divisions. Or, maybe have a cap at, for example 1/16.~~
    - Step 4: Make it possible to change the time signature. How should the existing track be affected?
      - Step 4a: Make it possible to change the number of 1/4 beats there are in one bar.
      - Step 4b: Make it possible to change the size of the denominator.
    - Step 5: Make it possible to divide the beat into arbitrary tuplets: triplets, quintuplets, septuples...
      - Half finished. Has support for it, only need to add it to the array METERS in FirstFragment.

- Think of a way to represent silence. Should be possible for now  to simply add a "quiet" ChordType, but the name of the
    chord must also change (to have no root, or in some way show that there won't be anything playing). See *Things to think about*
    for more details.

- Pause playback while changing BPM ~~(I think it's causing crashes sometimes)~~, or change it dynamically. It doesn't
    feel intuitive the way it is currently.

- Create patterns that the chord can be played in. So, multiple hits or arpeggiating the chord, for example.

- The metronome is always on when starting playback, regardless of slider setting.

# Things to think about:

- What is really the reason for using midi? I don't think there is a reason! Honestly, it just makes things complicated.
    I think the initial purpose was to make it more "generic" and stuff, but it doesn't seem helpful. Right now, the acutal
    reason I want to keep it is because I think it's cool, but that's not really a valid reason.



- How should the rest of the track be affected by changing the length of a chord, or removing a chord? I should implement
    some kind of placeholder, or better yet, rests! to handle this. Or, just lengthen the chord before to preserve overall
    track length. That would ensure minimal complexity for the user, and I'm not sure that having a representation for
    is desirable anyway.

- Note may also not need to be an enum. Especially since every 12th note is the same, only an octave off. So
    it might actually help with playing in different octaves and so on. I don't think having notes in an external format
    would be helpful, though.

- How should inversions and playing in different octaves be treated? Fun fact: both B and Cb exist and can be used,
    but are an octave apart. Although with the removal of "flat" button, I don't actually need both anymore. But see
    the above comment for more thought about this.

- Are key signatures something I should take into consideration? Currently, all notes are either natural or flat, which
    doesn't make sense from a music theory perspective, but on the other hand, having key signatures might add unnecessary
    complexity, since it works fine like this. It's just a bit weird.

- How should editing the chord that you're adding supposed to work? I'm not really happy with the current situation. I
    need to find a way that is simple, so I don't think adding chords by means of entering each individual note
    is good. I also need to think about how to integrate having more colours and chord variations. I could
    have another spinner with one option per variation, but might become overly verbose. I should think about the
    possible colours that I want to include to decide whether that is a good idea or not. Regarding flats and sharps,
    maybe I should use key signatures. But I fear that it would add unnecessary complexity.
    - Maybe having a pop up for adding and editing chords could work. That way, it would also be uniform, and I don't
          think that there is much use for being able to change the chord before adding it.

# Things put on ice:

- Make it possible to change the length of chords.
  - I don't think this is necessary.

# Things that aren't relevant anymore:

- Make it possible to decide where to insert a new chord.
  - I don't think that's necessary, now that you can move chords around easily.

- Change the chord box to a vertical ScrollView. The problem is that I'll need some kind of horizontal inner layout, which
    wraps to match parent size. LinearLayout doesn't do this the way I want it to.
  - Not relevant: changed to RecyclerView instead.

- ~~Move chord arrays into a constants file.~~

- ~~Move notes into a constants file, thus removing the Note enum. This should also remove the need for overloading.~~

- ~~Research how removing MidiEvents from a Track works.~~

- I might need a custom class to hold the MidiTrack, so that I can easily access both the visual and internal
    representations of the track, since it's inefficient to have to go through the events and look for NoteOn and NoteOff
    events. This should also make tempo related things easier, although I should also look into how the library treats
    such things. Although I'm still unsure of how to remove chords.

- Instead of having chords as an array of intervals, we could have a chord be an array with, for example, every scale
    degree and an indicator telling whether it is sharp (#), flat (b), unchanged (1), or quiet (0). Cmin7 could then
    look like

        [1, 0, b, 0, 1, 0, b]

    One problem with this approach is if we want to have, for instance, all of b2, 2 and b3. On the other hand, this is
    probably not going to happen.

The next step should be to add functionality for changing the colour of the chord that's being added, and I don't think
    that that will be very difficult. However, there are things to be considered. Currently I only have two colours: major
    and minor. But I do want to add more stuff, and need to think about how to integrate that.

- The playback is a bit jerky right at the end/beginning of the loop. I'm not sure why this happens, but I believe it
    might have to do with stopping and restarting midiDriver in between. But for some reason, it freaks out when I don't
    do that. I should look into it, but it's also fine the way it is right now.

- Fix length when last chord is empty - should it be quiet for 1 bar or go directly to start?

# Things that are finished:

- Make it possible to move chords around (not necessarily with UI). Necessary for ~~removal and~~ insertion of chords.
  (Not actually necessary for removal.)

- There are timing issues in the emulator, regarding the metronome. See if it's an actual problem. If it is, fix.

- Add a toggleable metronome track.

- Fix bugs related to RecyclerView.

- Change the chord view to a RecycleView.

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

- Add some kind of UI.
    - ~~Step 1: being able to play a predetermined track.~~
    - ~~Step 2: being able to add and remove predetermined chords.~~
    - ~~Step 3: having a visual representation of the current track (see another point).~~
    - ~~Step 4: being able to change the root of the chord that's being added.~~
    - ~~Step 5: being able to change the colour of the chord that's being added.~~
    - Step pre 6, 7: make it possible to move chords around (not necessarily with UI). Necessary for ~~removal and~~ insertion of chords. (Not actually necessary for removal.)
    - ~~Step 6: being able to pick which chord to remove.~~
    - Step 7: being able to decide where to insert a new chord.
    - ~~Step 8: being able to change the root and colour of an already existing chord.~~

- Make it possible to change the playback speed.

- Make it possible to loop the track.

- Refactor colour -> intervals.

- It sometimes crashes when pressing play after having stopped.

- Make sure that the names in ChordType and the names showed in the spinner have the same source (same for root note).

- Make it so playback stops when pressing Edit.

- Fix bug regarding ChordType names.

- Fix how chords are removed.

- Fix chord boxes going solidly gray after clicking on them. Alternatively, think of a new way of displaying the chords.

-  Maybe ChordType shouldn't be an enum. I think it would be cool to be able to create custom chord types, so having them
    in a different non code dependent format could be cool. Maybe with the help of json? I should look into it.
