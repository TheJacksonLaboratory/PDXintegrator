Model creation module
=====================

The following table shows the recommendations from the  `PDX-MI manuscript <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_.


+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Field                          | Rec | Description                          | Example                              |  PDXNet                                        |
+================================+=====+======================================+======================================+================================================+
| Submitter PDX ID               | E   | Unique ID for the PDX Model          | PDX#123                              | CenterID:PatientID                             |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Mouse strain                   | E   | Name of strain used for engraftment  |NOD.Cg-Prkdc<scid> Il2rg<tm1Wj>l/SzJ  | create PDXNet classes?                         |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Mouse source                   | E   | Institution supplying strain         |The Jackson Laboratory                | create PDXNet classes?                         |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Mouse immune system humanised? | E   |                                      |yes/no                                | PDXNET:No  (No humanized mice in PDXNet)       |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Type of humanisation           | E   |                                      |CD34+hematopoietic stem cell-engrafted| NONE                                           |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Tumor preparation              | D   |                                      |suspension                            | ?                                              |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Injection Type                 | D   |                                      |subcutaneous                          | ?                                              |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Injection Site                 | D   |                                      |right flank                           | ?                                              |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Mouse treatment for engraftment| D   |                                      |estrogen    treatment                 | ?                                              |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Engraftment rate               | D   |                                      |80%                                   | literal                                        |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Engraftment time               | D   |                                      |8 weeks                               | literal                                        |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+

1. **Submitter PDX ID**.
This field is analogous to PatientID. We display as CenterID:PDXID and keep an internal ID that will not be shown
externally to act as a primary key. For instance, PDX 123 from JAX would be shown on the PDXNet website as JAX:PDX-123

2. **Mouse Strain**.
We will allow strains to be denoted according to the MGI guidelines. 

3. **Mouse Source**.
Institution providing the strain. An enumeration.

4. **Strain Immune System Humanised?**
No.  PDX Net will not have PDX models with humanized mice.

5. **Type of Humanisation**.
N/A for PDXNet

6. **Tumor Preparation**.
tumor    solid,    cell    suspension,    asite
Enumeration (**TODO** Need input: list of all the methods and whether any vocabulary exists?)

7. **Injection Type and Site**.
subcutaneous;    right    flank
**TODO** is this two fields?
Enumeration (**TODO** Need input: list of all the methods and whether any vocabulary exists?)

8. **Mouse Treatment for Engraftment**.
estrogen    treatment
Enumeration (**TODO** Need input: list of all the methods and whether any vocabulary exists?)

9. **Engraftment Rate**.
80%. Would it be better to state “n of m” rather than a percentage? **TODO** Use percent or N of M?

10. **Engraftment Time**.
8 weeks
Number of weeks/days **TODO** decide on units: days or weeks.
