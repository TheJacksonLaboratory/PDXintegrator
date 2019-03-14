package org.jax.pdxintegrator.uberon;


import org.jax.pdxintegrator.exception.PDXException;
import org.monarchinitiative.phenol.ontology.data.TermId;

public class UberonTerm {

    /** The numerical part of an NCIT concept id (e.g., id would be 123 for NCIT_C123) */
    private final int id;
    /** The label (title) of this NCIT term. */
    private final String label;
    /** In our OLPG/ontolib library, term ids are represented by a prefix and a code. */
    private final static String UBERON_PREFIX= "UBERON:";

    /**
     *
     * @param idString a String such as NCIT_C123
     * @param label The title of this NCIT term
     * @throws PDXException if we cannot parse the id String, an exception is thrown.
     */
    public UberonTerm(String idString, String  label) throws PDXException {
        if (!idString.startsWith("UBERON_")) {
            throw new PDXException("Malformed UBERON id: "+idString);
        }
        idString=idString.substring(7);
        try {
            id=Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new PDXException("Malformed UBERON id (could not parse numeric id: "+idString);
        }
        this.label=label;
    }

    public String getId() {
        return String.format("%07d",id);
    }

    public String getIdAsString() {
        return String.format("UBERON:%07d",id);
    }

    public String getLabel() {
        return label;
    }

    public TermId getTermId() { return TermId.of(UBERON_PREFIX,String.format("%07d",this.id));

    }

}
