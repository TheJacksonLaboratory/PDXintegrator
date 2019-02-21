package org.jax.pdxintegrator.model.modelstudy;

import java.util.ArrayList;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment;

public class PdxModelStudy {

    private String modelID;
    private String studyID;
    private String description;
    private Integer passage;
    private String hostStrain;
    private String implantationSite;
    private String baselineTumorTargetSize;

    private ArrayList<PdxStudyTreatment> treatments = new ArrayList<>();

    public PdxModelStudy(String modelId, String studyId) {
        this.modelID = modelId;
        this.studyID = studyId;

    }

    private PdxModelStudy(String modelID, String studyID, ArrayList<PdxStudyTreatment> treatments) {
        this.modelID = modelID;
        this.studyID = studyID;
        this.treatments = treatments;

    }

    /**
     * @return the treatments
     */
    public ArrayList<PdxStudyTreatment> getTreatments() {
        return treatments;
    }

    /**
     * @param treatments the treatments to set
     */
    public void setTreatments(ArrayList<PdxStudyTreatment> treatments) {
        this.treatments = treatments;
    }

    /**
     * @return the studyID
     */
    public String getStudyID() {
        return studyID;
    }

    /**
     * @param studyID the studyID to set
     */
    public void setStudyID(String studyID) {
        this.studyID = studyID;
    }

    /**
     * @return the modelID
     */
    public String getModelID() {
        return modelID;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the passage
     */
    public Integer getPassage() {
        return passage;
    }

    /**
     * @param passage the passage to set
     */
    public void setPassage(Integer passage) {
        this.passage = passage;
    }

    /**
     * @return the hostStrain
     */
    public String getHostStrain() {
        return hostStrain;
    }

    /**
     * @param hostStrain the hostStrain to set
     */
    public void setHostStrain(String hostStrain) {
        this.hostStrain = hostStrain;
    }

    /**
     * @return the implantationSite
     */
    public String getImplantationSite() {
        return implantationSite;
    }

    /**
     * @param implantationSite the implantationSite to set
     */
    public void setImplantationSite(String implantationSite) {
        this.implantationSite = implantationSite;
    }

    /**
     * @return the baselineTumorTargetSize
     */
    public String getBaselineTumorTargetSize() {
        return baselineTumorTargetSize;
    }

    /**
     * @param baselineTumorTargetSize the baselineTumorTargetSize to set
     */
    public void setBaselineTumorTargetSize(String baselineTumorTargetSize) {
        this.baselineTumorTargetSize = baselineTumorTargetSize;
    }

    public static class Builder {

        private String modelID;
        private String studyID;
        private ArrayList<PdxStudyTreatment> treatments;

        public Builder(String modelID, String studyID) {
            this.modelID = modelID;
            this.studyID = studyID;

        }

        public Builder treatments(ArrayList<PdxStudyTreatment> treatments) {
            this.treatments = treatments;
            return this;
        }

        public PdxModelStudy build() {
            return new PdxModelStudy(modelID, studyID, treatments);
        }

    }
}
