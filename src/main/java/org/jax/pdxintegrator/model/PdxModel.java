package org.jax.pdxintegrator.model;


import java.util.ArrayList;
import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.modelstudy.PdxModelStudy;
import org.jax.pdxintegrator.model.omicsfile.PdxOmicsFile;
import org.jax.pdxintegrator.model.patient.PdxPatient;
import org.jax.pdxintegrator.model.qualityassurance.PdxQualityAssurance;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;

/**
 * This is the central class that coordinates the RDF model of a PDX case.
 */
public class PdxModel {


    private final PdxPatient patient;  // contains patient treatments
    private final ArrayList<PdxClinicalTumor> clinicalTumors;
    private final ArrayList<PdxModelCreation> modelCreations;
    private final ArrayList<PdxQualityAssurance> qualityAssurances;
    private final ArrayList<PdxModelStudy> modelStudies;  // contains study treatmnets
    private final ArrayList<PdxOmicsFile> omicsFiles;
    private final String PDTC;


    public PdxModel(String pdtc, PdxPatient pat, ArrayList<PdxClinicalTumor> clinTum, ArrayList<PdxModelCreation> mcreation, ArrayList<PdxQualityAssurance> quality, ArrayList<PdxModelStudy> modelStudy, ArrayList<PdxOmicsFile> omicsFiles){
        this.PDTC = pdtc;
        this.patient=pat;
        this.clinicalTumors=clinTum;
        this.modelCreations=mcreation;
        this.qualityAssurances=quality;
        this.modelStudies = modelStudy;
        this.omicsFiles = omicsFiles;
    }


    public PdxPatient getPatient() {
        return patient;
    }
    public ArrayList<PdxClinicalTumor> getClinicalTumor() { return clinicalTumors; }
    public ArrayList<PdxModelCreation> getModelCreation() { return modelCreations;}
    public ArrayList<PdxQualityAssurance> getQualityAssurance() { return qualityAssurances; }
    public ArrayList<PdxModelStudy> getModelStudy() { return modelStudies;}
    public ArrayList<PdxOmicsFile> getOmicsFiles() { return omicsFiles;}
    public String getPDTC(){return PDTC;}

}
