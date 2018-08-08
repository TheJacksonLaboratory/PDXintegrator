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
    // the following were removed by Mike L. et al
    private boolean metastasis;
    private int metastasisPassage;
    private String metastasisLocation; // could be multiple locations
    
   
    // these were added by Mike L. et al
    // i don't know what they are
    private String endpoint1;
    private String endpoint2;
    private String endpoint3;
    
    private ArrayList<PdxStudyTreatment> treatments = new ArrayList<>();
    
    public PdxModelStudy(String modelId, String studyId){
        this.modelID = modelId;
        this.studyID = studyId;
        
    }

    private PdxModelStudy(String modelID, String studyID, ArrayList<PdxStudyTreatment> treatments,
            boolean metastasis, int metastasisPassage, String metastasisLocation) {
        this.modelID = modelID;
        this.studyID = studyID;
        this.treatments = treatments;
        this.metastasis = metastasis;
        this.metastasisPassage = metastasisPassage;
        this.metastasisLocation = metastasisLocation;
       
        
       

    }

    /**
     * @return the metastasis
     */
    public boolean hasMetastasis() {
        return metastasis;
    }

    /**
     * @return the metastasisPassage
     */
    public int getMetastasisPassage() {
        return metastasisPassage;
    }

    /**
     * @return the metastasisLocation
     */
    public String getMetastasisLocation() {
        return metastasisLocation;
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
     * @return the endpoint1
     */
    public String getEndpoint1() {
        return endpoint1;
    }

    /**
     * @param endpoint1 the endpoint1 to set
     */
    public void setEndpoint1(String endpoint1) {
        this.endpoint1 = endpoint1;
    }

    /**
     * @return the endpoint2
     */
    public String getEndpoint2() {
        return endpoint2;
    }

    /**
     * @param endpoint2 the endpoint2 to set
     */
    public void setEndpoint2(String endpoint2) {
        this.endpoint2 = endpoint2;
    }

    /**
     * @return the endpoint3
     */
    public String getEndpoint3() {
        return endpoint3;
    }

    /**
     * @param endpoint3 the endpoint3 to set
     */
    public void setEndpoint3(String endpoint3) {
        this.endpoint3 = endpoint3;
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
        private boolean metastasis;
        private int metastasisPassage;
        private String metastasisLocation; // could be multiple locations
      
       
      
         public Builder(String modelID, String studyID){
           this.modelID = modelID;
           this.studyID = studyID;
           
       }
        
       public Builder treatments(ArrayList<PdxStudyTreatment> treatments){
           this.treatments = treatments;
           return this;
       }

        public Builder metastasis(boolean metastasis) {
            this.metastasis = metastasis;
            return this;
        }

        public Builder metastasisLocation(String metastasesLocation) {
            this.metastasisLocation = metastasesLocation;
            return this;
        }

      
       
     

        public PdxModelStudy build() {
            return new PdxModelStudy(modelID,studyID, treatments,
                    metastasis, metastasisPassage, metastasisLocation);
        }

    }
}
