package org.jax.pdxintegrator.model.tumor;

import com.github.phenomics.ontolib.ontology.data.TermId;

/**
 * Main class for modeling the Clinical/Tumor part of the PDX-MI
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxClinicalTumor {


    private String patientID;
    private String submitterTumorID;
    /** The term id for an UBERON term for the anatomical tissue of origin of the tumor. */
    private String tissueOfOrigin; // primary tissue
    private TermId tissueOfOriginTerm;
    /** NCIT Term id for primary, recurrence, metastasis. */
    private TermId categoryTerm; // disease progression
    private String specimenTissue;
    private TermId specimenTissueTerm;
    
    private String tissueHistology;
    /** NCIT Term id for tissue histology */
    private TermId tissueHistologyTerm;
    
    private String clinicalMarkers;
    private String tumorGrade;
    private String tStage;
    private String nStage;
    private String mStage;
    private String overallStage;
    private boolean treatmentNaive;
    private String sampleType;
    private String sublineOf;
    private String sublineReason;
    
   

    public PdxClinicalTumor(String ptID,String tumorID){
        this.patientID = ptID;
        this.submitterTumorID = tumorID;
    }
    
    public String getSubmitterTumorID() {
        return submitterTumorID;
    }

    


    /**
     * @return the patientID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * @param patientID the patientID to set
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * @param submitterTumorID the submitterTumorID to set
     */
    public void setSubmitterTumorID(String submitterTumorID) {
        this.submitterTumorID = submitterTumorID;
    }

    /**
     * @param tissueOfOrigin the tissueOfOrigin to set
     */
    public void setTissueOfOrigin(String tissueOfOrigin) {
        this.tissueOfOrigin = tissueOfOrigin;
    }

    /**
     * @return the tissueOfOriginTerm
     */
    public TermId getTissueOfOriginTerm() {
        return tissueOfOriginTerm;
    }

    /**
     * @param tissueOfOriginTerm the tissueOfOriginTerm to set
     */
    public void setTissueOfOriginTerm(TermId tissueOfOriginTerm) {
        this.tissueOfOriginTerm = tissueOfOriginTerm;
    }

    /**
     * @return the categoryTerm
     */
    public TermId getCategoryTerm() {
        return categoryTerm;
    }

    /**
     * @param categoryTerm the categoryTerm to set
     */
    public void setCategoryTerm(TermId categoryTerm) {
        this.categoryTerm = categoryTerm;
    }

    /**
     * @return the specimenTissue
     */
    public String getSpecimenTissue() {
        return specimenTissue;
    }

    /**
     * @param specimenTissue the specimenTissue to set
     */
    public void setSpecimenTissue(String specimenTissue) {
        this.specimenTissue = specimenTissue;
    }

    /**
     * @return the specimenTissueTerm
     */
    public TermId getSpecimenTissueTerm() {
        return specimenTissueTerm;
    }

    /**
     * @param specimenTissueTerm the specimenTissueTerm to set
     */
    public void setSpecimenTissueTerm(TermId specimenTissueTerm) {
        this.specimenTissueTerm = specimenTissueTerm;
    }

    /**
     * @param tissueHistology the tissueHistology to set
     */
    public void setTissueHistology(String tissueHistology) {
        this.tissueHistology = tissueHistology;
    }

    /**
     * @return the tissueHistologyTerm
     */
    public TermId getTissueHistologyTerm() {
        return tissueHistologyTerm;
    }

    /**
     * @param tissueHistologyTerm the tissueHistologyTerm to set
     */
    public void setTissueHistologyTerm(TermId tissueHistologyTerm) {
        this.tissueHistologyTerm = tissueHistologyTerm;
    }

    /**
     * @return the clinicalMarkers
     */
    public String getClinicalMarkers() {
        return clinicalMarkers;
    }

    /**
     * @param clinicalMarkers the clinicalMarkers to set
     */
    public void setClinicalMarkers(String clinicalMarkers) {
        this.clinicalMarkers = clinicalMarkers;
    }

    /**
     * @param tumorGrade the tumorGrade to set
     */
    public void setTumorGrade(String tumorGrade) {
        this.tumorGrade = tumorGrade;
    }

    /**
     * @return the tStage
     */
    public String getTStage() {
        return tStage;
    }

    /**
     * @param tStage the tStage to set
     */
    public void setTStage(String tStage) {
        this.tStage = tStage;
    }

    /**
     * @return the nStage
     */
    public String getNStage() {
        return nStage;
    }

    /**
     * @param nStage the nStage to set
     */
    public void setNStage(String nStage) {
        this.nStage = nStage;
    }

    /**
     * @return the mStage
     */
    public String getMStage() {
        return mStage;
    }

    /**
     * @param mStage the mStage to set
     */
    public void setMStage(String mStage) {
        this.mStage = mStage;
    }

    /**
     * @return the overallStage
     */
    public String getOverallStage() {
        return overallStage;
    }

    /**
     * @param overallStage the overallStage to set
     */
    public void setOverallStage(String overallStage) {
        this.overallStage = overallStage;
    }

    /**
     * @return the treatmentNaive
     */
    public boolean isTreatmentNaive() {
        return treatmentNaive;
    }

    /**
     * @param treatmentNaive the treatmentNaive to set
     */
    public void setTreatmentNaive(boolean treatmentNaive) {
        this.treatmentNaive = treatmentNaive;
    }

    /**
     * @return the sampleType
     */
    public String getSampleType() {
        return sampleType;
    }

    /**
     * @param sampleType the sampleType to set
     */
    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    /**
     * @return the sublineOf
     */
    public String getSublineOf() {
        return sublineOf;
    }

    /**
     * @param sublineOf the sublineOf to set
     */
    public void setSublineOf(String sublineOf) {
        this.sublineOf = sublineOf;
    }

    /**
     * @return the sublineReason
     */
    public String getSublineReason() {
        return sublineReason;
    }

    /**
     * @param sublineReason the sublineReason to set
     */
    public void setSublineReason(String sublineReason) {
        this.sublineReason = sublineReason;
    }

    /**
     * @return the tissueOfOrigin
     */
    public String getTissueOfOrigin() {
        return tissueOfOrigin;
    }

    /**
     * @return the tissueHistology
     */
    public String getTissueHistology() {
        return tissueHistology;
    }

    /**
     * @return the tumorGrade
     */
    public String getTumorGrade() {
        return tumorGrade;
    }

    

}
