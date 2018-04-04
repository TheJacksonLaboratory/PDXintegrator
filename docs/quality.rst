Model quality assurance
=======================

The model quality assurance module captures information about tissue provenance and fidelity of the passaged tumor with respect to key characteristics of the patient tumor.

The following table shows the recommendations from the  `PDX-MI manuscript <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_.

In terms of RDF modeling, we will add these items as properties of the Pdx Sample.



+---------------------------------+-----+--------------------------------------+------------------------------------------------+
| Field                           | Rec | Example                              |  PDXNet                                        |
+=================================+=====+======================================+================================================+
|Tumor Characterization Technology| E   |histology    and    IHC               | create PDXNet classes?                         |
+---------------------------------+-----+--------------------------------------+------------------------------------------------+
|Tumor not of mouse/EBV origin    | E   | yes/no;negative for  murine CD45     | Yes/No enumeration                             |
+---------------------------------+-----+--------------------------------------+------------------------------------------------+
|Response to Standard of Care     | D   | Not    assessed                      | enumeration of 5 items                         |
+---------------------------------+-----+--------------------------------------+------------------------------------------------+
|Animal    health    status       | D   | SPF/SOPF                             | ?????                                          |
+---------------------------------+-----+--------------------------------------+------------------------------------------------+
|Passage QA performed             | D   |  P4                                  | ?????                                          |
+---------------------------------+-----+--------------------------------------+------------------------------------------------+
Table 2.4. Model creation Q/A module. Rec: Recommendation; E: essential; D:desirable.


1. **Tumor Characterization Technology**
**TODO** Need to get a complete list.

2. **Tumor confirmed not to be of mouse/EBV origin**
**TODO** should this be a YES/NO or should we record how this was determined, e.g.,
negative    for    murine    CD45

3. **Response to Standard of Care**
Some producers evaluate how well a PDX model recapitulates the originating tumor's response by measuring PDX tumor
growth response to standard-of-care treatment and this is included as a desirable attribute. We will model this
using an enumeration (RDF entities):

* Not assessed
* Complete response
* Partial response
* Stable disease
* Progressive disease

See <http://tumor.informatics.jax.org/mtbwi/live/www/html/SOCHelp.html#protocol>

4. **Animal health status**
**TODO** Would it be correct to say that PDXNet has a minimal mouse health checklist and that all animals that do not pass
this are not included in the data? Therefore, the central database will only be including results from mice that passed
and it does not need to go overboard in recording this information?

5. **Passage QA performed**
The passage or passages on which QA was performed. As models are repeately passaged QA status may change.
Will PDXNet be including data from models that only partially pass the criteria or is it an absolute rule-out if QA is not perfect? Will this be a list of passage #s?


