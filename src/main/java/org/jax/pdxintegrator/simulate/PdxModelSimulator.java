package org.jax.pdxintegrator.simulate;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.github.phenomics.ontolib.ontology.data.ImmutableTermId;
import com.github.phenomics.ontolib.ontology.data.TermId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.io.DrugBankEntry;
import org.jax.pdxintegrator.model.PdxModel;
import org.jax.pdxintegrator.model.modelcreation.MouseTreatmentForEngraftment;
import org.jax.pdxintegrator.model.modelcreation.PdxModelCreation;
import org.jax.pdxintegrator.model.modelcreation.TumorPrepMethod;
import org.jax.pdxintegrator.model.patient.*;
import org.jax.pdxintegrator.model.qualityassurance.ModelCharacterization;
import org.jax.pdxintegrator.model.qualityassurance.PdxQualityAssurance;
import org.jax.pdxintegrator.model.qualityassurance.ResponseToStandardOfCare;
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

    private List<DrugBankEntry> drugbankentrylist;

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

        drugbankentrylist = DrugBankEntry.parseDrugBankTabFile();

        diagnosis=getRandomNCITTermId();

        PdxPatient patient  = buildPatient();
        PdxClinicalTumor clinicalTumor = buildClinicalTumor(patient);
        PdxModelCreation modelCreation = buildModelCreation(patient);
        PdxQualityAssurance qualityAssurance = buildQualityAssuranceModule(patient);
        // same for other categories
        buildModel(patient,clinicalTumor,modelCreation,qualityAssurance);
    }


    public PdxModel getPdxmodel() {
        return pdxmodel;
    }

    private void buildModel(PdxPatient patient,
                            PdxClinicalTumor clinicalTumor,
                            PdxModelCreation modelCreation,
                            PdxQualityAssurance quality) {
        this.pdxmodel = new PdxModel(patient,clinicalTumor,modelCreation,quality);
    }



    private PdxModelCreation buildModelCreation(PdxPatient patient) {
        String pdxID=String.format("PDX-%s",patient.getSubmitterPatientID());
        TumorPrepMethod tumorprepmethod=getRandomTumorPrepMethod();
        MouseTreatmentForEngraftment mouseRx = getRandomMouseTreatmentForEngraftment();
        double engraftment = getRandomEngraftmentPercent();
        int engraftmentDays=getRandomEngraftmentTime();
        return new PdxModelCreation.Builder(pdxID).
                mouseStrain("NOD.Cg-Prkdc<scid> Il2rg<tm1Wj>l/SzJ").
                mouseSource("JAX").
                tumorPreparationMethod(tumorprepmethod).
                mouseTreatmentForEngraftment(mouseRx).
                engraftmentRate(engraftment).
                engraftmentTimeInDays(engraftmentDays).
                humanizationType("CD34+hematopoietic stem cell-engrafted").build();
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

    private PdxQualityAssurance buildQualityAssuranceModule(PdxPatient patient) {
        ModelCharacterization characterization = getRandomModelCharacterization();
        ResponseToStandardOfCare response = getRandomResponseToStandardOfCare();
        boolean tumorNotMouseNotEbv  = getRandomBoolean();
        boolean animalHealthStatusSufficient= getRandomBoolean();
        boolean passageQaPerformed= getRandomBoolean();
        PdxQualityAssurance.Builder builder = new PdxQualityAssurance.Builder(characterization,tumorNotMouseNotEbv,passageQaPerformed).
                response(response).
                animalHealthStatusOk(animalHealthStatusSufficient);
        return builder.build();
    }



    private PdxPatient buildPatient() {
        PdxPatient.Builder builder= new PdxPatient.Builder(patientID,
                gender,
                age,
                diagnosis,
                consent,
                ethnicityRace).currentTreatmentDrug(getRandomCancerDrug());
        return builder.build();
    }


    /**
     * For now we return a random uberon term. Better would be to return the matching tissue for the NCIT diagnosis
     * @param ncitTerm The term of the cancer for which we are retrieving the corresponding UBERON anatomy id (TODO -- add logic!)
     * @return termid of UBERON anatomy term corresponding to the cancer diagnosis in ncitTerm
     */
    private TermId getUberonId(TermId ncitTerm) {
        if (uberonTerms==null || uberonTerms.isEmpty()) {
            logger.error("Uberon terms not initialized...terminating simulations");
            System.exit(1);
        }
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

    private double getRandomEngraftmentPercent() {
        return 100*new Random().nextDouble();
    }

    private int getRandomEngraftmentTime() {
        int leftLimit = 3;
        int rightLimit = 30;
        int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
        return generatedInteger;
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

    private TumorPrepMethod  getRandomTumorPrepMethod() {
        TumorPrepMethod[] vals = TumorPrepMethod.values();
        int randomindex = random.nextInt(vals.length);
        TumorPrepMethod stage = vals[randomindex];
        return stage;
    }

    private MouseTreatmentForEngraftment getRandomMouseTreatmentForEngraftment() {
        MouseTreatmentForEngraftment[] vals = MouseTreatmentForEngraftment.values();
        int randomindex = random.nextInt(vals.length);
        return vals[randomindex];
    }

    private  ModelCharacterization getRandomModelCharacterization() {
        ModelCharacterization[] vals = ModelCharacterization.values();
        int randomindex = random.nextInt(vals.length);
        return vals[randomindex];
    }

    private  ResponseToStandardOfCare getRandomResponseToStandardOfCare() {
        ResponseToStandardOfCare[] vals = ResponseToStandardOfCare.values();
        int randomindex = random.nextInt(vals.length);
        return vals[randomindex];
    }

    private boolean getRandomBoolean() {
        double d = new Random().nextDouble();
        return d>0.5D;
    }

    private String getRandomCancerDrug() {
        int r = new Random().nextInt(10);
        int i=0;
        String current=null;
        for (DrugBankEntry entry : drugbankentrylist) {
            if (entry.getCategory().startsWith("ANTINEOPLASTIC")) {
                current=String.format("%s[%s;%s]",entry.getName(),entry.getDbID(),entry.getCas());
                i++;
                if (i>=r) return current;
            }
        }
        return current; // should never happen, but whatever
    }

}
