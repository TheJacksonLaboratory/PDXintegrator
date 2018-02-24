package org.jax.pdxintegrator.rdf;


import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.datatypes.xsd.impl.XSDBaseNumericType;
import org.apache.jena.datatypes.xsd.impl.XSDDouble;
import org.apache.jena.rdf.model.*;
import org.jax.pdxintegrator.exception.PDXException;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.modelcreation.MouseTreatmentForEngraftment;
import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.modelcreation.TumorPrepMethod;
import org.jax.pdxintegrator.model.patient.Consent;
import org.jax.pdxintegrator.model.patient.Gender;
import org.jax.pdxintegrator.model.patient.PdxPatient;
import org.jax.pdxintegrator.model.qualityassurance.ModelCharacterization;
import org.jax.pdxintegrator.model.qualityassurance.PdxQualityAssurance;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToStandardOfCare;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;

import java.io.OutputStream;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * This class coordinates the transformation of one or more {@link org.jax.pdxintegrator.model.PdxModel} objects
 * as an RDF graph.
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxModel2Rdf {

    private final List<PdxModel> pdxmodels;


    /** "Root" of the entire RDF graph. */
    private Model rdfModel = ModelFactory.createDefaultModel();

    // RDF properties needed throughout the model

    private Property hasPatientIdProperty=null;
    private Property hasSubmitterTumorIdProperty=null;
    private Property hasDiagnosisProperty=null;
    private Property hasTumorProperty=null;
    private Property hasTissueOfOriginProperty=null;
    private Property hasTumorCategoryProperty=null;
    private Property hasTumorHistologyProperty=null;
    private Property hasTumorGradeProperty=null;
    private Property hasStageProperty=null;
    private Property hasPdxModelProperty=null;
    private Property hasStrainProperty=null;
    private Property mouseSourceProperty=null;
    private Property strainHumanizedProperty=null;
    private Property humanizationTypeProperty=null;
    private Property tumorPreparation=null;
    private Property mouseTreatmentForEngraftment=null;
    private Property engraftmentPercentProperty=null;
    private Property engraftTimeInDaysProperty=null;
    private Property hasTumorCharacterizationProperty=null;
    private Property tumorNotEbvNotMouseProperty=null;
    private Property pdxTumorResponseProperty=null;
    private Property animalHealthStatusSatisfactoryProperty=null;
    private Property passageQaPerformedProperty=null;
    /** This property specifies the NCIT diagnosis of a patient's diagnosis. */
    private Property cancerDiagnosis=null;
    /** This property specifies the gender. ToDO decide on whether to use NCIT for this. */
    private Property genderProperty=null;
    /** This property specifies the age range. ToDo -- decide whether to use bins for this etc. */
    private Property ageProperty=null;
    /** This property specifies the consent given by the patient. TODO enough? */
    private Property consentProperty=null;
    /** This property specifies the population group of the patient. */
    private Property ethnicityProperty=null;

    private Resource maleSex=null;
    private Resource femaleSex=null;
    private Resource noConsent=null;
    private Resource yesConsent=null;
    private Resource TRUE_RESOURCE=null;
    private Resource FALSE_RESOURCE=null;
    private Resource academicConsent=null;
    private Resource tumorSample=null;
    /** Tumor preparation types */
    private Resource tumorPrepSolid,tumorPrepSuspension,tumorPrepAscites;
    /** Mouse treatment types */
    private Resource mouseRxGCSF,mouseRxEstrogen;
    /** PDX Tumor response types */
    private Resource notAssessed, completeResponse, partialResponse, stableDisease, progressiveDisease;
    /** Tumor characterization types */
    private Resource IHC,histology;



    private Resource thisPatient=null;
    private Resource thisPdxModel=null;

    private static final String PDXNET_NAMESPACE = "http://pdxnetwork/pdxmodel_";
    private final static String NCIT_NAMESPACE = "http://purl.obolibrary.org/obo/NCIT_";
    private final static String UBERON_NAMESPACE ="http://purl.obolibrary.org/obo/UBERON_";


    public PdxModel2Rdf(List<PdxModel> modelList) {
        this.pdxmodels=modelList;
    }


    public void outputRDF(OutputStream out) {
        specifyPrefixes();
        createEntities();
        for (PdxModel pdxmod : pdxmodels) {
            outputModelRDF(pdxmod);
        }
        System.out.println( "# -- PATIENT -- #" );
        rdfModel.write(System.out,"Turtle");
        //rdfModel.write(System.out,"RDF/XML");//// now write the model in XML form to a file
        //rdfModel.write(System.out, "RDF/XML-ABBREV");
        //rdfModel.write(System.out, "N-TRIPLES");
        rdfModel.write( out );//,"Turtle");
    }


    private void outputModelRDF(PdxModel pdxmodel) {
        outputPatientRDF(pdxmodel);
        outputTumorRDF(pdxmodel.getClinicalTumor());
        outputModelCreationRdf(pdxmodel);
        outputQualityAssuranceRdf(pdxmodel);
        // to do -- other areas of the PDX-MI
    }



    /**
     * Todo add a suffix depending on the total number of diagnosis this patient has. For now just _01.
     */
    private void outputPatientRDF(PdxModel pdxmodel) {
        PdxPatient patient = pdxmodel.getPatient();
        String diagnosis = patient.getDiagnosis().getId();
        String diagnosisURI=String.format("%s%s",NCIT_NAMESPACE,diagnosis);
        String patientURI=String.format("%s%s",PDXNET_NAMESPACE,patient.getSubmitterPatientID());
        this.tumorSample = rdfModel.createResource(String.format("%s%s",PDXNET_NAMESPACE,pdxmodel.getClinicalTumor().getSubmitterTumorID()));
        Resource diagnosisResource = rdfModel.createResource(diagnosisURI);
        Resource sex = patient.getGender().equals(Gender.FEMALE) ? femaleSex : maleSex;
        Resource consent = patient.getConsent().equals(Consent.YES) ? yesConsent :
                patient.getConsent().equals(Consent.NO) ? noConsent : academicConsent;

       this.thisPatient
                = rdfModel.createResource(patientURI)
                .addProperty(hasPatientIdProperty,patient.getSubmitterPatientID())
                .addProperty(hasDiagnosisProperty, diagnosisResource)
                .addProperty(hasTumorProperty,tumorSample)
                .addProperty(genderProperty,sex)
                .addProperty(ageProperty,patient.getAge().getAgeString())
                .addProperty(consentProperty,consent)
                .addProperty(ethnicityProperty,patient.getEthnicityRace().getEthnicityString());
    }


    private void outputTumorRDF(PdxClinicalTumor clintumor) {
        String tumorURI=String.format("%s%s",PDXNET_NAMESPACE,clintumor.getSubmitterTumorID());
        Resource category = rdfModel.createResource(String.format("%s%s",NCIT_NAMESPACE,clintumor.getCategory().getId()));
        Resource tissue = rdfModel.createResource(UBERON_NAMESPACE +clintumor.getTissueOfOrigin().getId());
        Resource histology = rdfModel.createResource(NCIT_NAMESPACE+clintumor.getTissueHistology().getId());
        Resource stage = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getStage().getId());
        Resource grade = rdfModel.createResource(NCIT_NAMESPACE + clintumor.getTumorGrade().getId());
        this.tumorSample.addProperty(hasSubmitterTumorIdProperty,clintumor.getSubmitterTumorID())
                .addProperty(hasTissueOfOriginProperty,tissue)
                .addProperty(hasTumorHistologyProperty,histology)
                .addProperty(hasStageProperty,stage)
                .addProperty(hasTumorGradeProperty,grade)
                .addProperty(hasTumorCategoryProperty,category);
    }



    private void outputModelCreationRdf(PdxModel model ) {
        PdxModelCreation mcreation = model.getModelCreation();
        this.thisPdxModel = rdfModel.createResource(PDXNET_NAMESPACE + mcreation.getSubmitterPdxId());
        this.thisPatient.addProperty(hasPdxModelProperty,this.thisPdxModel);
        this.thisPdxModel.addProperty(hasStrainProperty,mcreation.getMouseStrain());
        this.thisPdxModel.addProperty(mouseSourceProperty,mcreation.getMouseSource());
        if (mcreation.isStrainImmuneSystemHumanized()) {
            this.thisPdxModel.addProperty(strainHumanizedProperty,TRUE_RESOURCE);
            this.thisPdxModel.addProperty(humanizationTypeProperty,mcreation.getHumanizationType());
        } else {
            this.thisPdxModel.addProperty(strainHumanizedProperty,FALSE_RESOURCE);
        }
        TumorPrepMethod prep = mcreation.getTumorPreparation();
        switch (prep) {
            case SOLID:this.thisPdxModel.addProperty(tumorPreparation,tumorPrepSolid);
            break;
            case ASCITES:this.thisPdxModel.addProperty(tumorPreparation,tumorPrepAscites);
            break;
            case SUSPENSION:this.thisPdxModel.addProperty(tumorPreparation,tumorPrepSuspension);
            break;
        }
        MouseTreatmentForEngraftment rx = mcreation.getMouseTreatmentForEngraftment();
        switch (rx) {
            case GCSF: this.thisPdxModel.addProperty(mouseTreatmentForEngraftment,mouseRxGCSF); break;
            case ESTROGEN: this.thisPdxModel.addProperty(mouseTreatmentForEngraftment,mouseRxEstrogen); break;
        }
        this.thisPdxModel.addProperty(engraftmentPercentProperty,
                ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentRate()),
                XSDDatatype.XSDdecimal));
        this.thisPdxModel.addProperty(engraftTimeInDaysProperty,
                ResourceFactory.createTypedLiteral(String.valueOf(mcreation.getEngraftmentTimeInDays()),
                        XSDDatatype.XSDinteger));
    }


    private void outputQualityAssuranceRdf(PdxModel model) {
        PdxQualityAssurance quality = model.getQualityAssurance();
        ResponseToStandardOfCare response = quality.getResponse();
        switch (response) {
            case NOT_ASSESSED: this.thisPdxModel.addProperty(pdxTumorResponseProperty,notAssessed); break;
            case STABLE_DISEASE: this.thisPdxModel.addProperty(pdxTumorResponseProperty,stableDisease); break;
            case PARTIAL_RESPONSE:this.thisPdxModel.addProperty(pdxTumorResponseProperty,partialResponse); break;
            case COMPLETE_RESPONSE:this.thisPdxModel.addProperty(pdxTumorResponseProperty,completeResponse); break;
            case PROGRESSIVE_DISEASE:this.thisPdxModel.addProperty(pdxTumorResponseProperty,progressiveDisease); break;
        }
        ModelCharacterization characterization = quality.getTumorCharacterizationTechnology();
        switch (characterization) {
            case IHC: this.thisPdxModel.addProperty(hasTumorCharacterizationProperty,IHC);
            case HISTOLOGY:this.thisPdxModel.addProperty(hasTumorCharacterizationProperty,histology);
        }
        if (quality.isAnimalHealthStatusSufficient()) {
            this.thisPdxModel.addProperty(animalHealthStatusSatisfactoryProperty,TRUE_RESOURCE);
        } else {
            this.thisPdxModel.addProperty(animalHealthStatusSatisfactoryProperty,FALSE_RESOURCE);
        }
        if (quality.isPassageQaPerformed()) {
            this.thisPdxModel.addProperty(passageQaPerformedProperty,TRUE_RESOURCE);
        } else {
            this.thisPdxModel.addProperty(passageQaPerformedProperty,FALSE_RESOURCE);
        }
        if (quality.isTumorNotMouseNotEbv()) {
            this.thisPdxModel.addProperty(tumorNotEbvNotMouseProperty,TRUE_RESOURCE);
        } else {
            this.thisPdxModel.addProperty(tumorNotEbvNotMouseProperty,FALSE_RESOURCE);
        }

    }




    private void createEntities() {
        this.maleSex=rdfModel.createResource(PDXNET_NAMESPACE + "male");
        this.femaleSex = rdfModel.createResource( PDXNET_NAMESPACE + "female" );
        this.noConsent = rdfModel.createResource(PDXNET_NAMESPACE+"consent_NO");
        this.yesConsent = rdfModel.createResource(PDXNET_NAMESPACE+"consent_YES");
        this.academicConsent = rdfModel.createResource(PDXNET_NAMESPACE+"consent_ACADEMIC_ONLY");
        this.TRUE_RESOURCE = rdfModel.createResource(PDXNET_NAMESPACE+"True");
        this.FALSE_RESOURCE = rdfModel.createResource(PDXNET_NAMESPACE+"False");
        //tumor solid, cell suspension, asite
        this.tumorPrepSolid = rdfModel.createResource(PDXNET_NAMESPACE+"Solid");
        this.tumorPrepSuspension = rdfModel.createResource(PDXNET_NAMESPACE+"Suspension");
        this.tumorPrepAscites = rdfModel.createResource(PDXNET_NAMESPACE+"Ascites");
        this.mouseRxGCSF = rdfModel.createResource(PDXNET_NAMESPACE+"G-CSF");
        this.mouseRxEstrogen = rdfModel.createResource(PDXNET_NAMESPACE+"Estrogen");


        this.notAssessed= rdfModel.createResource(PDXNET_NAMESPACE+"Not_assessed");
        this.completeResponse= rdfModel.createResource(PDXNET_NAMESPACE+"Complete_response");
        this.partialResponse = rdfModel.createResource(PDXNET_NAMESPACE+"Partial_response");
        this.stableDisease = rdfModel.createResource(PDXNET_NAMESPACE+"Stable_disease");
        this.progressiveDisease = rdfModel.createResource(PDXNET_NAMESPACE+"Progressive_disease");

        this.IHC = rdfModel.createResource(PDXNET_NAMESPACE+"IHC");
        this.histology = rdfModel.createResource(PDXNET_NAMESPACE+"Histology");

    }



    private void specifyPrefixes() {
        this.hasPatientIdProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "patient_id");
        this.hasDiagnosisProperty = rdfModel.createProperty( PDXNET_NAMESPACE + "hasDiagnosis" );
        this.hasSubmitterTumorIdProperty = rdfModel.createProperty(PDXNET_NAMESPACE + "hasSubmitterTumorId");
        this.hasTumorProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"hasTumor");
        this.cancerDiagnosis = rdfModel.createProperty( PDXNET_NAMESPACE + "cancerDiagnosis" );
        this.genderProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"gender");
        this.ageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "age_group");
        this.consentProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"consent");
        this.ethnicityProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"ethnicity");
        this.hasTissueOfOriginProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"tissueOfOrigin");
        this.hasTumorCategoryProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"tumorCategory");
        this.hasTumorHistologyProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"tumorHistology");
        this.hasTumorGradeProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"tumorGrade");
        this.hasStageProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"stage");
        this.hasPdxModelProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"hasPdxModel");
        this.hasStrainProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"hasStrain");
        this.strainHumanizedProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"strainHumanized");
        this.humanizationTypeProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"humanizationType");
        this.tumorPreparation = rdfModel.createProperty(PDXNET_NAMESPACE,"tumorPreparation");
        this.mouseSourceProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"mouseSource");
        this.mouseTreatmentForEngraftment = rdfModel.createProperty(PDXNET_NAMESPACE,"mouseTreatmentForEngraftment");
        this.engraftmentPercentProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"engraftmentPercent");
        this.engraftTimeInDaysProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"engraftmentTimeInDays");
        this.hasTumorCharacterizationProperty= rdfModel.createProperty(PDXNET_NAMESPACE,"pdxTumorCharacterization");
        this.tumorNotEbvNotMouseProperty= rdfModel.createProperty(PDXNET_NAMESPACE,"notEbvNotMouse");
        this.pdxTumorResponseProperty= rdfModel.createProperty(PDXNET_NAMESPACE,"pdxTumorResponse");
        this.animalHealthStatusSatisfactoryProperty= rdfModel.createProperty(PDXNET_NAMESPACE,"animalHealthStatusOk");
        this.passageQaPerformedProperty= rdfModel.createProperty(PDXNET_NAMESPACE,"passageQAperformed");
        rdfModel.setNsPrefix( "PDXNET", PDXNET_NAMESPACE);
        rdfModel.setNsPrefix( "NCIT", NCIT_NAMESPACE);
        rdfModel.setNsPrefix("UBERON",UBERON_NAMESPACE);

    }



}
