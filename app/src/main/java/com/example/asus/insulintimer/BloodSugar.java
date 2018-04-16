package com.example.asus.insulintimer;

/**
 * Created by Mharjorie Sandel on 28/03/2018.
 */

public class BloodSugar {
    private int bloodsugar,id;
    private String mealtype, insulintype, notes, date;

    public BloodSugar(){

    }

    public BloodSugar(int id, int bloodsugar, String mealtype, String insulintype, String notes, String date){
        this.id = id;
        this.bloodsugar = bloodsugar;
        this.mealtype = mealtype;
        this.insulintype = insulintype;
        this.notes = notes;
        this.date = date;
    }
    public BloodSugar(int id, int bloodsugar, String mealtype, String insulintype, String date){
        this.id = id;
        this.bloodsugar = bloodsugar;
        this.mealtype = mealtype;
        this.insulintype = insulintype;
        this.date = date;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public int getBloodsugar(){
        return this.bloodsugar;
    }
    public String getMealtype(){
        return this.mealtype;
    }
    public String getInsulintype(){
        return this.insulintype;
    }
    public String getNotes(){
        return this.notes;
    }
    public void setBloodsugar(int bloodsugar){
        this.bloodsugar = bloodsugar;
    }
    public void setMealtype(String mealtype){
        this.mealtype = mealtype;
    }
    public void setInsulintype(String insulintype){
        this.insulintype = insulintype;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }

}
