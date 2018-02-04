package org.jax.pdxintegrator.simulate.patient;

import java.util.Random;

import com.github.phenomics.ontolib.ontology.data.ImmutableTermId;
import com.github.phenomics.ontolib.ontology.data.ImmutableTermPrefix;
import com.github.phenomics.ontolib.ontology.data.TermId;
import com.github.phenomics.ontolib.ontology.data.TermPrefix;
import javafx.scene.control.RadioMenuItem;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.patient.*;

/**
 * This class is intended to illustrate how to instantiate the PdxPatient module with data.
 * We use a very simply way of generating random data, beware it is not intended to be used
 * for any analysis--for demonstration only!
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxModelSimulator {

    private final String patientID;
    private final Gender gender;
    private final Age age;
    private final TermId diagnosis;
    private final Consent consent;
    private final EthnicityRace ethnicityRace;

    private final Random random=new Random();



    PdxModel pdxmodel=null;

    public PdxModelSimulator(int id) {
        this.patientID=String.format("PAT-%d",id);
        this.gender=getRandomGender();
        this.age=getRandomAge();
        this.consent=getRandomConsent();
        this.ethnicityRace=getRandomEthnicity();
        diagnosis=getRandomNCITTermId();
        PdxPatient patient  = buildPatient();
        // same for other categories
        buildModel(patient);
    }


    public PdxModel getPdxmodel() {
        return pdxmodel;
    }

    private void buildModel(PdxPatient patient ) {
        this.pdxmodel = new PdxModel(patient);
    }




    private PdxPatient buildPatient() {
        PdxPatient.Builder builder= new PdxPatient.Builder(patientID,
                gender,
                age,
                diagnosis,
                consent,
                ethnicityRace);
        return builder.build();
    }





    /**
     * ToDo -- input the NCIT for this.
     * @return
     */
    private TermId getRandomNCITTermId() {
        TermPrefix NCIT_PREFIX = new ImmutableTermPrefix("NCIT");
        Random r = new Random();
        int diagnosisCode=r.nextInt(10_000);
        TermId ncitTermId = new ImmutableTermId(NCIT_PREFIX,String.valueOf(diagnosisCode));
        return ncitTermId;
    }


    /**
     * @return a random Gender
     */
    private Gender getRandomGender() {
        Gender [] values = Gender.values();
        return values[random.nextInt(values.length)];
    }


    private Age getRandomAge() {
        Age[] values = Age.values();
        return values[random.nextInt(values.length)];
    }

    private Consent getRandomConsent() {
        Consent[] values = Consent.values();
        return values[random.nextInt(values.length)];
    }

    private EthnicityRace getRandomEthnicity() {
        EthnicityRace[] values = EthnicityRace.values();
        return values[random.nextInt(values.length)];
    }



}
