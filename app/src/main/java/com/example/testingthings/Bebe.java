package com.example.testingthings;

import com.github.mikephil.charting.data.Entry;

import java.io.Serializable;
import java.util.List;

public class Bebe{
    private int id;
    private String name;
    private List<Entry> timeSeriesData;

    public Bebe(int id, String name, List<Entry> timeSeriesData) {
        this.id = id;
        this.name = name;
        this.timeSeriesData = timeSeriesData;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Entry> getTimeSeriesData() {
        return timeSeriesData;
    }
}
