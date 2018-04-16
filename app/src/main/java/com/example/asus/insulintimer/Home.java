package com.example.asus.insulintimer;

/**
 * Created by Mharjorie Sandel on 04/03/2018.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Home extends Fragment {
    private List<Tip> tipList = new ArrayList<>();
    private List<Tip> tipList2 = new ArrayList<>();
    private RecyclerView recyclerView;
    private TipAdapter tipAdapter;
    private ImageView iv;
    private TextView sugarVal, date;
    private Button tap, min, max, down;
    private ScrollView sv;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private String user, minval, maxval, today, datetoday, mincount, maxcount;
    private Long count, longval;
    private int intcount, val, randomnum, sTipsCounter;
    private Date d;
    private ScrollView scroll;
    private Button sTips;
    private Random r;
    private SimpleDateFormat ft;

    public Home () {

    }

    public static Home newInstance() {
        Home fragment = new Home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
           // super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_layout, container, false);

        getActivity().setTitle("Home");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        user = prefs.getString("username","");

        scroll = (ScrollView) view.findViewById(R.id.scroll);
        sTips = (Button) view.findViewById(R.id.b_down);




        userDataReference = databaseReference.child("users").child(user);
        userDataReference.child("records").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    count = dataSnapshot.getChildrenCount();
                    if (count != 0) {
                        intcount = count.intValue();
                        //Toast.makeText(getContext(), String.valueOf(intcount) + "int", Toast.LENGTH_SHORT).show();
                        val = intcount;
                        userDataReference.child("records").child(String.valueOf(val)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                longval = dataSnapshot.child("bloodsugar").getValue(Long.class);
                                sugarVal.setText(String.valueOf(longval.intValue()));
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                userDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        minval = dataSnapshot.child("minblood").getValue(String.class);
                        min.setText(minval);
                        maxval = dataSnapshot.child("maxblood").getValue(String.class);
                        max.setText(maxval);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });










        //recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tipAdapter = new TipAdapter(tipList2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tipAdapter);
        prepareData();

        //data
        date = (TextView) view.findViewById(R.id.textView10);
        today = String.valueOf(date.getText());
        d = new Date();
        ft = new SimpleDateFormat("EEE, MMM d, ''yy");
        datetoday = ft.format(d);
        date.setText(datetoday);
        sugarVal = (TextView) view.findViewById(R.id.tv_bs);
        iv = (ImageView) view.findViewById(R.id.imageView);
        AnimateBell();
        sv = (ScrollView) view.findViewById(R.id.scroll);
        sv.smoothScrollTo(0,view.getTop());
        tap = (Button) view.findViewById(R.id.b_tap);
        min = (Button) view.findViewById(R.id.b_min);
        min.getBackground().setAlpha(64);
        max = (Button) view.findViewById(R.id.b_max);
        max.getBackground().setAlpha(64);
        down = (Button) view.findViewById(R.id.b_down);
        scroll.setScrollbarFadingEnabled(false);
        down.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                sTipsCounter += 1;
                sv.fullScroll(ScrollView.FOCUS_DOWN);
                if (sTipsCounter % 2 == 0) {
                    scroll.fullScroll(View.FOCUS_UP);
                    down.setText("Tap to See tips");
                }
                else{
                    down.setText("Tap to Close");
                }
            }
        });
        tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fM = getActivity().getSupportFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                AddActivity add= new AddActivity();
                fT.replace(R.id.frame_layout, add).addToBackStack(null);
                fT.commit();

                // /Intent addIntent = new Intent(getContext(), AddActivity.class);
                //startActivity(addIntent);
                //Toast.makeText(getContext(), "Home!", Toast.LENGTH_SHORT).show();

            }
        });
        scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        return view;
    }
    private void prepareData() {
        Tip t = new Tip("5 Smart Ways to Beat Type 2 Diabetes", "How to Beat Type 2 Diabetes With Diet and Lifestyle Changes","https://www.everydayhealth.com/type-2-diabetes/treatment/managing/");
        tipList.add(t);

        t = new Tip("Managing Diabetes", "4 Steps to Manage Your Diabetes for Life","https://www.niddk.nih.gov/health-information/diabetes/overview/managing-diabetes/4-steps");
        tipList.add(t);

        t = new Tip("13 Diabetes Tips to Improve Blood Sugar Control", "How to Take Control Now","http://www.diabeticlivingonline.com/monitoring/blood-sugar/13-diabetes-tips-to-improve-blood-sugar-control");
        tipList.add(t);

        t = new Tip("Tips to Control Your Blood Sugar", "Take Small Steps Toward a Healthy Eating Plan","http://www.diabeticlivingonline.com/monitoring/blood-sugar/tips-to-control-your-blood-sugar");
        tipList.add(t);

        t = new Tip("How To Control Your Diabetes", "5 Tips to Get Your Diabetes Under Control","https://www.webmd.com/diabetes/features/5-tips-get-your-diabetes-under-control#1");
        tipList.add(t);

        t = new Tip("10 Diet and Exercise Tricks to Control Diabetes", "Here are easy tips and tricks to help get blood sugar under control","http://www.health.com/health/gallery/0,,20469017,00.html");
        tipList.add(t);

        t = new Tip("The 16 Best Foods to Control Diabetes", "Figuring out the best foods to eat when you have diabetes","https://www.healthline.com/nutrition/16-best-foods-for-diabetics");
        tipList.add(t);

        t = new Tip("The Diabetes Diet", "Healthy Eating Tips to Prevent, Control, and Reverse Diabetes","https://www.helpguide.org/articles/diets/the-diabetes-diet.htm");
        tipList.add(t);

        t = new Tip("10 ways to eat well with diabetes", "Try out these top tips for healthy eating","https://www.diabetes.org.uk/guide-to-diabetes/enjoy-food/eating-with-diabetes/10-ways-to-eat-well-with-diabetes");
        tipList.add(t);

        t = new Tip("Top 25 Power Foods for Diabetes", "The Best Foods for Diabetes","http://www.diabeticlivingonline.com/food-to-eat/nutrition/top-25-power-foods-diabetes");
        tipList.add(t);

        tipAdapter.notifyDataSetChanged();

        r = new Random();
        for (int i = 0; i < 2; i++){
            randomnum = r.nextInt(tipList.size());
            tipList2.add(tipList.get(randomnum));
        }
    }

    public void AnimateBell() {
        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shakeanimation);
        iv.setAnimation(shake);
    }


}