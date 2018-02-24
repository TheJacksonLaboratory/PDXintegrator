package org.jax.pdxintegrator.model.modelcreation;

public enum MouseTreatmentForEngraftment {
    ESTROGEN("Estrogen"),GCSF("Granulocyte colony-stimulating factor");

    private final String name;

    MouseTreatmentForEngraftment(String n) {
        name=n;
    }

    public String getMouseTreatmentForEngraftmentString(){ return name; }
}

