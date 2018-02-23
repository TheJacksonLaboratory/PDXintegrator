package org.jax.pdxintegrator.simulate;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.github.phenomics.ontolib.ontology.data.ImmutableTermId;
import com.github.phenomics.ontolib.ontology.data.TermId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.patient.*;
import org.jax.pdxintegrator.model.tumor.PdxClinicalTumor;
import org.jax.pdxintegrator.ncit.neoplasm.NcitTerm;
import org.jax.pdxintegrator.uberon.UberonTerm;

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


    private final TermId metastasis = ImmutableTermId.constructWithPrefix("NCIT:C19151");
    private final TermId primaryNeoplasm = ImmutableTermId.constructWithPrefix("NCIT:C8509");
    private final TermId recurrence = ImmutableTermId.constructWithPrefix("NCIT:C3352");



    /** List of NCIT Terms for neoplasms. Our simulation will choose one of these diagnoses at random. */
    private final List<NcitTerm> neoplasmTerms;
    /** List of terms like "Grade 2" */
    private final List<NcitTerm> gradeTerms;
    private final List<NcitTerm> stageTerms;

    private final List<UberonTerm> uberonTerms;



    private PdxModel pdxmodel=null;

    public PdxModelSimulator(int id, List<NcitTerm> neoplasms, List<NcitTerm> grades, List<NcitTerm> stages, List<UberonTerm> uberons) {
        this.patientID=String.format("PAT-%d",id);
        logger.trace(String.format("Simulating patient %s (will choose random diagnosis from %d NCIT terms",patientID,neoplasms.size()));
        this.gender=getRandomGender();
        this.age=getRandomAge();
        this.consent=getRandomConsent();
        this.ethnicityRace=getRandomEthnicity();
        this.neoplasmTerms=neoplasms;
        this.gradeTerms=grades;
        this.stageTerms=stages;
        this.uberonTerms=uberons;
        Objects.requireNonNull(neoplasms);
        Objects.requireNonNull(stageTerms);
        Objects.requireNonNull(uberons);
        diagnosis=getRandomNCITTermId();


        PdxPatient patient  = buildPatient();
        PdxClinicalTumor clinicalTumor = buildClinicalTumor(patient);
        // same for other categories
        buildModel(patient,clinicalTumor);
    }


    public PdxModel getPdxmodel() {
        return pdxmodel;
    }

    private void buildModel(PdxPatient patient, PdxClinicalTumor clinicalTumor ) {
        this.pdxmodel = new PdxModel(patient,clinicalTumor);
    }




    private PdxClinicalTumor buildClinicalTumor(PdxPatient patient ) {
        String tumorID=String.format("TUMOR-%s",patient.getSubmitterPatientID());
        TermId uberon = getUberonId(patient.getDiagnosis());
        TermId category = getRandomTumorCategory();
        TermId diagnosis = patient.getDiagnosis();
        TermId grade = getRandomTumorGrade();
        TermId stage = getRandomStage();
        return new PdxClinicalTumor.Builder(tumorID).
                tissueOfOrigin(uberon).
                tumorCategory(category).
                histology(diagnosis).
                grade(grade).
                stage(stage).
                build();
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
     * For now we return a random uberon term. Better would be to return the matching tissue for the NCIT diagnosis
     * @param ncitTerm The term of the cancer for which we are retrieving the corresponding UBERON anatomy id (TODO -- add logic!)
     * @return termid of UBERON anatomy term corresponding to the cancer diagnosis in ncitTerm
     */
    private TermId getUberonId(TermId ncitTerm) {
        int randomIndex=random.nextInt(this.uberonTerms.size());
        UberonTerm term = uberonTerms.get(randomIndex);
        return term.getTermId();
    }


    private TermId  getRandomTumorGrade() {
        int randomindex = random.nextInt(this.gradeTerms.size());
        NcitTerm grade = gradeTerms.get(randomindex);
        return grade.getTermId();
    }

    private TermId  getRandomStage() {
        int randomindex = random.nextInt(this.stageTerms.size());
        NcitTerm stage = stageTerms.get(randomindex);
        return stage.getTermId();
    }


    private TermId getRandomTumorCategory() {
        int randomIndex = random.nextInt(3);
        if (randomIndex==0) { return metastasis; }
        else if (randomIndex==1) { return primaryNeoplasm; }
        else return recurrence;
    }


    /**
     * @return a TermId that represents an NCIT neoplasm term.
     */
    private TermId getRandomNCITTermId() {

        logger.trace("getting random number for size="+neoplasmTerms.size());
        int randomIndex=random.nextInt(this.neoplasmTerms.size());
        NcitTerm term = neoplasmTerms.get(randomIndex);
        return term.getTermId();
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
