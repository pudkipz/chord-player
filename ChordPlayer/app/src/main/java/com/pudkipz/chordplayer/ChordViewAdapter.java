package com.pudkipz.chordplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChordViewAdapter extends RecyclerView.Adapter<ChordViewAdapter.ViewHolder> {

    private final List<BooleanChord> localDataSet;
    static OnChordButtonPressedListener listener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ChordButton chordButton;

        public ViewHolder(View view) {
            super(view);

            chordButton = view.findViewById(R.id.chord_button);
        }

        public ChordButton getChordButton() {
            return chordButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public ChordViewAdapter(List<BooleanChord> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.getTextView().setText(localDataSet[position]);

        // Define click listener for the ViewHolder's View
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                //listener.onChordButtonPressed(chordButton);
                listener.onChordButtonPressed(localDataSet.get(position));
                //viewHolder.getChordButton().toggle();
                localDataSet.get(position).toggle();
                notifyDataSetChanged();
            }
        });

        if (localDataSet.get(position).isSelected()) {
            viewHolder.getChordButton().setSelectedColor();
        } else {
            viewHolder.getChordButton().setDeselectedColor();
        }

        viewHolder.getChordButton().setChord(localDataSet.get(position).getChord());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (localDataSet == null) {
            return 0;
        }

        return localDataSet.size();
    }

    public void listen(OnChordButtonPressedListener l) {
        listener = l;
    }
}

