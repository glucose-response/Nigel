package com.example.testingthings;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

public class Person {
    private int id;
    private String name;
    private List<Entry> timeSeriesData;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Person(String name, List<Entry> timeSeriesData) {
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
