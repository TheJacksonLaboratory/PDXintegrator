Clinical/Tumor module
=====================
The following table shows the recommendations from the  `PDX-MI manuscript <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_.


+------------------------------+-----+--------------------------------+------------------------------------------------+
| Field                        | Rec | Example                        |  PDXNet                                        |
+==============================+=====+================================+================================================+
| Submitter Tumor   ID         | E   | TUM-123                        | Analogous to PatientID                         |
+------------------------------+-----+--------------------------------+------------------------------------------------+
|  Primary tumor tissue        | E   | breast                         | UBERON code                                    |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Primary,met, recurrence      | E   | breast                         | enumeration                                    |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Specimen tumor tissue        | E   | breast                         | UBERON code                                    |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Tissue histology             | E   |invasive ductal carcinoma       | NCIT code                                      |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Tumor Grade; classification  | E   |grade    3;    Elston           | PDXNet codes                                   |
+------------------------------+-----+--------------------------------+------------------------------------------------+
|Disease Stage; classification | E   |ER+,    PR+,    HER2+;          | ???                                            |
+------------------------------+-----+--------------------------------+------------------------------------------------+
|   from untreated patient?    | E   |yes/no                          | enumeration                                    |
+------------------------------+-----+--------------------------------+------------------------------------------------+
|  tumor sample type           | D   | biopsy                         | enumeration                                    |
+------------------------------+-----+--------------------------------+------------------------------------------------+
|  from an existing PDX model? | D   | yes/no                         | see below                                      |
+------------------------------+-----+--------------------------------+------------------------------------------------+


1. **Submitter Tumor ID**
Display as CenterID:TumorID and keep an internal ID that will not be shown externally to act as a primary key.
Tumor 123 from JAX would be shown on the PDXNet website as JAX:TUM-123


2. **Primary tumor tissue of origin**
We can infer this from the NCIT code if desired. For now, the simulation shows a random UBERON code for an anatomical entity.
We will use the uberon cross-species anatomy ontology [8] that is developed by Monarch Initiative (M. Haendel, C. Mungall).

3. **Primary, metastasis,recurrence**
For now, we are eusingg PDXNet entities, but we should use the NCIT terms for these items.
This would allow users to enter a more specific NCIT term such as Distant metastasis (C18206), which is a child of Metastasis (C19151)

4. **Specimen tumor tissue**
Uberon as above. Possibly use ICD-0?

5. **Tissue histology**
This is the pathologist's diagnosis and may often represent a refinement of the clinical diagnosis given in the Patient/Clinical module.
Should probably use the same terminology as diagnosis, but represent the pathologist's findings

6. **Tumor Grade; classification**
For now we are using PDXNet codes, but we will switch to the NCIT subhierarchy, although I think they may need some TLC.
We will work with NCIT to revise these terms as a part of Monarch’s ongoing collaboration with NCIT.
Possibly us AJCC. Should this be seperated into pT,pN,PM?

7. **Disease Stage; classification**
T3N2M1;    TNM    or    Non    applicable    (example    blood    cancer)
Should follow Tumor Grade; classification standard

8. **Specific  markers (diagnostic linked)**
Most of the assays such as IHC are covered by the NCIT under the subhierarchy “Laboratory Procedure”. That NCIT subhierachy also includes items for Receptor status (e.g., HER2/Neu positive), and these will be linked to external representations of genes/proteins by the Monarch collaboration.


9. **Is tumor from untreated patient?**
yes/no  (enumeration)
**TODO** define untreated
JAX: PDX models are considered treatment naive if the patient did not receive chemotherapy, immunotherapy, hormone therapy or radiation therapy for this primary cancer within 5 years prior to sample collection and/or within 1 year for a different cancer.

10. **Original tumor sample type**
biopsy,    surgical    sample,        ascites    fluid,    blood,    etc
NCIT has a subhierarchy of terms for biopsy and biopsy locations, that will be linked to uberon etc by the Monarch collaboration. Some terms appear to be missing, e.g., “ascites fluid”, but will be added to NCIT as needed for PDXNet.


11. **Tumor from an existing PDX model? ID?  Why sub-line?**
The PDX-MI manuscript shows the example ``Yes, PDX#123, lost cisplatin resistance``. In PDXNet, we will use
Yes/no and reference to a PDX model that is identified by CenterID:ModelID (same as Submitter    PDX    ID below).
We may need to create our own mini-terminology to describe the reasons for using a subline
(ToDo).
