package org.jax.pdxintegrator.rdf;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.patient.Consent;
import org.jax.pdxintegrator.model.patient.Gender;
import org.jax.pdxintegrator.model.patient.PdxPatient;
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
    private Resource academicConsent=null;
    private Resource tumorSample=null;

    private static final String PDXNET_NAMESPACE = "http://pdxnetwork/pdxmodel#";
    private final static String NCIT_NAMESPACE = "http://purl.obolibrary.org/obo/NCIT#";
    private final static String UBERON_NAMESPACE ="http://purl.obolibrary.org/obo/UBERON#";


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
        rdfModel.write( out );//,"Turtle");
    }


    private void outputModelRDF(PdxModel pdxmodel) {
        outputPatientRDF(pdxmodel);
        outputTumorRDF(pdxmodel.getClinicalTumor());
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

        Resource thisPatient
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




    private void createEntities() {
        this.maleSex=rdfModel.createResource(PDXNET_NAMESPACE + "male");
        this.femaleSex = rdfModel.createResource( PDXNET_NAMESPACE + "female" );
        this.noConsent = rdfModel.createResource(PDXNET_NAMESPACE+"consent_NO");
        this.yesConsent = rdfModel.createResource(PDXNET_NAMESPACE+"consent_YES");
        this.academicConsent = rdfModel.createResource(PDXNET_NAMESPACE+"consent_ACADEMIC_ONLY");
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
        rdfModel.setNsPrefix( "PDXNET", PDXNET_NAMESPACE);
        rdfModel.setNsPrefix( "NCIT", NCIT_NAMESPACE);
        rdfModel.setNsPrefix("UBERON",UBERON_NAMESPACE);

    }



}
