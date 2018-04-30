Clinical/Patient Module
=======================

The `PDX-MI <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_ provides a
clinical module that is divided into two submodules:
“clinical/patient” and “clinical/tumor.” “Clinical/patient” requires
information about the patient from which the engrafted tumor originates,
including age, sex, ethnicity, and disease diagnosis.


+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
| Field                   |Rec| Description                         | Example                   |  PDXNet                     |
+=========================+===+=====================================+===========================+=============================+
|Submitter Patient ID     | D | Unique ID. Tumor id if not supplied | PAT-123                   | CenterID:PatientID          |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Sex                      | D | Patient Sex                         | female                    | NCIT:C16576                 |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Age at diagnosis         | D | Age at diagnosis                    | 30                        | binned in 5 year age groups |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Age at collection        | D | Age at specimen collection          | 30                        | binned in 5 year age groups |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Submitted Diagnosis      | E | Initial Clinical Diagnosis          | invasive breast cancer    | invasive breast cancer      |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Diagnosis                | E | Initial Clinical Diagnosis Term     | invasive breast cancer    | NCIT:C6257                  |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Consent to share         | E | Patient Consent to share data       |                           | PDXNET:consent_ACADEMIC_ONLY|
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Ethnicity                | D |                                     | caucasian                 | currently string literal    |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Race                     | D |                                     |                           | currently string literal    |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Current treatment drug   | D |                                     | everolimus                |  CHEMBL83                   |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Current trtmnt protocol  | D |                                     | afinitor;10 mg/day        |                             |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Prior treatment protocol | D |                                     | afinitor;10 mg/day        |                             |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
|Prior treatment response | D |                                     |                           |  RECIST                     |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+
| Virology Status         | D |                                     | HIV-/HBV-/HCV+/HTLV-/EBV+ |  NCIT                       |
+-------------------------+---+-------------------------------------+---------------------------+-----------------------------+

Table 1. Rec: Recommendation; E: essential; D:desirable. Desireable fields will be shown as 'Not Reported' if no data is provided.

1. **Submitter Patient ID**. Used when possible for identifying multiple models from the same patient.
Patient 123 from JAX would be shown on the PDXNet website as JAX:PAT-123. Will use supplied PDXNet abbreviations to prefix any ID. If no ID is supplied, the ID will be based on Tumor ID. 

2. **Sex**. Patient sex.
We will use this field to record biological sex. We will use the NCIT terms:

* Female (`Code NCIT:C16576 <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&ns=ncit&code=C16576>`_):  A person who belongs to the sex that normally produces ova. The term is used to indicate biological sex distinctions, or cultural gender role distinctions, or both.
* Male (`Code NCIT:C20197 <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&ns=ncit&code=C20197>`_): A person who belongs to the sex that normally produces sperm. The term is used to indicate biological sex distinctions, cultural gender role distinctions, or both.

3. **Age at Diagnosis**. Patient age at time of initial clinical diagnosis in years.
For display : to reduce the possibility of patient identification, PDX-MI recommends grouping ages into 5-year groups, although more granular groupings may be used in cases such as pediatric tumors if approved by a contributor's Institutional Review Board.
Here, we have implemented binned age groups as follows. ::

    PDXNET:PAT-1511  PDXNET:ageBinLowerRange 55 ;
        PDXNET:ageBinUpperRange      59 ;
        (...) .


In the simulation code, we simulate using 5 year bins, but any ranges could be used in real code. For now, the age is
understood to be in years, and if there is a need to be more precise we would need to change the model. 
**TODO** it may be better to include the word "year" in the predicate name?

Note that there is a mistake in the PDX-MI, which uses a six year range instead of a five year range: 30–35 (binned in 5-year age groups)

4. **Age at Collection**. Patient age when specimen was collected in years. 
Will be binned for dispaly as above.

5. **Submitted Diagnosis**. The initial clinical diagnosis provided as free text.

6. **Diagnosis**. Initial clinical diagnosis. Submitted diagnosis mapped to NCIT term  
Note this represents the initial diagnosis and may be less precise
than the histological diagnosis used in the second module. We will take the diagnosis codes from NCIT. The following
shows an example triple for an individual with a
diagnosis of `Central Nervous System Histiocytic Sarcoma (NCIT:C129807) <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp&ns=ncit?dictionary=NCI_Thesaurus&code=C129807>`_. ::


      PDXNET:PAT-1511 PDXNET:hasDiagnosis   NCIT:C129807 .


7. **Consent to share data**. Patient consent.
Reporting on consent is essential. We are using the following codes.

* PDXNET:consent_NO
* PDXNET:consent_YES
* PDXNET:consent_ACADEMIC_ONLY

**TODO** 1 define what we mean by these categories and add more categories as necessary.
**TODO** 2. Define relation to NCIT term for `Consent (NCIT:C25460) <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp&ns=ncit?dictionary=NCI_Thesaurus&code=C25460>`_.

8. **Ethnicity**  Patient ethnicity.
**TODO** we need to decide which reference terminology to use. One option is to adopt the NCIT terminology, which includes

* `Ethnic Group (NCIT:C16564) <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&ns=ncit&code=C16564>`_ (which includes concepts such as Hispanic etc.)

9. **Race** Patient race.
**TODO** we need to decide which reference terminology to use. One option is to adopt the NCIT terminology, which includes

* `Race (NCIT:C17049) <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&ns=ncit&code=C17049>`_ (with multiple subclasses specifying various populations)


10. **Current treatment drug** Patient treatment at time of specimen sample.
We would like to have a resource to that represents classes, ingredients, brand names, dosage forms, etc., in a computable manner.
There are several contenders, including `ChEMBL <https://www.ebi.ac.uk/chembl/>`_ and
`RxNorm <https://www.nlm.nih.gov/research/umls/rxnorm/>`_ (a standardized drug nomenclature maintained by the National Library of Medicine), but
I think that `DrugBank <https://www.drugbank.ca/>`_ is the best option because it is comprehensive, it combines detailed drug data with comprehensive drug target information,
it has an open source (`Creative Common’s Attribution-NonCommercial 4.0 International License <https://creativecommons.org/licenses/by-nc/4.0/legalcode>`_)
license, and it is easy to use. A newer resource  `DrugCentral <http://drugcentral.org>`_ can also be used to map between many resources.
**TODO** this is an issue that will require thought and consensus building. Please communicate ideas/comments to Peter.
Currently, the PDXNet simulation is showing data from DrugBank as a literal (String). ::

    PDXNET:PAT-248 PDXNET:currentTreatmentDrug  "Leuprolide[DB00007;53714-56-0]" .

