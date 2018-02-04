package org.jax.pdxintegrator.command;

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
    }




    private void queryPatientIDs() {
        String queryString =
                "PREFIX pdxnet: <http://pdxnetwork/pdxmodel#> \n" +
                        "SELECT ?patient_id ?consent ?diagnosis \n" +
                        "WHERE { \n"+
                        "      ?x pdxnet:patient_id ?patient_id . \n" +
                        "      ?x pdxnet:consent ?consent . \n" +
                        "      ?x pdxnet:hasDiagnosis ?diagnosis . \n" +
                        "      }";
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
