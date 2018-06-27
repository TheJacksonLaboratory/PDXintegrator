/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jax.pdxintegrator.command;

import com.github.phenomics.ontolib.ontology.data.TermId;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.rdf.PdxModel2Rdf;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.modelstudy.PdxModelStudy;
import org.jax.pdxintegrator.model.modelstudy.PdxStudyTreatment;
import org.jax.pdxintegrator.model.omicsfile.PdxOmicsFile;
import org.jax.pdxintegrator.model.patient.Age;
import org.jax.pdxintegrator.model.patient.Consent;
import org.jax.pdxintegrator.model.patient.PdxPatient;
import org.jax.pdxintegrator.model.patient.PdxPatientTreatment;
import org.jax.pdxintegrator.model.patient.Sex;
import org.jax.pdxintegrator.model.qualityassurance.PdxQualityAssurance;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;

public class JAXDataCommand extends Command {

    private static final Logger logger = LogManager.getLogger();

    private HashMap<String, Integer> labelIndex = new HashMap();
    private HashMap<String, String[]> modelData = new HashMap();

    private List<PdxModel> models = new ArrayList<>();

    public JAXDataCommand() {
        logger.trace("Loading JAX data");

    }

    @Override
    public void execute() {

        parse();
        outputData();

    }

    private void outputData() {
        logger.trace("We will now output JAX PDX data for "+models.size()+" models.");

        PdxModel2Rdf p2rdf = new PdxModel2Rdf(models);
        try{
            FileOutputStream fos = new FileOutputStream("jaxData.rdf", false);
            p2rdf.outputRDF(fos);
        }catch(Exception e){
            e.printStackTrace();
        }
       

    }

    

