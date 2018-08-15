/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jax.pdxintegrator.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.apache.jena.assembler.JA.data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.model.PdxModel;
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
import org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;
import org.jax.pdxintegrator.rdf.PdxModel2Rdf;

/**
 *
 * @author sbn
 */
public class BCMDataCommand extends Command {

    private static final Logger logger = LogManager.getLogger();

    private HashMap<String, Integer> labelIndex = new HashMap();
    private HashMap<String, String[]> modelData = new HashMap();
    private HashMap<String, String[]> treatmentMap = new HashMap();

    private ArrayList<PdxModel> models = new ArrayList<>();

    public BCMDataCommand() {
        logger.trace("Loading BCM data");

    }

    @Override
    public void execute() {

        String base = "C:/Users/sbn/Desktop/PDXNet/BCM/BCM";

        String[] files = {"ModelDetails.txt", "Patient.txt", "StudyTreatments.txt",
            "ModelStudy.txt", "PatientTreatments.txt", "Tumor.txt", "Omics.txt", "QA.txt"};

        HashMap<String, PdxPatient> patients = buildPatients(parse(base + "Patient.txt"));
        HashMap<String, ArrayList<PdxPatientTreatment>> patientTreatments = buildPatientTreatments(parse(base + "PatientTreatments.txt"));

        HashMap<String, ArrayList<PdxClinicalTumor>> clinicalTumors = buildTumors(parse(base + "Tumor.txt"));
        HashMap<String, ArrayList<PdxModelCreation>> modelCreations = buildModelCreations(parse(base + "ModelDetails.txt"));

        HashMap<String, ArrayList<PdxOmicsFile>> omicsFiles = buildOmicsFiles(parse(base + "Omics.txt"));

        HashMap<String, ArrayList<PdxQualityAssurance>> qas = buildQAs(parse(base + "QA.txt"));
        HashMap<String, ArrayList<PdxModelStudy>> modelStudies = buildModelStudies(parse(base + "ModelStudy.txt"));
        
        HashMap<String, ArrayList<PdxStudyTreatment>> studyTreatments = buildStudyTreatments(parse(base+"StudyTreatments.txt"));

        for (String patientID : patients.keySet()) {
            PdxPatient modelPatient = patients.get(patientID);
            modelPatient.setPatientTreatments(patientTreatments.get(patientID));
            ArrayList<PdxClinicalTumor> modelTumors = clinicalTumors.get(patientID);

            ArrayList<PdxQualityAssurance> modelQAs = new ArrayList<>();
            ArrayList<PdxModelStudy> modelModelStudies = new ArrayList<>();

            ArrayList<PdxOmicsFile> modelOmicsFiles = new ArrayList<>();

            ArrayList<PdxModelCreation> modelModelCreations = new ArrayList<>();
            for (PdxClinicalTumor tumor : modelTumors) {
                modelModelCreations.addAll(modelCreations.get(tumor.getSubmitterTumorID()));

            }

            for (PdxModelCreation mc : modelModelCreations) {
                modelQAs.addAll(qas.get(mc.getModelID()));
                if(modelStudies.containsKey(mc.getModelID())){
                    modelModelStudies.addAll(modelStudies.get(mc.getModelID()));
                    
                }else{
                    System.out.println("no studies for "+mc.getModelID());
                }
                modelOmicsFiles.addAll(omicsFiles.get(mc.getModelID()));
            }
            for(PdxModelStudy study : modelModelStudies){
                if(studyTreatments.containsKey(study.getModelID())){
                    study.setTreatments(studyTreatments.get(study.getModelID()));
                }
            }

            System.out.println("Patient " + patientID + " has " + modelTumors.size() + " tumors, " + modelModelCreations.size() + " modelcreations, " + modelQAs.size() + " qas, " + modelModelStudies.size() + " modelStudies, " + modelOmicsFiles.size() + " omicsfiles.");

            models.add(new PdxModel(modelPatient, modelTumors, modelModelCreations, modelQAs, modelModelStudies, modelOmicsFiles));
        }

        PdxModel2Rdf p2rdf = new PdxModel2Rdf(models);
        try {
            FileOutputStream fos = new FileOutputStream("bcmData.rdf", false);
            p2rdf.outputRDF(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> parse(String fileStr) {

        ArrayList<String> data = new ArrayList<>();
        try {
            File f = new File(fileStr);
            BufferedReader buf = new BufferedReader(new FileReader(f));
            String line;
            while ((line = buf.readLine()) != null) {
                if(line.trim().length()>0){
                    data.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private HashMap<String, PdxPatient> buildPatients(ArrayList<String> data) {
        // PDTC	Patient ID	Sex	Age at Primary Diagnosis
        //Race	Ethnicity	Virology Status	Consent for PDX Generation
        HashMap<String, PdxPatient> patients = new HashMap<>();
        for (String row : data) {
            String[] parts = row.split("\t",-1);
            PdxPatient.Builder builder = new PdxPatient.Builder(parts[0], parts[1]);
            String sex = parts[2];
            if (sex != null) {
                if (sex.toUpperCase().startsWith("F")) {

                    builder.sex(Sex.FEMALE);
                } else if (sex.toUpperCase().startsWith("M")) {
                    builder.sex(Sex.MALE);
                }
            }

            builder.ageAtDiagnosis(Age.getAgeForString(parts[3]));
            builder.race(parts[4]);
            builder.ethnicity(parts[5]);
            builder.virologyStatus(parts[6]);
            String consent = parts[7];
            if (consent.toUpperCase().startsWith("Y")) {
                builder.consent(Consent.YES);
            } else {
                builder.consent(Consent.NO);
            }

            patients.put(parts[1], builder.build());

        }
        return patients;
    }

    private HashMap<String, ArrayList<PdxClinicalTumor>> buildTumors(ArrayList<String> data) {
        //Patient ID	Tumor ID	Event Index	Collection Procedure
        //Treatment Naïve	Age at collection	Initial Diagnosis
        //Clinical Event Point	Primary Tumor Origin	Specimen Tissue	
        //Tissue Histology	Clinical Diagnostic Markers	Tumor Grade	
        //T Stage	N Stage 	M Stage	Overall Stage	Sample Type	
        //Known Metastatic Sites	Short Tandem Repeat (STR) Analysis	
        //List each STR marker in a separate column to fill in values	STR Evaluation
        HashMap<String, ArrayList<PdxClinicalTumor>> tumors = new HashMap<>();
        for (String row : data) {
            String[] parts = row.split("\t",-1);
            PdxClinicalTumor tumor = new PdxClinicalTumor(parts[0],parts[1]);
            tumor.setEventIndex(parts[2]);
            tumor.setCollectionProcedure(parts[3]);
            
            if(parts[4].toLowerCase().startsWith("y")){
                tumor.setTreatmentNaive(true);
            }else if(parts[4].toLowerCase().startsWith("n")){
                tumor.setTreatmentNaive(false);
            }
            
            tumor.setAgeAtCollection(parts[5]);
            tumor.setInitialDiagnosis(parts[6].replaceAll("\"",""));
            tumor.setClinicalEventPoint(parts[7]);
            tumor.setTissueOfOrigin(parts[8]);
            tumor.setSpecimenTissue(parts[9].replaceAll("\"",""));
            tumor.setTissueHistology(parts[10].replaceAll("\"",""));
            tumor.setClinicalMarkers(parts[11]);
            tumor.setTumorGrade(parts[12]);
            tumor.setTStage(parts[13]);
            tumor.setNStage(parts[14]);
            tumor.setMStage(parts[15]);
            tumor.setOverallStage(parts[16]);
            tumor.setSampleType(parts[17]);
            tumor.setMetastaticSites(parts[18].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",",";").replaceAll("\"", ""));
            tumor.setStrAnalysis(parts[19]);
            //str list?? parts[20]
            tumor.setStrEvaluation(parts[21]);
            
            if(tumors.containsKey(parts[0])){
                tumors.get(parts[0]).add(tumor);
            }else{
                ArrayList<PdxClinicalTumor> list = new ArrayList<>();
                list.add(tumor);
                tumors.put(parts[0],list);
            }
        }
        return tumors;
    }

    private HashMap<String, ArrayList<PdxPatientTreatment>> buildPatientTreatments(ArrayList<String> data) {
        //Patient ID	Event Index	clincal treatment setting	Regimen (Drug)
        //Clinical Response	Pathological Response	Reason Stopped	Treatment Notes

        HashMap<String, ArrayList<PdxPatientTreatment>> treatments = new HashMap<>();
        for (String row : data) {
            String[] parts = row.split("\t",-1);
            PdxPatientTreatment treatment = new PdxPatientTreatment();
            treatment.setPatientID(parts[0]);
            treatment.setEventIndex(parts[1]);
            treatment.setClinicalTreatmentSetting(parts[2]);
            treatment.setRegimen(parts[3].replaceAll("\\[","").replaceAll("\\]","").replaceAll(",",";").replaceAll("\"",""));
            treatment.setResponse(ResponseToTreatment.getResponse(parts[4]));
            //treatment.setPathologicalResponse(getResponse(parts[5]));
            treatment.setReasonStopped(parts[6]);
            treatment.setTreatmentNotes(parts[7]);
            
            if(treatments.containsKey(parts[0])){
                treatments.get(parts[0]).add(treatment);
            }else{
                ArrayList<PdxPatientTreatment> list = new ArrayList<>();
                list.add(treatment);
                treatments.put(parts[0],list);
            }
           
        }
        return treatments;
    }

    private HashMap<String, ArrayList<PdxModelCreation>> buildModelCreations(ArrayList<String> data) {
    /*    Tumor ID
            Model ID
            Host Strain
            Mouse Source/Vendor
            Source/Vendor Model Number
            Mouse Sex
            Is Mouse Humanized For Initial Engraftment?
            Humanization Type
            Engraftment Material
            Treatment for Engraftment
            Engraftment Procedure
            Engraftment Site
            Was tumor of origin tissue cryopreserved prior to engraftment?
            PDX Model Histology
            PDX Doubling Time
            Can PDX be viably cryopreserved for Passage?
            Cryopreserved tissue engraftment quality
            PDX Model Metastasis
            Known Model Metastatic site(s)
            Does PDX model Macro-Metastasis Require Excision  of the primary tumor?
            Is this a Subline of Another Model
            Subline Reason
            */
        HashMap<String, ArrayList<PdxModelCreation>> models = new HashMap<>();
        for (String row : data) {
            String[] parts = row.split("\t",-1);
            
            PdxModelCreation model = new PdxModelCreation(parts[0],parts[1]);
            model.setMouseStrain(parts[2]);
            model.setMouseSource(parts[3]+ " "+parts[4]);
            model.setMouseSex(parts[5]);
            
            //always no
            if(parts[6].toLowerCase().startsWith("y"))
                model.setHumanized(true);
            if(parts[6].toLowerCase().startsWith("n")){
                model.setHumanized(false);
            }
           // always not applicable
            model.setHumanizationType(parts[7]);
            model.setEngraftmentMaterial(parts[8]);
            model.setTreatmentForEngraftment(parts[9]);
            model.setEngraftmentProcedure(parts[10]);
            model.setEngraftmentSite(parts[11]);
            model.setCryopreservedBeforeEngraftment(getBoolean(parts[12]));
            model.setModelHistology(parts[13]);
            try{
                model.setDoublingTime(new Integer(parts[14]));
            }catch(NumberFormatException e){
                System.out.println("cant convert "+parts[14]+" into a doubling time");
            }
            model.setViablyCryopresered(getBoolean(parts[15]));
            // missing this field in RDF
            //model.setCryopreservedQuaility(parts[16]);
            
            if(parts[17].toLowerCase().startsWith("y"))
                model.setMetastasis(true);
            if(parts[17].toLowerCase().startsWith("n") || parts[17].toLowerCase().startsWith("micro")){
                model.setMetastasis(false);
            }
            
            model.setMetastaticSites(parts[18]);
            
            if(parts[19].toLowerCase().startsWith("y"))
                model.setMacroMetastasisRequiresExcision(true);
            if(parts[19].toLowerCase().startsWith("n")){
                model.setMacroMetastasisRequiresExcision(false);
            }
           
         //   model.setSublineOfModel(parts[12]);
        //    model.setSublineReason(parts[13]);
        
            if(models.containsKey(parts[0])){
                models.get(parts[0]).add(model);
            }else{
                ArrayList<PdxModelCreation> list = new ArrayList<>();
                list.add(model);
                models.put(parts[0],list);
            }
            
        }
        return models;
    }

    private HashMap<String, ArrayList<PdxQualityAssurance>> buildQAs(ArrayList<String> data) {
        // Model ID	Short Tandem Repeat (STR) Analysis	Passage Tested	
        //STR Evaluation	STR Notes	Clinical Diagnostic Markers	
        //Assay Result	Clinical Diagnostic Marker Notes	CD45 IHC	
        //Assay Result	Pan Cytokeratin	Assay Result	Human-specific Cytokeratin 19	
        //EBV Transcript Detection	Assay Result	Mouse Pathogen Status	Overall Evaluation
        HashMap<String, ArrayList<PdxQualityAssurance>> qas = new HashMap<>();
        for (String row : data) {
            String[] parts = row.split("\t",-1);
            PdxQualityAssurance qa = new PdxQualityAssurance(parts[0]);
            qa.setStrAnalysis(parts[1]);
            qa.setPassageTested(getPassage(parts[2]));
            qa.setStrEvaluation(parts[3]);
            qa.setStrNotes(parts[4]);
            qa.setClinicalDiagnosticMarkers(parts[5].replaceAll("\\[","").replaceAll("\\]","").replaceAll(",",";"));
            qa.setCd45IHC(parts[6]);
            qa.setCd45IHCAssayResult(parts[7]);
            qa.setPanCytokeratin(parts[8]);
            qa.setPanCytokeratinAssayResult(parts[9]);
            qa.setHumanSpecificCytokeratin19(parts[10]);
            qa.setEbvTranscriptDetection(parts[11]);
            qa.setEbvTranscriptDetectionResult(parts[11]);
            qa.setMousePathogenStatus(parts[12]);
            qa.setOverallEvaluation(parts[13]);
            
            if(qas.containsKey(parts[0])){
                qas.get(parts[0]).add(qa);
            }else{
                ArrayList<PdxQualityAssurance> list = new ArrayList<>();
                list.add(qa);
                qas.put(parts[0], list);
            }
            
            
        }
        return qas;
    }

    private HashMap<String, ArrayList<PdxModelStudy>> buildModelStudies(ArrayList<String> data) {
        //Model ID	Study ID	Study Description	Passage Used	Host Strain
        //Implantation site	Baseline Tumor Target Size	Endpoint 1	Endpoint 2	Endpoint 3

        HashMap<String, ArrayList<PdxModelStudy>> studies = new HashMap<>();
        for (String row : data) {
            String[] parts = row.split("\t",-1);
            parts[0] = "BCM-"+parts[0];
            PdxModelStudy study = new PdxModelStudy(parts[0], parts[1]);
            study.setDescription(parts[2]);
            study.setPassage(getPassage(parts[3]));
            study.setHostStrain(parts[4]);
            study.setImplantationSite(parts[5]);
            study.setBaselineTumorTargetSize(parts[6]);
          
            
            // using modelID as key won't work ??
            if(studies.containsKey(parts[0])){
                studies.get(parts[0]).add(study);
            }else{
                ArrayList<PdxModelStudy> list = new ArrayList<>();
                list.add(study);
                studies.put(parts[0],list);
            }
            
        }
        return studies;
    }

    private HashMap<String, ArrayList<PdxStudyTreatment>> buildStudyTreatments(ArrayList<String> data) {
        //Model ID	Study ID	Cohort/Treatment Arm Designation	
        //Cohort Size	Drug	NSC#	Dose	Route	Dosing Schedule	Number of Cycles
        //Study Duration	Endpoint 1 Response	Endpoint 2 Response	Endpoint 3 Response

        HashMap<String, ArrayList<PdxStudyTreatment>> treatments = new HashMap<>();
        for (String row : data) {
            String[] parts = row.split("\t",-1);
            parts[0] = "BCM-"+parts[0];
            PdxStudyTreatment trt = new PdxStudyTreatment(parts[1]);
            trt.setModelID(parts[0]);
            trt.setCohort(parts[2]);
            trt.setCohortSize(new Integer(parts[3]));
            trt.setDrug(parts[4]);
            trt.setDose(parts[6]);
            trt.setRoute(parts[7]);
            trt.setDosingSchedule(parts[8]);
            trt.setNumberOfCycles(new Integer(parts[9]));
            trt.setStudyDuration(new Integer(parts[10].toLowerCase().replace("days", "").trim()));
            trt.setEndpoint1Response(ResponseToTreatment.getResponse(parts[11]));
            
             if(treatments.containsKey(parts[0])){
                treatments.get(parts[0]).add(trt);
            }else{
                ArrayList<
                        PdxStudyTreatment> list = new ArrayList<>();
                list.add(trt);
                treatments.put(parts[0],list);
            }
            
        }
        return treatments;
    }

    private HashMap<String, ArrayList<PdxOmicsFile>> buildOmicsFiles(ArrayList<String> data) {

        //Patient ID	Model ID	Access Level	Created datetime	
        //Data Category	Data Format	Data Type	Sample Type	
        //Experimental Strategy	File Size	Platform	Capture Kit	
        //Updated Datetime	Is FFPE	Paired_end	File name	Passage
        HashMap<String, ArrayList<PdxOmicsFile>> omics = new HashMap<>();
        
        String fileName ="placeHolderOmicsFileName";
        int index = 1;

        for (String row : data) {

            String[] parts = row.split("\t",-1);

            PdxOmicsFile omicsFile = new PdxOmicsFile();

            omicsFile.setPatientID(parts[0]);
            omicsFile.setModelID(parts[1]);
            omicsFile.setAccessLevel(parts[2]);
            omicsFile.setCreatedDateTime(parts[3]);
            omicsFile.setDataCategory(parts[4]);
            omicsFile.setDataFormat(parts[5]);
            omicsFile.setDataType(parts[6]);
            omicsFile.setSampleType(parts[7]);
            omicsFile.setExperimentalStrategy(parts[8]);
            omicsFile.setFileSize(parts[9]);
            omicsFile.setPlatform(parts[10]);
            omicsFile.setCaptureKit(parts[11]);
            omicsFile.setUpdatedDateTime(fixDate(parts[12]));
            omicsFile.setIsFFPE(getBoolean(parts[13]));
            omicsFile.setIsPairedEnd(getBoolean(parts[14]));
            //omicsFile.setFileName(parts[15]);
            //fix me!
            omicsFile.setFileName(fileName+index++);
            omicsFile.setPassage(parts[16]);
            
            if(omics.containsKey(omicsFile.getModelID())){
                omics.get(omicsFile.getModelID()).add(omicsFile);
            }else{
                
                ArrayList<PdxOmicsFile> list = new ArrayList<>();
                list.add(omicsFile);
                omics.put(omicsFile.getModelID(), list);
            }
        }
        return omics;
    }

    
    private Integer getPassage(String p){
        Integer passage = null;
        try{
            passage = new Integer(p.toLowerCase().replace("p", "").trim());
        }catch(Exception e){
         System.out.println("can't convert passage "+p+" to integer");
         
        }
        return passage;
    }
    
    private Boolean getBoolean(String b){
       b = b.toLowerCase();
        switch(b){
            case "yes":
                    return true;
            case "true":
                    return true;
            case "no":
                    return false;
            case "false":
                    return false;
            default:
                    return null;
        }
    }
    
    // i think dates should be yyyy-mm-dd
    // we get ddmmyyyy or maybe mmddyyyy (yay)
    private String fixDate(String date){
        String fixedDate = null;
        if(date != null && date.length()==8){
            fixedDate = date.substring(4)+"-"+date.substring(2,4)+"-"+date.substring(0,2);
        }
        return fixedDate;
    }
    
}
