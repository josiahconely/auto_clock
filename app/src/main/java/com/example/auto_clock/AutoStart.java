package com.example.auto_clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        System.out.println("got to AutoStartTop");
        Intent startServiceIntent = new Intent(context, MyAutoService.class);
        context.startService(startServiceIntent);
        System.out.println("got to AutoStartBottom");
    }
}
