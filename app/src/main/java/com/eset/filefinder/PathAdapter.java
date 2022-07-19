package com.eset.filefinder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PathAdapter extends RecyclerView.Adapter<PathViewHolder>{
    List<String> items;

    public PathAdapter(List<String> items)
    {
        this.items = items;
    }

    @NonNull
    @Override
    public PathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_item, parent, false);
        return new PathViewHolder(view).linkPathAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull PathViewHolder holder, int position) {
        holder.textView.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class PathViewHolder extends RecyclerView.ViewHolder
{
    public TextView textView;
    private PathAdapter adapter;

    public PathViewHolder(View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.txt_path);
        itemView.findViewById(R.id.ibtn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.items.remove(getBindingAdapterPosition());
                adapter.notifyItemRemoved(getBindingAdapterPosition());
            }
        });
    }

    public PathViewHolder linkPathAdapter(PathAdapter adapter){
        this.adapter = adapter;
        return this;
    }
}