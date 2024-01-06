package com.example.nigel;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Baby {
    private int NigelID;
    private LocalDate dateOfBirth;
    private String birthday;
    private double birthWeight;
    private double gestationalAge;
    private String notes;
    private List<Entry> timeSeriesData;

    /**
     * Constructor for a Baby object with all fields
     */
    public Baby(int NigelID, LocalDate dateOfBirth, double birthWeight, double gestationalAge, String notes, List<Entry> timeSeriesData) {
        this.NigelID = NigelID;
        this.dateOfBirth = dateOfBirth;
        this.birthWeight = birthWeight;
        this.gestationalAge = gestationalAge;
        this.notes = notes;
        try{
            this.timeSeriesData = timeSeriesData;
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }

    }

    /**
     * Constructor for a Baby object for database
     */
    public Baby(int NigelID, String birthday, double birthWeight, double gestationalAge, String notes) {
        try{
            this.NigelID = NigelID;
            this.birthday = birthday;
            this.gestationalAge = gestationalAge;
            this.birthWeight = birthWeight;
            this.notes = notes;
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }
    }

    /**
     * Constructor for a Baby object without TimeSeries List
     */
    public Baby(int NigelID, LocalDate dateOfBirth, double birthWeight, double gestationalAge, String notes) {
        try{
            this.NigelID = NigelID;
            this.dateOfBirth = dateOfBirth;
            this.gestationalAge = gestationalAge;
            this.birthWeight = birthWeight;
            this.notes = notes;
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }
    }

    /**
     * Getters
     */
    public int getId() {
        return NigelID;
    }
    public LocalDate getDateOfBirth() {return dateOfBirth;}
    public double getBirthWeight() {
        return birthWeight;
    }
    public List<Entry> getTimeSeriesData() {
        return timeSeriesData;
    }
    public double getGestationalAge() {return gestationalAge;}
    public String getNotes() {return notes;}

    /**
     * Setters
     */
    public void setId(int NigelID) {
        this.NigelID = NigelID;
    }
    public void setTimeSeriesData(List<Entry> timeSeriesData) {
        this.timeSeriesData = timeSeriesData;}
    public void setdateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setbirthWeight(double birthWeight) {
        this.birthWeight = birthWeight;
    }
    public void setGestationalAge(double gestationalAge) {this.gestationalAge = gestationalAge;}
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Sourced: ChatGPT
     * Function finds the age of the baby and returns a reader friendly string
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getAge(){
        Period period = Period.between(dateOfBirth, LocalDate.now());
        return period.getYears() + " years, " + period.getMonths() + " months, " + period.getDays() + " days";
    }
    /**
     * Sourced: ChatGPT
     * Function finds the age of the baby and returns a reader friendly string
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getAgeForTest(){
        Period period = Period.between(dateOfBirth, LocalDate.of(2024,2,1));
        return period.getYears() + " years, " + period.getMonths() + " months, " + period.getDays() + " days";
    }

    /**
     * Function returns a readable string of the date of birth
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String dateOfBirthToString(){
        int year = dateOfBirth.getYear();
        int month = dateOfBirth.getMonthValue();
        int day = dateOfBirth.getDayOfMonth();
        String dateOfBirth = year + "-" + month + "-" + day;
        return dateOfBirth;
    }
}
