package com.eset.filefinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FileAdapter extends RecyclerView.Adapter<FileViewHolder>{
    List<String> items;

    public FileAdapter(List<String> items)
    {
        this.items = items;
    }

    public void updateData(List<String> updateItems) {
        items.clear();
        items.addAll(updateItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        return new FileViewHolder(view).linkFileAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.textView.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class FileViewHolder extends RecyclerView.ViewHolder
{
    public TextView textView;
    private FileAdapter adapter;

    public FileViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.txt_file);
    }

    public FileViewHolder linkFileAdapter(FileAdapter adapter){
        this.adapter = adapter;
        return this;
    }
}