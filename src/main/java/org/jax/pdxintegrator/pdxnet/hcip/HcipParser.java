package org.jax.pdxintegrator.pdxnet.hcip;

import com.google.common.collect.ImmutableList;
import org.jax.pdxintegrator.model.PdxSex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A class to parse the CSV file from HCIP
 *		paclitaxel

 * <ol>
 *     <li>Sample: Y0ACP5</li>
 *     <li>Sex: Female</li>
 *     <li>Age: 40</li>
 *     <li>Ethnicity: caucasion</li>
 *     <li>Diagnosis: Invasive Ductal Carcinoma</li>
 *     <li>Treatment: doxorubicin</li>
 *     <li>regimen: single dose</li>
 *     <li>regimen: doxorubicin	single dose	paclitaxel; 14 weekly cycles; 127mg/m2</li>
 *     <li>progressive?: progressive disease or blank</li>
 *     <li>???: yes</li>
 *     <li>???: 704112</li>
 *     <li>Tissue of origin: Breast</li>
 *     <li>Primary vs. Met: Primary or Metastasis</li>
 *     <li>Sample derived by: surgical sample</li>
 *     <li>Histological diagnosis: Invasive Ductal Carcinoma</li>
 *     <li>Histologic Grade: 3; Nottingham</li>
 *     <li>Stage: Stage IV</li>
 *     <li>Markers: ER negative; PR negative; HER2 negative</li>
 *     <li>???: no</li>
 *     <li>Strain: NOD/SCID and NSG (Jax)</li>
 *     <li>???: no</li>
 *     <li>???: N/A</li>
 *     <li>???: solid</li>
 *     <li>???: cleared mammary fat pad</li>
 *     <li>???: blank or estrogen pellet</li>
 *     <li>???: 80-100%</li>
 *     <li>???: histology; IHC</li>
 *     <li>???: multiple</li>
 *     <li>PDX regimen: doxorubicin+cyclophosphamide</li>
 *     <li>PDX dosing: dox:1mg/kg/wk iv; cyclophosph 100mg/kg/wk ip</li>
 *     <li>???: paclitaxel</li>
 *     <li>???: 10mg/kg/q3d iv</li>
 *     <li>???: progressive disease</li>
 *     <li>???: doxorubicin</li>
 *     <li>???: 10mg/kg/wk iv</li>
 *     <li>???: stable disease/complete response</li>
 *     <li>???: exome; RNAseq; CNV; RPPA</li>
 *     <li>???: Lung</li>
 *     <li>???: NOD.SCID: 2 months to 1cm</li>
 *     <li>???: yes</li>
 *     <li>???: PMC3553601</li>
 *     <li>contact: alana.welm@hci.utah.edu</li>
 * </ol>
 */

public class HcipParser {

    private static final int SAMPLE_IDX=0;
    private static final int SEX_IDX=1;
    private static final int AGE_IDX=2;
    private static final int ETHNICITY_IDX=3;
    private static final int DIAGNOSIS_IDX=4;
    private static final int TREATMENT_IDX=5;
    private static final int REGIMEN_IDX=6;

    /** Number of fields in a HCIP record. Seems to be 42. Use 40 for now, there is some variability. TODO check */
    private static final int HCIP_FIELD_COUNT=40;


    private final String hcipPath;

    private ImmutableList<HcipEntry> entrylist=null;


    public HcipParser(String path) {
        this.hcipPath=path;
    }

    public void debugPrint() {
        if (entrylist==null) {
            System.out.println("could not print Hcip entries -- list was null");
            return;
        }
        for (HcipEntry entry : entrylist) {
            System.out.println(entry.toRDF());
        }
    }



    public void parseHcipData() {
        ImmutableList.Builder<HcipEntry> builder = new ImmutableList.Builder<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.hcipPath));
            String line=null;
            while ((line=br.readLine())!=null) {
                System.out.println(line);
                HcipEntry entry = parseline(line);
                if (entry!=null) builder.add(entry);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.entrylist=builder.build();
    }


    private HcipEntry parseline(String line) {
        String F[] = line.split(",");
        String sample=F[SAMPLE_IDX];
        String sex=F[SEX_IDX];
        PdxSex pdxSex = PdxSex.PdxSexFromString(sex);
        String age=F[AGE_IDX];
        String ethnicity=F[ETHNICITY_IDX];
        String diagnosis=F[DIAGNOSIS_IDX];
        String treatment=F[TREATMENT_IDX];
        String regimen=F[REGIMEN_IDX];
        HcipEntry entry = new HcipEntry(sample,pdxSex,diagnosis);
        return entry;
    }


}