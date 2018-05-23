package org.jax.pdxintegrator.model;


import java.util.ArrayList;
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
    private final ArrayList<PdxClinicalTumor> clinicalTumor;
    private final ArrayList<PdxModelCreation> modelCreation;
    private final ArrayList<PdxQualityAssurance> qualityAssurance;
    private final ArrayList<PdxModelStudy> modelStudy;



    public PdxModel(PdxPatient pat, ArrayList<PdxClinicalTumor> clinTum, ArrayList<PdxModelCreation> mcreation, ArrayList<PdxQualityAssurance> quality, ArrayList<PdxModelStudy> modelStudy){
        this.patient=pat;
        this.clinicalTumor=clinTum;
        this.modelCreation=mcreation;
        this.qualityAssurance=quality;
        this.modelStudy = modelStudy;

    }


    public PdxPatient getPatient() {
        return patient;
    }
    public ArrayList<PdxClinicalTumor> getClinicalTumor() { return clinicalTumor; }
    public ArrayList<PdxModelCreation> getModelCreation() { return modelCreation;}
    public ArrayList<PdxQualityAssurance> getQualityAssurance() { return qualityAssurance; }
    public ArrayList<PdxModelStudy> getModelStudy() { return modelStudy;}

}
