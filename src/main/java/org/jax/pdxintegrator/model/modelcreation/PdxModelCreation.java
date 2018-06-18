package org.jax.pdxintegrator.model.modelcreation;

/**
 * This class represents the Model Creation module of the PDX-MI.
 *
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxModelCreation {

    private String tumorID;
    private String modelID;
    private int passage;
    private String mouseStrain;
    private String mouseSource;
    private String mouseSex;
    private boolean humanized;
    private String humanizationType;
    private String engraftmentProcedure;
    private String engraftmnetSite;
    private String treatmentForEngraftment;
    private boolean tissueCryoPreserved;
    private String engraftmentRate;
    private String engraftmentTime;
    
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
     * @return the passage
     */
    public int getPassage() {
        return passage;
    }

    /**
     * @param passage the passage to set
     */
    public void setPassage(int passage) {
        this.passage = passage;
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

    /**
     * @return the tissueCryoPreserved
     */
    public boolean isTissueCryoPreserved() {
        return tissueCryoPreserved;
    }

    /**
     * @param tissueCryoPreserved the tissueCryoPreserved to set
     */
    public void setTissueCryoPreserved(boolean tissueCryoPreserved) {
        this.tissueCryoPreserved = tissueCryoPreserved;
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

}
