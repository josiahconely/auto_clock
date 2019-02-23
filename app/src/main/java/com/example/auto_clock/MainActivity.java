package com.example.auto_clock;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

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

        // creates the data base
        SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("clockInOut.db", MODE_PRIVATE, null);

        // initializes the
        String sql;
        sql = "CREATE TABLE IF NOT EXISTS inOutLog(in_time TEXT, out_time TEXT);";
        sqLiteDatabase.execSQL(sql);


        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM inOutLog;", null);
        if(query.moveToFirst()) {
            do {
                String in = query.getString(0);
                String out = query.getString(1);
                Toast.makeText(this, "In = " +in + " Out = " + out, Toast.LENGTH_LONG).show();
            } while(query.moveToNext());
        }
        query.close();
        sqLiteDatabase.close();
    }

    public void OnClickClockIn (View v){
        TextView textView_status = findViewById(R.id.text_view_status);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(!(sharedpreferences.getBoolean("isClockedIn", false))) {
            Calendar calender = Calendar.getInstance();
            textView_status.setText("Clocked In");

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("in_time", calender.getTime().toString());
            //updates the in out flag
            editor.putBoolean("isClockedIn", true);
            editor.commit();
        }
        else{textView_status.setText("Already Clocked In");}

    }

    public void OnClickClockOut (View v) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        TextView textView_status = findViewById(R.id.text_view_status);
        if (sharedpreferences.getBoolean("isClockedIn", false)) {
            //gets the current time

            Calendar calender = Calendar.getInstance();
            //updates the

            textView_status.setText("Clocked Out");


            String in_time = sharedpreferences.getString("in_time", "error, no in_time");
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isClockedIn", false);
            editor.commit();

            String sql;
            sql = String.format("INSERT INTO inOutLog VALUES('%1$s' , '%2$s');", in_time, calender.getTime().toString());
            SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("clockInOut.db", MODE_PRIVATE, null);
            sqLiteDatabase.execSQL(sql);
            sql = "CREATE TABLE IF NOT EXISTS inOutLog(log_id INT, in_time TEXT, out_time TEXT);";
            sqLiteDatabase.execSQL(sql);

            //displays Toast text of log info for user
            Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM inOutLog;", null);
            if (query.moveToFirst()) {
                do {
                    String in = query.getString(0);
                    String out = query.getString(1);
                    Toast.makeText(this, "In = " + in + " Out = " + out, Toast.LENGTH_LONG).show();
                } while (query.moveToNext());
            }
            query.close();
            sqLiteDatabase.close();
        }
        else{textView_status.setText("Already Clocked Out");}

    }
}

