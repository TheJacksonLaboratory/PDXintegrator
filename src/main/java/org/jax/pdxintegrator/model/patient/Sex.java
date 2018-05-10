package org.jax.pdxintegrator.model.patient;

/**
 * Note that we have Female (Code C16576)
 */
public enum Sex {
    FEMALE("female"), MALE("male");

    private final String name;

    Sex(String n) {
        name=n;
    }

    public String getGenderString(){ return name; }
}
