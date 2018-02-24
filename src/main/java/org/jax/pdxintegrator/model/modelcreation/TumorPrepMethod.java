package org.jax.pdxintegrator.model.modelcreation;

public enum TumorPrepMethod {

    SOLID("Solid"), SUSPENSION("Suspension"), ASCITES("Ascites");

    private final String name;

    TumorPrepMethod(String n) {
        name=n;
    }

    public String getTumorPrepMethodString(){ return name; }
}
