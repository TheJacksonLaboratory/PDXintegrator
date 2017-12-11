package org.jax.pdxintegrator.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum PdxSex {
    FEMALE("F","f","female","woman"),
    MALE("M","m","male","man"),
    UNKNOWN;

    private static final Logger logger = LogManager.getLogger();

    private ImmutableSet<String> synonyms;

    PdxSex(String ...syn) {
        ImmutableSet.Builder<String> builder=new ImmutableSet.Builder<>();
        for (String s:syn) {
            builder.add(s.toLowerCase());
        }
        synonyms=builder.build();
    }

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
