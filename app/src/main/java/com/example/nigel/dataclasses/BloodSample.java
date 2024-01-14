package com.example.nigel.dataclasses;

import java.io.Serializable;

public class BloodSample extends DataSample implements Serializable {
    // Make a class that has a constructor that takes in a timestamp and a glucose value
    private Float glucoseValue;
    private Float lactateValue;

    public BloodSample(
            long timestamp,
            int nigelID,
            float glucoseValue,
            float lactateValue) {
        super(timestamp, nigelID);
        this.glucoseValue = glucoseValue;
        this.lactateValue = lactateValue;

    }
    // Make a method that returns the glucose value
    public Float getGlucoseValue() {
        return glucoseValue;
    }

    public Float getLactateValue() {
        return lactateValue;
    }


}
