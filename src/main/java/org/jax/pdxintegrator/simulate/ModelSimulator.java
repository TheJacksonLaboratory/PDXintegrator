package org.jax.pdxintegrator.simulate;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VCARD;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.simulate.patient.PdxModelSimulator;

public class ModelSimulator {

    private PdxModel pdxmodel;

    /**
     * Central place to create a new simulated patient. Calling code should ensure that the id's passed to this
     * constructor are distinct in case multiple patients are to be simulated.
     * @param id
     */
    public ModelSimulator(int id) {
    }




    private void simulatePatient(int id) {
        PdxModelSimulator simulator = new PdxModelSimulator(id);
        this.pdxmodel = simulator.getPdxmodel();
    }


}
