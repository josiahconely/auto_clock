package com.example.auto_clock;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LogViewAndEditActivity extends AppCompatActivity {

    private RecylerAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<LogEntry> list;
    private List<String> stringList;
    BusinessLogic busLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view_and_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        layoutManager = new LinearLayoutManager(this);
        recyclerView = new RecyclerView(this);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);

        busLogic = new BusinessLogic(this);

        list  = new ArrayList<>();
        stringList = new ArrayList<>();
        list = busLogic.getAllLog();

        //creates string for display from list of LogEntry
        String timeIn;
        String timeOut;
        String format;
        for(final LogEntry item: list ){
            timeIn = (item.get_in().getTime().toString());
            timeOut = (item.get_out().getTime().toString());
            format = "IN TIME:" + timeIn+ "  \nOUT TIME: " + timeOut;
            stringList.add(format);
        }

        adapter = new RecylerAdapter(stringList);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
