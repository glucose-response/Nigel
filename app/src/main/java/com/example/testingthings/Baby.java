package com.example.testingthings;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

public class Baby {
    private int id;
    private List<Entry> timeSeriesData;

    private int birthDate;

    private float weight;

    private String group;

    public Baby(int id,
                int birthDate,
                float weight,
                String group,
                List<Entry> timeSeriesData) {
        this.id = id;
        this.birthDate = birthDate;
        this.weight = weight;
        this.group = group;
        this.timeSeriesData = timeSeriesData;
    }

    public int getId() {
        return id;
    }
    public int getBirthDate() {
        return birthDate;
    }
    public float getWeight() {
        return weight;
    }
    public String getGroup() {
        return group;
    }
    public List<Entry> getTimeSeriesData() {
        return timeSeriesData;
    }
}
