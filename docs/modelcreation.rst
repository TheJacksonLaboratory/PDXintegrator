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
|Strain immune system humanised? | E   |yes/no                                | PDXNET:YEs, PDXNET:No                          |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
|Type of humanisation            | E   |CD34+hematopoietic stem cell-engrafted| ?                                              |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
|Tumor preparation               | E   |suspension                            | ?                                              |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
|Mouse treatment for engraftment | D   |estrogen    treatment                 | ?                                              |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
|Engraftment rate                | D   |80%                                   | literal                                        |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+
|Engraftment time                | D   |8 weeks                               | literal                                        |
+--------------------------------+-----+--------------------------------------+------------------------------------------------+

1. **Submitter PDX ID**
This field is analogous to PatientID. We display as CenterID:PDXID and keep an internal ID that will not be shown
externally to act as a primary key. For instance, PDX 123 from JAX would be shown on the PDXNet website as JAX:PDX-123

2. **Mouse strain (and source)**.
We will allow strains to be denoted according to the
Jackson    Laboratory
Strain nomenclature according to MGI guidelines. Source as enumeration (separate field!).


3. **Strain    immune    system    humanised?**
yes/no
enumeration

4. **Type of humanisation**
CD34+    hematopoietic    stem    cell-engrafted/    PBMC/    Thymus/
Thymus-fetal    liver/    iPSC/other
I need input from experts as to the proper vocabulary to describe humanized mouse strains.

5. **Tumor    preparation**
tumor    solid,    cell    suspension,    asite
Enumeration (need input: list of all the methods and whether any vocabulary exists?)

6. **Injection type and site**
subcutaneous;    right    flank
Enumeration (need input: list of all the methods and whether any vocabulary exists?)

7. **Mouse treatment for engraftment**
estrogen    treatment
Enumeration (need input: list of all the methods and whether any vocabulary exists?)


8. **Engraftment rate**
80%. It would be better to state “n of m” rather than a percentage.

9. **Engraftment time**
8 weeks
Number of weeks/days
