package org.jax.pdxintegrator.model.patient;

public enum Race {
    WHITE("White"),
    ASIAN("Asian"), 
    BAA("Black or African American"), 
    AMINALNA("American Indian or Alaska Native"), 
    NHOPI("Native Hawaiian or Other Pacific Islander"), 
    UNKNOWN("Unknown");

    private final String name;

    Race(String n) {
        name = n;
    }

    public String getRaceString() {
        return name;
    }
}
