package org.jax.pdxintegrator.model.patient;

/**
 * Note that we have Female (Code C16576)
 */
public enum Gender {
    FEMALE("female"), MALE("male");

    private final String name;

    Gender(String n) {
        name=n;
    }

    public String getGenderString(){ return name; }
}
