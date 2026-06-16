package com.example.chords.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chords.R;

import java.util.List;

public class FavoriteAdapter
        extends RecyclerView.Adapter<
        FavoriteAdapter.ViewHolder> {

    private List<String> data;
    private OnItemClickListener listener;
    public interface OnItemLongClickListener {
        void onLongClick(int position);
    }

    private OnItemLongClickListener longListener;
    public FavoriteAdapter(
            List<String> data,
            OnItemClickListener listener,
            OnItemLongClickListener longListener) {

        this.data = data;
        this.listener = listener;
        this.longListener = longListener;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvItem;

        public ViewHolder(
                View view) {

            super(view);

            tvItem =
                    view.findViewById(
                            R.id.tvItem
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
                        R.layout.item_progression,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            ViewHolder holder,
            int position) {

        String item = data.get(position);

        String[] arr =
                item.split("\\|");

        if (arr.length >= 2) {

            holder.tvItem.setText(

                    "⭐ "
                            + arr[0]
                            + "\n"
                            + arr[1]
                            .replace(",", " - ")
            );

        } else {

            holder.tvItem.setText(
                    item
            );
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onClick(item);
        });
        holder.itemView.setOnLongClickListener(v -> {

            longListener.onLongClick(position);

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public interface OnItemClickListener {
        void onClick(String progression);
    }
}