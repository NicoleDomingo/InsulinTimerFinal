package com.example.asus.insulintimer;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import sql.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private String user;
    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";
    private SharedPreferences settings = null;
    private SharedPreferences.Editor editor = null;
    private PendingIntent pendingIntent;
    public AlarmManager alarmManager;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbr = db.getReference();
    private FragmentTransaction transaction;
    private FragmentManager fM;
    String mincount, maxcount;
    Ringtone r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        user = prefs.getString("username", "");

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        Helper.disableShiftMode(bottomNavigationView);

        fM = getSupportFragmentManager();
        resetStack();
        fM.beginTransaction().replace(R.id.frame_layout, Home.newInstance()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener

                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        resetStack();
                        Bundle bundle = new Bundle();
                        bundle.putString("userN", user);
                        Fragment selectedFragment = null;


                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                selectedFragment = Home.newInstance();
                                //Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_profile:
                                selectedFragment = Profile.newInstance();
                                //Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_schedule:
                                selectedFragment = Schedule.newInstance();
                                //Toast.makeText(MainActivity.this, "Schedule!", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_report:
                                selectedFragment = Report.newInstance();
                                //Toast.makeText(MainActivity.this, "Report!", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_about:
                                //Toast.makeText(MainActivity.this, "About!", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_help:
                                //Toast.makeText(MainActivity.this, "Help!", Toast.LENGTH_SHORT).show();
                                break;

                        }
                        selectedFragment.setArguments(bundle);
                        fM = getSupportFragmentManager();
                        if (item.getItemId() == R.id.menu_home)
                            fM.beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
                        else
                            fM.beginTransaction().replace(R.id.frame_layout, selectedFragment).addToBackStack(null).commit();
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout || id == R.id.menu_about || id == R.id.menu_help){
            resetStack();
            switch (id) {
                case R.id.menu_about:
                    fM = getSupportFragmentManager();
                    fM.beginTransaction().replace(R.id.frame_layout, About.newInstance()).addToBackStack(null).commit();
                    //Toast.makeText(MainActivity.this, "About!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_help:
                    fM = getSupportFragmentManager();
                    fM.beginTransaction().replace(R.id.frame_layout, Help.newInstance()).addToBackStack(null).commit();
                    //Toast.makeText(MainActivity.this, "Help!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_logout:
                    SharedPreferences dsp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = dsp.edit();
                    editor.remove("username");
                    editor.commit();
                    finish();
                    Intent loginIntent = new Intent(MainActivity.this, ActivityLogin.class);
                    startActivity(loginIntent);

                    //Toast.makeText(MainActivity.this, "Help!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else {
            fM =getSupportFragmentManager();
            fM.beginTransaction().replace(R.id.frame_layout, AddAlarm.newInstance()).addToBackStack(null).commit();
            }
        return super.onOptionsItemSelected(item);
    }

    public void resetStack(){
        int count = fM.getBackStackEntryCount();
        for (int i = 0; i < count; i++){
            fM.popBackStackImmediate();
        }
    }
}
