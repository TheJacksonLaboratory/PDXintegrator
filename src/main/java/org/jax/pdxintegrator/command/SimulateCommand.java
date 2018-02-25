package org.jax.pdxintegrator.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.ncit.NcitOwlApiParser;
import org.jax.pdxintegrator.ncit.neoplasm.NcitTerm;
import org.jax.pdxintegrator.rdf.PdxModel2Rdf;
import org.jax.pdxintegrator.simulate.PdxModelSimulator;
import org.jax.pdxintegrator.uberon.UberonOntologyParser;
import org.jax.pdxintegrator.uberon.UberonTerm;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SimulateCommand extends Command{
    private static final Logger logger = LogManager.getLogger();

    private static final int N_cases=2000;

    private List<PdxModel> simulatedModels=new ArrayList<>();

    private List<NcitTerm> neoplasmTerms=null;
    /** List of terms like "Grade 2" */
    private List<NcitTerm> gradeTerms;

    private List<NcitTerm> stageTerms;

    private List<UberonTerm> uberonTerms=null;

    private final String outfilename;

    private final String ncitOboPath;

    private final String uberonPath;

    public SimulateCommand(String fname, String ncit,String uberon) {
        logger.trace("Simulating PDF net data...");
        outfilename=fname;
        ncitOboPath=ncit;
        uberonPath=uberon;
    }


    @Override
    public void execute() {

        inputNcit();
        inputUberon();
        outputSimulation();


    }


    private void outputSimulation() {
        logger.trace("We will now output simulated PDX cases...");
        for (int i = 0; i < N_cases; i++) {
            PdxModel pm = simulateCase(i);
            simulatedModels.add(pm);
        }
        PdxModel2Rdf p2rdf = new PdxModel2Rdf(simulatedModels);
        try {
            FileOutputStream fos = new FileOutputStream(outfilename, false);
            p2rdf.outputRDF(fos);
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        }
    }





    private void inputNcit() {
        NcitOwlApiParser parser = new NcitOwlApiParser(this.ncitOboPath);
        parser.parse();
        this.neoplasmTerms=parser.getTermlist();
        this.gradeTerms=parser.getGradeTermList();
        this.stageTerms=parser.getStageTermList();
    }

    private void inputUberon() {
        UberonOntologyParser parser = new UberonOntologyParser(this.uberonPath);
        parser.parse();
        this.uberonTerms=parser.getTermlist();
    }




    private PdxModel simulateCase(int i) {
        PdxModelSimulator simulator = new PdxModelSimulator(i, this.neoplasmTerms, this.gradeTerms, this.stageTerms,this.uberonTerms);
        return simulator.getPdxmodel();
    }


}
