package org.jax.pdxintegrator.rdf;


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
import java.util.Calendar;
import java.util.List;
import org.apache.jena.shared.BadURIException;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.jax.pdxintegrator.model.modelstudy.PdxModelStudy;
import org.jax.pdxintegrator.model.modelstudy.PdxStudyTreatment;
import org.jax.pdxintegrator.model.omicsfile.PdxOmicsFile;
import org.jax.pdxintegrator.model.patient.PdxPatientTreatment;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.COMPLETE_RESPONSE;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.NOT_ASSESSED;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.PARTIAL_RESPONSE;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.PROGRESSIVE_DISEASE;
import static org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment.STABLE_DISEASE;
import org.jax.pdxintegrator.model.tumor.TumorGrade;
import org.monarchinitiative.phenol.ontology.data.TermId;

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

    private final TermId male = TermId.of("NCIT:C20197");
    private final TermId female = TermId.of("NCIT:C16576");

    // RDF properties needed throughout the model
    private Property hasPatientIdProperty;
    private Property hasSubmitterTumorIdProperty;

    private Property hasTumorProperty;
   
    private Property hasTumorGradeProperty;
    private Property hasTStageProperty;
    private Property hasMStageProperty;
    private Property hasNStageProperty;
    private Property hasPdxModelProperty;
    private Property hasStrainProperty;
    private Property hasStrainSourceProperty;
    private Property hasModelEngraftmentProcedureProperty;
    private Property hasModelEngraftmentSiteProperty;
    private Property hasModelHumanizedProperty;
    private Property hasModelHumanizationTypeProperty;
    private Property hasTumorPreparation;
    private Property hasModelTreatmentForEngraftmentProperty;
    private Property hasModelEngraftmentPercentProperty;
    private Property hasModelEngraftTimeInDaysProperty;
    private Property hasModelHistologyProperty;
    private Property hasModelHistologyTermProperty;

    private Property hasPassagedTissueCryopreservedProperty;
    private Property hasSublineOfModelProperty;
    private Property hasSublineReasonProperty;

    private Property hasTumorCharacterizationProperty;
    private Property tumorNotEbvNotMouseProperty;
    private Property hasPdxTumorResponseProperty;
    private Property currentTreatmentDrug;
    private Property primaryDiagnosisAgeBinLowerRange;
    private Property primaryDiagnosisAgeBinUpperRange;

    private Property hasSexProperty;
    private Property hasConsentProperty;

    private Property hasEthnicityProperty;
    private Property hasRaceProperty;
    private Property hasPatientVirolgyStatusProperty;

    private Property hasStudyTreatmentProperty;

    private Property hasPatientProperty;
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
    private Property hasStudyTreatmentEndpoint1ResponseProperty;
    private Property hasStudyTreatmentEndpoint2ResponseProperty;
    private Property hasStudyTreatmentEndpoint3ResponseProperty;

    private Property hasImplantationSiteProperty;
    private Property hasBaselineTumorTargetSizeProperty;
    private Property hasStudyDescriptionProperty;
    private Property hasStudyHostStrainProperty;

    private Property hasTumorEventIndexProperty;
    private Property hasTumorTreatmentNaiveProperty;
    private Property hasPatientAgeAtCollectionProperty;
    private Property hasTumorInitialDiagnosisProperty;
    private Property hasTumorInitialDiagnosisTermProperty;
    private Property hasTumorTissueOfOriginProperty;
    private Property hasTumorTissueOfOriginTermProperty;
    private Property hasTumorSpecimenTissueProperty;
    private Property hasTumorSpecimenTissueTermProperty;
    private Property hasTumorTissueHistologyProperty;
    private Property hasTumorTissueHistologyTermProperty;
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

    // for files
    private Property hasFile;
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
    private Property hasFFPE;
    private Property hasPairedEnd;
    private Property hasFileName;

    private Property hasPassage;

    private Property hasClinicalEventPointProperty;
    private Property hasClinicalEventPointTermProperty;
    private Property hasCollectionProcedureProperty;

    private Property hasMetastaticSitesProperty;
    private Property hasMetastaticSiteTermProperty;
    private Property hasMetastasisProperty;

 
    private Property hasStrAnalysisProperty;
    private Property hasStrEvaluationProperty;
    private Property hasStrMarkersProperty;
    private Property hasCryopreservedBeforeEngraftmentProperty;
    private Property hasDoublingTimeProperty;
    private Property hasEngraftmentMaterialProperty;

    private Property hasMacroMetastasisRequiresExcisionProperty;

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
    private Resource academicConsent;
    
    private Literal trueLiteral;
    private Literal falseLiteral;
    

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
    private Resource notAssessed, completeResponse, partialResponse, stableDisease, progressiveDisease,GX,G1,G2,G3,G4;

    /*
        For file access levels
     */
    private Resource controlledAccess;
    private Resource openAccess;
    
    // For OMICS file data format
    private Resource fastqGZ, fastq, bam, cram, md5;
    
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
    private OntClass pdxFile;
    

    private OntClass pdxDiagnosis;
    private OntClass pdxSex;
    private OntClass pdxTreatmentResponse;
    private OntClass pdxTumorSampleType;
    private OntClass pdxModelCharacterization;
    private OntClass pdxFileAccessLevel;

    private OntClass pdxPatientConsent;
    private OntClass pdxTreatmentForEngraftment;
    private OntClass pdxPassageQaPerformed;
    private OntClass pdxTissueOfOrigin;
    private OntClass pdxSpecimenTissue;
    private OntClass pdxTumorCategory;
    private OntClass pdxTumorGrade;
    private OntClass pdxTumorStage;
    private OntClass pdxTumorHistology;
    private OntClass pdxMetastaticSite;
    private OntClass pdxDataFormat;

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

        String pdxFileURI = String.format("%s%s", PDXNET_NAMESPACE, "#File");
        this.pdxFile = rdfModel.createClass(pdxFileURI);
        this.pdxFile.addProperty(RDFS.label, "File");

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

        String pdxPassageQaPerformedURI = String.format("%s%s", PDXNET_NAMESPACE, "#PassageQAPerformed");
        this.pdxPassageQaPerformed = rdfModel.createClass(pdxPassageQaPerformedURI);
        this.pdxPassageQaPerformed.addProperty(RDFS.label, "Passage QA performed");

        String pdxTissueOfOriginURI = String.format("%s%s", PDXNET_NAMESPACE, "#TissueOfOrigin");
        this.pdxTissueOfOrigin = rdfModel.createClass(pdxTissueOfOriginURI);
        this.pdxTissueOfOrigin.addProperty(RDFS.label, "Tissue of origin");

        String pdxSpecimenTissueURI = String.format("%s%s", PDXNET_NAMESPACE, "#SpecimenTissue");
        this.pdxSpecimenTissue = rdfModel.createClass(pdxSpecimenTissueURI);
        this.pdxSpecimenTissue.addProperty(RDFS.label, "Specimen tissue");

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

        String pdxMetasticSiteURI = String.format("%s%s", PDXNET_NAMESPACE, "#MetastaticSite");
        this.pdxMetastaticSite = rdfModel.createClass(pdxMetasticSiteURI);
        this.pdxMetastaticSite.addProperty(RDFS.label, "Metastatic site");

        String pdxFileAccessLevelURI = String.format("%s%s", PDXNET_NAMESPACE, "#FileAccessLevel");
        this.pdxFileAccessLevel = rdfModel.createClass(pdxFileAccessLevelURI);
        this.pdxFileAccessLevel.addProperty(RDFS.label, "File access level");
        
        String pdxDataFormatURI = String.format("%s%s", PDXNET_NAMESPACE, "#DataFormat");
        this.pdxDataFormat = rdfModel.createClass(pdxDataFormatURI);
        this.pdxDataFormat.addProperty(RDFS.label, "Data Format");

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

        outputFileRDF(pdxModel);
    }

    private void outputPatientRDF(PdxModel pdxmodel) {
        PdxPatient patient = pdxmodel.getPatient();

        String patientURI = String.format("%s/%s", PDXNET_NAMESPACE + "/patient", patient.getSubmitterPatientID());

        Resource patientResource
                = rdfModel.createResource(patientURI)
                .addProperty(RDFS.label, patient.getSubmitterPatientID())
                .addProperty(RDF.type, this.pdxPatient)
                .addProperty(hasPatientIdProperty, patient.getSubmitterPatientID());

        Resource consent = patient.getConsent().equals(Consent.YES) ? yesConsent : noConsent;
        consent.addProperty(RDF.type, this.pdxPatientConsent);
        patientResource.addProperty(hasConsentProperty, consent);

        //TODO: Race and Ethnicity should be typed
        setProperty(patientResource, hasEthnicityProperty, patient.getEthnicity().getEthnicityString());

        setProperty(patientResource, hasRaceProperty, patient.getRace().getRaceString());

        setProperty(patientResource, hasPatientVirolgyStatusProperty, patient.getVirologyStatus());

        setProperty(patientResource, hasPatientClinicalTreatmentSettingProperty, patient.getClinicalTreatmentSetting());

        setProperty(patientResource, hasPatientTreatmentNotesProperty, patient.getTreatmentNotes());

        try {
            Resource sex = patient.getSex().equals(Sex.FEMALE) ? femaleSex : maleSex;
            patientResource.addProperty(hasSexProperty, sex);
        } catch (Exception e) {// no sex provided don't add}

        }

        patientResource.addProperty(primaryDiagnosisAgeBinLowerRange,
                ResourceFactory.createTypedLiteral(String.valueOf(patient.getAgeAtDiagnosis().getLower()),
                        XSDDatatype.XSDinteger));
        patientResource.addProperty(primaryDiagnosisAgeBinUpperRange,
                ResourceFactory.createTypedLiteral(String.valueOf(patient.getAgeAtDiagnosis().getUpper()),
                        XSDDatatype.XSDinteger));

        if (patient.getPatientTreatments() != null) {
            for (PdxPatientTreatment ptTreatment : patient.getPatientTreatments()) {
                Integer eventIndex = 0;

                // should probably use the index supplied if there is one?
                // would need to verify index count matches number of treatment records
                if (ptTreatment.getEventIndex() != null && ptTreatment.getEventIndex().trim().length() > 0) {
                    try {
                        eventIndex = new Integer(ptTreatment.getEventIndex());
                    } catch (NumberFormatException nfe) {
                        System.out.println("cant convert event index " + ptTreatment.getEventIndex() + " to an integer");
                    }
                }

                String id = eventIndex + ptTreatment.getRegimen() + ptTreatment.getResponse();
                id = Math.abs(id.hashCode()) + "";

                Resource treatment = rdfModel.createResource(PDXNET_NAMESPACE + "/" + patient.getSubmitterPatientID() + "/patienttreatment/" + id);
                treatment.addProperty(RDF.type, this.pdxPatientTreatment);
                treatment.addProperty(RDFS.label, ptTreatment.getRegimen());
                setProperty(treatment, hasPatientTreatmentRegimen, ptTreatment.getRegimen());

                setProperty(treatment, hasPatientTreatmentResponse, ptTreatment.getResponse());
                setProperty(treatment, hasPatientTreatmentReasonStopped, ptTreatment.getReasonStopped());

                treatment.addProperty(hasPatientTreatmentIndexProperty,
                        ResourceFactory.createTypedLiteral(String.valueOf(eventIndex), XSDDatatype.XSDinteger));
                treatment.addProperty(hasPatientProperty, patientResource);

                patientResource.addProperty(hasPatientTreatmentProperty, treatment);
            }
        }
    }

    private void outputTumorRDF(ArrayList<PdxClinicalTumor> clinTumors) {

        for (PdxClinicalTumor clintumor : clinTumors) {

            Resource tumorSample = rdfModel.createResource(String.format("%s/%s", PDXNET_NAMESPACE + "/tumor", clintumor.getSubmitterTumorID()));

            tumorSample.addProperty(hasSubmitterTumorIdProperty, clintumor.getSubmitterTumorID());

            try{
                setProperty(tumorSample, hasTumorEventIndexProperty, new Integer(clintumor.getEventIndex()));
            }catch(NumberFormatException e){
                System.out.println("can't convert event index "+clintumor.getEventIndex()+" to an integer");
            }
            

            setProperty(tumorSample, hasTumorTreatmentNaiveProperty, clintumor.isTreatmentNaive());

            setProperty(tumorSample, hasTumorTreatmentNaiveProperty, clintumor.isTreatmentNaive());

            try{
                // this should be binned.
                setProperty(tumorSample, hasPatientAgeAtCollectionProperty, new Integer(clintumor.getAgeAtCollection()));
            }catch(NumberFormatException e){
                System.out.println("can't convert age at collection "+clintumor.getAgeAtCollection()+" to an integer");
            }

            setProperty(tumorSample, hasTumorInitialDiagnosisProperty, clintumor.getInitialDiagnosis());

            try {
 
                Resource diagnosisResource = rdfModel.createResource(String.format("%s%s", NCIT_NAMESPACE, clintumor.getInitialDiagnosisTerm().getId()));
                diagnosisResource.addProperty(RDF.type, this.pdxDiagnosis);
                tumorSample.addProperty(hasTumorInitialDiagnosisTermProperty, diagnosisResource);
            } catch (NullPointerException npe) {
                // no diagnosis term
            }

            setProperty(tumorSample, hasTumorTissueOfOriginProperty, clintumor.getTissueOfOrigin());

            try {
                Resource tissueResource = rdfModel.createResource(UBERON_NAMESPACE + clintumor.getTissueOfOriginTerm().getId());
                tissueResource.addProperty(RDF.type, this.pdxTissueOfOrigin);
                tumorSample.addProperty(hasTumorTissueOfOriginTermProperty, tissueResource);
            } catch (NullPointerException npe) {
                // no tissue term
            }

            setProperty(tumorSample, hasTumorSpecimenTissueProperty, clintumor.getSpecimenTissue());
            try {
                Resource tissueResource = rdfModel.createResource(UBERON_NAMESPACE + clintumor.getSpecimenTissueTerm().getId());
                tissueResource.addProperty(RDF.type, this.pdxSpecimenTissue);
                tumorSample.addProperty(hasTumorSpecimenTissueTermProperty, tissueResource);
            } catch (NullPointerException npe) {
                // no tissue term
            }

            setProperty(tumorSample, hasTumorTissueHistologyProperty, clintumor.getTissueHistology());
            try {
                Resource histologyTerm = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getTissueHistologyTerm().getId());
                histologyTerm.addProperty(RDF.type, this.pdxTumorHistology);
                tumorSample.addProperty(hasTumorTissueHistologyTermProperty, histologyTerm);
            } catch (NullPointerException npe) {
                // no histology term
            }

            if (clintumor.getClinicalMarkers() != null && clintumor.getClinicalMarkers().contains(";")) {
                String[] sites = clintumor.getClinicalMarkers().split(";");
                for (String site : sites) {
                    setProperty(tumorSample, hasTumorClinicalMarkersProperty, site);
                }
            } else {
                setProperty(tumorSample, hasTumorClinicalMarkersProperty, clintumor.getClinicalMarkers());
            }
            setProperty(tumorSample, hasTStageProperty, clintumor.getTStage());

            setProperty(tumorSample, hasMStageProperty, clintumor.getMStage());

            setProperty(tumorSample, hasNStageProperty, clintumor.getNStage());

            setProperty(tumorSample, hasTumorGradeProperty, clintumor.getTumorGrade());
           

            setProperty(tumorSample, hasTumorOverallStageProperty, clintumor.getOverallStage());

            // there are terms for this tumorPrepAscites etc...
            setProperty(tumorSample, hasTumorSampleTypeProperty, clintumor.getSampleType());

            setProperty(tumorSample, hasClinicalEventPointProperty, clintumor.getClinicalEventPoint());
            // need term
            //setProperty(tumorSample,hasClinicalEventPointTermProperty,clintumor.getClinicalEventPointTerm());

            setProperty(tumorSample, hasCollectionProcedureProperty, clintumor.getCollectionProcedure());

            if (clintumor.getMetastaticSites() != null && clintumor.getMetastaticSites().contains(";")) {
                String[] sites = clintumor.getMetastaticSites().split(";");
                for (String site : sites) {
                    setProperty(tumorSample, hasMetastaticSitesProperty, site.trim());
                }
            } else {
                setProperty(tumorSample, hasMetastaticSitesProperty, clintumor.getMetastaticSites());
            }

            if (clintumor.getMetastaticSiteTerms() != null) {
                for (TermId site : clintumor.getMetastaticSiteTerms()) {
                    try {
                        Resource tissueResource = rdfModel.createResource(UBERON_NAMESPACE + site.getId());
                        tissueResource.addProperty(RDF.type, this.pdxMetastaticSite);
                        tumorSample.addProperty(hasMetastaticSiteTermProperty, tissueResource);
                    } catch (NullPointerException npe) {
                        // no term
                    }
                    //    
                }
            }

            setProperty(tumorSample, hasStrAnalysisProperty, clintumor.getStrAnalysis());
            setProperty(tumorSample, hasStrEvaluationProperty, clintumor.getStrEvaluation());
            setProperty(tumorSample, hasStrMarkersProperty, clintumor.getStrMarkers());

            tumorSample.addProperty(RDF.type, this.pdxClinicalTumor);
            tumorSample.addProperty(RDFS.label, clintumor.getSubmitterTumorID());

            // bi directional
            tumorSample.addProperty(hasPatientProperty, rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE + "/patient", clintumor.getPatientID())));
            System.out.println(tumorSample.getURI() + " has patient " + String.format("%s/%s", PDXNET_NAMESPACE + "/patient", clintumor.getPatientID()));
            rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE + "/patient", clintumor.getPatientID())).addProperty(this.hasTumorProperty, tumorSample);

        }
    }

    private void outputModelStudyRDF(PdxModel model) {
        ArrayList<PdxModelStudy> modelStudies = model.getModelStudy();

        for (PdxModelStudy modelStudy : modelStudies) {
            Resource thisModelStudy = rdfModel.createResource(PDXNET_NAMESPACE + "/study/" + modelStudy.getStudyID());
            Resource modelCreation = rdfModel.getResource(PDXNET_NAMESPACE + "/" + modelStudy.getModelID());

            thisModelStudy.addProperty(hasPdxModelProperty, modelCreation);

            thisModelStudy.addProperty(RDF.type, this.pdxModelStudy);
            thisModelStudy.addProperty(RDFS.label, "Model study for " + modelStudy.getModelID());

            setProperty(thisModelStudy, hasBaselineTumorTargetSizeProperty, modelStudy.getBaselineTumorTargetSize());
            setProperty(thisModelStudy, hasStudyDescriptionProperty, modelStudy.getDescription());
            setProperty(thisModelStudy, hasStudyHostStrainProperty, modelStudy.getHostStrain());
            setProperty(thisModelStudy, hasImplantationSiteProperty, modelStudy.getImplantationSite());

            rdfModel.getResource(PDXNET_NAMESPACE + "/" + modelStudy.getModelID()).addProperty(hasStudyProperty, thisModelStudy);

            for (PdxStudyTreatment studyTreatment : modelStudy.getTreatments()) {

                String cohort = "";
                if (studyTreatment.getCohort() != null) {
                    cohort = studyTreatment.getCohort();
                }
                String id = modelStudy.getModelID() + cohort + studyTreatment.getDrug() + studyTreatment.getDose();
                id = Math.abs(id.hashCode()) + "";

                Resource treatment = rdfModel.createResource(PDXNET_NAMESPACE + "/study/" + modelStudy.getStudyID() + "/treatment/" + id);
                setProperty(treatment, hasStudyTreatmentDrugProperty, studyTreatment.getDrug());
                setProperty(treatment, hasStudyTreatmentDoseProperty, studyTreatment.getDose());
                setProperty(treatment, hasStudyTreatmentRouteProperty, studyTreatment.getRoute());

                setProperty(treatment, hasCohortProperty, studyTreatment.getCohort());
                setProperty(treatment, hasCohortSizeProperty, studyTreatment.getCohortSize());
                setProperty(treatment, hasDosingScheduleProperty, studyTreatment.getDosingSchedule());

                setProperty(treatment, hasStudyTreatmentEndpoint1ResponseProperty, studyTreatment.getEndpoint1Response());
                setProperty(treatment, hasStudyTreatmentEndpoint2ResponseProperty, studyTreatment.getEndpoint2Response());
                setProperty(treatment, hasStudyTreatmentEndpoint3ResponseProperty, studyTreatment.getEndpoint3Response());
                setProperty(treatment, hasNumberOfCyclesProperty, studyTreatment.getNumberOfCycles());
                setProperty(treatment, hasStudyDurationProperty, studyTreatment.getStudyDuration());
               

                treatment.addProperty(hasStudyProperty, thisModelStudy);

                treatment.addProperty(RDF.type, this.pdxStudyTreatment);

                String label = studyTreatment.getDrug() + " " + studyTreatment.getDose();

                if ((studyTreatment.getDrug().contains("+") || studyTreatment.getDrug().contains(";")) && studyTreatment.getDose().contains(";")) {
                    String[] drugs = studyTreatment.getDrug().split("\\+|;");
                    String[] doses = studyTreatment.getDose().split(";");
                    label = drugs[0] + " " + doses[0] + "," + drugs[1] + " " + doses[1];
                }

                treatment.addProperty(RDFS.label, label);

                thisModelStudy.addProperty(hasStudyTreatmentProperty, treatment);
                // new for BCM... does this work?
                rdfModel.getResource(PDXNET_NAMESPACE + "/" + modelStudy.getModelID()).addProperty(hasStudyTreatmentProperty, treatment);

            }

        }
    }

    private void outputModelCreationRdf(PdxModel model) {
        ArrayList<PdxModelCreation> mcreations = model.getModelCreation();
        for (PdxModelCreation mcreation : mcreations) {

            Resource modelCreation = rdfModel.createResource(PDXNET_NAMESPACE + "/" + mcreation.getModelID());

            Resource tumorSample = rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE + "/tumor", mcreation.getTumorID()));

            tumorSample.addProperty(hasPdxModelProperty, modelCreation);
            modelCreation.addProperty(hasTumorProperty, tumorSample);
            setProperty(modelCreation, hasStrainProperty, mcreation.getMouseStrain());

            setProperty(modelCreation, hasStrainSourceProperty, mcreation.getMouseSource());

            try {
                Resource sex = mcreation.getMouseSex().equals(Sex.FEMALE) ? femaleSex : maleSex;
                modelCreation.addProperty(hasSexProperty, sex);
            } catch (Exception e) {
                // no sex provided don't add
            }

            if (mcreation.isHumanized() != null && mcreation.isHumanized()) {
                modelCreation.addProperty(hasModelHumanizedProperty, trueLiteral);
                modelCreation.addProperty(hasModelHumanizationTypeProperty, mcreation.getHumanizationType());
            } else {
                modelCreation.addProperty(hasModelHumanizedProperty, falseLiteral);
            }

            setProperty(modelCreation, hasModelEngraftmentProcedureProperty, mcreation.getEngraftmentProcedure());

            setProperty(modelCreation, hasModelEngraftmentSiteProperty, mcreation.getEngraftmentSite());

            setProperty(modelCreation, hasModelTreatmentForEngraftmentProperty, mcreation.getTreatmentForEngraftment());

            // Carol always calls this concordance.... ie is the passaged/engrafted tissue in concordance with pt tissue based on histologic findings
            setProperty(modelCreation, hasModelHistologyProperty, mcreation.getModelHistology());
            //need term

            setProperty(modelCreation, hasSublineOfModelProperty, mcreation.getSublineOfModel());

            setProperty(modelCreation, hasSublineReasonProperty, mcreation.getSublineReason());

            if (mcreation.getEngraftmentRate() != null) {
                modelCreation.addProperty(hasModelEngraftmentPercentProperty,
                        ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentRate()),
                                XSDDatatype.XSDdouble));
            }

            if (mcreation.getEngraftmentTime() != null) {
                modelCreation.addProperty(hasModelEngraftTimeInDaysProperty,
                        ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentTime()),
                                XSDDatatype.XSDinteger));
            }
            
            
            setProperty(modelCreation,hasCryopreservedBeforeEngraftmentProperty,mcreation.getCryopreservedBeforeEngraftment());
            setProperty(modelCreation,hasDoublingTimeProperty,mcreation.getDoublingTime());
            setProperty(modelCreation,hasEngraftmentMaterialProperty,mcreation.getEngraftmentMaterial());
            
            setProperty(modelCreation,hasMacroMetastasisRequiresExcisionProperty,mcreation.getMacroMetastasisRequiresExcision());
            
            setProperty(modelCreation,hasViablyCryopreseredProperty,mcreation.getViablyCryopresered());


            setProperty(modelCreation, hasCryopreservedBeforeEngraftmentProperty, mcreation.getCryopreservedBeforeEngraftment());
            setProperty(modelCreation, hasDoublingTimeProperty, mcreation.getDoublingTime());
            setProperty(modelCreation, hasEngraftmentMaterialProperty, mcreation.getEngraftmentMaterial());

            setProperty(modelCreation, hasMacroMetastasisRequiresExcisionProperty, mcreation.getMacroMetastasisRequiresExcision());

            setProperty(modelCreation, this.hasMetastasisProperty, mcreation.getMetastasis());
            

            if (mcreation.getMetastaticSites() != null && mcreation.getMetastaticSites().contains(";")) {
                String[] sites = mcreation.getMetastaticSites().split(";");
                for (String site : sites) {
                    setProperty(modelCreation, hasMetastaticSitesProperty, site.trim());
                }
            } else {
                setProperty(modelCreation, hasMetastaticSitesProperty, mcreation.getMetastaticSites());
            }

            for (TermId site : mcreation.getMetastaticSiteTerms()) {
                try {
                    Resource tissueResource = rdfModel.createResource(UBERON_NAMESPACE + site.getId());
                    tissueResource.addProperty(RDF.type, this.pdxMetastaticSite);
                    modelCreation.addProperty(hasMetastaticSiteTermProperty, tissueResource);
                } catch (NullPointerException npe) {
                    // no term
                }
            }

            setProperty(modelCreation, hasViablyCryopreseredProperty, mcreation.getViablyCryopresered());

            modelCreation.addProperty(RDF.type, this.pdxModelCreation);
            modelCreation.addProperty(RDFS.label, "PDX Model " + mcreation.getModelID());
        }
    }

    /**
     * @param model TODO
     */
    private void outputQualityAssuranceRdf(PdxModel model) {

        ArrayList<PdxQualityAssurance> qas = model.getQualityAssurance();

        for (PdxQualityAssurance quality : qas) {
            /*
             * @TODO PassageTested can be null what to do then? -- "Passage not
             * specified"?
             */
            String passage = "-NotSpecified";
            if (quality.getPassageTested() != null) {
                passage = quality.getPassageTested().toString();
            }
            Resource qa = rdfModel.createResource(PDXNET_NAMESPACE + "/" + quality.getModelID() + "/QA/P" + passage);
            qa.addProperty(RDF.type, this.pdxQualityAssurance);
            qa.addProperty(RDFS.label, "QA for " + quality.getModelID() + " at passage P" + passage);

            setProperty(qa, this.hasQAMethodProperty, quality.getQcMethod());
            setProperty(qa, this.hasQAResultProperty, quality.getQcResult());
            setProperty(qa, this.hasPassageQaPerformedProperty, "P" + passage);
            setProperty(qa, this.hasAnimalHealthStatusSatisfactoryProperty, quality.getAnimalHealthStatus());

            setProperty(qa, hasCd45IHCProperty, quality.getCd45IHC());
            setProperty(qa, hasCd45IHCAssayResultProperty, quality.getCd45IHCAssayResult());
            setProperty(qa, hasClinicalDiagnosticMarkerAssayResultProperty, quality.getClinicalDiagnosticMarkerAssayResult());
            setProperty(qa, hasClinicalDiagnosticMarkerNotesProperty, quality.getClinicalDiagnosticMarkerNotes());

            if (quality.getClinicalDiagnosticMarkers() != null && quality.getClinicalDiagnosticMarkers().contains(";")) {
                String[] markers = quality.getClinicalDiagnosticMarkers().split(";");
                for (String marker : markers) {
                    setProperty(qa, hasClinicalDiagnosticMarkersProperty, marker);
                }
            } else {
                setProperty(qa, hasClinicalDiagnosticMarkersProperty, quality.getClinicalDiagnosticMarkers());
            }
            setProperty(qa, hasEbvTranscriptDetectionProperty, quality.getEbvTranscriptDetection());
            setProperty(qa, hasEbvTranscriptDetectionResultProperty, quality.getEbvTranscriptDetectionResult());
            setProperty(qa, hasHumanSpecificCytokeratin19Property, quality.getHumanSpecificCytokeratin19());
            setProperty(qa, hasMousePathogenStatusProperty, quality.getMousePathogenStatus());
            setProperty(qa, hasOverallEvaluationProperty, quality.getOverallEvaluation());
            setProperty(qa, hasPanCytokeratinAssayResultProperty, quality.getPanCytokeratinAssayResult());
            setProperty(qa, hasStrNotesProperty, quality.getStrNotes());

            this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, quality.getModelID())).addProperty(this.hasQAProperty, qa);
            qa.addProperty(this.hasPdxModelProperty, this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, quality.getModelID())));

        }
    }

    private void outputFileRDF(PdxModel model) {

        for (PdxOmicsFile omicsFile : model.getOmicsFiles()) {

            // is file name sufficent?
            Resource thisOmicsFile = rdfModel.createResource(PDXNET_NAMESPACE + "/File/" + model.getPDTC() + "/" + omicsFile.getFileName());
            thisOmicsFile.addProperty(RDF.type, this.pdxFile);
            thisOmicsFile.addProperty(RDFS.label, omicsFile.getFileName());

            if (omicsFile.getPatientID() != null) {
                this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE + "/patient", omicsFile.getPatientID())).addProperty(this.hasFile, thisOmicsFile);
                thisOmicsFile.addProperty(this.hasPatientProperty, this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE + "/patient", omicsFile.getPatientID())));
            }
            if (omicsFile.getModelID() != null) {
                this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getModelID())).addProperty(this.hasFile, thisOmicsFile);
                thisOmicsFile.addProperty(this.hasPdxModelProperty, this.rdfModel.getResource(String.format("%s/%s", PDXNET_NAMESPACE, omicsFile.getModelID())));
            }

            setAccessLevel(thisOmicsFile, omicsFile.getAccessLevel());
            String date = omicsFile.getCreatedDateTime();
            System.out.println(date);
            if(date != null && date.trim().length()>0){
                if(date.length()==7)date = "0"+date;
                Calendar c = new Calendar.Builder().setDate(new Integer(date.substring(4)),new Integer(date.substring(0,2))-1, new Integer(date.substring(2,4))).build();
          
                System.out.println(date+" parsed as "+new Integer(date.substring(4))+","+new Integer(date.substring(0,2))+","+new Integer(date.substring(2,4)));
                System.out.println(c.getTime().toString());
                System.out.println(rdfModel.createTypedLiteral(c));
                thisOmicsFile.addProperty(this.hasCreatedDateTime,rdfModel.createTypedLiteral(c));
            }
            
            thisOmicsFile.addProperty(this.hasDataCategory, omicsFile.getDataCategory());
            // this is a resource
            setDataFormat(thisOmicsFile, omicsFile.getDataFormat());
            thisOmicsFile.addProperty(this.hasDataType, omicsFile.getDataType());
            
            // this should be Patient, Patient normal or PDX
            thisOmicsFile.addProperty(this.hasSampleType, omicsFile.getSampleType());
            thisOmicsFile.addProperty(this.hasExperimentalStrategy, omicsFile.getExperimentalStrategy());
            
            
             if (omicsFile.getFileSize() != null) {
                thisOmicsFile.addProperty(this.hasFileSize,
                        ResourceFactory.createTypedLiteral(String.valueOf(omicsFile.getFileSize()),
                                XSDDatatype.XSDinteger));
            }
            
            
            
            thisOmicsFile.addProperty(this.hasPlatform, omicsFile.getPlatform());
            thisOmicsFile.addProperty(this.hasCaptureKit, omicsFile.getCaptureKit());
            date = omicsFile.getUpdatedDateTime();

              if(date != null){
                if(date.length()==7)date = "0"+date;
                Calendar c = new Calendar.Builder().setDate(new Integer(date.substring(4)),new Integer(date.substring(0,2))-1, new Integer(date.substring(2,4))).build();
          
            //    System.out.println(date+" parsed as "+new Integer(date.substring(4))+","+new Integer(date.substring(0,2))+","+new Integer(date.substring(2,4)));
            //    System.out.println(c.getTime().toString());
            //    System.out.println(rdfModel.createTypedLiteral(c));
                thisOmicsFile.addProperty(this.hasUpdatedDateTime,rdfModel.createTypedLiteral(c));
            }
            
            
            setProperty(thisOmicsFile, this.hasFFPE, omicsFile.getIsFFPE());
            setProperty(thisOmicsFile, this.hasPairedEnd, omicsFile.getPairedEnd());

            thisOmicsFile.addProperty(this.hasFileName, omicsFile.getFileName());
            setProperty(thisOmicsFile, this.hasPassage, "P"+omicsFile.getPassage());

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
    
    private Resource getTumorGradeResource(TumorGrade grade) {
        Resource resource = null;
        switch (grade) {
            case G1:
                resource = G1;
                break;
            case G2:
                resource = G2;
                break;
            case G3:
                resource = G3;
                break;
            case G4:
                resource = G4;
                break;
            case GX:
                resource = GX;
                break;
            
            
        }
        return resource;
    }
    
    private Resource setDataFormat(Resource resource, String dataFormat){
        dataFormat = dataFormat.toLowerCase();
        Resource format;
         switch(dataFormat){
            case "md5":
                    format = this.md5;
                    break;
            case "cram":
                    format = this.cram;
                    break;
            case "bam":
                    format = this.bam;
                    break;  
            case "fastq":
                    format = this.fastq;
                    break;
            case "fastq.gz":
                    format = this.fastqGZ;
                    break;
            default:
                    format =  null;
         }
         resource.addProperty(this.hasDataFormat,format);
         return resource;
    }

    private Resource setAccessLevel(Resource resource, String accessLevel) {
        if (accessLevel != null) {
            if ("controlled".equals(accessLevel.toLowerCase())) {
                resource.addProperty(this.hasAccessLevel, this.controlledAccess);
            } else if("open".equals(accessLevel.toLowerCase())) {
                resource.addProperty(this.hasAccessLevel, this.openAccess);
            }else{
                System.out.println("Access Level:" + accessLevel + " does not correspond to an object type!");
            }
        }
        return resource;
    }

    private Resource setProperty(Resource resource, Property property, ResponseToTreatment response) {
        if (response != null) {
            resource.addProperty(property, getResponseResource(response));
        }
        return resource;
    }
    
    private Resource setProperty(Resource resource, Property property, TumorGrade grade){
        if(grade != TumorGrade.UNK){
            resource.addProperty(property, getTumorGradeResource(grade));
        }
        return resource;
    }

    // add a string property if the value is not null
    private Resource setProperty(Resource resource, Property property, String value) {
        try {
            if (value != null && value.length() > 0) {
                resource.addProperty(property, value);
            }
        } catch (Exception e) {
            System.out.println("value " + value);
        }
        return resource;
    }

    // add an integer property if the value is not null
    private Resource setProperty(Resource resource, Property property, Integer value) {
        if (value != null) {
            resource.addProperty(property,
                    ResourceFactory.createTypedLiteral(String.valueOf(value),
                            XSDDatatype.XSDinteger));
        }
        return resource;
    }

    // add a boolean property if the value is not null
    private Resource setProperty(Resource resource, Property property, Boolean value) {
        if (value != null) {
            if (value) {
                resource.addProperty(property, trueLiteral);
            } else {
                resource.addProperty(property, falseLiteral);
            }
        }
        return resource;
    }
    
    
   


    private void createEntities() {

        trueLiteral = rdfModel.createTypedLiteral("true", XSDDatatype.XSDboolean);
        falseLiteral = rdfModel.createTypedLiteral("false", XSDDatatype.XSDboolean);

        this.maleSex = rdfModel.createResource(NCIT_NAMESPACE + male.getId());
        this.maleSex.addProperty(RDF.type, this.pdxSex);

        this.femaleSex = rdfModel.createResource(NCIT_NAMESPACE + female.getId());
        this.femaleSex.addProperty(RDF.type, this.pdxSex);

        this.noConsent = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "consent_NO");
        this.noConsent.addProperty(RDFS.label, "No patient consent provided");
        this.noConsent.addProperty(RDF.type, this.pdxPatientConsent);

        this.yesConsent = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "consent_YES");
        this.yesConsent.addProperty(RDFS.label, "Patient consent provided");
        this.yesConsent.addProperty(RDF.type, this.pdxPatientConsent);

        this.academicConsent = rdfModel.createResource(PDXNET_NAMESPACE + "/" + "consent_ACADEMIC_ONLY");
        this.academicConsent.addProperty(RDFS.label, "Academic consent only");
        this.academicConsent.addProperty(RDF.type, this.pdxPatientConsent);

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

        this.controlledAccess = rdfModel.createResource(PDXNET_NAMESPACE + "/access_level/" + "Controlled");
        this.controlledAccess.addProperty(RDFS.label, "Controlled Access");
        this.controlledAccess.addProperty(RDF.type, this.pdxFileAccessLevel);
        
        
        this.openAccess = rdfModel.createResource(PDXNET_NAMESPACE + "/access_level/" + "Open");
        this.openAccess.addProperty(RDFS.label, "Open Access");
        this.openAccess.addProperty(RDF.type, this.pdxFileAccessLevel);
        
        this.GX = rdfModel.createResource(PDXNET_NAMESPACE+"/tumor_grade/"+TumorGrade.GX.name());
        this.GX.addProperty(RDFS.label,TumorGrade.GX.name());
        this.GX.addProperty(RDF.type, this.pdxTumorGrade);
        
        this.G1 = rdfModel.createResource(PDXNET_NAMESPACE+"/tumor_grade/"+TumorGrade.G1.name());
        this.G1.addProperty(RDFS.label,TumorGrade.G1.name());
        this.G1.addProperty(RDF.type, this.pdxTumorGrade);
        
        this.G2 = rdfModel.createResource(PDXNET_NAMESPACE+"/tumor_grade/"+TumorGrade.G2.name());
        this.G2.addProperty(RDFS.label,TumorGrade.G2.name());
        this.G2.addProperty(RDF.type, this.pdxTumorGrade);
        
        this.G3 = rdfModel.createResource(PDXNET_NAMESPACE+"/tumor_grade/"+TumorGrade.G3.name());
        this.G3.addProperty(RDFS.label,TumorGrade.G3.name());
        this.G3.addProperty(RDF.type, this.pdxTumorGrade);
        
        this.G4 = rdfModel.createResource(PDXNET_NAMESPACE+"/tumor_grade/"+TumorGrade.G4.name());
        this.G4.addProperty(RDFS.label,TumorGrade.G4.name());
        this.G4.addProperty(RDF.type, this.pdxTumorGrade);

        //omics file formats
        this.fastqGZ = rdfModel.createResource(PDXNET_NAMESPACE + "/data_format/" + "FASTQ.GZ");
        this.fastqGZ.addProperty(RDFS.label, "FASTQ.GZ");
        this.fastqGZ.addProperty(RDF.type, this.pdxDataFormat);
        
        this.fastq = rdfModel.createResource(PDXNET_NAMESPACE + "/data_format/" + "FASTQ");
        this.fastq.addProperty(RDFS.label, "FASTQ");
        this.fastq.addProperty(RDF.type, this.pdxDataFormat);
        
        this.bam = rdfModel.createResource(PDXNET_NAMESPACE + "/data_format/" + "BAM");
        this.bam.addProperty(RDFS.label, "BAM");
        this.bam.addProperty(RDF.type, this.pdxDataFormat);
        
        this.cram = rdfModel.createResource(PDXNET_NAMESPACE + "/data_format/" + "CRAM");
        this.cram.addProperty(RDFS.label, "CRAM");
        this.cram.addProperty(RDF.type, this.pdxDataFormat);
        
        this.md5 = rdfModel.createResource(PDXNET_NAMESPACE + "/data_format/" + "MD5");
        this.md5.addProperty(RDFS.label, "MD5");
        this.md5.addProperty(RDF.type, this.pdxDataFormat);
        
        
    }

    private void specifyPrefixes() {
        // unique idenfitifer for the patient
        this.hasPatientIdProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "#hasPatientID");
        this.hasPatientIdProperty.addProperty(RDFS.label, "has Patient ID");
        this.hasPatientIdProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPatientIdProperty.addProperty(RDFS.range, XSD.xstring);
        
        // Unique tumor id
        this.hasSubmitterTumorIdProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "#hasSubmitterTumorId");
        this.hasSubmitterTumorIdProperty.addProperty(RDFS.label, "has Unique identifer for sampled tissue");
        this.hasSubmitterTumorIdProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasSubmitterTumorIdProperty.addProperty(RDFS.range, XSD.xstring);
        
        // Tumor Sample Object
        this.hasTumorProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasClinicalTumor");
        this.hasTumorProperty.addProperty(RDFS.label, "has Clinical tumor");
        this.hasTumorProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorProperty.addProperty(RDFS.range, this.pdxClinicalTumor);

        this.hasPatientProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatient");
        this.hasPatientProperty.addProperty(RDFS.label, "has Patient");
        this.hasPatientProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasPatientProperty.addProperty(RDFS.range, this.pdxPatient);

        this.hasStudyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasModelStudy");
        this.hasStudyProperty.addProperty(RDFS.label, "has Model study");
        this.hasStudyProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasStudyProperty.addProperty(RDFS.range, this.pdxModelStudy);

        this.hasStudyTreatmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatment");
        this.hasStudyTreatmentProperty.addProperty(RDFS.label, "has Study treatment");
        this.hasStudyTreatmentProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasStudyTreatmentProperty.addProperty(RDFS.range, this.pdxStudyTreatment);

        this.hasPatientTreatmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatment");
        this.hasPatientTreatmentProperty.addProperty(RDFS.label, "has Patient treatment");
        this.hasPatientTreatmentProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasPatientTreatmentProperty.addProperty(RDFS.range, this.pdxPatientTreatment);

        // Patient sex
        this.hasSexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSex");
        this.hasSexProperty.addProperty(RDFS.label, "has Sex");
        this.hasSexProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasSexProperty.addProperty(RDFS.range, this.pdxSex);

        // Patient has provided consent to share data
        this.hasConsentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientConsent");
        this.hasConsentProperty.addProperty(RDFS.label, "has Patient consent");
        this.hasConsentProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasConsentProperty.addProperty(RDFS.range, this.pdxPatientConsent);

        // Ethnicity of patient
        this.hasEthnicityProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEthnicity");
        this.hasEthnicityProperty.addProperty(RDFS.label, "has Ethnicity");
        this.hasEthnicityProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        // should probably be defined classes
        this.hasEthnicityProperty.addProperty(RDFS.range, XSD.xstring);

        // Race of patient
        this.hasRaceProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasRace");
        this.hasRaceProperty.addProperty(RDFS.label, "has Race");
        this.hasRaceProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        // should probably be defined classes
        this.hasRaceProperty.addProperty(RDFS.range, XSD.xstring);

        // Virology status of patient
        this.hasPatientVirolgyStatusProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientVirologyStatus");
        this.hasPatientVirolgyStatusProperty.addProperty(RDFS.label, "has Patient virology status");
        this.hasPatientVirolgyStatusProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPatientVirolgyStatusProperty.addProperty(RDFS.range, XSD.xstring);
        
        // Clinical treatment setting status of patient
        this.hasPatientClinicalTreatmentSettingProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientClinicalTreatmentSetting");
        this.hasPatientClinicalTreatmentSettingProperty.addProperty(RDFS.label, "has Patient clinical treatment setting");
        this.hasPatientClinicalTreatmentSettingProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPatientClinicalTreatmentSettingProperty.addProperty(RDFS.range, XSD.xstring);
        
        // Treatment notes for patient
        this.hasPatientTreatmentNotesProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentNotes");
        this.hasPatientTreatmentNotesProperty.addProperty(RDFS.label, "has Patient treatmnet notes");
        this.hasPatientTreatmentNotesProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPatientTreatmentNotesProperty.addProperty(RDFS.range, XSD.xstring);

       

        // Histology for model
        this.hasModelHistologyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasModelHistology");
        this.hasModelHistologyProperty.addProperty(RDFS.label, "has Model histology");
        this.hasModelHistologyProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasModelHistologyProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasModelHistologyTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasModelHistologyTerm");
        this.hasModelHistologyTermProperty.addProperty(RDFS.label, "has Model histology term");
        this.hasModelHistologyTermProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasModelHistologyTermProperty.addProperty(RDFS.range,this.pdxTumorHistology);

        // Grade of tumor
        this.hasTumorGradeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorGrade");
        this.hasTumorGradeProperty.addProperty(RDFS.label, "has Tumor grade");
        this.hasTumorGradeProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorGradeProperty.addProperty(RDFS.range, this.pdxTumorGrade);
        

        // Stage of tumor these should probably all be objects since we are using AJCC vocab?
        this.hasTumorOverallStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasOverallStage");
        this.hasTumorOverallStageProperty.addProperty(RDFS.label, "has Overall stage");
        this.hasTumorOverallStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorOverallStageProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasTStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTStage");
        this.hasTStageProperty.addProperty(RDFS.label, "has T stage");
        this.hasTStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTStageProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasNStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasNStage");
        this.hasNStageProperty.addProperty(RDFS.label, "has N stage");
        this.hasNStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasNStageProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasMStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMStage");
        this.hasMStageProperty.addProperty(RDFS.label, "has M stage");
        this.hasMStageProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasMStageProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasTumorSampleTypeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorSampleType");
        this.hasTumorSampleTypeProperty.addProperty(RDFS.label, "has Tumor sample type");
        this.hasTumorSampleTypeProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorSampleTypeProperty.addProperty(RDFS.range, XSD.xstring);

        // PDX model generated from patient tumor
        this.hasPdxModelProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPdxModel");
        this.hasPdxModelProperty.addProperty(RDFS.label, "has PDX model");
        this.hasPdxModelProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasPdxModelProperty.addProperty(RDFS.range, this.pdxModelCreation);
        
        
        // Mouse strain for PDX model
        this.hasStrainProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStrain");
        this.hasStrainProperty.addProperty(RDFS.label, "has Strain");
        this.hasStrainProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStrainProperty.addProperty(RDFS.range, XSD.xstring);

        // Is the mouse strain humanized
        this.hasModelHumanizedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasHumanization");
        this.hasModelHumanizedProperty.addProperty(RDFS.label, "has Humanization");
        this.hasModelHumanizedProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasModelHumanizedProperty.addProperty(RDFS.range, XSD.xboolean);

        // Type of mouse humanization
        this.hasModelHumanizationTypeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasHumanizationType");
        this.hasModelHumanizationTypeProperty.addProperty(RDFS.label, "has Type of mouse humanization");
        this.hasModelHumanizationTypeProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasModelHumanizationTypeProperty.addProperty(RDFS.range, XSD.xstring);

        // Type of tumor preperation
        this.hasTumorPreparation = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorPreparation");
        this.hasTumorPreparation.addProperty(RDFS.label, "has Type of tumor preparation for engraftment");
        this.hasTumorPreparation.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorPreparation.addProperty(RDFS.range, XSD.xstring);

        // Instituion providing mouse
        this.hasStrainSourceProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStrainSource");
        this.hasStrainSourceProperty.addProperty(RDFS.label, "has Institution providing strain");
        this.hasStrainSourceProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStrainSourceProperty.addProperty(RDFS.range, XSD.xstring);

        // Treatement of mouse prior to engraftment
        this.hasModelTreatmentForEngraftmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTreatmentForEngraftment");
        this.hasModelTreatmentForEngraftmentProperty.addProperty(RDFS.label, "has Treatment for engraftment");
        this.hasModelTreatmentForEngraftmentProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasModelTreatmentForEngraftmentProperty.addProperty(RDFS.range, XSD.xstring);

        // Percent of successful engraftments 
        this.hasModelEngraftmentPercentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentPercent");
        this.hasModelEngraftmentPercentProperty.addProperty(RDFS.label, "has Engraftment percent");
        this.hasModelEngraftmentPercentProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasModelEngraftmentPercentProperty.addProperty(RDFS.range, XSD.xdouble);

        // Days for successful engraftment
        this.hasModelEngraftTimeInDaysProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentTimeInDays");
        this.hasModelEngraftTimeInDaysProperty.addProperty(RDFS.label, "has Engraftment time in days");
        this.hasModelEngraftTimeInDaysProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasModelEngraftTimeInDaysProperty.addProperty(RDFS.range,XSD.integer);

        this.hasModelEngraftmentProcedureProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentProcedure");
        this.hasModelEngraftmentProcedureProperty.addProperty(RDFS.label, "has Engraftment procedure");
        this.hasModelEngraftmentProcedureProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasModelEngraftmentProcedureProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasModelEngraftmentSiteProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentSite");
        this.hasModelEngraftmentSiteProperty.addProperty(RDFS.label, "has Engraftment site");
        this.hasModelEngraftmentSiteProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasModelEngraftmentSiteProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasPassagedTissueCryopreservedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPassagedTissueCryopreserved");
        this.hasPassagedTissueCryopreservedProperty.addProperty(RDFS.label, "has Passaged tissue was cryopreserved");
        this.hasPassagedTissueCryopreservedProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPassagedTissueCryopreservedProperty.addProperty(RDFS.range, XSD.xboolean);

        this.hasSublineOfModelProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSublineOfModel");
        this.hasSublineOfModelProperty.addProperty(RDFS.label, "has Subline of model");
        this.hasSublineOfModelProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasSublineOfModelProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasSublineReasonProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSublineReason");
        this.hasSublineReasonProperty.addProperty(RDFS.label, "has Subline reason");
        this.hasSublineReasonProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasSublineReasonProperty.addProperty(RDFS.range, XSD.xstring);

       
        this.hasBaselineTumorTargetSizeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasBaselineTumorTargetSize");
        this.hasBaselineTumorTargetSizeProperty.addProperty(RDFS.label, "has Baseline tumor target size");
        this.hasBaselineTumorTargetSizeProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasBaselineTumorTargetSizeProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasStudyDescriptionProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyDescription");
        this.hasStudyDescriptionProperty.addProperty(RDFS.label, "has Study description");
        this.hasStudyDescriptionProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStudyDescriptionProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasStudyHostStrainProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyHostStrain");
        this.hasStudyHostStrainProperty.addProperty(RDFS.label, "has Study host strain");
        this.hasStudyHostStrainProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStudyHostStrainProperty.addProperty(RDFS.range, XSD.xstring);

        // should be a corresponding anatomy term
        this.hasImplantationSiteProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasImplantationSite");
        this.hasImplantationSiteProperty.addProperty(RDFS.label, "has Implantation site");
        this.hasImplantationSiteProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasImplantationSiteProperty.addProperty(RDFS.range, XSD.xstring);

        // Tumor Characterization
        this.hasTumorCharacterizationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorCharacterization");
        this.hasTumorCharacterizationProperty.addProperty(RDFS.label, "has Tumor characterization");
        this.hasTumorCharacterizationProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorCharacterizationProperty.addProperty(RDFS.range, XSD.xstring);

        // Tumor is not EBV or mouse tissue
        this.tumorNotEbvNotMouseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasNotEbvNotMouseStatus");
        this.tumorNotEbvNotMouseProperty.addProperty(RDFS.label, "has Mouse is not EBV positive, tumor tissue is not mouse origin");
        this.tumorNotEbvNotMouseProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.tumorNotEbvNotMouseProperty.addProperty(RDFS.range, XSD.xboolean);

        // PDX model response to treatment
        this.hasPdxTumorResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorResponse");
        this.hasPdxTumorResponseProperty.addProperty(RDFS.label, "has Tumor response");
        this.hasPdxTumorResponseProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPdxTumorResponseProperty.addProperty(RDFS.range, this.pdxTreatmentResponse);

        // The pdx model's health status
        this.hasAnimalHealthStatusSatisfactoryProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSatisfactoryAnimalHealthStatus");
        this.hasAnimalHealthStatusSatisfactoryProperty.addProperty(RDFS.label, "has Satisfactory animal health status");
        this.hasAnimalHealthStatusSatisfactoryProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasAnimalHealthStatusSatisfactoryProperty.addProperty(RDFS.range, XSD.xboolean);

        // Passage on which QA was performed
        this.hasPassageQaPerformedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPassageQAPerformed");
        this.hasPassageQaPerformedProperty.addProperty(RDFS.label, "has Passage QA performed");
        this.hasPassageQaPerformedProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPassageQaPerformedProperty.addProperty(RDFS.range, XSD.xstring);

        // Patient current treatment drug
        this.currentTreatmentDrug = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCurrentTreatmentDrug");
        this.currentTreatmentDrug.addProperty(RDFS.label, "has Current patient treatment drug");
        this.currentTreatmentDrug.addProperty(RDF.type, OWL.DatatypeProperty);
        this.currentTreatmentDrug.addProperty(RDFS.range, XSD.xstring);

        // Lower age range for Patient at primary diagnosis
        this.primaryDiagnosisAgeBinLowerRange = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPrimaryDiagnosisAgeBinLowerRange");
        this.primaryDiagnosisAgeBinLowerRange.addProperty(RDFS.label, "has Primary diagnosis age lower range of 5 year bin");
        this.primaryDiagnosisAgeBinLowerRange.addProperty(RDF.type, OWL.DatatypeProperty);
        this.primaryDiagnosisAgeBinLowerRange.addProperty(RDFS.range, XSD.integer);

        // Upper age range for Patient when sample was taken
        this.primaryDiagnosisAgeBinUpperRange = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPrimaryDiagnosisAgeBinUpperRange");
        this.primaryDiagnosisAgeBinUpperRange.addProperty(RDFS.label, "has Primary diagnosis upper range of 5 year bin");
        this.primaryDiagnosisAgeBinUpperRange.addProperty(RDF.type, OWL.DatatypeProperty);
        this.primaryDiagnosisAgeBinUpperRange.addProperty(RDFS.range, XSD.integer);
        
        this.hasPatientTreatmentIndexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentIndex");
        this.hasPatientTreatmentIndexProperty.addProperty(RDFS.label, "has Patient treatment index");
        this.hasPatientTreatmentIndexProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPatientTreatmentIndexProperty.addProperty(RDFS.range, XSD.integer);
        
        this.hasPatientTreatmentRegimen = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentRegimen");
        this.hasPatientTreatmentRegimen.addProperty(RDFS.label, "has Patient treatment regimen");
        this.hasPatientTreatmentRegimen.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPatientTreatmentRegimen.addProperty(RDFS.range, XSD.xstring);
        
        this.hasPatientTreatmentResponse = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTreatmentResponse");
        this.hasPatientTreatmentResponse.addProperty(RDFS.label, "has Treatment reponse");
        this.hasPatientTreatmentResponse.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasPatientTreatmentResponse.addProperty(RDFS.range, this.pdxTreatmentResponse);

        this.hasPatientTreatmentReasonStopped = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientTreatmentReasonStopped");
        this.hasPatientTreatmentReasonStopped.addProperty(RDFS.label, "has Patient treatment reason stopped");
        this.hasPatientTreatmentReasonStopped.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPatientTreatmentReasonStopped.addProperty(RDFS.range, XSD.xstring);

        this.hasTumorEventIndexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorEventIndex");
        this.hasTumorEventIndexProperty.addProperty(RDFS.label, "has Tumor collection at treatment index");
        this.hasTumorEventIndexProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorEventIndexProperty.addProperty(RDFS.range, XSD.integer);
        
        this.hasTumorTreatmentNaiveProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorTreatmentNaive");
        this.hasTumorTreatmentNaiveProperty.addProperty(RDFS.label, "has Treatment naive");
        this.hasTumorTreatmentNaiveProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorTreatmentNaiveProperty.addProperty(RDFS.range, XSD.xboolean);

        // should be binned?
        this.hasPatientAgeAtCollectionProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPatientAgeAtCollection");
        this.hasPatientAgeAtCollectionProperty.addProperty(RDFS.label, "has Patient age at collection");
        this.hasPatientAgeAtCollectionProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPatientAgeAtCollectionProperty.addProperty(RDFS.range, XSD.integer);

        this.hasTumorInitialDiagnosisProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorInitialDiagnosis");
        this.hasTumorInitialDiagnosisProperty.addProperty(RDFS.label, "has Initial diagnosis");
        this.hasTumorInitialDiagnosisProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorInitialDiagnosisProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasTumorInitialDiagnosisTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorInitialDiagnosisTerm");
        this.hasTumorInitialDiagnosisTermProperty.addProperty(RDFS.label, "has Initial diagnosis term");
        this.hasTumorInitialDiagnosisTermProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorInitialDiagnosisTermProperty.addProperty(RDFS.range,this.pdxDiagnosis);

        this.hasTumorTissueOfOriginProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorTissueOfOrigin");
        this.hasTumorTissueOfOriginProperty.addProperty(RDFS.label, "has Tissue of origin");
        this.hasTumorTissueOfOriginProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorTissueOfOriginProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasTumorTissueOfOriginTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorTissueOfOriginTerm");
        this.hasTumorTissueOfOriginTermProperty.addProperty(RDFS.label, "has Tissue of origin term");
        this.hasTumorTissueOfOriginTermProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorTissueOfOriginTermProperty.addProperty(RDFS.range,this.pdxTissueOfOrigin);

        this.hasTumorSpecimenTissueProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorSpecimenTissue");
        this.hasTumorSpecimenTissueProperty.addProperty(RDFS.label, "has Specimen tissue");
        this.hasTumorSpecimenTissueProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorSpecimenTissueProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasTumorSpecimenTissueTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorSpecimenTissueTerm");
        this.hasTumorSpecimenTissueTermProperty.addProperty(RDFS.label, "has Specimen tissue term");
        this.hasTumorSpecimenTissueTermProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorSpecimenTissueTermProperty.addProperty(RDFS.range,this.pdxSpecimenTissue);

        this.hasTumorTissueHistologyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorTissueHistology");
        this.hasTumorTissueHistologyProperty.addProperty(RDFS.label, "has Tumor tissue histology");
        this.hasTumorTissueHistologyProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorTissueHistologyProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasTumorTissueHistologyTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorTissueHistologyTerm");
        this.hasTumorTissueHistologyTermProperty.addProperty(RDFS.label, "has Tissue histology term");
        this.hasTumorTissueHistologyTermProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorTissueHistologyTermProperty.addProperty(RDFS.range,this.pdxTumorHistology);

        this.hasTumorClinicalMarkersProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorClinicalMarkers");
        this.hasTumorClinicalMarkersProperty.addProperty(RDFS.label, "has Diagnostic clinical markers");
        this.hasTumorClinicalMarkersProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasTumorClinicalMarkersProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasStudyTreatmentDrugProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentDrug");
        this.hasStudyTreatmentDrugProperty.addProperty(RDFS.label, "has Study treatment drug");
        this.hasStudyTreatmentDrugProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStudyTreatmentDrugProperty.addProperty(RDFS.range, XSD.xstring);
        
        this.hasStudyTreatmentEndpoint1ResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentEndpoint1Response");
        this.hasStudyTreatmentEndpoint1ResponseProperty.addProperty(RDFS.label, "has Study treatment endpoint 1 response");
        this.hasStudyTreatmentEndpoint1ResponseProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasStudyTreatmentEndpoint1ResponseProperty.addProperty(RDFS.range, this.pdxTreatmentResponse);

        this.hasStudyTreatmentEndpoint2ResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentEndpoint2Response");
        this.hasStudyTreatmentEndpoint2ResponseProperty.addProperty(RDFS.label, "has Study treatment endpoint 2 response");
        this.hasStudyTreatmentEndpoint2ResponseProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasStudyTreatmentEndpoint2ResponseProperty.addProperty(RDFS.range, this.pdxTreatmentResponse);
        
        this.hasStudyTreatmentEndpoint3ResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentEndpoint3Response");
        this.hasStudyTreatmentEndpoint3ResponseProperty.addProperty(RDFS.label, "has Study treatment endpoint 3 response");
        this.hasStudyTreatmentEndpoint3ResponseProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasStudyTreatmentEndpoint3ResponseProperty.addProperty(RDFS.range, this.pdxTreatmentResponse);

        this.hasStudyTreatmentDoseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentDose");
        this.hasStudyTreatmentDoseProperty.addProperty(RDFS.label, "has Study treatment dose");
        this.hasStudyTreatmentDoseProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStudyTreatmentDoseProperty.addProperty(RDFS.range, XSD.xstring);
        
        this.hasStudyTreatmentRouteProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentRoute");
        this.hasStudyTreatmentRouteProperty.addProperty(RDFS.label, "has Study treatment route");
        this.hasStudyTreatmentRouteProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStudyTreatmentRouteProperty.addProperty(RDFS.range, XSD.xstring);
        
        this.hasStudyTreatmentFrequencyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyTreatmentFrequency");
        this.hasStudyTreatmentFrequencyProperty.addProperty(RDFS.label, "has Study treatment frequency");
        this.hasStudyTreatmentFrequencyProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStudyTreatmentFrequencyProperty.addProperty(RDFS.range, XSD.xstring);
        
        // for files
        this.hasFile = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFile");
        this.hasFile.addProperty(RDFS.label, "has File");
        this.hasFile.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasFile.addProperty(RDFS.range, this.pdxFile);

        this.hasAccessLevel = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasAccessLevel");
        this.hasAccessLevel.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasAccessLevel.addProperty(RDFS.range, this.pdxFileAccessLevel);
        this.hasAccessLevel.addProperty(RDFS.label, "has Access level");

        this.hasCreatedDateTime = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCreatedDateTime");
        this.hasCreatedDateTime.addProperty(RDFS.label, "has Created date and time");
        this.hasCreatedDateTime.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasCreatedDateTime.addProperty(RDFS.range, XSD.dateTime);

        this.hasDataCategory = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDataCategory");
        this.hasDataCategory.addProperty(RDFS.label, "has Data category");
        this.hasDataCategory.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasDataCategory.addProperty(RDFS.range, XSD.xstring);

        this.hasDataFormat = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDataFormat");
        this.hasDataFormat.addProperty(RDFS.label, "has Data format");
        this.hasDataFormat.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasDataFormat.addProperty(RDFS.range, XSD.xstring);

        this.hasDataType = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDataType");
        this.hasDataType.addProperty(RDFS.label, "has Data type");
        this.hasDataType.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasDataType.addProperty(RDFS.range, XSD.xstring);

        this.hasSampleType = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSampleType");
        this.hasSampleType.addProperty(RDFS.label, "has Sample type");
        this.hasSampleType.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasSampleType.addProperty(RDFS.range, XSD.xstring);

        this.hasExperimentalStrategy = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasExperimentalStrategy");
        this.hasExperimentalStrategy.addProperty(RDFS.label, "has Experimental strategy");
        this.hasExperimentalStrategy.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasExperimentalStrategy.addProperty(RDFS.range, XSD.xstring);

        this.hasFileSize = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFileSize");
        this.hasFileSize.addProperty(RDFS.label, "has File size");
        this.hasFileSize.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasFileSize.addProperty(RDFS.range, XSD.integer);

        this.hasPlatform = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPlatform");
        this.hasPlatform.addProperty(RDFS.label, "has Platform");
        this.hasPlatform.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPlatform.addProperty(RDFS.range, XSD.xstring);

        this.hasCaptureKit = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCaptureKit");
        this.hasCaptureKit.addProperty(RDFS.label, "has Capture kit");
        this.hasCaptureKit.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasCaptureKit.addProperty(RDFS.range, XSD.xstring);

        this.hasUpdatedDateTime = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasUpdateDateTime");
        this.hasUpdatedDateTime.addProperty(RDFS.label, "has Update date and time");
        this.hasUpdatedDateTime.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasUpdatedDateTime.addProperty(RDFS.range, XSD.dateTime);

        this.hasFFPE = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFFPE");
        this.hasFFPE.addProperty(RDFS.label, "has FFPE");
        this.hasFFPE.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasFFPE.addProperty(RDFS.range, XSD.xboolean);

        this.hasPairedEnd = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPairedEnd");
        this.hasPairedEnd.addProperty(RDFS.label, "has Paired end");
        this.hasPairedEnd.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPairedEnd.addProperty(RDFS.range, XSD.integer);

        this.hasFileName = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasFileName");
        this.hasFileName.addProperty(RDFS.label, "has File name");
        this.hasFileName.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasFileName.addProperty(RDFS.range,XSD.xstring);
        
        this.hasPassage = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasModelPassage");
        this.hasPassage.addProperty(RDFS.label, "has Model passage");
        this.hasPassage.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPassage.addProperty(RDFS.range,XSD.xstring);
        
        this.hasQAMethodProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasQAMethod");
        this.hasQAMethodProperty.addProperty(RDFS.label, "has QA method");
        this.hasQAMethodProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasQAMethodProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasQAResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasQAResult");
        this.hasQAResultProperty.addProperty(RDFS.label, "has QA result");
        this.hasQAResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasQAResultProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasQAPassProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasQAPass");
        this.hasQAPassProperty.addProperty(RDFS.label, "has QA passed");
        this.hasQAPassProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasQAPassProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasQAProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasQualityAssurance");
        this.hasQAProperty.addProperty(RDFS.label, "has Quality assurance");
        this.hasQAProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasQAProperty.addProperty(RDFS.range, this.pdxQualityAssurance);

        this.hasAnimalHealthStatusProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasAnimalHealthStatus");
        this.hasAnimalHealthStatusProperty.addProperty(RDFS.label, "has Animal health status");
        this.hasAnimalHealthStatusProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasAnimalHealthStatusProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasClinicalEventPointProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasClinicalEventPoint");
        this.hasClinicalEventPointProperty.addProperty(RDFS.label, "has Clinical event point");
        this.hasClinicalEventPointProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasClinicalEventPointProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasClinicalEventPointTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasClinicalEventPointTerm");
        this.hasClinicalEventPointTermProperty.addProperty(RDFS.label, "has Clinical event point term");
        this.hasClinicalEventPointTermProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasClinicalEventPointTermProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasCollectionProcedureProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCollectionProcedure");
        this.hasCollectionProcedureProperty.addProperty(RDFS.label, "has Collection procedure");
        this.hasCollectionProcedureProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasCollectionProcedureProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasStrAnalysisProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSTRAnalysis");
        this.hasStrAnalysisProperty.addProperty(RDFS.label, "has STR analysis");
        this.hasStrAnalysisProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStrAnalysisProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasStrEvaluationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSTREvaluation");
        this.hasStrEvaluationProperty.addProperty(RDFS.label, "has STR evaluation");
        this.hasStrEvaluationProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStrEvaluationProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasStrMarkersProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSTRMarkers");
        this.hasStrMarkersProperty.addProperty(RDFS.label, "has STR markers");
        this.hasStrMarkersProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStrMarkersProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasCryopreservedBeforeEngraftmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasBeenCryopreservedBeforeEngraftment");
        this.hasCryopreservedBeforeEngraftmentProperty.addProperty(RDFS.label, "has Been cryopreserved before engraftment");
        this.hasCryopreservedBeforeEngraftmentProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasCryopreservedBeforeEngraftmentProperty.addProperty(RDFS.range, XSD.xboolean);

        this.hasDoublingTimeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDoublingTime");
        this.hasDoublingTimeProperty.addProperty(RDFS.label, "has Doubling time");
        this.hasDoublingTimeProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasDoublingTimeProperty.addProperty(RDFS.range,XSD.integer);
        
        this.hasEngraftmentMaterialProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEngraftmentMaterial");
        this.hasEngraftmentMaterialProperty.addProperty(RDFS.label, "has Engraftment material");
        this.hasEngraftmentMaterialProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasEngraftmentMaterialProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasMacroMetastasisRequiresExcisionProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMacroMetastasisRequiresExcision");
        this.hasMacroMetastasisRequiresExcisionProperty.addProperty(RDFS.label, "has Macro metastasis requires excision");
        this.hasMacroMetastasisRequiresExcisionProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasMacroMetastasisRequiresExcisionProperty.addProperty(RDFS.range, XSD.xboolean);

        this.hasMetastasisProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMetastasis");
        this.hasMetastasisProperty.addProperty(RDFS.label, "has Metastasis");
        this.hasMetastasisProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasMetastasisProperty.addProperty(RDFS.range, XSD.xboolean);
        
        this.hasMetastaticSitesProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMetastaticSite");
        this.hasMetastaticSitesProperty.addProperty(RDFS.label, "has Metastastatic site");
        this.hasMetastaticSitesProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasMetastaticSitesProperty.addProperty(RDFS.range, XSD.xstring);
        
        this.hasMetastaticSiteTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMetastaticSiteTerm");
        this.hasMetastaticSiteTermProperty.addProperty(RDFS.label, "has Metastastatic site term");
        this.hasMetastaticSiteTermProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasMetastaticSiteTermProperty.addProperty(RDFS.range, this.pdxMetastaticSite);

        this.hasViablyCryopreseredProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasViablyCryopresered");
        this.hasViablyCryopreseredProperty.addProperty(RDFS.label, "has Viably cryopresered");
        this.hasViablyCryopreseredProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasViablyCryopreseredProperty.addProperty(RDFS.range, XSD.xboolean);
        
        this.hasPdtcProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPDTC");
        this.hasPdtcProperty.addProperty(RDFS.label, "has PDTC");
        this.hasPdtcProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPdtcProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasCd45IHCProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCD45IHC");
        this.hasCd45IHCProperty.addProperty(RDFS.label, "has CD45 IHC");
        this.hasCd45IHCProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasCd45IHCProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasCd45IHCAssayResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCD45IHCAssayResult");
        this.hasCd45IHCAssayResultProperty.addProperty(RDFS.label, "has CD45 IHC assay result");
        this.hasCd45IHCAssayResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasCd45IHCAssayResultProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasClinicalDiagnosticMarkerAssayResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasClinicalDiagnosticMarkerAssayResult");
        this.hasClinicalDiagnosticMarkerAssayResultProperty.addProperty(RDFS.label, "has Clinical diagnostic marker assay result");
        this.hasClinicalDiagnosticMarkerAssayResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasClinicalDiagnosticMarkerAssayResultProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasClinicalDiagnosticMarkerNotesProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasClinicalDiagnosticMarkerNotes");
        this.hasClinicalDiagnosticMarkerNotesProperty.addProperty(RDFS.label, "has Clinical diagnostic marker notes");
        this.hasClinicalDiagnosticMarkerNotesProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasClinicalDiagnosticMarkerNotesProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasClinicalDiagnosticMarkersProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasClinicalDiagnosticMarkers");
        this.hasClinicalDiagnosticMarkersProperty.addProperty(RDFS.label, "has Clinical diagnostic markers");
        this.hasClinicalDiagnosticMarkersProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasClinicalDiagnosticMarkersProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasEbvTranscriptDetectionProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEbvTranscriptDetection");
        this.hasEbvTranscriptDetectionProperty.addProperty(RDFS.label, "has EBV transcript detection");
        this.hasEbvTranscriptDetectionProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasEbvTranscriptDetectionProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasEbvTranscriptDetectionResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasEbvTranscriptDetectionResult");
        this.hasEbvTranscriptDetectionResultProperty.addProperty(RDFS.label, "has EBV transcript detection result");
        this.hasEbvTranscriptDetectionResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasEbvTranscriptDetectionResultProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasHumanSpecificCytokeratin19Property = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasHumanSpecificCytokeratin19");
        this.hasHumanSpecificCytokeratin19Property.addProperty(RDFS.label, "has Human specific cytokeratin 19");
        this.hasHumanSpecificCytokeratin19Property.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasHumanSpecificCytokeratin19Property.addProperty(RDFS.range,XSD.xstring);
        
        this.hasMousePathogenStatusProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasMousePathogenStatus");
        this.hasMousePathogenStatusProperty.addProperty(RDFS.label, "has Mouse pathogen status");
        this.hasMousePathogenStatusProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasMousePathogenStatusProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasOverallEvaluationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasOverallEvaluation");
        this.hasOverallEvaluationProperty.addProperty(RDFS.label, "has Overall evaluation");
        this.hasOverallEvaluationProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasOverallEvaluationProperty.addProperty(RDFS.range,XSD.xstring);
        
        this.hasPanCytokeratinAssayResultProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasPanCytokeratinAssayResult");
        this.hasPanCytokeratinAssayResultProperty.addProperty(RDFS.label, "has PanCytokeratin assay result");
        this.hasPanCytokeratinAssayResultProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasPanCytokeratinAssayResultProperty.addProperty(RDFS.range, XSD.xstring);
        
        this.hasStrNotesProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasSTRNotes");
        this.hasStrNotesProperty.addProperty(RDFS.label, "has STR notes");
        this.hasStrNotesProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStrNotesProperty.addProperty(RDFS.range, XSD.xstring);
        
        this.hasCohortProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCohort");
        this.hasCohortProperty.addProperty(RDFS.label, "has Cohort");
        this.hasCohortProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasCohortProperty.addProperty(RDFS.range, XSD.xstring);

        this.hasCohortSizeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasCohortSize");
        this.hasCohortSizeProperty.addProperty(RDFS.label, "has Cohort size");
        this.hasCohortSizeProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasCohortSizeProperty.addProperty(RDFS.range, XSD.integer);

        this.hasDosingScheduleProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasDosingSchedule");
        this.hasDosingScheduleProperty.addProperty(RDFS.label, "has Dosing schedule");
        this.hasDosingScheduleProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasDosingScheduleProperty.addProperty(RDFS.range, XSD.xstring);
        
        this.hasNumberOfCyclesProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasNumberOfCycles");
        this.hasNumberOfCyclesProperty.addProperty(RDFS.label, "has Number of cycles");
        this.hasNumberOfCyclesProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasNumberOfCyclesProperty.addProperty(RDFS.range, XSD.integer);

        this.hasStudyDurationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasStudyDuration");
        this.hasStudyDurationProperty.addProperty(RDFS.label, "has Study duration");
        this.hasStudyDurationProperty.addProperty(RDF.type, OWL.DatatypeProperty);
        this.hasStudyDurationProperty.addProperty(RDFS.range, XSD.integer);

        rdfModel.setNsPrefix("PDXNET", PDXNET_NAMESPACE);
        rdfModel.setNsPrefix("NCIT", NCIT_NAMESPACE);
        rdfModel.setNsPrefix("UBERON", UBERON_NAMESPACE);

    }

}
