package com.example.asus.insulintimer;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Asus on 3/10/2018.
 */

public class AlarmAdapter extends RecyclerView.Adapter <AlarmAdapter.MyViewHolder> {
    public List<AlarmData> alarmArrayList;
    private String user;
    private PendingIntent pendingIntent;
    private Context context;
    public AlarmManager alarmManager;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbr = db.getReference();
    private static final int ALARM_REQUEST_CODE = 133;
    private GestureDetector gestureDetector;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView timeTxt, dayTxt, ampmtxt;
        public Switch isOn;
        public View mView;


        public MyViewHolder(View view) {
            super(view);
            timeTxt = (TextView) view.findViewById(R.id.tv_time);
            dayTxt = (TextView) view.findViewById(R.id.tv_alarm);
            ampmtxt = (TextView) view.findViewById(R.id.am_pm);
            mView = view;


        }



        public void setToggle(boolean Switch) {
            isOn = (Switch) mView.findViewById(R.id.isOn);
            isOn.setChecked(Switch);
            if (alarmArrayList.get(getAdapterPosition()).getOn() == true){
                int hourOfUser = alarmArrayList.get(getAdapterPosition()).getHour();
                int minuteOfUser = alarmArrayList.get(getAdapterPosition()).getMinute();
                Calendar calendar = Calendar.getInstance();
                int hourOfPhone = calendar.get(Calendar.HOUR_OF_DAY); //24hr format
                int minOfPhone = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);

                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(context, alarmArrayList.get(getAdapterPosition()).getCounter(), alarmIntent, 0);
                int finalhour;
                int finalminute;
                int mintosec;
                int hourtosec;
                int finaltime;

                if (hourOfUser > hourOfPhone){
                    if (minuteOfUser > minOfPhone){
                        finalhour = hourOfUser - hourOfPhone;
                        finalminute = Math.abs(minuteOfUser - minOfPhone);
                        mintosec = finalminute * 60;
                        hourtosec = finalhour * 60 * 60;
                        finaltime = mintosec + hourtosec - seconds;
                    }
                    else if (minuteOfUser == minOfPhone){
                        finalhour = hourOfUser - hourOfPhone;
                        hourtosec = finalhour * 60 * 60;
                        finaltime = hourtosec - seconds;

                    }
                    else{
                        finalhour = hourOfUser - hourOfPhone - 1;
                        finalminute = 60 - ( minOfPhone- minuteOfUser);
                        mintosec = finalminute * 60;
                        hourtosec = finalhour * 60 * 60;
                        finaltime =  mintosec + hourtosec - seconds;
                    }

                }
                else if (hourOfPhone > hourOfUser){
                    if (minuteOfUser > minOfPhone){
                        finalhour= hourOfPhone - hourOfUser ;
                        finalminute= Math.abs(minuteOfUser - minOfPhone);
                        mintosec = finalminute * 60;
                        hourtosec = (24-finalhour) * 60 * 60;
                        finaltime = mintosec + hourtosec - seconds;
                    }
                    else if (minuteOfUser == minOfPhone){
                        finalhour= hourOfPhone - hourOfUser ;
                        hourtosec = (24-finalhour) * 60 * 60;
                        finaltime = hourtosec - seconds;

                    }
                    else{
                        finalhour= hourOfPhone - hourOfUser ;
                        finalminute = 60 - ( minOfPhone- minuteOfUser);
                        mintosec = finalminute * 60;
                        hourtosec = (24-finalhour-1) * 60 * 60;
                        finaltime = mintosec + hourtosec - seconds;
                    }
                }
                else{
                    if(minOfPhone > minuteOfUser) {
                        finalminute =  60-(minOfPhone - minuteOfUser);
                        mintosec = finalminute * 60;
                        hourtosec = 23 * 60 * 60;
                        finaltime = mintosec + hourtosec - seconds;

                    }
                    else if (minuteOfUser > minOfPhone){
                        finalminute = minuteOfUser - minOfPhone;
                        mintosec = finalminute * 60;
                        finaltime = mintosec - seconds;
                    }
                    else{
                        finaltime = 0;
                    }

                }
                triggerAlarmManager(finaltime);
            }
            else{
                Log.d("ALARMS ARE OFF", "NAKAOFF ALARM");
            }

            isOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();

