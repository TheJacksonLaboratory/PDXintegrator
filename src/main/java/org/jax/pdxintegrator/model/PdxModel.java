package org.jax.pdxintegrator.model;

import org.jax.pdxintegrator.model.patient.PdxPatient;

/**
 * This is the central class that coordinates the RDF model of a PDX case.
 */
public class PdxModel {

    private final PdxPatient patient;


    public PdxModel(PdxPatient pat){
        this.patient=pat;
    }


}
