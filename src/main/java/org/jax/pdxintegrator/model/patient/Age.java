package org.jax.pdxintegrator.model.patient;

/**
 * TODO we need to decide how to represent Age. Is it a five year bin? Is it age at diagnosis or
 * age at specimen collection?
 */
public enum Age {
    age0_4("0-4 years"),
    age5_9("5-9 years"),
    age10_14("10-14 years"),
    age_15_19("15-19 years"),
    age20_24("20-24 years");


    private final String name;

    Age(String n) {
        name=n;
    }

    public String getAgeString(){ return name; }
}
