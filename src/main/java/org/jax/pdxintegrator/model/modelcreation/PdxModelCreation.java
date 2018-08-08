package org.jax.pdxintegrator.model.modelcreation;

/**
 * This class represents the Model Creation module of the PDX-MI.
 *
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxModelCreation {
   
    private String tumorID;
    // pdmr or institution creating model?
    private String modelID;
    private String mouseStrain;
    private String mouseSource;
    private String mouseSex;
    private Boolean humanized;
    private String humanizationType;
    private String engraftmentMaterial;
    private String treatmentForEngraftment;
    private String engraftmentProcedure;
    private String engraftmnetSite;
    private Boolean cryopreservedBeforeEngraftment;
    private String modelHistology;
    private Integer doublingTime;
    private Boolean viablyCryopresered;
    private Boolean metastasis;
    private String metastastaticSites;
    private Boolean macroMetastasisRequiresExcision;
    private String sublineOfModel;
    private String sublineReason;
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
    public Boolean isHumanized() {
        return humanized;
    }

    /**
     * @param humanized the humanized to set
     */
    public void setHumanized(Boolean humanized) {
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

    
    /**
     * @return the engraftmentMaterial
     */
    public String getEngraftmentMaterial() {
        return engraftmentMaterial;
    }

    /**
     * @param engraftmentMaterial the engraftmentMaterial to set
     */
    public void setEngraftmentMaterial(String engraftmentMaterial) {
        this.engraftmentMaterial = engraftmentMaterial;
    }

    /**
     * @return the cryopreservedBeforeEngraftment
     */
    public Boolean getCryopreservedBeforeEngraftment() {
        return cryopreservedBeforeEngraftment;
    }

    /**
     * @param cryopreservedBeforeEngraftment the cryopreservedBeforeEngraftment to set
     */
    public void setCryopreservedBeforeEngraftment(Boolean cryopreservedBeforeEngraftment) {
        this.cryopreservedBeforeEngraftment = cryopreservedBeforeEngraftment;
    }

    /**
     * @return the doublingTime
     */
    public Integer getDoublingTime() {
        return doublingTime;
    }

    /**
     * @param doublingTime the doublingTime to set
     */
    public void setDoublingTime(Integer doublingTime) {
        this.doublingTime = doublingTime;
    }

    /**
     * @return the viablyCryopresered
     */
    public Boolean getViablyCryopresered() {
        return viablyCryopresered;
    }

    /**
     * @param viablyCryopresered the viablyCryopresered to set
     */
    public void setViablyCryopresered(Boolean viablyCryopresered) {
        this.viablyCryopresered = viablyCryopresered;
    }

    /**
     * @return the metastasis
     */
    public Boolean getMetastasis() {
        return metastasis;
    }

    /**
     * @param metastasis the metastasis to set
     */
    public void setMetastasis(Boolean metastasis) {
        this.metastasis = metastasis;
    }

    /**
     * @return the metastastaticSites
     */
    public String getMetastastaticSites() {
        return metastastaticSites;
    }

    /**
     * @param metastastaticSites the metastastaticSites to set
     */
    public void setMetastastaticSites(String metastastaticSites) {
        this.metastastaticSites = metastastaticSites;
    }

    /**
     * @return the macroMetastasisRequiresExcision
     */
    public Boolean getMacroMetastasisRequiresExcision() {
        return macroMetastasisRequiresExcision;
    }

    /**
     * @param macroMetastasisRequiresExcision the macroMetastasisRequiresExcision to set
     */
    public void setMacroMetastasisRequiresExcision(Boolean macroMetastasisRequiresExcision) {
        this.macroMetastasisRequiresExcision = macroMetastasisRequiresExcision;
    }

   

}
