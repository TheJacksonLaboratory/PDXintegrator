package org.jax.pdxintegrator.model.qualityassurance;

public enum ModelCharacterization {

    IHC("Immunohistochemistry"),
    HISTOLOGY("Histology");

    private final String name;

    ModelCharacterization(String n) {
        name=n;
    }

    public String getModelCharacterizationString(){ return name; }
}
