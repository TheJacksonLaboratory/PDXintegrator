package org.jax.pdxintegrator.model;


import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.modelstudy.PdxModelStudy;
import org.jax.pdxintegrator.model.patient.PdxPatient;
import org.jax.pdxintegrator.model.qualityassurance.PdxQualityAssurance;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;

/**
 * This is the central class that coordinates the RDF model of a PDX case.
 */
public class PdxModel {


    private final PdxPatient patient;
    private final PdxClinicalTumor clinicalTumor;
    private final PdxModelCreation modelCreation;
    private final PdxQualityAssurance qualityAssurance;
    private final PdxModelStudy modelStudy;



    public PdxModel(PdxPatient pat, PdxClinicalTumor clinTum, PdxModelCreation mcreation, PdxQualityAssurance quality, PdxModelStudy modelStudy){
        this.patient=pat;
        this.clinicalTumor=clinTum;
        this.modelCreation=mcreation;
        this.qualityAssurance=quality;
        this.modelStudy = modelStudy;

    }


    public PdxPatient getPatient() {
        return patient;
    }
    public PdxClinicalTumor getClinicalTumor() { return clinicalTumor; }
    public PdxModelCreation getModelCreation() { return modelCreation;}
    public PdxQualityAssurance getQualityAssurance() { return qualityAssurance; }
    public PdxModelStudy getModelStudy() { return modelStudy;}

}
