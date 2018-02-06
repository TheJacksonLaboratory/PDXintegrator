package org.jax.pdxintegrator.ncit;

import org.apache.jena.base.Sys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.obolibrary.obo2owl.OWLAPIObo2Owl;
import org.obolibrary.oboformat.model.Clause;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class NcitOwlApiParser {
    private static final Logger logger = LogManager.getLogger();

    private final String NcitOboPath;


    public NcitOwlApiParser(String path) {
        this.NcitOboPath=path;
    }


    public void parse() {
        OBOFormatParser oboparser = new OBOFormatParser();
        try {
            OBODoc doc =oboparser.parse(NcitOboPath);
            OWLOntologyManager m = OWLManager.createOWLOntologyManager();
            OWLAPIObo2Owl obo2owl = new OWLAPIObo2Owl(m);
            OWLOntology ontology = obo2owl.convert(doc);
//            ontology.
//            logger.trace(String.format("I got %d terms",doc.getTermFrames().size()));
            Collection<Frame> frames = doc.getTermFrames();
            Frame neoplasm = doc.getInstanceFrame("NCIT:C37915");
            if (neoplasm == null) {
                System.out.println("Could not retrieve C37915");
                int c=0;
                for (Frame f : frames) {
                    System.out.println(f.toString());
                    String id = f.getId();
                    System.out.println("id=\"" + id + "\"");
                    System.out.println("frametype: " + f.getType().toString());
                    Collection<Clause> clauses= f.getClauses();
                    for (Clause cl : clauses) {
                        System.out.println("\t clause " + cl.toString()) ;
                    }

                    Set<String> tags =f.getTags();
                    for (String t: tags) {
                        System.out.println("tag "+ t + ": "+ f.getTagValue(t));
                    }
                    if (c++>100)break;
                }
            } else {
                System.out.println(neoplasm.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OWLOntologyCreationException oce) {
            oce.printStackTrace();
        }
    }
}
