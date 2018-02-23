package org.jax.pdxintegrator.model.tumor;

import com.github.phenomics.ontolib.ontology.data.TermId;

/**
 * Main class for modeling the Clinical/Tumor part of the PDX-MI
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxClinicalTumor {


    private final String submitterTumorID;
    /** The term id for an UBERON term for the anatomical tissue of origin of the tumor. */
    private final TermId tissueOfOrigin;
    /** NCIT Term id for primary, recurrence, metastasis. */
    private final TermId category;
    /** NCIT Term id for tissue histology */
    private final TermId tissueHistology;

    public TermId getTumorGrade() {
        return tumorGrade;
    }

    public TermId getStage() {
        return stage;
    }

    /** e.g., "Grade 2" */
    private final TermId tumorGrade;

    private final TermId stage;


    public String getSubmitterTumorID() {
        return submitterTumorID;
    }

    public TermId getTissueOfOrigin() {
        return tissueOfOrigin;
    }

    public TermId getCategory() {
        return category;
    }
    public TermId getTissueHistology() {
        return tissueHistology;
    }

    PdxClinicalTumor(String tumorID, TermId tissue, TermId cat, TermId histology, TermId grade, TermId stage) {
        submitterTumorID=tumorID;
        tissueOfOrigin=tissue;
        category=cat;
        tissueHistology=histology;
        this.tumorGrade=grade;
        this.stage=stage;
    }



    public static class Builder {
        private final String submitterTumorID;

        private TermId tissueOfOrigin=null;
        private TermId category=null;
        private TermId tissueHistology;
        private TermId tumorGrade=null;
        private TermId stage=null;

        public Builder(String id) {
            this.submitterTumorID=id;
        }
        public Builder tissueOfOrigin(TermId tissue) {
            tissueOfOrigin=tissue;
            return this;
        }
        public Builder tumorCategory(TermId cat) {
            category=cat;
            return this;
        }

        public Builder histology(TermId histo) {
            this.tissueHistology=histo;
            return this;
        }

        public Builder grade(TermId grade) {
            this.tumorGrade=grade;
            return this;
        }

        public Builder stage(TermId grade) {
            this.stage=grade;
            return this;
        }

        public PdxClinicalTumor build() {
            return new PdxClinicalTumor(submitterTumorID,tissueOfOrigin,category,tissueHistology, tumorGrade,stage);
        }
    }
}
