package com.example.auto_clock;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MyViewHolder> {
    private List<String> list;

    ////////
    public RecylerAdapter(List<String> list){
        this.list = list;
    }
    ////////
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView inTimeTextView;
        public TextView outTimeTextView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }
    ////////
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_view_layout,parent,false );
        MyViewHolder myViewHolder = new MyViewHolder(textView);
        return myViewHolder;
    }
    ////////
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.VersionName.setText(list.get(position));
    }
    ////////
    @Override
    public int getItemCount() {
        return list.size();
    }
    ////////
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView VersionName;
        public MyViewHolder(TextView itemView) {
            super(itemView);
            VersionName = itemView;
        }
    }
}


