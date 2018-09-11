package org.jax.pdxintegrator.simulate;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.github.phenomics.ontolib.ontology.data.ImmutableTermId;
import com.github.phenomics.ontolib.ontology.data.TermId;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.io.DrugBankEntry;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.modelcreation.MouseTreatmentForEngraftment;
import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.modelcreation.TumorPrepMethod;
import org.jax.pdxintegrator.model.modelstudy.PdxModelStudy;
import org.jax.pdxintegrator.model.modelstudy.PdxStudyTreatment;
import org.jax.pdxintegrator.model.omicsfile.PdxOmicsFile;
import org.jax.pdxintegrator.model.patient.*;
import org.jax.pdxintegrator.model.qualityassurance.ModelCharacterization;
import org.jax.pdxintegrator.model.qualityassurance.PdxQualityAssurance;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;
import org.jax.pdxintegrator.ncit.neoplasm.NcitTerm;
import org.jax.pdxintegrator.uberon.UberonTerm;

/**
 * This class is intended to illustrate how to instantiate the PdxPatient module with data.
 * We use a very simply way of generating random data, beware it is not intended to be used
 * for any analysis--for demonstration only!
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxModelSimulator {
    private static final Logger logger = LogManager.getLogger();
    private final String patientID;
    private final Sex sex;
    private final Age age;
    private final TermId diagnosis;
    private final Consent consent;
    private Random random=new Random();


    private final TermId metastasis = ImmutableTermId.constructWithPrefix("NCIT:C19151");
    private final TermId primaryNeoplasm = ImmutableTermId.constructWithPrefix("NCIT:C8509");
    private final TermId recurrence = ImmutableTermId.constructWithPrefix("NCIT:C3352");



    /** List of NCIT Terms for neoplasms. Our simulation will choose one of these diagnoses at random. */
    private final List<NcitTerm> neoplasmTerms;
    /** List of terms like "Grade 2" */
    private final List<NcitTerm> gradeTerms;
    private final List<NcitTerm> stageTerms;

    private final List<UberonTerm> uberonTerms;

    private List<DrugBankEntry> drugbankentrylist;

    private PdxModel pdxmodel=null;

    public PdxModelSimulator(int id, List<NcitTerm> neoplasms, List<NcitTerm> grades, List<NcitTerm> stages, List<UberonTerm> uberons) {
        this.patientID=String.format("PAT-%d",id);
        logger.trace(String.format("Simulating patient %s (will choose random diagnosis from %d NCIT terms",patientID,neoplasms.size()));
        this.sex=getRandomSex();
        this.age=getRandomAge();
        this.consent=getRandomConsent();
        
        this.neoplasmTerms=neoplasms;
        this.gradeTerms=grades;
        this.stageTerms=stages;
        this.uberonTerms=uberons;
        Objects.requireNonNull(neoplasms);
        Objects.requireNonNull(stageTerms);
        Objects.requireNonNull(uberons);

        drugbankentrylist = DrugBankEntry.parseDrugBankTabFile();

        diagnosis=getRandomNCITTermId();

        PdxPatient patient  = buildPatient();
        // really should simulate 1-N
        // simulate random # of tumors then for each random # of modles then for each random # of studies etc
        ArrayList<PdxClinicalTumor> clinicalTumors = buildClinicalTumors(patient);
        ArrayList<PdxModelCreation> modelCreations = new ArrayList();
        for(PdxClinicalTumor tumor : clinicalTumors){
            modelCreations.addAll(buildModelCreations(tumor.getSubmitterTumorID()));
        
        }
        
        ArrayList<PdxOmicsFile> omicsFiles = new ArrayList();
        
        ArrayList<PdxQualityAssurance> qas = new ArrayList();
        ArrayList<PdxModelStudy> modelStudies = new ArrayList();
        for(PdxModelCreation modelCreation : modelCreations){
            modelStudies.addAll(buildModelStudies(modelCreation.getModelID()));
            qas.add(buildQualityAssuranceModule(modelCreation.getModelID()));
            omicsFiles.add(buildOmicsFile(modelCreation.getModelID(),null));
        }
        
        omicsFiles.add(buildOmicsFile(null,patientID));
        // same for other categories
        
        this.pdxmodel = new PdxModel("PDTCName",patient,clinicalTumors,modelCreations,qas, modelStudies,omicsFiles);
    }


    public PdxModel getPdxmodel() {
        return pdxmodel;
    }

    private static int omicsCount = 1;
    private PdxOmicsFile buildOmicsFile(String modelID, String patientID){
        PdxOmicsFile omicsFile = new PdxOmicsFile();
        omicsFile.setAccessLevel("access level value");
        omicsFile.setCaptureKit("capture kit value");
        omicsFile.setCreatedDateTime("Tuesday at noon");
        omicsFile.setDataCategory("data category value");
        omicsFile.setDataFormat("data format value");
        omicsFile.setDataType("data type value");
        omicsFile.setExperimentalStrategy("exp strategy");
        omicsFile.setFileName("simulatedOmicsFile"+omicsCount++);
        omicsFile.setFileSize("34K");
        omicsFile.setIsFFPE(true);
        omicsFile.setIsPairedEnd(true);
        omicsFile.setModelID(modelID);
        omicsFile.setPatientID(patientID);
        omicsFile.setPassage("P1");
        omicsFile.setPlatform("whole exome");
        omicsFile.setSampleType("tumor");
        omicsFile.setUpdatedDateTime("update date");
        
        return omicsFile;
    }



    private ArrayList<PdxModelCreation> buildModelCreations(String tumorID) {
        /*  Needs these fields
            private String tumorID;
            private String modelID;
            private int passage;
            private String mouseStrain;
            private String mouseSource;
            private String mouseSex;
            private boolean humanized;
            private String humanizationType;
            private String engraftmnetProcedure;
            private String engraftmnetSite;
            private String treatmentForEngraftment;
            private boolean tissueCryoPreserved;
            private String engraftmentRate;
            private String engraftmentTime;
        
        */
        ArrayList<PdxModelCreation> modelCreations = new ArrayList();
        int count = random.nextInt(3)+1;
        while(count>0){
            String modelID=String.format("PDXModel-%s",tumorID)+"-"+count;
            TumorPrepMethod tumorprepmethod=getRandomTumorPrepMethod();
            MouseTreatmentForEngraftment mouseRx = getRandomMouseTreatmentForEngraftment();
            double engraftment = getRandomEngraftmentPercent();
            int engraftmentDays=getRandomEngraftmentTime();

            PdxModelCreation modelCreation =  new PdxModelCreation(tumorID,modelID);
            modelCreation.setMouseStrain("NOD.Cg-Prkdc<scid> Il2rg<tm1Wj>l/SzJ");
            modelCreation.setMouseSource("JAX");
            modelCreation.setMouseSex(random.nextInt(1) > 0 ? "Male":"Female");
            modelCreation.setEngraftmentProcedure(tumorprepmethod.getTumorPrepMethodString());
            modelCreation.setTreatmentForEngraftment(mouseRx.getMouseTreatmentForEngraftmentString());
            modelCreation.setEngraftmentRate(engraftment+"");
            modelCreation.setEngraftmentTime(engraftmentDays+"");
            if(random.nextInt(1)>0){
                modelCreation.setHumanized(true);
                modelCreation.setHumanizationType("CD34+hematopoietic stem cell-engrafted");
            }
            modelCreations.add(modelCreation);
            count--;
        }
        return modelCreations;
    }


    private ArrayList<PdxClinicalTumor> buildClinicalTumors(PdxPatient patient ) {
        ArrayList<PdxClinicalTumor> tumors = new ArrayList();
        int count = random.nextInt(2)+1;
        while(count>0){
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
            String tumorID=String.format("TUMOR-%s",patient.getSubmitterPatientID())+"-"+count;
            TermId uberon = getUberonId(diagnosis);
            TermId category = getRandomTumorCategory();
            
            TermId grade = getRandomTumorGrade();
            TermId stage = getRandomStage();
            PdxClinicalTumor tumor = new PdxClinicalTumor(patient.getSubmitterPatientID(),tumorID);
            tumor.setClincalEventPointTerm(category);
            tumor.setClinicalMarkers("BRCA+");
            tumor.setMStage(("MStagePlaceholder"));
            tumor.setNStage("NstagePlaceholder");
            tumor.setTStage("TstagePlaceholder");
            tumor.setOverallStage(stage.getId());
            tumor.setTumorGrade(grade.getId());
            tumor.setPatientID(patientID);
            tumor.setSampleType("sampleType");
            tumor.setTissueOfOrigin(uberon.toString());
            tumor.setTissueOfOriginTerm(uberon);
            tumor.setTissueHistology("histologyPlaceHolder");
            tumor.setTissueHistologyTerm(diagnosis);
            tumor.setSpecimenTissue(uberon.toString());
            tumor.setSpecimenTissueTerm(uberon);
            tumors.add(tumor);
            count--;
            
        }
        return tumors;
    }

    private PdxQualityAssurance buildQualityAssuranceModule(String modelID) {
       
        
        PdxQualityAssurance qa = new PdxQualityAssurance(modelID);
        qa.setAnimalHealthStatus("animalHealthStatus");
        qa.setPassageTested(0);
        qa.setQcMethod("QCMethod");
        
        qa.setQcResult("QC result");
        return qa;
    }
    
    private ArrayList<PdxModelStudy> buildModelStudies(String modelID){
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
        int count = random.nextInt(3)+1;
        while(count>0){
            PdxModelStudy.Builder builder = new PdxModelStudy.Builder(modelID, "Study-"+modelID+"-"+count);
            
            builder.treatments(buildStudyTreatments("Study-"+modelID+"-"+count));
            studies.add(builder.build());
            count--;
            }
        
        return studies;
    }



    private PdxPatient buildPatient() {
        
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
        ArrayList<PdxPatientTreatment> treatments = buildPatientTreatments();
        String virologyStatus = "HIV-/HPV+";
        PdxPatient.Builder builder= new PdxPatient.Builder("PDTCName",patientID);
        builder.virologyStatus(virologyStatus);
        builder.treatments(treatments);
        
        builder.sex(sex);
        builder.consent(consent);
       
        builder.ageAtDiagnosis(age);
        builder.ethnicity("hispanic");
        builder.race("caucasian");
                
        
        return builder.build();
    }

    
    private ArrayList<PdxPatientTreatment> buildPatientTreatments(){
        ArrayList<PdxPatientTreatment> treatments = new ArrayList();
        int numTreatments = random.nextInt(4);
        while(numTreatments > 0){
            PdxPatientTreatment treatment = new PdxPatientTreatment();
            treatment.setEventIndex(numTreatments+"");
            
            treatment.setRegimen(this.getRandomCancerDrug());
            treatment.setReasonStopped("did not tolerate");
            treatment.setResponse(this.getRandomResponseToStandardOfCare());
            treatments.add(treatment);
            numTreatments--;
        }
        return treatments;
    }
    
    
    private ArrayList<PdxStudyTreatment> buildStudyTreatments(String studyID){
        ArrayList<PdxStudyTreatment> treatments = new ArrayList();
        int numTreatments = random.nextInt(4);
         while(numTreatments > 0){
            PdxStudyTreatment treatment = new PdxStudyTreatment(studyID);
            treatment.setDose("1");
          
         
            treatment.setRoute("in chow");
            treatment.setDrug(this.getRandomCancerDrug());
            
            treatments.add(treatment);
            
            numTreatments--;
        }
        return treatments;
    }

    /**
     * For now we return a random uberon term. Better would be to return the matching tissue for the NCIT diagnosis
     * @param ncitTerm The term of the cancer for which we are retrieving the corresponding UBERON anatomy id (TODO -- add logic!)
     * @return termid of UBERON anatomy term corresponding to the cancer diagnosis in ncitTerm
     */
    private TermId getUberonId(TermId ncitTerm) {
        if (uberonTerms==null || uberonTerms.isEmpty()) {
            logger.error("Uberon terms not initialized...terminating simulations");
            System.exit(1);
        }
        int randomIndex=random.nextInt(this.uberonTerms.size());
        UberonTerm term = uberonTerms.get(randomIndex);
        return term.getTermId();
    }


    private TermId  getRandomTumorGrade() {
        int randomindex = random.nextInt(this.gradeTerms.size());
        NcitTerm grade = gradeTerms.get(randomindex);
        return grade.getTermId();
    }

    private TermId  getRandomStage() {
        int randomindex = random.nextInt(this.stageTerms.size());
        NcitTerm stage = stageTerms.get(randomindex);
        return stage.getTermId();
    }

    private double getRandomEngraftmentPercent() {
        return 100*new Random().nextDouble();
    }

    private int getRandomEngraftmentTime() {
        int leftLimit = 3;
        int rightLimit = 30;
        int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
        return generatedInteger;
    }


    private TermId getRandomTumorCategory() {
        int randomIndex = random.nextInt(3);
        if (randomIndex==0) { return metastasis; }
        else if (randomIndex==1) { return primaryNeoplasm; }
        else return recurrence;
    }


    /**
     * @return a TermId that represents an NCIT neoplasm term.
     */
    private TermId getRandomNCITTermId() {

        logger.trace("getting random number for size="+neoplasmTerms.size());
        int randomIndex=random.nextInt(this.neoplasmTerms.size());
        NcitTerm term = neoplasmTerms.get(randomIndex);
        return term.getTermId();
    }


    /**
     * @return a random Sex
     */
    private Sex getRandomSex() {
        Sex [] values = Sex.values();
        return values[random.nextInt(values.length)];
    }


    private Age getRandomAge() {
        Age[] values = Age.values();
        return values[random.nextInt(values.length)];
    }

    private Consent getRandomConsent() {
        Consent[] values = Consent.values();
        return values[random.nextInt(values.length)];
    }

    private EthnicityRace getRandomEthnicity() {
        EthnicityRace[] values = EthnicityRace.values();
        return values[random.nextInt(values.length)];
    }

    private TumorPrepMethod  getRandomTumorPrepMethod() {
        TumorPrepMethod[] vals = TumorPrepMethod.values();
        int randomindex = random.nextInt(vals.length);
        TumorPrepMethod stage = vals[randomindex];
        return stage;
    }

    private MouseTreatmentForEngraftment getRandomMouseTreatmentForEngraftment() {
        MouseTreatmentForEngraftment[] vals = MouseTreatmentForEngraftment.values();
        int randomindex = random.nextInt(vals.length);
        return vals[randomindex];
    }

    private  ModelCharacterization getRandomModelCharacterization() {
        ModelCharacterization[] vals = ModelCharacterization.values();
        int randomindex = random.nextInt(vals.length);
        return vals[randomindex];
    }

    private  ResponseToTreatment getRandomResponseToStandardOfCare() {
        ResponseToTreatment[] vals = ResponseToTreatment.values();
        int randomindex = random.nextInt(vals.length);
        return vals[randomindex];
    }

    private boolean getRandomBoolean() {
        double d = new Random().nextDouble();
        return d>0.5D;
    }

    private String getRandomCancerDrug() {
        int r = new Random().nextInt(10);
        int i=0;
        String current=null;
        for (DrugBankEntry entry : drugbankentrylist) {
            if (entry.getCategory().startsWith("ANTINEOPLASTIC")) {
                current=String.format("%s[%s;%s]",entry.getName(),entry.getDbID(),entry.getCas());
                i++;
                if (i>=r) return current;
            }
        }
        return current; // should never happen, but whatever
    }

}
