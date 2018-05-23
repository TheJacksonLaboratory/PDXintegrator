package org.jax.pdxintegrator.model.modelstudy;

import com.github.phenomics.ontolib.ontology.data.TermId;
import java.util.ArrayList;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment;

public class PdxModelStudy {

    private String modelID;
    private String studyID;
    private boolean metastasis;
    private int metastasisPassage;
    private TermId metastasisLocation; // could be multiple locations
    private int doublingLagTime;  // days?
    private ResponseToTreatment response;
    private ArrayList<PdxStudyTreatment> treatments;

    private PdxModelStudy(String modelID, String studyID, ArrayList<PdxStudyTreatment> treatments,
            boolean metastasis, int metastasisPassage, TermId metastasisLocation,
            int doublingLagTime, ResponseToTreatment response ) {
        this.modelID = modelID;
        this.studyID = studyID;
        this.treatments = treatments;
        this.metastasis = metastasis;
        this.metastasisPassage = metastasisPassage;
        this.metastasisLocation = metastasisLocation;
        this.doublingLagTime = doublingLagTime;
        this.response = response;
       

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
    public TermId getMetastasisLocation() {
        return metastasisLocation;
    }

    /**
     * @return the doublingLagTime
     */
    public int getDoublingLagTime() {
        return doublingLagTime;
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
     * @return the response
     */
    public ResponseToTreatment getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(ResponseToTreatment response) {
        this.response = response;
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
        private TermId metastasisLocation; // could be multiple locations
        private int doublingLagTime;  // days?
        private ResponseToTreatment response;
      
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

        public Builder metastasisLocation(TermId metastasesLocation) {
            this.metastasisLocation = metastasesLocation;
            return this;
        }

        public Builder doublingLagTime(int doublingLagTime) {
            this.doublingLagTime = doublingLagTime;
            return this;
        }
        
        public Builder response(ResponseToTreatment response){
            this.response = response;
            return this;
        }

     

        public PdxModelStudy build() {
            return new PdxModelStudy(modelID,studyID, treatments,
                    metastasis, metastasisPassage, metastasisLocation,
                    doublingLagTime, response);
        }

    }
}
