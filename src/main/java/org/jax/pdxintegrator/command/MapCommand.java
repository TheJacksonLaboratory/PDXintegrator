package org.jax.pdxintegrator.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.util.Properties;
/**
 * This is the command to coordinate mapping of the PDX center data to the common RDF format of PDXnet
 * ToDo still a prototype!
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 * @version 0.1.0
 */
public class MapCommand extends Command{
    private static final Logger logger = LogManager.getLogger();

    private String hcipPath=null;

   /** Path to the ncit.obo file. */
    private final String ncitPath;

    public MapCommand(String ncitpath) {
        ncitPath=ncitpath;
    }



    @Override
    public void execute() {
        logger.info(String.format("Ingesting the NCIT obo file at %s",ncitPath));
        try {
            parseNcit();
        } catch (IOException e){
            e.printStackTrace();
        }
        //parseHCIP();
    }



    private void parseNcit() throws IOException {
       //
        System.err.println("Not supported/to-do");

    }






    private void loadProperties(String config) {
        Properties prop = new Properties();
        InputStream input = null;
        File f = new File(config);
        if (!f.exists()) {
            logger.error(String.format("Could not find config file at %s",config));
            return;
        }
        try {
            input = new FileInputStream(config);
            prop.load(input);
            System.out.println(prop.getProperty("hcipfile"));
            this.hcipPath=(String)prop.get("hcipfile");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
