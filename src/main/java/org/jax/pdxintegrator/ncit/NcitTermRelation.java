package org.jax.pdxintegrator.ncit;

import com.github.phenomics.ontolib.formats.mpo.MpoRelationQualifier;
import com.github.phenomics.ontolib.ontology.data.TermId;
import com.github.phenomics.ontolib.ontology.data.TermRelation;

public class NcitTermRelation implements TermRelation {

    /** Serial UId for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Source {@link TermId}.
     */
    private final TermId source;

    /**
     * Destination {@link TermId}.
     */
    private final TermId dest;

    /** Id for this term relation, corresponds to Id of edge in graph. */
    private final int id;

    /**
     * {@link NcitRelationQualifier} for this term relation.
     */
    private final NcitRelationQualifier relationQualifier;

    /**
     * Constructor.
     *
     * @param source Source {@link TermId}.
     * @param dest Destination {@link TermId}.
     * @param id The term relation's Id, corresponds to Id of edge in graph.
     * @param relationQualifier The relation's further qualifier.
     */
    public NcitTermRelation(TermId source, TermId dest, int id,
                           NcitRelationQualifier relationQualifier) {
        this.source = source;
        this.dest = dest;
        this.id = id;
        this.relationQualifier = relationQualifier;
    }

    @Override
    public TermId getSource() {
        return source;
    }

    @Override
    public TermId getDest() {
        return dest;
    }

    @Override
    public int getId() {
        return id;
    }

    /**
     * @return The relation's qualifier.
     */
    public NcitRelationQualifier getRelationQualifier() {
        return relationQualifier;
    }

    @Override
    public String toString() {
        return "NcitTermRelation [source=" + source + ", dest=" + dest + ", id=" + id
                + ", relationQualifier=" + relationQualifier + "]";
    }

}
