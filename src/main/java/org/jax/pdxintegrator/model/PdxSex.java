package org.jax.pdxintegrator.model;

import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Represent sex as an enumeration constant...
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 * @version 0.1.0
 */
public enum PdxSex {
    FEMALE("F","f","female","woman"),
    MALE("M","m","male","man"),
    UNKNOWN;

    private static final Logger logger = LogManager.getLogger();
    /** List of synonyms for male and female, kept in lower case for ease of searching. */
    private ImmutableSet<String> synonyms;

    /**
     * @param syn list of synonyms.
     */
    PdxSex(String ...syn) {
        ImmutableSet.Builder<String> builder=new ImmutableSet.Builder<>();
        for (String s:syn) {
            builder.add(s.toLowerCase());
        }
        synonyms=builder.build();
    }
    /** @return The PdxSex enumeration constant corresponding to the argument. */
    public static PdxSex PdxSexFromString(String s) {
        String slower=s.toLowerCase();
        if (MALE.synonyms.contains(slower)) return MALE;
        else if (FEMALE.synonyms.contains(slower)) return FEMALE;
        else {
            logger.error("Could not identify PdxSex for " + s);
            return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case MALE: return "male";
            case FEMALE: return "female";
            case UNKNOWN:
                default:return "unknown";
        }
    }

}
