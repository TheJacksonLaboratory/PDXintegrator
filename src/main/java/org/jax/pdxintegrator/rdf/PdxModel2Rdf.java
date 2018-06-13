package org.jax.pdxintegrator.rdf;

import com.github.phenomics.ontolib.ontology.data.ImmutableTermId;
import com.github.phenomics.ontolib.ontology.data.TermId;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.*;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.patient.Consent;
import org.jax.pdxintegrator.model.patient.Sex;
import org.jax.pdxintegrator.model.patient.PdxPatient;
import org.jax.pdxintegrator.model.qualityassurance.PdxQualityAssurance;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.shared.BadURIException;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.jax.pdxintegrator.model.modelstudy.PdxModelStudy;
import org.jax.pdxintegrator.model.modelstudy.PdxStudyTreatment;
import org.jax.pdxintegrator.model.omicsfile.PdxOmicsFile;
import org.jax.pdxintegrator.model.patient.PdxPatientTreatment;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.COMPLETE_RESPONSE;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.NOT_ASSESSED;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.PARTIAL_RESPONSE;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.PROGRESSIVE_DISEASE;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.STABLE_DISEASE;

/**
 * This class coordinates the transformation of one or more
 * {@link org.jax.pdxintegrator.model.PdxModel} objects as an RDF graph.
 *
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxModel2Rdf {

    private final List<PdxModel> pdxmodels;

    /**
     * "Root" of the entire RDF graph.
     */
    //private Model rdfModel = ModelFactory.createDefaultModel();
    private OntModel rdfModel;

    public final TermId male = ImmutableTermId.constructWithPrefix("NCIT:C20197");
    public final TermId female = ImmutableTermId.constructWithPrefix("NCIT:C16576");

    // RDF properties needed throughout the model
    private Property hasPatientIdProperty = null;
    private Property hasSubmitterTumorIdProperty = null;
    private Property hasDiagnosisProperty = null;
    private Property hasTumorProperty = null;
    private Property hasTissueOfOriginProperty = null;
    private Property hasTumorCategoryProperty = null;
    private Property hasTumorHistologyProperty = null;
    private Property hasTumorGradeProperty = null;
    private Property hasOverallStageProperty = null;
    private Property hasTStageProperty = null;
    private Property hasMStageProperty = null;
    private Property hasNStageProperty = null;
    private Property hasPdxModelProperty = null;
    private Property hasStrainProperty = null;
    private Property mouseSourceProperty = null;
    private Property strainHumanizedProperty = null;
    private Property humanizationTypeProperty = null;
    private Property tumorPreparation = null;
    private Property mouseTreatmentForEngraftment = null;
    private Property engraftmentPercentProperty = null;
    private Property engraftTimeInDaysProperty = null;
    private Property hasTumorCharacterizationProperty = null;
    private Property tumorNotEbvNotMouseProperty = null;
    private Property pdxTumorResponseProperty = null;
    private Property animalHealthStatusSatisfactoryProperty = null;
    private Property passageQaPerformedProperty = null;
    private Property currentTreatmentDrug = null;
    private Property ageBinLowerRange = null;
    private Property ageBinUpperRange = null;
    /**
     * This property specifies the NCIT diagnosis of a patient's diagnosis.
     */
    private Property cancerDiagnosis = null;
    /**
     * This property specifies the sex. ToDO decide on whether to use NCIT for
     * this.
     */
    private Property sexProperty = null;
    /**
     * This property specifies the consent given by the patient. TODO enough?
     */
    private Property consentProperty = null;
    /**
     * This property specifies the population group of the patient.
     */
    private Property ethnicityProperty = null;

    private Property pdxStudyTreatmentProperty = null;
    private Property pdxDoublingLagTimeProperty = null;
    private Property pdxStudyHasMetastasisProperty = null;
    private Property pdxStudyMetastasisLocationProperty = null;
    private Property pdxStudyMetastasisPassageProperty = null;
    private Property pdxStudyTumorOmicsProperty = null;
    private Property fromPatientProperty = null;
    private Property hasStudyProperty = null;
    private Property hasStudyTreatmentProperty = null;
    private Property hasPatientTreatmentProperty = null;

    private Property patientTreatmentIndexProperty = null;
    private Property patientTreatmentPostSampleProperty = null;
    private Property patientTreatmentRegimen = null;
    private Property patientTreatmentResponse = null;
    private Property patientTreatmentReasonStopped = null;

    private Property studyTreatmentDrug = null;
    private Property studyTreatmentDose = null;
    private Property studyTreatmentUnits = null;
    private Property studyTreatmentRoute = null;
    private Property studyTreatmentFrequency = null;
    
    
    // for omics files
    private Property hasOmicsFile;
    private Property hasAccessLevel;
    private Property hasCreatedDateTime;
    private Property hasDataCategory;	
    private Property hasDataFormat;
    private Property hasDataType;
    // this should exist somewher else also....
    private Property hasSampleType;	
    private Property hasExperimentalStrategy;
    private Property hasFileSize;
    private Property hasPlatform;	
    private Property hasCaptureKit;	
    private Property hasUpdatedDateTime;
    private Property hasFFPEPairedEnd;	
    private Property hasFileName;
    private Property hasPatientAssociation;
    private Property hasModelAssociation;

    private Resource maleSex = null;
    private Resource femaleSex = null;
    private Resource noConsent = null;
    private Resource yesConsent = null;
    private Resource TRUE_RESOURCE = null;
    private Resource FALSE_RESOURCE = null;
    private Resource academicConsent = null;

    /**
     * Tumor preparation types
     */
    private Resource tumorPrepSolid, tumorPrepSuspension, tumorPrepAscites;
    /**
     * Mouse treatment types
     */
    private Resource mouseRxGCSF, mouseRxEstrogen;
    /**
     * PDX Tumor response types
     */
    private Resource notAssessed, completeResponse, partialResponse, stableDisease, progressiveDisease;
    /**
     * Tumor characterization types
     */
    private Resource IHC, histology;

    // rather than global variables, get resouce from the model by ID
