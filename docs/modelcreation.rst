Model creation module
=====================

The following table shows the recommendations from the  `PDX-MI manuscript <https://www.ncbi.nlm.nih.gov/pubmed/29092942/>`_.


+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Field                          | Rec | Description                          | Example                              |  PDXNet                                        |
+================================+=====+======================================+======================================+================================================+
| Submitter PDX ID               | E   | Unique ID for the PDX Model          | PDX#123                              | CenterID:PatientID                             |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Passage                        | E   | Tissue Passsage. P0 tissue is from PT| P1                                   | create PDXNet classes?                         |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Mouse strain                   | E   | Name of strain used for engraftment  |NOD.Cg-Prkdc<scid> Il2rg<tm1Wj>l/SzJ  | create PDXNet classes?                         |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Mouse source                   | E   | Institution supplying strain         |The Jackson Laboratory                | create PDXNet classes?                         |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Mouse sex                      | D   | Sex of mouse used for engaftment     | Male                                 |                                                | 
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Mouse immune system humanised? | E   |                                      |yes/no                                |                                                |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Type of humanisation           | E   |                                      |CD34+hematopoietic stem cell-engrafted|                                                |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Engraftment Procedure          | D   |                                      |suspension                            | ?                                              |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Engraftment Method             | D   |                                      |                                      | ?                                              |
+--------------------------------+-----+--------------------------------------+--------------------------------------+------------------------------------------------+
| Engraftment Site               | D   |                                      |right flank                           | ?                                              |
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

2. **Passage**.
We neet to have a standard for this. Suggesting that P0 would indicate engrafted tissue is from the patient. P1 tissue comes from P0.
P2 from P1 etc.

3. **Mouse Strain**.
We will allow strains to be denoted according to the MGI guidelines. 

4. **Mouse Source**.
Institution providing the strain. An enumeration.

5 **Mouse Sex**.

6. **Strain Immune System Humanised?**
Yes or No

7. **Type of Humanisation**.
Description of humanisation method.

8. **Engraftmet Procedure**.
Enumeration (**TODO** Need input: list of all the methods and whether any vocabulary exists?)

9. **Engraftment Method**.
Enumeration (**TODO** Need input: list of all the methods and whether any vocabulary exists?)

10. **Engraftment Site**.
Subcutaenous, Mammary Fat Pad, Orthotopic, etc.
Where the tissue is engrafted to the mouse.

11. **Mouse Treatment for Engraftment**.
estrogen    treatment
Enumeration (**TODO** Need input: list of all the methods and whether any vocabulary exists?)

12. **Engraftment Rate**.
80%. Would it be better to state “n of m” rather than a percentage? **TODO** Use percent or N of M?

13. **Engraftment Time**.
8 weeks
Number of weeks/days PDMR uses "Estimated days from implant to 500 mm3"
Both Rate and Time would benefit from noting if tissue was cryopreserved. Should this be captured as a field? 
