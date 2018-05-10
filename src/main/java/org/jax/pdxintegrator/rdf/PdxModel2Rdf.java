package org.jax.pdxintegrator.rdf;

import com.github.phenomics.ontolib.ontology.data.ImmutableTermId;
import com.github.phenomics.ontolib.ontology.data.TermId;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.*;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.modelcreation.MouseTreatmentForEngraftment;
import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.modelcreation.TumorPrepMethod;
import org.jax.pdxintegrator.model.patient.Consent;
import org.jax.pdxintegrator.model.patient.Sex;
import org.jax.pdxintegrator.model.patient.PdxPatient;
import org.jax.pdxintegrator.model.qualityassurance.ModelCharacterization;
import org.jax.pdxintegrator.model.qualityassurance.PdxQualityAssurance;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToTreatment;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;

import java.io.OutputStream;
import java.util.List;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.jax.pdxintegrator.model.modelstudy.PdxModelStudy;
import org.jax.pdxintegrator.model.modelstudy.PdxStudyTreatment;
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
    private Property hasStageProperty = null;
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

    private Resource thisPatient = null;
    private Resource thisPdxModelCreation = null;
    private Resource thisTumorSample = null;
    private Resource thisModelStudy = null;

    private static final String PDXNET_NAMESPACE = "http://pdxnetwork.org/pdxmodel/_";
    private final static String NCIT_NAMESPACE = "http://purl.obolibrary.org/obo/NCIT_";
    private final static String UBERON_NAMESPACE = "http://purl.obolibrary.org/obo/UBERON_";

    /// these are the classes needed for the Seven Bridges model
    private OntClass pdxPatient;
    private OntClass pdxDiagnosis;
    private OntClass pdxSex;
    private OntClass pdxModelStudy;
    private OntClass pdxClinicalTumor;
    private OntClass pdxQualityAssurance;
    private OntClass pdxModelCreation;
    private OntClass pdxPatientTreatment;
    private OntClass pdxStudyTreatment;

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
        rdfModel.write(out);//,"Turtle");

    }

    private void initializeModelFramework() {
        this.rdfModel = ModelFactory.createOntologyModel();

        String pdxMiPatientURI = String.format("%s%s", PDXNET_NAMESPACE, "Patient");
        this.pdxPatient = rdfModel.createClass(pdxMiPatientURI);
        this.pdxPatient.addProperty(RDFS.label, "Patient");

        String pdxMiPatientTreatmentURI = String.format("%s%s", PDXNET_NAMESPACE, "PatientTreatment");
        this.pdxPatientTreatment = rdfModel.createClass(pdxMiPatientTreatmentURI);
        this.pdxPatientTreatment.addProperty(RDFS.label, "Patient Treatment");

        String pdxMiDiagnosisURI = String.format("%s%s", PDXNET_NAMESPACE, "Diagnosis");
        this.pdxDiagnosis = rdfModel.createClass(pdxMiDiagnosisURI);
        this.pdxDiagnosis.addProperty(RDFS.label, "Diagnosis");

        String pdxMiSexURI = String.format("%s%s", PDXNET_NAMESPACE, "Sex");
        this.pdxSex = rdfModel.createClass(pdxMiSexURI);
        this.pdxSex.addProperty(RDFS.label, "Sex");

        String pdxMiModelStudyURI = String.format("%s%s", PDXNET_NAMESPACE, "ModelStudy");
        this.pdxModelStudy = rdfModel.createClass(pdxMiModelStudyURI);
        this.pdxModelStudy.addProperty(RDFS.label, "Model Study");

        String pdxMiStudyTreatmentURI = String.format("%s%s", PDXNET_NAMESPACE, "StudyTreatment");
        this.pdxStudyTreatment = rdfModel.createClass(pdxMiStudyTreatmentURI);
        this.pdxStudyTreatment.addProperty(RDFS.label, "Study Treatment");

        String pdxMiClinicalTumorURI = String.format("%s%s", PDXNET_NAMESPACE, "ClinicalTumor");
        this.pdxClinicalTumor = rdfModel.createClass(pdxMiClinicalTumorURI);
        this.pdxClinicalTumor.addProperty(RDFS.label, "Clinical Tumor");

        String pdxMiQualityAssuranceURI = String.format("%s%s", PDXNET_NAMESPACE, "QualityAssurance");
        this.pdxQualityAssurance = rdfModel.createClass(pdxMiQualityAssuranceURI);
        this.pdxQualityAssurance.addProperty(RDFS.label, "Quality Assurance");

        String pdxMiModelCreationURI = String.format("%s%s", PDXNET_NAMESPACE, "ModelCreation");
        this.pdxModelCreation = rdfModel.createClass(pdxMiModelCreationURI);
        this.pdxModelCreation.addProperty(RDFS.label, "Model Creation");

        String pdxMiTreatmentResponseURI = String.format("%s%s", PDXNET_NAMESPACE, "TreatmentResponse");
        this.pdxTreatmentResponse = rdfModel.createClass(pdxMiTreatmentResponseURI);
        this.pdxTreatmentResponse.addProperty(RDFS.label, "Treatment Response");

        String pdxMiTumorSampleTypeURI = String.format("%s%s", PDXNET_NAMESPACE, "TumorSampleType");
        this.pdxTumorSampleType = rdfModel.createClass(pdxMiTumorSampleTypeURI);
        this.pdxTumorSampleType.addProperty(RDFS.label, "Tumor Sample Type");

        String pdxMiModelCharacterizationURI = String.format("%s%s", PDXNET_NAMESPACE, "ModelCharacterization");
        this.pdxModelCharacterization = rdfModel.createClass(pdxMiModelCharacterizationURI);
        this.pdxModelCharacterization.addProperty(RDFS.label, "Model Characterization");

        String pdxMiPatientConsentURI = String.format("%s%s", PDXNET_NAMESPACE, "PatientConsent");
        this.pdxPatientConsent = rdfModel.createClass(pdxMiPatientConsentURI);
        this.pdxPatientConsent.addProperty(RDFS.label, "Patient Consent");

        String pdxMiTreatmentForEngraftmentURI = String.format("%s%s", PDXNET_NAMESPACE, "TreatmentForEngraftment");
        this.pdxTreatmentForEngraftment = rdfModel.createClass(pdxMiTreatmentForEngraftmentURI);
        this.pdxTreatmentForEngraftment.addProperty(RDFS.label, "Mouse treatment for engraftment");

        String pdxMiPassageQaPerformedURI = String.format("%s%s", PDXNET_NAMESPACE, "PassageQaPerformed");
        this.pdxPassageQaPerformed = rdfModel.createClass(pdxMiPassageQaPerformedURI);
        this.pdxPassageQaPerformed.addProperty(RDFS.label, "Passage QA performed");

        String pdxMiTissueOfOriginURI = String.format("%s%s", PDXNET_NAMESPACE, "TissueOfOrigin");
        this.pdxTissueOfOrigin = rdfModel.createClass(pdxMiTissueOfOriginURI);
        this.pdxTissueOfOrigin.addProperty(RDFS.label, "Tissue of origin");

        String pdxMiTumorCategoryURI = String.format("%s%s", PDXNET_NAMESPACE, "TumorCategory");
        this.pdxTumorCategory = rdfModel.createClass(pdxMiTumorCategoryURI);
        this.pdxTumorCategory.addProperty(RDFS.label, "Tumor category");

        String pdxMiTumorGradeURI = String.format("%s%s", PDXNET_NAMESPACE, "TumorGrade");
        this.pdxTumorGrade = rdfModel.createClass(pdxMiTumorGradeURI);
        this.pdxTumorGrade.addProperty(RDFS.label, "Tumor grade");

        String pdxMiTumorStageURI = String.format("%s%s", PDXNET_NAMESPACE, "TumorStage");
        this.pdxTumorStage = rdfModel.createClass(pdxMiTumorStageURI);
        this.pdxTumorStage.addProperty(RDFS.label, "Tumor stage");

        String pdxMiTumorHistologyURI = String.format("%s%s", PDXNET_NAMESPACE, "TumorHistology");
        this.pdxTumorHistology = rdfModel.createClass(pdxMiTumorHistologyURI);
        this.pdxTumorHistology.addProperty(RDFS.label, "Tumor histology");

        String pdxMiBooleanURI = String.format("%s%s", PDXNET_NAMESPACE, "Boolean");
        this.pdxBoolean = rdfModel.createClass(pdxMiBooleanURI);
        this.pdxBoolean.addProperty(RDFS.label, "Boolean");
    }

    private void outputPdxModel(PdxModel pdxmodel) {

        // Clinincal/Patient Module
        outputPatientRDF(pdxmodel);
        // Clinical/Tumor Module
        outputTumorRDF(pdxmodel.getClinicalTumor());
        // Model Creation Module
        outputModelCreationRdf(pdxmodel);
        // Quality Assurance Module
        outputQualityAssuranceRdf(pdxmodel);
        // to do -- other areas of the PDX-MI
        outputModelStudyRDF(pdxmodel);
    }

    /**
     * Todo add a suffix depending on the total number of diagnosis this patient
     * has. For now just _01.
     */
    private void outputPatientRDF(PdxModel pdxmodel) {
        PdxPatient patient = pdxmodel.getPatient();
        String diagnosis = patient.getDiagnosis().getId();
        String diagnosisURI = String.format("%s%s", NCIT_NAMESPACE, diagnosis);
        String patientURI = String.format("%s%s", PDXNET_NAMESPACE, patient.getSubmitterPatientID());
        this.thisTumorSample = rdfModel.createResource(String.format("%s%s", PDXNET_NAMESPACE, pdxmodel.getClinicalTumor().getSubmitterTumorID()));

        Resource diagnosisResource = rdfModel.createResource(diagnosisURI);
        Resource sex = patient.getSex().equals(Sex.FEMALE) ? femaleSex : maleSex;
        Resource consent = patient.getConsent().equals(Consent.YES) ? yesConsent
                : patient.getConsent().equals(Consent.NO) ? noConsent : academicConsent;

        consent.addProperty(RDF.type, this.pdxPatientConsent);

        diagnosisResource.addProperty(RDF.type, this.pdxDiagnosis);

        this.thisPatient
                = rdfModel.createResource(patientURI)
                .addProperty(RDFS.label, patient.getSubmitterPatientID())
                .addProperty(RDF.type, this.pdxPatient)
                .addProperty(hasPatientIdProperty, patient.getSubmitterPatientID())
                .addProperty(hasDiagnosisProperty, diagnosisResource)
                .addProperty(hasTumorProperty, thisTumorSample)
                .addProperty(sexProperty, sex)
                .addProperty(consentProperty, consent)
                .addProperty(ethnicityProperty, patient.getEthnicityRace().getEthnicityString());

        this.thisPatient.addProperty(ageBinLowerRange,
                ResourceFactory.createTypedLiteral(String.valueOf(patient.getAge().getLower()),
                        XSDDatatype.XSDinteger));
        this.thisPatient.addProperty(ageBinUpperRange,
                ResourceFactory.createTypedLiteral(String.valueOf(patient.getAge().getUpper()),
                        XSDDatatype.XSDinteger));

        // start working on nexted PT treatments
        // need to define RFD properties first
        for (PdxPatientTreatment ptTreatment : patient.getPatientTreatments()) {
            // treatment uri patient/drug/index?
            //this.thisPatient.addProperty(hasTreatment,
                    

        }
    }

    private void outputTumorRDF(PdxClinicalTumor clintumor) {
        //String tumorURI=String.format("%s%s",PDXNET_NAMESPACE,clintumor.getSubmitterTumorID());

        Resource category = rdfModel.createResource(String.format("%s%s", NCIT_NAMESPACE, clintumor.getCategory().getId()));
        category.addProperty(RDF.type, this.pdxTumorCategory);
        //    category.addProperty(RDFS.label,"Tumor category");

        Resource tissue = rdfModel.createResource(UBERON_NAMESPACE + clintumor.getTissueOfOrigin().getId());
        tissue.addProperty(RDF.type, this.pdxTissueOfOrigin);
        //   tissue.addProperty(RDFS.label, "Tumor tissue of origin");

        Resource histology = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getTissueHistology().getId());
        histology.addProperty(RDF.type, this.pdxTumorHistology);
        //    histology.addProperty(RDFS.label,"Tumor histology");

        Resource stage = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getStage().getId());
        stage.addProperty(RDF.type, this.pdxTumorStage);
        //     stage.addProperty(RDFS.label,"Tumor stage");

        Resource grade = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getTumorGrade().getId());
        grade.addProperty(RDF.type, this.pdxTumorGrade);
        //     grade.addProperty(RDFS.label, "Tumor grade");

        this.thisTumorSample.addProperty(hasSubmitterTumorIdProperty, clintumor.getSubmitterTumorID())
                .addProperty(hasTissueOfOriginProperty, tissue)
                .addProperty(hasTumorHistologyProperty, histology)
                .addProperty(hasStageProperty, stage)
                .addProperty(hasTumorGradeProperty, grade)
                .addProperty(hasTumorCategoryProperty, category);

        this.thisTumorSample.addProperty(RDF.type, this.pdxClinicalTumor);
        this.thisTumorSample.addProperty(RDFS.label, clintumor.getSubmitterTumorID());

        // bi directional
        this.thisTumorSample.addProperty(fromPatientProperty, this.thisPatient);
    }

    private void outputModelStudyRDF(PdxModel model) {
        PdxModelStudy modelStudy = model.getModelStudy();
        // how is a model study identified? it should have a name or ID
        // this naming convention won't work for multiple studies for the same model
        // this.thisModelStudy = rdfModel.createResource(String.format("%s%s",PDXNET_NAMESPACE,"Study "+model.getModelCreation().getSubmitterPdxId()));
        this.thisModelStudy = rdfModel.createResource(PDXNET_NAMESPACE + "Study_" + model.getModelCreation().getSubmitterPdxId());

        this.thisModelStudy.addProperty(hasPdxModelProperty, this.thisPdxModelCreation);

        Resource tissue = rdfModel.createResource(UBERON_NAMESPACE + modelStudy.getMetastasisLocation().getId());

        ResponseToTreatment response = modelStudy.getResponse();
        switch (response) {
            case NOT_ASSESSED:
                this.thisModelStudy.addProperty(pdxTumorResponseProperty, notAssessed);
                break;
            case STABLE_DISEASE:
                this.thisModelStudy.addProperty(pdxTumorResponseProperty, stableDisease);
                break;
            case PARTIAL_RESPONSE:
                this.thisModelStudy.addProperty(pdxTumorResponseProperty, partialResponse);
                break;
            case COMPLETE_RESPONSE:
                this.thisModelStudy.addProperty(pdxTumorResponseProperty, completeResponse);
                break;
            case PROGRESSIVE_DISEASE:
                this.thisModelStudy.addProperty(pdxTumorResponseProperty, progressiveDisease);
                break;
        }

        int index = 0;
        for (PdxStudyTreatment studyTreatment : modelStudy.getTreatments()) {
            index++;
            // is this sufficent for an Id?
            Resource treatment = rdfModel.createResource(PDXNET_NAMESPACE + modelStudy.getStudyID()+"/treatment"+index); 
            treatment.addProperty(studyTreatmentDrug, studyTreatment.getDrug());
            treatment.addProperty(studyTreatmentDose, studyTreatment.getDose());
            treatment.addProperty(studyTreatmentUnits, studyTreatment.getUnits());
            treatment.addProperty(studyTreatmentRoute, studyTreatment.getRoute());
            treatment.addProperty(studyTreatmentFrequency, studyTreatment.getFrequency());
            treatment.addProperty(hasStudyProperty, this.thisModelStudy);
            
            this.thisModelStudy.addProperty(hasStudyTreatmentProperty, treatment);

        }
        this.thisModelStudy.addProperty(pdxDoublingLagTimeProperty, ResourceFactory.createTypedLiteral(String.valueOf(modelStudy.getDoublingLagTime()),
                XSDDatatype.XSDinteger))
                .addProperty(pdxStudyHasMetastasisProperty, modelStudy.isMetastasis() ? TRUE_RESOURCE : FALSE_RESOURCE)
                .addProperty(pdxStudyMetastasisLocationProperty, tissue)
                .addProperty(pdxStudyMetastasisPassageProperty, ResourceFactory.createTypedLiteral(String.valueOf(modelStudy.getMetastasisPassage()),
                        XSDDatatype.XSDinteger));

        this.thisModelStudy.addProperty(RDF.type, this.pdxModelStudy);
        this.thisModelStudy.addProperty(RDFS.label, "Model study for " + model.getModelCreation().getSubmitterPdxId());

        this.thisPdxModelCreation.addProperty(hasStudyProperty, this.thisModelStudy);
    }

    private void outputModelCreationRdf(PdxModel model) {
        PdxModelCreation mcreation = model.getModelCreation();
        this.thisPdxModelCreation = rdfModel.createResource(PDXNET_NAMESPACE + mcreation.getSubmitterPdxId());

        this.thisTumorSample.addProperty(hasPdxModelProperty, this.thisPdxModelCreation);
        this.thisPdxModelCreation.addProperty(hasTumorProperty, thisTumorSample);
        this.thisPdxModelCreation.addProperty(hasStrainProperty, mcreation.getMouseStrain());
        this.thisPdxModelCreation.addProperty(mouseSourceProperty, mcreation.getMouseSource());
        if (mcreation.isStrainImmuneSystemHumanized()) {
            this.thisPdxModelCreation.addProperty(strainHumanizedProperty, TRUE_RESOURCE);
            this.thisPdxModelCreation.addProperty(humanizationTypeProperty, mcreation.getHumanizationType());
        } else {
            this.thisPdxModelCreation.addProperty(strainHumanizedProperty, FALSE_RESOURCE);
        }
        TumorPrepMethod prep = mcreation.getTumorPreparation();
        switch (prep) {
            case SOLID:
                this.thisPdxModelCreation.addProperty(tumorPreparation, tumorPrepSolid);
                break;
            case ASCITES:
                this.thisPdxModelCreation.addProperty(tumorPreparation, tumorPrepAscites);
                break;
            case SUSPENSION:
                this.thisPdxModelCreation.addProperty(tumorPreparation, tumorPrepSuspension);
                break;
        }
        MouseTreatmentForEngraftment rx = mcreation.getMouseTreatmentForEngraftment();
        switch (rx) {
            case GCSF:
                this.thisPdxModelCreation.addProperty(mouseTreatmentForEngraftment, mouseRxGCSF);
                break;
            case ESTROGEN:
                this.thisPdxModelCreation.addProperty(mouseTreatmentForEngraftment, mouseRxEstrogen);
                break;
        }
        this.thisPdxModelCreation.addProperty(engraftmentPercentProperty,
                ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentRate()),
                        XSDDatatype.XSDdecimal));
        this.thisPdxModelCreation.addProperty(engraftTimeInDaysProperty,
                ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentTimeInDays()),
                        XSDDatatype.XSDinteger));

        this.thisPdxModelCreation.addProperty(RDF.type, this.pdxModelCreation);
        this.thisPdxModelCreation.addProperty(RDFS.label, "PDX Model " + mcreation.getSubmitterPdxId());

    }

    private void outputQualityAssuranceRdf(PdxModel model) {
        PdxQualityAssurance quality = model.getQualityAssurance();
        ResponseToTreatment response = quality.getResponse();
        switch (response) {
            case NOT_ASSESSED:
                this.thisPdxModelCreation.addProperty(pdxTumorResponseProperty, notAssessed);
                break;
            case STABLE_DISEASE:
                this.thisPdxModelCreation.addProperty(pdxTumorResponseProperty, stableDisease);
                break;
            case PARTIAL_RESPONSE:
                this.thisPdxModelCreation.addProperty(pdxTumorResponseProperty, partialResponse);
                break;
            case COMPLETE_RESPONSE:
                this.thisPdxModelCreation.addProperty(pdxTumorResponseProperty, completeResponse);
                break;
            case PROGRESSIVE_DISEASE:
                this.thisPdxModelCreation.addProperty(pdxTumorResponseProperty, progressiveDisease);
                break;
        }
        ModelCharacterization characterization = quality.getTumorCharacterizationTechnology();
        switch (characterization) {
            case IHC:
                this.thisPdxModelCreation.addProperty(hasTumorCharacterizationProperty, IHC);
            case HISTOLOGY:
                this.thisPdxModelCreation.addProperty(hasTumorCharacterizationProperty, histology);
        }

        // do these FALSE and TRUE resources need to be typed seperately?
        // so this adds the qa values directly to the model should these fields be part of the ModelCreation Module?
        if (quality.isAnimalHealthStatusSufficient()) {
            this.thisPdxModelCreation.addProperty(animalHealthStatusSatisfactoryProperty, TRUE_RESOURCE);
        } else {
            this.thisPdxModelCreation.addProperty(animalHealthStatusSatisfactoryProperty, FALSE_RESOURCE);
        }
        if (quality.isPassageQaPerformed()) {
            this.thisPdxModelCreation.addProperty(passageQaPerformedProperty, TRUE_RESOURCE);
        } else {
            this.thisPdxModelCreation.addProperty(passageQaPerformedProperty, FALSE_RESOURCE);
        }
        if (quality.isTumorNotMouseNotEbv()) {
            this.thisPdxModelCreation.addProperty(tumorNotEbvNotMouseProperty, TRUE_RESOURCE);
        } else {
            this.thisPdxModelCreation.addProperty(tumorNotEbvNotMouseProperty, FALSE_RESOURCE);
        }

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

        this.yesConsent = rdfModel.createResource(PDXNET_NAMESPACE + "consent_YES");
        this.yesConsent.addProperty(RDFS.label, "Patient consent provided");
        this.yesConsent.addProperty(RDF.type, this.pdxPatientConsent);

        this.academicConsent = rdfModel.createResource(PDXNET_NAMESPACE + "consent_ACADEMIC_ONLY");
        this.academicConsent.addProperty(RDFS.label, "Academic consent only");
        this.academicConsent.addProperty(RDF.type, this.pdxPatientConsent);

        this.TRUE_RESOURCE = rdfModel.createResource(PDXNET_NAMESPACE + "True");
        this.TRUE_RESOURCE.addProperty(RDFS.label, "True");
        this.TRUE_RESOURCE.addProperty(RDF.type, this.pdxBoolean);

        this.FALSE_RESOURCE = rdfModel.createResource(PDXNET_NAMESPACE + "False");
        this.FALSE_RESOURCE.addProperty(RDFS.label, "False");
        this.FALSE_RESOURCE.addProperty(RDF.type, this.pdxBoolean);

        this.tumorPrepSolid = rdfModel.createResource(PDXNET_NAMESPACE + "Solid");
        this.tumorPrepSuspension = rdfModel.createResource(PDXNET_NAMESPACE + "Suspension");
        this.tumorPrepAscites = rdfModel.createResource(PDXNET_NAMESPACE + "Ascites");

        this.tumorPrepSolid.addProperty(RDFS.label, "Tumor preperation solid");
        this.tumorPrepSuspension.addProperty(RDFS.label, "Tumor preperation suspension");
        this.tumorPrepAscites.addProperty(RDFS.label, "Tumor preperation ascites");

        this.tumorPrepSolid.addProperty(RDF.type, this.pdxTumorSampleType);
        this.tumorPrepSuspension.addProperty(RDF.type, this.pdxTumorSampleType);
        this.tumorPrepAscites.addProperty(RDF.type, this.pdxTumorSampleType);

        // treatment for engfatement
        this.mouseRxGCSF = rdfModel.createResource(PDXNET_NAMESPACE + "G-CSF");
        this.mouseRxGCSF.addProperty(RDFS.label, "G-CSF treatment for engraftment");
        this.mouseRxGCSF.addProperty(RDF.type, this.pdxTreatmentForEngraftment);

        this.mouseRxEstrogen = rdfModel.createResource(PDXNET_NAMESPACE + "Estrogen");
        this.mouseRxEstrogen.addProperty(RDFS.label, "Estrogen treatment for engraftment");
        this.mouseRxEstrogen.addProperty(RDF.type, this.pdxTreatmentForEngraftment);

        this.notAssessed = rdfModel.createResource(PDXNET_NAMESPACE + "Not_assessed");
        this.completeResponse = rdfModel.createResource(PDXNET_NAMESPACE + "Complete_response");
        this.partialResponse = rdfModel.createResource(PDXNET_NAMESPACE + "Partial_response");
        this.stableDisease = rdfModel.createResource(PDXNET_NAMESPACE + "Stable_disease");
        this.progressiveDisease = rdfModel.createResource(PDXNET_NAMESPACE + "Progressive_disease");

        this.notAssessed.addProperty(RDFS.label, "Not assessed");
        this.completeResponse.addProperty(RDFS.label, "Complete response");
        this.partialResponse.addProperty(RDFS.label, "Partial response");
        this.stableDisease.addProperty(RDFS.label, "Stable disease");
        this.progressiveDisease.addProperty(RDFS.label, "Progressive disease");

        this.notAssessed.addProperty(RDF.type, this.pdxTreatmentResponse);
        this.completeResponse.addProperty(RDF.type, this.pdxTreatmentResponse);
        this.partialResponse.addProperty(RDF.type, this.pdxTreatmentResponse);
        this.stableDisease.addProperty(RDF.type, this.pdxTreatmentResponse);
        this.progressiveDisease.addProperty(RDF.type, this.pdxTreatmentResponse);

        this.IHC = rdfModel.createResource(PDXNET_NAMESPACE + "IHC");
        this.histology = rdfModel.createResource(PDXNET_NAMESPACE + "Histology");
        this.IHC.addProperty(RDF.type, this.pdxModelCharacterization);
        this.IHC.addProperty(RDFS.label, "IHC");

        this.histology.addProperty(RDF.type, this.pdxModelCharacterization);
        this.histology.addProperty(RDFS.label, "Histology");

    }

    private void specifyPrefixes() {
        // unique idenfitifer for the patient
        this.hasPatientIdProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "haPatientID");
        this.hasPatientIdProperty.addProperty(RDFS.label, "Unique identifer for patient");
        this.hasPatientIdProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Patient's Initial Clinical Diagnosis
        this.hasDiagnosisProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "hasDiagnosis");
        this.hasDiagnosisProperty.addProperty(RDFS.label, "Patient's initial clincal diagnosis");
        this.hasDiagnosisProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasDiagnosisProperty.addProperty(RDFS.domain, this.pdxPatient);
        this.hasDiagnosisProperty.addProperty(RDFS.range, this.pdxDiagnosis);

        // Unique tumor id
        this.hasSubmitterTumorIdProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "hasSubmitterTumorId");
        this.hasSubmitterTumorIdProperty.addProperty(RDFS.label, "Unique identifer for sampled tissue");
        this.hasSubmitterTumorIdProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Tumor Sample Object
        this.hasTumorProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasTumor");
        this.hasTumorProperty.addProperty(RDFS.label, "Patient tumor sample");
        this.hasTumorProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorProperty.addProperty(RDFS.range, this.pdxClinicalTumor);

        this.fromPatientProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasPatient");
        this.fromPatientProperty.addProperty(RDFS.label, "Derived from patient");
        this.fromPatientProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.fromPatientProperty.addProperty(RDFS.range, this.pdxPatient);

        this.hasStudyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStudy");
        this.hasStudyProperty.addProperty(RDFS.label, "Has model study");
        this.hasStudyProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasStudyProperty.addProperty(RDFS.range, this.pdxModelStudy);

        this.hasStudyTreatmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStudyTreatment");
        this.hasStudyTreatmentProperty.addProperty(RDFS.label, "Has study treatment");
        this.hasStudyTreatmentProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasStudyTreatmentProperty.addProperty(RDFS.range, this.pdxStudyTreatment);

        this.hasPatientTreatmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasPatientTreatment");
        this.hasPatientTreatmentProperty.addProperty(RDFS.label, "Has patient treatment");
        this.hasPatientTreatmentProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasPatientTreatmentProperty.addProperty(RDFS.range, this.pdxPatientTreatment);

        // Not sure what this is, it isn't used.
        //this.cancerDiagnosis = rdfModel.createProperty( PDXNET_NAMESPACE + "cancerDiagnosis" );
        //this.cancerDiagnosis.addProperty(RDFS.label, "");
        // this.cancerDiagnosis.addProperty(RDF.type,OWL.ObjectProperty);
        // Patient sex
        this.sexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasSex");
        this.sexProperty.addProperty(RDFS.label, "Has sex");
        this.sexProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.sexProperty.addProperty(RDFS.range, this.pdxSex);

        // Patient has provided consent to share data
        this.consentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasConsent");
        this.consentProperty.addProperty(RDFS.label, "Patient consent to share data");
        this.consentProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Ethnicity of patient
        this.ethnicityProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasEthnicity");
        this.ethnicityProperty.addProperty(RDFS.label, "Patient ethnicity");
        this.ethnicityProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Patient tumor tissue of origin
        this.hasTissueOfOriginProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasTissueOfOrigin");
        this.hasTissueOfOriginProperty.addProperty(RDFS.label, "Tumor's tissue of origin");
        this.hasTissueOfOriginProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Tumor Category
        this.hasTumorCategoryProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasTumorCategory");
        this.hasTumorCategoryProperty.addProperty(RDFS.label, "Tumor category");
        this.hasTumorCategoryProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Histology for tumor
        this.hasTumorHistologyProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasTumorHistology");
        this.hasTumorHistologyProperty.addProperty(RDFS.label, "Pathologist's Histologic Diagnosis");
        this.hasTumorHistologyProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorHistologyProperty.addProperty(RDFS.range, this.pdxDiagnosis);

        // Grade of tumor
        this.hasTumorGradeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasTumorGrade");
        this.hasTumorGradeProperty.addProperty(RDFS.label, "Tumor grade");
        this.hasTumorGradeProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Stage of tumor
        this.hasStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStage");
        this.hasStageProperty.addProperty(RDFS.label, "Tumor stage");
        this.hasStageProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // PDX model generated from patient tumor
        this.hasPdxModelProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasPdxModel");
        this.hasPdxModelProperty.addProperty(RDFS.label, "PDX model generated from patient tissue");
        this.hasPdxModelProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Mouse strain for PDX model
        this.hasStrainProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStrain");
        this.hasStrainProperty.addProperty(RDFS.label, "Mouse strain used for engraftment");
        this.hasStrainProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Is the mouse strain humanized
        this.strainHumanizedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasHumanization");
        this.strainHumanizedProperty.addProperty(RDFS.label, "Was mouse strain humanized");
        this.strainHumanizedProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Type of mouse humanization
        this.humanizationTypeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasHumanizationType");
        this.humanizationTypeProperty.addProperty(RDFS.label, "Type of mouse humanization");
        this.humanizationTypeProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Type of tumor preperation
        this.tumorPreparation = rdfModel.createProperty(PDXNET_NAMESPACE, "hasTumorPreparation");
        this.tumorPreparation.addProperty(RDFS.label, "Type of tumor preparation for engraftment");
        this.tumorPreparation.addProperty(RDF.type, OWL.ObjectProperty);

        // Instituion providing mouse
        this.mouseSourceProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasSource");
        this.mouseSourceProperty.addProperty(RDFS.label, "Institution providing mouse");
        this.mouseSourceProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Treatement of mouse prior to engraftment
        this.mouseTreatmentForEngraftment = rdfModel.createProperty(PDXNET_NAMESPACE, "hasTreatmentForEngraftment");
        this.mouseTreatmentForEngraftment.addProperty(RDFS.label, "Mouse treatment for engraftment");
        this.mouseTreatmentForEngraftment.addProperty(RDF.type, OWL.ObjectProperty);

        // Percent of successful engraftments 
        this.engraftmentPercentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasEngraftmentPercent");
        this.engraftmentPercentProperty.addProperty(RDFS.label, "Engraftment percent");
        this.engraftmentPercentProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Days for successful engraftment
        this.engraftTimeInDaysProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasEngraftmentTimeInDays");
        this.engraftTimeInDaysProperty.addProperty(RDFS.label, "Engraftment time in days");
        this.engraftTimeInDaysProperty.addProperty(RDF.type, OWL.DatatypeProperty);

        // Tumor Characterization
        this.hasTumorCharacterizationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasTumorCharacterization");
        this.hasTumorCharacterizationProperty.addProperty(RDFS.label, "Tumor Characterization");
        this.hasTumorCharacterizationProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Tumor is not EBV or mouse tissue
        this.tumorNotEbvNotMouseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasNotEbvNotMouseStatus");
        this.tumorNotEbvNotMouseProperty.addProperty(RDFS.label, "Mouse is not EBV positive, tumor tissue is not mouse origin");
        this.tumorNotEbvNotMouseProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // PDX model response to treatment
        this.pdxTumorResponseProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasTumorResponse");
        this.pdxTumorResponseProperty.addProperty(RDFS.label, "Tumor Response");
        this.pdxTumorResponseProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // The pdx model's health status
        this.animalHealthStatusSatisfactoryProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasOKAnimalHealthStatus");
        this.animalHealthStatusSatisfactoryProperty.addProperty(RDFS.label, "Animal health status is ok");
        this.animalHealthStatusSatisfactoryProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Passage on which QA was performed
        this.passageQaPerformedProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasPassageQAperformed");
        this.passageQaPerformedProperty.addProperty(RDFS.label, "Passage QA performed");
        this.passageQaPerformedProperty.addProperty(RDF.type, OWL.ObjectProperty);

        // Patient current treatment drug
        this.currentTreatmentDrug = rdfModel.createProperty(PDXNET_NAMESPACE, "hasCurrentTreatmentDrug");
        this.currentTreatmentDrug.addProperty(RDFS.label, "Current patient treatment drug");
        this.currentTreatmentDrug.addProperty(RDF.type, OWL.DatatypeProperty);

        // Lower age range for Patient when sample was taken
        this.ageBinLowerRange = rdfModel.createProperty(PDXNET_NAMESPACE, "hasAgeBinLowerRange");
        this.ageBinLowerRange.addProperty(RDFS.label, "Lower range of 5 year age bin");
        this.ageBinLowerRange.addProperty(RDFS.domain, pdxPatient);
        this.ageBinLowerRange.addProperty(RDF.type, OWL.DatatypeProperty);

        // Upper age range for Patient when sample was taken
        this.ageBinUpperRange = rdfModel.createProperty(PDXNET_NAMESPACE, "hasAgeBinUpperRange");
        this.ageBinUpperRange.addProperty(RDFS.label, "Upper range of 5 year age bin");
        this.ageBinUpperRange.addProperty(RDFS.domain, pdxPatient);
        this.ageBinUpperRange.addProperty(RDF.type, OWL.DatatypeProperty);

        this.pdxStudyTreatmentProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStudyTreatment");
        this.pdxStudyTreatmentProperty.addProperty(RDFS.label, "Study treatment");
        this.pdxStudyTreatmentProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.pdxDoublingLagTimeProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasDoublingLagTime");
        this.pdxDoublingLagTimeProperty.addProperty(RDFS.label, "Doubling lag time");
        this.pdxDoublingLagTimeProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.pdxStudyHasMetastasisProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasMetastasis");
        this.pdxStudyHasMetastasisProperty.addProperty(RDFS.label, "Study model has metastasis");
        this.pdxStudyHasMetastasisProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.pdxStudyMetastasisLocationProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasMetastasisLocation");
        this.pdxStudyMetastasisLocationProperty.addProperty(RDFS.label, "Model metastasis location");
        this.pdxStudyMetastasisLocationProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.pdxStudyMetastasisPassageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasMetastasisPassage");
        this.pdxStudyMetastasisPassageProperty.addProperty(RDFS.label, "Model metastasis passage");
        this.pdxStudyMetastasisPassageProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.patientTreatmentIndexProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasPatientTreatmentIndex");
        this.patientTreatmentIndexProperty.addProperty(RDFS.label, "Patient treatment index");
        this.patientTreatmentIndexProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.patientTreatmentPostSampleProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "hasPatientTreatmentPostSample");
        this.patientTreatmentPostSampleProperty.addProperty(RDFS.label, "Patient treatment post sample");
        this.patientTreatmentPostSampleProperty.addProperty(RDF.type, OWL.ObjectProperty);

        this.patientTreatmentRegimen = rdfModel.createProperty(PDXNET_NAMESPACE, "hasPatientTreatmentRegimen");
        this.patientTreatmentRegimen.addProperty(RDFS.label, "Patient treatment regimen");
        this.patientTreatmentRegimen.addProperty(RDF.type, OWL.ObjectProperty);

        this.patientTreatmentResponse = rdfModel.createProperty(PDXNET_NAMESPACE, "hasPatientTreatmentResponse");
        this.patientTreatmentResponse.addProperty(RDFS.label, "Patient treatment reponse");
        this.patientTreatmentResponse.addProperty(RDF.type, OWL.ObjectProperty);
        this.patientTreatmentResponse.addProperty(RDFS.range, this.pdxTreatmentResponse);

        this.patientTreatmentReasonStopped = rdfModel.createProperty(PDXNET_NAMESPACE, "hasPatientTreatmentReasonStopped");
        this.patientTreatmentReasonStopped.addProperty(RDFS.label, "Patient treatment reason stopped");
        this.patientTreatmentReasonStopped.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentDrug = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStudyTreatmentDrug");
        this.studyTreatmentDrug.addProperty(RDFS.label, "Study treatment drug");
        this.studyTreatmentDrug.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentDose = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStudyTreatmentDose");
        this.studyTreatmentDose.addProperty(RDFS.label, "Study treatment dose");
        this.studyTreatmentDose.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentUnits = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStudyTreatmentUnits");
        this.studyTreatmentUnits.addProperty(RDFS.label, "Study treatment units");
        this.studyTreatmentUnits.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentRoute = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStudyTreatmentRoute");
        this.studyTreatmentRoute.addProperty(RDFS.label, "Study treatment route");
        this.studyTreatmentRoute.addProperty(RDF.type, OWL.ObjectProperty);

        this.studyTreatmentFrequency = rdfModel.createProperty(PDXNET_NAMESPACE, "hasStudyTreatmentFrequency");
        this.studyTreatmentFrequency.addProperty(RDFS.label, "Study treatment frequency");
        this.studyTreatmentFrequency.addProperty(RDF.type, OWL.ObjectProperty);

        rdfModel.setNsPrefix("PDXNET", PDXNET_NAMESPACE);
        rdfModel.setNsPrefix("NCIT", NCIT_NAMESPACE);
        rdfModel.setNsPrefix("UBERON", UBERON_NAMESPACE);

    }

}
