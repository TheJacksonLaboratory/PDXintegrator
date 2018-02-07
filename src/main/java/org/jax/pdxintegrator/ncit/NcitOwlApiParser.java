package org.jax.pdxintegrator.ncit;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.exception.PDXException;
import org.jax.pdxintegrator.ncit.neoplasm.NcitNeoplasmTerm;
import org.obolibrary.obo2owl.OWLAPIObo2Owl;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.io.IOException;
import java.util.*;

/**
 * A parser for the ncit.obo file that makes use of OWLAPI and extracts lists of {@link NcitNeoplasmTerm} objects.
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class NcitOwlApiParser {
    private static final Logger logger = LogManager.getLogger();

    private final String NcitOboPath;

    private List<NcitNeoplasmTerm> termlist=new ArrayList<>();


    public NcitOwlApiParser(String path) {
        this.NcitOboPath=path;
    }

    public Set<OWLAnnotation> getAllAnnotationAxioms(OWLClass cls, OWLOntology ontology) {
        Set<OWLAnnotation> axioms=new HashSet<>();
        EntitySearcher.getAnnotations(cls.getIRI(), ontology).forEach( annot -> axioms.add(annot));
       return axioms;
    }


    private void addTerm(String termId, String termLabel) {
        termLabel=termLabel.replace("^^xsd:string","");
        termLabel=termLabel.replaceAll("\"","");
        try {
            NcitNeoplasmTerm term = new NcitNeoplasmTerm(termId,termLabel);
            termlist.add(term);
        } catch (PDXException pde) {
            pde.printStackTrace();
        }

    }


    public void parse() {
        OBOFormatParser oboparser = new OBOFormatParser();
        logger.trace("Beginning parse of ncit.obo (this may take a few moments)...");
        try {
            OBODoc doc =oboparser.parse(NcitOboPath);
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLDataFactory datafactory = manager.getOWLDataFactory();
            OWLObjectProperty hasId = datafactory.getOWLObjectProperty(IRI.create("http://www.geneontology.org/formats/oboInOwl#id"));
            OWLAPIObo2Owl obo2owl = new OWLAPIObo2Owl(manager);
            OWLOntology ontology = obo2owl.convert(doc);
            StructuralReasonerFactory factory = new StructuralReasonerFactory();
            OWLReasoner reasoner = factory.createReasoner(ontology);
            OWLClass neoplasm = manager.getOWLDataFactory().getOWLClass(IRI.create("http://purl.obolibrary.org/obo/NCIT_C3262"));
            NodeSet<OWLClass> neoplasms = reasoner.getSubClasses(neoplasm);   //.getSubclasses(neoplasm, false).getFlattened();
            for (Node<OWLClass> node : neoplasms) {
                node.entities().forEach( ne -> {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OWLOntologyCreationException oce) {
            oce.printStackTrace();
        }
        logger.trace("We ingested " + termlist.size() + " NCIT Neoplasm terms");
    }

    public List<NcitNeoplasmTerm> getTermlist() {
        return termlist;
    }
}
