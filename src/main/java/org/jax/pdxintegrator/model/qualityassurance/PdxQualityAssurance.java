package org.jax.pdxintegrator.model.qualityassurance;
/**
 * This class represents the Model Creation module of the PDX-MI.
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class PdxQualityAssurance {

    
    

    
    private String modelID;
    private String strAnalysis;
    private Integer passageTested;
    private String strEvaluation;
    private String strNotes;
    private String clinicalDiagnosticMarkers;
    private String clinicalDiagnosticMarkerAssayResult;
    private String clinicalDiagnosticMarkerNotes;
    private String cd45IHC;
    private String cd45IHCAssayResult;
    private String PanCytokeratin;
    private String PanCytokeratinAssayResult;
    private String humanSpecificCytokeratin19;
    private String ebvTranscriptDetection;
    private String ebvTranscriptDetectionResult;
    private String mousePathogenStatus;
    private String overallEvaluation;
            
            
    private String qcMethod;
    private String qcResult;
    private String animalHealthStatus;
    
    
    public PdxQualityAssurance(String modelID){
        this.modelID = modelID;
        
    }

    /**
     * @return the modelID
     */
    public String getModelID() {
        return modelID;
    }

    /**
     * @return the passage
     */
    public Integer getPassageTested() {
        return passageTested;
    }

    /**
     * @param passage the passage to set
     */
    public void setPassageTested(Integer passage) {
        this.passageTested = passage;
    }

    /**
     * @return the qcMethod
     */
    public String getQcMethod() {
        return qcMethod;
    }

    /**
     * @param qcMethod the qcMethod to set
     */
    public void setQcMethod(String qcMethod) {
        this.qcMethod = qcMethod;
    }

    /**
     * @return the qcResult
     */
    public String getQcResult() {
        return qcResult;
    }

    /**
     * @param qcResult the qcResult to set
     */
    public void setQcResult(String qcResult) {
        this.qcResult = qcResult;
    }

    /**
     * @return the animalHealthStatus
     */
    public String getAnimalHealthStatus() {
        return animalHealthStatus;
    }

    /**
     * @param animalHealthStatus the animalHealthStatus to set
     */
    public void setAnimalHealthStatus(String animalHealthStatus) {
        this.animalHealthStatus = animalHealthStatus;
    }

    /**
     * @return the strAnalysis
     */
    public String getStrAnalysis() {
        return strAnalysis;
    }

    /**
     * @param strAnalysis the strAnalysis to set
     */
    public void setStrAnalysis(String strAnalysis) {
        this.strAnalysis = strAnalysis;
    }

    /**
     * @return the strEvaluation
     */
    public String getStrEvaluation() {
        return strEvaluation;
    }

    /**
     * @param strEvaluation the strEvaluation to set
     */
    public void setStrEvaluation(String strEvaluation) {
        this.strEvaluation = strEvaluation;
    }

    /**
     * @return the strNotes
     */
    public String getStrNotes() {
        return strNotes;
    }

    /**
     * @param strNotes the strNotes to set
     */
    public void setStrNotes(String strNotes) {
        this.strNotes = strNotes;
    }

    /**
     * @return the clinicalDiagnosticMarkers
     */
    public String getClinicalDiagnosticMarkers() {
        return clinicalDiagnosticMarkers;
    }

    /**
     * @param clinicalDiagnosticMarkers the clinicalDiagnosticMarkers to set
     */
    public void setClinicalDiagnosticMarkers(String clinicalDiagnosticMarkers) {
        this.clinicalDiagnosticMarkers = clinicalDiagnosticMarkers;
    }

    /**
     * @return the clinicalDiagnosticMarkerAssayResult
     */
    public String getClinicalDiagnosticMarkerAssayResult() {
        return clinicalDiagnosticMarkerAssayResult;
    }

    /**
     * @param clinicalDiagnosticMarkerAssayResult the clinicalDiagnosticMarkerAssayResult to set
     */
    public void setClinicalDiagnosticMarkerAssayResult(String clinicalDiagnosticMarkerAssayResult) {
        this.clinicalDiagnosticMarkerAssayResult = clinicalDiagnosticMarkerAssayResult;
    }

    /**
     * @return the clinicalDiagnosticMarkerNotes
     */
    public String getClinicalDiagnosticMarkerNotes() {
        return clinicalDiagnosticMarkerNotes;
    }

    /**
     * @param clinicalDiagnosticMarkerNotes the clinicalDiagnosticMarkerNotes to set
     */
    public void setClinicalDiagnosticMarkerNotes(String clinicalDiagnosticMarkerNotes) {
        this.clinicalDiagnosticMarkerNotes = clinicalDiagnosticMarkerNotes;
    }

    /**
     * @return the cd45IHC
     */
    public String getCd45IHC() {
        return cd45IHC;
    }

    /**
     * @param cd45IHC the cd45IHC to set
     */
    public void setCd45IHC(String cd45IHC) {
        this.cd45IHC = cd45IHC;
    }

    /**
     * @return the cd45IHCAssayResult
     */
    public String getCd45IHCAssayResult() {
        return cd45IHCAssayResult;
    }

    /**
     * @param cd45IHCAssayResult the cd45IHCAssayResult to set
     */
    public void setCd45IHCAssayResult(String cd45IHCAssayResult) {
        this.cd45IHCAssayResult = cd45IHCAssayResult;
    }

    /**
     * @return the PanCytokeratinAssayResult
     */
    public String getPanCytokeratinAssayResult() {
        return PanCytokeratinAssayResult;
    }

    /**
     * @param PanCytokeratinAssayResult the PanCytokeratinAssayResult to set
     */
    public void setPanCytokeratinAssayResult(String PanCytokeratinAssayResult) {
        this.PanCytokeratinAssayResult = PanCytokeratinAssayResult;
    }

    /**
     * @return the humanSpecificCytokeratin19
     */
    public String getHumanSpecificCytokeratin19() {
        return humanSpecificCytokeratin19;
    }

    /**
     * @param humanSpecificCytokeratin19 the humanSpecificCytokeratin19 to set
     */
    public void setHumanSpecificCytokeratin19(String humanSpecificCytokeratin19) {
        this.humanSpecificCytokeratin19 = humanSpecificCytokeratin19;
    }

    /**
     * @return the ebvTranscriptDetection
     */
    public String getEbvTranscriptDetection() {
        return ebvTranscriptDetection;
    }

    /**
     * @param ebvTranscriptDetection the ebvTranscriptDetection to set
     */
    public void setEbvTranscriptDetection(String ebvTranscriptDetection) {
        this.ebvTranscriptDetection = ebvTranscriptDetection;
    }

    /**
     * @return the ebvTranscriptDetectionResult
     */
    public String getEbvTranscriptDetectionResult() {
        return ebvTranscriptDetectionResult;
    }

    /**
     * @param ebvTranscriptDetectionResult the ebvTranscriptDetectionResult to set
     */
    public void setEbvTranscriptDetectionResult(String ebvTranscriptDetectionResult) {
        this.ebvTranscriptDetectionResult = ebvTranscriptDetectionResult;
    }

    /**
     * @return the mousePathogenStatus
     */
    public String getMousePathogenStatus() {
        return mousePathogenStatus;
    }

    /**
     * @param mousePathogenStatus the mousePathogenStatus to set
     */
    public void setMousePathogenStatus(String mousePathogenStatus) {
        this.mousePathogenStatus = mousePathogenStatus;
    }

    /**
     * @return the overallEvaluation
     */
    public String getOverallEvaluation() {
        return overallEvaluation;
    }

    /**
     * @param overallEvaluation the overallEvaluation to set
     */
    public void setOverallEvaluation(String overallEvaluation) {
        this.overallEvaluation = overallEvaluation;
    }

    /**
     * @return the PanCytokeratin
     */
    public String getPanCytokeratin() {
        return PanCytokeratin;
    }

    /**
     * @param PanCytokeratin the PanCytokeratin to set
     */
    public void setPanCytokeratin(String PanCytokeratin) {
        this.PanCytokeratin = PanCytokeratin;
    }

   
}
