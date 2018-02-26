Drugbank
========

We propose to use the `Drugbank <https://www.drugbank.ca/>`_ resource to track medications. According to the Drugbank website (2018-02-26),
The latest release of DrugBank (version 5.0.11, released 2017-12-20) contains 10,999 drug entries including 2,504
approved small molecule drugs, 942 approved biotech (protein/peptide) drugs, 109 nutraceuticals and over 5,108 experimental drugs.
A recent `Nucleic Acid Research database article <https://www.ncbi.nlm.nih.gov/pubmed/29126136>`_ provides further details.
Drugbank is licensed under a a Creative Common’s Attribution-NonCommercial 4.0 International License, and therefore
can be used freely for non-commercial applications.

Our usage of drugbank
~~~~~~~~~~~~~~~~~~~~~
We used the Java JAXB tool ``xjc`` to create Java classes that mirror the XSD XML schema definition for Drugbase (the
schema is available here: https://www.drugbank.ca/releases/latest). To use PDXIntegrator, you will need to download the
Drugbase XML file (which requires free registration). Then follow these steps to unpack the XML file and to remove the
space character from the file name. ::

    $ unzip drugbank_all_full_database.xml.zip
        Archive:  drugbank_all_full_database.xml.zip
        inflating: full database.xml
    $ mv full\ database.xml fulldatabase.xml

Now we can extract the contents of the XML file for use by PDXIntegrator. The following command does that and creates a
new file in the ``data`` directory that is also used by PDXIntegrator to store downloaded files. ::

    $ java -jar target/PdxIntegrator.jar drugbank --drugbank fulldatabase.xml

Adjust the path to ``fulldatabase.xml`` as necessary.



This will generate a new file in the data directory called
drugbank.tab. The contents of this file look like this. ::

    Denileukin diftitox     173146-27-5     DB00004 ANTINEOPLASTIC AND IMMUNOMODULATING AGENTS
    Etanercept      185243-69-0     DB00005 ANTINEOPLASTIC AND IMMUNOMODULATING AGENTS
    Bivalirudin     128270-60-0     DB00006 BLOOD AND BLOOD FORMING ORGANS
    Leuprolide      53714-56-0      DB00007 ANTINEOPLASTIC AND IMMUNOMODULATING AGENTS
    Peginterferon alfa-2a   198153-51-4     DB00008 ANTINEOPLASTIC AND IMMUNOMODULATING AGENTS
    Alteplase       105857-23-6     DB00009 BLOOD AND BLOOD FORMING ORGANS
    Sermorelin      86168-78-7      DB00010 SYSTEMIC HORMONAL PREPARATIONS, EXCL. SEX HORMONES AND INSULINS
    Interferon alfa-n1      74899-72-2      DB00011 ANTINEOPLASTIC AND IMMUNOMODULATING AGENTS

For the purposes of this demonstration program, we will use the antineoplastic agents. ToDO--if we choose to stick with
DrugBank, we can exploit the hierarchy and will need to emit some RDF to represent the hierarchy that is recorded in the
XML file (for now, we will treat these medications as literals for demo purposes).