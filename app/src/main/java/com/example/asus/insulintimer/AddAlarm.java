package com.example.asus.insulintimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mharjorie Sandel on 11/03/2018.
 */

public class AddAlarm extends Fragment {
    Button saveAlarm;
    private List<AlarmData> alarmArrayList = new ArrayList<>();
    private ArrayList<String> days = new ArrayList<>();
    private String format, user;
    private int hour, minute;
    private int counter;
    private long lastcount=0;
    private TimePicker timePicker;
    private AlarmData newAlarm;
    private Switch repeat;
    private Button addB, cancelB;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbr = db.getReference();


    public static AddAlarm newInstance() {
        AddAlarm fragment = new AddAlarm();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle("Add Alarm");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addalarm_layout, container, false);

        final Calendar calendar = Calendar.getInstance();
        saveAlarm = (Button) view.findViewById(R.id.menu_savealarm);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        addB = (Button) view.findViewById(R.id.ba_add);
        cancelB = (Button) view.findViewById(R.id.ba_cancel);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        user = prefs.getString("username","");


        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();

                if (hour == 0) {
                    //hour += 12;
                    format = "AM";
                } else if (hour == 12) {
                    format = "PM";
                } else if (hour > 12) {
                    // hour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }

                dbr.child("users").child(user).child("schedules").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            alarmArrayList.clear();
                            int i = 0;
                            counter = (int)lastcount ;
                            for (DataSnapshot childRow : dataSnapshot.getChildren()) {
                                newAlarm = childRow.getValue(AlarmData.class);
                                newAlarm.setCounter(i);
                                 i++;
                                alarmArrayList.add(newAlarm);
                            }
                            i++;
                            AlarmData newAlarm = new AlarmData(hour, minute, days, true, format, counter);
                            alarmArrayList.add(newAlarm);
                            dbr.child("users").child(user).child("schedules").setValue(alarmArrayList);
                        } else {
                            counter = (int) lastcount;
                            AlarmData newAlarm = new AlarmData(hour, minute, days, true, format, counter);
                            alarmArrayList.add(newAlarm);
                            dbr.child("users").child(user).child("schedules").setValue(alarmArrayList);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Schedule fragobj = new Schedule();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragobj);
                transaction.commit();
            }
        });
        dbr.child("users").child(user).child("schedules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lastcount = dataSnapshot.getChildrenCount();
                if (lastcount != 0) {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fM = getActivity().getSupportFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                Schedule s = new Schedule();
                fT.replace(R.id.frame_layout, s);

                fT.commit();
            }
        });


        return view;
    }

}
