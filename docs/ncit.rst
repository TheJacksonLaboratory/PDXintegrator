NCIT
====
We will use the NCIT thesaurus to provide IDs for the concepts in PDXnet. /



Parsing the NCIT ontology file with OWL-API
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The National Cancer Institute (NCI) has developed a `thesaurus (NCIT) <https://ncit.nci.nih.gov/ncitbrowser/>`_
of many kinds of cancer-relevant data.
The `Monarch Initiative <https://monarchinitiative.org/>`_ has developed a version of this ontology as an OBO file.
This `Wiki <https://github.com/NCI-Thesaurus/thesaurus-obo-edition/wiki>`_ provides details of this effort.

PDXIntegrator will download the OBO file with the following command. ::

    $ java -jar target/PdxIntegrator.jar download

The ncit.obo file will be stored in a newly created directory called ``data``. If the file is already present, PDXIntegrator
will emit a warning message (delete the file if you want PDXIntegrator to download a fresh copy of the file).

PDXIntegrator uses the code in the class ``NcitOwlApiParser`` to parse the Neoplasm terms of NCIT (only). Currently,
it only saves the IDs and the labels (although it may be useful in the future to also ingest the synonyms in case this
code will be used to drive an autocomplete. This is sufficient to cause the simulated patients to have
real NCIT diagnosis codes for the cancers.

For the SPARQL queries, it will be necessary to include the subclass definitions of NCIT to enable queries that
use the NCIT hierarchy.





