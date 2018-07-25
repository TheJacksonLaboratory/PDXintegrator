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
    private Property hasPatientIdProperty;
    private Property hasSubmitterTumorIdProperty;
    private Property hasDiagnosisProperty;
    private Property hasTumorProperty;
    private Property hasTissueOfOriginProperty;
    private Property hasTumorCategoryProperty;
    private Property hasTumorHistologyProperty;
    private Property hasTumorGradeProperty;
    private Property hasTStageProperty;
    private Property hasMStageProperty;
    private Property hasNStageProperty;
    private Property hasPdxModelProperty;
    private Property hasStrainProperty;
    private Property hasStrainSourceProperty;
    private Property hasMouseSexProperty;
    private Property hasModelEngraftmentProcedureProperty;
    private Property hasModelEngraftmentSiteProperty;
    private Property hasModelHumanizedProperty;
    private Property hasModelHumanizationTypeProperty;
    private Property hasTumorPreparation;
    private Property hasModelTreatmentForEngraftmentProperty;
    private Property hasModelEngraftmentPercentProperty;
    private Property hasModelEngraftTimeInDaysProperty;
    private Property hasModelHistologyProperty;
    private Property hasPassagedTissueCryopreservedProperty;
    private Property hasSublineOfModelProperty;
    private Property hasSublineReasonProperty;

    private Property hasTumorCharacterizationProperty;
    private Property tumorNotEbvNotMouseProperty;
    private Property hasPdxTumorResponseProperty;
    private Property currentTreatmentDrug;
    private Property primaryDiagnosisAgeBinLowerRange;
    private Property primaryDiagnosisAgeBinUpperRange;

    private Property sexProperty;
    private Property consentProperty;

    private Property ethnicityProperty;
    private Property raceProperty;
    private Property patientVirolgyStatusProperty;

    private Property hasStudyTreatmentProperty;

    private Property fromPatientProperty;
    private Property hasStudyProperty;

    private Property hasPatientTreatmentProperty;

    private Property hasPatientTreatmentIndexProperty;
    private Property hasPatientTreatmentRegimen;
    private Property hasPatientTreatmentResponse;
    private Property hasPatientTreatmentReasonStopped;
    private Property hasPatientClinicalTreatmentSettingProperty;
    private Property hasPatientTreatmentNotesProperty;

    private Property hasStudyTreatmentDrugProperty;
    private Property hasStudyTreatmentDoseProperty;
    private Property hasStudyTreatmentRouteProperty;
    private Property hasStudyTreatmentFrequencyProperty;
    private Property hasStudyHasMetastasisProperty;
    private Property hasStudyMetastasisLocationProperty;
    private Property hasStudyMetastasisPassageProperty;
    private Property hasStudyTreatmentEndpoint1ResponseProperty;
    private Property hasStudyTreatmentEndpoint2ResponseProperty;
    private Property hasStudyTreatmentEndpoint3ResponseProperty;

    private Property hasTumorEventIndexProperty;
    private Property hasTumorTreatmentNaiveProperty;
    private Property hasPatientAgeAtCollectionProperty;
    private Property hasTumorInitialDiagnosisProperty;
    private Property hasTumorTissueOfOriginProperty;
    private Property hasTumorSpecimenTissueProperty;
    private Property hasTumorTissueHistologyProperty;
    private Property hasTumorClinicalMarkersProperty;
    private Property hasTumorOverallStageProperty;
    private Property hasTumorSampleTypeProperty;

    private Property hasQAMethodProperty;
    private Property hasQAResultProperty;
    private Property hasQAPassProperty;
    private Property hasAnimalHealthStatusProperty;
    private Property hasAnimalHealthStatusSatisfactoryProperty;
    private Property hasPassageQaPerformedProperty;
    private Property hasQAProperty;

    // for omics files
    private Property hasOmicsFile;
    private Property hasAccessLevel;
    private Property hasCreatedDateTime;
    private Property hasDataCategory;
    private Property hasDataFormat;
    private Property hasDataType;
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
    private Property hasPassage;

    // new properties should get all of these organize
    private Property hasClinicalEventPointProperty;
    private Property hasCollectionProcedureProperty;
    private Property hasDiseaseProgressionTermProperty;
    private Property hasMetastaticSitesProperty;
    private Property hasSpecimenTissueTermProperty;
    private Property hasStrAnalysisProperty;
    private Property hasStrEvaluationProperty;
    private Property hasStrMarkersProperty;
    private Property hasCryopreservedBeforeEngraftmentProperty;
    private Property hasDoublingTimeProperty;
    private Property hasEngraftmentMaterialProperty;
    private Property hasHumanizedProperty;
    private Property hasMacroMetastasisRequiresExcisionProperty;
    private Property hasMetastastaticSitesProperty;
    private Property hasViablyCryopreseredProperty;


    private Property hasPdtcProperty;

    private Property hasCd45IHCProperty;
    private Property hasCd45IHCAssayResultProperty;

    private Property hasClinicalDiagnosticMarkerAssayResultProperty;
    private Property hasClinicalDiagnosticMarkerNotesProperty;
    private Property hasClinicalDiagnosticMarkersProperty;
    private Property hasEbvTranscriptDetectionProperty;
    private Property hasEbvTranscriptDetectionResultProperty;
    private Property hasHumanSpecificCytokeratin19Property;
    private Property hasMousePathogenStatusProperty;
    private Property hasOverallEvaluationProperty;
    private Property hasPanCytokeratinAssayResultProperty;
    private Property hasStrNotesProperty;

    private Property hasCohortProperty;
    private Property hasCohortSizeProperty;
    private Property hasDosingScheduleProperty;
   
    private Property hasNumberOfCyclesProperty;
    private Property hasStudyDurationProperty;
    
    
    
    private Resource maleSex;
    private Resource femaleSex;
    private Resource noConsent;
    private Resource yesConsent;
    private Resource TRUE_RESOURCE;
    private Resource FALSE_RESOURCE;
    private Resource academicConsent;
    
    

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

    private static final String PDXNET_NAMESPACE = "http://pdxnetwork.org/pdxmodel";
    private final static String NCIT_NAMESPACE = "http://purl.obolibrary.org/obo/NCIT_";
    private final static String UBERON_NAMESPACE = "http://purl.obolibrary.org/obo/UBERON_";

    /// these are the classes for 8 part the data model
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
        try {
            rdfModel.write(out);//,"Turtle");
        } catch (BadURIException bue) {
            bue.printStackTrace();
        }

    }

    private void initializeModelFramework() {
        this.rdfModel = ModelFactory.createOntologyModel();

        String pdxPatientURI = String.format("%s%s", PDXNET_NAMESPACE, "#Patient");
        this.pdxPatient = rdfModel.createClass(pdxPatientURI);
        this.pdxPatient.addProperty(RDFS.label, "Patient");

        String pdxPatientTreatmentURI = String.format("%s%s", PDXNET_NAMESPACE, "#PatientTreatment");
        this.pdxPatientTreatment = rdfModel.createClass(pdxPatientTreatmentURI);
        this.pdxPatientTreatment.addProperty(RDFS.label, "Patient Treatment");

        String pdxDiagnosisURI = String.format("%s%s", PDXNET_NAMESPACE, "#Diagnosis");
        this.pdxDiagnosis = rdfModel.createClass(pdxDiagnosisURI);
        this.pdxDiagnosis.addProperty(RDFS.label, "Diagnosis");

        String pdxSexURI = String.format("%s%s", PDXNET_NAMESPACE, "#Sex");
        this.pdxSex = rdfModel.createClass(pdxSexURI);
        this.pdxSex.addProperty(RDFS.label, "Sex");

        String pdxModelStudyURI = String.format("%s%s", PDXNET_NAMESPACE, "#ModelStudy");
        this.pdxModelStudy = rdfModel.createClass(pdxModelStudyURI);
        this.pdxModelStudy.addProperty(RDFS.label, "Model Study");

        String pdxStudyTreatmentURI = String.format("%s%s", PDXNET_NAMESPACE, "#StudyTreatment");
        this.pdxStudyTreatment = rdfModel.createClass(pdxStudyTreatmentURI);
        this.pdxStudyTreatment.addProperty(RDFS.label, "Study Treatment");

        String pdxClinicalTumorURI = String.format("%s%s", PDXNET_NAMESPACE, "#ClinicalTumor");
        this.pdxClinicalTumor = rdfModel.createClass(pdxClinicalTumorURI);
        this.pdxClinicalTumor.addProperty(RDFS.label, "Clinical Tumor");

        String pdxQualityAssuranceURI = String.format("%s%s", PDXNET_NAMESPACE, "#QualityAssurance");
        this.pdxQualityAssurance = rdfModel.createClass(pdxQualityAssuranceURI);
        this.pdxQualityAssurance.addProperty(RDFS.label, "Quality Assurance");

        String pdxModelCreationURI = String.format("%s%s", PDXNET_NAMESPACE, "#ModelCreation");
        this.pdxModelCreation = rdfModel.createClass(pdxModelCreationURI);
        this.pdxModelCreation.addProperty(RDFS.label, "Model Creation");

        String pdxOmicsFileURI = String.format("%s%s", PDXNET_NAMESPACE, "#OmicsFile");
        this.pdxOmicsFile = rdfModel.createClass(pdxOmicsFileURI);
        this.pdxOmicsFile.addProperty(RDFS.label, "Omics File");

        String pdxTreatmentResponseURI = String.format("%s%s", PDXNET_NAMESPACE, "#TreatmentResponse");
        this.pdxTreatmentResponse = rdfModel.createClass(pdxTreatmentResponseURI);
        this.pdxTreatmentResponse.addProperty(RDFS.label, "Treatment Response");

        String pdxTumorSampleTypeURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorSampleType");
        this.pdxTumorSampleType = rdfModel.createClass(pdxTumorSampleTypeURI);
        this.pdxTumorSampleType.addProperty(RDFS.label, "Tumor Sample Type");

        String pdxModelCharacterizationURI = String.format("%s%s", PDXNET_NAMESPACE, "#ModelCharacterization");
        this.pdxModelCharacterization = rdfModel.createClass(pdxModelCharacterizationURI);
        this.pdxModelCharacterization.addProperty(RDFS.label, "Model Characterization");

        String pdxPatientConsentURI = String.format("%s%s", PDXNET_NAMESPACE, "#PatientConsent");
        this.pdxPatientConsent = rdfModel.createClass(pdxPatientConsentURI);
        this.pdxPatientConsent.addProperty(RDFS.label, "Patient Consent");

        String pdxTreatmentForEngraftmentURI = String.format("%s%s", PDXNET_NAMESPACE, "#TreatmentForEngraftment");
        this.pdxTreatmentForEngraftment = rdfModel.createClass(pdxTreatmentForEngraftmentURI);
        this.pdxTreatmentForEngraftment.addProperty(RDFS.label, "Mouse treatment for engraftment");

        String pdxPassageQaPerformedURI = String.format("%s%s", PDXNET_NAMESPACE, "#PassageQaPerformed");
        this.pdxPassageQaPerformed = rdfModel.createClass(pdxPassageQaPerformedURI);
        this.pdxPassageQaPerformed.addProperty(RDFS.label, "Passage QA performed");

        String pdxTissueOfOriginURI = String.format("%s%s", PDXNET_NAMESPACE, "#TissueOfOrigin");
        this.pdxTissueOfOrigin = rdfModel.createClass(pdxTissueOfOriginURI);
        this.pdxTissueOfOrigin.addProperty(RDFS.label, "Tissue of origin");

        String pdxTumorCategoryURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorCategory");
        this.pdxTumorCategory = rdfModel.createClass(pdxTumorCategoryURI);
        this.pdxTumorCategory.addProperty(RDFS.label, "Tumor category");

        String pdxTumorGradeURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorGrade");
        this.pdxTumorGrade = rdfModel.createClass(pdxTumorGradeURI);
        this.pdxTumorGrade.addProperty(RDFS.label, "Tumor grade");

        String pdxTumorStageURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorStage");
        this.pdxTumorStage = rdfModel.createClass(pdxTumorStageURI);
        this.pdxTumorStage.addProperty(RDFS.label, "Tumor stage");

        String pdxTumorHistologyURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorHistology");
        this.pdxTumorHistology = rdfModel.createClass(pdxTumorHistologyURI);
        this.pdxTumorHistology.addProperty(RDFS.label, "Tumor histology");

        String pdxBooleanURI = String.format("%s%s", PDXNET_NAMESPACE, "#Boolean");
        this.pdxBoolean = rdfModel.createClass(pdxBooleanURI);
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

    private void outputPatientRDF(PdxModel pdxmodel) {
        PdxPatient patient = pdxmodel.getPatient();

        String patientURI = String.format("%s/%s", PDXNET_NAMESPACE, patient.getSubmitterPatientID());

        Resource patientResource
                = rdfModel.createResource(patientURI)
                .addProperty(RDFS.label, patient.getSubmitterPatientID())
                .addProperty(RDF.type, this.pdxPatient)
                .addProperty(hasPatientIdProperty, patient.getSubmitterPatientID());

        Resource consent = patient.getConsent().equals(Consent.YES) ? yesConsent : noConsent;
        consent.addProperty(RDF.type, this.pdxPatientConsent);
        patientResource.addProperty(consentProperty, consent);

        setProperty(patientResource, ethnicityProperty, patient.getEthnicity());

        setProperty(patientResource, raceProperty, patient.getRace());

        setProperty(patientResource, patientVirolgyStatusProperty, patient.getVirologyStatus());

        setProperty(patientResource, hasPatientClinicalTreatmentSettingProperty, patient.getClinicalTreatmentSetting());

        setProperty(patientResource, hasPatientTreatmentNotesProperty, patient.getTreatmentNotes());

        try {
            Resource sex = patient.getSex().equals(Sex.FEMALE) ? femaleSex : maleSex;
            patientResource.addProperty(sexProperty, sex);
        } catch (Exception e) {// no sex provided don't add}

        }

        patientResource.addProperty(primaryDiagnosisAgeBinLowerRange,
                ResourceFactory.createTypedLiteral(String.valueOf(patient.getAgeAtDiagnosis().getLower()),
                        XSDDatatype.XSDinteger));
        patientResource.addProperty(primaryDiagnosisAgeBinUpperRange,
                ResourceFactory.createTypedLiteral(String.valueOf(patient.getAgeAtDiagnosis().getUpper()),
                        XSDDatatype.XSDinteger));

        int index = 0;
        if (patient.getPatientTreatments() != null) {
            for (PdxPatientTreatment ptTreatment : patient.getPatientTreatments()) {
                index++;

                // should probably use the index supplied if there is one?
                // would need to verify index count matches number of treatment records
                Resource treatment = rdfModel.createResource(PDXNET_NAMESPACE + "/" + patient.getSubmitterPatientID() + "/treatment" + index);
                treatment.addProperty(hasPatientTreatmentRegimen, ptTreatment.getRegimen());

                treatment.addProperty(hasPatientTreatmentResponse, getResponseResource(ptTreatment.getResponse()));
                treatment.addProperty(hasPatientTreatmentReasonStopped, ptTreatment.getReasonStopped());
                treatment.addProperty(hasPatientTreatmentIndexProperty,
                        ResourceFactory.createTypedLiteral(String.valueOf(ptTreatment.getEventIndex()), XSDDatatype.XSDinteger));
                treatment.addProperty(hasPatientTreatmentProperty, patientResource);

                patientResource.addProperty(hasPatientTreatmentProperty, treatment);
            }
        }
    }

    private void outputTumorRDF(ArrayList<PdxClinicalTumor> clinTumors) {

        for (PdxClinicalTumor clintumor : clinTumors) {

            Resource tumorSample = rdfModel.createResource(String.format("%s/%s", PDXNET_NAMESPACE, clintumor.getSubmitterTumorID()));

            tumorSample.addProperty(hasSubmitterTumorIdProperty, clintumor.getSubmitterTumorID());

            setProperty(tumorSample, hasTumorEventIndexProperty, clintumor.getEventIndex());

            // can this be unknown?
            tumorSample.addProperty(hasTumorTreatmentNaiveProperty, clintumor.isTreatmentNaive() ? this.TRUE_RESOURCE : this.FALSE_RESOURCE);

            // eventually bin this
            setProperty(tumorSample, hasPatientAgeAtCollectionProperty, clintumor.getAgeAtCollection());

            //required?
            setProperty(tumorSample, hasTumorInitialDiagnosisProperty, clintumor.getInitialDiagnosis());

            setProperty(tumorSample, hasTumorTissueOfOriginProperty, clintumor.getTissueOfOrigin());

            
            setProperty(tumorSample, hasTumorSpecimenTissueProperty, clintumor.getSpecimenTissue());

            setProperty(tumorSample, hasTumorTissueHistologyProperty, clintumor.getTissueHistology());

            // split into multiple values?
            setProperty(tumorSample, hasTumorClinicalMarkersProperty, clintumor.getClinicalMarkers());

            setProperty(tumorSample, hasTStageProperty, clintumor.getTStage());

            setProperty(tumorSample, hasMStageProperty, clintumor.getMStage());

            setProperty(tumorSample, hasNStageProperty, clintumor.getNStage());

            setProperty(tumorSample, hasTumorGradeProperty, clintumor.getTumorGrade());

            setProperty(tumorSample, hasTumorOverallStageProperty, clintumor.getOverallStage());

            setProperty(tumorSample, hasTumorSampleTypeProperty, clintumor.getSampleType());
            
            setProperty(tumorSample,hasClinicalEventPointProperty,clintumor.getClinicalEventPoint());
            setProperty(tumorSample,hasCollectionProcedureProperty,clintumor.getCollectionProcedure());
          //  setProperty(tumorSample,hasDiseaseProgressionTermProperty,clintumor.getDiseaseProgressionTerm());
            setProperty(tumorSample,hasMetastaticSitesProperty,clintumor.getMetastaticSites());
         //   setProperty(tumorSample,hasSpecimenTissueTermProperty,clintumor.getSpecimenTissueTerm());
            setProperty(tumorSample,hasStrAnalysisProperty,clintumor.getStrAnalysis());
            setProperty(tumorSample,hasStrEvaluationProperty,clintumor.getStrEvaluation());
            setProperty(tumorSample,hasStrMarkersProperty,clintumor.getStrMarkers());

            try {
                String diagnosisTerm = clintumor.getInitialDiagnosisTerm().getId();
                String diagnosisURI = String.format("%s%s", NCIT_NAMESPACE, diagnosisTerm);
                Resource diagnosisResource = rdfModel.createResource(diagnosisURI);
                diagnosisResource.addProperty(RDF.type, this.pdxDiagnosis);
                tumorSample.addProperty(hasDiagnosisProperty, diagnosisResource);
            } catch (NullPointerException npe) {
                // no diagnosis term
            }

            try {
                Resource tissue = rdfModel.createResource(UBERON_NAMESPACE + clintumor.getTissueOfOriginTerm().getId());
                tissue.addProperty(RDF.type, this.pdxTissueOfOrigin);
                tumorSample.addProperty(hasTissueOfOriginProperty, tissue);
            } catch (NullPointerException npe) {
                // no tissue term
            }

            try {
                Resource histologyTerm = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getTissueHistologyTerm().getId());
                histologyTerm.addProperty(RDF.type, this.pdxTumorHistology);
                tumorSample.addProperty(hasTumorHistologyProperty, histologyTerm);
            } catch (NullPointerException npe) {
                // no histology term
            }

            tumorSample.addProperty(RDF.type, this.pdxClinicalTumor);
            tumorSample.addProperty(RDFS.label, clintumor.getSubmitterTumorID());

            // bi directional
            tumorSample.addProperty(fromPatientProperty, rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, clintumor.getPatientID())));

        }
    }

    private void outputModelStudyRDF(PdxModel model) {
        ArrayList<PdxModelStudy> modelStudies = model.getModelStudy();

        for (PdxModelStudy modelStudy : modelStudies) {
            Resource thisModelStudy = rdfModel.createResource(PDXNET_NAMESPACE + "/" + modelStudy.getStudyID());
            Resource modelCreation = rdfModel.createResource(PDXNET_NAMESPACE + "/" + modelStudy.getModelID());

            thisModelStudy.addProperty(hasPdxModelProperty, modelCreation);

            if (modelStudy.hasMetastasis()) {
                thisModelStudy.addProperty(this.hasStudyHasMetastasisProperty, this.TRUE_RESOURCE);

                // there could be multiple met locations
                /* 
                    convert the possilble list of tissues into UBERON terms
                    Resource tissue = rdfModel.createResource(UBERON_NAMESPACE + modelStudy.getMetastasisLocationTerm().getId());
                    thisModelStudy.addProperty(this.pdxStudyMetastasisLocationTermProperty,tissue);
                 */
                // split this on some delimiter and iterate
                thisModelStudy.addProperty(this.hasStudyMetastasisLocationProperty, modelStudy.getMetastasisLocation());

            } else {
                thisModelStudy.addProperty(this.hasStudyHasMetastasisProperty, this.FALSE_RESOURCE);
            }

            thisModelStudy.addProperty(hasStudyMetastasisPassageProperty, ResourceFactory.createTypedLiteral(String.valueOf(modelStudy.getMetastasisPassage()),
                    XSDDatatype.XSDinteger));

            thisModelStudy.addProperty(RDF.type, this.pdxModelStudy);
            thisModelStudy.addProperty(RDFS.label, "Model study for " + modelStudy.getModelID());
            rdfModel.getResource(PDXNET_NAMESPACE + "/" + modelStudy.getModelID()).addProperty(hasStudyProperty, thisModelStudy);

            int index = 0;
            for (PdxStudyTreatment studyTreatment : modelStudy.getTreatments()) {
                index++;
                // is this sufficent for an Id?

                Resource treatment = rdfModel.createResource(PDXNET_NAMESPACE + "/" + modelStudy.getStudyID() + "/treatment" + index);
                setProperty(treatment, hasStudyTreatmentDrugProperty, studyTreatment.getDrug());
                setProperty(treatment, hasStudyTreatmentDoseProperty, studyTreatment.getDose());
                setProperty(treatment, hasStudyTreatmentRouteProperty, studyTreatment.getRoute());
                
                setProperty(treatment,hasCohortProperty,studyTreatment.getCohort());
                setProperty(treatment,hasCohortSizeProperty,studyTreatment.getCohortSize());
                setProperty(treatment,hasDosingScheduleProperty,studyTreatment.getDosingSchedule());
                
               
                setProperty(treatment,hasStudyTreatmentEndpoint1ResponseProperty, studyTreatment.getEndpoint1Response());
                setProperty(treatment,hasStudyTreatmentEndpoint2ResponseProperty,studyTreatment.getEndpoint2Response());
                setProperty(treatment,hasStudyTreatmentEndpoint3ResponseProperty,studyTreatment.getEndpoint3Response());
                setProperty(treatment,hasNumberOfCyclesProperty,studyTreatment.getNumberOfCycles());
                setProperty(treatment,hasStudyDurationProperty,studyTreatment.getStudyDuration());
                

                treatment.addProperty(hasStudyProperty, thisModelStudy);

                treatment.addProperty(RDF.type, this.pdxStudyTreatment);

                
                String label = studyTreatment.getDrug() + " " + studyTreatment.getDose();
                
                // this  '+' and ';' parsing is jax specific
                if (studyTreatment.getDrug().contains("+")) {
                    String[] drugs = studyTreatment.getDrug().split("\\+");
                    String[] doses = studyTreatment.getDose().split(";");
                    label = drugs[0] + " " + doses[0] + "," + drugs[1] + " " + doses[1];
                }

                treatment.addProperty(RDFS.label, label);

                thisModelStudy.addProperty(hasStudyTreatmentProperty, treatment);

            }

        }
    }

    private void outputModelCreationRdf(PdxModel model) {
        ArrayList<PdxModelCreation> mcreations = model.getModelCreation();
        for (PdxModelCreation mcreation : mcreations) {

            Resource modelCreation = rdfModel.createResource(PDXNET_NAMESPACE + "/" + mcreation.getModelID());

            Resource tumorSample = rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, mcreation.getTumorID()));

            tumorSample.addProperty(hasPdxModelProperty, modelCreation);
            modelCreation.addProperty(hasTumorProperty, tumorSample);
            setProperty(modelCreation, hasStrainProperty, mcreation.getMouseStrain());

            setProperty(modelCreation, hasStrainSourceProperty, mcreation.getMouseSource());

            // I think NCIT terms for sex start with 'A person ..' and might not be appropriate?
            setProperty(modelCreation, hasMouseSexProperty, mcreation.getMouseSex());

            if (mcreation.isHumanized()) {
                modelCreation.addProperty(hasModelHumanizedProperty, TRUE_RESOURCE);
                modelCreation.addProperty(hasModelHumanizationTypeProperty, mcreation.getHumanizationType());
            } else {
                modelCreation.addProperty(hasModelHumanizedProperty, FALSE_RESOURCE);
            }

            setProperty(modelCreation, hasModelEngraftmentProcedureProperty, mcreation.getEngraftmentProcedure());

            setProperty(modelCreation, hasModelEngraftmentSiteProperty, mcreation.getEngraftmnetSite());

            setProperty(modelCreation, hasModelTreatmentForEngraftmentProperty, mcreation.getTreatmentForEngraftment());

            // Carol always calls this concordance.... ie is the passaged/engrafted tissue in concordance with pt tissue based on histologic findings
            setProperty(modelCreation, hasModelHistologyProperty, mcreation.getModelHistology());

         //   if (mcreation.getPassagedTissueCryoPreserved() != null) {
         //       if (mcreation.getPassagedTissueCryoPreserved()) {
         //           modelCreation.addProperty(hasPassagedTissueCryopreservedProperty, TRUE_RESOURCE);
         //       } else {
         //           modelCreation.addProperty(hasPassagedTissueCryopreservedProperty, FALSE_RESOURCE);
         //       }
         //   }

            setProperty(modelCreation, hasSublineOfModelProperty, mcreation.getSublineOfModel());

            setProperty(modelCreation, hasSublineReasonProperty, mcreation.getSublineReason());

            if (mcreation.getEngraftmentRate() != null) {
                modelCreation.addProperty(hasModelEngraftmentPercentProperty,
                        ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentRate()),
                                XSDDatatype.XSDdecimal));
            }

            if (mcreation.getEngraftmentTime() != null) {
                modelCreation.addProperty(hasModelEngraftTimeInDaysProperty,
                        ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentTime()),
                                XSDDatatype.XSDinteger));
            }
            
            
            setProperty(modelCreation,hasCryopreservedBeforeEngraftmentProperty,mcreation.getCryopreservedBeforeEngraftment());
            setProperty(modelCreation,hasDoublingTimeProperty,mcreation.getDoublingTime());
            setProperty(modelCreation,hasEngraftmentMaterialProperty,mcreation.getEngraftmentMaterial());
            setProperty(modelCreation,hasHumanizedProperty,mcreation.getHumanized());
            setProperty(modelCreation,hasMacroMetastasisRequiresExcisionProperty,mcreation.getMacroMetastasisRequiresExcision());
            setProperty(modelCreation,hasMetastastaticSitesProperty,mcreation.getMetastastaticSites());
            setProperty(modelCreation,hasViablyCryopreseredProperty,mcreation.getViablyCryopresered());


            modelCreation.addProperty(RDF.type, this.pdxModelCreation);
            modelCreation.addProperty(RDFS.label, "PDX Model " + mcreation.getModelID());
        }
    }

    /**
     * @TODO @param model
     */
    private void outputQualityAssuranceRdf(PdxModel model) {

       
        ArrayList<PdxQualityAssurance> qas = model.getQualityAssurance();

        for (PdxQualityAssurance quality : qas) {
            /**
             * @TODO
             * PassageTested can be null what to do then?  -- "assage not specified"?
             */
            Resource qa = rdfModel.createResource(PDXNET_NAMESPACE + "/" + quality.getModelID() + "/QA/P" + quality.getPassageTested());
            qa.addProperty(RDF.type, this.pdxQualityAssurance);
            qa.addProperty(RDFS.label, "QA for " + quality.getModelID() + " at passage P" + quality.getPassageTested());

            setProperty(qa, this.hasQAMethodProperty, quality.getQcMethod());
            setProperty(qa, this.hasQAResultProperty, quality.getQcResult());
            setProperty(qa, this.hasPassageQaPerformedProperty, "P" + quality.getPassageTested());
            setProperty(qa, this.hasAnimalHealthStatusSatisfactoryProperty, quality.getAnimalHealthStatus());
            
            setProperty(qa,hasCd45IHCProperty,quality.getCd45IHC());
            setProperty(qa,hasCd45IHCAssayResultProperty,quality.getCd45IHCAssayResult());
            setProperty(qa,hasClinicalDiagnosticMarkerAssayResultProperty,quality.getClinicalDiagnosticMarkerAssayResult());
            setProperty(qa,hasClinicalDiagnosticMarkerNotesProperty,quality.getClinicalDiagnosticMarkerNotes());
            setProperty(qa,hasClinicalDiagnosticMarkersProperty,quality.getClinicalDiagnosticMarkers());
            setProperty(qa,hasEbvTranscriptDetectionProperty,quality.getEbvTranscriptDetection());
            setProperty(qa,hasEbvTranscriptDetectionResultProperty,quality.getEbvTranscriptDetectionResult());
            setProperty(qa,hasHumanSpecificCytokeratin19Property,quality.getHumanSpecificCytokeratin19());
            setProperty(qa,hasMousePathogenStatusProperty,quality.getMousePathogenStatus());
            setProperty(qa,hasOverallEvaluationProperty,quality.getOverallEvaluation());
            setProperty(qa,hasPanCytokeratinAssayResultProperty,quality.getPanCytokeratinAssayResult());
            setProperty(qa,hasStrNotesProperty,quality.getStrNotes());
            

            this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, quality.getModelID())).addProperty(this.hasQAProperty, qa);
            qa.addProperty(this.hasPdxModelProperty, this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, quality.getModelID())));

        }
    }

    private void outputOmicsFileRDF(PdxModel model) {
        for (PdxOmicsFile omicsFile : model.getOmicsFiles()) {

            // is file name sufficent?
            Resource thisOmicsFile = rdfModel.createResource(PDXNET_NAMESPACE + "/" + omicsFile.getFileName());
            thisOmicsFile.addProperty(RDF.type, this.pdxOmicsFile);
            thisOmicsFile.addProperty(RDFS.label, omicsFile.getFileName());

            if (omicsFile.getPatientID() != null) {
                this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getPatientID())).addProperty(this.hasOmicsFile, thisOmicsFile);
                thisOmicsFile.addProperty(this.hasPatientAssociation, this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getPatientID())));
            }
            if (omicsFile.getModelID() != null) {
                this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getModelID())).addProperty(this.hasOmicsFile, thisOmicsFile);
                thisOmicsFile.addProperty(this.hasModelAssociation, this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getModelID())));
            }

            thisOmicsFile.addProperty(this.hasAccessLevel, omicsFile.getAccessLevel());
            thisOmicsFile.addProperty(this.hasCreatedDateTime, omicsFile.getCreatedDateTime());
            thisOmicsFile.addProperty(this.hasDataCategory, omicsFile.getDataCategory());
            thisOmicsFile.addProperty(this.hasDataFormat, omicsFile.getDataFormat());
            thisOmicsFile.addProperty(this.hasDataType, omicsFile.getDataType());
            thisOmicsFile.addProperty(this.hasSampleType, omicsFile.getSampleType());
            thisOmicsFile.addProperty(this.hasExperimentalStrategy, omicsFile.getExperimentalStrategy());
            thisOmicsFile.addProperty(this.hasFileSize, omicsFile.getFileSize());
            thisOmicsFile.addProperty(this.hasPlatform, omicsFile.getPlatform());
            thisOmicsFile.addProperty(this.hasCaptureKit, omicsFile.getCaptureKit());
            thisOmicsFile.addProperty(this.hasUpdatedDateTime, omicsFile.getUpdatedDateTime());
            // i suppose this is a boolean and should be treated differently ...
            thisOmicsFile.addProperty(this.hasFFPEPairedEnd, omicsFile.getIsFFPEPairedEnd());
            thisOmicsFile.addProperty(this.hasFileName, omicsFile.getFileName());
            thisOmicsFile.addProperty(this.hasPassage, omicsFile.getPassage());

        }

    }

    private Resource getResponseResource(ResponseToTreatment response) {
        Resource resource = notAssessed;
        switch (response) {
            case NOT_ASSESSED:
                resource = notAssessed;
                break;
            case STABLE_DISEASE:
                resource = stableDisease;
                break;
            case PARTIAL_RESPONSE:
                resource = partialResponse;
                break;
            case COMPLETE_RESPONSE:
                resource = completeResponse;
                break;
            case PROGRESSIVE_DISEASE:
                resource = progressiveDisease;
                break;
        }
        return resource;
    }

    // add a string property if the value is not null
    private Resource setProperty(Resource resource, Property property, String value) {
        if (value != null && value.length() > 0) {
            resource.addProperty(property, value);
        }
        return resource;
    }
    
    // add an integer property if the value is not null
    private Resource setProperty(Resource resource, Property property, Integer value) {
        if (value != null ) {
             resource.addProperty(property,
                        ResourceFactory.createTypedLiteral(String.valueOf(value),
                                XSDDatatype.XSDinteger));
        }
        return resource;
    }
    
     // add a boolean property if the value is not null
    private Resource setProperty(Resource resource, Property property, Boolean value) {
        if (value != null) {
            if(value){
                resource.addProperty(property, TRUE_RESOURCE);
            }else{
                resource.addProperty(property, FALSE_RESOURCE);
            }
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

        this.yesConsent = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "consent_YES");
        this.yesConsent.addProperty(RDFS.label, "Patient consent provided");
        this.yesConsent.addProperty(RDF.type, this.pdxPatientConsent);

        this.academicConsent = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "consent_ACADEMIC_ONLY");
        this.academicConsent.addProperty(RDFS.label, "Academic consent only");
        this.academicConsent.addProperty(RDF.type, this.pdxPatientConsent);

        this.TRUE_RESOURCE = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "True");
        this.TRUE_RESOURCE.addProperty(RDFS.label, "True");
        this.TRUE_RESOURCE.addProperty(RDF.type, this.pdxBoolean);

        this.FALSE_RESOURCE = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "False");
        this.FALSE_RESOURCE.addProperty(RDFS.label, "False");
        this.FALSE_RESOURCE.addProperty(RDF.type, this.pdxBoolean);

        this.tumorPrepSolid = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Solid");
        this.tumorPrepSuspension = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Suspension");
        this.tumorPrepAscites = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Ascites");

        this.tumorPrepSolid.addProperty(RDFS.label, "Tumor preperation solid");
        this.tumorPrepSuspension.addProperty(RDFS.label, "Tumor preperation suspension");
        this.tumorPrepAscites.addProperty(RDFS.label, "Tumor preperation ascites");

        this.tumorPrepSolid.addProperty(RDF.type, this.pdxTumorSampleType);
        this.tumorPrepSuspension.addProperty(RDF.type, this.pdxTumorSampleType);
        this.tumorPrepAscites.addProperty(RDF.type, this.pdxTumorSampleType);

        // treatment for engfatement
        this.mouseRxGCSF = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "G-CSF");
        this.mouseRxGCSF.addProperty(RDFS.label, "G-CSF treatment for engraftment");
        this.mouseRxGCSF.addProperty(RDF.type, this.pdxTreatmentForEngraftment);

        this.mouseRxEstrogen = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Estrogen");
        this.mouseRxEstrogen.addProperty(RDFS.label, "Estrogen treatment for engraftment");
        this.mouseRxEstrogen.addProperty(RDF.type, this.pdxTreatmentForEngraftment);

        this.notAssessed = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Not_assessed");
        this.notAssessed.addProperty(RDFS.label, "Not assessed");
        this.notAssessed.addProperty(RDF.type, this.pdxTreatmentResponse);

        this.completeResponse = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Complete_response");
        this.completeResponse.addProperty(RDFS.label, "Complete response");
        this.completeResponse.addProperty(RDF.type, this.pdxTreatmentResponse);

        this.partialResponse = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Partial_response");
        this.partialResponse.addProperty(RDFS.label, "Partial response");
        this.partialResponse.addProperty(RDF.type, this.pdxTreatmentResponse);

        this.stableDisease = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Stable_disease");
        this.stableDisease.addProperty(RDFS.label, "Stable disease");
        this.stableDisease.addProperty(RDF.type, this.pdxTreatmentResponse);

        this.progressiveDisease = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Progressive_disease");
        this.progressiveDisease.addProperty(RDFS.label, "Progressive disease");
        this.progressiveDisease.addProperty(RDF.type, this.pdxTreatmentResponse);

        this.IHC = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "IHC");
        this.IHC.addProperty(RDF.type, this.pdxModelCharacterization);
        this.IHC.addProperty(RDFS.label, "IHC");

        this.histology = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "Histology");
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
        this.consentProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Ethnicity of patient
        this.ethnicityProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEthnicity");
        this.ethnicityProperty.addProperty(RDFS.label, "Patient ethnicity");
        this.ethnicityProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Race of patient
        this.raceProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasRace");
        this.raceProperty.addProperty(RDFS.label, "Patient race");
        this.raceProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Virology status of patient
        this.patientVirolgyStatusProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#patientVirologyStatus");
        this.patientVirolgyStatusProperty.addProperty(RDFS.label, "Patient virology status");
        this.patientVirolgyStatusProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Clinical treatment setting status of patient
        this.hasPatientClinicalTreatmentSettingProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#patientClinicalTreatmentSetting");
        this.hasPatientClinicalTreatmentSettingProperty.addProperty(RDFS.label, "Patient clinical treatment setting");
        this.hasPatientClinicalTreatmentSettingProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Treatment notes for patient
        this.hasPatientTreatmentNotesProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#patientTreatmentNotes");
        this.hasPatientTreatmentNotesProperty.addProperty(RDFS.label, "Patient treatmnet notes");
        this.hasPatientTreatmentNotesProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Patient tumor tissue of origin
        this.hasTissueOfOriginProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTissueOfOrigin");
        this.hasTissueOfOriginProperty.addProperty(RDFS.label, "Tumor's tissue of origin");
        this.hasTissueOfOriginProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Tumor Category
        this.hasTumorCategoryProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorCategory");
        this.hasTumorCategoryProperty.addProperty(RDFS.label, "Tumor category");
        this.hasTumorCategoryProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Histology for tumor
        this.hasTumorHistologyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorHistology");
        this.hasTumorHistologyProperty.addProperty(RDFS.label, "Pathologist's Histologic Diagnosis");
        this.hasTumorHistologyProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        // we don't know what these values will look like
       // this.hasTumorHistologyProperty.addProperty(RDFS.range, this.pdxDiagnosis);

        // Grade of tumor
        this.hasTumorGradeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorGrade");
        this.hasTumorGradeProperty.addProperty(RDFS.label, "Tumor grade");
        this.hasTumorGradeProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Stage of tumor
        this.hasTumorOverallStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasOverallStage");
        this.hasTumorOverallStageProperty.addProperty(RDFS.label, "Tumor overall stage");
        this.hasTumorOverallStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasTStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTStage");
        this.hasTStageProperty.addProperty(RDFS.label, "Tumor T stage");
        this.hasTStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasNStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasNStage");
        this.hasNStageProperty.addProperty(RDFS.label, "Tumor N stage");
        this.hasNStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasMStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMStage");
        this.hasMStageProperty.addProperty(RDFS.label, "Tumor M stage");
        this.hasMStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasTumorSampleTypeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSampleType");
        this.hasTumorSampleTypeProperty.addProperty(RDFS.label, "Tumor sample type");
        this.hasTumorSampleTypeProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // PDX model generated from patient tumor
        this.hasPdxModelProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPdxModel");
        this.hasPdxModelProperty.addProperty(RDFS.label, "PDX model generated from patient tissue");
        this.hasPdxModelProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Mouse strain for PDX model
        this.hasStrainProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStrain");
        this.hasStrainProperty.addProperty(RDFS.label, "Mouse strain used for engraftment");
        this.hasStrainProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Is the mouse strain humanized
        this.hasModelHumanizedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasHumanization");
        this.hasModelHumanizedProperty.addProperty(RDFS.label, "Was model humanized");
        this.hasModelHumanizedProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Type of mouse humanization
        this.hasModelHumanizationTypeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasHumanizationType");
        this.hasModelHumanizationTypeProperty.addProperty(RDFS.label, "Type of mouse humanization");
        this.hasModelHumanizationTypeProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Type of tumor preperation
        this.hasTumorPreparation = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorPreparation");
        this.hasTumorPreparation.addProperty(RDFS.label, "Type of tumor preparation for engraftment");
        this.hasTumorPreparation.addProperty(RDF.type, OWL.DatatypeProperty);

        // Instituion providing mouse
        this.hasStrainSourceProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStrainSource");
        this.hasStrainSourceProperty.addProperty(RDFS.label, "Institution providing strain");
        this.hasStrainSourceProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Treatement of mouse prior to engraftment
        this.hasModelTreatmentForEngraftmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTreatmentForEngraftment");
        this.hasModelTreatmentForEngraftmentProperty.addProperty(RDFS.label, "Model treatment for engraftment");
        this.hasModelTreatmentForEngraftmentProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Percent of successful engraftments 
        this.hasModelEngraftmentPercentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentPercent");
        this.hasModelEngraftmentPercentProperty.addProperty(RDFS.label, "Engraftment percent");
        this.hasModelEngraftmentPercentProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Days for successful engraftment
        this.hasModelEngraftTimeInDaysProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentTimeInDays");
        this.hasModelEngraftTimeInDaysProperty.addProperty(RDFS.label, "Engraftment time in days");
        this.hasModelEngraftTimeInDaysProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasMouseSexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMouseSex");
        this.hasMouseSexProperty.addProperty(RDFS.label, "Mouse sex");
        this.hasMouseSexProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasModelEngraftmentProcedureProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentProcedure");
        this.hasModelEngraftmentProcedureProperty.addProperty(RDFS.label, "Engraftment procedure");
        this.hasModelEngraftmentProcedureProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasModelEngraftmentSiteProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentSite");
        this.hasModelEngraftmentSiteProperty.addProperty(RDFS.label, "Engraftment site");
        this.hasModelEngraftmentSiteProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasModelTreatmentForEngraftmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTreatmentForEngraftment");
        this.hasModelTreatmentForEngraftmentProperty.addProperty(RDFS.label, "Treatment for engraftment");
        this.hasModelTreatmentForEngraftmentProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasPassagedTissueCryopreservedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPassagedTissueCryopreserved");
        this.hasPassagedTissueCryopreservedProperty.addProperty(RDFS.label, "Passaged tissue was cryopreserved");
        this.hasPassagedTissueCryopreservedProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasSublineOfModelProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSublineOfModel");
        this.hasSublineOfModelProperty.addProperty(RDFS.label, "Subline of model");
        this.hasSublineOfModelProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasSublineReasonProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSublineReason");
        this.hasSublineReasonProperty.addProperty(RDFS.label, "Subline reason");
        this.hasSublineReasonProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Tumor Characterization
        this.hasTumorCharacterizationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorCharacterization");
        this.hasTumorCharacterizationProperty.addProperty(RDFS.label, "Tumor Characterization");
        this.hasTumorCharacterizationProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        

        // Tumor is not EBV or mouse tissue
        this.tumorNotEbvNotMouseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasNotEbvNotMouseStatus");
        this.tumorNotEbvNotMouseProperty.addProperty(RDFS.label, "Mouse is not EBV positive, tumor tissue is not mouse origin");
        this.tumorNotEbvNotMouseProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // PDX model response to treatment
        this.hasPdxTumorResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorResponse");
        this.hasPdxTumorResponseProperty.addProperty(RDFS.label, "Tumor Response");
        this.hasPdxTumorResponseProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // The pdx model's health status
        this.hasAnimalHealthStatusSatisfactoryProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSatisfactoryAnimalHealthStatus");
        this.hasAnimalHealthStatusSatisfactoryProperty.addProperty(RDFS.label, "Animal health status is satisfactory");
        this.hasAnimalHealthStatusSatisfactoryProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Passage on which QA was performed
        this.hasPassageQaPerformedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPassageQAperformed");
        this.hasPassageQaPerformedProperty.addProperty(RDFS.label, "Passage QA performed");
        this.hasPassageQaPerformedProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Patient current treatment drug
        this.currentTreatmentDrug = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCurrentTreatmentDrug");
        this.currentTreatmentDrug.addProperty(RDFS.label, "Current patient treatment drug");
        this.currentTreatmentDrug.addProperty(RDF.type, OWL.DatatypeProperty);

        // Lower age range for Patient at primary diagnosis
        this.primaryDiagnosisAgeBinLowerRange = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPrimaryDiagnosisAgeBinLowerRange");
        this.primaryDiagnosisAgeBinLowerRange.addProperty(RDFS.label, "Primary diagnosis age lower range of 5 year bin");
        //this.primaryDiagnosisAgeBinLowerRange.addProperty(RDFS.domain, pdxPatient);
        this.primaryDiagnosisAgeBinLowerRange.addProperty(RDF.type, OWL.DatatypeProperty);

        // Upper age range for Patient when sample was taken
        this.primaryDiagnosisAgeBinUpperRange = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPrimaryDiagnosisAgeBinUpperRange");
        this.primaryDiagnosisAgeBinUpperRange.addProperty(RDFS.label, "Primary diagnosis upper range of 5 year bin");
       // this.primaryDiagnosisAgeBinUpperRange.addProperty(RDFS.domain, pdxPatient);
        this.primaryDiagnosisAgeBinUpperRange.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasPatientTreatmentIndexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentIndex");
        this.hasPatientTreatmentIndexProperty.addProperty(RDFS.label, "Patient treatment index");
        this.hasPatientTreatmentIndexProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasPatientTreatmentRegimen = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentRegimen");
        this.hasPatientTreatmentRegimen.addProperty(RDFS.label, "Patient treatment regimen");
        this.hasPatientTreatmentRegimen.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasPatientTreatmentResponse = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentResponse");
        this.hasPatientTreatmentResponse.addProperty(RDFS.label, "Patient treatment reponse");
        this.hasPatientTreatmentResponse.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPatientTreatmentResponse.addProperty(RDFS.range, this.pdxTreatmentResponse);

        this.hasPatientTreatmentReasonStopped = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentReasonStopped");
        this.hasPatientTreatmentReasonStopped.addProperty(RDFS.label, "Patient treatment reason stopped");
        this.hasPatientTreatmentReasonStopped.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasTumorEventIndexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorEventIndex");
        this.hasTumorEventIndexProperty.addProperty(RDFS.label, "Tumor collection at treatment index");
        this.hasTumorEventIndexProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasTumorTreatmentNaiveProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorTreatmentNaive");
        this.hasTumorTreatmentNaiveProperty.addProperty(RDFS.label, "Treatment naive");
        this.hasTumorTreatmentNaiveProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasPatientAgeAtCollectionProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientAgeAtCollection");
        this.hasPatientAgeAtCollectionProperty.addProperty(RDFS.label, "Patient age at collection");
        this.hasPatientAgeAtCollectionProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasTumorInitialDiagnosisProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorInitialDiagnosis");
        this.hasTumorInitialDiagnosisProperty.addProperty(RDFS.label, "Initial diagnosis");
        this.hasTumorInitialDiagnosisProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasTumorTissueOfOriginProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorTissueOfOrigin");
        this.hasTumorTissueOfOriginProperty.addProperty(RDFS.label, "Tissue of origin");
        this.hasTumorTissueOfOriginProperty.addProperty(RDF.type, OWL.DatatypeProperty);


        this.hasTumorSpecimenTissueProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorSpecimenTissue");
        this.hasTumorSpecimenTissueProperty.addProperty(RDFS.label, "Specimen tissue");
        this.hasTumorSpecimenTissueProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasTumorTissueHistologyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorTissueHistology");
        this.hasTumorTissueHistologyProperty.addProperty(RDFS.label, "Tissue histology");
        this.hasTumorTissueHistologyProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasTumorClinicalMarkersProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorClinicalMarkers");
        this.hasTumorClinicalMarkersProperty.addProperty(RDFS.label, "Diagnostic clinical markers");
        this.hasTumorClinicalMarkersProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyTreatmentDrugProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentDrug");
        this.hasStudyTreatmentDrugProperty.addProperty(RDFS.label, "Study treatment drug");
        this.hasStudyTreatmentDrugProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyTreatmentEndpoint1ResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentEndpoint1Response");
        this.hasStudyTreatmentEndpoint1ResponseProperty.addProperty(RDFS.label, "Study treatment endpoint 1 response");
        this.hasStudyTreatmentEndpoint1ResponseProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyTreatmentEndpoint2ResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentEndpoint2Response");
        this.hasStudyTreatmentEndpoint2ResponseProperty.addProperty(RDFS.label, "Study treatment endpoint 2 response");
        this.hasStudyTreatmentEndpoint2ResponseProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyTreatmentEndpoint3ResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentEndpoint3Response");
        this.hasStudyTreatmentEndpoint3ResponseProperty.addProperty(RDFS.label, "Study treatment endpoint 3 response");
        this.hasStudyTreatmentEndpoint3ResponseProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyTreatmentDoseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentDose");
        this.hasStudyTreatmentDoseProperty.addProperty(RDFS.label, "Study treatment dose");
        this.hasStudyTreatmentDoseProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyTreatmentRouteProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentRoute");
        this.hasStudyTreatmentRouteProperty.addProperty(RDFS.label, "Study treatment route");
        this.hasStudyTreatmentRouteProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyTreatmentFrequencyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentFrequency");
        this.hasStudyTreatmentFrequencyProperty.addProperty(RDFS.label, "Study treatment frequency");
        this.hasStudyTreatmentFrequencyProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyTreatmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatment");
        this.hasStudyTreatmentProperty.addProperty(RDFS.label, "Study treatment");
        this.hasStudyTreatmentProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyHasMetastasisProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMetastasis");
        this.hasStudyHasMetastasisProperty.addProperty(RDFS.label, "Study model has metastasis");
        this.hasStudyHasMetastasisProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyMetastasisLocationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMetastasisLocation");
        this.hasStudyMetastasisLocationProperty.addProperty(RDFS.label, "Model metastasis location");
        this.hasStudyMetastasisLocationProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyMetastasisPassageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMetastasisPassage");
        this.hasStudyMetastasisPassageProperty.addProperty(RDFS.label, "Model metastasis passage");
        this.hasStudyMetastasisPassageProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // for omics files
        this.hasOmicsFile = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasOmicsFile");
        this.hasOmicsFile.addProperty(RDFS.label, "Has associated OMICS file");
        this.hasOmicsFile.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasOmicsFile.addProperty(RDFS.range, this.pdxOmicsFile);

        this.hasPatientAssociation = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientAssociation");
        this.hasPatientAssociation.addProperty(RDFS.label, "Has associated patient");
        this.hasPatientAssociation.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasPatientAssociation.addProperty(RDFS.range, this.pdxPatient);

        this.hasModelAssociation = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasModelAssociation");
        this.hasModelAssociation.addProperty(RDFS.label, "Has associated model");
        this.hasModelAssociation.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasModelAssociation.addProperty(RDFS.range, this.pdxModelCreation);

        this.hasAccessLevel = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasAccessLevel");
        this.hasAccessLevel.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasAccessLevel.addProperty(RDFS.label, "Access level");

        this.hasCreatedDateTime = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCreatedDateTime");
        this.hasCreatedDateTime.addProperty(RDFS.label, "Created date and time");
        this.hasCreatedDateTime.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasDataCategory = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDataCategory");
        this.hasDataCategory.addProperty(RDFS.label, "Data category");
        this.hasDataCategory.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasDataFormat = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDataFormat");
        this.hasDataFormat.addProperty(RDFS.label, "Data format");
        this.hasDataFormat.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasDataType = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDataType");
        this.hasDataType.addProperty(RDFS.label, "Data type");
        this.hasDataType.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasSampleType = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSampleType");
        this.hasSampleType.addProperty(RDFS.label, "Sample type");
        this.hasSampleType.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasExperimentalStrategy = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasExperimentalStrategy");
        this.hasExperimentalStrategy.addProperty(RDFS.label, "Experimental strategy");
        this.hasExperimentalStrategy.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasFileSize = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFileSize");
        this.hasFileSize.addProperty(RDFS.label, "File size");
        this.hasFileSize.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasPlatform = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPlatform");
        this.hasPlatform.addProperty(RDFS.label, "Platform");
        this.hasPlatform.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasCaptureKit = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCaptureKit");
        this.hasCaptureKit.addProperty(RDFS.label, "Capture kit");
        this.hasCaptureKit.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasUpdatedDateTime = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasUpdateDateTime");
        this.hasUpdatedDateTime.addProperty(RDFS.label, "Update date and time");
        this.hasUpdatedDateTime.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasFFPEPairedEnd = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFFPEPairedEnd");
        this.hasFFPEPairedEnd.addProperty(RDFS.label, "FFPE paired end");
        this.hasFFPEPairedEnd.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasFileName = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFileName");
        this.hasFileName.addProperty(RDFS.label, "File name");
        this.hasFileName.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasPassage = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasModelPassage");
        this.hasPassage.addProperty(RDFS.label, "Model passage");
        this.hasPassage.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasQAMethodProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasQAMethod");
        this.hasQAMethodProperty.addProperty(RDFS.label, "QA method");
        this.hasQAMethodProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasQAResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasQAResult");
        this.hasQAResultProperty.addProperty(RDFS.label, "QA result");
        this.hasQAResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasQAPassProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasQAPass");
        this.hasQAPassProperty.addProperty(RDFS.label, "QA passed");
        this.hasQAPassProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasQAProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasQA");
        this.hasQAProperty.addProperty(RDFS.label, "Quality assurance");
        this.hasQAProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasQAProperty.addProperty(RDFS.range, this.pdxQualityAssurance);

        this.hasAnimalHealthStatusProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasAnimalHealthStatus");
        this.hasAnimalHealthStatusProperty.addProperty(RDFS.label, "Animal health status");
        this.hasAnimalHealthStatusProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        
        
       
        
        this.hasClinicalEventPointProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasClinicalEventPoint");
        this.hasClinicalEventPointProperty.addProperty(RDFS.label,"Clinical event point");
        this.hasClinicalEventPointProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasCollectionProcedureProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasCollectionProcedure");
        this.hasCollectionProcedureProperty.addProperty(RDFS.label,"Collection procedure");
        this.hasCollectionProcedureProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasDiseaseProgressionTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasDiseaseProgressionTerm");
        this.hasDiseaseProgressionTermProperty.addProperty(RDFS.label,"Disease progression term");
        this.hasDiseaseProgressionTermProperty.addProperty(RDF.type, OWL.DatatypeProperty);


        this.hasSpecimenTissueTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasSpecimenTissueTerm");
        this.hasSpecimenTissueTermProperty.addProperty(RDFS.label,"Specimen tissue term");
        this.hasSpecimenTissueTermProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.hasStrAnalysisProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasSTRAnalysis");
        this.hasStrAnalysisProperty.addProperty(RDFS.label,"STR analysis");
        this.hasStrAnalysisProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStrEvaluationProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasSTREvaluation");
        this.hasStrEvaluationProperty.addProperty(RDFS.label,"STR evaluation");
        this.hasStrEvaluationProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStrMarkersProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasSTRMarkers");
        this.hasStrMarkersProperty.addProperty(RDFS.label,"STR markers");
        this.hasStrMarkersProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasCryopreservedBeforeEngraftmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasCryopreservedBeforeEngraftment");
        this.hasCryopreservedBeforeEngraftmentProperty.addProperty(RDFS.label,"Cryopreserved before engraftment");
        this.hasCryopreservedBeforeEngraftmentProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasDoublingTimeProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasDoublingTime");
        this.hasDoublingTimeProperty.addProperty(RDFS.label,"Doubling time");
        this.hasDoublingTimeProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasEngraftmentMaterialProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasEngraftmentMaterial");
        this.hasEngraftmentMaterialProperty.addProperty(RDFS.label,"Engraftment material");
        this.hasEngraftmentMaterialProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasHumanizedProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasHumanized");
        this.hasHumanizedProperty.addProperty(RDFS.label,"Humanized");
        this.hasHumanizedProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasMacroMetastasisRequiresExcisionProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasMacroMetastasisRequiresExcision");
        this.hasMacroMetastasisRequiresExcisionProperty.addProperty(RDFS.label,"Macro metastasis requires excision");
        this.hasMacroMetastasisRequiresExcisionProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasMetastastaticSitesProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasMetastastaticSite");
        this.hasMetastastaticSitesProperty.addProperty(RDFS.label,"Metastastatic site");
        this.hasMetastastaticSitesProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasViablyCryopreseredProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasViablyCryopresered");
        this.hasViablyCryopreseredProperty.addProperty(RDFS.label,"Viably cryopresered");
        this.hasViablyCryopreseredProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasPdtcProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasPDTC");
        this.hasPdtcProperty.addProperty(RDFS.label,"PDTC");
        this.hasPdtcProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasCd45IHCProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasCD45IHC");
        this.hasCd45IHCProperty.addProperty(RDFS.label,"CD45 IHC");
        this.hasCd45IHCProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasCd45IHCAssayResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasCD45IHCAssayResult");
        this.hasCd45IHCAssayResultProperty.addProperty(RDFS.label,"CD45 IHC assay result");
        this.hasCd45IHCAssayResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasClinicalDiagnosticMarkerAssayResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasClinicalDiagnosticMarkerAssayResult");
        this.hasClinicalDiagnosticMarkerAssayResultProperty.addProperty(RDFS.label,"Clinical diagnostic marker assay result");
        this.hasClinicalDiagnosticMarkerAssayResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasClinicalDiagnosticMarkerNotesProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasClinicalDiagnosticMarkerNotes");
        this.hasClinicalDiagnosticMarkerNotesProperty.addProperty(RDFS.label,"Clinical diagnostic marker notes");
        this.hasClinicalDiagnosticMarkerNotesProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasClinicalDiagnosticMarkersProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasClinicalDiagnosticMarkers");
        this.hasClinicalDiagnosticMarkersProperty.addProperty(RDFS.label,"Clinical diagnostic markers");
        this.hasClinicalDiagnosticMarkersProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasEbvTranscriptDetectionProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasEbvTranscriptDetection");
        this.hasEbvTranscriptDetectionProperty.addProperty(RDFS.label,"EBV transcript detection");
        this.hasEbvTranscriptDetectionProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasEbvTranscriptDetectionResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasEbvTranscriptDetectionResult");
        this.hasEbvTranscriptDetectionResultProperty.addProperty(RDFS.label,"EBV transcript detection result");
        this.hasEbvTranscriptDetectionResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasHumanSpecificCytokeratin19Property = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasHumanSpecificCytokeratin19");
        this.hasHumanSpecificCytokeratin19Property.addProperty(RDFS.label,"Human specific cytokeratin 19");
        this.hasHumanSpecificCytokeratin19Property.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasMousePathogenStatusProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasMousePathogenStatus");
        this.hasMousePathogenStatusProperty.addProperty(RDFS.label,"Mouse pathogen status");
        this.hasMousePathogenStatusProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasOverallEvaluationProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasOverallEvaluation");
        this.hasOverallEvaluationProperty.addProperty(RDFS.label,"Overall evaluation");
        this.hasOverallEvaluationProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasPanCytokeratinAssayResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasPanCytokeratinAssayResult");
        this.hasPanCytokeratinAssayResultProperty.addProperty(RDFS.label,"PanCytokeratin assay result");
        this.hasPanCytokeratinAssayResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStrNotesProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasSTRNotes");
        this.hasStrNotesProperty.addProperty(RDFS.label,"STR notes");
        this.hasStrNotesProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasCohortProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasCohort");
        this.hasCohortProperty.addProperty(RDFS.label,"Cohort");
        this.hasCohortProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasCohortSizeProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasCohortSize");
        this.hasCohortSizeProperty.addProperty(RDFS.label,"Cohort size");
        this.hasCohortSizeProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasDosingScheduleProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasDosingSchedule");
        this.hasDosingScheduleProperty.addProperty(RDFS.label,"Dosing schedule");
        this.hasDosingScheduleProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        

        this.hasNumberOfCyclesProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasNumberOfCycles");
        this.hasNumberOfCyclesProperty.addProperty(RDFS.label,"Number of cycles");
        this.hasNumberOfCyclesProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        this.hasStudyDurationProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"#hasStudyDuration");
        this.hasStudyDurationProperty.addProperty(RDFS.label,"Study duration");
        this.hasStudyDurationProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        

        rdfModel.setNsPrefix("PDXNET", PDXNET_NAMESPACE);
        rdfModel.setNsPrefix("NCIT", NCIT_NAMESPACE);
        rdfModel.setNsPrefix("UBERON", UBERON_NAMESPACE);

    }

}
