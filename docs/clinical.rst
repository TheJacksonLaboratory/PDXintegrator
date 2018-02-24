Clinical/Patient Module
=======================

The following table shows the recommendations from the  `PDX-MI manuscript <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_.


+------------------------------+-----+--------------------------------+------------------------------------------------+
| Field                        | Rec | Example                        |  PDXNet                                        |
+==============================+=====+================================+================================================+
| Submitter Patient ID         | E   | PAT-123                        | CenterID:PatientID                             |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Gender                       | E   | female                         | PDXNET_:Female                                 |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Age                          | E   | 30-35                          | binned in 5 year age groups                    |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Diagnosis                    | E   | invasive breast cancer         | NCIT:C6257                                     |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Consent to share             | E   | invasive breast cancer         | PDXNET:consent_ACADEMIC_ONLY                   |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Ethnicity/Race               | E   | caucasian                      | currently string literal                       |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Current treatment drug       | D   | everolimus                     |  CHEMBL83                                      |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Current treatment protocol   | D   | afinitor;10 mg/day             |  requires discussion                           |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Prior treatment protocol     | D   | afinitor;10 mg/day             |  requires discussion                           |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Response to prior treatment  | D   | HIV-/HBV-/HCV+/HTLV-/EBV+      |  NCIT                                          |
+------------------------------+-----+--------------------------------+------------------------------------------------+
Table 1. Rec: Recommendation; E: essential; D:desirable.

1. **Submitter Patient ID**. Display as  and keep an internal ID that will not be shown externally to act as a primary key.
Patient 123 from JAX would be shown on the PDXNet website as JAX:PAT-123

2. **Gender**. Enumeration: F, M
(biological sex).

3. **Age**.
Binned in 5 year age groups.

4. **Diagnosis**. Example ````. Note that this often represents the initial diagnosis and may be less precise
than the histological diagnosis used in the second module. We will take the diagnosis codes from NCIT.

5. **Consent to share data**.
yes/no/available to academic centers only

6. **Ethnicity/Race**
Here we need to decide which reference terminology to use.

7. **Current treatment drug**
We would like to have a resource to that represents classes, ingredients, brand names, dosage forms, etc., in a computable manner.
There are probably two main contenders, ChEMBL [3]and RxNorm [4] (a standardized drug nomenclature maintained by the National Library of Medicine).
ChEMBL has two attributes to recommend it: its more open license as compared to RxNorm, and the ease of mapping to other ontologies such as  BioAssay Ontology, Unit Ontology, Quantities, Units, Dimensions and Data Types Ontology and Chemical Information Ontology (CHEMINF) [3]. A new resource such as DrugCentral can also be used to map between many resources [5] This paper [6] also describes several resources useful for mapping.

8. **Current treatment protocol (dose; details)**
There is currently no ontology that I know of for representing dosages. There are many ways of representing dosages, e.g., 10 mg/day or 5 mg b.i.d. and I would suggest we revisit this topic after all of the PDXNet centers have submitted example data. We should probably not over-engineer this.

9. **Prior treatment protocol**
The medication data should be represented as above. The surgery data could be represented using MedDRA codes
(a rich and highly specific standardised medical terminology to facilitate sharing of regulatory information internationally
for medical products used by humans), but MedDRA does not have an open license and I am not sure about reuse/redistribution.
MeSH would be an option, although MeSH is not always ontologically well structured, but there are a large number of terms.
The NCI thesaurus [7] has a hierarchy of terms for Intervention or Procedure, including Cancer Diagnostic or Therapeutic
procedure, including terms for operations such as mastectomy. This is probably sufficient for our needs, and I would suggest we use this.

10. **Response to prior treatment**
progressive disease (RECIST1.1)
These items can be represented in the NCIT, which has a subhierarchy for Clinical Course of Disease, which includes items such as “Complete remission”, “Progressive disease” and many more.

11. **Virology status**
Probably the NCIT subhierarchy of Viral infection (which includes these viruses and many more) would be best. We can represent this in RDX using patientID was_diagnosed_with EBV Infection (or was_ruled_out_in).


