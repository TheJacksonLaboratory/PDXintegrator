Demonstration of simulating cases and performing SPARQL queries
===============================================================

This page demonstrates how to run the demonstration query.

First we run the simulate command of PDXIntegrator to produce an RDF file with
"randomized" cases. By default, 5 random cases are produced. ::

    $ java -jar target/PdxIntegrator.jar simulate

This will produce a file called ``simulatedCases.rdf`` in the current working directory.
This file will use the RDF/XML format, but the program will also emit the same RDF data
in the Turtle format. For instance,  ::

    @prefix PDXNET: <http://pdxnetwork/pdxmodel#> .
    @prefix NCIT:  <http://purl.obolibrary.org/obo/NCIT#> .
    @prefix UBERON: <http://purl.obolibrary.org/obo/UBERON#> .

    PDXNET:PAT-1  PDXNET:age_group  "0-4 years" ;
        PDXNET:consent       PDXNET:consent_YES ;
        PDXNET:ethnicity     "Sephardic" ;
        PDXNET:gender        PDXNET:female ;
        PDXNET:hasDiagnosis  NCIT:C130038 ;
        PDXNET:hasTumor      PDXNET:TUMOR-PAT-1 ;
        PDXNET:patient_id    "PAT-1" .

    PDXNET:TUMOR-PAT-1  PDXNET:hasSubmitterTumorId
                "TUMOR-PAT-1" ;
        PDXNET:stage                NCIT:C19251 ;
        PDXNET:tissueOfOrigin       UBERON:35975 ;
        PDXNET:tumorCategory        NCIT:C3352 ;
        PDXNET:tumorGrade           NCIT:C121173 ;
        PDXNET:tumorHistology       NCIT:C130038 .

    PDXNET:PAT-0  PDXNET:age_group  "15-19 years" ;
        PDXNET:consent       PDXNET:consent_NO ;
        PDXNET:ethnicity     "hispanic or latino" ;
        PDXNET:gender        PDXNET:male ;
        PDXNET:hasDiagnosis  NCIT:C7326 ;
        PDXNET:hasTumor      PDXNET:TUMOR-PAT-0 ;
        PDXNET:patient_id    "PAT-0" .

    PDXNET:TUMOR-PAT-0  PDXNET:hasSubmitterTumorId
                "TUMOR-PAT-0" ;
        PDXNET:stage                NCIT:C19251 ;
        PDXNET:tissueOfOrigin       UBERON:4146 ;
        PDXNET:tumorCategory        NCIT:C8509 ;
        PDXNET:tumorGrade           NCIT:C48934 ;
        PDXNET:tumorHistology       NCIT:C7326 .



