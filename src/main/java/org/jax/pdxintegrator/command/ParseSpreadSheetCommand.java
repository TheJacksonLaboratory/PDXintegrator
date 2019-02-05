/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jax.pdxintegrator.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jax.pdxintegrator.model.ModelTerms;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.modelstudy.PdxModelStudy;
import org.jax.pdxintegrator.model.modelstudy.PdxStudyTreatment;
import org.jax.pdxintegrator.model.omicsfile.PdxOmicsFile;
import org.jax.pdxintegrator.model.patient.Age;
import org.jax.pdxintegrator.model.patient.Consent;
import org.jax.pdxintegrator.model.patient.Ethnicity;
import org.jax.pdxintegrator.model.patient.PdxPatient;
import org.jax.pdxintegrator.model.patient.PdxPatientTreatment;
import org.jax.pdxintegrator.model.patient.Race;
import org.jax.pdxintegrator.model.patient.Sex;
import org.jax.pdxintegrator.model.qualityassurance.PdxQualityAssurance;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;
import org.jax.pdxintegrator.model.tumor.TumorGrade;
import org.jax.pdxintegrator.rdf.PdxModel2Rdf;

/**
 *
 * @author sbn
 */
public class ParseSpreadSheetCommand extends Command {
    
    
    private static int PATIENT_COLUMNS = 8;
    private static int PATIENT_TREATMENT_COLUMNS = 8;
    private static int TUMOR_COLUMNS = 22;
    private static int MODEL_DETAILS_COLUMNS = 22;
    private static int QA_COLUMNS = 18;
    
    private static int MODEL_STUDY_COLUMNS = 7;
    private static int STUDY_TREATMENT_COLUMNS = 14;
    private static int OMICS_FILE_COLUMNS = 17;

    private String spreadSheetFileString;
    private String pdtc; 
    private String rdfFileString;
    
    private HashMap<String,String> patientIDs = new HashMap();
    private HashMap<String,String> modelIDs = new HashMap();
    private HashMap<String,String> tumorIDs = new HashMap();
    private HashMap<String,String> modelStudyIDs = new HashMap();
    
    private StringBuilder messages = new StringBuilder();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

   
    
    public ParseSpreadSheetCommand(String spreadSheetFile, String rdfFile) {
        this.spreadSheetFileString = spreadSheetFile;
 
        this.rdfFileString = rdfFile;
    }

    public static void main(String[] args) {
        
        String xlsxFile = "C:/Users/sbn/Desktop/PDXNet/ryan/MDA.xlsx";
        String outFile = xlsxFile.replace(".xlsx", ".rdf");
        if(xlsxFile.equals(outFile)){
            System.out.println("Wrong format "+xlsxFile);
            System.exit(0);
                    
        }

        ParseSpreadSheetCommand pssc = new ParseSpreadSheetCommand(xlsxFile, outFile);

        pssc.execute();
        
       
    }

