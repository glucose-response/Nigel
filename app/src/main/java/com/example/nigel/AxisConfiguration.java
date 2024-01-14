package com.example.nigel;

// AxisConfiguration.java
/** This class is used to configure the axis of the graph*/
public class AxisConfiguration {

    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    /**Constructor*/
    public AxisConfiguration(float minX, float maxX, float minY, float maxY) {
        // Negative values are set to 0.
        this.minX = (minX < 0) ? 0 : minX;
        this.maxX = (maxX < 0) ? 0 : maxX;
        this.minY = (minY < 0) ? 0 : minY;
        this.maxY = (maxY < 0) ? 0 : maxY;
    }

    /**Getters*/
    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }
}
