package com.example.auto_clock;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    //public static final String MyPREFERENCES = "MyPrefs" ;
    //SharedPreferences sharedpreferences;
    //BusinessLogic businessLogic;
    //DataBase
    private BusinessLogic busLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //sets date and time for Display
        Calendar calender = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calender.getTime());
        String currentTime = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(calender.getTime());
        TextView textView_date = findViewById(R.id.text_view_date);
        TextView textView_time = findViewById(R.id.text_view_time);
        textView_date.setText(currentDate);
        textView_time.setText(currentTime);

        Button clock_in_btn = (Button) findViewById(R.id.button_in);
        Button clock_out_btn = (Button) findViewById(R.id.button_out);

        busLogic = new BusinessLogic(this);
    }

    public void OnClickClockIn (View v){
        TextView textView_status = findViewById(R.id.text_view_status);
        //clock in runs the clock in function to update database
        if (busLogic.clockIn()){
            //updates text_view_status to clocked in
            textView_status.setText("Clocked In");
            Calendar calender = Calendar.getInstance();
            //updates the Last_Clock_In_field field on screen
            TextView textView = findViewById(R.id.Last_Clock_In_field);
            textView.setText(calender.getTime().toString());
        }
        //if already clocked in updates text_view_status to:
        else{textView_status.setText("Already Clocked In");}

    }

    public void OnClickClockOut (View v) {
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        TextView textView_status = findViewById(R.id.text_view_status);

        //checks the isClockedIn flag to determin what to do
        if (busLogic.clockOut()) {
            Calendar calender = Calendar.getInstance();
            //updates text_view_status to clocked out
            textView_status.setText("Clocked Out");
            //updates the last clock out field in view
            TextView textView = findViewById(R.id.Last_Clock_Out_field);
            textView.setText(calender.getTime().toString());
        }
        //if clocked out pressed twice
        else{textView_status.setText("Already Clocked Out");}
    }
}

