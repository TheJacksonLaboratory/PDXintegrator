package org.jax.pdxintegrator.pdxnet.hcip;

import org.jax.pdxintegrator.model.PdxSex;
/**
 * Represent one entry (line) in the HCIP data.
 * TODO understand the format of the HCIP file and make this more nearly complete.
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 * @version 0.1.0
 */
public class HcipEntry {

    private static final String HCIP_PREFIX="hcip";

    private final String sampleID;
    private final PdxSex sex;
    private final String diagnosis;


    public HcipEntry(String id, PdxSex pdxsex,String dx){
        sampleID=id;
        sex=pdxsex;
        diagnosis=dx;
    }

    /**
    *Note: NCIT:C16960 is the term for Patient
     */
    public String toRDF() {
        //sample123 a obi:Sample .
        String s1=String.format("%s:%s a NCIT:C16960 .\n",HCIP_PREFIX,sampleID);
        String s2=String.format("%s:%s is_sex %s .\n",HCIP_PREFIX,sampleID,sex.toString());
        String s3=String.format("%s:%s has-disease %s:%s-disease-instance1 .\n",HCIP_PREFIX,sampleID,HCIP_PREFIX,sampleID);
        String s4=String.format("%s:%s-disease-instance1 a %s",HCIP_PREFIX,sampleID,diagnosis);
        return s1+s2+s3+s4;
    }
}
