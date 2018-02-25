package org.jax.pdxintegrator.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * This class represents one line of the simplified drugbank entry we made.
 */
public class DrugBankEntry {

    private final String name;
    private final String cas;
    private final String dbID;
    private final String category;

    public String getName() {
        return name;
    }

    public String getCas() {
        return cas;
    }

    public String getDbID() {
        return dbID;
    }

    public String getCategory() {
        return category;
    }

    public DrugBankEntry(String fields[]) {
        if (fields.length!=4) {
            System.err.println("SHOULD NEVER HAPPEN. WE WERE EXPECTING FOUR FIELDS FOR DRUGBANK BOT GOT " + fields.length);
            System.err.println(Arrays.stream(fields).collect(Collectors.joining("\t")));
            System.exit(1);
        }
            name=fields[0];
            cas=fields[1];
            dbID=fields[2];
            category=fields[3];
    }

    public static List<DrugBankEntry> parseDrugBankTabFile() {
        List<DrugBankEntry> lst = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/drugbank.tab"));
            String line;
            while ((line=br.readLine())!=null) {
                String A[] = line.split("\t");
                DrugBankEntry entry = new DrugBankEntry(A);
                lst.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lst;
    }


}
