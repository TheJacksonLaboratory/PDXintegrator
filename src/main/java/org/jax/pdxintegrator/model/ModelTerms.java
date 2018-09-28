/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jax.pdxintegrator.model;

import com.github.phenomics.ontolib.ontology.data.TermId;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.jax.pdxintegrator.model.ModelTerms.NCIT;
import static org.jax.pdxintegrator.model.ModelTerms.UBERON;
import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;
import org.jax.pdxintegrator.ncit.NcitOwlApiParser;
import org.jax.pdxintegrator.ncit.neoplasm.NcitTerm;
import org.jax.pdxintegrator.uberon.UberonOntologyParser;
import org.jax.pdxintegrator.uberon.UberonTerm;

/**
 * Manages populating OntologyTerms for a PDXModel object
 *
 * Clinical Diagnosis--NCIT Tissue Histology -- NCIT Primary Tissue Origin --
 * UBERON Specimen Tissue --UBERON Metastatic Sites --UBERON
 *
 *
 *
 * @author sbn
 */
public class ModelTerms {

    static final String UBERON = "Uberon";
    static final String NCIT = "NCIT";
    static final String NOTFOUND = "NOT FOUND";

    NcitOwlApiParser ncitParser;
    UberonOntologyParser uberonParser;

   
    
    HashMap<String, String> missingUBERON = new HashMap<>();
    HashMap<String, String> missingNCIT = new HashMap<>();

    HashMap<String, String> ncitMappings = new HashMap<>();
    HashMap<String, String> uberonMappings = new HashMap<>();

    public void findTerms(List<PdxModel> models) {
        ncitParser = new NcitOwlApiParser("data/ncit.obo");
        ncitParser.parse();

        uberonParser = new UberonOntologyParser("data/basic.obo");
        uberonParser.parse();

        String pdtc = "";
        String mappingFile="";

        for (PdxModel model : models) {
            // don't need a new file if the PDTC didn't change
            if(!pdtc.equals(model.getPDTC())){
                pdtc = model.getPDTC();
                mappingFile = "data/"+model.getPDTC()+"mappingFile.txt";
                loadMappings(mappingFile);
            }
            for (PdxClinicalTumor tumor : model.getClinicalTumor()) {
                
                tumor.setInitialDiagnosisTerm(getNcitNeoplasmTerm(tumor.getInitialDiagnosis(),tumor.getTissueOfOrigin()));

                tumor.setTissueHistologyTerm(getNcitNeoplasmTerm(tumor.getTissueHistology(),tumor.getTissueOfOrigin()));

                tumor.setTissueOfOriginTerm(getUberonTerm(tumor.getTissueOfOrigin()));

                tumor.setSpecimenTissueTerm(getUberonTerm(tumor.getSpecimenTissue()));
                
                // if we do this we explicity change submitted values;
                //tumor.getTumorGrade(getTumorGrades(tumor.getTumorGrade()));

                String metsT = tumor.getMetastaticSites(); // possible list of ; seperated terms
                if (metsT != null) {
                    String[] parts = metsT.split(";");
                    for (String met : parts) {
                        tumor.addMetastaticSiteTerm(getUberonTerm(met.trim()));

                    }
                }

            }

            for (PdxModelCreation mc : model.getModelCreation()) {
                String metsT = mc.getMetastaticSites();
                if (metsT != null) {
                    String[] parts = metsT.split(";");
                    for (String met : parts) {

                        mc.addMetastaticSiteTerm(getUberonTerm(met.trim()));
                    }
                }
            }
        

            //   write out terms
            //  can we only do this once? not for every model?
            try {

                FileWriter fw = new FileWriter(mappingFile);

                for(String value : ncitMappings.keySet()){
                    fw.write(NCIT + "\t" + value + "\t" + ncitMappings.get(value) + "\n");
                }

                for(String value : uberonMappings.keySet()){
                    fw.write(UBERON + "\t" + value + "\t" + uberonMappings.get(value) + "\n");
                }

                for (String value : missingNCIT.keySet()) {

                    fw.write(NCIT + "\t" + value + "\t" + NOTFOUND + "\n");
                }

                for (String value : missingUBERON.keySet()) {

                    fw.write(UBERON + "\t" + value + "\t" + NOTFOUND + "\n");

                }

                fw.close();
                System.out.println("Wrote  to " + mappingFile);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }

   
    private void loadMappings(String mappingFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(mappingFile));

            String line = null;
            // for each line in the file
            // new lines are always treated as delimiters
           
            while ((line = br.readLine()) != null) {
                //   System.out.println(line);
                String[] parts = line.split("\t");
                if (parts.length >= 3 && !NOTFOUND.equals(parts[2].trim())) {

                    if (parts[0].trim().equals(NCIT)) {

                        ncitMappings.put(parts[1].trim(), parts[2].trim());

                    } else if (parts[0].trim().equals(UBERON)) {
                        uberonMappings.put(parts[1].trim(), parts[2].trim());

                    } else {
                        System.out.println("Error in mapping file unknown Ontology:" + parts[0]);
                    }
                } else if (!NOTFOUND.equals(parts[2].trim()))  {
                    
                    System.out.println("Error in mapping file bad format:" + line);
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("No mappings file, will create new one.");

        } catch (Exception ioe) {
            // something went wrong
            ioe.printStackTrace();
        }

    }

    private TermId getUberonTerm(String value) {
        UberonTerm term = null;
        TermId termId = null;
        if (value != null && value.trim().length() > 0) {
            term = uberonParser.getUberonTerm(value);
            if (term == null && uberonMappings.get(value) != null) {
                term = uberonParser.getUberonTerm(uberonMappings.get(value));
            }
            if (term == null) {
                missingUBERON.put(value,value);
                uberonMappings.remove(value);
            } else {
                termId = term.getTermId();
            }
        }
        return termId;

    }

    private TermId getNcitNeoplasmTerm(String value, String tissue) {
        NcitTerm term = null;
        TermId termId = null;
        if (value != null && value.trim().length() > 0) {
            term = ncitParser.getNeoplasm(value);
            if (term == null && ncitMappings.get(tissue+":"+value) != null) {
                term = ncitParser.getNeoplasm(ncitMappings.get(tissue+":"+value));
            }
            if (term == null) {
                missingNCIT.put(tissue+":"+value,value);
                ncitMappings.remove(tissue+":"+value);
            } else {
                termId = term.getTermId();
            }
        }
        return termId;
    }

}


