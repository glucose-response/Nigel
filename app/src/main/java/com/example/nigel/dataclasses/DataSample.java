package com.example.nigel.dataclasses;

public abstract class DataSample {
    // Make a class that has to have a timestamp variable, and a constructor that must take in a timestamp
    private long timestamp;
    private int NigelID;

    public DataSample(long timestamp, int nigelID) {
        this.timestamp = timestamp;
        this.NigelID = nigelID;
    }

    // Make a method that returns the timestamp
    public long getTimestamp() {
        return timestamp;
    }

    public int getNigelID() {return NigelID;}
}