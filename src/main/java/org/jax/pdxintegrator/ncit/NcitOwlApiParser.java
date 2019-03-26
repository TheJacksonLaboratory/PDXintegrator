package org.jax.pdxintegrator.ncit;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.exception.PDXException;
import org.jax.pdxintegrator.ncit.neoplasm.NcitTerm;
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

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A parser for the ncit.obo file that makes use of OWLAPI and extracts lists of {@link NcitTerm} objects.
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class NcitOwlApiParser {
    private static final Logger logger = LogManager.getLogger();

    private final String NcitOboPath;

    private List<NcitTerm> termlist=new ArrayList<>();
    private HashMap<String, NcitTerm> termMap = new HashMap<>();

    private List<NcitTerm> gradeTermList=new ArrayList<>();

    private List<NcitTerm> stageTermList=new ArrayList<>();


    public NcitOwlApiParser(String path) {
        this.NcitOboPath=path;
    }

    private Set<OWLAnnotation> getAllAnnotationAxioms(OWLClass cls, OWLOntology ontology) {
        Set<OWLAnnotation> axioms=new HashSet<>();
        EntitySearcher.getAnnotations(cls.getIRI(), ontology).forEach(axioms::add);
       return axioms;
    }


    private void addTerm(List<NcitTerm> list, String termId, String termLabel) {
        termLabel=termLabel.replace("^^xsd:string","");
        termLabel=termLabel.replaceAll("\"","");
        try {
            NcitTerm term = new NcitTerm(termId,termLabel);
            list.add(term);
            termMap.put(termLabel.toLowerCase(),term);
        } catch (PDXException pde) {
            pde.printStackTrace();
        }

    }

    /**
     * We parse the (large) ncit file and extract all the terms from some interesting subontologies
     * neoplasm: the hierarchy of cancer diagnoses
     * grade: // Disease Grade Qualifier (Code C28076) terms like "Grade 2"
     */
    public void parse() {
        OBOFormatParser oboparser = new OBOFormatParser();
        logger.trace("Beginning parse of ncit.obo (this may take a few moments)...");
        try {
            //OBODoc doc =oboparser.parse(NcitOboPath);
            final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(NcitOboPath));


//            OWLDataFactory datafactory = manager.getOWLDataFactory();
//            OWLObjectProperty hasId = datafactory.getOWLObjectProperty(IRI.create("http://www.geneontology.org/formats/oboInOwl#id"));
//            OWLAPIObo2Owl obo2owl = new OWLAPIObo2Owl(manager);
//            OWLOntology ontology = obo2owl.convert(doc);
            StructuralReasonerFactory factory = new StructuralReasonerFactory();
            OWLReasoner reasoner = factory.createReasoner(ontology);
            OWLClass neoplasm = manager.getOWLDataFactory().getOWLClass(IRI.create("http://purl.obolibrary.org/obo/NCIT_C3262"));
            OWLClass diseaseGradeQualifier = manager.getOWLDataFactory().getOWLClass("http://purl.obolibrary.org/obo/NCIT_C28076");
            OWLClass stageClass = manager.getOWLDataFactory().getOWLClass("http://purl.obolibrary.org/obo/NCIT_C16899"); // term for Stage
            NodeSet<OWLClass> neoplasms = reasoner.getSubClasses(neoplasm);   //.getSubclasses(neoplasm, false).getFlattened();
            for (Node<OWLClass> node : neoplasms) {
                node.entities().forEach( ne -> {
                    IRI iri = ne.getIRI();
                    Set<OWLAnnotation> annotationAxioms = getAllAnnotationAxioms(ne,ontology);
                    for (OWLAnnotation annot : annotationAxioms) {
                        //System.out.println("\t"+annot.toString());
                        if (annot.getProperty().isLabel()) {
                            String label = annot.getValue().toString();
                         
                         //   System.out.println("Term:"+label);
                           addTerm(termlist,iri.getShortForm(),label);
                           break;
                        }
                    }
                });
            }
            // now the grade subontology
            NodeSet<OWLClass> gradeTerms = reasoner.getSubClasses(diseaseGradeQualifier);
            for (Node<OWLClass> node : gradeTerms) {
                node.entities().forEach( ne -> {
                    IRI iri = ne.getIRI();
                    Set<OWLAnnotation> annotationAxioms = getAllAnnotationAxioms(ne,ontology);
                    for (OWLAnnotation annot : annotationAxioms) {
                        
                        if (annot.getProperty().isLabel()) {
                            String label = annot.getValue().toString();
                            if(label.replaceAll("\"","").startsWith("Grade"))
                      //      System.out.println("Grade:"+label+" "+iri.getShortForm());
                            
                            addTerm(gradeTermList,iri.getShortForm(),label);
                            break;
                        }
                    }
                });
            }
            // now the stage subontology
            NodeSet<OWLClass> stageTerms = reasoner.getSubClasses(stageClass);
            for (Node<OWLClass> node : stageTerms) {
                node.entities().forEach(ne -> {
                    IRI iri = ne.getIRI();
                    Set<OWLAnnotation> annotationAxioms = getAllAnnotationAxioms(ne, ontology);
                    for (OWLAnnotation annot : annotationAxioms) {
                       
                        if (annot.getProperty().isLabel()) {
                            String label = annot.getValue().toString();
                      //      System.out.println("Stage:"+label);
                            addTerm(stageTermList, iri.getShortForm(), label);
                            break;
                        }
                    }
                });
            }
        } catch (OWLOntologyCreationException oce) {
            oce.printStackTrace();
        }
        logger.trace("We ingested " + termlist.size() + " NCIT Neoplasm terms");
    }

    public List<NcitTerm> getTermlist() {
        return termlist;
    }
    public List<NcitTerm> getGradeTermList() { return gradeTermList; }
    public List<NcitTerm> getStageTermList() { return stageTermList; }
    
    public NcitTerm getNeoplasm(String term){
        return termMap.get(term.toLowerCase());
    }
}
