package org.jax.pdxintegrator.ncit.neoplasm;

import com.github.phenomics.ontolib.ontology.data.ImmutableTermId;
import com.github.phenomics.ontolib.ontology.data.ImmutableTermPrefix;
import com.github.phenomics.ontolib.ontology.data.TermId;
import com.github.phenomics.ontolib.ontology.data.TermPrefix;
import org.jax.pdxintegrator.exception.PDXException;

public class NcitNeoplasmTerm {
    /** The numerical part of an NCIT concept id (e.g., id would be 123 for NCIT_C123) */
    private final int id;
    /** The label (title) of this NCIT term. */
    private final String label;
    /** In our OLPG/ontolib library, term ids are represented by a prefix and a code. */
    private final static TermPrefix NCIT_PREFIX= new ImmutableTermPrefix("NCIT");

    /**
     *
     * @param idString a String such as NCIT_C123
     * @param label The title of this NCIT term
     * @throws PDXException if we cannot parse the id String, an exception is thrown.
     */
    public NcitNeoplasmTerm(String idString, String  label) throws PDXException{
        if (!idString.startsWith("NCIT_C")) {
            throw new PDXException("Malformed NCIT id: "+idString);
        }
        idString=idString.substring(6);
        try {
            id=Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new PDXException("Malformed NCIT id (could not parse numeric id: "+idString);
        }
        this.label=label;
    }

    public int getId() {
        return id;
    }

    public String getIdAsString() {
        return String.format("NCIT:C%d",id);
    }

    public String getLabel() {
        return label;
    }

    public TermId getTermId() { return new ImmutableTermId(NCIT_PREFIX,String.format("C%d",this.id));

    }


}
