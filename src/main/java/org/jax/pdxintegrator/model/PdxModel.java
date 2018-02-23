package org.jax.pdxintegrator.model;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.jax.pdxintegrator.model.patient.Consent;
import org.jax.pdxintegrator.model.patient.Gender;
import org.jax.pdxintegrator.model.patient.PdxPatient;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;

/**
 * This is the central class that coordinates the RDF model of a PDX case.
 */
public class PdxModel {


    private final PdxPatient patient;
    private final PdxClinicalTumor clinicalTumor;



    public PdxModel(PdxPatient pat, PdxClinicalTumor clinTum){
        this.patient=pat;
        clinicalTumor=clinTum;
    }


    public PdxPatient getPatient() {
        return patient;
    }

    public PdxClinicalTumor getClinicalTumor() { return clinicalTumor; }
}
