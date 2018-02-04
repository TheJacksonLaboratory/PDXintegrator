Setting up PDXIntegrator
========================

PDXIntegrator is a Java program that requires at least Java version 8. PDXIntegrator is provided
as a maven project. The quickest way to set it up is to clone the project from the GitHub site
at https://github.com/TheJacksonLaboratory/PDXintegrator and then to use maven to build the project. In the
following, we show a command that will display a help message--if you see this, then you have successfully
built the program. ::

    $ git clone https://github.com/TheJacksonLaboratory/PDXintegrator
    $ cd PDXintegrator
    $ mvn package
    $ $ java -jar target/PdxIntegrator.jar

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

Running the simulation command
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Currently, we are building out a complete RDF model for the PDX Minimal Information standard
(https://www.ncbi.nlm.nih.gov/pubmed/29092942). Our strategy for development is to generate
random PDX cases, write the corresponding RDF code to file (with the ``simulate`` command). Then, to test
the query-ability of the model, we ingest the RDF file and query it using SPARQL queries (with the ``query``
command). This is inteded to allow collaborators to view the model and make suggestions for improvement.
The end game would be to develop this code to allow ETL of PDX data into this model. The following sections
show how to set up and run the simulation


Downloading the NCIT ontology file
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The National Cancer Institute (NCI) has developed a `thesaurus (NCIT) <https://ncit.nci.nih.gov/ncitbrowser/>`_
of many kinds of cancer-relevant data.
The `Monarch Initiative <https://monarchinitiative.org/>`_ has developed a version of this ontology as an OBO file.
This `Wiki <https://github.com/NCI-Thesaurus/thesaurus-obo-edition/wiki>`_ provides details of this effort.

PDXIntegrator will download the OBO file with the following command. ::

    $ java -jar target/PdxIntegrator.jar download

The ncit.obo file will be stored in a newly created directory called ``data``. If the file is already present, PDXIntegrator
will emit a warning message (delete the file if you want PDXIntegrator to download a fresh copy of the file).

Note that currently there is a format error in the ncit.obo file. If the original file is used, you will see an error messae (Exception)
that looks something like this. ::

    Parsing OBO file /home/peter/IdeaProjects/PDXintegrator/data/ncit.obo...
    line 439468:5 mismatched input '"' expecting QuotedString
    Exception in thread "main" java.lang.NullPointerException
    (...rest omitted...)

The cause of the problem can be investigated with the following awk command. ::

    $ awk 'FNR==439468' data/ncit.obo
    def: "A recombinant human papillomavirus (HPV), genetically engineered fusion protein vaccine in which the three HPV16 viral proteins L2, E6 and E7 are fused together in a single tandem fusion protein (TA-CIN; HPV16 L2\\E6\\E7), with potential immunoprotective and antineoplastic properties. Upon administration, HPV16 L2\\E6\\E7 fusion protein vaccine TA-CIN may stimulate the immune system to generate HPV16 E6\\E7-specific CD4+ and CD8+ T-cell responses as well as the induction of L2-specific antibodies. In addition, this vaccine may prevent infection and the development of other HPV16-associated diseases. L2, a minor viral capsid protein, is able to induce a strong antibody response against certain HPV types." [] {http://purl.obolibrary.org/obo/NCIT_P378="NCI"}

That is, the OBO format does not allow unescaped slashes (``\\``) in the text of the definition. You may need to scroll to see
the offending part of the definition: ``(TA-CIN; HPV16 L2\\E6\\E7)``.

The following sed command can be used to replace all occurences of ``\\`` with ``--``. ::

    $ sed 's#[^:]\\#-#g' ncit.obo > tmp.obo

The element ``[^:]`` keeps sed from matching URLs (e.g., http://purl.obolibrary.org/obo/NCIT_P383), so that only instances
of ``\\`` that are not preceded by a semicolon get replaced.


A similar issue comes up with use of the forward slash in definitions, e.g., ::

    An orally bioavailable protein tyrosine kinase inhibitor of mutated forms of the tumor-associated antigen mast/stem cell factor receptor c-Kit (SCFR),

We remove this with the following sed command. ::

    $ sed 's#[^"]/#-#g' tmp.obo > tmp2.obo

