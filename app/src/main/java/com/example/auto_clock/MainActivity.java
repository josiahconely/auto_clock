package com.example.auto_clock;
import android.Manifest;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    private static final String MyPREFERENCES = "myPreferences";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;
    private BusinessLogic busLogic;
    private SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sets date and time for Display
        Calendar calender = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calender.getTime());

        TextView textView_date = findViewById(R.id.text_view_date);
        textView_date.setText(currentDate);

        busLogic = new BusinessLogic(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final Switch toggle = (Switch) findViewById(R.id.Auto_Switch);
        toggle.setChecked(sharedpreferences.getBoolean("AutoSwitchOn", false));
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!tryStartAuto()) {
                        toggle.setChecked(false);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("AutoSwitchOn", false);
                        editor.commit();

                    }else {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("AutoSwitchOn", true);
                        editor.commit();
                    }
                } else {
                    StopAuto();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("AutoSwitchOn", false);
                    editor.commit();

                }
            }
        });
        //Starts background service on start of app
    }

    public void OnClickClockIn(View v) {
        TextView textView_status = findViewById(R.id.text_view_status);
        //clock in runs the clock in function to update database
        if (busLogic.clockIn()) {
            //updates text_view_status to clocked in
            textView_status.setText("Clocked In");
            Calendar calender = Calendar.getInstance();
            //updates the Last_Clock_In_field field on screen
            TextView textView = findViewById(R.id.Last_Clock_In_field);
            textView.setText(calender.getTime().toString());
        }
        //if already clocked in updates text_view_status to:
        else {
            textView_status.setText("Already Clocked In");
        }
    }

    public void OnClickClockOut(View v) {
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
        else {
            textView_status.setText("Already Clocked Out");
        }
    }

    public void onViewLog(View v) {
        Intent intent = new Intent(this, LogViewActivity.class);
        startActivity(intent);
    }

    private Boolean tryStartAuto() {
        if(!isMyServiceRunning(MyAutoService.class)){
            System.out.println("got to AutoStartTop");

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                System.out.println("got to AutoStartTop Permissions pass: starting service");

                //Starts the Service if permissions pass
                Intent startServiceIntent = new Intent(this, MyAutoService.class);
                startServiceIntent.setAction("com.example.auto_clock.action.RunGPSCheck");
                this.startService(startServiceIntent);

                Toast.makeText(this, "Auto Clock Turned On",
                        Toast.LENGTH_SHORT).show();
                return true;

            } else {

                //Permissions not granted for location
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(this, "Auto Clock needs permission to access your location",
                                Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            return false;
        } return true;
    }

    private void StopAuto() {
        Intent intent = new Intent(this, MyAutoService.class);
        stopService(intent);
        Toast.makeText(this, "Auto Clock Turned Off",
                Toast.LENGTH_SHORT).show();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

