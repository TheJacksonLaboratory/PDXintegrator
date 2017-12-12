package org.jax.pdxintegrator.command;

import org.jax.pdxintegrator.ncit.NcitOboParser;
import org.jax.pdxintegrator.ncit.NcitOntology;
import org.jax.pdxintegrator.pdxnet.hcip.HcipParser;

import java.io.*;
import java.util.Comparator;
import java.util.Properties;

public class MapCommand extends Command{

    private String hcipPath=null;

    private final static String DEFAULT_NCIT_PATH="data/ncit.obo";
    private String ncitPath=DEFAULT_NCIT_PATH;

    public MapCommand(String configFile) {
        loadProperties(configFile);
    }


    @Override
    public void execute() {
        try {
            parseNcit();
        } catch (IOException e){
            e.printStackTrace();
        }
        //parseHCIP();
    }



    private void parseNcit() throws IOException {
        NcitOboParser parser = new NcitOboParser(new File(ncitPath));
        NcitOntology ontology = parser.parse();
    }



    private void parseHCIP() {
        HcipParser parser = new HcipParser(this.hcipPath);
        parser.parseHcipData();
        parser.debugPrint();

    }





    private void loadProperties(String config) {
        Properties prop = new Properties();
        InputStream input = null;
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