//    private Resource thisPatient = null;
//    private Resource thisPdxModelCreation = null;
//    private Resource thisTumorSample = null;
//    private Resource thisModelStudy = null;
    private static final String PDXNET_NAMESPACE = "http://pdxnetwork.org/pdxmodel";
    private final static String NCIT_NAMESPACE = "http://purl.obolibrary.org/obo/NCIT_";
    private final static String UBERON_NAMESPACE = "http://purl.obolibrary.org/obo/UBERON_";

    /// these are the classes needed for the Seven Bridges model
    private OntClass pdxPatient;
    private OntClass pdxModelStudy;
    private OntClass pdxClinicalTumor;
    private OntClass pdxQualityAssurance;
    private OntClass pdxModelCreation;
    private OntClass pdxPatientTreatment;
    private OntClass pdxStudyTreatment;
    private OntClass pdxOmicsFile;
    
    private OntClass pdxDiagnosis;
    private OntClass pdxSex;
    private OntClass pdxTreatmentResponse;
    private OntClass pdxTumorSampleType;
    private OntClass pdxModelCharacterization;

    private OntClass pdxPatientConsent;
    private OntClass pdxTreatmentForEngraftment;
    private OntClass pdxPassageQaPerformed;
    private OntClass pdxTissueOfOrigin;
    private OntClass pdxTumorCategory;
    private OntClass pdxTumorGrade;
    private OntClass pdxTumorStage;
    private OntClass pdxTumorHistology;
    private OntClass pdxBoolean;
    
   

    public PdxModel2Rdf(List<PdxModel> modelList) {
        this.pdxmodels = modelList;
    }

    public void outputRDF(OutputStream out) {
        initializeModelFramework();
        specifyPrefixes();
        createEntities();
        for (PdxModel pdxmod : pdxmodels) {
            outputPdxModel(pdxmod);
        }
        System.out.println("# -- PATIENT -- #");
        rdfModel.write(System.out, "Turtle");
        //rdfModel.write(System.out,"RDF/XML");//// now write the model in XML form to a file
        //rdfModel.write(System.out, "RDF/XML-ABBREV");
        //rdfModel.write(System.out, "N-TRIPLES");
        try{
            rdfModel.write(out);//,"Turtle");
        }catch(BadURIException bue){
            bue.printStackTrace();
        }

    }

    private void initializeModelFramework() {
        this.rdfModel = ModelFactory.createOntologyModel();

        String pdxMiPatientURI = String.format("%s%s", PDXNET_NAMESPACE, "#Patient");
        this.pdxPatient = rdfModel.createClass(pdxMiPatientURI);
        this.pdxPatient.addProperty(RDFS.label, "Patient");

        String pdxMiPatientTreatmentURI = String.format("%s%s", PDXNET_NAMESPACE, "#PatientTreatment");
        this.pdxPatientTreatment = rdfModel.createClass(pdxMiPatientTreatmentURI);
        this.pdxPatientTreatment.addProperty(RDFS.label, "Patient Treatment");

        String pdxMiDiagnosisURI = String.format("%s%s", PDXNET_NAMESPACE, "#Diagnosis");
        this.pdxDiagnosis = rdfModel.createClass(pdxMiDiagnosisURI);
        this.pdxDiagnosis.addProperty(RDFS.label, "Diagnosis");

        String pdxMiSexURI = String.format("%s%s", PDXNET_NAMESPACE, "#Sex");
        this.pdxSex = rdfModel.createClass(pdxMiSexURI);
        this.pdxSex.addProperty(RDFS.label, "Sex");

        String pdxMiModelStudyURI = String.format("%s%s", PDXNET_NAMESPACE, "#ModelStudy");
        this.pdxModelStudy = rdfModel.createClass(pdxMiModelStudyURI);
        this.pdxModelStudy.addProperty(RDFS.label, "Model Study");

        String pdxMiStudyTreatmentURI = String.format("%s%s", PDXNET_NAMESPACE, "#StudyTreatment");
        this.pdxStudyTreatment = rdfModel.createClass(pdxMiStudyTreatmentURI);
        this.pdxStudyTreatment.addProperty(RDFS.label, "Study Treatment");

        String pdxMiClinicalTumorURI = String.format("%s%s", PDXNET_NAMESPACE, "#ClinicalTumor");
        this.pdxClinicalTumor = rdfModel.createClass(pdxMiClinicalTumorURI);
        this.pdxClinicalTumor.addProperty(RDFS.label, "Clinical Tumor");

        String pdxMiQualityAssuranceURI = String.format("%s%s", PDXNET_NAMESPACE, "#QualityAssurance");
        this.pdxQualityAssurance = rdfModel.createClass(pdxMiQualityAssuranceURI);
        this.pdxQualityAssurance.addProperty(RDFS.label, "Quality Assurance");

        String pdxMiModelCreationURI = String.format("%s%s", PDXNET_NAMESPACE, "#ModelCreation");
        this.pdxModelCreation = rdfModel.createClass(pdxMiModelCreationURI);
        this.pdxModelCreation.addProperty(RDFS.label, "Model Creation");
        
        String pdxOmicsFileURI = String.format("%s%s", PDXNET_NAMESPACE, "#OmicsFile");
        this.pdxOmicsFile = rdfModel.createClass(pdxOmicsFileURI);
        this.pdxOmicsFile.addProperty(RDFS.label, "Omics File");

        String pdxMiTreatmentResponseURI = String.format("%s%s", PDXNET_NAMESPACE, "#TreatmentResponse");
        this.pdxTreatmentResponse = rdfModel.createClass(pdxMiTreatmentResponseURI);
        this.pdxTreatmentResponse.addProperty(RDFS.label, "Treatment Response");

        String pdxMiTumorSampleTypeURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorSampleType");
        this.pdxTumorSampleType = rdfModel.createClass(pdxMiTumorSampleTypeURI);
        this.pdxTumorSampleType.addProperty(RDFS.label, "Tumor Sample Type");

        String pdxMiModelCharacterizationURI = String.format("%s%s", PDXNET_NAMESPACE, "#ModelCharacterization");
        this.pdxModelCharacterization = rdfModel.createClass(pdxMiModelCharacterizationURI);
        this.pdxModelCharacterization.addProperty(RDFS.label, "Model Characterization");

        String pdxMiPatientConsentURI = String.format("%s%s", PDXNET_NAMESPACE, "#PatientConsent");
        this.pdxPatientConsent = rdfModel.createClass(pdxMiPatientConsentURI);
        this.pdxPatientConsent.addProperty(RDFS.label, "Patient Consent");

        String pdxMiTreatmentForEngraftmentURI = String.format("%s%s", PDXNET_NAMESPACE, "#TreatmentForEngraftment");
        this.pdxTreatmentForEngraftment = rdfModel.createClass(pdxMiTreatmentForEngraftmentURI);
        this.pdxTreatmentForEngraftment.addProperty(RDFS.label, "Mouse treatment for engraftment");

        String pdxMiPassageQaPerformedURI = String.format("%s%s", PDXNET_NAMESPACE, "#PassageQaPerformed");
        this.pdxPassageQaPerformed = rdfModel.createClass(pdxMiPassageQaPerformedURI);
        this.pdxPassageQaPerformed.addProperty(RDFS.label, "Passage QA performed");

        String pdxMiTissueOfOriginURI = String.format("%s%s", PDXNET_NAMESPACE, "#TissueOfOrigin");
        this.pdxTissueOfOrigin = rdfModel.createClass(pdxMiTissueOfOriginURI);
        this.pdxTissueOfOrigin.addProperty(RDFS.label, "Tissue of origin");

        String pdxMiTumorCategoryURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorCategory");
        this.pdxTumorCategory = rdfModel.createClass(pdxMiTumorCategoryURI);
        this.pdxTumorCategory.addProperty(RDFS.label, "Tumor category");

        String pdxMiTumorGradeURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorGrade");
        this.pdxTumorGrade = rdfModel.createClass(pdxMiTumorGradeURI);
        this.pdxTumorGrade.addProperty(RDFS.label, "Tumor grade");

        String pdxMiTumorStageURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorStage");
        this.pdxTumorStage = rdfModel.createClass(pdxMiTumorStageURI);
        this.pdxTumorStage.addProperty(RDFS.label, "Tumor stage");

        String pdxMiTumorHistologyURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorHistology");
        this.pdxTumorHistology = rdfModel.createClass(pdxMiTumorHistologyURI);
        this.pdxTumorHistology.addProperty(RDFS.label, "Tumor histology");

        String pdxMiBooleanURI = String.format("%s%s", PDXNET_NAMESPACE, "#Boolean");
        this.pdxBoolean = rdfModel.createClass(pdxMiBooleanURI);
        this.pdxBoolean.addProperty(RDFS.label, "Boolean");
    }

    private void outputPdxModel(PdxModel pdxModel) {

        // Clinincal/Patient Module and PT Treatments
        outputPatientRDF(pdxModel);
        // Clinical/Tumor Module
        outputTumorRDF(pdxModel.getClinicalTumor());
        // Model Creation Module
        outputModelCreationRdf(pdxModel);
        // Quality Assurance Module
        outputQualityAssuranceRdf(pdxModel);
        // Model Study and Study Treatments
        outputModelStudyRDF(pdxModel);
        
        outputOmicsFileRDF(pdxModel);
    }

    /**
     * Todo add a suffix depending on the total number of diagnosis this patient
     * has. For now just _01.
     */
    private void outputPatientRDF(PdxModel pdxmodel) {
        PdxPatient patient = pdxmodel.getPatient();

        String diagnosisTerm = patient.getDiagnosisTerm().getId();
        String diagnosisURI = String.format("%s%s", NCIT_NAMESPACE, diagnosisTerm);
        String patientURI = String.format("%s/%s", PDXNET_NAMESPACE, patient.getSubmitterPatientID());

        //this.thisTumorSample = rdfModel.createResource(String.format("%s%s", PDXNET_NAMESPACE, pdxmodel.getClinicalTumor().getSubmitterTumorID()));
        Resource diagnosisResource = rdfModel.createResource(diagnosisURI);
        Resource sex = patient.getSex().equals(Sex.FEMALE) ? femaleSex : maleSex;
        Resource consent = patient.getConsent().equals(Consent.YES) ? yesConsent
                : patient.getConsent().equals(Consent.NO) ? noConsent : academicConsent;

        consent.addProperty(RDF.type, this.pdxPatientConsent);

        diagnosisResource.addProperty(RDF.type, this.pdxDiagnosis);

        Resource patientResource
                = rdfModel.createResource(patientURI)
                .addProperty(RDFS.label, patient.getSubmitterPatientID())
                .addProperty(RDF.type, this.pdxPatient)
                .addProperty(hasPatientIdProperty, patient.getSubmitterPatientID())
                .addProperty(hasDiagnosisProperty, diagnosisResource)
                // do this when we create the tumorSample
                //.addProperty(hasTumorProperty, thisTumorSample)
                .addProperty(sexProperty, sex)
                .addProperty(consentProperty, consent)
                .addProperty(ethnicityProperty, patient.getEthnicity());

        patientResource.addProperty(ageBinLowerRange,
                ResourceFactory.createTypedLiteral(String.valueOf(patient.getAgeAtDiagnosis().getLower()),
                        XSDDatatype.XSDinteger));
        patientResource.addProperty(ageBinUpperRange,
                ResourceFactory.createTypedLiteral(String.valueOf(patient.getAgeAtDiagnosis().getUpper()),
                        XSDDatatype.XSDinteger));

        // start working on nexted PT treatments
        // need to define RFD properties first
        int index = 0;
        for (PdxPatientTreatment ptTreatment : patient.getPatientTreatments()) {
            index++;
                // is this sufficent for an Id?
                // private boolean postSample;
                // private String regimen;
                // private ResponseToTreatment response;
                //private String reasonStopped;
                Resource treatment = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ patient.getSubmitterPatientID() + "/treatment" + index);
                treatment.addProperty(patientTreatmentRegimen, ptTreatment.getRegimen());
                treatment.addProperty(this.patientTreatmentPostSampleProperty, ptTreatment.isPostSample()?this.TRUE_RESOURCE:this.FALSE_RESOURCE);
                treatment.addProperty(patientTreatmentResponse, getResponseResource(ptTreatment.getResponse()));
                treatment.addProperty(patientTreatmentReasonStopped, ptTreatment.getReasonStopped());
                treatment.addProperty(this.patientTreatmentIndexProperty,
                        ResourceFactory.createTypedLiteral(String.valueOf(ptTreatment.getIndex()),XSDDatatype.XSDinteger));
                treatment.addProperty(this.hasPatientTreatmentProperty, patientResource);

                patientResource.addProperty(hasPatientTreatmentProperty, treatment);
        }
    }

    private void outputTumorRDF(ArrayList<PdxClinicalTumor> clinTumors) {

        /*
         private String clinicalMarkers;
            private String tumorGrade;
            private String tStage;
            private String nStage;
            private String mStage;
            private String overallStage;
            private boolean treatmentNaive;
            private String sampleType;
            private String sublineOf;
            private String sublineReason;
        */
        for (PdxClinicalTumor clintumor : clinTumors) {
            //String tumorURI=String.format("%s%s",PDXNET_NAMESPACE,clintumor.getSubmitterTumorID());

            Resource tumorSample = rdfModel.createResource(String.format("%s/%s", PDXNET_NAMESPACE, clintumor.getSubmitterTumorID()));
            //rdfModelMap.put(clintumor.getSubmitterTumorID(),tumorSample);
            Resource category = rdfModel.createResource(String.format("%s%s", NCIT_NAMESPACE, clintumor.getCategoryTerm().getId()));
            category.addProperty(RDF.type, this.pdxTumorCategory);
            //    category.addProperty(RDFS.label,"Tumor category");

            Resource tissue = rdfModel.createResource(UBERON_NAMESPACE + clintumor.getTissueOfOriginTerm().getId());
            tissue.addProperty(RDF.type, this.pdxTissueOfOrigin);
            //   tissue.addProperty(RDFS.label, "Tumor tissue of origin");

            Resource histology = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getTissueHistologyTerm().getId());
            histology.addProperty(RDF.type, this.pdxTumorHistology);
            //    histology.addProperty(RDFS.label,"Tumor histology");

            //Resource stage = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getStage().getId());
            // stage.addProperty(RDF.type, this.pdxTumorStage);
            //     stage.addProperty(RDFS.label,"Tumor stage");

            //Resource grade = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getTumorGrade().getId());
            //grade.addProperty(RDF.type, this.pdxTumorGrade);
            //     grade.addProperty(RDFS.label, "Tumor grade");

            tumorSample.addProperty(hasSubmitterTumorIdProperty, clintumor.getSubmitterTumorID())
                    .addProperty(hasTissueOfOriginProperty, tissue)
                    .addProperty(hasTumorHistologyProperty, histology)
                    .addProperty(hasOverallStageProperty, clintumor.getOverallStage())
                    .addProperty(hasTStageProperty, clintumor.getTStage())
                    .addProperty(hasMStageProperty, clintumor.getMStage())
                    .addProperty(hasNStageProperty, clintumor.getNStage())
                    .addProperty(hasTumorGradeProperty, clintumor.getTumorGrade())
                    .addProperty(hasTumorCategoryProperty, category);

            tumorSample.addProperty(RDF.type, this.pdxClinicalTumor);
            tumorSample.addProperty(RDFS.label, clintumor.getSubmitterTumorID());

            // bi directional
            tumorSample.addProperty(fromPatientProperty, rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, clintumor.getPatientID())));

        }
    }

    private void outputModelStudyRDF(PdxModel model) {
        ArrayList<PdxModelStudy> modelStudies = model.getModelStudy();

        // how is a model study identified? it should have a name or ID
        // this naming convention won't work for multiple studies for the same model
        // this.thisModelStudy = rdfModel.createResource(String.format("%s%s",PDXNET_NAMESPACE,"Study "+model.getModelCreation().getSubmitterPdxId()));
        for (PdxModelStudy modelStudy : modelStudies) {
            Resource thisModelStudy = rdfModel.createResource(PDXNET_NAMESPACE + "/Study-" + modelStudy.getStudyID());
            Resource modelCreation = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ modelStudy.getModelID());
            
            thisModelStudy.addProperty(hasPdxModelProperty, modelCreation);
            
            if(modelStudy.hasMetastasis()){
                thisModelStudy.addProperty(this.pdxStudyHasMetastasisProperty,this.TRUE_RESOURCE);
                Resource tissue = rdfModel.createResource(UBERON_NAMESPACE + modelStudy.getMetastasisLocation().getId());
                thisModelStudy.addProperty(this.pdxStudyMetastasisLocationProperty,tissue);
                // no property for met tissue?
            }else{
                thisModelStudy.addProperty(this.pdxStudyHasMetastasisProperty,this.FALSE_RESOURCE);
            }
           
            
            ResponseToTreatment response = modelStudy.getResponse();
            
            thisModelStudy.addProperty(pdxTumorResponseProperty, getResponseResource(response));

            int index = 0;
            for (PdxStudyTreatment studyTreatment : modelStudy.getTreatments()) {
                index++;
                // is this sufficent for an Id?
                Resource treatment = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ modelStudy.getStudyID() + "/treatment" + index);
                treatment.addProperty(studyTreatmentDrug, studyTreatment.getDrug());
                treatment.addProperty(studyTreatmentDose, studyTreatment.getDose());
                treatment.addProperty(studyTreatmentUnits, studyTreatment.getUnits());
                treatment.addProperty(studyTreatmentRoute, studyTreatment.getRoute());
                treatment.addProperty(studyTreatmentFrequency, studyTreatment.getFrequency());
                treatment.addProperty(hasStudyProperty, thisModelStudy);

                thisModelStudy.addProperty(hasStudyTreatmentProperty, treatment);

            }
            thisModelStudy.addProperty(pdxDoublingLagTimeProperty, ResourceFactory.createTypedLiteral(String.valueOf(modelStudy.getDoublingLagTime()),
                    XSDDatatype.XSDinteger))
                    .addProperty(pdxStudyMetastasisPassageProperty, ResourceFactory.createTypedLiteral(String.valueOf(modelStudy.getMetastasisPassage()),
                            XSDDatatype.XSDinteger));

            thisModelStudy.addProperty(RDF.type, this.pdxModelStudy);
            thisModelStudy.addProperty(RDFS.label, "Model study for " + modelStudy.getModelID());
            rdfModel.getResource(PDXNET_NAMESPACE +"/"+ modelStudy.getModelID()).addProperty(hasStudyProperty, thisModelStudy);

        }
    }

    private void outputModelCreationRdf(PdxModel model) {
        ArrayList<PdxModelCreation> mcreations = model.getModelCreation();
        for (PdxModelCreation mcreation : mcreations) {

            Resource modelCreation = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ mcreation.getModelID());

            Resource tumorSample = rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, mcreation.getTumorID()));

            tumorSample.addProperty(hasPdxModelProperty, modelCreation);
            modelCreation.addProperty(hasTumorProperty, tumorSample);
            modelCreation.addProperty(hasStrainProperty, mcreation.getMouseStrain());
            modelCreation.addProperty(mouseSourceProperty, mcreation.getMouseSource());
            if (mcreation.isHumanized()) {
                modelCreation.addProperty(strainHumanizedProperty, TRUE_RESOURCE);
                modelCreation.addProperty(humanizationTypeProperty, mcreation.getHumanizationType());
            } else {
                modelCreation.addProperty(strainHumanizedProperty, FALSE_RESOURCE);
            }
            

            modelCreation.addProperty(mouseTreatmentForEngraftment, mcreation.getTreatmentForEngraftment());
            modelCreation.addProperty(engraftmentPercentProperty,
                    ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentRate()),
                            XSDDatatype.XSDdecimal));
            modelCreation.addProperty(engraftTimeInDaysProperty,
                    ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentTime()),
                            XSDDatatype.XSDinteger));

            modelCreation.addProperty(RDF.type, this.pdxModelCreation);
            modelCreation.addProperty(RDFS.label, "PDX Model " + mcreation.getModelID());
        }
    }

    private void outputQualityAssuranceRdf(PdxModel model) {
        
     /*   
         private String modelID;
    private int passage;
    private String qcMethod;
    private String qcResult;
    private String animalHealthStatus;
    */
        ArrayList<PdxQualityAssurance> qas = model.getQualityAssurance();

        for (PdxQualityAssurance quality : qas) {
           
        }
    }
    
    private void outputOmicsFileRDF(PdxModel model){
        for(PdxOmicsFile omicsFile : model.getOmicsFiles()){
            
            
            // is file name sufficent?
            Resource thisOmicsFile = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ omicsFile.getFileName());
            thisOmicsFile.addProperty(RDF.type, this.pdxOmicsFile);
            thisOmicsFile.addProperty(RDFS.label, omicsFile.getFileName());
            
            if(omicsFile.getPatientID()!=null){
                this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getPatientID())).addProperty(this.hasOmicsFile, thisOmicsFile);
                thisOmicsFile.addProperty(this.hasPatientAssociation,this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getPatientID()))); 
            }
            if(omicsFile.getModelID()!=null){
                this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getModelID())).addProperty(this.hasOmicsFile, thisOmicsFile);
                thisOmicsFile.addProperty(this.hasModelAssociation,String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getModelID()));
            }
            
            thisOmicsFile.addProperty(this.hasAccessLevel, omicsFile.getAccessLevel());
            thisOmicsFile.addProperty(this.hasCreatedDateTime,omicsFile.getCreatedDateTime());
            thisOmicsFile.addProperty(this.hasDataCategory,omicsFile.getDataCategory());
            thisOmicsFile.addProperty(this.hasDataFormat,omicsFile.getDataFormat());
            thisOmicsFile.addProperty(this.hasDataType,omicsFile.getDataType());
            thisOmicsFile.addProperty(this.hasSampleType,omicsFile.getSampleType());
            thisOmicsFile.addProperty(this.hasExperimentalStrategy,omicsFile.getExperimentalStrategy());
            thisOmicsFile.addProperty(this.hasFileSize,omicsFile.getFileSize());
            thisOmicsFile.addProperty(this.hasPlatform,omicsFile.getPlatform());
            thisOmicsFile.addProperty(this.hasCaptureKit,omicsFile.getCaptureKit());
            thisOmicsFile.addProperty(this.hasUpdatedDateTime,omicsFile.getUpdatedDateTime());
            // i suppose this is a boolean and should be treated differently ...
            thisOmicsFile.addProperty(this.hasFFPEPairedEnd,omicsFile.getIsFFPEPairedEnd());
            thisOmicsFile.addProperty(this.hasFileName,omicsFile.getFileName());
       
        }
        
    }
    
    private Resource getResponseResource(ResponseToTreatment response){
        Resource resource = notAssessed;
         switch (response) {
                case NOT_ASSESSED:
                    resource =  notAssessed;
                    break;
                case STABLE_DISEASE:
                    resource =  stableDisease;
                    break;
                case PARTIAL_RESPONSE:
                    resource =  partialResponse;
                    break;
                case COMPLETE_RESPONSE:
                    resource =  completeResponse;
                    break;
                case PROGRESSIVE_DISEASE:
                    resource =  progressiveDisease;
                    break;
            }
         return resource;
    }

    private void createEntities() {
        this.maleSex = rdfModel.createResource(NCIT_NAMESPACE + male.getId());
        this.femaleSex = rdfModel.createResource(NCIT_NAMESPACE + female.getId());
        this.maleSex.addProperty(RDF.type, this.pdxSex);
        //   this.maleSex.addProperty(RDFS.label,"Male");

        this.femaleSex.addProperty(RDF.type, this.pdxSex);
        //   this.femaleSex.addProperty(RDFS.label,"Female");

        this.noConsent = rdfModel.createResource(PDXNET_NAMESPACE + "consent_NO");
        this.noConsent.addProperty(RDFS.label, "No patient consent provided");
        this.noConsent.addProperty(RDF.type, this.pdxPatientConsent);

        this.yesConsent = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "consent_YES");
        this.yesConsent.addProperty(RDFS.label, "Patient consent provided");
        this.yesConsent.addProperty(RDF.type, this.pdxPatientConsent);

        this.academicConsent = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "consent_ACADEMIC_ONLY");
        this.academicConsent.addProperty(RDFS.label, "Academic consent only");
        this.academicConsent.addProperty(RDF.type, this.pdxPatientConsent);

        this.TRUE_RESOURCE = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "True");
        this.TRUE_RESOURCE.addProperty(RDFS.label, "True");
        this.TRUE_RESOURCE.addProperty(RDF.type, this.pdxBoolean);

        this.FALSE_RESOURCE = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "False");
        this.FALSE_RESOURCE.addProperty(RDFS.label, "False");
        this.FALSE_RESOURCE.addProperty(RDF.type, this.pdxBoolean);

        this.tumorPrepSolid = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Solid");
        this.tumorPrepSuspension = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Suspension");
        this.tumorPrepAscites = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Ascites");

        this.tumorPrepSolid.addProperty(RDFS.label, "Tumor preperation solid");
        this.tumorPrepSuspension.addProperty(RDFS.label, "Tumor preperation suspension");
        this.tumorPrepAscites.addProperty(RDFS.label, "Tumor preperation ascites");

        this.tumorPrepSolid.addProperty(RDF.type, this.pdxTumorSampleType);
        this.tumorPrepSuspension.addProperty(RDF.type, this.pdxTumorSampleType);
        this.tumorPrepAscites.addProperty(RDF.type, this.pdxTumorSampleType);

        // treatment for engfatement
        this.mouseRxGCSF = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "G-CSF");
        this.mouseRxGCSF.addProperty(RDFS.label, "G-CSF treatment for engraftment");
        this.mouseRxGCSF.addProperty(RDF.type, this.pdxTreatmentForEngraftment);

        this.mouseRxEstrogen = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Estrogen");
        this.mouseRxEstrogen.addProperty(RDFS.label, "Estrogen treatment for engraftment");
        this.mouseRxEstrogen.addProperty(RDF.type, this.pdxTreatmentForEngraftment);

        this.notAssessed = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Not_assessed");
        this.notAssessed.addProperty(RDFS.label, "Not assessed");
        this.notAssessed.addProperty(RDF.type, this.pdxTreatmentResponse);
        
        this.completeResponse = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Complete_response");
        this.completeResponse.addProperty(RDFS.label, "Complete response");
        this.completeResponse.addProperty(RDF.type, this.pdxTreatmentResponse);
        
        this.partialResponse = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Partial_response");
        this.partialResponse.addProperty(RDFS.label, "Partial response");
        this.partialResponse.addProperty(RDF.type, this.pdxTreatmentResponse);
        
        this.stableDisease = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Stable_disease");
        this.stableDisease.addProperty(RDFS.label, "Stable disease");
        this.stableDisease.addProperty(RDF.type, this.pdxTreatmentResponse);
        
        this.progressiveDisease = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Progressive_disease");
        this.progressiveDisease.addProperty(RDFS.label, "Progressive disease");
        this.progressiveDisease.addProperty(RDF.type, this.pdxTreatmentResponse);

        this.IHC = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "IHC");
        this.IHC.addProperty(RDF.type, this.pdxModelCharacterization);
        this.IHC.addProperty(RDFS.label, "IHC");

        this.histology = rdfModel.createResource(PDXNET_NAMESPACE +"/"+ "Histology");
        this.histology.addProperty(RDF.type, this.pdxModelCharacterization);
        this.histology.addProperty(RDFS.label, "Histology");

    }

    private void specifyPrefixes() {
        // unique idenfitifer for the patient
        this.hasPatientIdProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "#hasPatientID");
        this.hasPatientIdProperty.addProperty(RDFS.label, "Unique identifer for patient");
        this.hasPatientIdProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Patient's Initial Clinical Diagnosis
        this.hasDiagnosisProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "#hasDiagnosis");
        this.hasDiagnosisProperty.addProperty(RDFS.label, "Patient's initial clincal diagnosis");
        this.hasDiagnosisProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasDiagnosisProperty.addProperty(RDFS.domain, this.pdxPatient);
        this.hasDiagnosisProperty.addProperty(RDFS.range, this.pdxDiagnosis);

        // Unique tumor id
        this.hasSubmitterTumorIdProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "#hasSubmitterTumorId");
        this.hasSubmitterTumorIdProperty.addProperty(RDFS.label, "Unique identifer for sampled tissue");
        this.hasSubmitterTumorIdProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Tumor Sample Object
        this.hasTumorProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumor");
        this.hasTumorProperty.addProperty(RDFS.label, "Patient tumor sample");
        this.hasTumorProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorProperty.addProperty(RDFS.range, this.pdxClinicalTumor);

        this.fromPatientProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatient");
        this.fromPatientProperty.addProperty(RDFS.label, "Derived from patient");
        this.fromPatientProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.fromPatientProperty.addProperty(RDFS.range, this.pdxPatient);

        this.hasStudyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudy");
        this.hasStudyProperty.addProperty(RDFS.label, "Has model study");
        this.hasStudyProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasStudyProperty.addProperty(RDFS.range, this.pdxModelStudy);

        this.hasStudyTreatmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatment");
        this.hasStudyTreatmentProperty.addProperty(RDFS.label, "Has study treatment");
        this.hasStudyTreatmentProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasStudyTreatmentProperty.addProperty(RDFS.range, this.pdxStudyTreatment);

        this.hasPatientTreatmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatment");
        this.hasPatientTreatmentProperty.addProperty(RDFS.label, "Has patient treatment");
        this.hasPatientTreatmentProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasPatientTreatmentProperty.addProperty(RDFS.range, this.pdxPatientTreatment);

       
        // Patient sex
        this.sexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSex");
        this.sexProperty.addProperty(RDFS.label, "Has sex");
        this.sexProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.sexProperty.addProperty(RDFS.range, this.pdxSex);

        // Patient has provided consent to share data
        this.consentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasConsent");
        this.consentProperty.addProperty(RDFS.label, "Patient consent to share data");
        this.consentProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Ethnicity of patient
        this.ethnicityProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEthnicity");
        this.ethnicityProperty.addProperty(RDFS.label, "Patient ethnicity");
        this.ethnicityProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Patient tumor tissue of origin
        this.hasTissueOfOriginProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTissueOfOrigin");
        this.hasTissueOfOriginProperty.addProperty(RDFS.label, "Tumor's tissue of origin");
        this.hasTissueOfOriginProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Tumor Category
        this.hasTumorCategoryProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorCategory");
        this.hasTumorCategoryProperty.addProperty(RDFS.label, "Tumor category");
        this.hasTumorCategoryProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Histology for tumor
        this.hasTumorHistologyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorHistology");
        this.hasTumorHistologyProperty.addProperty(RDFS.label, "Pathologist's Histologic Diagnosis");
        this.hasTumorHistologyProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorHistologyProperty.addProperty(RDFS.range, this.pdxDiagnosis);

        // Grade of tumor
        this.hasTumorGradeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorGrade");
        this.hasTumorGradeProperty.addProperty(RDFS.label, "Tumor grade");
        this.hasTumorGradeProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Stage of tumor
        this.hasOverallStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasOverallStage");
        this.hasOverallStageProperty.addProperty(RDFS.label, "Tumor overall stage");
        this.hasOverallStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        
        this.hasTStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTStage");
        this.hasTStageProperty.addProperty(RDFS.label, "Tumor T stage");
        this.hasTStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        
        this.hasNStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasNStage");
        this.hasNStageProperty.addProperty(RDFS.label, "Tumor N stage");
        this.hasNStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        
        this.hasMStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMStage");
        this.hasMStageProperty.addProperty(RDFS.label, "Tumor M stage");
        this.hasMStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // PDX model generated from patient tumor
        this.hasPdxModelProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPdxModel");
        this.hasPdxModelProperty.addProperty(RDFS.label, "PDX model generated from patient tissue");
        this.hasPdxModelProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Mouse strain for PDX model
        this.hasStrainProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStrain");
        this.hasStrainProperty.addProperty(RDFS.label, "Mouse strain used for engraftment");
        this.hasStrainProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Is the mouse strain humanized
        this.strainHumanizedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasHumanization");
        this.strainHumanizedProperty.addProperty(RDFS.label, "Was mouse strain humanized");
        this.strainHumanizedProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Type of mouse humanization
        this.humanizationTypeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasHumanizationType");
        this.humanizationTypeProperty.addProperty(RDFS.label, "Type of mouse humanization");
        this.humanizationTypeProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Type of tumor preperation
        this.tumorPreparation = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorPreparation");
        this.tumorPreparation.addProperty(RDFS.label, "Type of tumor preparation for engraftment");
        this.tumorPreparation.addProperty(RDF.type, OWL.ObjectProperty);

        // Instituion providing mouse
        this.mouseSourceProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSource");
        this.mouseSourceProperty.addProperty(RDFS.label, "Institution providing mouse");
        this.mouseSourceProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Treatement of mouse prior to engraftment
        this.mouseTreatmentForEngraftment = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTreatmentForEngraftment");
        this.mouseTreatmentForEngraftment.addProperty(RDFS.label, "Mouse treatment for engraftment");
        this.mouseTreatmentForEngraftment.addProperty(RDF.type, OWL.ObjectProperty);

        // Percent of successful engraftments 
        this.engraftmentPercentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentPercent");
        this.engraftmentPercentProperty.addProperty(RDFS.label, "Engraftment percent");
        this.engraftmentPercentProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Days for successful engraftment
        this.engraftTimeInDaysProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentTimeInDays");
        this.engraftTimeInDaysProperty.addProperty(RDFS.label, "Engraftment time in days");
        this.engraftTimeInDaysProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Tumor Characterization
        this.hasTumorCharacterizationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorCharacterization");
        this.hasTumorCharacterizationProperty.addProperty(RDFS.label, "Tumor Characterization");
        this.hasTumorCharacterizationProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Tumor is not EBV or mouse tissue
        this.tumorNotEbvNotMouseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasNotEbvNotMouseStatus");
        this.tumorNotEbvNotMouseProperty.addProperty(RDFS.label, "Mouse is not EBV positive, tumor tissue is not mouse origin");
        this.tumorNotEbvNotMouseProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // PDX model response to treatment
        this.pdxTumorResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorResponse");
        this.pdxTumorResponseProperty.addProperty(RDFS.label, "Tumor Response");
        this.pdxTumorResponseProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // The pdx model's health status
        this.animalHealthStatusSatisfactoryProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasOKAnimalHealthStatus");
        this.animalHealthStatusSatisfactoryProperty.addProperty(RDFS.label, "Animal health status is ok");
        this.animalHealthStatusSatisfactoryProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Passage on which QA was performed
        this.passageQaPerformedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPassageQAperformed");
        this.passageQaPerformedProperty.addProperty(RDFS.label, "Passage QA performed");
        this.passageQaPerformedProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Patient current treatment drug
        this.currentTreatmentDrug = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCurrentTreatmentDrug");
        this.currentTreatmentDrug.addProperty(RDFS.label, "Current patient treatment drug");
        this.currentTreatmentDrug.addProperty(RDF.type, OWL.DatatypeProperty);

        // Lower age range for Patient when sample was taken
        this.ageBinLowerRange = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasAgeBinLowerRange");
        this.ageBinLowerRange.addProperty(RDFS.label, "Lower range of 5 year age bin");
        this.ageBinLowerRange.addProperty(RDFS.domain, pdxPatient);
        this.ageBinLowerRange.addProperty(RDF.type, OWL.DatatypeProperty);

        // Upper age range for Patient when sample was taken
        this.ageBinUpperRange = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasAgeBinUpperRange");
        this.ageBinUpperRange.addProperty(RDFS.label, "Upper range of 5 year age bin");
        this.ageBinUpperRange.addProperty(RDFS.domain, pdxPatient);
        this.ageBinUpperRange.addProperty(RDF.type, OWL.DatatypeProperty);

        this.pdxStudyTreatmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatment");
        this.pdxStudyTreatmentProperty.addProperty(RDFS.label, "Study treatment");
        this.pdxStudyTreatmentProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.pdxDoublingLagTimeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDoublingLagTime");
        this.pdxDoublingLagTimeProperty.addProperty(RDFS.label, "Doubling lag time");
        this.pdxDoublingLagTimeProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.pdxStudyHasMetastasisProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMetastasis");
        this.pdxStudyHasMetastasisProperty.addProperty(RDFS.label, "Study model has metastasis");
        this.pdxStudyHasMetastasisProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.pdxStudyMetastasisLocationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMetastasisLocation");
        this.pdxStudyMetastasisLocationProperty.addProperty(RDFS.label, "Model metastasis location");
        this.pdxStudyMetastasisLocationProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.pdxStudyMetastasisPassageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMetastasisPassage");
        this.pdxStudyMetastasisPassageProperty.addProperty(RDFS.label, "Model metastasis passage");
        this.pdxStudyMetastasisPassageProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.patientTreatmentIndexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentIndex");
        this.patientTreatmentIndexProperty.addProperty(RDFS.label, "Patient treatment index");
        this.patientTreatmentIndexProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.patientTreatmentPostSampleProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentPostSample");
        this.patientTreatmentPostSampleProperty.addProperty(RDFS.label, "Patient treatment post sample");
        this.patientTreatmentPostSampleProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.patientTreatmentRegimen = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentRegimen");
        this.patientTreatmentRegimen.addProperty(RDFS.label, "Patient treatment regimen");
        this.patientTreatmentRegimen.addProperty(RDF.type, OWL.ObjectProperty);

        this.patientTreatmentResponse = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentResponse");
        this.patientTreatmentResponse.addProperty(RDFS.label, "Patient treatment reponse");
        this.patientTreatmentResponse.addProperty(RDF.type, OWL.ObjectProperty);
        this.patientTreatmentResponse.addProperty(RDFS.range, this.pdxTreatmentResponse);

        this.patientTreatmentReasonStopped = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentReasonStopped");
        this.patientTreatmentReasonStopped.addProperty(RDFS.label, "Patient treatment reason stopped");
        this.patientTreatmentReasonStopped.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentDrug = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentDrug");
        this.studyTreatmentDrug.addProperty(RDFS.label, "Study treatment drug");
        this.studyTreatmentDrug.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentDose = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentDose");
        this.studyTreatmentDose.addProperty(RDFS.label, "Study treatment dose");
        this.studyTreatmentDose.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentUnits = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentUnits");
        this.studyTreatmentUnits.addProperty(RDFS.label, "Study treatment units");
        this.studyTreatmentUnits.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentRoute = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentRoute");
        this.studyTreatmentRoute.addProperty(RDFS.label, "Study treatment route");
        this.studyTreatmentRoute.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentFrequency = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentFrequency");
        this.studyTreatmentFrequency.addProperty(RDFS.label, "Study treatment frequency");
        this.studyTreatmentFrequency.addProperty(RDF.type, OWL.ObjectProperty);

        // for omics files
        this.hasOmicsFile = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasOmicsFile");
        this.hasOmicsFile.addProperty(RDFS.label, "Has associated OMICS file");
        this.hasOmicsFile.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasOmicsFile.addProperty(RDFS.range, this.pdxOmicsFile);
        
        this.hasPatientAssociation = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasPatientAssociation");
        this.hasPatientAssociation.addProperty(RDFS.label, "Has associated patient");
        this.hasPatientAssociation.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasPatientAssociation.addProperty(RDFS.range, this.pdxPatient);
        
        this.hasModelAssociation = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasModelAssociation");
        this.hasModelAssociation.addProperty(RDFS.label, "Has associated model");
        this.hasModelAssociation.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasModelAssociation.addProperty(RDFS.range, this.pdxModelCreation);
        
        this.hasAccessLevel = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasAccessLevel");
        this.hasAccessLevel.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasAccessLevel.addProperty(RDFS.label, "Access level");
                
        this.hasCreatedDateTime  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCreatedDateTime");
        this.hasCreatedDateTime.addProperty(RDFS.label, "Created date and time");
        this.hasCreatedDateTime.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasDataCategory  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDataCategory");	
        this.hasDataCategory.addProperty(RDFS.label, "Data category");
        this.hasDataCategory.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasDataFormat  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDataFormat");
        this.hasDataFormat.addProperty(RDFS.label, "Data format");
        this.hasDataFormat.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasDataType  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDataType");
        this.hasDataType.addProperty(RDFS.label, "Data type");
        this.hasDataType.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasSampleType  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSampleType");	
        this.hasSampleType.addProperty(RDFS.label, "Sample type");
        this.hasSampleType.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasExperimentalStrategy  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasExperimentalStrategy");
         this.hasExperimentalStrategy.addProperty(RDFS.label, "Experimental strategy");
         this.hasExperimentalStrategy.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasFileSize  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFileSize");
        this.hasFileSize.addProperty(RDFS.label, "File size");
        this.hasFileSize.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasPlatform  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPlatform");	
        this.hasPlatform.addProperty(RDFS.label, "Platform");
        this.hasPlatform.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasCaptureKit  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCaptureKit");	
        this.hasCaptureKit.addProperty(RDFS.label, "Capture kit");
        this.hasCaptureKit.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasUpdatedDateTime  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasUpdateDateTime");
        this.hasUpdatedDateTime.addProperty(RDFS.label, "Update date and time");
        this.hasUpdatedDateTime.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasFFPEPairedEnd  = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFFPEPairedEnd");	
        this.hasFFPEPairedEnd.addProperty(RDFS.label, "FFPE paired end");
        this.hasFFPEPairedEnd.addProperty(RDF.type, OWL.ObjectProperty);
        
        this.hasFileName = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFileName");
        this.hasFileName.addProperty(RDFS.label, "File name");
        this.hasFileName.addProperty(RDF.type, OWL.ObjectProperty);
                
                
        rdfModel.setNsPrefix("PDXNET", PDXNET_NAMESPACE);
        rdfModel.setNsPrefix("NCIT", NCIT_NAMESPACE);
        rdfModel.setNsPrefix("UBERON", UBERON_NAMESPACE);

    }

}
