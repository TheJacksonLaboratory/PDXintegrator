package org.jax.pdxintegrator.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.io.FileDownloadException;
import org.jax.pdxintegrator.io.FileDownloader;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadCommand extends Command {
    private static final Logger logger = LogManager.getLogger();

    private String downloadDirectory=null;



    private final static String NCIT_OBO="https://stars.renci.org/var/NCIt/ncit.obo";

     /**
     * Download all three files that we need for the analysis.
     * @param path
     */
    public DownloadCommand(String path){
        this.downloadDirectory=path;
    }


    /**
     * Download the hp.obo and the phenotype_annotation.tab files.
     */
    public void execute() {
        downloadNcitOntologyIfNeeded();
    }

    private void downloadNcitOntologyIfNeeded() {
        File f = new File(String.format("%s%sncit.obo",downloadDirectory,File.separator));
        if (f.exists()) {
            logger.warn(String.format("Cowardly refusing to download ncit.obo since we found it at %s",f.getAbsolutePath()));
            return;
        }
        FileDownloader downloader=new FileDownloader();
        try {
            URL url = new URL(NCIT_OBO);
            logger.debug("Created url from "+NCIT_OBO+": "+url.toString());
            downloader.copyURLToFile(url, new File(f.getAbsolutePath()));
        } catch (MalformedURLException e) {
            logger.error("Malformed URL for ncit.obo");
            logger.error(e,e);
        } catch (FileDownloadException e) {
            logger.error("Error downloading ncit.obo");
            logger.error(e,e);
        }
        logger.trace(String.format("Successfully downloaded ncit.obo file at %s",f.getAbsolutePath()));
    }

}
