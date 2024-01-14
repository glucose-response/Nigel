package com.example.nigel.test;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nigel.dataclasses.BloodSample;
import com.example.nigel.dataclasses.DataSample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BloodSampleTest {

    /**
     * Tests the BloodSample class
     */
    BloodSample bloodSample;
    @BeforeEach
    void setUp() {
        bloodSample = new BloodSample(2l, 2, 3f, 4f);
    }
    @Test
    void getGlucoseValue() {
        assertEquals(bloodSample.getGlucoseValue(), 3f);
    }

    @Test
    void getLactateValue() {
        assertEquals(bloodSample.getLactateValue(), 4f);
    }
    @Test
    void getID() {
        assertEquals(bloodSample.getNigelID(), 2);
    }
    @Test
    void getTimeStamp() {
        assertEquals(bloodSample.getTimestamp(), 2l);
    }
    @Test
    void checkType() {
        assertEquals(bloodSample.getClass(), BloodSample.class);
    }
    @Test
    void checkSuperType() {
        assertTrue(bloodSample instanceof DataSample);
    }
    @Test
    void notEquals() {
        BloodSample bloodSample2 = new BloodSample(3l, 3, 4f, 5f);
        assertNotEquals(bloodSample, bloodSample2);
    }
}