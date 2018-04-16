package com.example.asus.insulintimer;

/**
 * Created by Mharjorie Sandel on 04/03/2018.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Report extends Fragment {
    private Spinner spin, spin2;
    private RecyclerView recyclerView;
    private String date[] = {"Select", "January", "February", "March",
            "April", "May", "June", "July", "August", "September", "October",
            "November", "December"};
    private String date2[] = {"Select", "Week 1", "Week 2", "Week 3","Week 4"};
    private ArrayAdapter<String> dateadapter;
    private ArrayList<Integer> allbs, bbbs;
    private TextView minall, avgall, maxall, bbmin, bbavg, bbmax,
            abmin, abavg, abmax, blmin, blavg, blmax, almin, alavg, almax,
            bsmin, bsavg, bsmax, asmin, asavg, asmax, bdmin, bdavg, bdmax,
            admin, adavg, admax, fmin, favg, fmax, bmin, bavg, bmax, omin,
            oavg, omax, summary;
    private Date d, dbd, sd, ed;
    private Calendar c;
    private int m, recordid, counterpls, finalid, all, overall,
            min, max, minbb, maxbb, minab, maxab, minbl, maxbl,
            minal, maxal, minbs, maxbs, minas, maxas, minbd,
            maxbd, minad, maxad, minf, maxf, minb, maxb, mino,
            maxo, bball, aball, blall, alall, bsall, asall,
            bdall, adall, fall, ball, oall,bbid, abid, blid,
            alid, bsid, asid, bdid, adid, fid, bid, oid, aid,
            weeknumber,year, flag, flag2, recordVal, id;
    private String user, datedb, monthdb, yeardb, mealt, item1, item2, month, datestamp,
            insulin;
    private TextView select, oa, bb, ab, bl, al, bs,as, bd, ad, o, b, f;
    private double idf;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private SimpleDateFormat sdf, df;
    private List<Integer> idList = new ArrayList<>();
    private List<Integer> idList1 = new ArrayList<>();
    private List<Integer> idList2 = new ArrayList<>();
    private List<Integer> idList3 = new ArrayList<>();
    private List<Integer> idList4 = new ArrayList<>();
    private List<Integer> idList5 = new ArrayList<>();
    private List<Integer> idList6 = new ArrayList<>();
    private List<Integer> idList7 = new ArrayList<>();
    private List<Integer> idList8 = new ArrayList<>();
    private List<Integer> idList9 = new ArrayList<>();
    private List<Integer> idList10 = new ArrayList<>();
    private List<Integer> idList11 = new ArrayList<>();
    private List<Integer> recordList = new ArrayList<>();
    private List<Integer> recordList1 = new ArrayList<>();
    private List<Integer> recordList2 = new ArrayList<>();
    private List<Integer> recordList3 = new ArrayList<>();
    private List<Integer> recordList4 = new ArrayList<>();
    private List<Integer> recordList5 = new ArrayList<>();
    private List<Integer> recordList6 = new ArrayList<>();
    private List<Integer> recordList7 = new ArrayList<>();
    private List<Integer> recordList8 = new ArrayList<>();
    private List<Integer> recordList9 = new ArrayList<>();
    private List<Integer> recordList10 = new ArrayList<>();
    private List<Integer> recordList11 = new ArrayList<>();
    private AlertDialog dialogTrial;
    private Record r;
    private RecordAdapter recordAdapter;
    SharedPreferences dsp;
    boolean isChecked;



    public static Report newInstance() {
        Report fragment = new Report();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_layout, container, false);
        getActivity().setTitle("Reports");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        user = prefs.getString("username","");
        isChecked = prefs.getBoolean("isChecked",false);

        View checkBoxView = View.inflate(getContext(), R.layout.checkbox, null);
        CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dsp = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = dsp.edit();
                editor.putBoolean("isChecked", isChecked);
                editor.commit();

            }
        });

        if(isChecked == false){
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Details");
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setMessage("You can click on each meal type for more details about your record.");
            alertDialog.setView(checkBoxView);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            /*int dividerId = alertDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = alertDialog.findViewById(dividerId);
            divider.setBackgroundColor(Color.parseColor("#8d0000"));

            int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
            TextView tv = (TextView) alertDialog.findViewById(textViewId);
            tv.setTextColor(Color.parseColor("#8d0000"));*/
        }
        else{
            Log.wtf("Checked","Nope");
        }
        userDataReference = databaseReference.child("users").child(user);
        //week
        sdf = new SimpleDateFormat("yyyy.MM.dd");
        final int week = 18;
        allbs = new ArrayList<>();
        bbbs = new ArrayList<>();
        c= Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        c.clear();
        c.set(Calendar.WEEK_OF_YEAR, week);
        c.set(Calendar.YEAR, year);
        d = c.getTime();
        c.set(Calendar.WEEK_OF_YEAR, 18);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        minall = (TextView) view.findViewById(R.id.a_min);
        avgall = (TextView) view.findViewById(R.id.a_avg);
        maxall = (TextView) view.findViewById(R.id.a_max);
        bbmin = (TextView) view.findViewById(R.id.bb_min);
        bbavg = (TextView) view.findViewById(R.id.bb_avg);
        bbmax = (TextView) view.findViewById(R.id.bb_max);
        abmin = (TextView) view.findViewById(R.id.ab_min);
        abavg = (TextView) view.findViewById(R.id.ab_avg);
        abmax = (TextView) view.findViewById(R.id.ab_max);
        blmin = (TextView) view.findViewById(R.id.bl_min);
        blavg = (TextView) view.findViewById(R.id.bl_avg);
        blmax = (TextView) view.findViewById(R.id.bl_max);
        almin = (TextView) view.findViewById(R.id.al_min);
        alavg = (TextView) view.findViewById(R.id.al_avg);
        almax = (TextView) view.findViewById(R.id.al_max);
        bsmin = (TextView) view.findViewById(R.id.bs_min);
        bsavg = (TextView) view.findViewById(R.id.bs_avg);
        bsmax = (TextView) view.findViewById(R.id.bs_max);
        asmin = (TextView) view.findViewById(R.id.as_min);
        asavg = (TextView) view.findViewById(R.id.as_avg);
        asmax = (TextView) view.findViewById(R.id.as_max);
        bdmin = (TextView) view.findViewById(R.id.bd_min);
        bdavg = (TextView) view.findViewById(R.id.bd_avg);
        bdmax = (TextView) view.findViewById(R.id.bd_max);
        admin = (TextView) view.findViewById(R.id.ad_min);
        adavg = (TextView) view.findViewById(R.id.ad_avg);
        admax = (TextView) view.findViewById(R.id.ad_max);
        fmin = (TextView) view.findViewById(R.id.f_min);
        favg = (TextView) view.findViewById(R.id.f_avg);
        fmax = (TextView) view.findViewById(R.id.f_max);
        bmin = (TextView) view.findViewById(R.id.b_min);
        bavg = (TextView) view.findViewById(R.id.b_avg);
        bmax = (TextView) view.findViewById(R.id.b_max);
        omin = (TextView) view.findViewById(R.id.o_min);
        oavg = (TextView) view.findViewById(R.id.o_avg);
        omax = (TextView) view.findViewById(R.id.o_max);
        select = (TextView) view.findViewById(R.id.textView13);
        summary = (TextView) view.findViewById(R.id.textView14);
        oa = (TextView) view.findViewById(R.id.tbl_txt5);
        bb = (TextView) view.findViewById(R.id.tbl_txt13);
        ab = (TextView) view.findViewById(R.id.tbl_txt17);
        bl = (TextView) view.findViewById(R.id.tbl_txt21);
        al = (TextView) view.findViewById(R.id.tbl_txt25);
        bs = (TextView) view.findViewById(R.id.tbl_txt29);
        as = (TextView) view.findViewById(R.id.tbl_txt33);
        bd = (TextView) view.findViewById(R.id.tbl_txt37);
        ad = (TextView) view.findViewById(R.id.tbl_txt41);
        b = (TextView) view.findViewById(R.id.tbl_txt49);
        f = (TextView) view.findViewById(R.id.tbl_txt45);
        o = (TextView) view.findViewById(R.id.tbl_txt53);
        spin = (Spinner) view.findViewById(R.id.spindate);
        spin2 = (Spinner) view.findViewById(R.id.spindate2);
        dateadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, date);
        spin.setAdapter(dateadapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item1 = adapterView.getItemAtPosition(i).toString();
                if (item1.equals("January")){
                    month = "01";
                }
                else if (item1.equals("February")){
                    month = "02";
                }
                else if (item1.equals("Select")){
                    month = "00";
                    spin2.setAdapter(null);
                    summary.setText("No Records");
                }
                else if (item1.equals("March")){
                    month = "03";
                }
                else if (item1.equals("April")){
                    month = "04";
                }
                else if (item1.equals("May")){
                    month = "05";
                }
                else if (item1.equals("June")){
                    month = "06";
                }
                else if (item1.equals("July")){
                    month = "07";
                }
                else if (item1.equals("August")){
                    month = "08";
                }
                else if (item1.equals("September")){
                    month = "09";
                }
                else if (item1.equals("October")){
                    month = "10";

                }
                else if (item1.equals("November")){
                    month = "11";
                }
                else if (item1.equals("December")) {
                    month = "12";
                }
                userDataReference.child("records").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final long counter = dataSnapshot.getChildrenCount();
                        min= Integer.MAX_VALUE;
                        max = Integer.MIN_VALUE;
                        minbb = Integer.MAX_VALUE;
                        maxbb = Integer.MIN_VALUE;
                        minab = Integer.MAX_VALUE;
                        maxab = Integer.MIN_VALUE;
                        minbl = Integer.MAX_VALUE;
                        maxbl = Integer.MIN_VALUE;
                        minal = Integer.MAX_VALUE;
                        maxal = Integer.MIN_VALUE;
                        minbs = Integer.MAX_VALUE;
                        maxbs = Integer.MIN_VALUE;
                        minas = Integer.MAX_VALUE;
                        maxas = Integer.MIN_VALUE;
                        minbd = Integer.MAX_VALUE;
                        maxbd = Integer.MIN_VALUE;
                        minad = Integer.MAX_VALUE;
                        maxad = Integer.MIN_VALUE;
                        minf = Integer.MAX_VALUE;
                        maxf = Integer.MIN_VALUE;
                        minb = Integer.MAX_VALUE;
                        maxb = Integer.MIN_VALUE;
                        mino = Integer.MAX_VALUE;
                        maxo = Integer.MIN_VALUE;
                        bball = 0;
                        aball = 0;
                        flag = 0;
                        maxall.setText("0");
                        minall.setText("0");
                        avgall.setText("0");
                        bbmax.setText("0");
                        bbmin.setText("0");
                        bbavg.setText("0");
                        abmax.setText("0");
                        abmin.setText("0");
                        abavg.setText("0");
                        blmax.setText("0");
                        blmin.setText("0");
                        blavg.setText("0");
                        almax.setText("0");
                        almin.setText("0");
                        alavg.setText("0");
                        bdmax.setText("0");
                        bdmin.setText("0");
                        bdavg.setText("0");
                        admax.setText("0");
                        admin.setText("0");
                        adavg.setText("0");
                        bsmax.setText("0");
                        bsmin.setText("0");
                        bsavg.setText("0");
                        asmax.setText("0");
                        asmin.setText("0");
                        asavg.setText("0");
                        bmax.setText("0");
                        bmin.setText("0");
                        bavg.setText("0");
                        omax.setText("0");
                        omin.setText("0");
                        oavg.setText("0");
                        fmax.setText("0");
                        fmin.setText("0");
                        favg.setText("0");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            datedb = snapshot.child("date").getValue(String.class);
                            monthdb = datedb.substring(5, 7);
                            yeardb = datedb.substring(0,4);
                            if (yeardb.equals(String.valueOf(year))){

                                if (month.equals(monthdb) && !month.equals(00)) {
                                    flag = flag + 1;
                                    summary.setText("Blood Sugar Summary (mg/dL)");
                                    select.setText("Select week for the Month of " + item1 +", " +yeardb);
                                    recordid = snapshot.child("id").getValue(Integer.class);
                                    dateadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, date2);
                                    spin2.setAdapter(dateadapter);
                                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            item2 = adapterView.getItemAtPosition(i).toString();

                                            if (month.equals("01")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 1;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 2;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 3;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 4;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 5;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("02")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 5;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 6;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 7;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 8;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 9;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("03")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 9;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 10;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 11;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 12;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 13;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("04")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 14;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 15;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 16;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 17;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 18;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("05")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 18;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 19;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 20;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 21;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 22;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("06")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 22;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 22;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 23;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 24;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 25;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("07")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 26;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 27;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 28;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 29;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 30;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("08")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 30;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 31;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 32;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 33;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 34;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("09")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 34;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 35;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 36;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 37;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 38;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("10")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 39;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 40;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 41;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 42;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 43;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("11")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 43;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 44;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 45;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 46;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 47;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            } else if (month.equals("12")) {
                                                if (item2.equals("Week 1")) {
                                                    weeknumber = 48;
                                                } else if (item2.equals("Week 2")) {
                                                    weeknumber = 49;
                                                } else if (item2.equals("Week 3")) {
                                                    weeknumber = 50;
                                                } else if (item2.equals("Week 4")) {
                                                    weeknumber = 51;
                                                } else if (item2.equals("Week 5")) {
                                                    weeknumber = 52;
                                                } else if (item2.equals("Select")) {
                                                    weeknumber = 0;
                                                }
                                            }

                                            if (weeknumber != 0) {
                                                c.clear();
                                                //startdate
                                                c.set(Calendar.WEEK_OF_YEAR, weeknumber);
                                                c.set(Calendar.YEAR, year);
                                                d = c.getTime();
                                                //enddate
                                                c.set(Calendar.WEEK_OF_YEAR, weeknumber);
                                                c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                                                //SAMPLE
                                                df = new SimpleDateFormat("yyyy.MM.dd");
                                                String sampledate = "2018.02.11";
                                                try {
                                                    Date fsampledate = df.parse(sampledate);
                                                    String fstartdate = df.format(fsampledate);
                                                    Date ff = df.parse(fstartdate);
                                                    Date fs = df.parse(sdf.format(d));
                                                    Date fe = df.parse(sdf.format(c.getTime()));
                                                    if (ff.before(fe) && ff.after(fs)) {
                                                        Log.d("YES", "NO");
                                                    } else {
                                                        Log.d("NO", "NO");
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                //SAMPLE
                                                userDataReference.child("records").addListenerForSingleValueEvent(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        maxall.setText("0");
                                                        minall.setText("0");
                                                        avgall.setText("0");
                                                        bbmax.setText("0");
                                                        bbmin.setText("0");
                                                        bbavg.setText("0");
                                                        abmax.setText("0");
                                                        abmin.setText("0");
                                                        abavg.setText("0");
                                                        blmax.setText("0");
                                                        blmin.setText("0");
                                                        blavg.setText("0");
                                                        almax.setText("0");
                                                        almin.setText("0");
                                                        alavg.setText("0");
                                                        bdmax.setText("0");
                                                        bdmin.setText("0");
                                                        bdavg.setText("0");
                                                        admax.setText("0");
                                                        admin.setText("0");
                                                        adavg.setText("0");
                                                        bsmax.setText("0");
                                                        bsmin.setText("0");
                                                        bsavg.setText("0");
                                                        asmax.setText("0");
                                                        asmin.setText("0");
                                                        asavg.setText("0");
                                                        bmax.setText("0");
                                                        bmin.setText("0");
                                                        bavg.setText("0");
                                                        omax.setText("0");
                                                        omin.setText("0");
                                                        oavg.setText("0");
                                                        fmax.setText("0");
                                                        fmin.setText("0");
                                                        favg.setText("0");
                                                        min = 100000000;
                                                        max = 0;
                                                        overall = 0;
                                                        minbb = 100000000;
                                                        maxbb = 0;
                                                        bbid = 0;
                                                        bball = 0;
                                                        flag2 = 0;
                                                        minab = 100000000;
                                                        maxab = 0;
                                                        abid = 0;
                                                        aball = 0;
                                                        minbl = 100000000;
                                                        maxbl = 0;
                                                        blid = 0;
                                                        blall = 0;
                                                        minal = 100000000;
                                                        maxal = 0;
                                                        alid = 0;
                                                        alall = 0;
                                                        minbs = 100000000;
                                                        maxbs = 0;
                                                        bsid = 0;
                                                        bsall = 0;
                                                        minas = 100000000;
                                                        maxas = 0;
                                                        asid = 0;
                                                        asall = 0;
                                                        minbd = 100000000;
                                                        maxbd = 0;
                                                        bdid = 0;
                                                        bdall = 0;
                                                        minad = 100000000;
                                                        maxad = 0;
                                                        adid = 0;
                                                        adall = 0;
                                                        minb = 100000000;
                                                        maxb = 0;
                                                        bid = 0;
                                                        ball = 0;
                                                        mino = 100000000;
                                                        maxo = 0;
                                                        oid = 0;
                                                        oall = 0;
                                                        minf = 100000000;
                                                        maxf = 0;
                                                        fid = 0;
                                                        fall = 0;
                                                        idf = 0;
                                                        aid = 0;
                                                        idList.clear();
                                                        idList1.clear();
                                                        idList2.clear();
                                                        idList3.clear();
                                                        idList4.clear();
                                                        idList5.clear();
                                                        idList6.clear();
                                                        idList7.clear();
                                                        idList8.clear();
                                                        idList9.clear();
                                                        idList10.clear();
                                                        idList11.clear();
                                                        recordList.clear();
                                                        recordList1.clear();
                                                        recordList2.clear();
                                                        recordList3.clear();
                                                        recordList4.clear();
                                                        recordList5.clear();
                                                        recordList6.clear();
                                                        recordList7.clear();
                                                        recordList8.clear();
                                                        recordList9.clear();
                                                        recordList10.clear();
                                                        recordList11.clear();
                                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                            counterpls = counterpls + 1;

                                                            datestamp = ds.child("date").getValue(String.class);

                                                            try {
                                                                dbd = df.parse(datestamp);
                                                                sd = df.parse(sdf.format(d));
                                                                ed = df.parse(sdf.format(c.getTime()));

                                                                if ((dbd.before(ed) || dbd.equals(ed)) && (dbd.after(sd) || dbd.equals(sd))) {
                                                                    flag2 = flag2 + 1;
                                                                    id = ds.child("id").getValue(Integer.class);
                                                                    select.setText(item2 + " of the Month of " + item1 + ", " +yeardb);
                                                                    summary.setText("Blood Sugar Summary (mg/dL)");
                                                                    all = ds.child("bloodsugar").getValue(Integer.class);
                                                                    aid = aid + 1;
                                                                    if (all < min) {
                                                                        min = all;
                                                                        minall.setText(String.valueOf(min));

                                                                    }
                                                                    if (all > max) {
                                                                        max = all;
                                                                        maxall.setText(String.valueOf(max));

                                                                    }
                                                                    idList.add(id);
                                                                    recordList.add(all);
                                                                    overall = all + overall;
                                                                    double allf = overall;
                                                                    idf = aid;
                                                                    avgall.setText(String.valueOf(Math.round(allf / idf)));


                                                                    mealt = ds.child("mealtype").getValue(String.class);
                                                                    if (mealt.equals("Before Breakfast")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        bbid = bbid + 1;
                                                                        if (all < minbb) {
                                                                            minbb = all;
                                                                            bbmin.setText(String.valueOf(minbb));
                                                                        }
                                                                        if (all > maxbb) {
                                                                            maxbb = all;
                                                                            bbmax.setText(String.valueOf(maxbb));
                                                                        }
                                                                        idList1.add(id);
                                                                        recordList1.add(all);
                                                                        bball = all + bball;
                                                                        double bbidf = bbid;
                                                                        double bballf = bball;
                                                                        bbavg.setText(String.valueOf(Math.round(bballf / bbidf)));
                                                                    } else if (mealt.equals("After Breakfast")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        abid = abid + 1;
                                                                        if (all < minab) {
                                                                            minab = all;
                                                                            abmin.setText(String.valueOf(minab));
                                                                        }
                                                                        if (all > maxab) {
                                                                            maxab = all;
                                                                            abmax.setText(String.valueOf(maxab));
                                                                        }
                                                                        recordList2.add(all);
                                                                        idList2.add(id);
                                                                        aball = all + aball;
                                                                        double abidf = abid;
                                                                        double aballf = aball;
                                                                        abavg.setText(String.valueOf(Math.round(aballf / abidf)));
                                                                    } else if (mealt.equals("Before Lunch")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        blid = blid + 1;
                                                                        if (all < minbl) {
                                                                            minbl = all;
                                                                            blmin.setText(String.valueOf(minbl));
                                                                        }
                                                                        if (all > maxbl) {
                                                                            maxbl = all;
                                                                            blmax.setText(String.valueOf(maxbl));
                                                                        }
                                                                        recordList3.add(all);
                                                                        idList3.add(id);
                                                                        blall = all + blall;
                                                                        double blidf = blid;
                                                                        double blallf = blall;
                                                                        blavg.setText(String.valueOf(Math.round(blallf / blidf)));
                                                                    } else if (mealt.equals("After Lunch")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        alid = alid + 1;
                                                                        if (all < minal) {
                                                                            minal = all;
                                                                            almin.setText(String.valueOf(minal));
                                                                        }
                                                                        if (all > maxal) {
                                                                            maxal = all;
                                                                            almax.setText(String.valueOf(maxal));
                                                                        }
                                                                        recordList4.add(all);
                                                                        idList4.add(id);
                                                                        alall = all + alall;
                                                                        double alidf = alid;
                                                                        double alallf = alall;
                                                                        alavg.setText(String.valueOf(Math.round(alallf / alidf)));
                                                                    } else if (mealt.equals("Before Snack")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        bsid = bsid + 1;
                                                                        if (all < minbs) {
                                                                            minbs = all;
                                                                            bsmin.setText(String.valueOf(minbs));
                                                                        }
                                                                        if (all > maxbs) {
                                                                            maxbs = all;
                                                                            bsmax.setText(String.valueOf(maxbs));
                                                                        }
                                                                        recordList5.add(all);
                                                                        idList5.add(id);
                                                                        bsall = all + bsall;
                                                                        double bsidf = abid;
                                                                        double bsallf = aball;
                                                                        bsavg.setText(String.valueOf(Math.round(bsallf / bsidf)));
                                                                    } else if (mealt.equals("After Snack")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        asid = asid + 1;
                                                                        if (all < minas) {
                                                                            minas = all;
                                                                            abmin.setText(String.valueOf(minas));
                                                                        }
                                                                        if (all > maxas) {
                                                                            maxas = all;
                                                                            asmax.setText(String.valueOf(maxas));
                                                                        }
                                                                        recordList6.add(all);
                                                                        idList6.add(id);
                                                                        asall = all + asall;
                                                                        double asidf = asid;
                                                                        double asallf = asall;
                                                                        asavg.setText(String.valueOf(Math.round(asallf / asidf)));
                                                                    } else if (mealt.equals("Before Dinner")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        bdid = bdid + 1;
                                                                        if (all < minbd) {
                                                                            minbd = all;
                                                                            bdmin.setText(String.valueOf(minbd));
                                                                        }
                                                                        if (all > maxbd) {
                                                                            maxbd = all;
                                                                            bdmax.setText(String.valueOf(maxbd));
                                                                        }
                                                                        recordList7.add(all);
                                                                        idList7.add(id);
                                                                        bdall = all + bdall;
                                                                        double bdidf = bdid;
                                                                        double bdallf = bdall;
                                                                        bdavg.setText(String.valueOf(Math.round(bdallf / bdidf)));
                                                                    } else if (mealt.equals("After Dinner")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        adid = adid + 1;
                                                                        if (all < minad) {
                                                                            minad = all;
                                                                            admin.setText(String.valueOf(minad));
                                                                        }
                                                                        if (all > maxad) {
                                                                            maxad = all;
                                                                            admax.setText(String.valueOf(maxad));
                                                                        }
                                                                        recordList8.add(all);
                                                                        idList8.add(id);
                                                                        adall = all + adall;
                                                                        double adidf = adid;
                                                                        double adallf = adall;
                                                                        adavg.setText(String.valueOf(Math.round(adallf / adidf)));
                                                                    } else if (mealt.equals("Fasting")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        fid = fid + 1;
                                                                        if (all < minf) {
                                                                            minf = all;
                                                                            fmin.setText(String.valueOf(minf));
                                                                        }
                                                                        if (all > maxf) {
                                                                            maxf = all;
                                                                            fmax.setText(String.valueOf(maxf));
                                                                        }
                                                                        recordList9.add(all);
                                                                        idList9.add(id);
                                                                        fall = all + fall;
                                                                        double fidf = fid;
                                                                        double fallf = fall;
                                                                        favg.setText(String.valueOf(Math.round(fallf / fidf)));
                                                                    } else if (mealt.equals("Bedtime")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        bid = bid + 1;
                                                                        if (all < minb) {
                                                                            minb = all;
                                                                            bmin.setText(String.valueOf(minb));
                                                                        }
                                                                        if (all > maxb) {
                                                                            maxb = all;
                                                                            bmax.setText(String.valueOf(maxb));
                                                                        }
                                                                        recordList10.add(all);
                                                                        idList10.add(id);
                                                                        ball = all + ball;
                                                                        double bidf = bid;
                                                                        double ballf = ball;
                                                                        bavg.setText(String.valueOf(Math.round(ballf / bidf)));
                                                                    } else if (mealt.equals("Other")) {
                                                                        id = ds.child("id").getValue(Integer.class);
                                                                        all = ds.child("bloodsugar").getValue(Integer.class);
                                                                        oid = oid + 1;
                                                                        if (all < mino) {
                                                                            mino = all;
                                                                            omin.setText(String.valueOf(mino));
                                                                        }
                                                                        if (all > maxo) {
                                                                            maxo = all;
                                                                            omax.setText(String.valueOf(maxo));
                                                                        }
                                                                        recordList11.add(all);
                                                                        idList11.add(id);
                                                                        oall = all + oall;
                                                                        double oidf = oid;
                                                                        double oallf = oall;
                                                                        oavg.setText(String.valueOf(Math.round(oallf / oidf)));
                                                                    }
                                                                } else if (flag2 == 0) {
                                                                    summary.setText("No Records");
                                                                    select.setText("Select week for the month of " + item1);
                                                                }
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }

                                                });
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });


                                    userDataReference.child("records").child(String.valueOf(recordid + 1)).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            id = dataSnapshot.child("id").getValue(Integer.class);
                                            all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                            aid = aid + 1;
                                            if (all < min) {
                                                min = all;
                                                minall.setText(String.valueOf(min));
                                            }
                                            if (all > max) {
                                                max = all;
                                                maxall.setText(String.valueOf(max));
                                            }
                                            overall = all + overall;
                                            allbs.add(all);
                                            idList.add(id);
                                            recordList.add(all);
                                            double allf = overall;
                                            idf = aid;
                                            avgall.setText(String.valueOf(Math.round(allf / idf)));
                                            mealt = dataSnapshot.child("mealtype").getValue(String.class);
                                            if (mealt.equals("Before Breakfast")) {

                                                id = dataSnapshot.child("id").getValue(Integer.class);
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                bbid = bbid + 1;
                                                if (all > maxbb) {
                                                    maxbb = all;
                                                    bbmax.setText(String.valueOf(maxbb));
                                                }
                                                if (all < minbb) {
                                                    minbb = all;
                                                    bbmin.setText(String.valueOf(minbb));
                                                }
                                                idList1.add(id);
                                                recordList1.add(all);
                                                bball = all + bball;
                                                double bbidf = bbid;
                                                double bballf = bball;
                                                bbavg.setText(String.valueOf(Math.round(bballf / bbidf)));
                                            } else if (mealt.equals("After Breakfast")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                abid = abid + 1;
                                                if (all > maxab) {
                                                    maxab = all;
                                                    abmax.setText(String.valueOf(maxab));
                                                }
                                                if (all < minab) {
                                                    minab = all;
                                                    abmin.setText(String.valueOf(minab));
                                                }
                                                idList2.add(id);
                                                recordList2.add(all);
                                                aball = all + aball;
                                                double abidf = abid;
                                                double aballf = aball;
                                                abavg.setText(String.valueOf(Math.round(aballf / abidf)));
                                            } else if (mealt.equals("Before Lunch")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                blid = blid + 1;
                                                if (all > maxbl) {
                                                    maxbl = all;
                                                    blmax.setText(String.valueOf(maxbl));
                                                }
                                                if (all < minbl) {
                                                    minbl = all;
                                                    blmin.setText(String.valueOf(minbl));
                                                }
                                                idList3.add(id);
                                                recordList3.add(all);
                                                blall = all + blall;
                                                double blidf = blid;
                                                double blallf = blall;
                                                blavg.setText(String.valueOf(Math.round(blallf / blidf)));
                                            } else if (mealt.equals("After Lunch")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                alid = alid + 1;
                                                if (all > maxal) {
                                                    maxal = all;
                                                    almax.setText(String.valueOf(maxal));
                                                }
                                                if (all < minal) {
                                                    minal = all;
                                                    almin.setText(String.valueOf(minal));
                                                }
                                                idList4.add(id);
                                                recordList4.add(all);
                                                alall = all + alall;
                                                double alidf = alid;
                                                double alallf = alall;
                                                alavg.setText(String.valueOf(Math.round(alallf / alidf)));
                                            } else if (mealt.equals("Before Snack")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                bsid = bsid + 1;
                                                if (all > maxbs) {
                                                    maxbs = all;
                                                    bsmax.setText(String.valueOf(maxbs));
                                                }
                                                if (all < minbs) {
                                                    minbs = all;
                                                    bsmin.setText(String.valueOf(minbs));
                                                }
                                                idList5.add(id);
                                                recordList5.add(all);
                                                bsall = all + bsall;
                                                double bsidf = abid;
                                                double bsallf = aball;
                                                bsavg.setText(String.valueOf(Math.round(bsallf / bsidf)));
                                            } else if (mealt.equals("After Snack")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                asid = asid + 1;
                                                if (all > maxas) {
                                                    maxas = all;
                                                    asmax.setText(String.valueOf(maxas));
                                                }
                                                if (all < minas) {
                                                    minas = all;
                                                    abmin.setText(String.valueOf(minas));
                                                }
                                                idList6.add(id);
                                                recordList6.add(all);
                                                asall = all + asall;
                                                double asidf = asid;
                                                double asallf = asall;
                                                asavg.setText(String.valueOf(Math.round(asallf / asidf)));
                                            } else if (mealt.equals("Before Dinner")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                bdid = bdid + 1;
                                                if (all > maxbd) {
                                                    maxbd = all;
                                                    bdmax.setText(String.valueOf(maxbd));
                                                }
                                                if (all < minbd) {
                                                    minbd = all;
                                                    bdmin.setText(String.valueOf(minbd));
                                                }
                                                idList7.add(id);
                                                recordList7.add(all);
                                                bdall = all + bdall;
                                                double bdidf = bdid;
                                                double bdallf = bdall;
                                                bdavg.setText(String.valueOf(Math.round(bdallf / bdidf)));
                                            } else if (mealt.equals("After Dinner")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                adid = adid + 1;
                                                if (all > maxad) {
                                                    maxad = all;
                                                    admax.setText(String.valueOf(maxad));
                                                }
                                                if (all < minad) {
                                                    minad = all;
                                                    admin.setText(String.valueOf(minad));
                                                }
                                                idList8.add(id);
                                                recordList8.add(all);
                                                adall = all + adall;
                                                double adidf = adid;
                                                double adallf = adall;
                                                adavg.setText(String.valueOf(Math.round(adallf / adidf)));
                                            } else if (mealt.equals("Fasting")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                fid = fid + 1;
                                                if (all > maxf) {
                                                    maxf = all;
                                                    fmax.setText(String.valueOf(maxf));
                                                }
                                                if (all < minf) {
                                                    minf = all;
                                                    fmin.setText(String.valueOf(minf));
                                                }
                                                idList9.add(id);
                                                recordList9.add(all);
                                                fall = all + fall;
                                                double fidf = fid;
                                                double fallf = fall;
                                                favg.setText(String.valueOf(Math.round(fallf / fidf)));
                                            } else if (mealt.equals("Bedtime")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                bid = bid + 1;
                                                if (all > maxb) {
                                                    maxb = all;
                                                    bmax.setText(String.valueOf(maxb));
                                                }
                                                if (all < minb) {
                                                    minb = all;
                                                    bmin.setText(String.valueOf(minb));
                                                }
                                                idList10.add(id);
                                                recordList10.add(all);
                                                ball = all + ball;
                                                double bidf = bid;
                                                double ballf = ball;
                                                bavg.setText(String.valueOf(Math.round(ballf / bidf)));
                                            } else if (mealt.equals("Other")) {
                                                all = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                                oid = oid + 1;
                                                if (all > maxo) {
                                                    maxo = all;
                                                    omax.setText(String.valueOf(maxo));
                                                }
                                                if (all < mino) {
                                                    mino = all;
                                                    omin.setText(String.valueOf(mino));
                                                }
                                                idList11.add(id);
                                                recordList11.add(all);
                                                oall = all + oall;
                                                double oidf = oid;
                                                double oallf = oall;
                                                oavg.setText(String.valueOf(Math.round(oallf / oidf)));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                } else if (flag == 0) {
                                    summary.setText("No Records");
                                    select.setText("Select Month");
                                    spin2.setAdapter(null);
                                }
                                min = 100000000;
                                max = 0;
                                overall = 0;
                                minbb = 100000000;
                                maxbb = 0;
                                bbid = 0;
                                bball = 0;
                                minab = 100000000;
                                maxab = 0;
                                abid = 0;
                                aball = 0;
                                minbl = 100000000;
                                maxbl = 0;
                                blid = 0;
                                blall = 0;
                                minal = 100000000;
                                maxal = 0;
                                alid = 0;
                                alall = 0;
                                minbs = 100000000;
                                maxbs = 0;
                                bsid = 0;
                                bsall = 0;
                                minas = 100000000;
                                maxas = 0;
                                asid = 0;
                                asall = 0;
                                minbd = 100000000;
                                maxbd = 0;
                                bdid = 0;
                                bdall = 0;
                                minad = 100000000;
                                maxad = 0;
                                adid = 0;
                                adall = 0;
                                minb = 100000000;
                                maxb = 0;
                                bid = 0;
                                ball =0;
                                mino = 100000000;
                                maxo = 0;
                                oid = 0;
                                oall = 0;
                                minf = 100000000;
                                maxf = 0;
                                fid = 0;
                                fall = 0;
                                idf = 0;
                                aid = 0;
                                idList.clear();
                                idList1.clear();
                                idList2.clear();
                                idList3.clear();
                                idList4.clear();
                                idList5.clear();
                                idList6.clear();
                                idList7.clear();
                                idList8.clear();
                                idList9.clear();
                                idList10.clear();
                                idList11.clear();
                                recordList.clear();
                                recordList1.clear();
                                recordList2.clear();
                                recordList3.clear();
                                recordList4.clear();
                                recordList5.clear();
                                recordList6.clear();
                                recordList7.clear();
                                recordList8.clear();
                                recordList9.clear();
                                recordList10.clear();
                                recordList11.clear();
                            }
                            else {
                                Log.d("MAGKAIBANG YEAR", "UGH");
                            }

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        oa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList, idList, "All Records:");
            }
        });
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList1, idList1,"Before Breakfast Records:");
            }
        });
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList2, idList2, "After Breakfast Records:");
            }
        });
        bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList3, idList3,"Before Lunch Records:");
            }
        });
        al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList4, idList4,"After Lunch Records:");
            }
        });
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList5,idList5, "Before Snack Records:");
            }
        });
        as.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList6,idList6, "After Snack Records:");
            }
        });
        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList7, idList7,"Before Dinner Records:");
            }
        });
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList8, idList8,"After Dinner Records:");
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList9,idList9, "Bedtime Records:");
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList10,idList10, "Other Records:");
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEachRecord(recordList11,idList11, "Fasting Records:");
            }
        });
        return view;
    }

    public void showEachRecord(List<Integer> list, final List<Integer> listugh, String title){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        final View miniView = LayoutInflater.from(getContext()).inflate(R.layout.custom, null);
        TextView click = (TextView) miniView.findViewById(R.id.tv_click);
        final LinearLayout miniLayout = miniView.findViewById(R.id.plusrecords);
        if (list.size() != 0){
            for (int i = 0; i < list.size(); i++){
                recordVal = list.get(i);
                int val = listugh.get(i);
                Log.d("records", String.valueOf(recordVal));
                Log.d("id", String.valueOf(val));
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                final View rowView = layoutInflater.inflate(R.layout.row_record, null);
                TextView rowrec = (TextView) rowView.findViewById(R.id.recordrow);

                final int finalI = i;
                rowrec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int idVal = listugh.get(finalI);
                        dialogTrial.dismiss();
                        userDataReference.child("records").child(String.valueOf(idVal+1)).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long lastcount = dataSnapshot.getChildrenCount();
                                int valblood = dataSnapshot.child("bloodsugar").getValue(Integer.class);
                                insulin = dataSnapshot.child("insulintype").getValue(String.class);
                                String mealtype = dataSnapshot.child("mealtype").getValue(String.class);
                                String notes = dataSnapshot.child("notes").getValue(String.class);
                                String date = dataSnapshot.child("date").getValue(String.class);


                                LayoutInflater inflater = getLayoutInflater();
                                View alertLayout = inflater.inflate(R.layout.custom2, null);
                                final EditText blood = alertLayout.findViewById(R.id.et_bs);
                                final EditText mt = alertLayout.findViewById(R.id.et_mt);
                                final EditText it = alertLayout.findViewById(R.id.et_it);
                                final EditText note = alertLayout.findViewById(R.id.et_n);
                                final TextView dr = alertLayout.findViewById(R.id.tv_dr);
                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                blood.setText(String.valueOf(valblood) + " mg/dL");
                                mt.setText(mealtype);
                                it.setText(insulin);
                                note.setText(notes);
                                dr.setText("Date Recorded: " + date);
                                alert.setTitle("Record ID: " + String.valueOf(idVal));
                                alert.setView(alertLayout);
                                alert.setCancelable(false);

                                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialog = alert.create();
                                dialog.show();
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#8d0000"));

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                });

                rowrec.setText(String.valueOf(recordVal) + " mg/dL");
                miniLayout.addView(rowView);
            }

            dialog.setView(miniView);
            dialog.setTitle(title);
            dialog.setCancelable(false);
            dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        else {
            dialog.setTitle("No Records");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        dialogTrial = dialog.create();
        dialogTrial.show();
        dialogTrial.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#8d0000"));

    }
}