package org.jax.pdxintegrator.model.patient;

import com.github.phenomics.ontolib.ontology.data.TermId;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PdxPatient {

    private final String submitterPatientID;
    private final Sex sex;
    private final Age ageAtDiagnosis;
    private final Age ageAtCollection;
    private final TermId diagnosisTerm;
    private final String diagnosis;
    private final Consent consent;
    private final String ethnicity;
    private final String race;
    private ArrayList<PdxPatientTreatment> patientTreatments;
    private String virologyStatus;

 
    public String getSubmitterPatientID() {
        return submitterPatientID;
    }

    public Sex getSex() {
        return sex;
    }

    public Age getAgeAtDiagnosis() {
        return ageAtDiagnosis;
    }
    
    public Age getAgeAtCollection() {
        return ageAtCollection;
    }

    public TermId getDiagnosisTerm() {
        return diagnosisTerm;
    }
    
    public String diagnosis() {
        return diagnosis;
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

    private PdxPatient(String id, Sex s, Age ageAtDiagnosis, Age ageAtCollection, TermId dx, String diagnosis, Consent c, String race, String ethnicity, ArrayList<PdxPatientTreatment> rx, String vs){
        this.submitterPatientID=id;
        this.sex= s;
        this.ageAtDiagnosis = ageAtDiagnosis;
        this.ageAtCollection = ageAtCollection;
        this.diagnosisTerm=dx;
        this.diagnosis = diagnosis;
        this.consent=c;
        this.ethnicity=ethnicity;
        this.race = race;
        this.patientTreatments=rx;
        this.virologyStatus = vs;
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






    public static class Builder {
        private  String submitterPatientID;
        private  Sex sex;
        private  Age ageAtDiagnosis;
        private  Age ageAtCollection;
        private  TermId diagnosisTerm;
        private  String diagnosis;
        private  Consent consent;
        private  String ethnicity;
        private  String race;
        private ArrayList<PdxPatientTreatment> patientTreatments;
        private String virologyStatus;


        public Builder(String id){
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
        
        public Builder ageAtCollection(Age aaC){
            this.ageAtCollection = aaC;
            return this;
        }
        
        public Builder diagnosisTerms(TermId diagnosis){
            this.diagnosisTerm = diagnosis;
            return this;
        }
        
        
        public Builder diagnosis(String diagnosis){
            this.diagnosis  = diagnosis;
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
        
        
        public PdxPatient build() {
        
            PdxPatient pat = new PdxPatient(submitterPatientID, sex, ageAtDiagnosis, ageAtCollection, diagnosisTerm, diagnosis, consent, race,ethnicity, patientTreatments, virologyStatus);
  
            return pat;
        }


    }

}
