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

        //checks the isClockedIn flag to determin what to do
        if(!(sharedpreferences.getBoolean("isClockedIn", false))) {
            //gets the current time
            Calendar calender = Calendar.getInstance();
            //updates text_view_status to clocked in
            textView_status.setText("Clocked In");

            /*saves an "in_time" to preferences so that it can be added to the database when
            clock out is clicked*/
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("in_time", calender.getTime().toString());
            //updates the isClockedIn flag
            editor.putBoolean("isClockedIn", true);
            editor.commit();

            //updates the Last_Clock_In_field field on screen
            TextView textView = findViewById(R.id.Last_Clock_In_field);
            textView.setText(calender.getTime().toString());
        }
        //if already clocked in updates text_view_status to:
        else{textView_status.setText("Already Clocked In");}

    }

    public void OnClickClockOut (View v) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        TextView textView_status = findViewById(R.id.text_view_status);

        //checks the isClockedIn flag to determin what to do
        if (sharedpreferences.getBoolean("isClockedIn", false)) {
            //gets the current time
            Calendar calender = Calendar.getInstance();
            //updates text_view_status to clocked out
            textView_status.setText("Clocked Out");

            // collects the in_time from saved prefrences
            String in_time = sharedpreferences.getString("in_time", "error, no in_time");
            //updates the isClockedIn preference
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isClockedIn", false);
            editor.commit();

            // creates sql update
            String sql;
            //enters into database a new row of in,out time
            SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("clockInOut.db", MODE_PRIVATE, null);
            sql = String.format("INSERT INTO inOutLog VALUES('%1$s' , '%2$s');", in_time, calender.getTime().toString());
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
            //closes database
            query.close();
            sqLiteDatabase.close();

            //updates the last clock out field in view
            TextView textView = findViewById(R.id.Last_Clock_Out_field);
            textView.setText(calender.getTime().toString());
        }
        //if clocked out pressed twice
        else{textView_status.setText("Already Clocked Out");}
    }
}

