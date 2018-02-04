package org.jax.pdxintegrator.model.patient;

public enum EthnicityRace {
    LATINO("hispanic or latino"),
        EUROPEAN("European/caucasian"),
    SEPHARDIC("Sephardic"),
    INTERCONTINENTAL("Intercontinental");

    private final String name;

    EthnicityRace(String n) { name=n;}

    public String getEthnicityString() { return name; }
}
