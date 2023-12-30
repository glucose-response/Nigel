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
    private int id;
    private List<Entry> timeSeriesData;
    private long birthDate;
    private double weight;
    private double gestationalAge;
    private String group;

    /**
     * Constructor for a Baby object with all fields
     */
    public Baby(int id, long birthDate, double weight, double gestationalAge, String group, List<Entry> timeSeriesData) {
        this.id = id;
        this.birthDate = birthDate;
        this.weight = weight;
        this.gestationalAge = gestationalAge;
        this.group = group;
        try{
            this.timeSeriesData = timeSeriesData;
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }

    }

    /**
     * Constructor for a Baby object without TimeSeries List
     */
    public Baby(int id, long birthDate, double weight, double gestationalAge, String group) {
        try{
            this.id = id;
            this.birthDate = birthDate;
            this.gestationalAge = gestationalAge;
            this.weight = weight;
            this.group = group;
            this.timeSeriesData = new ArrayList<Entry>();
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }
    }

    /**
     * Getters and Setters
     */
    public int getId() {
        return id;
    }
    public long getBirthDate() {
        return birthDate;
    }
    public double getWeight() {
        return weight;
    }
    public String getGroup() {
        return group;
    }
    public List<Entry> getTimeSeriesData() {
        return timeSeriesData;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeSeriesData(List<Entry> timeSeriesData) {
        this.timeSeriesData = timeSeriesData;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public double getGestationalAge() {return gestationalAge;}

    public void setGestationalAge(double gestationalAge) {this.gestationalAge = gestationalAge;}

    public String getBirthDateString(){
        return convertUnixToString(birthDate);
    }

    public int[] getAge(){
        long difference = System.currentTimeMillis()/1000 - birthDate;
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
        long difference = 1706572800L - birthDate;
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
