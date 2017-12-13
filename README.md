# PDXintegrator
Code for mapping PDX Net data to a common data model.
THe project can be run as a series of steps. 

## Download NCIT ontology file
To start with, we download
the NCIT obo file to the data directory (which will be created) with the 
following command. 
```aidl
$ mvn clean package
$ java -jar target/pdxintegrator.jar download
```
## Parse ncit.obo

The NCIT file is large, and we need to run the program with more memory (todo how mch)
```
$ java --Xmx4g -jar target/pdxintegrator.jar map
```


Note that there is currently an issue with the ontology parsing.
line 439468:5 mismatched input '"' expecting QuotedString
Exception in thread "main" java.lang.NullPointerException
	at com.github.phenomics.ontolib.io.obo.OboParserListener.exitKeyValueDef(OboParserListener.java:290)
	at de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser$KeyValueDefContext.exitRule(Antlr4OboParser.java:1929)
	at org.antlr.v4.runtime.Parser.triggerExitRuleEvent(Parser.java:408)
	at org.antlr.v4.runtime.Parser.exitRule(Parser.java:642)
	at de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser.oboFile(Antlr4OboParser.java:214)
	at com.github.phenomics.ontolib.io.obo.OboParser.parseInputStream(OboParser.java:119)
	at com.github.phenomics.ontolib.io.obo.OboParser.parseFile(OboParser.java:71)
	at com.github.phenomics.ontolib.io.obo.OboImmutableOntologyLoader.load(OboImmutableOntologyLoader.java:100)
	at org.jax.pdxintegrator.ncit.NcitOboParser.parse(NcitOboParser.java:55)
	at org.jax.pdxintegrator.command.MapCommand.parseNcit(MapCommand.java:37)
	at org.jax.pdxintegrator.command.MapCommand.execute(MapCommand.java:26)
	at org.jax.pdxintegrator.PdxIntegrator.main(PdxIntegrator.java:19)

```
def: "A recombinant human papillomavirus (HPV), genetically engineered fusion protein vaccine in which the three HPV16 viral proteins L2, E6 and E7 are fused together in a single tandem fusion protein (TA-CIN; HPV16 L2\\E6\\E7), with potential immunoprotective and antineoplastic properties. Upon administration, HPV16 L2\\E6\\E7 fusion protein vaccine TA-CIN may stimulate the immune system to generate HPV16 E6\\E7-specific CD4+ and CD8+ T-cell responses as well as the induction of L2-specific antibodies. In addition, this vaccine may prevent infection and the development of other HPV16-associated diseases. L2, a minor viral capsid protein, is able to induce a strong antibody response against certain HPV types." [] {http://purl.obolibrary.org/obo/NCIT_P378="NCI"}
```
This is solved by manually editing the line to
```
def: "A recombinant human papillomavirus (HPV)" [] {http://purl.obolibrary.org/obo/NCIT_P378="NCI"}
```
Then we get
```
line 477326:26 extraneous input 'EXACT' expecting {' ', Eol2, Comment2}
Exception in thread "main" java.lang.IllegalStateException: Failed to parse at line 477326 due to extraneous input 'EXACT' expecting {' ', Eol2, Comment2}
	at com.github.phenomics.ontolib.io.obo.OboParser$1.syntaxError(OboParser.java:109)
	at org.antlr.v4.runtime.ProxyErrorListener.syntaxError(ProxyErrorListener.java:41)
	at org.antlr.v4.runtime.Parser.notifyErrorListeners(Parser.java:544)
	at org.antlr.v4.runtime.DefaultErrorStrategy.reportUnwantedToken(DefaultErrorStrategy.java:349)
	at org.antlr.v4.runtime.DefaultErrorStrategy.singleTokenDeletion(DefaultErrorStrategy.java:513)
	at org.antlr.v4.runtime.DefaultErrorStrategy.sync(DefaultErrorStrategy.java:238)
	at de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser.keyValueName(Antlr4OboParser.java:1744)
	at de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser.termStanzaKeyValue(Antlr4OboParser.java:807)
	at de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser.termStanza(Antlr4OboParser.java:694)
	at de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser.stanza(Antlr4OboParser.java:619)
	at de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser.stanzas(Antlr4OboParser.java:549)
	at de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser.oboFile(Antlr4OboParser.java:205)
	at com.github.phenomics.ontolib.io.obo.OboParser.parseInputStream(OboParser.java:119)
	at com.github.phenomics.ontolib.io.obo.OboParser.parseFile(OboParser.java:71)
	at com.github.phenomics.ontolib.io.obo.OboImmutableOntologyLoader.load(OboImmutableOntologyLoader.java:100)
	at org.jax.pdxintegrator.ncit.NcitOboParser.parse(NcitOboParser.java:55)
	at org.jax.pdxintegrator.command.MapCommand.parseNcit(MapCommand.java:37)
	at org.jax.pdxintegrator.command.MapCommand.execute(MapCommand.java:26)
	at org.jax.pdxintegrator.PdxIntegrator.main(PdxIntegrator.java:19)
```
This is line
```
name: CDISC Questionnaire EXACT Test Name Terminology
```
This was solved by changing the line to
```
name: CDISC Questionnaire
```
Similarly, line 477347 had to be repaired:
```
name: CDISC Questionnaire EXACT Test Code Terminology
```
And

