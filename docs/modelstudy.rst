Model study module
==================

Tumors from PDX often undergo comprehensive genomic characterization and/or treatment in controlled dosing studies
to define therapeutic response and resistance. PDX-MI includes desirable fields in the reporting of these studies
that supplement existing guidelines for reporting on in vivo
biomedical research ( `Meehan et al., 2017 <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_).


+------------------------------+-----+--------------------------------+------------------------------------------------+
| Field                        | Rec | Example                        |  PDXNet                                        |
+==============================+=====+================================+================================================+
| Study name or identifier     |     |PDX-123P3 Pertuzmab/Trastuzumab |  Needs to be unique to attach files            |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Treatment                    | D   |pertuzumab in  combination      |  List of 1 or more generic drugs               |
|                              |     |with trastuzumab; CHEMBL2007641 |                                                |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Treatment    protocol        | D   |trastuzumab (30 mg/kg loading   |  Additional module to capture                  |
|                              |     |dose, 15 mg/kg weekly);         |  Drug,Dose,Route,Frequency                     |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Treatment    Response        | D   |                                | RECIST Term                                    |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Passage                      | D   | P2                             | Integer                                        |
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Metastasis                   | D   | Yes                            | Yes or No                                      | 
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Metastasized to              | D   | Liver                          | Uberon                                         | 
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Metastasis in passage        | D   | P3                             | Integer                                        | 
+------------------------------+-----+--------------------------------+------------------------------------------------+
| Lag time/doubling time       | D   | 48h                            | separate elements                              |
+------------------------------+-----+--------------------------------+------------------------------------------------+
Table 2.5. Model study module. Rec: Recommendation; E: essential; D:desirable.

1. **Study Name or identifier** (if not available could be model + passage + treatment)
A way to uniquly identify the study. Human readable or unique ID

2. **Treatment**
List of treatment(s) as generic terms for medications. 

3. **Treatment protocol**
For each treatment drug provide drug name, dosage, route and frequency.

4. **Treatment response**
complete    response,    partial    response,    stable    disease,    progressive
disease.

5 **Model Passage**
Passage(s) of models used in study. Assumption is P0 modles have tissue directly from patient. 
P1 is engrafted with tissue from P0 etc. 

 **Tumor    OMICS**:    This was removed. It will be populated based on types of files uploaded for Study/Model. 
 
6,7,8 **Development  of metastases in strain**
We will code this as Yes/no; site as uberon; passage as enumeration

9. **Lag time/doubling time of tumor**
We will code this as the number of hours.
