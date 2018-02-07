package org.jax.pdxintegrator.simulate.patient;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.github.phenomics.ontolib.ontology.data.ImmutableTermId;
import com.github.phenomics.ontolib.ontology.data.ImmutableTermPrefix;
import com.github.phenomics.ontolib.ontology.data.TermId;
import com.github.phenomics.ontolib.ontology.data.TermPrefix;
import javafx.scene.control.RadioMenuItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.patient.*;
import org.jax.pdxintegrator.ncit.neoplasm.NcitNeoplasmTerm;

/**
 * This class is intended to illustrate how to instantiate the PdxPatient module with data.
 * We use a very simply way of generating random data, beware it is not intended to be used
 * for any analysis--for demonstration only!
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxModelSimulator {
    private static final Logger logger = LogManager.getLogger();
    private final String patientID;
    private final Gender gender;
    private final Age age;
    private final TermId diagnosis;
    private final Consent consent;
    private final EthnicityRace ethnicityRace;
    private Random random=new Random();

    /** List of NCIT Terms for neoplasms. Our simulation will choose one of these diagnoses at random. */
    private final List<NcitNeoplasmTerm> neoplasmTerms;



    PdxModel pdxmodel=null;

    public PdxModelSimulator(int id, List<NcitNeoplasmTerm> neoplasms) {
        this.patientID=String.format("PAT-%d",id);
        logger.trace(String.format("Simulating patient %s (will choose random diagnosis from %d NCIT terms",patientID,neoplasms.size()));
        this.gender=getRandomGender();
        this.age=getRandomAge();
        this.consent=getRandomConsent();
        this.ethnicityRace=getRandomEthnicity();
        this.neoplasmTerms=neoplasms;
        diagnosis=getRandomNCITTermId();
        Objects.requireNonNull(neoplasms);

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
     * @return a TermId that represents an NCIT neoplasm term.
     */
    private TermId getRandomNCITTermId() {

        logger.trace("getting random number for size="+neoplasmTerms.size());
        int randomIndex=random.nextInt(this.neoplasmTerms.size());
        NcitNeoplasmTerm term = neoplasmTerms.get(randomIndex);
        TermId ncitTermId = term.getTermId();
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
