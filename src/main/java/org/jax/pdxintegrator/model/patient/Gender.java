package org.jax.pdxintegrator.model.patient;

public enum Gender {
    FEMALE("female"), MALE("male");

    private final String name;

    Gender(String n) {
        name=n;
    }

    public String getGenderString(){ return name; }
}
