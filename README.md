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
