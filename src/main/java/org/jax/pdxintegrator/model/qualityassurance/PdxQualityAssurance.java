package org.jax.pdxintegrator.model.qualityassurance;
/**
 * This class represents the Model Creation module of the PDX-MI.
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxQualityAssurance {

    private final ModelCharacterization tumorCharacterizationTechnology;


    private final boolean tumorNotMouseNotEbv;
    private final ResponseToStandardOfCare response;
    private final boolean animalHealthStatusSufficient;
    private final boolean passageQaPerformed;

    private PdxQualityAssurance(ModelCharacterization characterization, boolean notMouseNotEbv, boolean passageQa, ResponseToStandardOfCare resp, boolean animalHeatlStatus){
        this.tumorCharacterizationTechnology=characterization;
        this.tumorNotMouseNotEbv=notMouseNotEbv;
        this.response=resp;
        this.animalHealthStatusSufficient=animalHeatlStatus;
        this.passageQaPerformed=passageQa;
    }

    public ModelCharacterization getTumorCharacterizationTechnology() {
        return tumorCharacterizationTechnology;
    }

    public boolean isTumorNotMouseNotEbv() {
        return tumorNotMouseNotEbv;
    }

    public ResponseToStandardOfCare getResponse() {
        return response;
    }

    public boolean isAnimalHealthStatusSufficient() {
        return animalHealthStatusSufficient;
    }

    public boolean isPassageQaPerformed() {
        return passageQaPerformed;
    }


    public static class Builder {
        private ModelCharacterization tumorCharacterizationTechnology;
        private boolean tumorNotMouseNotEbv;
        private ResponseToStandardOfCare response;
        private boolean animalHealthStatusSufficient;
        private boolean passageQaPerformed;

        public Builder(ModelCharacterization characterization, boolean notMouseNotEbv, boolean passageQa) {
            tumorCharacterizationTechnology=characterization;
            tumorNotMouseNotEbv=notMouseNotEbv;
            passageQaPerformed=passageQa;
        }

        public Builder response(ResponseToStandardOfCare rep) {
            this.response=rep;
            return this;
        }

        public Builder animalHealthStatusOk(boolean ok) {
            this.animalHealthStatusSufficient=ok;
            return this;
        }


        public PdxQualityAssurance build() {
            return new PdxQualityAssurance(tumorCharacterizationTechnology,
                    tumorNotMouseNotEbv,
                    passageQaPerformed,
                    response, animalHealthStatusSufficient);
        }


    }
}