The String currently shows the name (Leuprolide), the DrugBank ID (DB00007), and the CAS id (53714-56-0). If we decide to
go with DrugBank, then probable the triple should be formed like this. ::

    PDXNET:PAT-248 PDXNET:currentTreatmentDrug  drugbank:DB00007 .

By adding other information from DrugBank to the RDF data available in our query engine, it would be possible to formulate
expressive queries about PDX models that have been treated by drugs that correspond to some overall treatment category (e.g.,
Leuprolide corresponds to L02AE - Gonadotropin releasing hormone analogues), have certain indications (e.g., Leuprolide is
indicated for Advanced Prostate Cancer), interact with certain drugs (e.g., Allicin;	The therapeutic efficacy of Allicin
can be decreased when used in combination with Leuprolide), etc.

11. **Current treatment protocol (dose; details)**
There is currently no ontology that I know of for representing dosages. There are many ways of representing dosages,
e.g., 10 mg/day or 5 mg b.i.d. **TODO** discuss what methodology would work best for PDX centers.


12. **Prior treatment protocol**
The medication data should be represented as above. The surgery data could be represented using MedDRA codes
(a rich and highly specific standardised medical terminology to facilitate sharing of regulatory information internationally
for medical products used by humans), but MedDRA does not have an open license and it may be difficult to reuse/redistribute,
and so if we want to use MedDRA we would need to come to an agreement with them.
MeSH would be an option, although MeSH is not always ontologically well structured, but there are a large number of terms.
The NCI thesaurus has a hierarchy of terms for Intervention or Procedure, including Cancer Diagnostic or Therapeutic
procedure, including terms for operations such
as `Mastectomy (NICT:C15277) <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp&ns=ncit?dictionary=NCI_Thesaurus&code=C15277>`_.
 This is probably sufficient for our needs, and I would suggest we use this.
**TODO** -- decide if the NCIT codes are sufficient for our needs. I suggest that we examine the subhierarchy underneath
the term `Cancer Diagnostic or Therapeutic Procedure (Code C79426) <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp&ns=ncit?dictionary=NCI_Thesaurus&code=C79426>`_.

13. **Response to prior treatment**
progressive disease (RECIST1.1)
These items can be represented in the NCIT, which has a subhierarchy
for `Clinical Course of Disease (Code C35461) <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp&ns=ncit?dictionary=NCI_Thesaurus&code=C35461>`_,
which includes items such as “Complete remission”, “Progressive disease” and many more.
Currently, the PDXIntegrator uses the following five terms

* notAssessed
* completeResponse
* partialResponse
* stableDisease
* progressiveDisease

**TODO** Decide on whether we want to limit this category to a small number of terms (like the above), to allow
any term from the NCIT Clinical Course of Disease subhierarchy, or choose some other scheme.
Currently, I am using the PDXNET namespace for these terms in the RDF code,
but we should use the NCIT namespace once we have decided where to take this.


14. **Virology status**
Probably the NCIT subhierarchy
of `Viral infection (Code C3439) <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp&ns=ncit?dictionary=NCI_Thesaurus&code=C3439>`_,
(which includes these viruses and many more) would be best.
We can represent this in RDX using a scheme such as this. ::

    PDXNET:PAT-248 PDXNET:virologyStatus  NCIT:C141405 .


where `NCIT:C141405 <https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&ns=ncit&code=C141405>`_
is the code for Hepatitis B Virus Positive (Code C141405). Note that we may either want to use the terms for virus infection
(which is a clinical diagnosis) or for serology (as in this example, with the term coming from the Laboratory Finding subhierarchy of NCIT).
It depends on how we want to model this.
**TODO** Determine the terminology and the depth of detail we want to capture.
