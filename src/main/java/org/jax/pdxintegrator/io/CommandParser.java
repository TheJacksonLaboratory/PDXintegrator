package org.jax.pdxintegrator.io;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.command.*;


import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class to parse command-line arguments
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 * @version 0.1.1
 */
public class CommandParser {
    private static final Logger logger = LogManager.getLogger();
    private String hpoPath=null;
    private String annotationPath=null;
    /** Path to a file with a list of HPO terms that a "patient" has. */
    private String patientAnnotations=null;
    /** Path to directory where we will download the needed files. */
    private String dataDownloadDirectory=null;

    private String mycommand=null;

    private String configFile=null;

    private String ncitPath=null;

    private String uberonPath=null;

    private String rdfFilename=null;

    private String drugBankPath=null;

    private String n_cases=null;

    private Command command=null;

    private final static String DEFAULT_NCIT_PATH="data/ncit.obo";

    private final static String DEFAULT_UBERON_PATH="data/basic.obo";

    private final static String DEFAULT_RDF_FILENAME="simulatedCases.rdf";

    private final static String DEFAULT_DRUGBANK_OUTPUT_FILE="data/drugbank.tab";

    public String getHpoPath() {
        return hpoPath;
    }

    public String getAnnotationPath() {
        return annotationPath;
    }

    public String getPatientAnnotations() { return patientAnnotations; }


    private String getConfigFile() {
        ClassLoader classLoader = CommandParser.class.getClassLoader();
        return classLoader.getResource("config.properties").getFile();
    }

    public CommandParser(String args[]) {
        final CommandLineParser cmdLineGnuParser = new DefaultParser();

        final Options gnuOptions = constructOptions();
        CommandLine commandLine;

        try
        {
            commandLine = cmdLineGnuParser.parse(gnuOptions, args);
            String category[] = commandLine.getArgs();
            if (category.length < 1) {
                printUsage("[ERROR] no command");
            } else if (category.length >1) {
                String cmd= Arrays.stream(args).collect(Collectors.joining(" "));
                printUsage(String.format("Malformed command line with %d arguments: %s",category.length,cmd));
            } else{
                mycommand = category[0];
            }
            if (commandLine.hasOption("c")) {
                this.configFile=commandLine.getOptionValue("c");
            } else {
                this.configFile=getConfigFile();
            }
            if (commandLine.hasOption("u")) {
                this.uberonPath=commandLine.getOptionValue("u");
            } else {
                this.uberonPath=DEFAULT_UBERON_PATH;
            }
            if (commandLine.hasOption("o")) {
                hpoPath=commandLine.getOptionValue("o");
            }
            if (commandLine.hasOption("drugbank")) {
                drugBankPath=commandLine.getOptionValue("drugbank");
            }
            if (commandLine.hasOption("a")) {
                annotationPath=commandLine.getOptionValue("a");
            }
            if (commandLine.hasOption("n")) {
                ncitPath=commandLine.getOptionValue("n");
            } else {
                ncitPath=DEFAULT_NCIT_PATH;
            }
            if (commandLine.hasOption("n_cases")) {
                this.n_cases=commandLine.getOptionValue("n_cases");
            }
            if (commandLine.hasOption("r")) {
                rdfFilename=commandLine.getOptionValue("r");
            } else {
                rdfFilename=DEFAULT_RDF_FILENAME;
            }
            if (commandLine.hasOption("i")) {
                patientAnnotations=commandLine.getOptionValue("i");
            }
            if (commandLine.hasOption("d")) {
                this.dataDownloadDirectory=commandLine.getOptionValue("d");
            }

            if (mycommand.equals("download")) {
                if (this.dataDownloadDirectory == null) {
                    System.out.println("Will download to the default location: \"data\"");
                    this.dataDownloadDirectory="data";
                }
                logger.warn(String.format("Download command to %s",dataDownloadDirectory));
                this.command=new DownloadCommand(dataDownloadDirectory);
            } else if (mycommand.equals("map")) {
                this.command = new MapCommand(ncitPath);
            } else if (mycommand.equals("query")) {
                this.command = new QueryCommand(rdfFilename);
            }else if (mycommand.equals("simulate")) {
                if (n_cases!=null) {
                    try {
                        Integer n=Integer.parseInt(n_cases);
                        this.command=new SimulateCommand(rdfFilename, ncitPath,uberonPath,n);
                    } catch (NumberFormatException nfe) {
                        printUsage("[ERROR] Could not parse number of cases argument: " + n_cases);
                    }
                } else {
                    this.command = new SimulateCommand(rdfFilename, ncitPath, uberonPath);
                }
            } else if (mycommand.equals("drugbank")) {
                if (drugBankPath==null) {
                    printUsage("[ERROR] --drugbank option required for drugbank command");
                }
                this.command = new DrugBankCommand(drugBankPath,DEFAULT_DRUGBANK_OUTPUT_FILE);
            } else if (mycommand.equals("JAXData")){
                this.command = new JAXDataCommand();
            }else if (mycommand.equals("BCMData")){
                this.command = new BCMDataCommand();
            }else {
                printUsage(String.format("Did not recognize command: %s", mycommand));
            }
        }
        catch (ParseException parseException)  // checked exception
        {
            System.err.println(
                    "Encountered exception while parsing using GnuParser:\n"
                            + parseException.getMessage() );
        }

    }


