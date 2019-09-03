package com.netcomm.hfcl.model;

/**
 * Created by Netcomm on 8/28/2017.
 */

public class FileUploadModel {

    private String fileName;
    private String fileContent;
    private String fileExtension;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
