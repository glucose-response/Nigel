package com.example.nigel.dataclasses;

public class SweatSample extends DataSample {
    private Float glucoseValue; // Glucose /mM,Sodium,Lactate,Potassium,Chloride,Calcium
    private Float sodiumValue;
    private Float lactateValue;
    private Float potassiumValue;
    private Float chlorideValue;
    private Float calciumValue;
    public SweatSample(Long timestamp, int nigelID, Float glucoseValue, Float sodiumValue,Float lactateValue,
                       Float potassiumValue, Float chlorideValue, Float calciumValue) {
        super(timestamp, nigelID);
        this.glucoseValue = glucoseValue;
        this.sodiumValue = sodiumValue;
        this.lactateValue = lactateValue;
        this.potassiumValue = potassiumValue;
        this.chlorideValue = chlorideValue;
        this.calciumValue = calciumValue;
    }

    // getters
    public Float getGlucoseValue(){
        return glucoseValue;
    }
    public Float getSodiumValue(){
        return sodiumValue;
    }
    public Float getLactateValue() {
        return lactateValue;
    }
    public Float getPotassiumValue(){
        return potassiumValue;
    }
    public Float getChlorideValue(){
        return chlorideValue;
    }
    public Float getCalciumValue(){
        return  calciumValue;
    }
}