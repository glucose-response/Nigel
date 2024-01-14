package com.example.nigel;

import com.example.nigel.dataclasses.DataSample;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Baby implements Serializable{
    private int id;
    private List<DataSample> timeSeriesData;
    private long birthDate;
    private LocalDate dateOfBirth;
    private String birthday;
    private double weight;
    private double gestationalAge;
    private String notes;

    public Baby(int id,
                double gestationalAge,
                long birthDate,
                double weight,
                String notes,
                List<DataSample> timeSeriesData) {
        this.id = id;
        this.gestationalAge = gestationalAge;
        this.birthDate = birthDate;
        this.weight = weight;
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
    public Baby(int id, String birthday, double weight, double gestationalAge, String notes) {
        try{
            this.id = id;
            this.birthday = birthday;
            this.gestationalAge = gestationalAge;
            this.weight = weight;
            this.notes = notes;
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }
    }

    /**
     * Constructor for a Baby object without TimeSeries List
     */
    public Baby(int id, LocalDate dateOfBirth, double weight, double gestationalAge, String notes) {
        try{
            this.id = id;
            this.dateOfBirth = dateOfBirth;
            this.gestationalAge = gestationalAge;
            this.weight = weight;
            this.notes = notes;
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }
    }

    /**
     * Getters
     */
    public int getId() {
        return id;
    }
    public double getGestationalAge() {
        return gestationalAge;
    }
    public long getBirthDate() {
        return birthDate;
    }
    public double getWeight() {
        return weight;
    }
    public String getNotes() {
        return notes;
    }

    public List<DataSample> getTimeSeriesData() {
        return timeSeriesData;
    }

    /**
     * Setters
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setBirthDate(long birthDate){
        this.birthDate = birthDate;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public void setGestationalAge(double gestationalAge) {this.gestationalAge = gestationalAge;}

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setTimeSeriesData(List<DataSample> timeSeriesData) {
        this.timeSeriesData = timeSeriesData;
    }
    public void insertEvent(DataSample dataSample){
        timeSeriesData.add(dataSample);
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
