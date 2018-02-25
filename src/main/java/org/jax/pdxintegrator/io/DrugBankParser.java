package org.jax.pdxintegrator.io;

import org.jax.pdxintegrator.drugbank.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DrugBankParser {

    private final String pathToDrugbankXmlFile;

    private final String outputFile;

    public DrugBankParser(String path, String output) {
        outputFile=output;
        this.pathToDrugbankXmlFile=path;
    }


    public void parse() {
        System.out.println("JAXB Drugbank XML parser: "+ this.pathToDrugbankXmlFile);
        System.out.println();


        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            JAXBContext context = JAXBContext.newInstance(DrugbankType.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            //DrugbankType user = (DrugbankType)
            JAXBElement<DrugbankType> root= unmarshaller.unmarshal(new StreamSource(new File(this.pathToDrugbankXmlFile)),DrugbankType.class);
            DrugbankType dbt =  root.getValue();
            List<DrugType> druglist = dbt.getDrug();
            int i=0;
            for (DrugType drug : druglist) {
                String fields[] = new String[4];

                    //System.out.println(drug.getName() + ":" + drug.getCasNumber() +": "+drug.getMechanismOfAction());
                    fields[0] =drug.getName();
                    fields[1] = drug.getCasNumber();
                    List<DrugbankDrugSaltIdType> saltidlist = drug.getDrugbankId();
                    String ids = saltidlist.stream().
                            filter(DrugbankDrugSaltIdType::isPrimary).
                            map(DrugbankDrugSaltIdType::getValue).
                            collect(Collectors.joining(";"));
                    fields[2] = ids!=null?ids:"-";
                    AtcCodeListType atcclist = drug.getAtcCodes();
                    List<AtcCodeType> atclist = atcclist.getAtcCode();
                    for (AtcCodeType atc : atclist) {
                        List<AtcCodeLevelType> levels = atc.getLevel();
                        if (levels!=null && levels.size()>0) {
                            AtcCodeLevelType lev = levels.get(levels.size()-1);
                            System.out.println(atc.toString() + ": " + atc.getCode() );
                            System.out.println("\t"+lev.getCode() +" "+lev.getValue());
                            fields[3]=lev.getValue();
                        }
                    }
                    writer.write(Arrays.stream(fields).collect(Collectors.joining("\t"))+"\n");

            }
            writer.close();

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