    public void execute() {

        try {
            File file = new File(spreadSheetFileString);

            if (file.exists()) {

                FileInputStream excelFile = new FileInputStream(file);

                Workbook workbook = new XSSFWorkbook(excelFile);

                if (8 == workbook.getNumberOfSheets()) {

                    validateCounts(workbook);

                    HashMap<String, PdxPatient> patients = buildPatient(getSheetData(workbook.getSheetAt(0)));
                    HashMap<String, ArrayList<PdxPatientTreatment>> patientTreatments = buildPatientTreatments(getSheetData(workbook.getSheetAt(1)));
                    HashMap<String, ArrayList<PdxClinicalTumor>> clinicalTumors = buildTumors(getSheetData(workbook.getSheetAt(2)));
                    HashMap<String, ArrayList<PdxModelCreation>> modelCreations = buildModelDetails(getSheetData(workbook.getSheetAt(3)));
                    HashMap<String, ArrayList<PdxQualityAssurance>> qas = buildQAs(getSheetData(workbook.getSheetAt(4)));
                    HashMap<String, ArrayList<PdxModelStudy>> modelStudies = buildModelStudies(getSheetData(workbook.getSheetAt(5)));
                    HashMap<String, ArrayList<PdxStudyTreatment>> studyTreatments = buildStudyTreatments(getSheetData(workbook.getSheetAt(6)));
                    HashMap<String, ArrayList<PdxOmicsFile>> omicsFiles = buildOmicsFiles(getSheetData(workbook.getSheetAt(7), true));

                    ArrayList<PdxModel> models = new ArrayList<>();

                    for (String patientID : patients.keySet()) {
                        PdxPatient modelPatient = patients.get(patientID);
                        modelPatient.setPatientTreatments(patientTreatments.get(patientID));
                        ArrayList<PdxClinicalTumor> modelTumors = clinicalTumors.get(patientID);

                        ArrayList<PdxQualityAssurance> modelQAs = new ArrayList<>();
                        ArrayList<PdxModelStudy> modelModelStudies = new ArrayList<>();

                        ArrayList<PdxOmicsFile> modelOmicsFiles = new ArrayList<>();

                        ArrayList<PdxModelCreation> modelModelCreations = new ArrayList<>();
                        for (PdxClinicalTumor tumor : modelTumors) {
                            if(modelCreations.get(tumor.getSubmitterTumorID()) != null){
                                modelModelCreations.addAll(modelCreations.get(tumor.getSubmitterTumorID()));
                            }
                        }

                        for (PdxModelCreation mc : modelModelCreations) {
                            if(qas.containsKey(mc.getModelID())){
                                modelQAs.addAll(qas.get(mc.getModelID()));
                            } else {
                                messages.append("\nNo QAs for " + mc.getModelID());
                            }
                            
                            if (modelStudies.containsKey(mc.getModelID())) {
                                modelModelStudies.addAll(modelStudies.get(mc.getModelID()));
                            
                            } else {
                                messages.append("\nNo studies for " + mc.getModelID());
                            }

                            if (omicsFiles.containsKey(mc.getModelID())) {
                                modelOmicsFiles.addAll(omicsFiles.get(mc.getModelID()));
                            } else {
                                messages.append("\nNo OMICS Files for " + mc.getModelID());
                            }

                        }
                        for (PdxModelStudy study : modelModelStudies) {
                            if (studyTreatments.containsKey(study.getModelID())) {
                                study.setTreatments(studyTreatments.get(study.getModelID()));
                            }
                        }

                        messages.append("\nPatient " + patientID + " has " + modelTumors.size() + " tumors, " + modelModelCreations.size() + " model details, " + modelQAs.size() + " qas, " + modelModelStudies.size() + " modelStudies, " +((PdxModelStudy)modelModelStudies.get(0)).getTreatments().size()+" treatments, " + modelOmicsFiles.size() + " omicsfiles.");

                        models.add(new PdxModel(this.pdtc, modelPatient, modelTumors, modelModelCreations, modelQAs, modelModelStudies, modelOmicsFiles));
                    }

                    if(messages.indexOf("FATAL")==-1){
                        ModelTerms mt = new ModelTerms();
                        String mappingFile = this.spreadSheetFileString.replace(".xlsx", ".mapping");
                        if(mappingFile.equals(this.spreadSheetFileString)){
                            System.out.println("Wrong format "+this.spreadSheetFileString);
                            System.exit(0);
                        }
                        System.out.println(messages.toString());
                        System.out.println("No fatal errors generating RDF.");
                        mt.setMappingFile(mappingFile);
                        
                        mt.findTerms(models);
                        PdxModel2Rdf p2rdf = new PdxModel2Rdf(models);
                        try {
                            FileOutputStream fos = new FileOutputStream(rdfFileString, false);
                            p2rdf.outputRDF(fos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println("FATAL errors encounterd not generating RDF");
                        System.out.println(messages.toString());
                    }
                }else{
                    messages.append("\nFATAL ERROR:There must be 8 sheets there are "+workbook.getNumberOfSheets());
                }

            }else{
                messages.append("\nFATAL ERROR:No such file:" + spreadSheetFileString);
            }
        } catch (Exception e) {
            messages.append("\nERROR: parsing "+spreadSheetFileString);
            e.printStackTrace();
        }

    }

    private HashMap<String, PdxPatient> buildPatient(ArrayList<ArrayList<String>> sheetData) {
        /*
        0 PDTC  -- this  is problematic? as the patient doesn't origniate with the PDTC as in TMZ pilot       
        1 Patient ID  -- unique per row 
        2 Sex         
        3 Age at Primary Diagnosis  -- bin to 5 years
        4 Race        
        5 Ethnicity
        6 Virology Status
        7 Consent for PDX Generation
         */
        HashMap<String, PdxPatient> patients = new HashMap<>();

        String pdtc, ptID, sex, age = "", race, ethnicity, vStatus, consent;

        for (ArrayList<String> row : sheetData) {
            
            while (row.size() < PATIENT_COLUMNS ) {
                row.add("");
            }
            
            pdtc = row.get(0);
            this.pdtc = pdtc;
            ptID = fixID("Patient: Patient ID",row.get(1));
            sex = row.get(2);
            age = row.get(3);
            race = row.get(4);
            ethnicity = row.get(5);
            vStatus = row.get(6);
            consent = row.get(7);

            patientIDs.put(ptID,"");
            
            PdxPatient.Builder builder = new PdxPatient.Builder(pdtc, ptID);

            if (sex != null) {
                if (sex.toUpperCase().startsWith("F")) {
                    builder.sex(Sex.FEMALE);
                } else if (sex.toUpperCase().startsWith("M")) {
                    builder.sex(Sex.MALE);
                }else{
                    messages.append("\nPatient: Sex '"+sex+"' can't be converted to male or female.");
                }
            }

            builder.ageAtDiagnosis(Age.getAgeForString(age));

            
            builder.race(getRace(race));
            builder.ethnicity(getEthnicity(ethnicity));
            
            //TODO:ENUM?
            builder.virologyStatus(vStatus);

            if (consent.toUpperCase().startsWith("Y")) {
                builder.consent(Consent.YES);
            } else {
                builder.consent(Consent.NO);
            }

            patients.put(ptID, builder.build());

        }

        return patients;

    }

    public HashMap<String, ArrayList<PdxPatientTreatment>> buildPatientTreatments(ArrayList<ArrayList<String>> sheetData) {
        //Patient ID	Event Index	clincal treatment setting	Regimen (Drug)
        //Clinical Response	Pathological Response	Reason Stopped	Treatment Notes

        String ptID = "";
        HashMap<String, ArrayList<PdxPatientTreatment>> treatments = new HashMap<>();
        for (ArrayList<String> row : sheetData) {
            
            while (row.size() < PATIENT_TREATMENT_COLUMNS ) {
                row.add("");
            }

            ptID = fixID("PT Treatment: PT ID",row.get(0));
            
            if(!patientIDs.containsKey(ptID)){
                messages.append("\nPatient treatment has unknown patient id ").append(ptID);
                continue;
            }
            
            PdxPatientTreatment treatment = new PdxPatientTreatment();
            treatment.setPatientID(ptID);
            treatment.setEventIndex(row.get(1));
            treatment.setClinicalTreatmentSetting(row.get(2));
            treatment.setRegimen(row.get(3));
            treatment.setResponse(ResponseToTreatment.getResponse(row.get(4)));
            // TODO what is this? not in datamodel but in spreadsheets
            // treatment.setPathologicalResponse(row.get(5));
            treatment.setReasonStopped(row.get(6));
            treatment.setTreatmentNotes(row.get(7));

            if (treatments.containsKey(ptID)) {
                treatments.get(ptID).add(treatment);
            } else {
                ArrayList<PdxPatientTreatment> list = new ArrayList<>();
                list.add(treatment);
                treatments.put(ptID, list);
            }

        }
        return treatments;
    }

    public HashMap<String, ArrayList<PdxClinicalTumor>> buildTumors(ArrayList<ArrayList<String>> sheetData) {
        //Patient ID	Tumor ID	Event Index	Collection Procedure
        //Treatment Naïve	Age at collection	Initial Diagnosis
        //Clinical Event Point	Primary Tumor Origin	Specimen Tissue	
        //Tissue Histology	Clinical Diagnostic Markers	Tumor Grade	
        //T Stage	N Stage 	M Stage	Overall Stage	Sample Type	
        //Known Metastatic Sites	Short Tandem Repeat (STR) Analysis	
        //these are wrong maybe ?List each STR marker in a separate column to fill in values	STR Evaluation
        HashMap<String, ArrayList<PdxClinicalTumor>> tumors = new HashMap<>();
        
        for (ArrayList<String> row : sheetData) {

            while(row.size() < TUMOR_COLUMNS) {
                row.add("");
            }

            String ptID = fixID("Tumor: Patient ID",row.get(0));
            String tumorID = fixID("Tumor: Tumor ID",row.get(1));
            
            tumorIDs.put(tumorID,"");
            
            if(!patientIDs.containsKey(ptID)){
                messages.append("\nTumor ").append(tumorID).append(" has unknown patient id ").append(ptID);
                continue;
            }
            
            PdxClinicalTumor tumor = new PdxClinicalTumor(ptID, tumorID);
            tumor.setEventIndex(row.get(2));
            tumor.setCollectionProcedure(row.get(3));
            tumor.setTreatmentNaive(getBoolean("Tumor: treatment naive",row.get(4)));
            tumor.setAgeAtCollection(getInteger("Tumor: age at collection ",row.get(5)).toString());
            tumor.setInitialDiagnosis(row.get(6));
            tumor.setClinicalEventPoint(row.get(7));
            tumor.setTissueOfOrigin(row.get(8));
            tumor.setSpecimenTissue(row.get(9));
            tumor.setTissueHistology(row.get(10));
            tumor.setClinicalMarkers(row.get(11));
            tumor.setTumorGrade(TumorGrade.getTumorGrade(row.get(12)));
            tumor.setTStage(row.get(13));
            tumor.setNStage(row.get(14));
            tumor.setMStage(row.get(15));
            tumor.setOverallStage(row.get(16));
            tumor.setSampleType(row.get(17));
            tumor.setMetastaticSites(row.get(18).replaceAll(",", ";"));
            tumor.setStrAnalysis(row.get(19));
            //TODO str list?? row.get(20)
            tumor.setStrEvaluation(row.get(21));

            if (tumors.containsKey(row.get(0))) {
                tumors.get(row.get(0)).add(tumor);
            } else {
                ArrayList<PdxClinicalTumor> list = new ArrayList<>();
                list.add(tumor);
                tumors.put(row.get(0), list);
            }
        }
        return tumors;
    }

    public HashMap<String, ArrayList<PdxModelCreation>> buildModelDetails(ArrayList<ArrayList<String>> sheetData) {
        /*    Tumor ID
            Model ID
            Host Strain
            Mouse Source
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
        for (ArrayList<String> row : sheetData) {

            while (row.size() < MODEL_DETAILS_COLUMNS ) {
                row.add("");
                
            }

            String tumorID = fixID("Model Creation: Tumor ID",row.get(0));
            String modelID = fixID("Model Creation: Model ID",row.get(1));
            
            if(!tumorIDs.containsKey(tumorID)){
                messages.append("Model ").append(modelID).append(" contains unknown tumor ID ").append(tumorID);
                continue;
            }
            
            modelIDs.put(modelID,"");
            
            PdxModelCreation model = new PdxModelCreation(tumorID, modelID);
            
          
            model.setMouseStrain(row.get(2).trim());

            model.setMouseSource(row.get(3) + " " + row.get(4));

            String sex = row.get(5);
            if (sex.toUpperCase().startsWith("F")) {
                model.setMouseSex(Sex.FEMALE);
            } else if (sex.toUpperCase().startsWith("M")) {
                model.setMouseSex(Sex.MALE);
            }

            model.setHumanized(getBoolean("Model Creation: isHumanized",row.get(6)));

            model.setHumanizationType(row.get(7));
            model.setEngraftmentMaterial(row.get(8));
            model.setTreatmentForEngraftment(row.get(9));
            model.setEngraftmentProcedure(row.get(10));
            model.setEngraftmentSite(row.get(11));
            model.setCryopreservedBeforeEngraftment(getBoolean("Model Creation: Cryopreserved before engraftment",row.get(12)));
            model.setModelHistology(row.get(13));
            model.setDoublingTime(getInteger("Model Creation: doubling time",row.get(14)));
            
            model.setViablyCryopresered(getBoolean("Model Creation: viably cryopreserved",row.get(15)));

            //NOT IN RDF OUTPUT
            // TODO model.setCryopreservedQuaility(row.get(16));
            model.setMetastasis(getBoolean("Model Creation: has metastasis",row.get(17)));

            model.setMetastaticSites(row.get(18));

            model.setMacroMetastasisRequiresExcision(getBoolean("Model Creation: met requires excision",row.get(19)));

            model.setSublineOfModel(row.get(20));
            model.setSublineReason(row.get(21));

            if (models.containsKey(model.getTumorID())) {
                models.get(model.getTumorID()).add(model);
            } else {
                ArrayList<PdxModelCreation> list = new ArrayList<>();
                list.add(model);
                models.put(model.getTumorID(), list);
            }

        }
        return models;
    }

    public HashMap<String, ArrayList<PdxQualityAssurance>> buildQAs(ArrayList<ArrayList<String>> sheetData) {
        /*
            Model ID
            Short Tandem Repeat (STR) Analysis
            Passage Tested
            STR Evaluation
            STR Notes
            Clinical Diagnostic Markers
            Assay Result
            Clinical Diagnostic Marker Notes
            CD45 IHC
            CD45 IHC Assay Result
            Pan Cytokeratin
            Pan Cytokeratin Assay Result
            Human-specific Cytokeratin 19
            Human Specific Cytokeratin 19 Result
            EBV Transcript Detection
            EBV Transcript Assay Result
            Mouse Pathogen Status
            Overall Evaluation
        */
        HashMap<String, ArrayList<PdxQualityAssurance>> qas = new HashMap<>();
        for (ArrayList<String> row : sheetData) {

            while (row.size() < QA_COLUMNS ) {
                row.add("");
            }
            
            String modelID = fixID("QA: Model ID",row.get(0));
            
            if(!modelIDs.containsKey(modelID)){
                messages.append("\nQA has unknown model ID ").append(modelID);
                continue;
            }
            
            PdxQualityAssurance qa = new PdxQualityAssurance(modelID);
            qa.setStrAnalysis(row.get(1));
            qa.setPassageTested(getPassage("QA: Passage tested",row.get(2)));
            qa.setStrEvaluation(row.get(3));
            qa.setStrNotes(row.get(4));
            qa.setClinicalDiagnosticMarkers(row.get(5).replaceAll(",", ";"));
            qa.setClinicalDiagnosticMarkerAssayResult(row.get(6));
            qa.setClinicalDiagnosticMarkerNotes(row.get(7));
            qa.setCd45IHC(row.get(8));
            qa.setCd45IHCAssayResult(row.get(9));
            qa.setPanCytokeratin(row.get(10));
            qa.setPanCytokeratinAssayResult(row.get(11));
            qa.setHumanSpecificCytokeratin19(row.get(12));
            /*TODO missing in RDF and model */
            //qa.setHumanSpecificCytokeratin19Result(row.get(13));
            qa.setEbvTranscriptDetection(row.get(14));
            qa.setEbvTranscriptDetectionResult(row.get(15));
            qa.setMousePathogenStatus(row.get(16));
            qa.setOverallEvaluation(row.get(17));

            if (qas.containsKey(qa.getModelID())) {
                qas.get(qa.getModelID()).add(qa);
            } else {
                ArrayList<PdxQualityAssurance> list = new ArrayList<>();
                list.add(qa);
                qas.put(qa.getModelID(), list);
            }

        }
        return qas;
    }

    public HashMap<String, ArrayList<PdxModelStudy>> buildModelStudies(ArrayList<ArrayList<String>> sheetData) {
        //Model ID	Study ID	Study Description	Passage Used	Host Strain	
        //Implantation site	Baseline Tumor Target Size	Endpoint 1	Endpoint 2	Endpoint 3


        HashMap<String, ArrayList<PdxModelStudy>> studies = new HashMap<>();
        for (ArrayList<String> row : sheetData) {

            while (row.size() < MODEL_STUDY_COLUMNS ) {
                row.add("");
            }
            String modelID = fixID("Model Study: Study ID",row.get(0));
            String modelStudyID = fixID("Model Study: Model ID",row.get(1));
           
            
            if(!modelIDs.containsKey(modelID)){
                messages.append("Model study ").append(modelStudyID).append(" has unknown model ID ").append("modelID");
                continue;
            }
            
            modelStudyIDs.put(modelStudyID,"");
            
            PdxModelStudy study = new PdxModelStudy(modelID,modelStudyID);
            study.setDescription(row.get(2));
            study.setPassage(getPassage("Model Study: passage ",row.get(3)));
            study.setHostStrain(row.get(4));
            study.setImplantationSite(row.get(5));
            study.setBaselineTumorTargetSize(row.get(6));
           

            // TODO using modelID as key won't work ??
            if (studies.containsKey(study.getModelID())) {
                studies.get(study.getModelID()).add(study);
            } else {
                ArrayList<PdxModelStudy> list = new ArrayList<>();
                list.add(study);
                studies.put(study.getModelID(), list);
            }

        }
        return studies;
    }

    public HashMap<String, ArrayList<PdxStudyTreatment>> buildStudyTreatments(ArrayList<ArrayList<String>> sheetData) {
        //Model ID	Study ID	Cohort/Treatment Arm Designation	Cohort Size
        //Drug	NSC#	Dose	Route	Dosing Schedule	Number of Cycles	Study Duration
        //Endpoint 1 Response	Endpoint 2 Response	Endpoint 3 Response


        HashMap<String, ArrayList<PdxStudyTreatment>> treatments = new HashMap<>();
        for (ArrayList<String> row : sheetData) {
            
            while (row.size() < STUDY_TREATMENT_COLUMNS ) {
                row.add("");
            }
            
            String studyID = fixID("Study Treatment: Study ID",row.get(1));
            
            if(!modelStudyIDs.containsKey(studyID)){
                messages.append("\nStudy Treatment contains uknown model study id ").append(studyID);
                continue;
            }

            PdxStudyTreatment trt = new PdxStudyTreatment(studyID);
            trt.setModelID(fixID("Study Treatment: Model ID",row.get(0)));
            trt.setCohort(row.get(2));
            trt.setCohortSize(getInteger("Study Treatment: cohort size",row.get(3)));
            trt.setDrug(row.get(4));
            trt.setDose(row.get(6));
            trt.setRoute(row.get(7));
            trt.setDosingSchedule(row.get(8));
            trt.setNumberOfCycles(getInteger("Study Treatment: number of cycles",row.get(9)));
            trt.setStudyDuration(getInteger("Study Treatment: study duration",row.get(10)));
            trt.setEndpoint1Response(ResponseToTreatment.getResponse(row.get(11)));
            trt.setEndpoint2Response(ResponseToTreatment.getResponse(row.get(12)));
            trt.setEndpoint3Response(ResponseToTreatment.getResponse(row.get(13)));

            if (treatments.containsKey(trt.getModelID())) {
                treatments.get(trt.getModelID()).add(trt);
            } else {
                ArrayList<PdxStudyTreatment> list = new ArrayList<>();
                list.add(trt);
                treatments.put(trt.getModelID(), list);
            }

        }
        return treatments;
    }

    public HashMap<String, ArrayList<PdxOmicsFile>> buildOmicsFiles(ArrayList<ArrayList<String>> sheetData) {

        //Patient ID, Model ID, Access Level, Created datetime, Data Category, 
        //Data Format, Data Type, Sample Type, 
        //Experimental Strategy, Platform, Capture Kit, Updated Datetime, 
        //Is FFPE, Paired_end, File name, File Size, Passage            
        HashMap<String, ArrayList<PdxOmicsFile>> omics = new HashMap<>();

        for (ArrayList<String> row : sheetData) {
            
            while (row.size() < OMICS_FILE_COLUMNS ) {
                row.add("");
            }
            
            PdxOmicsFile omicsFile = new PdxOmicsFile();

            omicsFile.setPatientID(fixID("OMICS: PT ID",row.get(0)));
            omicsFile.setModelID(fixID("OMICS: Model ID",row.get(1)));
            omicsFile.setAccessLevel(row.get(2));
            //TODO validate dates 
            try{
                omicsFile.setCreatedDateTime(getInteger("OMICS: Create date",row.get(3)).toString());
            }catch(Exception e){}
            omicsFile.setDataCategory(row.get(4));
            omicsFile.setDataFormat(row.get(5));
            omicsFile.setDataType(row.get(6));
            omicsFile.setSampleType(row.get(7));
            omicsFile.setExperimentalStrategy(row.get(8));
            omicsFile.setPlatform(row.get(9));
            omicsFile.setCaptureKit(row.get(10));
            try{
                omicsFile.setUpdatedDateTime(getInteger("OMICS: Update date",row.get(11)).toString());
            }catch(Exception e){}
            omicsFile.setIsFFPE(getBoolean("OMICS: isFFPE",row.get(12)));
            // paired end should be 1,2 or null
            // however sometime supplied as True (=2) False (=1)
            omicsFile.setPairedEnd(getInteger("OMICS: Paired End",row.get(13)));
            omicsFile.setFileName(row.get(14));
            
            try{
                String val = row.get(15);
                val = fixFileSize(val);
                omicsFile.setFileSize(new Long(val).toString());
            }catch(Exception e){
                messages.append("\n"+row.get(15)+" is not an integer. OMICS:Filesize should be in bytes");
            }
            
            
            omicsFile.setPassage(getPassage("OMICS: Passage ",row.get(16)));

            // don't bother if no filename is provided.
            if (row.get(14).trim().length() > 0) {
                if (omics.containsKey(omicsFile.getModelID())) {
                    omics.get(omicsFile.getModelID()).add(omicsFile);
                } else {

                    ArrayList<PdxOmicsFile> list = new ArrayList<>();
                    list.add(omicsFile);
                    omics.put(omicsFile.getModelID(), list);
                }
            }else{
                messages.append("\nSkipping row in OMICS sheet no value for file name in row "+row.toString());
                
            }
        }

        return omics;
    }
    
    // if Excel decides the ID is a number it will add ".0" to the end.
    // hopefully all IDs will start with the PDTC abbreviation but just in case ...
    // probably these messages should get logged or suppressed
    private String fixID(String what,String id){
        if(id == null){
            messages.append("\nFATAL error: requried field "+what+" is has no value");
            return null;
        }
        //messages.append("\n"+what+" "+id);
        if(id.endsWith(".0")){
            messages.append("\nFixing "+what+" "+id+" to ");
            id = id.substring(0, id.length()-2);
            messages.append("\n"+id);
        }
        return id.trim();
    }
    
    private void sheetStats(Sheet sheet) {
        int i = 1;
        messages.append("\n"+sheet.getSheetName());
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext()) {
            Row r = it.next();
            if (r.getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null) {
                messages.append("\n"+i + " " + r.getLastCellNum());
            }
            i++;
        }
    }



    private void validateCounts(Workbook workbook) {
        ArrayList<Integer> countList = new ArrayList();
        countList.add(PATIENT_COLUMNS);
        countList.add(PATIENT_TREATMENT_COLUMNS);
        countList.add(TUMOR_COLUMNS);
        countList.add(MODEL_DETAILS_COLUMNS);
        countList.add(QA_COLUMNS);
        countList.add(MODEL_STUDY_COLUMNS);
        countList.add(STUDY_TREATMENT_COLUMNS);
        countList.add(OMICS_FILE_COLUMNS);
        
        for(int i = 0; i < countList.size(); i++){
            int cellCount = getCellCount(workbook.getSheetAt(i).getRow(0));
            if (cellCount != countList.get(i)) {
                messages.append("\n FATAL Error:"+workbook.getSheetAt(i).getSheetName()+" should have "+ countList.get(i)+" columns but has "+cellCount);
            }
    }}

    
    private int getCellCount(Row row){
        int count = 0;
        Iterator<Cell> it = row.cellIterator();
        while(it.hasNext()){
            Cell c = it.next();
            if(c.getCellType()!=CellType.BLANK){
                // these are column headers and should all be strings
                if(c.getStringCellValue().trim().length()>0){
                    count++;
                }
                
            }
        }
        return count;
    }
    private ArrayList<ArrayList<String>> getSheetData(Sheet sheet) {
        return getSheetData(sheet, false);
    }

    private ArrayList<ArrayList<String>> getSheetData(Sheet sheet, boolean omics) {
        int i = 0;
        ArrayList<ArrayList<String>> sheetData = new ArrayList();

        
        Iterator<Row> it = sheet.iterator();

        //skip first row it is column headers
        it.next();
        // skip comments
        it.next();

        while (it.hasNext()) {
            i = 0;
            ArrayList<String> rowData = new ArrayList();
            Row r = it.next();
            // don't process if first cell is empty unless its the omics sheet then check the second cell as well
            if ((r.getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null)
                    || (omics && (r.getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null))) {
                while (i < r.getLastCellNum()) {
                    Cell cell = r.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
                  
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            rowData.add(cell.getNumericCellValue() + "");
                            break;
                        case STRING:
                            rowData.add(cell.getStringCellValue().trim());
                            break;
                        case BOOLEAN:
                            if (cell.getBooleanCellValue()) {
                                rowData.add("TRUE");
                            } else {
                                rowData.add("FALSE");
                            }
                            break;
                        case BLANK:
                            rowData.add("");
                            break;
                        default:
                            // this is a log message the user can't do anything...
                            messages.append("\n"+cell.getCellType() + " not managed skipping");

                    }
                    i++;
                }

            }
            if (rowNotEmpty(rowData)) {
                sheetData.add(rowData);
            }
        }
       
        return sheetData;
    }

