package com.example.auto_clock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class edit_log_entry extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String MyPREFERENCES = "myPreferences";
    private TextView message;
    private LogEntry Log;
    private BusinessLogic bl;
    private TimePicker in_Time ;
    private TimePicker out_Time ;
    private DatePicker in_Cal ;
    private DatePicker out_Cal ;
    boolean in_time_edit;


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
        message = findViewById(R.id.editINOUTtimeDate);


        in_Time.setCurrentHour(Log.get_in().get(Calendar.HOUR_OF_DAY));
        in_Time.setCurrentMinute(Log.get_in().get(Calendar.MINUTE));

        out_Time.setCurrentHour(Log.get_out().get(Calendar.HOUR_OF_DAY));
        out_Time.setCurrentMinute(Log.get_out().get(Calendar.MINUTE));

        in_Cal.updateDate(Log.get_in().get(Calendar.YEAR),Log.get_in().get(Calendar.MONTH),
                Log.get_in().get(Calendar.DAY_OF_MONTH));
        out_Cal.updateDate(Log.get_out().get(Calendar.YEAR),Log.get_out().get(Calendar.MONTH),
                Log.get_out().get(Calendar.DAY_OF_MONTH));
        in_time_edit= true;
        message.setText(R.string.Edit_in_time);
    }

    public LogEntry getLogfromDataBase(int index){
        bl =new BusinessLogic(this);
        return bl.getLogEntryRow(index);
    }

    public void onClickEditInTime(View v){
        System.out.println("Edit In Time");
        in_time_edit = true;
        System.out.println(in_time_edit);
        message.setText(R.string.Edit_in_time);
        in_Time.setVisibility(View.VISIBLE);
        out_Time.setVisibility(View.INVISIBLE);
        in_Cal.setVisibility(View.INVISIBLE);
        out_Cal.setVisibility(View.INVISIBLE);

    }
    public void onClickEditOutTime(View v){
        System.out.println("Edit Out Time");
        in_time_edit = false;
        System.out.println(in_time_edit);
        message.setText(R.string.Edit_out_time);
        in_Time.setVisibility(View.INVISIBLE);
        out_Time.setVisibility(View.VISIBLE);
        in_Cal.setVisibility(View.INVISIBLE);
        out_Cal.setVisibility(View.INVISIBLE);
    }

    public void onClickChangeDate(View v){
        System.out.println(in_time_edit);
        if(in_time_edit == true) {
            System.out.println("Edit In Cal");
            in_Time.setVisibility(View.INVISIBLE);
            out_Time.setVisibility(View.INVISIBLE);
            in_Cal.setVisibility(View.VISIBLE);
            out_Cal.setVisibility(View.INVISIBLE);
            message.setText(R.string.Edit_in_date);
        }
        else{
            System.out.println("Edit Out Cal");
            in_Time.setVisibility(View.INVISIBLE);
            out_Time.setVisibility(View.INVISIBLE);
            in_Cal.setVisibility(View.INVISIBLE);
            out_Cal.setVisibility(View.VISIBLE);
            message.setText(R.string.Edit_out_date);
        }

    }
    public void onClickCancel(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClickSave(View v){
        Calendar calin = Calendar.getInstance();
        Calendar calout= Calendar.getInstance();

        //sets in time from pickers
        calin.set(Calendar.YEAR, in_Cal.getYear());
        calin.set(Calendar.MONTH, in_Cal.getMonth());
        calin.set(Calendar.DATE, in_Cal.getDayOfMonth());
        calin.set(Calendar.HOUR_OF_DAY, in_Time.getHour());
        calin.set(Calendar.MINUTE, in_Time.getMinute());
        calin.set(Calendar.SECOND, 0);
        System.out.println(calin.getTime().toString());

        //sets out time from pickers
        calout.set(Calendar.YEAR, out_Cal.getYear());
        calout.set(Calendar.MONTH, out_Cal.getMonth());
        calout.set(Calendar.DATE, out_Cal.getDayOfMonth());
        calout.set(Calendar.HOUR_OF_DAY, out_Time.getHour());
        calout.set(Calendar.MINUTE, out_Time.getMinute());
        calout.set(Calendar.SECOND, 0);
        System.out.println(calin.getTime().toString());

        LogEntry entry_to_save = new LogEntry(calin,calout,Log.get_latitude(),Log.get_longitude());
        bl.replaceLogRow(Log, entry_to_save);




        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