```
name: EXACT Questionnaire Question
```
(changed to Exact Questionnaire Question).

and line 477690
```
name: EXACT - Chest Feel Congested
```
change EXACT to Exact

and line 477740, and 477762 and 477784 and 477806 (and the following ca. 15 stanzas)
```
name: EXACT - How Often Cough Today
name: EXACT - Bring Up Mucus When Coughing
name: EXACT - Difficult to Bring Up Mucus
name: EXACT - Have Chest Discomfort

```

Then we get
```
Exception in thread "main" com.github.phenomics.ontolib.base.OntoLibRuntimeException: NAME tag must have cardinality 1
but was null (Stanza [type=TERM, stanzaEntries=[StanzaEntryId [id=NCIT:C4297, getType()=ID, getTrailingModifier()=null,
getComment()=null], StanzaEntryDef [text=A benign vascular lesion characterized by the presence of a complex network of
communicating arterial and venous vascular structures., dbXrefList=DbXrefList [dbXrefs=[]], getType()=DEF,
getTrailingModifier()=TrailingModifier [keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P378,
value=NCI]]], getComment()=null], StanzaEntrySynonym [text=Arteriovenous Angioma, termSynonymScope=EXACT,
synonymTypeName=null, dbXrefList=DbXrefList [dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier
[keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P383, value=SY],
KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384, value=NCI]]], getComment()=null],
StanzaEntrySynonym [text=Arteriovenous Hemangioma, termSynonymScope=EXACT, synonymTypeName=null,
dbXrefList=DbXrefList [dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier [keyValue=[KeyValue
[key=http://purl.obolibrary.org/obo/NCIT_P383, value=PT], KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384,
value=NCI]]], getComment()=null], StanzaEntrySynonym [text=Arteriovenous Malformation, termSynonymScope=EXACT,
synonymTypeName=null, dbXrefList=DbXrefList [dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier
[keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P383, value=SY],
KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384, value=NCI]]], getComment()=null],
StanzaEntrySynonym [text=Racemose Angioma, termSynonymScope=EXACT, synonymTypeName=null,
dbXrefList=DbXrefList [dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier
[keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P383, value=SY],
KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384, value=NCI]]], getComment()=null],
StanzaEntrySynonym [text=Racemose Hemangioma, termSynonymScope=EXACT, synonymTypeName=null, dbXrefList=DbXrefList
[dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier [keyValue=[KeyValue
[key=http://purl.obolibrary.org/obo/NCIT_P383, value=SY], KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384, value=NCI]]],
getComment()=null], StanzaEntryIsA [id=NCIT:C125190, getType()=IS_A, getTrailingModifier()=null, getComment()=Retired Concept 2015],
StanzaEntryGeneric [tag=property_value, value=NCIT:NHC0 "C4297" xsd:string, getType()=GENERIC, getTrailingModifier()=null,
getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P106 "Disease or Syndrome" xsd:string, getType()=GENERIC,
getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P108 "Arteriovenous Hemangioma"
xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value,
value=NCIT:P200 "Arteriovenous_Malformation" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null],
StanzaEntryGeneric [tag=property_value, value=NCIT:P207 "C0334533" xsd:string, getType()=GENERIC, getTrailingModifier()=null,
getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P310 "Retired_Concept" xsd:string, getType()=GENERIC,
getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P334 "9123/0" xsd:string,
getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P366
"Arteriovenous_Hemangioma" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null],
StanzaEntryGeneric [tag=property_value, value=NCIT:P98 "Thu Apr 23 14:45:43 EDT 2015 - See 'Arteriovenous_Malformation'"
xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryIsObsolete [value=true, getType()=IS_OBSOLETE, getTrailingModifier()=null, getComment()=null]], entryByType={DEF=[StanzaEntryDef [text=A benign vascular lesion characterized by the presence of a complex network of communicating arterial and venous vascular structures., dbXrefList=DbXrefList [dbXrefs=[]], getType()=DEF, getTrailingModifier()=TrailingModifier [keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P378, value=NCI]]], getComment()=null]], GENERIC=[StanzaEntryGeneric [tag=property_value, value=NCIT:NHC0 "C4297" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P106 "Disease or Syndrome" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P108 "Arteriovenous Hemangioma" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P200 "Arteriovenous_Malformation" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P207 "C0334533" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P310 "Retired_Concept" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P334 "9123/0" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P366 "Arteriovenous_Hemangioma" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null], StanzaEntryGeneric [tag=property_value, value=NCIT:P98 "Thu Apr 23 14:45:43 EDT 2015 - See 'Arteriovenous_Malformation'" xsd:string, getType()=GENERIC, getTrailingModifier()=null, getComment()=null]], ID=[StanzaEntryId [id=NCIT:C4297, getType()=ID, getTrailingModifier()=null, getComment()=null]], IS_A=[StanzaEntryIsA [id=NCIT:C125190, getType()=IS_A, getTrailingModifier()=null, getComment()=Retired Concept 2015]], IS_OBSOLETE=[StanzaEntryIsObsolete [value=true, getType()=IS_OBSOLETE, getTrailingModifier()=null, getComment()=null]], SYNONYM=[StanzaEntrySynonym [text=Arteriovenous Angioma, termSynonymScope=EXACT, synonymTypeName=null, dbXrefList=DbXrefList [dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier [keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P383, value=SY], KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384, value=NCI]]], getComment()=null], StanzaEntrySynonym [text=Arteriovenous Hemangioma, termSynonymScope=EXACT, synonymTypeName=null, dbXrefList=DbXrefList [dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier [keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P383, value=PT], KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384, value=NCI]]], getComment()=null], StanzaEntrySynonym [text=Arteriovenous Malformation, termSynonymScope=EXACT, synonymTypeName=null, dbXrefList=DbXrefList [dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier [keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P383, value=SY], KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384, value=NCI]]], getComment()=null], StanzaEntrySynonym [text=Racemose Angioma, termSynonymScope=EXACT, synonymTypeName=null, dbXrefList=DbXrefList [dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier [keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P383, value=SY], KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384, value=NCI]]], getComment()=null], StanzaEntrySynonym [text=Racemose Hemangioma, termSynonymScope=EXACT, synonymTypeName=null, dbXrefList=DbXrefList [dbXrefs=[]], getType()=SYNONYM, getTrailingModifier()=TrailingModifier [keyValue=[KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P383, value=SY], KeyValue [key=http://purl.obolibrary.org/obo/NCIT_P384, value=NCI]]], getComment()=null]]}])

```

