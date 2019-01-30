package org.jax.pdxintegrator.model.patient;

public enum Ethnicity {
    LATINO("Hispanic or Latino"), 
    NOTLATINO("Not Hispanic or Latino"),
    UNKNOWN("Unknown");

    private final String name;

    Ethnicity(String n) {
        name = n;
    }

    public String getEthnicityString() {
        return name;
    }
}
