package org.jax.pdxintegrator.command;

import org.jax.pdxintegrator.pdxnet.hcip.HcipParser;

import java.io.*;
import java.util.Comparator;
import java.util.Properties;

public class MapCommand extends Command{

    private String hcipPath=null;

    public MapCommand(String configFile) {
        loadProperties(configFile);
    }


    @Override
    public void execute() {
        parseHCIP();
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
