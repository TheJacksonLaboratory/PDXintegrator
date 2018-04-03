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
| Stage; T N M              | D   | Tumor Stage          |                         | AJCC TNM Stages         |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Diagnostic Markers        | D   | Clinical BioMarkers  | ER+,    PR+,    HER2+;  |                         |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Treatment Naive Patient   | D   |                      | yes/no                  | enumeration             |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Tumor Sample Type         | D   | Collection Procedure | biopsy                  | enumeration             |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Subline of                |     | Subline of model     | PDX-123                 | Model Identifier        |
+---------------------------+-----+----------------------+-------------------------+-------------------------+
| Subline reason            |     | Why a subline        | Lost Cisplatin Resist.  | String                  |
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
**TODO** For melanoma do we want to capture specimen location?

5. **Tissue histology**
This is the pathologist's diagnosis and may often represent a refinement of the clinical diagnosis given in the Patient/Clinical module. Should use the same terminology as diagnosis, but represent the pathologist's findings.

6. **Tumor Grade**
For now we are using PDXNet codes, but we will switch to the NCIT subhierarchy, although I think they may need some TLC.
We will work with NCIT to revise these terms as a part of Monarch’s ongoing collaboration with NCIT.

7. **Disease Stage; classification**
T3N2M1;    TNM    or    Non    applicable    (example    blood    cancer)
Should follow Tumor Grade; classification standard
Use AJCC. This will be seperated into pT,pN,PM and stage

8. **Specific  markers (diagnostic linked)**
Clinically relevant bio markers.
Pairs of Marker:Status where status can be "positive", "negative", a percent value, or a variant and optional platform
Most of the assays such as IHC are covered by the NCIT under the subhierarchy “Laboratory Procedure”. That NCIT subhierachy also includes items for Receptor status (e.g., HER2/Neu positive), and these will be linked to external representations of genes/proteins by the Monarch collaboration.

9. **Treatment naive patient?**
yes/no  (enumeration)
Yes means patient has never had neoadjuvant treatment. per TCGA

10. **Original tumor sample type**
The process used to collect the sample.
biopsy, surgical sample, punch 
NCIT has a subhierarchy of terms for biopsy and biopsy locations, that will be linked to uberon etc by the Monarch collaboration. Some terms appear to be missing, e.g., “ascites fluid”, but will be added to NCIT as needed for PDXNet.

11. **Subline of**
If this model is created as a subline of an existing model indicate which model it is a subline of.

12. **Subline reason**
We may need to create our own mini-terminology to describe the reasons for using a subline

