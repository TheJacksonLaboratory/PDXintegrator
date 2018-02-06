package org.jax.pdxintegrator.ncit;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;

public class NciJenaParser {
    private static final Logger logger = LogManager.getLogger();
    String nciThesaurusRdfXmlPath="data/Thesaurus.owl";




    public NciJenaParser() {

    }



    public void parse() {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);


        try {
            InputStream inputStream = new FileInputStream(nciThesaurusRdfXmlPath);
            logger.trace("Opening " + nciThesaurusRdfXmlPath);
            model.read(inputStream, "RDF/XML");
            logger.trace("model: " + model.toString());
            //model.read(inputStream, "OWL/XML");
            inputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ExtendedIterator<Individual> itI = model.listIndividuals();

        while (itI.hasNext()) {
            Individual i = itI.next();
            System.out.println(i.getLocalName());
        }
    }
}
