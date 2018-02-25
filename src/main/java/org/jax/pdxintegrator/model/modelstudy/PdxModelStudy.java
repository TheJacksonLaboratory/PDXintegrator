package org.jax.pdxintegrator.model.modelstudy;

import com.github.phenomics.ontolib.ontology.data.TermId;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToStandardOfCare;

public class PdxModelStudy {


    /*


2.5 Model study module
Field
Rec
Example
PDXNet
Treatment,    passage


D
pertuzumab    in    combination    with    trastuzumab;    CHEMBL2007641
and    CHEMBL1743082;    passage    P4
Treatment with medications will be recorded as in the clinical module. Passages will be recorded using an enumeration P1, P2, ...
Treatment    protocol    (dose;    details)
D


trastuzumab    (30    mg/kg    loading    dose,    15    mg/kg    weekly);
pertuzumab    (30    mg/kg    loading    dose,    15    mg/kg    weekly)




     */


    public static class Builder {
        /* Lag    time/doubling    time    of    tumor */
        int lagTime;
        boolean developmentOfMetastases;
        TermId locationOfMetastases; // uberon
        int passageOfMetastases;
        // tumor OMIMCs to do
        ResponseToStandardOfCare response;



    }
}
