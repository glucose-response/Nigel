package com.example.nigel.dataclasses;

public class FeedingDataSample extends DataSample {

    private String type;

    public FeedingDataSample(long timestamp, int nigelID, String type) {
        super(timestamp, nigelID);
        this.type = type;

    }

    public String getType() {
        return type;
    }
}