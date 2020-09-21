# TODO

## Things that should be done:

- Move chord arrays into a constants file.
- Move notes into a constants file, thus removing the Note enum. This should also remove the need for overloading.
- Research how removing MidiEvents from a Track works.
- Add some kind of UI.
    - Step 1: being able to play a predetermined track.
    - Step 2: being able to add and remove predetermined chords.
    - Step 3: being able to change the root of the chord.
    - Step 4: being able to change the colour of the chord.
    
    At this point, a new plan should be made.

## Things that should be considered:

- Instead of having chords as an array of intervals, we could have a chord be an array with, for example, every scale
    degree and an indicator telling whether it is sharp (#), flat (b), unchanged (1), or quiet (0). Cmin7 could then
    look like
        
        `[1, 0, b, 0, 1, 0, b]`
    
    One problem with this approach is if we want to have, for instance, all of b2, 2 and b3. On the other hand, this is
    probably not going to happen.