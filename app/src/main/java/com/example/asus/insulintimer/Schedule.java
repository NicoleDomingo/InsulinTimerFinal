package com.example.asus.insulintimer;

/**
 * Created by Nicole Domingo on 04/03/2018.
 */

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Schedule extends Fragment {
    private Button addalarm;
    private List<AlarmData> alarmArrayList = new ArrayList<>();
    private RecyclerView alarmListRV;
    private AlarmAdapter alarmAd;
    private AlarmData alarmD;
    private Context c;
    private String day, user;
    private PendingIntent pendingIntent;
    public AlarmManager alarmManager;
    private Bundle userBundle;
    private int hour, mins, format = 0;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbr = db.getReference();
    private static Schedule inst;
    private  FragmentManager fM;

    SwipeController swipeController = null;



    public static Schedule newInstance() {
        Schedule fragment = new Schedule();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.alarm_menu, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.schedule_layout, container, false);
        getActivity().setTitle("Schedule");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        user = prefs.getString("username","");

        addalarm = (Button) view.findViewById(R.id.addalarm);
        alarmListRV = (RecyclerView) view.findViewById(R.id.alarmList);

        c = getContext();
        alarmAd = new AlarmAdapter(alarmArrayList, user, getContext());

        dbr.child("users").child(user).child("schedules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    alarmArrayList.clear();
                    for (DataSnapshot childRow : dataSnapshot.getChildren()) {
                        alarmD = childRow.getValue(AlarmData.class);
                        alarmArrayList.add(alarmD);
                    }
                    alarmAd.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        alarmListRV.setLayoutManager(mLayoutManager);
        alarmListRV.setAdapter(alarmAd);






        /* Retrieve a PendingIntent that will perform a broadcast */
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                alarmAd.alarmArrayList.remove(position);
                alarmAd.notifyItemRemoved(position);
                alarmAd.notifyItemRangeChanged(position, alarmAd.getItemCount());

                dbr.child("users").child(user).child("schedules").setValue(alarmArrayList);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(alarmListRV);

        alarmListRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        return view;
    }

    private void order(List<AlarmData> persons) {

        Collections.sort(persons, new Comparator() {

            public int compare(Object o1, Object o2) {

                Integer x1 = ((AlarmData) o1).getHour();
                Integer x2 = ((AlarmData) o2).getHour();
                int sComp = x1.compareTo(x2);

                if (sComp != 0) {
                    return sComp;
                }

                Integer x3 = ((AlarmData) o1).getMinute();
                Integer x4 = ((AlarmData) o2).getMinute();
                return x3.compareTo(x4);
            }});
        alarmAd.notifyDataSetChanged();
    }
}

            /*public int compare(Object o1, Object o2) {

                Integer x1 = ((AlarmData) o1).getHour();
                Integer x2 = ((AlarmData) o2).getHour();
                Integer y1 = ((AlarmData) o1).getMinute();
                Integer y2 = ((AlarmData) o2).getMinute();
                if (x1 == x2) {
                    return y1.compareTo(y2);
                }
                int sComp = x1.compareTo(x2);

                if (sComp != 0) {
                    return sComp;
                }

                Integer x3 = ((AlarmData) o1).getMinute();
                Integer x4 = ((AlarmData) o2).getMinute();
                return x3.compareTo(x4);
            }});*/


