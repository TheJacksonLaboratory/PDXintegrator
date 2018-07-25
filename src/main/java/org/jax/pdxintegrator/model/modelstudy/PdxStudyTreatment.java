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
    private String cohort;
    private Integer cohortSize;
    private String drug;
    private Integer nsc;
    private String dose;
    private String route;
    private String dosingSchedule;
    private Integer numberOfCycles;
    private Integer studyDuration;
    private String endpoint1Response;
    private String endpoint2Response;
    private String endpoint3Response;

    
    public PdxStudyTreatment(String studyID) {
        this.studyID = studyID;
        
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
     * @return the cohort
     */
    public String getCohort() {
        return cohort;
    }

    /**
     * @param cohort the cohort to set
     */
    public void setCohort(String cohort) {
        this.cohort = cohort;
    }

    /**
     * @return the cohortSize
     */
    public Integer getCohortSize() {
        return cohortSize;
    }

    /**
     * @param cohortSize the cohortSize to set
     */
    public void setCohortSize(Integer cohortSize) {
        this.cohortSize = cohortSize;
    }

    /**
     * @return the dosingSchedule
     */
    public String getDosingSchedule() {
        return dosingSchedule;
    }

    /**
     * @param dosingSchedule the dosingSchedule to set
     */
    public void setDosingSchedule(String dosingSchedule) {
        this.dosingSchedule = dosingSchedule;
    }

    /**
     * @return the numberOfCycles
     */
    public Integer getNumberOfCycles() {
        return numberOfCycles;
    }

    /**
     * @param numberOfCycles the numberOfCycles to set
     */
    public void setNumberOfCycles(Integer numberOfCycles) {
        this.numberOfCycles = numberOfCycles;
    }

    /**
     * @return the studyDuration
     */
    public Integer getStudyDuration() {
        return studyDuration;
    }

    /**
     * @param studyDuration the studyDuration to set
     */
    public void setStudyDuration(Integer studyDuration) {
        this.studyDuration = studyDuration;
    }

    /**
     * @return the endpoint1Response
     */
    public String getEndpoint1Response() {
        return endpoint1Response;
    }

    /**
     * @param endpoint1Response the endpoint1Response to set
     */
    public void setEndpoint1Response(String endpoint1Response) {
        this.endpoint1Response = endpoint1Response;
    }

    /**
     * @return the endpoint2Response
     */
    public String getEndpoint2Response() {
        return endpoint2Response;
    }

    /**
     * @param endpoint2Response the endpoint2Response to set
     */
    public void setEndpoint2Response(String endpoint2Response) {
        this.endpoint2Response = endpoint2Response;
    }

    /**
     * @return the endpoint3Response
     */
    public String getEndpoint3Response() {
        return endpoint3Response;
    }

    /**
     * @param endpoint3Response the endpoint3Response to set
     */
    public void setEndpoint3Response(String endpoint3Response) {
        this.endpoint3Response = endpoint3Response;
    }

   
}
