Model study module
==================

Tumors from PDX often undergo comprehensive genomic characterization and/or treatment in controlled dosing studies
to define therapeutic response and resistance. PDX-MI includes desirable fields in the reporting of these studies
that supplement existing guidelines for reporting on in vivo
biomedical research ( `Meehan et al., 2017 <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_).


+------------------------------+-----+--------------------------------+------------------------------------------------+
| Field                        | Rec | Example                        |  PDXNet                                        |
+==============================+=====+================================+================================================+
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

What about Study ID or Name (if not available could be model + passage + treatment)?

1. **Treatment**
List of treatment(s) as generic terms for medications. 

2. **Treatment protocol**
For each treatment drug provide drug name, dosage, route and frequency.

3. **Treatment response**
complete    response,    partial    response,    stable    disease,    progressive
disease.

4 **Model Passage**
Passage(s) of models used in study. Assumption is P0 modles have tissue directly from patient. 
P1 is engrafted with tissue from P0 etc. 

 **Tumor    OMICS**:    This was removed. It will be populated based on types of files uploaded for Study/Model. 
 
5,6,7 **Development  of metastases in strain**
We will code this as Yes/no; site as uberon; passage as enumeration

8. **Lag time/doubling time of tumor**
We will code this as the number of hours.




