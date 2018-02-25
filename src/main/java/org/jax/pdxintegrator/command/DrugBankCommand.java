package org.jax.pdxintegrator.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.io.DrugBankParser;

/**
 * Parse the drug bank XML file to extract information we will want to use for the PDXNet.
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class DrugBankCommand extends Command{
    private static final Logger logger = LogManager.getLogger();
    private final String pathToDrugbankXmlFile;
    private final String outputFile;

    public DrugBankCommand(String path, String output) {
        outputFile=output;
        pathToDrugbankXmlFile=path;
    }

    @Override
    public void execute() {

        DrugBankParser parser = new DrugBankParser(this.pathToDrugbankXmlFile, this.outputFile);
        logger.trace("About to parse drugbank XML");
        parser.parse();
    }
}
