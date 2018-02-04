package org.jax.pdxintegrator.rdf;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.patient.Consent;
import org.jax.pdxintegrator.model.patient.Gender;
import org.jax.pdxintegrator.model.patient.PdxPatient;

import java.io.OutputStream;
import java.util.List;

/**
 * This class coordinates the transformation of one or more {@link org.jax.pdxintegrator.model.PdxModel} objects
 * as an RDF graph.
 */
public class PdxModel2Rdf {

    private final List<PdxModel> pdxmodels;


    /** "Root" of the entire RDF graph. */
    private Model rdfModel = ModelFactory.createDefaultModel();

    // RDF properties needed throughout the model

    Property hasPatientIdProperty=null;
    Property hasDiagnosisProperty=null;
    /** This property specifies the NCIT diagnosis of a patient's diagnosis. */
    Property cancerDiagnosis=null;
    /** This property specifies the gender. ToDO decide on whether to use NCIT for this. */
    Property genderProperty=null;
    /** This property specifies the age range. ToDo -- decide whether to use bins for this etc. */
    Property ageProperty=null;
    /** This property specifies the consent given by the patient. TODO enough? */
    Property consentProperty=null;
    /** This property specifies the population group of the patient. */
    Property ethnicityProperty=null;

    Resource maleSex=null;
    Resource femaleSex=null;
    Resource noConsent=null;
    Resource yesConsent=null;
    Resource academicConsent=null;

    private static final String PDXNET_NAMESPACE = "http://pdxnetwork/pdxmodel#";
    private final static String NCIT_NAMESPACE = "http://purl.obolibrary.org/obo/NCIT#";


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
        outputPatientRDF(pdxmodel.getPatient());
        // to do -- other areas of the PDX-MI
    }



    /**
     * Todo add a suffix depending on the total number of diagnosis this patient has. For now just _01.
     */
    private void outputPatientRDF(PdxPatient patient) {
        String diagnosis = patient.getDiagnosis().getIdWithPrefix() + "_01";
        String diagnosisURI=String.format("%s%s",PDXNET_NAMESPACE,diagnosis);
        String patientURI=String.format("%s%s",PDXNET_NAMESPACE,patient.getSubmitterPatientID());
        Resource diagnosisResource = rdfModel.createResource(diagnosisURI)
                .addProperty(cancerDiagnosis,patient.getDiagnosis().getIdWithPrefix());
        Resource sex = patient.getGender().equals(Gender.FEMALE) ? femaleSex : maleSex;
        // todo -- check one of these is true
        Resource consent = patient.getConsent().equals(Consent.YES) ? yesConsent :
                patient.getConsent().equals(Consent.NO) ? noConsent : academicConsent;
        // todo -- check one of these is true

        Resource thisPatient
                = rdfModel.createResource(patientURI)
                .addProperty(hasPatientIdProperty,patient.getSubmitterPatientID())
                .addProperty(hasDiagnosisProperty, diagnosisURI)
                .addProperty(genderProperty,maleSex)
                .addProperty(ageProperty,patient.getAge().getAgeString())
                .addProperty(consentProperty,consent)
                .addProperty(ethnicityProperty,patient.getEthnicityRace().getEthnicityString());
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
        this.cancerDiagnosis = rdfModel.createProperty( PDXNET_NAMESPACE + "cancerDiagnosis" );
        this.genderProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"gender");
        this.ageProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "age_group");
        this.consentProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"consent");
        this.ethnicityProperty = rdfModel.createProperty(PDXNET_NAMESPACE,"ethnicity");
        rdfModel.setNsPrefix( "pdxnet", PDXNET_NAMESPACE);
        rdfModel.setNsPrefix( "ncit", NCIT_NAMESPACE);

    }



}
