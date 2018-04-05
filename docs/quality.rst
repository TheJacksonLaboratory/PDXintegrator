Model quality assurance
=======================

The model quality assurance module captures information about tissue provenance and fidelity of the passaged tumor with respect to key characteristics of the patient tumor.

The following table shows the recommendations from the  `PDX-MI manuscript <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_.

In terms of RDF modeling, we will add these items as properties of the Pdx Sample.



+---------------------------------+-----+--------------------------------------+------------------------------------------------+
| Field                           | Rec | Example                              |  PDXNet                                        |
+=================================+=====+======================================+================================================+
|Quality control method(s)        | E   |histology    and    IHC               | create PDXNet classes?                         |
+---------------------------------+-----+--------------------------------------+------------------------------------------------+
|Quality control results          | E   |                                      | Text                                           |
+---------------------------------+-----+--------------------------------------+------------------------------------------------+
|Animal    health    status       | D   | SPF/SOPF                             | ?????                                          |
+---------------------------------+-----+--------------------------------------+------------------------------------------------+
|Passage QA performed             | D   |  P4                                  | Integer                                        |
+---------------------------------+-----+--------------------------------------+------------------------------------------------+
Table 2.4. Model creation Q/A module. Rec: Recommendation; E: essential; D:desirable.


1. **Quality control method**
**TODO** Need to get a set of QC methods.

2. **Quality control results**
Description of the results of the QA/QC method

3. **Animal health status**
Pertains to the status of mouse room models are kept in.

4. **Passage QA performed**
The passage or passages on which QA was performed. As models are repeately passaged QA status may change.
If QA/QC is done on multiple passages multiple QA sections can be added


