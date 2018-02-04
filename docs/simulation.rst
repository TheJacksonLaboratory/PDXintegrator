Demonstration of simulating cases and performing SPARQL queries
===============================================================

This page demonstrates how to run the demonstration query.

First we run the simulate command of PDXIntegrator to produce an RDF file with
"randomized" cases. By default, 5 random cases are produced. ::

    $ java -jar target/PdxIntegrator.jar simulate

This will produce a file called ``simulatedCases.rdf`` in the current working directory.
This file will use the RDF/XML format, but the program will also emit the same RDF data
in the Turtle format. For instance,  ::

    @prefix pdxnet: <http://pdxnetwork/pdxmodel#> .
    @prefix ncit:  <http://purl.obolibrary.org/obo/NCIT#> .

    pdxnet:NCIT:3020_01  pdxnet:cancerDiagnosis
                    "NCIT:3020" .

    pdxnet:PAT-3  pdxnet:age_group  "0-4 years" ;
        pdxnet:consent       pdxnet:consent_ACADEMIC_ONLY ;
        pdxnet:ethnicity     "Sephardic" ;
        pdxnet:gender        pdxnet:male ;
        pdxnet:hasDiagnosis  "http://pdxnetwork/pdxmodel#NCIT:160_01" ;
        pdxnet:patient_id    "PAT-3" .

    pdxnet:NCIT:160_01  pdxnet:cancerDiagnosis
                "NCIT:160" .

    pdxnet:PAT-1  pdxnet:age_group  "0-4 years" ;
        pdxnet:consent       pdxnet:consent_ACADEMIC_ONLY ;
        pdxnet:ethnicity     "Intercontinental" ;
        pdxnet:gender        pdxnet:male ;
        pdxnet:hasDiagnosis  "http://pdxnetwork/pdxmodel#NCIT:3020_01" ;
        pdxnet:patient_id    "PAT-1" .

    pdxnet:NCIT:1577_01  pdxnet:cancerDiagnosis
                "NCIT:1577" .

    pdxnet:PAT-4  pdxnet:age_group  "20-24 years" ;
        pdxnet:consent       pdxnet:consent_ACADEMIC_ONLY ;
        pdxnet:ethnicity     "hispanic or latino" ;
        pdxnet:gender        pdxnet:male ;
        pdxnet:hasDiagnosis  "http://pdxnetwork/pdxmodel#NCIT:8774_01" ;
        pdxnet:patient_id    "PAT-4" .

    pdxnet:NCIT:8774_01  pdxnet:cancerDiagnosis
                "NCIT:8774" .

    pdxnet:PAT-2  pdxnet:age_group  "0-4 years" ;
        pdxnet:consent       pdxnet:consent_ACADEMIC_ONLY ;
        pdxnet:ethnicity     "Sephardic" ;
        pdxnet:gender        pdxnet:male ;
        pdxnet:hasDiagnosis  "http://pdxnetwork/pdxmodel#NCIT:9079_01" ;
        pdxnet:patient_id    "PAT-2" .

    pdxnet:NCIT:9079_01  pdxnet:cancerDiagnosis
                "NCIT:9079" .

    pdxnet:PAT-0  pdxnet:age_group  "20-24 years" ;
        pdxnet:consent       pdxnet:consent_YES ;
        pdxnet:ethnicity     "Intercontinental" ;
        pdxnet:gender        pdxnet:male ;
        pdxnet:hasDiagnosis  "http://pdxnetwork/pdxmodel#NCIT:1577_01" ;
        pdxnet:patient_id    "PAT-0" .

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
Currently, there is a naive and simplified version of the first module that is intended to
get a start on things. We will go through the entire PDX-MI ontology specification in this
document :
https://docs.google.com/document/d/1M81y8wbT5gegUe35RZwS92bvHLYJrVPhaFnnkECgbto/edit
and will implement RDF patterns, and will test the ability to query the data with SPARQL. Once this
is mature and tested, we will adapt the code to provide ETL and Q/C functionalities.