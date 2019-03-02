package com.example.auto_clock;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Calendar;
import static android.content.Context.MODE_PRIVATE;


public class BusinessLogic extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void create_dataBase () {
        // creates the data base
        SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("clockInOut.db", MODE_PRIVATE, null);
            // initializes the table inOutLog
        String sql;
        sql = "CREATE TABLE IF NOT EXISTS inOutLog(in_time TEXT, out_time TEXT);";
        sqLiteDatabase.execSQL(sql);
    }

    public void display_DataBase_toast(){
        SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("clockInOut.db", MODE_PRIVATE, null);
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


    //saves a preference that user is clocked in
    //saves a preference of the clock in time
    public boolean clockIn(){
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //checks the isClockedIn flag to determin what to do
        if(!(sharedpreferences.getBoolean("isClockedIn", false))) {
            //gets the current time
            Calendar calender = Calendar.getInstance();

            /*saves an "in_time" to preferences so that it can be added to the database when
            clock out is clicked*/
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("in_time", calender.getTime().toString());
            //updates the isClockedIn flag
            editor.putBoolean("isClockedIn", true);
            editor.commit();

            return true;
        }
        return false;
    }

    public boolean clockOut(){
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //checks the isClockedIn flag to determin what to do
        if (sharedpreferences.getBoolean("isClockedIn", false)) {
            //gets the current time
            Calendar calender = Calendar.getInstance();

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

            display_DataBase_toast();
            return true;
        }
        //if clocked out pressed twice
        else{return false;}
    }




}