    private boolean rowNotEmpty(ArrayList<String> row) {
        for (String s : row) {
            if (s.trim().length() > 0) {
                return true;
            }
        }
        return false;
    }

    private void printSheetData(String name, ArrayList<ArrayList<String>> sheetData) {
        messages.append("\n"+name);
        for (ArrayList<String> row : sheetData) {
            for (String data : row) {
                messages.append("\n"+data + "\t");
            }
            messages.append("\n");
        }
    }

    private Integer getPassage(String where, String p) {
        Integer passage = null;
        try {
            passage = new Integer(p.toLowerCase().replace("p", "").trim());
        } catch (Exception e) {
            messages.append("\n"+where+" Can not convert passage '" + p + "' to integer. Defaulting to null");

        }
        return passage;
    }
    
    private Integer getInteger(String where,String i){
        try{
            return new Integer(new Double(i).intValue());
            
        }catch(Exception e){
            messages.append("\nCan not convert "+i+" to an integer value for "+where+". Defaulting to null");
        }
        return null;
    }

    private Boolean getBoolean(String where,String b) {
        
        if(b== null) return null;
        
        b = b.toLowerCase();
        switch (b) {
            case "yes":
                return true;
            case "true":
                return true;
            case "no":
                return false;
            case "false":
                return false;
            default:
                messages.append("\nCan't convert '"+b+"' to a boolean for "+where);
                return null;
        }
    }
    
