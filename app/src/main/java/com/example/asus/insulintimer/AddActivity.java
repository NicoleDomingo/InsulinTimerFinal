package com.example.asus.insulintimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.User;

/**
 * Created by Mharjorie Sandel on 10/03/2018.
 */

public class AddActivity extends Fragment {
    private String meal[] = {"Before Breakfast", "After Breakfast", "Before Lunch", "After Lunch", "Before Dinner", "After Dinner",
            "Before Snack", "After Snack", "Fasting", "Bedtime", "Other"};
    private ArrayAdapter<String> mealadapter;
    private String insulin[] = {"Rapid-acting", "Short-acting", "Intermediate- acting", "Long-acting"};
    private ArrayAdapter<String> insulinadapter;
    private Spinner spin1, spin2;
    private Button cancel, addB;
    private EditText sugarTxt, notes;
    private ArrayList<BloodSugar> readings;
    private BloodSugar bs;
    private int sugar, counter, count, mincount = 0 , maxcount = 0;
    private String m, i, n, min,max;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private String user, datestring, message, mincval, maxcval, fname, height, lname,
            maxblood, minblood, password, username, weight, minc, maxc,mincount1,maxcount1;
    public static final String FRAGMENT_CLASS = "fragment_class";
    private Date date;
    private SimpleDateFormat ft;
    private int cmin, cmax;
    private long lastcount;
    private User u;
    private static AddActivity add;
    public static AddActivity newInstance() {
        AddActivity fragment = new AddActivity();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        add= this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.addbs_layout);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        user = prefs.getString("username","");
        Log.d("username", String.valueOf(user));

        userDataReference = databaseReference.child("users").child(user);
        View view = inflater.inflate(R.layout.addbs_layout, container, false);
        getActivity().setTitle("Add Reading");
        readings = new ArrayList<>();

