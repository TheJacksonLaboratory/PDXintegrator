package org.jax.pdxintegrator.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.ncit.NcitOwlApiParser;
import org.jax.pdxintegrator.ncit.neoplasm.NcitNeoplasmTerm;
import org.jax.pdxintegrator.rdf.PdxModel2Rdf;
import org.jax.pdxintegrator.simulate.patient.PdxModelSimulator;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SimulateCommand extends Command{
    private static final Logger logger = LogManager.getLogger();

    private static final int N_cases=5;

    private List<PdxModel> simulatedModels=new ArrayList<>();

    private List<NcitNeoplasmTerm> neoplasmTerms=null;

    private final String outfilename;

    private final String ncitOboPath;

    public SimulateCommand(String fname, String ncit) {
        logger.trace("Simulating PDF net data...");
        outfilename=fname;
        ncitOboPath=ncit;
    }


    @Override
    public void execute() {

        inputNcit();
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
    }




    private PdxModel simulateCase(int i) {
        PdxModelSimulator simulator = new PdxModelSimulator(i, this.neoplasmTerms);
        return simulator.getPdxmodel();
    }


}
