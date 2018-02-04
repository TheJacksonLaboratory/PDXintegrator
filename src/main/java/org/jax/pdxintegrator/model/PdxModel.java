package org.jax.pdxintegrator.model;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.jax.pdxintegrator.model.patient.Consent;
import org.jax.pdxintegrator.model.patient.Gender;
import org.jax.pdxintegrator.model.patient.PdxPatient;

/**
 * This is the central class that coordinates the RDF model of a PDX case.
 */
public class PdxModel {


    private final PdxPatient patient;




    public PdxModel(PdxPatient pat){
        this.patient=pat;
    }


    public PdxPatient getPatient() {
        return patient;
    }
}
