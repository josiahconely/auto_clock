package com.example.auto_clock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TimePicker;

import java.util.Calendar;

public class edit_log_entry extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String MyPREFERENCES = "myPreferences";
    private LogEntry Log;
    private BusinessLogic bl;
    private TimePicker in_Time ;
    private TimePicker out_Time ;
    private CalendarView in_Cal ;
    private CalendarView out_Cal ;
    private boolean in_time_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_log_entry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log = getLogfromDataBase( Integer.parseInt(
                (sharedPreferences.getString("Item_to_Edit", "0"))));
        System.out.println("got to Log out" +Log.get_in().getTime().toString());
        in_Time = findViewById(R.id.In_timePicker);
        out_Time = findViewById(R.id.Out_timePicker);
        in_Cal = findViewById(R.id.In_Time_calender);
        out_Cal = findViewById(R.id.Out_Time_calender);


        in_Time.setCurrentHour(Log.get_in().get(Calendar.HOUR_OF_DAY));
        in_Time.setCurrentMinute(Log.get_in().get(Calendar.MINUTE));

        out_Time.setCurrentHour(Log.get_out().get(Calendar.HOUR_OF_DAY));
        out_Time.setCurrentMinute(Log.get_out().get(Calendar.MINUTE));

        in_Cal.setDate(Log.get_in().getTimeInMillis(),true,true);
        out_Cal.setDate(Log.get_out().getTimeInMillis(),true,true);
        in_time_edit= true;
    }

    public LogEntry getLogfromDataBase(int index){
        bl =new BusinessLogic(this);
        return bl.getLogEntryRow(index);
    }

    public void onClickEditInTime(View v){
        System.out.println("Edit In Time");
        in_time_edit = true;
        in_Time.setVisibility(View.VISIBLE);
        out_Time.setVisibility(View.INVISIBLE);
        in_Cal.setVisibility(View.INVISIBLE);
        out_Cal.setVisibility(View.INVISIBLE);

    }
    public void onClickEditOutTime(View v){
        System.out.println("Edit Out Time");
        in_time_edit = false;
        in_Time.setVisibility(View.INVISIBLE);
        out_Time.setVisibility(View.VISIBLE);
        in_Cal.setVisibility(View.INVISIBLE);
        out_Cal.setVisibility(View.INVISIBLE);
    }
    public void onClickSave(){

    }
    public void onClickChangeDate(View v){

        if(in_time_edit = true) {
            System.out.println("Edit In Cal");
            in_Time.setVisibility(View.INVISIBLE);
            out_Time.setVisibility(View.INVISIBLE);
            in_Cal.setVisibility(View.VISIBLE);
            out_Cal.setVisibility(View.INVISIBLE);
        }
        else{
            System.out.println("Edit Out Cal");
            in_Time.setVisibility(View.INVISIBLE);
            out_Time.setVisibility(View.INVISIBLE);
            in_Cal.setVisibility(View.INVISIBLE);
            out_Cal.setVisibility(View.VISIBLE);
        }

    }
    public void onClickCancel(View v){

    }
}
