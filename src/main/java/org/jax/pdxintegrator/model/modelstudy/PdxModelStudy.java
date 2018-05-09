package org.jax.pdxintegrator.model.modelstudy;

import com.github.phenomics.ontolib.ontology.data.TermId;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToStandardOfCare;

public class PdxModelStudy {

    private String treatment;  // a string is insufficent
    private ResponseToStandardOfCare treatmentResponse;// this isn't to standard of care, rather the (experimental) study treatment 
    private boolean metastasis;
    private int metastasisPassage;
    private TermId metastasisLocation; // could be multiple locations
    private int doublingLagTime;  // days?
    private Object tumorOmics; // we don't know what this will be

    private PdxModelStudy(String treatment, ResponseToStandardOfCare treatmentResponse,
            boolean metastasis, int metastasisPassage, TermId metastasisLocation,
            int doublingLagTime, Object tumorOmics) {
        this.treatment = treatment;
        this.treatmentResponse = treatmentResponse;
        this.metastasis = metastasis;
        this.metastasisPassage = metastasisPassage;
        this.metastasisLocation = metastasisLocation;
        this.doublingLagTime = doublingLagTime;
        this.tumorOmics = tumorOmics;

    }

    /**
     * @return the treatment
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * @return the treatmentResponse
     */
    public ResponseToStandardOfCare getTreatmentResponse() {
        return treatmentResponse;
    }

    /**
     * @return the metastasis
     */
    public boolean isMetastasis() {
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
     * @return the tumorOmics
     */
    public Object getTumorOmics() {
        return tumorOmics;
    }

    public static class Builder {

        private String treatment;  // a string is insufficent
        private ResponseToStandardOfCare treatmentResponse;
        private boolean metastasis;
        private int metastasisPassage;
        private TermId metastasisLocation; // could be multiple locations
        private int doublingLagTime;  // days?
        private Object tumorOmics; // we don't know what this will be

        public Builder treatment(String treatment) {
            this.treatment = treatment;
            return this;
        }

        public Builder treatmentResponse(ResponseToStandardOfCare treatmentResponse) {
            this.treatmentResponse = treatmentResponse;
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

        public Builder tumorOmics(Object tumorOmics) {
            this.tumorOmics = tumorOmics;
            return this;
        }

        public PdxModelStudy build() {
            return new PdxModelStudy(treatment, treatmentResponse,
                    metastasis, metastasisPassage, metastasisLocation,
                    doublingLagTime, tumorOmics);
        }

    }
}
