package com.example.nigel;

import com.example.nigel.dataclasses.DataSample;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Baby implements Serializable{
    private int NigelID;
    private double gestationalAge;

    private long birthDate; // in milliseconds for graphing
    private LocalDate dateOfBirth; // for age calculation
    private String birthday; // for database communications

    private double birthWeight;
    private String notes;

    private List<DataSample> timeSeriesData;

    /**
     * Constructor for graphing
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Baby(int id, double gestationalAge, long birthDate, double weight, String notes, List<DataSample> timeSeriesData) {
        try{
            this.NigelID = id;
            this.gestationalAge = gestationalAge;
            this.birthDate = birthDate;
            this.dateOfBirth = LocalDate.ofEpochDay(birthDate);
            this.birthday = dateOfBirthToString();
            this.birthWeight = weight;
            this.notes = notes;
            if(timeSeriesData == null){
                this.timeSeriesData = new ArrayList<>();
            } else {
                this.timeSeriesData = timeSeriesData;
            }
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }

    }

    /**
     * Constructor for a Baby object to be send to the database
     * note: birthday is a string, not a localdate
     * note: timeSeriesdata is empty
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Baby(int id, double gestationalAge, String birthday, double weight, String notes) {
        try{
            this.NigelID = id;
            this.birthday = birthday;
            this.gestationalAge = gestationalAge;
            this.birthWeight = weight;
            this.notes = notes;
        } catch (NullPointerException e){
            System.out.println("Null Time Series Data");
        }
    }

    /**
     * Constructor for a Baby object for dataset
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Baby(int id, double gestationalAge, LocalDate dateOfBirth, double weight, String notes, List<DataSample> timeSeriesData) {
        try{
            this.NigelID = id;
            this.gestationalAge = gestationalAge;
            this.dateOfBirth = dateOfBirth;
            this.birthday = dateOfBirthToString();
            this.birthDate = dateOfBirth.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
            this.birthWeight = weight;
            this.notes = notes;
            if(timeSeriesData == null){
                this.timeSeriesData = new ArrayList<>();
            } else {
                this.timeSeriesData = timeSeriesData;
            }
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
    public double getGestationalAge() {
        return gestationalAge;
    }
    public long getBirthDate() {
        return birthDate;
    }
    public LocalDate getDateOfBirth() {return dateOfBirth;}
    public String getBirthday() {return birthday;}
    public double getWeight() {
        return birthWeight;
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
        this.NigelID = id;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth;}
    public void setBirthday(String birthday) {this.birthday = birthday;}
    public void setBirthDate(long birthDate){
        this.birthDate = birthDate;
    }
    public void setWeight(double weight) {
        this.birthWeight = weight;
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
        Period period = Period.between(dateOfBirth, LocalDate.of(2024,2,9));
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
