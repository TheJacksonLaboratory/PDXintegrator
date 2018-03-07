Model creation module
=====================

The following table shows the recommendations from the  `PDX-MI manuscript <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_.


+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Field                          | Rec | Example                              |  PDXNet                                        |
+================================+=====+======================================+================================================+
| Submitter PDX     ID           | E   | PDX#123                              | CenterID:PatientID                             |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Mouse strain                   | E   |NOD.Cg-Prkdc<scid> Il2rg<tm1Wj>l/SzJ  | create PDXNet classes?                         |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Mouse source                   | E   |The Jackson Laboratory                | create PDXNet classes?                         |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Strain immune system humanised?| E   |yes/no                                |  PDXNET:No  (No humanized mice in PDXNet)      |                         
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Type of humanisation           | E   |CD34+hematopoietic stem cell-engrafted|  NONE                                          |    
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Tumor preparation              | E   |suspension                            | ?                                              |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Injection Type and Site        | E   |subcutaneous; right flank             | ?                                              |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Mouse treatment for engraftment| D   |estrogen    treatment                 | ?                                              |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Engraftment rate               | D   |80%                                   | literal                                        |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
| Engraftment time               | D   |8 weeks                               | literal                                        |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+

1. **Submitter PDX ID**
This field is analogous to PatientID. We display as CenterID:PDXID and keep an internal ID that will not be shown
externally to act as a primary key. For instance, PDX 123 from JAX would be shown on the PDXNet website as JAX:PDX-123

2. **Mouse Strain**.
We will allow strains to be denoted according to the MGI guidelines. 

3. **Mouse Source**
Institution providing the strain. An enumeration.

4. **Strain    immune    system    humanised?**
No.  PDX Net will not have PDX models with humanized mice.

5. **Type of humanisation**
N/A for PDXNet

6. **Tumor    preparation**
tumor    solid,    cell    suspension,    asite
Enumeration (**TODO** Need input: list of all the methods and whether any vocabulary exists?)

7. **Injection type and site**
subcutaneous;    right    flank
**TODO** is this two fields?
Enumeration (**TODO** Need input: list of all the methods and whether any vocabulary exists?)

8. **Mouse treatment for engraftment**
estrogen    treatment
Enumeration (**TODO** Need input: list of all the methods and whether any vocabulary exists?)

9. **Engraftment rate**
80%. Would it be better to state “n of m” rather than a percentage? **TODO** Use percent or N of M?

10. **Engraftment time**
8 weeks
Number of weeks/days **TODO** decide on units: days or weeks.
