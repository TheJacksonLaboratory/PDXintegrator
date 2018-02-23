package org.jax.pdxintegrator.uberon;

import com.github.phenomics.ontolib.ontology.data.ImmutableTermId;
import com.github.phenomics.ontolib.ontology.data.ImmutableTermPrefix;
import com.github.phenomics.ontolib.ontology.data.TermId;
import com.github.phenomics.ontolib.ontology.data.TermPrefix;
import org.jax.pdxintegrator.exception.PDXException;

public class UberonTerm {

    /** The numerical part of an NCIT concept id (e.g., id would be 123 for NCIT_C123) */
    private final int id;
    /** The label (title) of this NCIT term. */
    private final String label;
    /** In our OLPG/ontolib library, term ids are represented by a prefix and a code. */
    private final static TermPrefix UBERON_PREFIX= new ImmutableTermPrefix("UBERON");

    /**
     *
     * @param idString a String such as NCIT_C123
     * @param label The title of this NCIT term
     * @throws PDXException if we cannot parse the id String, an exception is thrown.
     */
    UberonTerm(String idString, String  label) throws PDXException {
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

    public int getId() {
        return id;
    }

    public String getIdAsString() {
        return String.format("UBERON:%d",id);
    }

    public String getLabel() {
        return label;
    }

    public TermId getTermId() { return new ImmutableTermId(UBERON_PREFIX,String.format("%d",this.id));

    }

}
