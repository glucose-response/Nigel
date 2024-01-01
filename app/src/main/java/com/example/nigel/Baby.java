package com.example.nigel;

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
    private long dateOfBirth;
    private double birthWeight;
    private double gestationalAge;
    private String notes;
    private List<Entry> timeSeriesData;

    /**
     * Constructor for a Baby object with all fields
     */
    public Baby(int NigelID, long dateOfBirth, double birthWeight, double gestationalAge, String notes, List<Entry> timeSeriesData) {
        this.NigelID = NigelID;
        this.dateOfBirth = dateOfBirth;
        this.birthWeight = birthWeight;
        this.gestationalAge = gestationalAge;
        try{
            this.timeSeriesData = timeSeriesData;
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }

    }

    /**
     * Constructor for a Baby object without TimeSeries List
     */
    public Baby(int NigelID, long dateOfBirth, double birthWeight, double gestationalAge, String notes) {
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
     * Getters and Setters
     */
    public int getId() {
        return NigelID;
    }
    public long getdateOfBirth() {
        return dateOfBirth;
    }
    public double getbirthWeight() {
        return birthWeight;
    }
    public List<Entry> getTimeSeriesData() {
        return timeSeriesData;
    }

    public void setId(int NigelID) {
        this.NigelID = NigelID;
    }

    public void setTimeSeriesData(List<Entry> timeSeriesData) {
        this.timeSeriesData = timeSeriesData;
    }

    public void setdateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setbirthWeight(double birthWeight) {
        this.birthWeight = birthWeight;
    }

    public double getGestationalAge() {return gestationalAge;}

    public void setGestationalAge(double gestationalAge) {this.gestationalAge = gestationalAge;}

    public String getdateOfBirthString(){
        return convertUnixToString(dateOfBirth);
    }

    public String getNotes() {return notes;}

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int[] getAge(){
        long difference = System.currentTimeMillis()/1000 - dateOfBirth;
        String[] details = convertUnixToString(difference).split("-");
        int size = details.length;
        int[] age = new int[size];
        for(int i = 0; i < size; i++){
            age[i] = Integer.parseInt(details[i]);
        }
        age[0] -= 1970;
        age[1] -= 1;
        return age;
    }
    /**
     * Function is "identical" to the previous function however it does not use the System
     * class as this cannot be Powermocked for testing. Hence, Line 96 says current time as
     * the 30th January 2024 (1706572800L)
     * @return an int array of the age of the baby              *
     */
    public int[] getAgeForTest(){
        long difference = 1706572800L - dateOfBirth;
        String[] details = convertUnixToString(difference).split("-");
        int size = details.length;
        int[] age = new int[size];
        for(int i = 0; i < size; i++){
            age[i] = Integer.parseInt(details[i]);
        }
        age[0] -= 1970;
        age[1] -= 1;
        return age;
    }
    /**
     * Sourced: https://www.w3resource.com/java-exercises/datetime/java-datetime-exercise-36.php
     * @return a String format of the UNIX date
     */
    public static String convertUnixToString(long unix){
        Date date = new Date(unix*1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT-0"));
        String[] stringDate = jdf.format(date).split(" ");
        return stringDate[0];
    }
}
