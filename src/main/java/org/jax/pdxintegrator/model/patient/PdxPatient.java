package org.jax.pdxintegrator.model.patient;

import java.util.ArrayList;

public class PdxPatient {
    
    //Fields: PDTC, Patient ID, Sex, Age at Primary Diagnosis, Race, Ethnicity, Virology Status, Consent
    // done

    private String pdtc;
    private final String submitterPatientID;
    private final Sex sex;
    private final Age ageAtDiagnosis;
    private final Consent consent;
    private final String ethnicity;
    private final String race;
    private ArrayList<PdxPatientTreatment> patientTreatments;
    private String virologyStatus;
    private String clinicalTreatmentSetting;
    private String treatmentNotes;

 
    public String getSubmitterPatientID() {
        return submitterPatientID;
    }

    public Sex getSex() {
        return sex;
    }

    public Age getAgeAtDiagnosis() {
        return ageAtDiagnosis;
    }
    
    

    public Consent getConsent() {
        return consent;
    }

    public String getRace() {
        return race;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    private PdxPatient(String pdtc, String id, Sex s, Age ageAtDiagnosis,  Consent c, String race, String ethnicity, ArrayList<PdxPatientTreatment> rx, String vs,
                       String clinicalTreatmentSetting, String treatmentNotes){
        this.pdtc = pdtc;
        this.submitterPatientID=id;
        this.sex= s;
        this.ageAtDiagnosis = ageAtDiagnosis;
        this.consent=c;
        this.ethnicity=ethnicity;
        this.race = race;
        this.patientTreatments=rx;
        this.virologyStatus = vs;
        this.clinicalTreatmentSetting = clinicalTreatmentSetting;
        this.treatmentNotes = treatmentNotes;
    }

    /**
     * @return the patientTreatments
     */
    public ArrayList<PdxPatientTreatment> getPatientTreatments() {
        return patientTreatments;
    }

    /**
     * @param patientTreatments the patientTreatments to set
     */
    public void setPatientTreatments(ArrayList<PdxPatientTreatment> patientTreatments) {
        this.patientTreatments = patientTreatments;
    }

    /**
     * @return the virologyStatus
     */
    public String getVirologyStatus() {
        return virologyStatus;
    }

    /**
     * @param virologyStatus the virologyStatus to set
     */
    public void setVirologyStatus(String virologyStatus) {
        this.virologyStatus = virologyStatus;
    }

    /**
     * @return the pdtc
     */
    public String getPdtc() {
        return pdtc;
    }

    /**
     * @param pdtc the pdtc to set
     */
    public void setPdtc(String pdtc) {
        this.pdtc = pdtc;
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






    public static class Builder {
        private String pdtc;
        private  String submitterPatientID;
        private  Sex sex;
        private  Age ageAtDiagnosis;
        
        private  Consent consent;
        private  String ethnicity;
        private  String race;
        private ArrayList<PdxPatientTreatment> patientTreatments;
        private String virologyStatus;
        private String clinicalTreatmentSetting;
        private String treatmentNotes;


        public Builder(String pdtc, String id){
            this.pdtc = pdtc;
            this.submitterPatientID=id;
           
        }
        
        public Builder sex(Sex sex){
            this.sex =sex;
            return this;
        } 
        
        public Builder ageAtDiagnosis(Age aaD){
            this.ageAtDiagnosis = aaD;
            return this;
        }
        
       
        
        
        public Builder consent(Consent consent){
            this.consent = consent;
            return this;
        }
        
        public Builder ethnicity(String ethnicity){
            this.ethnicity = ethnicity;
            return this;
        }
        
        public Builder race(String race){
            this.race = race;
            return this;
        }
        
        public Builder virologyStatus(String status) {
            virologyStatus=status;
            return this;
        }
        
        public Builder treatments(ArrayList<PdxPatientTreatment> treatments){
            patientTreatments = treatments;
            return this;
        }
        
        public Builder clinicalTreatmnetSetting(String cts){
            clinicalTreatmentSetting = cts;
            return this;
        }
        
        public Builder treatmentNotes(String notes){
            treatmentNotes = notes;
            return this;
        }
        
        
        public PdxPatient build() {
        
            PdxPatient pat = new PdxPatient(pdtc,submitterPatientID, sex, ageAtDiagnosis,  consent, race,ethnicity, patientTreatments, virologyStatus, clinicalTreatmentSetting, treatmentNotes);
  
            return pat;
        }


    }

}
