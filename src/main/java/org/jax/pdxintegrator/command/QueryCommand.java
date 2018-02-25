package org.jax.pdxintegrator.command;

import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class QueryCommand extends Command{
    private static final Logger logger = LogManager.getLogger();

    private final String infilename;

    private Model model;

    public QueryCommand(String filename) {
       infilename=filename;
    }

    @Override
    public void execute() {
        inputModel();
        System.out.println();
        System.out.println("DEMONSTRATION OF SQARQL QUERIES");
        System.out.println();
        queryPatientIDs();
        separator();
        queryNcitDiagnoses();
    }


    private void separator() {
        System.out.println();
        System.out.println("###########  Next Query  ###########  Next Query");
        System.out.println();
    }

    private void queryPatientIDs() {
        String queryString =
                "PREFIX pdxnet: <http://pdxnetwork/pdxmodel_> \n" +
                        "PREFIX ncit: <http://purl.obolibrary.org/obo/NCIT_> \n" +
                        "SELECT ?patient_id ?consent ?diagnosis \n" +
                        "WHERE { \n"+
                        "      ?x pdxnet:patient_id ?patient_id . \n" +
                        "      ?x pdxnet:consent ?consent . \n" +
                        "      ?x pdxnet:hasDiagnosis ?diagnosis . \n" +
                        "      } \n" +
                        "LIMIT 5";
        System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results, query);
        qe.close();
    }


/*
 PDXNET:consent               PDXNET:consent_NO ;
        PDXNET:currentTreatmentDrug  "Cetuximab[DB00002;205923-56-4]" ;
        PDXNET:ethnicity             "Sephardic" ;
        PDXNET:gender                PDXNET:female ;
        PDXNET:hasDiagnosis          NCIT:C141206 ;

 */
    private void queryNcitDiagnoses() {
        String queryString =
                "PREFIX pdxnet: <http://pdxnetwork/pdxmodel_> \n" +
                        "PREFIX ncit: <http://purl.obolibrary.org/obo/NCIT_> \n" +
                        "PREFIX uberon: <http://purl.obolibrary.org/obo/UBERON_> \n" +
                        "SELECT ?patient_id ?currentTreatmentDrug ?diagnosis \n" +
                        "WHERE { \n"+
                        "      ?x pdxnet:patient_id ?patient_id . \n" +
                        "      ?x pdxnet:currentTreatmentDrug ?currentTreatmentDrug . \n" +
                        "      ?x pdxnet:gender pdxnet:female . \n" +
                        "      ?x pdxnet:hasDiagnosis ?diagnosis . \n" +
                        "      } \n" +
                        "LIMIT 5";
        System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results, query);
        qe.close();
    }



    private void inputModel() {
        try {
            InputStream in = new FileInputStream(new File(infilename));
            this.model = ModelFactory.createDefaultModel();
            this.model.read(in,null); // null base URI, since model URIs are absolute
            in.close();

        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*


// Create a new query


         */
    }



}