    private Ethnicity getEthnicity(String eth){
        
        Ethnicity e = Ethnicity.UNKNOWN;
        
        if(eth != null){
            eth = eth.toLowerCase();
            if(eth.startsWith("not ")){
                e = Ethnicity.NOTLATINO;
            }else if(eth.contains("hispanic") || eth.contains("latino")){
                e = Ethnicity.LATINO;
            }else{
                messages.append("\n Unable to convert "+eth+" into standard ethnicity, using 'Unknown'");
            }
        }else{
            messages.append("\n No ethnicity specified, using 'Unknown'");
        }
        return e;
    }
    
    private Race getRace(String race){
        Race r = Race.UNKNOWN;
        
        if(race != null){
            race = race.toLowerCase();
            if(race.contains("white")){
                r = Race.WHITE;
            }else if(race.contains("asian")){
                r = Race.ASIAN;
                
            }else if(race.contains("black")){
                r = Race.BAA;
                
            }else if(race.contains("african")){
                r = Race.BAA;
                
            }else if(race.contains("indian")){
                r = Race.AMINALNA;
                
            }else if(race.contains("alaska")){
                r = Race.AMINALNA;
                
            }else if(race.contains("hawaiian")){
                r = Race.NHOPI;
                
            }else if(race.contains("pacific")){
                r = Race.NHOPI;
                
            }else{
                messages.append("\n Unable to convert "+race+" into standard race using 'Unknown'");
            }
        }else{
            messages.append("\n No race specified, using 'Unknown'");
        }
        
        
        return r;
    }

    
    public String fixFileSize(String sizeStr){
        Double size = null;
        String[] parts = sizeStr.split(" ");
        if(parts[1].contains("G")){
            size  = new Double(parts[0])* 1073741824;
        }
        if(parts[1].contains("K")){
            size  = new Double(parts[0])* 1024;
        }
        size =Math.ceil(size);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        return df.format(size);
    }
    
}
