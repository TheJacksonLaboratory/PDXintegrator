/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jax.pdxintegrator.model.patient;

import org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment;

/**
 *
 * @author sbn
 */
public class PdxPatientTreatment {
    
    //Fields: Patient ID, Event Index, clincal treatment setting, Regimen (Drug), Response, Reason Stopped, Treatment Notes
    //done
    
    private String patientID;
    private String eventIndex;
    private String clinicalTreatmentSetting;
    private String regimen;
    private ResponseToTreatment response;
    private String reasonStopped;
    private String treatmentNotes;

    /**
     * @return the index
     */
    public String getEventIndex() {
        return eventIndex;
    }

    /**
     * @param index the index to set
     */
    public void setEventIndex(String index) {
        this.eventIndex = index;
    }

   

    /**
     * @return the regimen
     */
    public String getRegimen() {
        return regimen;
    }

    /**
     * @param regimen the regimen to set
     */
    public void setRegimen(String regimen) {
        this.regimen = regimen;
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
     * @return the reasonStopped
     */
    public String getReasonStopped() {
        return reasonStopped;
    }

    /**
     * @param reasonStopped the reasonStopped to set
     */
    public void setReasonStopped(String reasonStopped) {
        this.reasonStopped = reasonStopped;
    }

    /**
     * @return the treatmentNotes
     */
    public String getTreatmentNotes() {
        return treatmentNotes;
    }

    /**
     * @param treatmentNotes the treatmentNotes to set
     */
    public void setTreatmentNotes(String treatmentNotes) {
        this.treatmentNotes = treatmentNotes;
    }

    /**
     * @return the patientID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * @param patientID the patientID to set
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * @return the clinicalTreatmentSetting
     */
    public String getClinicalTreatmentSetting() {
        return clinicalTreatmentSetting;
    }

    /**
     * @param clinicalTreatmentSetting the clinicalTreatmentSetting to set
     */
    public void setClinicalTreatmentSetting(String clinicalTreatmentSetting) {
        this.clinicalTreatmentSetting = clinicalTreatmentSetting;
    }
          
    
    
}
