package org.jax.pdxintegrator.model.qualityassurance;
/**
 * This class represents the Model Creation module of the PDX-MI.
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxQualityAssurance {

    private String modelID;
    private int passage;
    private String qcMethod;
    private String qcResult;
    private String animalHealthStatus;
    
    
    public PdxQualityAssurance(String modelID){
        this.modelID = modelID;
    }

    /**
     * @return the modelID
     */
    public String getModelID() {
        return modelID;
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
     * @return the qcMethod
     */
    public String getQcMethod() {
        return qcMethod;
    }

    /**
     * @param qcMethod the qcMethod to set
     */
    public void setQcMethod(String qcMethod) {
        this.qcMethod = qcMethod;
    }

    /**
     * @return the qcResult
     */
    public String getQcResult() {
        return qcResult;
    }

    /**
     * @param qcResult the qcResult to set
     */
    public void setQcResult(String qcResult) {
        this.qcResult = qcResult;
    }

    /**
     * @return the animalHealthStatus
     */
    public String getAnimalHealthStatus() {
        return animalHealthStatus;
    }

    /**
     * @param animalHealthStatus the animalHealthStatus to set
     */
    public void setAnimalHealthStatus(String animalHealthStatus) {
        this.animalHealthStatus = animalHealthStatus;
    }

   
}
