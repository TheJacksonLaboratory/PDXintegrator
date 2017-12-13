package org.jax.pdxintegrator.io;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.command.Command;
import org.jax.pdxintegrator.command.DownloadCommand;
import org.jax.pdxintegrator.command.MapCommand;



import java.io.PrintWriter;
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

    private Command command=null;

    public String getHpoPath() {
        return hpoPath;
    }

    public String getAnnotationPath() {
        return annotationPath;
    }

    public String getPatientAnnotations() { return patientAnnotations; }


    private String getConfigFile() {
        ClassLoader classLoader = CommandParser.class.getClassLoader();
        String path = classLoader.getResource("config.properties").getFile();
        return path;
    }

    public CommandParser(String args[]) {
        final CommandLineParser cmdLineGnuParser = new DefaultParser();

        final Options gnuOptions = constructOptions();
        CommandLine commandLine;

        try
        {
            commandLine = cmdLineGnuParser.parse(gnuOptions, args);
            String category[] = commandLine.getArgs();
            if (category.length != 1) {
                printUsage("command missing");
            } else {
                mycommand = category[0];
                logger.trace(String.format("I GOT COMMANT %s",mycommand));
            }
            if (commandLine.getArgs().length < 1) {
                printUsage("no arguments passed");
                return;
            }
            if (commandLine.hasOption("c")) {
                this.configFile=commandLine.getOptionValue("c");
            } else {
                this.configFile=getConfigFile();
            }

            if (commandLine.hasOption("o")) {
                hpoPath=commandLine.getOptionValue("o");
            }
            if (commandLine.hasOption("a")) {
                annotationPath=commandLine.getOptionValue("a");
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
                logger.trace("map command");
                this.command = new MapCommand(configFile);
            } else {
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
    public static Options constructOptions()
    {
        final Options gnuOptions = new Options();
        gnuOptions.addOption("o", "hpo", true, "HPO OBO file path")
                .addOption("d", "download", true, "path of directory to download files")
                .addOption("c", "config", true, "path of configuration file");
        return gnuOptions;
    }



    /**
     * Print usage information to provided OutputStream.
     */
    public static void printUsage(String message) {
        final PrintWriter writer = new PrintWriter(System.out);
        final HelpFormatter usageFormatter = new HelpFormatter();
        final String applicationName = "java -jar pdxintegrator.jar command";
        final Options options = constructOptions();
        usageFormatter.printUsage(writer, 120, applicationName, options);
        writer.println("\twhere command is one of download,....");
        writer.println("\t- download [-d directory]: Download neeeded files to directory at (-d).");
        writer.close();
        System.exit(0);
    }


}

