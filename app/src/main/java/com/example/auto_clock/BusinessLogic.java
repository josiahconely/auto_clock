package com.example.auto_clock;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class BusinessLogic {
    // DataBase helper
    private DBhelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    private static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;

    public BusinessLogic(Context context) {
        this.context = context;
        dbHelper = new DBhelper(context);
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
    }

    public void close() {
        dbHelper.close();
    }


    public void addLogInOut(LogEntry log){
        database = dbHelper.getWritableDatabase();

        //Sets the values to be added to the table
        ContentValues values = new ContentValues();
        values.put(DBhelper.COL_IN_TIME, Long.toString(log.get_in().getTimeInMillis()));
        values.put(DBhelper.COL_OUT_TIME, Long.toString(log.get_out().getTimeInMillis()));
        values.put(DBhelper.COL_LATITUDE, log.get_latitude());
        values.put(DBhelper.COL_LONGITUDE, log.get_longitude());

        database.insert(DBhelper.TABLE_NAME, null, values);
        System.out.println("Record Added");
        database.close();
    }

    // Returns List of Entire Log
    public List<LogEntry> getAllLog() {
        Calendar calendar = Calendar.getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<LogEntry> log = new ArrayList<LogEntry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBhelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LogEntry logEntry = new LogEntry();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(0)));
                logEntry.set_in(calendar);
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(1)));
                logEntry.set_out(calendar);
                logEntry.set_latitude(cursor.getString(2));
                logEntry.set_longitude(cursor.getString(3));
                // Adding LogIn and Out time to list
                log.add(logEntry);
                System.out.println(logEntry.get_in().toString());
            } while (cursor.moveToNext());
        }

        //Testing
        System.out.println("Bellow Checks Logs are true");
        for (LogEntry log_: log ) {
            System.out.println(log_.get_in().toString());
        }

        // return logInOut list
        return log;
    }

    //saves a preference that user is clocked in
    //saves a preference of the clock in time
    public boolean clockIn(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        System.out.println("status of isClocked in " +sharedpreferences.getBoolean("isClockedIn", false));
        //checks the isClockedIn flag to determin what to do
        if((sharedpreferences.getBoolean("isClockedIn", false))) {
            return false;
        }
        else {
            //gets the current time
            Calendar calender = Calendar.getInstance();

            /*saves an "in_time" to preferences so that it can be added to the database when
            clock out is clicked*/
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("in_time", Long.toString(calender.getTimeInMillis()));
            //updates the isClockedIn flag
            editor.putBoolean("isClockedIn", true);
            editor.commit();
            return true;
        }
    }

    public boolean clockOut(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        //checks the isClockedIn flag to determin what to do
        if (sharedpreferences.getBoolean("isClockedIn", false)) {
            //gets the current time
            Calendar out = Calendar.getInstance();
            //gets the In time
            Calendar in = Calendar.getInstance();
            String in_time = sharedpreferences.getString("in_time", null);
            LogEntry logEntry = new LogEntry();
            in.setTimeInMillis(Long.valueOf(in_time));
            //Builds the log entry
            logEntry.set_in(in);
            logEntry.set_out(out);

            //Commits the Log entry
            addLogInOut(logEntry);

            //updates the isClockedIn preference
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isClockedIn", false);
            editor.commit();

            return true;
        }
        //if clocked out pressed twice
        else{return false;}
    }
}



