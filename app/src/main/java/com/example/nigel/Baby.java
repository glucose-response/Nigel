package com.example.nigel;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Baby {
    private int id;
    private List<Entry> timeSeriesData;

    private long birthDate;

    private double weight;

    private String group;

    public Baby(int id,
                long birthDate,
                double weight,
                String group,
                List<Entry> timeSeriesData) {
        this.id = id;
        this.birthDate = birthDate;
        this.weight = weight;
        this.group = group;
        try{
            this.timeSeriesData = timeSeriesData;
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }

    }

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

    public String getBirthDateString(){
        return convertUnixToString(birthDate);
    }

    public long getAge(){
        long now = System.currentTimeMillis()/1000;
        long age = now - birthDate;
        return age;
    }
    public String getAgeString(){
        return convertUnixToString(getAge());
    }
    /**
     * Sourced: https://www.w3resource.com/java-exercises/datetime/java-datetime-exercise-36.php
     * @return a String format of the UNIX date
     */
    private String convertUnixToString(long unix){
        Date date = new Date(unix*1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String stringDate = jdf.format(date);
        return stringDate;
    }
}
