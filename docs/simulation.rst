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


We will use the corresponding RDF/XML file to perform demonstration SQPARL queries. For this, we
use the query command, which produces output like this. ::

    DEMONSTRATION OF SPARQL QUERIES

    PREFIX pdxnet: <http://pdxnetwork/pdxmodel#>
    SELECT ?patient_id ?consent ?diagnosis
    WHERE {
      ?x pdxnet:patient_id ?patient_id .
      ?x pdxnet:consent ?consent .
      ?x pdxnet:hasDiagnosis ?diagnosis .
      }
    Lock : main
    Lock : main
    -----------------------------------------------------------------------------------------
    | patient_id | consent                      | diagnosis                                 |
    =========================================================================================
    | "PAT-2"    | pdxnet:consent_ACADEMIC_ONLY | "http://pdxnetwork/pdxmodel#NCIT:9079_01" |
    | "PAT-3"    | pdxnet:consent_ACADEMIC_ONLY | "http://pdxnetwork/pdxmodel#NCIT:160_01"  |
    | "PAT-4"    | pdxnet:consent_ACADEMIC_ONLY | "http://pdxnetwork/pdxmodel#NCIT:8774_01" |
    | "PAT-0"    | pdxnet:consent_YES           | "http://pdxnetwork/pdxmodel#NCIT:1577_01" |
    | "PAT-1"    | pdxnet:consent_ACADEMIC_ONLY | "http://pdxnetwork/pdxmodel#NCIT:3020_01" |
    -----------------------------------------------------------------------------------------

Development plans
~~~~~~~~~~~~~~~~~
Currently, there are prototype versions of the first two modules.
We will go through the entire PDX-MI ontology specification in this
document :
https://docs.google.com/document/d/1M81y8wbT5gegUe35RZwS92bvHLYJrVPhaFnnkECgbto/edit
and will implement RDF patterns, and will test the ability to query the data with SPARQL. Once this
is mature and tested, we will adapt the code to provide ETL and Q/C functionalities.

Visualization
~~~~~~~~~~~~~
This is a nice tool for visualizing RDF graphs: http://visgraph3.org/