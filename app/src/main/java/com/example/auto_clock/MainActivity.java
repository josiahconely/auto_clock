package com.example.auto_clock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sets date and time for Display
        Calendar calender = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calender.getTime());
        String currentTime = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(calender.getTime());
        TextView textView_date = findViewById(R.id.text_view_date);
        TextView textView_time = findViewById(R.id.text_view_time);
        textView_date.setText(currentDate);
        textView_time.setText(currentTime);
    }
}
