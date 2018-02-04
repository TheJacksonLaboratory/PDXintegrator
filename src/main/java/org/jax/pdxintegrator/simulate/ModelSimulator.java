package org.jax.pdxintegrator.simulate;

import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VCARD;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.simulate.patient.PatientSimulator;

public class ModelSimulator {

    private PdxModel pdxmodel;

    /**
     * Central place to create a new simulated patient. Calling code should ensure that the id's passed to this
     * constructor are distinct in case multiple patients are to be simulated.
     * @param id
     */
    public ModelSimulator(int id) {
        createSmith();
        simulatePatient(id);
    }




    private void simulatePatient(int id) {
        PatientSimulator simulator = new PatientSimulator(id);

    }




    private void createSmith() {
        String personURI    = "http://somewhere/JohnSmith";
        String givenName    = "John";
        String familyName   = "Smith";
        String fullName     = givenName + " " + familyName;
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        //   and add the properties cascading style
        Resource johnSmith
                = model.createResource(personURI)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N,
                        model.createResource()
                                .addProperty(VCARD.Given, givenName)
                                .addProperty(VCARD.Family, familyName));

        // now write the model in XML form to a file
        model.write(System.out,"N-TRIPLES");
    }


}
