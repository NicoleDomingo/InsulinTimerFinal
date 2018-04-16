package com.example.asus.insulintimer;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
/**
 * Created by Nicole Domingo on 04/03/2018.
 */

public class AlarmNotificationService extends IntentService {
    private NotificationManager alarmNotificationManager;

    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 1;
    private PowerManager.WakeLock wakeLock;

    public AlarmNotificationService() {
        super("AlarmNotificationService");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }
    @Override
    public void onHandleIntent(Intent intent) {

        //Send notification
        Intent in = new Intent(getBaseContext(), AlarmNotif.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(in);

        AlarmReceiver.completeWakefulIntent(intent);

        wakeLock.release();
    }
}