    public Command getCommand() {
        return command;
    }

    /**
     * Construct and provide GNU-compatible Options.
     *
     * @return Options expected from command-line of GNU form.
     */
    private static Options constructOptions() {
        final Options gnuOptions = new Options();
        gnuOptions.addOption("o", "hpo", true, "HPO OBO file path")
                .addOption(null, "drugbank", true, "path of drugbank XML file")
                .addOption("d", "download", true, "path of directory to download files")
                .addOption("n","ncit",true, "path to ncit.obo")
                .addOption("r","rdf",true, "RDF filename (simulated cases)")
                .addOption("u","uberon",true, "path to uberon (basic.obo)")
                .addOption(null,"n_cases",true, "number of cases to simulate")
                .addOption("c", "config", true, "path of configuration file");
        return gnuOptions;
    }

    public static String getVersion() {
        String version = "0.0.0";// default, should be overwritten by the following.
        try {
            Package p = CommandParser.class.getPackage();
            version = p.getImplementationVersion();
        } catch (Exception e) {
            // do nothing
        }
        return version;
    }


    /**
     * Print usage information to provided OutputStream.
     */
    private static void printUsage(String message) {
        final PrintWriter writer = new PrintWriter(System.out);
        final HelpFormatter usageFormatter = new HelpFormatter();
        writer.println();
        if (message!=null) {
            writer.println(message);
            writer.println();
        }
        writer.println("Program: PdxIntegrator (Common Knowledge Graph PdxModel for PDXNet)");
        writer.println("Version: " + getVersion());
        writer.println();
        final String applicationName = "java -jar PdxIntegrator.jar command";
        final Options options = constructOptions();
        usageFormatter.printUsage(writer, 120, applicationName, options);
        writer.println("Available commands:");
        writer.println();
        writer.println("download");
        writer.println("\tjava -jar PdxIntegrator.jar download [-d directory]: Download NCI files to directory at -d (default=\"data\").");
        writer.println();
        writer.println("drugbank");
        writer.println("\tjava -jar PdxIntegrator.jar drugbank --drugbank <path>: process drugbank XML file.");
        writer.println("\t<path>: path to fulldatabase.xml (produced by unzipping downloaded XML file and removing space from file name)");
        writer.println();
        writer.println("simulate");
        writer.println("\tjava -jar PdxIntegrator.jar simulate [-d <directory>] [--n_cases <n>]: simulate PDXNet cases ");
        writer.println("\t<directory>: directory (default=\"data\") with downloaded files");
        writer.println("\t<n>: number of cases to simulate");
        writer.println();
        writer.println("map");
        writer.println("\tjava -jar PdxIntegrator.jar map [-d directory]: todo.");
        writer.println();
        writer.println("query");
        writer.println("\tjava -jar PdxIntegrator.jar query [-i file]: todo.");
        writer.println();
        writer.close();
        System.exit(0);
    }


}

