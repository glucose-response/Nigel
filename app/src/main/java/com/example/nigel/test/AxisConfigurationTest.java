package com.example.nigel.test;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nigel.AxisConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AxisConfigurationTest {

    /**
     * Tests the AxisConfiguration class
     */
    AxisConfiguration axisConfiguration;
    AxisConfiguration faultyAxisConfiguration;

    @BeforeEach
    public void setUp(){
        axisConfiguration = new AxisConfiguration(4,5,2,1);
        faultyAxisConfiguration = new AxisConfiguration(-4,-5,-2,-1);
    }

    @Test
    void getMinX() {
        assertEquals(4,axisConfiguration.getMinX());
    }

    @Test
    void getMaxX() {
        assertEquals(5,axisConfiguration.getMaxX());
    }

    @Test
    void getMinY() {
        assertEquals(2,axisConfiguration.getMinY());
    }

    @Test
    void getMaxY() {
        assertEquals(1,axisConfiguration.getMaxY());
    }

    @Test
    void negativeMinX() {
        assertEquals(0,faultyAxisConfiguration.getMinX());
    }
    @Test
    void negativeMaxX() {
        assertEquals(0,faultyAxisConfiguration.getMaxX());
    }
    @Test
    void negativeMinY() {
        assertEquals(0,faultyAxisConfiguration.getMinY());
    }
    @Test
    void negativeMaxY() {
        assertEquals(0,faultyAxisConfiguration.getMaxY());
    }
}