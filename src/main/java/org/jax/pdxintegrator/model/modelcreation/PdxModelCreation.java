package org.jax.pdxintegrator.model.modelcreation;

/**
 * This class represents the Model Creation module of the PDX-MI.
 *
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxModelCreation {
    
    //Fields: Tumor ID, Model ID, Host Strain, Mouse Source, Mouse Sex, Is Mouse Humanized, Humanization Type, Engraftment Procedure, Engraftment Site, Treatment for Engraftment, 
    //PDX Model Histology, Was passaged tissue cryopreserved, Passaged tissue engraftment rate, Passaged tissue engraftment time, Subline of Model, Subline Reason

    private String tumorID;
    // pdmr or institution creating model?
    private String modelID;
    private String mouseStrain;
    private String mouseSource;
    private String mouseSex;
    private boolean humanized;
    private String humanizationType;
    private String engraftmentProcedure;
    private String engraftmnetSite;
    private String treatmentForEngraftment;
    private String modelHistology;
    private Boolean passagedTissueCryoPreserved;
    private String engraftmentRate;
    private String engraftmentTime;
    private String sublineOfModel;
    private String sublineReason;
    
    public PdxModelCreation(String tumorID, String modelID){
        this.tumorID = tumorID;
        this.modelID = modelID;
    }

    /**
     * @return the tumorID
     */
    public String getTumorID() {
        return tumorID;
    }

    /**
     * @param tumorID the tumorID to set
     */
    public void setTumorID(String tumorID) {
        this.tumorID = tumorID;
    }

    /**
     * @return the modelID
     */
    public String getModelID() {
        return modelID;
    }

    /**
     * @param modelID the modelID to set
     */
    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

   

    /**
     * @return the mouseStrain
     */
    public String getMouseStrain() {
        return mouseStrain;
    }

    /**
     * @param mouseStrain the mouseStrain to set
     */
    public void setMouseStrain(String mouseStrain) {
        this.mouseStrain = mouseStrain;
    }

    /**
     * @return the mouseSource
     */
    public String getMouseSource() {
        return mouseSource;
    }

    /**
     * @param mouseSource the mouseSource to set
     */
    public void setMouseSource(String mouseSource) {
        this.mouseSource = mouseSource;
    }

    /**
     * @return the mouseSex
     */
    public String getMouseSex() {
        return mouseSex;
    }

    /**
     * @param mouseSex the mouseSex to set
     */
    public void setMouseSex(String mouseSex) {
        this.mouseSex = mouseSex;
    }

    /**
     * @return the humanized
     */
    public boolean isHumanized() {
        return humanized;
    }

    /**
     * @param humanized the humanized to set
     */
    public void setHumanized(boolean humanized) {
        this.humanized = humanized;
    }

    /**
     * @return the humanizationType
     */
    public String getHumanizationType() {
        return humanizationType;
    }

    /**
     * @param humanizationType the humanizationType to set
     */
    public void setHumanizationType(String humanizationType) {
        this.humanizationType = humanizationType;
    }

    /**
     * @return the engraftmnetProcedure
     */
    public String getEngraftmentProcedure() {
        return engraftmentProcedure;
    }

    /**
     * @param engraftmentProcedure the engraftmentProcedure to set
     */
    public void setEngraftmentProcedure(String engraftmentProcedure) {
        this.engraftmentProcedure = engraftmentProcedure;
    }

    /**
     * @return the engraftmnetSite
     */
    public String getEngraftmnetSite() {
        return engraftmnetSite;
    }

    /**
     * @param engraftmnetSite the engraftmnetSite to set
     */
    public void setEngraftmnetSite(String engraftmnetSite) {
        this.engraftmnetSite = engraftmnetSite;
    }

    /**
     * @return the treatmentForEngraftment
     */
    public String getTreatmentForEngraftment() {
        return treatmentForEngraftment;
    }

    /**
     * @param treatmentForEngraftment the treatmentForEngraftment to set
     */
    public void setTreatmentForEngraftment(String treatmentForEngraftment) {
        this.treatmentForEngraftment = treatmentForEngraftment;
    }

    public Boolean getPassagedTissueCryoPreserved() {
        return passagedTissueCryoPreserved;
    }

    public void setPassagedTissueCryoPreserved(boolean tissueCryoPreserved) {
        this.passagedTissueCryoPreserved = tissueCryoPreserved;
    }

    /**
     * @return the engraftmentRate
     */
    public String getEngraftmentRate() {
        return engraftmentRate;
    }

    /**
     * @param engraftmentRate the engraftmentRate to set
     */
    public void setEngraftmentRate(String engraftmentRate) {
        this.engraftmentRate = engraftmentRate;
    }

    /**
     * @return the engraftmentTime
     */
    public String getEngraftmentTime() {
        return engraftmentTime;
    }

    /**
     * @param engraftmentTime the engraftmentTime to set
     */
    public void setEngraftmentTime(String engraftmentTime) {
        this.engraftmentTime = engraftmentTime;
    }

    /**
     * @return the modelHistology
     */
    public String getModelHistology() {
        return modelHistology;
    }

    /**
     * @param modelHistology the modelHistology to set
     */
    public void setModelHistology(String modelHistology) {
        this.modelHistology = modelHistology;
    }

    /**
     * @return the sublineOfModel
     */
    public String getSublineOfModel() {
        return sublineOfModel;
    }

    /**
     * @param sublineOfModel the sublineOfModel to set
     */
    public void setSublineOfModel(String sublineOfModel) {
        this.sublineOfModel = sublineOfModel;
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

}
