package org.jax.pdxintegrator.model.modelcreation;
/**
 * This class represents the Model Creation module of the PDX-MI.
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxModelCreation {

    public String getSubmitterPdxId() {
        return submitterPdxId;
    }

    public String getMouseStrain() {
        return mouseStrain;
    }

    public String getMouseSource() {
        return mouseSource;
    }

    public boolean isStrainImmuneSystemHumanized() {
        return strainImmuneSystemHumanized;
    }

    public String getHumanizationType() {
        return humanizationType;
    }

    public TumorPrepMethod getTumorPreparation() {
        return tumorPreparation;
    }

    public String getInjectionType() {
        return injectionType;
    }

    public MouseTreatmentForEngraftment getMouseTreatmentForEngraftment() {
        return mouseTreatmentForEngraftment;
    }

    public double getEngraftmentRate() {
        return engraftmentRate;
    }

    public int getEngraftmentTimeInDays() {
        return engraftmentTimeInDays;
    }

    private final String submitterPdxId;
    private final String mouseStrain;
    private final String mouseSource;
    private final boolean strainImmuneSystemHumanized;
    private String humanizationType;
    private TumorPrepMethod tumorPreparation;
    private String injectionType;
    private MouseTreatmentForEngraftment mouseTreatmentForEngraftment;
    private double engraftmentRate;
    private int engraftmentTimeInDays;



    private PdxModelCreation(String id,
            String strain,
            String source,
            boolean humanized,
            String humanizationType,
           TumorPrepMethod tumorPrep,
             String injectionType,
                             MouseTreatmentForEngraftment mouseTreatmentEngraftment,
             double engraftmentRate,
             int engraftmentTimeInDays) {
        submitterPdxId =id;
        mouseStrain=strain;
        mouseSource=source;
        strainImmuneSystemHumanized=humanized;
        this.humanizationType=humanizationType;
        this.tumorPreparation =tumorPrep;
        this.injectionType=injectionType;
        this.mouseTreatmentForEngraftment=mouseTreatmentEngraftment;
        this.engraftmentRate=engraftmentRate;
        this.engraftmentTimeInDays=engraftmentTimeInDays;
    }


    public static class Builder {
        // initialize with n/a for unavailable. Builder will fill out whatever is available.
        private final String SubmitterPdxId;
        private String mouseStrain="n/a";
        private String mouseSource="n/a";
        private boolean strainImmuneSystemHumanized=false;
        private String humanizationType="n/a";
        private TumorPrepMethod TumorPreparation=null;
        private String injectionType="n/a";
        private MouseTreatmentForEngraftment mouseTreatmentForEngraftment=null;
        private double engraftmentRate=0D;
        private int engraftmentTimeInDays=0;

        public Builder(String id) {
            SubmitterPdxId=id;
        }

        public Builder mouseStrain(String strain) {
            this.mouseStrain=strain;
            return this;
        }

        public Builder mouseSource(String source) {
            this.mouseSource=source;
            return this;
        }

        public Builder immuneSystemHumanized(boolean humanized) {
            this.strainImmuneSystemHumanized=humanized;
            return this;
        }

        public Builder tumorPreparationMethod(TumorPrepMethod prep) {
            this.TumorPreparation=prep;
            return this;
        }

        public Builder humanizationType(String type) {
            this.humanizationType=type;
            return this;
        }

        public Builder injectionType(String type) {
            this.injectionType=type;
            return this;
        }

        public Builder mouseTreatmentForEngraftment(MouseTreatmentForEngraftment type) {
            this.mouseTreatmentForEngraftment=type;
            return this;
        }


        public Builder engraftmentRate(double rate) {
            this.engraftmentRate=rate;
            return this;
        }
        public Builder engraftmentTimeInDays(int days) {
            this.engraftmentTimeInDays=days;
            return this;
        }

        public PdxModelCreation build() {
            return new PdxModelCreation(SubmitterPdxId,
                    mouseStrain,
                    mouseSource,
                    strainImmuneSystemHumanized,
                    humanizationType,
                    TumorPreparation,
                    injectionType,
                    mouseTreatmentForEngraftment,
                    engraftmentRate,
                    engraftmentTimeInDays);
        }

    }

}
