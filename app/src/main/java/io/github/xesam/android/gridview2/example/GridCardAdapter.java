package io.github.xesam.android.gridview2.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.net.chelaile.android.components.R;

public class GridCardAdapter extends RecyclerView.Adapter<GridCardAdapter.GridCardViewHolder> {

    private List<GridItem> items;

    public GridCardAdapter(List<GridItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public GridCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_card, parent, false);
        return new GridCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridCardViewHolder holder, int position) {
        GridItem item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<GridItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class GridCardViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTextView;
        private TextView subtitleTextView;
        private TextView indexTextView;

        public GridCardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            titleTextView = itemView.findViewById(R.id.itemTitle);
            subtitleTextView = itemView.findViewById(R.id.itemSubtitle);
            indexTextView = itemView.findViewById(R.id.itemIndex);
        }

        public void bind(GridItem item, int position) {
            imageView.setImageResource(item.getImageResId());
            titleTextView.setText(item.getTitle());
            subtitleTextView.setText(item.getSubtitle());
            // 显示序号，从1开始
            indexTextView.setText(String.valueOf(position + 1));
        }
    }
}