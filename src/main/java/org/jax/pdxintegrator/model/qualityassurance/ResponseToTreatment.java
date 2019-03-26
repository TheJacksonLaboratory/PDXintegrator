package org.jax.pdxintegrator.model.qualityassurance;

public enum ResponseToTreatment {

    NOT_ASSESSED("Not assessed"),
    COMPLETE_RESPONSE("Complete response"),
    PARTIAL_RESPONSE("Partial response"),
    STABLE_DISEASE("Stable disease"),
    PROGRESSIVE_DISEASE("Progressive disease"),
    LOG2_FOLD_CHANGE("Log2 fold change from baseline");

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
            case "disease progression":
                return ResponseToTreatment.PROGRESSIVE_DISEASE;
            case "pr":
                return ResponseToTreatment.PARTIAL_RESPONSE;
            case "partial response":
                return ResponseToTreatment.PARTIAL_RESPONSE;
            case "sd":
                return ResponseToTreatment.STABLE_DISEASE;
            case "stable disease":
                return ResponseToTreatment.STABLE_DISEASE;
            case "log2":
                return ResponseToTreatment.LOG2_FOLD_CHANGE;
            default:
          //      System.out.println("Cant convert '"+response+"' to a response for treatment. Using 'Not assessed'");
                return ResponseToTreatment.NOT_ASSESSED;
        }
    }
}

