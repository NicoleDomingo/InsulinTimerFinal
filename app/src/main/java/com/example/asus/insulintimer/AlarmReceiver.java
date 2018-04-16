package com.example.asus.insulintimer;


import android.content.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by Nicole Domingo on 09/04/17.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        startAlarmService(context);
    }
    public void startAlarmService(Context context){
        Intent intentNext = new Intent(context, AlarmNotificationService.class);
        startWakefulService(context, intentNext);
    }


}