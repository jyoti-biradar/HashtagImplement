package com.example.hashtagimplementation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycleViewAdapter extends RecyclerView.Adapter<recycleViewAdapter.ViewHolder> {
    ArrayList<String> messageList;
    public recycleViewAdapter(ArrayList<String> messageList){
        this.messageList = messageList;

    }

    public void setItems(ArrayList<String> messageList){
        this.messageList = messageList;
    }
    @NonNull
    @Override
    public recycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.card_content_display,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull recycleViewAdapter.ViewHolder holder, int position) {
        String message =  messageList.get(position);
        holder.tvTagContent.setText(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTagContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTagContent=(TextView)itemView.findViewById(R.id.tvTagContent);

        }
    }
}
