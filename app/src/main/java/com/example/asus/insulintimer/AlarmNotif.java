package com.example.asus.insulintimer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.frakbot.glowpadbackport.GlowPadView;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmNotif extends AppCompatActivity {

    public static final int TARGET_SNOOZE = 0;
    public static final int TARGET_DRINK = 2;
    public static final int PENDING_NEXT_ACTIVITY=55;
    public static final int NOTIFICATION_ANNOUNCEMENT=55;
    public static final int PENDING_ALARM_RECEIVER=56;


    private PowerManager.WakeLock wl;
    //private Schedule schedule;

    private Ringtone r;
    private Vibrator v;
    private GlowPadView glowPad;
    Handler handler;
    Runnable autoCloser;

    boolean isClicked;
    boolean isMilitary;

    int userRingerMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmnotif);
        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);

        AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        glowPad = (GlowPadView) findViewById(R.id.gpv_alarm_medicine);
        userRingerMode = audioManager.getRingerMode();

        int volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        initializeContent();
        initializeContinuousPing();
        initializeTimedPresence();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    }

    public void initializeContent(){
        playRingtone();
        playVibration();

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        glowPad.setPointsMultiplier(12);
        glowPad.setOnTriggerListener(new GlowPadView.OnTriggerListener() {
            @Override
            public void onGrabbed(View v, int handle) {
                setClicked(true);
            }

            @Override
            public void onReleased(View v, int handle) {
                setClicked(false);
            }

            @Override
            public void onTrigger(View v, int target) {
                drink();
                glowPad.reset(true);
            }

            @Override
            public void onGrabbedStateChange(View v, int handle) {
                // Do nothing
            }

            @Override
            public void onFinishFinalAnimation() {
                // Do nothing
            }
        });
    }

    public void initializeContinuousPing(){
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while(true){
                        if(!isInterrupted() && !isClicked) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    glowPad.ping();
                                }
                            });
                            Thread.sleep(1500);
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    public void initializeTimedPresence(){
        handler = new Handler();
        autoCloser = new Runnable(){
            @Override
            public void run() {

            }
        };
        handler.postDelayed(autoCloser, 45000);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void resetRingerMode(){
        AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(userRingerMode);
    }

    public void playRingtone(){
        r.play();
    }

    public void playVibration(){
        v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    public boolean isClicked(){
        return isClicked;
    }
    public void setClicked(boolean bool){
        isClicked = bool;
    }

    public void drink(){
        resetRingerMode();

        if(r.isPlaying()){
            r.stop();
        }
        if(v.hasVibrator()){
            v.cancel();
        }


        finish();
    }

    @Override
    public void onBackPressed() {

    }

}
