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
    // the following were removed by Mike L. et al
    private boolean metastasis;
    private int metastasisPassage;
    private String metastasisLocation; // could be multiple locations
   
    // these were added by Mike L. et al
    // i don't know what they are
    private String endpoint1;
    private String endpoint2;
    private String endpoint3;
    
    private ArrayList<PdxStudyTreatment> treatments;

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
