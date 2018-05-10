package org.jax.pdxintegrator.model.patient;

import com.github.phenomics.ontolib.ontology.data.TermId;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PdxPatient {

    private final String submitterPatientID;
    private final Sex sex;
    private final Age age;
    private final TermId diagnosis;
    private final Consent consent;
    private final EthnicityRace ethnicityRace;
    private ArrayList<PdxPatientTreatment> patientTreatments;
    private String virologyStatus;

 
    public String getSubmitterPatientID() {
        return submitterPatientID;
    }

    public Sex getSex() {
        return sex;
    }

    public Age getAge() {
        return age;
    }

    public TermId getDiagnosis() {
        return diagnosis;
    }

    public Consent getConsent() {
        return consent;
    }

  

    public EthnicityRace getEthnicityRace() {
        return ethnicityRace;
    }

    private PdxPatient(String id, Sex g, Age a, TermId dx, Consent c, EthnicityRace er, ArrayList<PdxPatientTreatment> rx, String vs){
        submitterPatientID=id;
        sex=g;
        age=a;
        diagnosis=dx;
        consent=c;
        ethnicityRace =er;
        patientTreatments=rx;
        virologyStatus = vs;
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
        private final String submitterPatientID;
        private final Sex gender;
        private final Age age;
        private final TermId diagnosis;
        private final Consent consent;
        private final EthnicityRace ethnicityRace;
        private String virologyStatus;
        private ArrayList<PdxPatientTreatment> patientTreatments;


        public Builder(String id, Sex g, Age a, TermId dx, Consent c, EthnicityRace er,String vs){
            submitterPatientID=id;
            gender=g;
            age=a;
            diagnosis=dx;
            consent=c;
            ethnicityRace =er;
            virologyStatus = vs;
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
        
            PdxPatient pat = new PdxPatient(submitterPatientID,gender,age,diagnosis,consent,ethnicityRace,patientTreatments,virologyStatus);
  
            return pat;
        }


    }

}
