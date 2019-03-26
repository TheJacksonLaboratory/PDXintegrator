/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jax.pdxintegrator.model.tumor;

/**
 *
 * @author sbn
 */
public enum TumorGrade {
    
    
    GX("GX: Cannot be assessed  (also G9)"),
    G1("G1: Well differentiated"),
    G2("G2: Moderately differentiated"),
    G3("G3: Poorly Differentiated"),
    G4("G4: Undifferentiated"),
    Low("Low Grade"),
    Intermediate("Intermediate Grade"),
    High("High Grade"),
    UNK("Unknown");

    private final String name;

    TumorGrade(String n) {
        name=n;
    }
    
     public String getTumorGrade(){ return name; }
    
    public static TumorGrade getTumorGrade(String gradeIn){
        String grade="";
        gradeIn = gradeIn.toUpperCase().trim();
        switch(gradeIn){
            case "GRADE:1":
                gradeIn = "G1";
                break;
            case "GRADE:I":
                gradeIn = "G1";
                break;
            case "GRADE:2":
                gradeIn = "G2";
                break;
            case "GRADE:II":
                gradeIn = "G2";
                break;
            case "GRADE:3":
                gradeIn = "G3";
                break;
            case "GRADE:III":
                gradeIn = "G3";
                break;
            case "GRADE:4":
                gradeIn = "G4";
                break;
            case "GRADE:IV":
                gradeIn = "G4";
        }
        
        if(gradeIn.length()>2){
            grade = gradeIn.substring(0,2);
        }else{
            grade = gradeIn;
        }
       
        switch(grade){
            case "GX":
                return TumorGrade.GX;
            case "G1":
                return TumorGrade.G1;
            case "G2":
                return TumorGrade.G2;
            case "G3":
                return TumorGrade.G3;
            case "G4":
                return TumorGrade.G4;
            case "LO":
                return TumorGrade.Low;
            case "HI":
                return TumorGrade.High;
            case "IN":
                return TumorGrade.Intermediate;
            default:
              //  System.out.println("Cant convert '"+gradeIn+"' to a tumor grade. Using place holder 'Unknown'");
                return TumorGrade.UNK;
        }
    }
    
}
