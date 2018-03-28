Clinical/Tumor module
=====================
The following table shows the recommendations from the  `PDX-MI manuscript <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_.


+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Field                     | Rec | Description          | Example                 |  PDXNet                 |
+===========================+=====+======================+=========================+=========================+
| Submitter Tumor ID        | E   | Tissue ID            | TUM-123                 | Unique ID               |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Primary Tumor Tissue      | E   | Primary Tumor Tissue | breast                  | UBERON code             |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Primary, Met, Recurrence  | E   | Disease Progression  | Recurrence              | enumeration             |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Specimen Tumor Tissue     | E   | Sampled Tissue       | breast                  | UBERON code             |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Tissue Histology          | D   | Histologic Diagnosis |invasive ductal carcinoma| NCIT code               |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Tumor Grade               | D   | Tumor Grade          |grade    3               | AJCC Grade              |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Disease Stage; T N M      | D   | Tumor Stage          |                         | AJCC TNM Stages         |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Diagnostic Markers        | D   | Clinical BioMarkers  | ER+,    PR+,    HER2+;  |                         |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Treatment Naive Patient   | D   |                      | yes/no                  | enumeration             |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Tumor Sample Type         | D   | Collection Procedure | biopsy                  | enumeration             |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| From an existing PDX model| D   |                      | yes/no                  | see below               |
+---------------------------+-----+----------------------+-------------------------+-------------------------+


1. **Submitter Tumor ID**
Display as CenterID:TumorID to act as a primary key. If not provided, patient ID can be used.
Tumor 123 from JAX would be shown on the PDXNet website as JAX:TUM-123


2. **Primary tumor tissue**
The tissue of the primary tumor.
For now, the simulation shows a random UBERON code for an anatomical entity.
We will use the uberon cross-species anatomy ontology [8] that is developed by Monarch Initiative (M. Haendel, C. Mungall).

3. **Primary, metastasis,recurrence**
Is the specimen tissue from the primary tumor, a metastasis or a recurrance
For now, we are using PDXNet entities, but we should use the NCIT terms for these items.
This would allow users to enter a more specific NCIT term such as Distant metastasis (C18206), which is a child of Metastasis (C19151)

4. **Specimen tumor tissue**
Tissue from which the specimen was collected. Same as Primary tissue if the tumor is not metastatic.
Uberon as above.
**TODO** For melanoma do we want to capture specimen site?

5. **Tissue histology**
This is the pathologist's diagnosis and may often represent a refinement of the clinical diagnosis given in the Patient/Clinical module. Should use the same terminology as diagnosis, but represent the pathologist's findings.

6. **Tumor Grade**
For now we are using PDXNet codes, but we will switch to the NCIT subhierarchy, although I think they may need some TLC.
We will work with NCIT to revise these terms as a part of Monarch’s ongoing collaboration with NCIT.
**TODO** Define this field and determine ontology to use.

7. **Disease Stage; classification**
T3N2M1;    TNM    or    Non    applicable    (example    blood    cancer)
Should follow Tumor Grade; classification standard
Use AJCC. This should be seperated into pT,pN,PM and stage
**TODO** Define this field and determine ontology to use.

8. **Specific  markers (diagnostic linked)**
Clinically relevant bio markers.
Pairs of Marker:Status where status can be "positive", "negative", a percent value (or a variant?)
Most of the assays such as IHC are covered by the NCIT under the subhierarchy “Laboratory Procedure”. That NCIT subhierachy also includes items for Receptor status (e.g., HER2/Neu positive), and these will be linked to external representations of genes/proteins by the Monarch collaboration.

9. **Is tumor from untreated patient?**
yes/no  (enumeration)
**TODO** Define untreated.
**JAX:** "PDX models are considered treatment naive if the patient did not receive chemotherapy, immunotherapy, hormone therapy or radiation therapy for this primary cancer within 5 years prior to sample collection and/or within 1 year for a different cancer."

10. **Original tumor sample type**
The process used to collect the sample.
biopsy, surgical sample, punch 
NCIT has a subhierarchy of terms for biopsy and biopsy locations, that will be linked to uberon etc by the Monarch collaboration. Some terms appear to be missing, e.g., “ascites fluid”, but will be added to NCIT as needed for PDXNet.


11. **Tumor from an existing PDX model? ID?  Why sub-line?**
The PDX-MI manuscript shows the example ``Yes, PDX#123, lost cisplatin resistance``. In PDXNet, we will use
Yes/no and reference to a PDX model that is identified by CenterID:ModelID (same as Submitter    PDX    ID below).
We may need to create our own mini-terminology to describe the reasons for using a subline
**TODO**
  This could be  "Subline Of"=(modelID) and "Subline reason"= 
