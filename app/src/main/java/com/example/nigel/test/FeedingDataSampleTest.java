package com.example.nigel.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.example.nigel.dataclasses.FeedingDataSample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FeedingDataSampleTest {

    FeedingDataSample feedingDataSample;
    FeedingDataSample feedingDataSample2;

    @BeforeEach
    void setUp() {
        feedingDataSample = new FeedingDataSample(1l, 1, "bolus");
        feedingDataSample2 = new FeedingDataSample(2l, 2, "bottle");
    }
    @Test
    void notNull() {
        assertNotEquals(feedingDataSample,null);
    }
    @Test
    void getTimeStamp() {
        assertEquals(feedingDataSample.getTimestamp(),1l);
    }
    @Test
    void getNigelID() {
        assertEquals(feedingDataSample2.getNigelID(),2);
    }
    @Test
    void getType() {
        assertEquals(feedingDataSample2.getType(),"bottle");
    }
    @Test
    void notEquals() {
        assertNotEquals(feedingDataSample2, feedingDataSample);
    }
}
