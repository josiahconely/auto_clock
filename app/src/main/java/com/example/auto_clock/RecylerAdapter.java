package com.example.auto_clock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import java.util.List;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MyViewHolder> {
    private List<LogEntry> list;
    private OnItemClickListener mListener;
    public View view;
    private Context context;
    private static final String MyPREFERENCES = "myPreferences";
    private SharedPreferences sharedpreferences;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        mListener = listener;
    }

    ////////
    public RecylerAdapter(List<LogEntry> list, Context con){

        this.context = con;
        this.list = list;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        System.out.println("Got here2");

    }
    ////////
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView VersionName;
        public Button text1;
        private Context context;
        private static final String MyPREFERENCES = "myPreferences";
        private SharedPreferences sharedpreferences;


        public MyViewHolder(View itemView, final OnItemClickListener listener, Context c) {
            super(itemView);
            text1 = itemView.findViewById(R.id.log_view_item);
            Context context = c;
            sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            System.out.println("Got here1");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                        prefsEditor.putString("Item_to_Edit", Integer.toString(position));
                        prefsEditor.commit();
                        System.out.println("Got here");
                        //records the item in the list to edit
                        System.out.println(sharedpreferences.getString("Item to Edit", ""));
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    ////////
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_view_layout,parent,false );
        MyViewHolder myViewHolder = new MyViewHolder(textView, mListener, context);
        return myViewHolder;
    }
    ////////
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LogEntry current_entry = list.get(position);
        String v = current_entry.get_in().getTime().toString() + "  \n "
                + current_entry.get_out().getTime().toString();
        holder.text1.setText(v);
    }
    ////////
    @Override
    public int getItemCount() {
        return list.size();
    }


    ////////
    /*public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView VersionName;
        public MyViewHolder(TextView itemView) {
            super(itemView);
            VersionName = itemView;
        }
    }*/
}