This is an obsolete term. I add the name: obsolete Arteriovenous_Hemangioma
```
[Term]
id: NCIT:C4297
def: "A benign vascular lesion characterized by the presence of a complex network of communicating arterial and venous vascular structures." [] {http://purl.obolibrary.org/obo/NCIT_P378="NCI"}
synonym: "Arteriovenous Angioma" EXACT [] {http://purl.obolibrary.org/obo/NCIT_P383="SY", http://purl.obolibrary.org/obo/NCIT_P384="NCI"}
synonym: "Arteriovenous Hemangioma" EXACT [] {http://purl.obolibrary.org/obo/NCIT_P383="PT", http://purl.obolibrary.org/obo/NCIT_P384="NCI"}
synonym: "Arteriovenous Malformation" EXACT [] {http://purl.obolibrary.org/obo/NCIT_P383="SY", http://purl.obolibrary.org/obo/NCIT_P384="NCI"}
synonym: "Racemose Angioma" EXACT [] {http://purl.obolibrary.org/obo/NCIT_P383="SY", http://purl.obolibrary.org/obo/NCIT_P384="NCI"}
synonym: "Racemose Hemangioma" EXACT [] {http://purl.obolibrary.org/obo/NCIT_P383="SY", http://purl.obolibrary.org/obo/NCIT_P384="NCI"}
is_a: NCIT:C125190 ! Retired Concept 2015
property_value: NCIT:NHC0 "C4297" xsd:string
property_value: NCIT:P106 "Disease or Syndrome" xsd:string
property_value: NCIT:P108 "Arteriovenous Hemangioma" xsd:string
property_value: NCIT:P200 "Arteriovenous_Malformation" xsd:string
property_value: NCIT:P207 "C0334533" xsd:string
property_value: NCIT:P310 "Retired_Concept" xsd:string
property_value: NCIT:P334 "9123/0" xsd:string
property_value: NCIT:P366 "Arteriovenous_Hemangioma" xsd:string
property_value: NCIT:P98 "Thu Apr 23 14:45:43 EDT 2015 - See 'Arteriovenous_Malformation'" xsd:string
is_obsolete: true

```
Then we have
```
line 1452097:9 mismatched input '"' expecting QuotedString
```
This is
```
synonym: "P450D\\X" EXACT [] {http://purl.obolibrary.org/obo/NCIT_P383="SY", http://purl.obolibrary.org/obo/NCIT_P384="NCI"}
```
I removed the "\\" and this solved the problem
Then we have
```
line 1654233:26 mismatched input '"' expecting {' ', Eol2, Comment2}
```
This is
```
property_value: NCIT:P325 "A terpene with two isoprene units head to tail with hydroxyl group attached to the head methyl group. The chemical structure is CC(=CCC\\C(=C\\CO)\\C)C." xsd:string {http://purl.obolibrary.org/obo/NCIT_P378="CRCH"}
```
I changed CC(=CCC\\C(=C\\CO)\\C)C to CC(=CCCC(=CCO)C)C => OK
Then we have
```
line 1738392:26 mismatched input '"' expecting {' ', Eol2, Comment2}
```
again we have
```
property_value: NCIT:P325 "An allyl-S compound with the structure C=CCSS/C=C\\CS(=O)CC=C." xsd:string {http://purl.obolibrary.org/obo/NCIT_P378="CRCH"}
```
I removed the \\ and several other instances.