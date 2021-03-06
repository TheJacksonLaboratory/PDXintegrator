package org.jax.pdxintegrator.uberon;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.exception.PDXException;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.obolibrary.obo2owl.OWLAPIObo2Owl;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.TermId;

public class UberonOntologyParser {
    private static final Logger logger = LogManager.getLogger();
    /** Path to the {@code hp.obo} file. */
    private String uberonPath =null;

    private List<UberonTerm> termlist=new ArrayList<>();
    private HashMap<String,UberonTerm> termMap = new HashMap<>();


    Ontology inheritanceSubontology=null;
    Ontology abnormalPhenoSubOntology=null;
    /** Map of all of the Phenotypic abnormality terms (i.e., not the inheritance terms). */
    private Map<TermId, Term> termmap=null;

    public UberonOntologyParser(String path){
        uberonPath =path;
    }

   

    private void addTerm(String termId, String termLabel) {
        termLabel=termLabel.replace("^^xsd:string","");
        termLabel=termLabel.replaceAll("\"","");
        try {
            UberonTerm term = new UberonTerm(termId,termLabel);
            termlist.add(term);
            termMap.put(termLabel.toLowerCase(),term);
           
        } catch (PDXException pde) {
            pde.printStackTrace();
        }

    }


    public void parse() {
        OBOFormatParser oboparser = new OBOFormatParser();
        logger.trace("Beginning parse of uberon-basic.obo (this may take a few moments)...");
        try {
            OBODoc doc =oboparser.parse(uberonPath);
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLDataFactory datafactory = manager.getOWLDataFactory();
            OWLObjectProperty hasId = datafactory.getOWLObjectProperty(IRI.create("http://www.geneontology.org/formats/oboInOwl#id"));
            OWLAPIObo2Owl obo2owl = new OWLAPIObo2Owl(manager);
            OWLOntology ontology = obo2owl.convert(doc);
            StructuralReasonerFactory factory = new StructuralReasonerFactory();
            OWLReasoner reasoner = factory.createReasoner(ontology);
            Stream<OWLClass> classes = ontology.classesInSignature();
            classes.forEach( ne -> {
                    IRI iri = ne.getIRI();
                    Set<OWLAnnotation> annotationAxioms = getAllAnnotationAxioms(ne,ontology);
                    for (OWLAnnotation annot : annotationAxioms) {
                        //System.out.println("\t"+annot.toString());
                        if (annot.getProperty().isLabel()) {
                            String label = annot.getValue().toString();
                            addTerm(iri.getShortForm(),label);
                            
                            break;
                        }
                    }
                });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (OWLOntologyCreationException oce) {
            oce.printStackTrace();
        }
        logger.trace("We ingested " + termlist.size() + " Uberon terms");
    }

    private Set<OWLAnnotation> getAllAnnotationAxioms(OWLClass cls, OWLOntology ontology) {
        Set<OWLAnnotation> axioms=new HashSet<>();
        EntitySearcher.getAnnotations(cls.getIRI(), ontology).forEach(axioms::add);
        return axioms;
    }

    public List<UberonTerm> getTermlist() {
        return termlist;
    }
    
    public UberonTerm getUberonTerm(String name){
        return termMap.get(name.toLowerCase());
    }
}