SPARQL Queries
~~~~~~~~~~~~~~
We will use the corresponding RDF/XML file to perform demonstration SQPARL queries. For this, we
use the query command, which produces output like this. ::

    PREFIX pdxnet: <http://pdxnetwork/pdxmodel_>
    PREFIX ncit: <http://purl.obolibrary.org/obo/NCIT_>
    SELECT ?patient_id ?consent ?diagnosis
    WHERE {
      ?x pdxnet:patient_id ?patient_id .
      ?x pdxnet:consent ?consent .
      ?x pdxnet:hasDiagnosis ?diagnosis .
      }
    LIMIT 5
    Lock : main
    Lock : main
    ----------------------------------------------------------
    | patient_id | consent                      | diagnosis  |
    ==========================================================
    | "PAT-846"  | pdxnet:consent_NO            | ncit:C5235 |
    | "PAT-1256" | pdxnet:consent_ACADEMIC_ONLY | ncit:C4887 |
    | "PAT-127"  | pdxnet:consent_NO            | ncit:C5656 |
    | "PAT-179"  | pdxnet:consent_YES           | ncit:C7811 |
    | "PAT-1477" | pdxnet:consent_ACADEMIC_ONLY | ncit:C7965 |
    ----------------------------------------------------------

    ###########  Next Query  ###########  Next Query

    PREFIX pdxnet: <http://pdxnetwork/pdxmodel_>
    PREFIX ncit: <http://purl.obolibrary.org/obo/NCIT_>
    PREFIX uberon: <http://purl.obolibrary.org/obo/UBERON_>
    SELECT ?patient_id ?currentTreatmentDrug ?diagnosis
    WHERE {
      ?x pdxnet:patient_id ?patient_id .
      ?x pdxnet:currentTreatmentDrug ?currentTreatmentDrug .
      ?x pdxnet:gender pdxnet:female .
      ?x pdxnet:hasDiagnosis ?diagnosis .
      }
    LIMIT 5
    Lock : main
    Lock : main
    --------------------------------------------------------------------------
    | patient_id | currentTreatmentDrug                         | diagnosis  |
    ==========================================================================
    | "PAT-846"  | "Goserelin[DB00014;65807-02-5]"              | ncit:C5235 |
    | "PAT-1256" | "Sargramostim[DB00020;123774-72-1]"          | ncit:C4887 |
    | "PAT-1477" | "Peginterferon alfa-2a[DB00008;198153-51-4]" | ncit:C7965 |
    | "PAT-1770" | "Cetuximab[DB00002;205923-56-4]"             | ncit:C7061 |
    | "PAT-1676" | "Cetuximab[DB00002;205923-56-4]"             | ncit:C8834 |
    --------------------------------------------------------------------------

    ###########  Next Query  ###########  Next Query

    PREFIX pdxnet: <http://pdxnetwork/pdxmodel_>
    PREFIX ncit: <http://purl.obolibrary.org/obo/NCIT_>
    PREFIX uberon: <http://purl.obolibrary.org/obo/UBERON_>
    SELECT ?patient_id ?currentTreatmentDrug ?diagnosis ?age_lowerrange ?age_upperrange
    WHERE {
      ?x pdxnet:patient_id ?patient_id .
      ?x pdxnet:currentTreatmentDrug ?currentTreatmentDrug .
      ?x pdxnet:gender pdxnet:female .
      ?x pdxnet:hasDiagnosis ?diagnosis .
      ?x pdxnet:ageBinLowerRange ?age_lowerrange .
      ?x pdxnet:ageBinUpperRange ?age_upperrange .
      FILTER (?age_lowerrange > 55) .
      }
    LIMIT 5
    Lock : main
    Lock : main
    -----------------------------------------------------------------------------------------------------------
    | patient_id | currentTreatmentDrug                       | diagnosis   | age_lowerrange | age_upperrange |
    ===========================================================================================================
    | "PAT-1256" | "Sargramostim[DB00020;123774-72-1]"        | ncit:C4887  | 75             | 79             |
    | "PAT-1770" | "Cetuximab[DB00002;205923-56-4]"           | ncit:C7061  | 105            | 109            |
    | "PAT-75"   | "Denileukin diftitox[DB00004;173146-27-5]" | ncit:C5631  | 75             | 79             |
    | "PAT-1765" | "Pegfilgrastim[DB00019;208265-92-3]"       | ncit:C7964  | 80             | 84             |
    | "PAT-851"  | "Leuprolide[DB00007;53714-56-0]"           | ncit:C27754 | 65             | 69             |
    -----------------------------------------------------------------------------------------------------------


Development plans
~~~~~~~~~~~~~~~~~
Currently, there are prototype versions of all modules but one.
We will go through the entire PDX-MI ontology specification in this
document :
https://docs.google.com/document/d/1M81y8wbT5gegUe35RZwS92bvHLYJrVPhaFnnkECgbto/edit
and will implement RDF patterns, and will test the ability to query the data with SPARQL. Once this
is mature and tested, we will adapt the code to provide ETL and Q/C functionalities.

Visualization
~~~~~~~~~~~~~
This is a nice tool for visualizing RDF graphs: http://visgraph3.org/