package com.myapps.sdr.firebase_multiupload;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UploadList extends RecyclerView.Adapter<UploadList.ViewHolder> {
    public List<String> filenames;
    public List<String> filedone;
    public UploadList(List<String> filenames,List<String> filedone)
    {
        this.filenames = filenames;
        this.filedone = filedone;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filenameset = filenames.get(position);
        holder.filename.setText(filenameset);
    }

    @Override
    public int getItemCount() {
        return filenames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView filename;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
           view = itemView;
           filename = view.findViewById(R.id.textView);

        }
    }
}
