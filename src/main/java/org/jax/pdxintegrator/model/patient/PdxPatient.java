package org.jax.pdxintegrator.model.patient;

import com.github.phenomics.ontolib.ontology.data.TermId;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class PdxPatient {


    public PdxPatient(){

    }






    public static class Builder {
        private final String submitterPatientID;
        private final Gender gender;
        private final Age age;
        private final TermId diagnosis;
        private final Consent consent;
        private final EthnicityRace ethnicityRace;

        private List<String> currentTreatmentDrugs=new ArrayList<>();
        private List<String> currentTreatmentProtocol=new ArrayList<>();
        private List<String> priorTreatmentProtocol=new ArrayList<>();
        private List<String> responseToPriorTreatment=new ArrayList<>();
        private String virologyStatus=null;


        public Builder(String id, Gender g, Age a, TermId dx, Consent c, EthnicityRace er){
            submitterPatientID=id;
            gender=g;
            age=a;
            diagnosis=dx;
            consent=c;
            ethnicityRace =er;
        }
        /** TODO encapsulate drug and protocol in a class to make sure the order is correct. */
        public Builder currentTreatmentDrug(String drug) {
            currentTreatmentDrugs.add(drug);
            return this;
        }
        /** TODO encapsulate drug and protocol in a class to make sure the order is correct. */
        public Builder currentTreatmentProtocol(String protocol) {
            currentTreatmentProtocol.add(protocol);
            return this;
        }


        public Builder priorTreatmentProtocol(String priorProtocol) {
            priorTreatmentProtocol.add(priorProtocol);
            return this;
        }


        public Builder responseToPriorTreatment(String response) {
            responseToPriorTreatment.add(response);
            return this;
        }

        public Builder virologyStatus(String status) {
            virologyStatus=status;
            return this;
        }
    }

}
