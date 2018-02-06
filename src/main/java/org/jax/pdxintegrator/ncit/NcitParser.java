package org.jax.pdxintegrator.ncit;

import com.github.phenomics.ontolib.io.obo.OboParser;

public class NcitParser {

    private final String ncitPath;

    public NcitParser(String path) {
        ncitPath=path;
    }


    public void parseNcit() {

        //OboParser parser = new OboParser(true);
        NcitOwlApiParser noap = new NcitOwlApiParser(ncitPath);
    }


}
