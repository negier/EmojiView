package com.negier.emojiview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class EmojiRecyclerViewAdapter extends RecyclerView.Adapter<EmojiRecyclerViewAdapter.ViewHolder> {
    private List<Emoji> emojiList;

    public EmojiRecyclerViewAdapter(List<Emoji> emojiList) {
        this.emojiList = emojiList;
    }

    @NonNull
    @Override
    public EmojiRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ImageView itemView = (ImageView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_emoji, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiRecyclerViewAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.imageView.setImageResource(emojiList.get(i).getResId());
        if (onItemClickListener != null) {
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(emojiList.get(i));
                }
            });
            viewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(emojiList.get(i), true);
                    return false;
                }
            });
            viewHolder.imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        onItemClickListener.onItemLongClick(emojiList.get(i), false);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(@NonNull ImageView itemView) {
            super(itemView);
            imageView = itemView;
        }
    }

    interface OnItemClickListener {
        void onItemClick(Emoji emoji);

        void onItemLongClick(Emoji emoji, boolean touch);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
