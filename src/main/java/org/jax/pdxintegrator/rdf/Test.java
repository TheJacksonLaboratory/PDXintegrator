/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jax.pdxintegrator.rdf;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.BadURIException;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 *
 * @author sbn
 */
public class Test {

    private static final String PDXNET_NAMESPACE = "http://pdxnetwork.org/pdxmodel";
    private final static String NCIT_NAMESPACE = "http://purl.obolibrary.org/obo/NCIT_";
    OntModel rdfModel;

    private Property hasTumorTissueHistologyTermProperty;
    private OntClass pdxTumorHistology;
    private OntClass pdxClinicalTumor;
    
    public static void main(String[] args) {
        Test t = new Test();
        t.build();

    }

    public void build() {
        this.rdfModel = ModelFactory.createOntologyModel();

        String pdxTumorHistologyURI = String.format("%s%s", PDXNET_NAMESPACE, "#TumorHistology");
        this.pdxTumorHistology = rdfModel.createClass(pdxTumorHistologyURI);
        this.pdxTumorHistology.addProperty(RDFS.label, "Tumor histology");
        
        String pdxClinicalTumorURI = String.format("%s%s", PDXNET_NAMESPACE, "#ClinicalTumor");
        this.pdxClinicalTumor = rdfModel.createClass(pdxClinicalTumorURI);
        this.pdxClinicalTumor.addProperty(RDFS.label, "Clinical Tumor");


        this.hasTumorTissueHistologyTermProperty = rdfModel.createProperty(PDXNET_NAMESPACE, "#hasTumorTissueHistologyTerm");
        this.hasTumorTissueHistologyTermProperty.addProperty(RDFS.label, "has Tumor tissue histology term");
        this.hasTumorTissueHistologyTermProperty.addProperty(RDF.type, OWL.ObjectProperty);
        this.hasTumorTissueHistologyTermProperty.addProperty(RDFS.range, this.pdxTumorHistology);
        
        Resource tumorSample = rdfModel.createResource(String.format("%s/%s", PDXNET_NAMESPACE + "/tumor", "TumorID"));
        
        tumorSample.addProperty(RDF.type, this.pdxClinicalTumor);
        tumorSample.addProperty(RDFS.label, "Tumor ID");

        
        
        Resource histologyTerm = rdfModel.createResource(NCIT_NAMESPACE + "1234");
        histologyTerm.addProperty(RDF.type, this.pdxTumorHistology);
        tumorSample.addProperty(hasTumorTissueHistologyTermProperty, histologyTerm);
        
        
        rdfModel.write(System.out, "Turtle");
        //rdfModel.write(System.out,"RDF/XML");//// now write the model in XML form to a file
        //rdfModel.write(System.out, "RDF/XML-ABBREV");
        //rdfModel.write(System.out, "N-TRIPLES");
        try {
            rdfModel.write(System.out);//,"Turtle");
        } catch (BadURIException bue) {
            bue.printStackTrace();
        }


    }

}