                    dbr.child("users").child(user).child("schedules").child(String.valueOf(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Boolean val = dataSnapshot.child("on").getValue(Boolean.class);
                            if (val == false) {
                                dbr.child("users").child(user).child("schedules").child(String.valueOf(position)).child("on").setValue(true);
                                val = true;
                                int hourOfUser = alarmArrayList.get(getAdapterPosition()).getHour();
                                int minuteOfUser = alarmArrayList.get(getAdapterPosition()).getMinute();
                                Calendar calendar = Calendar.getInstance();
                                int hourOfPhone = calendar.get(Calendar.HOUR_OF_DAY); //24hr format
                                int minOfPhone = calendar.get(Calendar.MINUTE);
                                int seconds = calendar.get(Calendar.SECOND);

                                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                                pendingIntent = PendingIntent.getBroadcast(context, alarmArrayList.get(getAdapterPosition()).getCounter(), alarmIntent, 0);
                                int finalhour;
                                int finalminute;
                                int mintosec;
                                int hourtosec;
                                int finaltime;

                                if (hourOfUser > hourOfPhone){
                                    if (minuteOfUser > minOfPhone){
                                        finalhour = hourOfUser - hourOfPhone;
                                        finalminute = Math.abs(minuteOfUser - minOfPhone);
                                        mintosec = finalminute * 60;
                                        hourtosec = finalhour * 60 * 60;
                                        finaltime = mintosec + hourtosec - seconds;
                                    }
                                    else if (minuteOfUser == minOfPhone){
                                        finalhour = hourOfUser - hourOfPhone;
                                        hourtosec = finalhour * 60 * 60;
                                        finaltime = hourtosec - seconds;

                                    }
                                    else{
                                        finalhour = hourOfUser - hourOfPhone - 1;
                                        finalminute = 60 - ( minOfPhone- minuteOfUser);
                                        mintosec = finalminute * 60;
                                        hourtosec = finalhour * 60 * 60;
                                        finaltime =  mintosec + hourtosec - seconds;
                                    }

                                }
                                else if (hourOfPhone > hourOfUser){
                                    if (minuteOfUser > minOfPhone){
                                        finalhour= hourOfPhone - hourOfUser ;
                                        finalminute= Math.abs(minuteOfUser - minOfPhone);
                                        mintosec = finalminute * 60;
                                        hourtosec = (24-finalhour) * 60 * 60;
                                        finaltime = mintosec + hourtosec - seconds;
                                    }
                                    else if (minuteOfUser == minOfPhone){
                                        finalhour= hourOfPhone - hourOfUser ;
                                        hourtosec = (24-finalhour) * 60 * 60;
                                        finaltime = hourtosec - seconds;

                                    }
                                    else{
                                        finalhour= hourOfPhone - hourOfUser ;
                                        finalminute = 60 - ( minOfPhone- minuteOfUser);
                                        mintosec = finalminute * 60;
                                        hourtosec = (24-finalhour-1) * 60 * 60;
                                        finaltime = mintosec + hourtosec - seconds;
                                    }
                                }
                                else{
                                    if(minOfPhone > minuteOfUser) {
                                        finalminute =  60-(minOfPhone - minuteOfUser);
                                        mintosec = finalminute * 60;
                                        hourtosec = 23 * 60 * 60;
                                        finaltime = mintosec + hourtosec - seconds;

                                    }
                                    else if (minuteOfUser > minOfPhone){
                                        finalminute = minuteOfUser - minOfPhone;
                                        mintosec = finalminute * 60;
                                        finaltime = mintosec - seconds;
                                    }
                                    else{
                                        finaltime = 0;
                                    }

                                }
                                triggerAlarmManager(finaltime);

                            } else {
                                dbr.child("users").child(user).child("schedules").child(String.valueOf(position)).child("on").setValue(false);
                                val = false;
                                Intent cancelIntent = new Intent(context, AlarmReceiver.class);
                                pendingIntent = PendingIntent.getBroadcast(context,
                                        alarmArrayList.get(getAdapterPosition()).getCounter(),
                                        cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                pendingIntent.cancel();
                                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
                                alarmManager.cancel(pendingIntent);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }


    public AlarmAdapter(List<AlarmData> alarmArrayList, String user, Context context) {
        this.alarmArrayList = alarmArrayList;
        this.user = user;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AlarmData al = alarmArrayList.get(position);

        int hour = al.getHour();
        if (hour == 0) {
            hour += 12;
        } else if (hour > 12) {
            hour -= 12;
        }
        if (al.getMinute() < 10){
            holder.timeTxt.setText(hour + ":" + "0"+ al.getMinute() + "");
        }
        else{
            holder.timeTxt.setText(hour + ":" + al.getMinute() + "");
        }
        holder.ampmtxt.setText(al.getAm_pm());
        holder.setToggle(al.getOn());


    }

    @Override
    public int getItemCount() {
        return alarmArrayList.size();
    }

    public void triggerAlarmManager(int alarmTriggerTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, alarmTriggerTime);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

    }

    private static void order(List<AlarmData> persons) {

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
    }
}
