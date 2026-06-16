package com.example.chords.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chords.R;
import com.example.chords.model.Chord;

import java.util.List;

public class ChordAdapter extends
        RecyclerView.Adapter<
                ChordAdapter.ViewHolder> {

    private List<Chord> chords;

    public ChordAdapter(
            List<Chord> chords) {

        this.chords = chords;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvChord;

        public ViewHolder(
                View itemView) {

            super(itemView);

            tvChord =
                    itemView.findViewById(
                            R.id.tvChord
                    );
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_chord,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            ViewHolder holder,
            int position) {

        holder.tvChord.setText(
                chords.get(position)
                        .getDisplayName()
        );
    }

    @Override
    public int getItemCount() {

        return chords.size();
    }
}