    private void parse() {

        try {

            String[] labels = {"Model ID", "Project Type", "Model Status", "Model", "Model AKA", "MRN", "Gender", "Age",
                "Race", "Ethnicity", "Specimen Site", "Primary Site", "Initial Diagnosis",
                "Clinical Diagnosis", "Other Diagnosis Info", "Tumor Type", "Grades", "Markers",
                "Model Tags", "Stages", "M-Stage", "N-Stage", "T-Stage", "Sample Type", "Stock Num",
                "Strain", "Mouse Sex", "Engraftment Site", "Collecting Site", "Collection Date",
                "Received Date", "Accession Date", "P0 Engraftment Date", "P0 Success Date",
                "P1 Engraftment Date", "P1 Success Date", "P2 Engraftment Date", "P2 Success Date", "Comments"};

            for (int i = 0; i < labels.length; i++) {
                labelIndex.put(labels[i], i);
            }
            BufferedReader buf = new BufferedReader(new FileReader("C:/activePDX.txt"));
            String line = buf.readLine();
            while (line != null) {

                String[] parts = line.split("\t");
                build(parts);
                line = buf.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void build(String[] data) {

        PdxPatient patient = buildPatient(data);

        ArrayList<PdxClinicalTumor> clinicalTumors = buildClinicalTumors(data);
        ArrayList<PdxModelCreation> modelCreations = buildModelCreations(data);

        ArrayList<PdxOmicsFile> omicsFiles = new ArrayList();

        ArrayList<PdxQualityAssurance> qas = buildQualityAssuranceModule(data);
        ArrayList<PdxModelStudy> modelStudies = buildModelStudies(data);

        // omicsFiles.add(buildOmicsFile(null, patientID));
        // same for other categories
        models.add(new PdxModel(patient, clinicalTumors, modelCreations, qas, modelStudies, omicsFiles));
    }

    private PdxOmicsFile buildOmicsFile(String modelID, String patientID) {
        PdxOmicsFile omicsFile = new PdxOmicsFile();
        omicsFile.setAccessLevel("access level value");
        omicsFile.setCaptureKit("capture kit value");
        omicsFile.setCreatedDateTime("Tuesday at noon");
        omicsFile.setDataCategory("data category value");
        omicsFile.setDataFormat("data format value");
        omicsFile.setDataType("data type value");
        omicsFile.setExperimentalStrategy("exp strategy");
        omicsFile.setFileName("simulatedOmicsFile");
        omicsFile.setFileSize("34K");
        omicsFile.setIsFFPEPairedEnd("yes");
        omicsFile.setModelID(modelID);
        omicsFile.setPatientID(patientID);
        omicsFile.setPassage("P1");
        omicsFile.setPlatform("whole exome");
        omicsFile.setSampleType("tumor");
        omicsFile.setUpdatedDateTime("update date");

        return omicsFile;
    }

    private ArrayList<PdxModelCreation> buildModelCreations(String[] data) {
        /*  private String tumorID;
            // pdmr or institution creating model?
            private String modelID;
            private String mouseStrain;
            private String mouseSource;
            private String mouseSex;
            private boolean humanized;
            private String humanizationType;
            private String engraftmentProcedure;
            private String engraftmnetSite;
            private String treatmentForEngraftment;
            private String modelHistology;
            private boolean tissueCryoPreserved;
            private String engraftmentRate;
            private String engraftmentTime;
            private String sublineOfModel;
            private String sublineReason;
        
         */
        ArrayList<PdxModelCreation> modelCreations = new ArrayList();

        String modelID = getModelID(data);

        PdxModelCreation modelCreation = new PdxModelCreation(getTumorID(data), getModelID(data));
        modelCreation.setMouseStrain("NOD.Cg-Prkdc<scid> Il2rg<tm1Wj>l/SzJ");
        modelCreation.setMouseSource("The Jackson Lab stock #" + data[labelIndex.get("Stock Num")]);
        modelCreation.setMouseSex(data[labelIndex.get("Mouse Sex")]);
        modelCreation.setHumanized(false);
        modelCreation.setEngraftmnetSite(data[labelIndex.get("Engraftment Site")]);
        modelCreations.add(modelCreation);

        return modelCreations;
    }

    private ArrayList<PdxClinicalTumor> buildClinicalTumors(String[] data) {
        ArrayList<PdxClinicalTumor> tumors = new ArrayList();

        /* Populate these fields
                private String patientID;
                private String submitterTumorID;
                private String eventIndex;
                private boolean treatmentNaive;
                private String ageAtCollection;
                private String initialDiagnosis;
                private TermId initialDiagnosisTerm;

                // The term id for an UBERON term for the anatomical tissue of origin of the tumor. 
                private String tissueOfOrigin; // primary tissue
                private TermId tissueOfOriginTerm;
                private String diseaseProgression;
                // NCIT Term id for primary, recurrence, metastasis.
                private TermId diseaseProgressionTerm; // disease progression
                private String specimenTissue;
                private TermId specimenTissueTerm;

                private String tissueHistology;
                // NCIT Term id for tissue histology 
                private TermId tissueHistologyTerm;

                private String clinicalMarkers;
                private String tumorGrade;
                private String tStage;
                private String nStage;
                private String mStage;
                private String overallStage;
                private String sampleType;

         */
        PdxClinicalTumor tumor = new PdxClinicalTumor(getPatientID(data), getTumorID(data));

        tumor.setClinicalMarkers(data[labelIndex.get("Markers")]);
        tumor.setMStage(data[labelIndex.get("M-Stage")]);
        tumor.setNStage(data[labelIndex.get("N-Stage")]);
        tumor.setTStage(data[labelIndex.get("T-Stage")]);
        tumor.setOverallStage(data[labelIndex.get("Stages")]);
        tumor.setTumorGrade(data[labelIndex.get("Grades")]);
        tumor.setSampleType(data[labelIndex.get("Sample Type")]);
        tumor.setTissueOfOrigin(data[labelIndex.get("Primary Site")]);
        tumor.setTissueHistology(data[labelIndex.get("Clinical Diagnosis")]);

        tumor.setSpecimenTissue(data[labelIndex.get("Specimen Site")]);
        tumor.setDiseaseProgression(data[labelIndex.get("Tumor Type")]);
        tumors.add(tumor);

        return tumors;
    }

    private ArrayList<PdxQualityAssurance> buildQualityAssuranceModule(String data[]) {

        ArrayList<PdxQualityAssurance> qas = new ArrayList();
        PdxQualityAssurance qa = new PdxQualityAssurance(getModelID(data));

        qa.setPassage(0);
        qa.setQcMethod("QCMethod");

        qa.setQcResult("QC result");

        qas.add(qa);
        return qas;
    }

    private ArrayList<PdxModelStudy> buildModelStudies(String[] data) {
        /*
         private String modelID;
        private String studyID;
        private boolean metastasis;
        private int metastasisPassage;
        private TermId metastasisLocation; // could be multiple locations
        private int doublingLagTime;  // days?
        private ResponseToTreatment response;
        private ArrayList<PdxStudyTreatment> treatments;
         */
        ArrayList<PdxModelStudy> studies = new ArrayList();

        PdxModelStudy.Builder builder = new PdxModelStudy.Builder(getModelID(data), getStudyID(data));

        builder.treatments(buildStudyTreatments(getStudyID(data)));
        studies.add(builder.build());

        return studies;
    }

    private PdxPatient buildPatient(String[] data) {

        /*
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
         */
        PdxPatient.Builder builder = new PdxPatient.Builder("JAX", getPatientID(data));

        String sex = data[labelIndex.get("Gender")];
        if(sex != null){
            if(sex.toUpperCase().startsWith("F")){
               
                builder.sex(Sex.FEMALE);
            }else if(sex.toUpperCase().startsWith("M")){
                builder.sex(Sex.MALE);
            }
        }
        
        builder.consent(Consent.YES);

        // WRONG!
        builder.ageAtDiagnosis(getAge(data[labelIndex.get("Age")]));
        builder.ethnicity(data[labelIndex.get("Ethnicity")]);
        builder.race(data[labelIndex.get("Race")]);

        return builder.build();
    }

    private ArrayList<PdxPatientTreatment> buildPatientTreatments() {
        ArrayList<PdxPatientTreatment> treatments = new ArrayList();
        int numTreatments = 4;
        while (numTreatments > 0) {
            PdxPatientTreatment treatment = new PdxPatientTreatment();
            treatment.setEventIndex(numTreatments + "");

            treatment.setRegimen("patient treatment drug");
            treatment.setReasonStopped("did not tolerate");
           // treatment.setResponse(ResponseToStandardOfCare);
            treatments.add(treatment);
          //  numTreatments--;
        }
        return treatments;
    }

    private ArrayList<PdxStudyTreatment> buildStudyTreatments(String studyID) {
        ArrayList<PdxStudyTreatment> treatments = new ArrayList();
        int numTreatments = 0;
        while (numTreatments > 0) {
            PdxStudyTreatment.Builder builder = new PdxStudyTreatment.Builder(studyID);
            builder.setDose("1");
            builder.setUnits("mg/kg");
            builder.setFrequency("Daily");
            builder.setRoute("in chow");
            builder.setDrug("treatment drug");

            treatments.add(builder.build());

            numTreatments--;
        }
        return treatments;
    }

    private String getTumorID(String[] data) {
        return "JAX-Tumor-" + data[labelIndex.get("Model ID")];
    }

    private String getPatientID(String[] data) {
        return "JAX-PT-" + data[labelIndex.get("Model ID")];
    }

    private String getStudyID(String[] data) {
        return "JAX-Study-" + data[labelIndex.get("Model ID")];
    }

    private String getModelID(String[] data) {
        return "JAX-" + data[labelIndex.get("Model ID")];
    }
    
    private Age getAge(String age){
        Age a = Age.notProvided;
    
        try{
            int ageInt = new Integer(age);
            if(ageInt < 5) return Age.age0_4;
            if(ageInt < 10) return Age.age5_9;
            if(ageInt < 15) return Age.age10_14;
            if(ageInt < 20) return Age.age15_19;
            if(ageInt < 25) return Age.age20_24;
            if(ageInt < 30) return Age.age25_29;
            if(ageInt < 35) return Age.age30_34;
            if(ageInt < 40) return Age.age35_39;
            if(ageInt < 45) return Age.age40_44;
            if(ageInt < 50) return Age.age45_49;
            if(ageInt < 55) return Age.age50_54;
            if(ageInt < 60) return Age.age55_59;
            if(ageInt < 65) return Age.age60_64;
            if(ageInt < 70) return Age.age65_69;
            if(ageInt < 75) return Age.age70_74;
            if(ageInt < 80) return Age.age75_79;
            if(ageInt < 85) return Age.age80_84;
            if(ageInt < 90) return Age.age85_89;
            if(ageInt < 95) return Age.age90_94;
            if(ageInt < 100) return Age.age95_99;
            if(ageInt < 105) return Age.age100_104;
        }catch(Exception e){}
        return a;
            
            
        
    }

}
