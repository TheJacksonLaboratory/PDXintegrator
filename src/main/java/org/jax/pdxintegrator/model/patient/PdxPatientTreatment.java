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
    private int index;
    private boolean postSample;
    private String regimen;
    private ResponseToTreatment response;
    private String reasonStopped;

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the postSample
     */
    public boolean isPostSample() {
        return postSample;
    }

    /**
     * @param postSample the postSample to set
     */
    public void setPostSample(boolean postSample) {
        this.postSample = postSample;
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
          
    
    
}
