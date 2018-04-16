package com.example.asus.insulintimer;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Asus on 3/10/2018.
 */

public class AlarmData {
    private ArrayList<String> days = new ArrayList<>();
    private Boolean isOn = true;
    private int hour, minute, counter;
    private String am_pm;


    public AlarmData() {
    }

    public AlarmData(int hour, int minute, ArrayList<String>days, Boolean isOn, String am_pm, int counter){
        this.hour= hour;
        this.minute= minute;
        this.days= days;
        this.isOn= isOn;
        this.am_pm= am_pm;
        this.counter= counter;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public Boolean getOn() {
        return isOn;
    }

    public void setOn(Boolean on) {
        isOn = on;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

}
