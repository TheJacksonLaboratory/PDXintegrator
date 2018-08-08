/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jax.pdxintegrator.model.omicsfile;

/**
 *
 * @author sbn
 */
public class PdxOmicsFile {
    
            private String patientID;
            private String modelID;
            private String accessLevel;
            private String createdDateTime;
            private String dataCategory;	
            private String dataFormat;
            private String dataType;
            private String sampleType;	
            private String experimentalStrategy;
            private String fileSize;
            private String platform;	
            private String captureKit;	
            private String updatedDateTime;
            private Boolean isFFPE;
            private Boolean isPairedEnd;	
            private String fileName;
            private String passage;

    /**
     * @return the patientID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * @param patientID the patientID to set
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * @return the modelID
     */
    public String getModelID() {
        return modelID;
    }

    /**
     * @param modelID the modelID to set
     */
    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    /**
     * @return the accessLevel
     */
    public String getAccessLevel() {
        return accessLevel;
    }

    /**
     * @param accessLevel the accessLevel to set
     */
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * @return the createdDatetime
     */
    public String getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * @param createdDatetime the createdDatetime to set
     */
    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /**
     * @return the dataCategory
     */
    public String getDataCategory() {
        return dataCategory;
    }

    /**
     * @param dataCategory the dataCategory to set
     */
    public void setDataCategory(String dataCategory) {
        this.dataCategory = dataCategory;
    }

    /**
     * @return the dataFormat
     */
    public String getDataFormat() {
        return dataFormat;
    }

    /**
     * @param dataFormat the dataFormat to set
     */
    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the sampleType
     */
    public String getSampleType() {
        return sampleType;
    }

    /**
     * @param sampleType the sampleType to set
     */
    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    /**
     * @return the experimentalStrategy
     */
    public String getExperimentalStrategy() {
        return experimentalStrategy;
    }

    /**
     * @param experimentalStrategy the experimentalStrategy to set
     */
    public void setExperimentalStrategy(String experimentalStrategy) {
        this.experimentalStrategy = experimentalStrategy;
    }

    /**
     * @return the fileSize
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * @param platform the platform to set
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * @return the captureKit
     */
    public String getCaptureKit() {
        return captureKit;
    }

    /**
     * @param captureKit the captureKit to set
     */
    public void setCaptureKit(String captureKit) {
        this.captureKit = captureKit;
    }

    /**
     * @return the updatedDatetime
     */
    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    /**
     * @param updatedDatetime the updatedDatetime to set
     */
    public void setUpdatedDateTime(String updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    /**
     * @return the isFFPE
     */
    public Boolean getIsFFPE() {
        return isFFPE;
    }

    /**
     * @param isFFPE the isFFPE to set
     */
    public void setIsFFPE(Boolean isFFPE) {
        this.isFFPE = isFFPE;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the passage
     */
    public String getPassage() {
        return passage;
    }

    /**
     * @param passage the passage to set
     */
    public void setPassage(String passage) {
        this.passage = passage;
    }

    /**
     * @return the isPairedEnd
     */
    public Boolean getIsPairedEnd() {
        return isPairedEnd;
    }

    /**
     * @param isPairedEnd the isPairedEnd to set
     */
    public void setIsPairedEnd(Boolean isPairedEnd) {
        this.isPairedEnd = isPairedEnd;
    }
    
}
