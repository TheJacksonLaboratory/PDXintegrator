package org.jax.pdxintegrator.ncit;


import com.github.phenomics.ontolib.graph.data.ImmutableDirectedGraph;
import com.github.phenomics.ontolib.graph.data.ImmutableEdge;
import com.github.phenomics.ontolib.io.base.OntologyOboParser;
import com.github.phenomics.ontolib.io.obo.OboImmutableOntologyLoader;
import com.github.phenomics.ontolib.io.obo.OboOntologyEntryFactory;
import com.github.phenomics.ontolib.io.obo.Stanza;
import com.github.phenomics.ontolib.ontology.data.ImmutableOntology;
import com.github.phenomics.ontolib.ontology.data.TermId;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

import java.io.File;
import java.io.IOException;

/**
 * Parser for the National Cancer Institute's Thesaurus (NCIT) obo file {@code ncit.obo}.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 * @version 0.1.1
 */
public class NcitOboParser implements OntologyOboParser<NcitOntology> {

    /** Path to the OBO file to parse. */
    private final File oboFile;

    /** Whether debugging is enabled or not. */
    private final boolean debug;

    /**
     * Constructor.
     *
     * @param oboFile The OBO file to read.
     * @param debug Whether or not to enable debugging.
     */
    public NcitOboParser(File oboFile, boolean debug) {
        this.oboFile = oboFile;
        this.debug = debug;
    }

    /**
     * Constructor, disabled debugging.
     *
     * @param oboFile The OBO file to read.
     */
    public NcitOboParser(File oboFile) {
        this(oboFile, false);
    }

    /**
     * Parse OBO file into {@link NcitOntology} object.
     *
     * @return {@link NcitOntology} from parsing OBO file.
     * @throws IOException In case of problems with file I/O.
     */
    @SuppressWarnings("unchecked")
    public NcitOntology parse() throws IOException {
        final OboImmutableOntologyLoader<NcitTerm, NcitTermRelation> loader =
                new OboImmutableOntologyLoader<>(oboFile, debug);
        final NcitOboFactory factory = new NcitOboFactory();
        final ImmutableOntology<NcitTerm, NcitTermRelation> o = loader.load(factory);

        // Convert ImmutableOntology into Mpontology. The casts here are ugly and require the
        // @SuppressWarnings above but this saves us one factory layer of indirection.
        return new NcitOntology((ImmutableSortedMap<String, String>) o.getMetaInfo(),
                (ImmutableDirectedGraph<TermId, ImmutableEdge<TermId>>) o.getGraph(), o.getRootTermId(),
                o.getNonObsoleteTermIds(), o.getObsoleteTermIds(),
                (ImmutableMap<TermId, NcitTerm>) o.getTermMap(),
                (ImmutableMap<Integer, NcitTermRelation>) o.getRelationMap());
    }

    /**
     * @return The OBO file to parse.
     */
    public File getOboFile() {
        return oboFile;
    }


}
