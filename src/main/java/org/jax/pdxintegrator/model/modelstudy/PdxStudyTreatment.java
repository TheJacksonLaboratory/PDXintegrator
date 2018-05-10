/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jax.pdxintegrator.model.modelstudy;

/**
 *
 * @author sbn
 */
public class PdxStudyTreatment {

    private String studyID;
    private String drug;
    private String dose;
    private String units;
    private String route;
    private String frequency;

    public PdxStudyTreatment(String studyID, String drug, String dose, String units, String route, String frequency) {
        this.studyID = studyID;
        this.drug = drug;
        this.dose = dose;
        this.units = units;
        this.route = route;
        this.frequency = frequency;

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
     * @return the drug
     */
    public String getDrug() {
        return drug;
    }

    /**
     * @param drug the drug to set
     */
    public void setDrug(String drug) {
        this.drug = drug;
    }

    /**
     * @return the dose
     */
    public String getDose() {
        return dose;
    }

    /**
     * @param dose the dose to set
     */
    public void setDose(String dose) {
        this.dose = dose;
    }

    /**
     * @return the units
     */
    public String getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * @return the route
     */
    public String getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(String route) {
        this.route = route;
    }

    /**
     * @return the frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * @param freqeuncy the frequency to set
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public static class Builder {

        private String studyID;
        private String drug;
        private String dose;
        private String units;
        private String route;
        private String frequency;

        public Builder(String studyID) {
            this.studyID = studyID;
           
        }

        public Builder setDrug(String drug) {
            this.drug = drug;
            return this;
        }

        public Builder setDose(String dose) {
            this.dose = dose;
            return this;
        }

        public Builder setUnits(String units) {
            this.units = units;
            return this;
        }

        public Builder setRoute(String route) {
            this.route = route;
            return this;
        }

        public Builder setFrequency(String frequency) {
            this.frequency = frequency;
            return this;
        }

        public PdxStudyTreatment build() {
            return new PdxStudyTreatment(studyID, drug, dose, units, route, frequency);
        }
    }
}
