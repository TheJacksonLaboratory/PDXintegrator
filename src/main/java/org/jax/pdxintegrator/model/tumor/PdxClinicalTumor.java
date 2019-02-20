package org.jax.pdxintegrator.model.tumor;


import java.util.ArrayList;
import org.jax.pdxintegrator.uberon.UberonTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;

/**
 * Main class for modeling the Clinical/Tumor part of the PDX-MI
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxClinicalTumor {

    //Fields: Patient ID, Tumor ID, Event Index, Treatment Naïve, Age at collection, Initial Diagnosis, Primary Tissue, Disease Progression, 
    //Specimen Tissue, Tissue Histology, Clinical Diagnostic Markers, Tumor Grade, T Stage, N Stage , M Stage, Overall Stage, Sample Type

    private String patientID;
    private String submitterTumorID;
    private String eventIndex;
    private String collectionProcedure;
    private Boolean treatmentNaive;
    private String ageAtCollection;
    private String initialDiagnosis;
    private TermId initialDiagnosisTerm;
    private String tissueOfOrigin; // primary tissue
    private TermId tissueOfOriginTerm;
    private String clinicalEventPoint;
    /** NCIT Term id for primary, recurrence, metastasis. */
    private TermId clinicalEventPointTerm; // disease progression
    private String specimenTissue;
    private TermId specimenTissueTerm;
    private String tissueHistology;
    /** NCIT Term id for tissue histology */
    private TermId tissueHistologyTerm;
    private String clinicalMarkers;
    private TumorGrade tumorGrade;
    private String tStage;
    private String nStage;
    private String mStage;
    private String overallStage;
    private String sampleType;
    private String metastaticSites;
    private ArrayList<TermId> metastaticSiteTerms = new ArrayList<>();
    private String strAnalysis;
    private String strMarkers;
    private String strEvaluation;
    
   

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
    public void setTumorGrade(TumorGrade tumorGrade) {
        this.tumorGrade = tumorGrade;
    }

    /**
     * @return the tStage
     */
    public String getTStage() {
        return gettStage();
    }

    /**
     * @param tStage the tStage to set
     */
    public void setTStage(String tStage) {
        this.settStage(tStage);
    }

    /**
     * @return the nStage
     */
    public String getNStage() {
        return getnStage();
    }

    /**
     * @param nStage the nStage to set
     */
    public void setNStage(String nStage) {
        this.setnStage(nStage);
    }

    /**
     * @return the mStage
     */
    public String getMStage() {
        return getmStage();
    }

    /**
     * @param mStage the mStage to set
     */
    public void setMStage(String mStage) {
        this.setmStage(mStage);
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
    public Boolean isTreatmentNaive() {
        return treatmentNaive;
    }

    /**
     * @param treatmentNaive the treatmentNaive to set
     */
    public void setTreatmentNaive(Boolean treatmentNaive) {
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
    public TumorGrade getTumorGrade() {
        return tumorGrade;
    }

    /**
     * @return the eventIndex
     */
    public String getEventIndex() {
        return eventIndex;
    }

    /**
     * @param eventIndex the eventIndex to set
     */
    public void setEventIndex(String eventIndex) {
        this.eventIndex = eventIndex;
    }

    /**
     * @return the ageAtCollection
     */
    public String getAgeAtCollection() {
        return ageAtCollection;
    }

    /**
     * @param ageAtCollection the ageAtCollection to set
     */
    public void setAgeAtCollection(String ageAtCollection) {
        this.ageAtCollection = ageAtCollection;
    }

    /**
     * @return the initialDiagnosis
     */
    public String getInitialDiagnosis() {
        return initialDiagnosis;
    }

    /**
     * @param initialDiagnosis the initialDiagnosis to set
     */
    public void setInitialDiagnosis(String initialDiagnosis) {
        this.initialDiagnosis = initialDiagnosis;
    }

    /**
     * @return the initialDiagnosisTerm
     */
    public TermId getInitialDiagnosisTerm() {
        return initialDiagnosisTerm;
    }

    /**
     * @param initialDiagnosisTerm the initialDiagnosisTerm to set
     */
    public void setInitialDiagnosisTerm(TermId initialDiagnosisTerm) {
        this.initialDiagnosisTerm = initialDiagnosisTerm;
    }

   

    /**
     * @return the tStage
     */
    public String gettStage() {
        return tStage;
    }

    /**
     * @param tStage the tStage to set
     */
    public void settStage(String tStage) {
        this.tStage = tStage;
    }

    /**
     * @return the nStage
     */
    public String getnStage() {
        return nStage;
    }

    /**
     * @param nStage the nStage to set
     */
    public void setnStage(String nStage) {
        this.nStage = nStage;
    }

    /**
     * @return the mStage
     */
    public String getmStage() {
        return mStage;
    }

    /**
     * @param mStage the mStage to set
     */
    public void setmStage(String mStage) {
        this.mStage = mStage;
    }

    /**
     * @return the collectionProcedure
     */
    public String getCollectionProcedure() {
        return collectionProcedure;
    }

    /**
     * @param collectionProcedure the collectionProcedure to set
     */
    public void setCollectionProcedure(String collectionProcedure) {
        this.collectionProcedure = collectionProcedure;
    }

    /**
     * @return the clinicalEventPoint
     */
    public String getClinicalEventPoint() {
        return clinicalEventPoint;
    }

    /**
     * @param clinicalEventPoint the clinicalEventPoint to set
     */
    public void setClinicalEventPoint(String clinicalEventPoint) {
        this.clinicalEventPoint = clinicalEventPoint;
    }

    /**
     * @return the metastaticSites
     */
    public String getMetastaticSites() {
        return metastaticSites;
    }

    /**
     * @param metastaticSites the metastaticSites to set
     */
    public void setMetastaticSites(String metastaticSites) {
        this.metastaticSites = metastaticSites;
    }

    /**
     * @return the strAnalysis
     */
    public String getStrAnalysis() {
        return strAnalysis;
    }

    /**
     * @param strAnalysis the strAnalysis to set
     */
    public void setStrAnalysis(String strAnalysis) {
        this.strAnalysis = strAnalysis;
    }

    /**
     * @return the strMarkers
     */
    public String getStrMarkers() {
        return strMarkers;
    }

    /**
     * @param strMarkers the strMarkers to set
     */
    public void setStrMarkers(String strMarkers) {
        this.strMarkers = strMarkers;
    }

    /**
     * @return the strEvaluation
     */
    public String getStrEvaluation() {
        return strEvaluation;
    }

    /**
     * @param strEvaluation the strEvaluation to set
     */
    public void setStrEvaluation(String strEvaluation) {
        this.strEvaluation = strEvaluation;
    }

    /**
     * @return the clinicalEventPointTerm
     */
    public TermId getClinicalEventPointTerm() {
        return clinicalEventPointTerm;
    }

    /**
     * @param clinicalEventPointTerm the clinicalEventPointTerm to set
     */
    public void setClinicalEventPointTerm(TermId clinicalEventPointTerm) {
        this.clinicalEventPointTerm = clinicalEventPointTerm;
    }

    /**
     * @return the metastaticSiteTerms
     */
    public ArrayList<TermId> getMetastaticSiteTerms() {
        return metastaticSiteTerms;
    }

    /**
     * @param metastaticSiteTerms the metastaticSiteTerms to set
     */
    public void setMetastaticSiteTerms(ArrayList<TermId> metastaticSiteTerms) {
        this.metastaticSiteTerms = metastaticSiteTerms;
    }

    public void addMetastaticSiteTerm(TermId termId){
        this.metastaticSiteTerms.add(termId);
    }

}