        bs = new BloodSugar();
        counter = 0;
        addB = (Button) view.findViewById(R.id.menu_addbs);
        date = new Date();
        ft = new SimpleDateFormat ("yyyy.MM.dd");
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.addbs_layout);
        spin1 = (Spinner) view.findViewById(R.id.spinner);
        mealadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, meal);
        spin1.setAdapter(mealadapter);
        spin2 = (Spinner) view.findViewById(R.id.spinner1);
        insulinadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, insulin);
        spin2.setAdapter(insulinadapter);
        sugarTxt = (EditText) view.findViewById(R.id.sugarTxt);
        //sugarTxt.setFocusedByDefault(true);


        sugarTxt.requestFocus();
        notes = (EditText) view.findViewById(R.id.notes);
        cancel = (Button) view.findViewById(R.id.b_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fM = getActivity().getSupportFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                Home h = new Home();
                fT.replace(R.id.frame_layout, h).addToBackStack(null);
                fT.commit();
            }
        });


        addB = (Button) view.findViewById(R.id.b_add);
        addB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                readings.clear();
                if (!(sugarTxt.getText().toString().isEmpty())) {
                    if (!(Integer.valueOf(sugarTxt.getText().toString()) < 20) && !(Integer.valueOf(sugarTxt.getText().toString()) > 600)) {
                        if ((!sugarTxt.getText().toString().isEmpty() && !spin1.getSelectedItem().toString().isEmpty() && !spin2.getSelectedItem().toString().isEmpty()
                                && !notes.getText().toString().isEmpty()) || (!sugarTxt.getText().toString().isEmpty() && !spin1.getSelectedItem().toString().isEmpty() && !spin2.getSelectedItem().toString().isEmpty())) {
                            sugar = Integer.valueOf(sugarTxt.getText().toString());

                            databaseReference.child("users").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d("CHECK", "PUMAPASOK SIZ");

                                    mincval = dataSnapshot.child("minblood").getValue(String.class);
                                    maxcval = dataSnapshot.child("maxblood").getValue(String.class);
                                    cmin = Integer.parseInt(mincval);
                                    cmax = Integer.parseInt(maxcval);
                                    maxcount = Integer.parseInt(dataSnapshot.child("maxcount").getValue(String.class));
                                    mincount = Integer.parseInt(dataSnapshot.child("mincount").getValue(String.class));


                                    Log.d("NOTIF KA SIZ", mincval);
                                    if (sugar < cmin || sugar > cmax) {
                                        if (sugar < cmin) {
                                            Log.d("DAPAT PASOK", "PASOK");
                                            mincount += 1;
                                            databaseReference.child("users").child(user).child("mincount").setValue(String.valueOf(mincount));
                                            if (mincount == 5) {
                                                sendNotificationMin();
                                                databaseReference.child("users").child(user).child("mincount").setValue("0");
                                            }
                                            //need maset yung value dun sa min and maxount huhuh
                                        } else if (sugar > cmax) {
                                            Log.d("PUMAPASOK SA MAX", "NICELA");
                                            maxcount += 1;
                                            databaseReference.child("users").child(user).child("maxcount").setValue(String.valueOf(maxcount));
                                            if (maxcount == 5) {
                                                sendNotificationMax();
                                                databaseReference.child("users").child(user).child("maxcount").setValue("0");
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }//end of first if

                        if (!sugarTxt.getText().toString().isEmpty() && !spin1.getSelectedItem().toString().isEmpty() && !spin2.getSelectedItem().toString().isEmpty()
                                && !notes.getText().toString().isEmpty()) {
                         //   Toast.makeText(getContext(), "ADD 1", Toast.LENGTH_SHORT).show();
                            sugar = Integer.valueOf(sugarTxt.getText().toString());
                            m = spin1.getSelectedItem().toString();
                            i = spin2.getSelectedItem().toString();
                            n = notes.getText().toString();
                            datestring = ft.format(date);
                            count = (int) lastcount;

                            bs = new BloodSugar(count, sugar, m, i, n, datestring);
                            lastcount = lastcount + 1;
                            readings.add(bs);
                            userDataReference = databaseReference.child("users").child(user).child("records").child(String.valueOf(lastcount));
                            userDataReference.setValue(bs);


                        }//end of second if

                        else if (!sugarTxt.getText().toString().isEmpty() && !spin1.getSelectedItem().toString().isEmpty() && !spin2.getSelectedItem().toString().isEmpty()) {
                          //  Toast.makeText(getContext(), "ADD 2", Toast.LENGTH_SHORT).show();
                            sugar = Integer.valueOf(sugarTxt.getText().toString());
                            m = spin1.getSelectedItem().toString();
                            i = spin2.getSelectedItem().toString();
                            n = notes.getText().toString();
                            datestring = ft.format(date);
                            count = (int) lastcount;
                            bs = new BloodSugar(count, sugar, m, i, n, datestring);
                            lastcount = lastcount + 1;
                            readings.add(bs);
                            readings.add(bs);
                            userDataReference = databaseReference.child("users").child(user).child("records").child(String.valueOf(lastcount));
                            userDataReference.setValue(bs);

                        }
                        FragmentManager fM = getActivity().getSupportFragmentManager();
                        FragmentTransaction fT = fM.beginTransaction();
                        Home h = new Home();
                        fT.replace(R.id.frame_layout, h);
                        fT.commit();
                        //Toast.makeText(getActivity(), "Add!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "You cannot input below 20 or above 600.", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(getContext(), "Please input your blood sugar!", Toast.LENGTH_SHORT).show();
                }

            }

            });
            databaseReference.child("users").child(user).child("records").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lastcount = dataSnapshot.getChildrenCount();
                    if (lastcount != 0) {
                        //Toast.makeText(getContext(), String.valueOf(lastcount) + "oncreate", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Handle possible errors.
                }
            });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.menu_about:
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, About.newInstance());
                transaction.commit();
                //Toast.makeText(getContext(), "About!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_help:
                FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.frame_layout, Help.newInstance());
                transaction1.commit();
                //Toast.makeText(getContext(), "Help!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:
                SharedPreferences dsp = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = dsp.edit();
                editor.remove("username");
                editor.commit();
                Intent loginIntent = new Intent(getContext(),ActivityLogin.class);
                startActivity(loginIntent);

                //Toast.makeText(getContext(), "Help!", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void sendNotificationMin(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext());

        //Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(getContext(),MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.timerlogo);
        mBuilder.setContentTitle("Please see your Doctor!");
        mBuilder.setContentText("Minimum sugar level exceeded 5x.");
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setSound(alarmTone);

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }
    public void sendNotificationMax(){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext());

        //Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(getContext(),MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.timerlogo);
        mBuilder.setContentTitle("Please see your Doctor!");
        mBuilder.setContentText("Maximum sugar level exceeded 5x.");
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setSound(alarmTone);

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }


}





