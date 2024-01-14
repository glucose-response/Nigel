package com.example.nigel.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.nigel.dataclasses.BloodSample;
import com.example.nigel.dataclasses.DataSample;
import com.example.nigel.dataclasses.SweatSample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SweatSampleTest {

    /**
     * Tests the SweatSample class
     */
    SweatSample sweatSample;
    @BeforeEach
    void setUp() {
        sweatSample = new SweatSample(2l, 2, 3f, 4f, 5f, 6f, 7f, 8f);
    }

    @Test
    void checkNotNull() {
        assertNotEquals(sweatSample,null);
    }
    @Test
    void getID() {
        assertEquals(sweatSample.getNigelID(), 2);
    }
    @Test
    void getTimeStamp() {
        assertEquals(sweatSample.getTimestamp(), 2l);
    }
    @Test
    void getGlucoseValue() {
        assertEquals(sweatSample.getGlucoseValue(), 3f);
    }

    @Test
    void getSodiumValue() {
        assertEquals(sweatSample.getSodiumValue(), 4f);
    }
    @Test
    void getLactateValue() {
        assertEquals(sweatSample.getLactateValue(), 5f);
    }
    @Test
    void getPotassiumValue() {
        assertEquals(sweatSample.getPotassiumValue(), 6f);
    }
    @Test
    void getChlorideValue() {
        assertEquals(sweatSample.getChlorideValue(), 7f);
    }
    @Test
    void getCalciumValue() {
        assertEquals(sweatSample.getCalciumValue(), 8f);
    }
    @Test
    void checkType() {
        assertEquals(sweatSample.getClass(), SweatSample.class);
    }
    @Test
    void checkSuperType() {
        //Checks that the class extends DataSample
        assertTrue(sweatSample instanceof DataSample);
    }
    @Test
    void notEquals() {
        //Checks addresses are not equal
        SweatSample sweatSample2 = new SweatSample(2l, 2, 3f, 4f, 5f, 6f, 7f, 8f);
        assertNotEquals(sweatSample, sweatSample2);
    }
}