package org.jax.pdxintegrator.model.patient;

public enum Consent {
    YES("yes"),
    NO("no"),
    ACADEMIC("available to academic centers only");

    private final String name;

    Consent(String n) { name=n;}

    public String getConsentString() { return name; }
}
