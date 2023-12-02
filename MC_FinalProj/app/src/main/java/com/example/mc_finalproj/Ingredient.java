package com.example.mc_finalproj;


public class Ingredient {
    private String name;
    private String measurement;
    private String size;

    public Ingredient(String name, String measurement, String size) {
        this.name = name;
        this.measurement = measurement;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getSize() {
        return size;
    }
}
