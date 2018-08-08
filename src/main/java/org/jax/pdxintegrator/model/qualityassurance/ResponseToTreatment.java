package org.jax.pdxintegrator.model.qualityassurance;

public enum ResponseToTreatment {

    NOT_ASSESSED("Not assessed"),
    COMPLETE_RESPONSE("Complete response"),
    PARTIAL_RESPONSE("Partial response"),
    STABLE_DISEASE("Stable disease"),
    PROGRESSIVE_DISEASE("Progressive disease");

    private final String name;

    ResponseToTreatment(String n) {
        name=n;
    }

    public String getResponseToStandardOfCareString(){ return name; }
    
    public static ResponseToTreatment getResponse(String response){
        response = response.toLowerCase().trim();
        switch(response){
            case "cr":
                return ResponseToTreatment.COMPLETE_RESPONSE;
            case "complete response":
                return ResponseToTreatment.COMPLETE_RESPONSE;
            case "pd":
                return ResponseToTreatment.PROGRESSIVE_DISEASE;
            case "progressive disease":
                return ResponseToTreatment.PROGRESSIVE_DISEASE;
            case "pr":
                return ResponseToTreatment.PARTIAL_RESPONSE;
            case "partial response":
                return ResponseToTreatment.PARTIAL_RESPONSE;
            case "sd":
                return ResponseToTreatment.STABLE_DISEASE;
            case "stable disease":
                return ResponseToTreatment.STABLE_DISEASE;
            default:
                System.out.println("Cant convert "+response+" to a response for treatment using not assessed");
                return ResponseToTreatment.NOT_ASSESSED;
        }
    }
}

