package org.jax.pdxintegrator.io;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * This class uses the <a href="https://github.com/phenomics/ontolib">ontolb</a> library to
 * parse both the {@code hp.obo} file and the phenotype annotation file
 * {@code phenotype_annotation.tab}
 * (see <a href="http://human-phenotype-ontology.github.io/">HPO Homepage</a>).
 * @author Peter Robinson
 * @author Vida Ravanmehr
 * @version 0.1.1 (2017-11-15)
 */
public class HpoOntologyParser {
    private static final Logger logger = LogManager.getLogger();
    /** Path to the {@code hp.obo} file. */
    private String hpoOntologyPath=null;


    Ontology inheritanceSubontology=null;
    Ontology abnormalPhenoSubOntology=null;
    /** Map of all of the Phenotypic abnormality terms (i.e., not the inheritance terms). */
    private Map<TermId, Term> termmap=null;

    public HpoOntologyParser(String path){
        hpoOntologyPath=path;
    }

    /**
     * Parse the HP ontology file and place the data in {@link #abnormalPhenoSubOntology} and
     * {@link #inheritanceSubontology}.
     * @throws IOException
     */
    public void parseOntology() throws IOException {
        Ontology hpo;

        TermId inheritId = TermId.of("HP:0000005");
        TermId phenotypicAbId= TermId.of("HP:0000118");

        hpo =  OntologyLoader.loadOntology(new File(this.hpoOntologyPath));
        this.abnormalPhenoSubOntology = hpo.subOntology(phenotypicAbId);
        this.inheritanceSubontology = hpo.subOntology(inheritId);
    }

    public Ontology getPhenotypeSubontology() { return this.abnormalPhenoSubOntology; }
    public Ontology getInheritanceSubontology() { return  this.inheritanceSubontology; }





}
