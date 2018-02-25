Setting up PDXIntegrator
========================

PDXIntegrator is a Java program that requires at least Java version 8.

Note that for now, we are using a private fork of the ontolib library for parsing the NCIT obo file.
Go to https://github.com/monarch-initiative/OLPG and enter the following commands (notify Peter if
you need access). ::

    $ git clone https://github.com/monarch-initiative/OLPG
    $ cd OLPG
    $ mvn install

This will put the OLPG library files into your local maven repository (.m2 directory) and let you
build PDXIntegrator.


PDXIntegrator is provided
as a maven project. The quickest way to set it up is to clone the project from the GitHub site
at https://github.com/TheJacksonLaboratory/PDXintegrator and then to use maven to build the project.
In the
following, we show a command that will display a help message--if you see this, then you have successfully
built the program. ::

    $ git clone https://github.com/TheJacksonLaboratory/PDXintegrator
    $ cd PDXintegrator
    $ mvn package
    $ java -jar target/PdxIntegrator.jar

    [ERROR] no command

    Program: PdxIntegrator (Common Knowledge Graph PdxModel for PDXNet)
    Version: 0.0.2

    usage: java -jar PdxIntegrator.jar command [-c <arg>] [-d <arg>] [-o <arg>]
    Available commands:

    download
    	java -jar PdxIntegrator.jar download [-d directory]: Download NCI files to directory at -d (default="data").

    simulate
    	java -jar PdxIntegrator.jar simulate [-d directory]: Requires NCI files in directory at -d (default="data").

    map
    	java -jar PdxIntegrator.jar map [-d directory]: todo.



Running the drugbank command
~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Users will need to download the XML file from Drugbank: https://www.drugbank.ca
Go to the download tab. There is mandatory registration but the files are free under a CC-BY No commercial license.
Download the datafile. THe filename is ``full database.xml``. The space seems to make problems under linux, so remove the
space of the file name. ::

    $ mv full\ database.xml fulldatabase.xml

Then run the following command to extract the information we will need from the XML file (which is about 950 MB unpacked). ::

    $ java -jar target/PdxIntegrator.jar drugbank --drugbank fulldatabase.xml

Adjust the path to ``fulldatabase.xml`` as necessary. This will generate a new file in the data directory called
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

Running the simulation command
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Currently, we are building out a complete RDF model for the PDX Minimal Information standard
(https://www.ncbi.nlm.nih.gov/pubmed/29092942). Our strategy for development is to generate
random PDX cases, write the corresponding RDF code to file (with the ``simulate`` command). Then, to test
the query-ability of the model, we ingest the RDF file and query it using SPARQL queries (with the ``query``
command). This is inteded to allow collaborators to view the model and make suggestions for improvement.
The end game would be to develop this code to allow ETL of PDX data into this model. The following sections
show how to set up and run the simulation
