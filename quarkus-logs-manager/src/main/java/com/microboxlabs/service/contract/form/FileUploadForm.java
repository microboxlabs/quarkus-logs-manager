package com.microboxlabs.service.contract.form;

import jakarta.ws.rs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.io.InputStream;

public class FileUploadForm {
    @FormParam("file")
    @PartType("application/octet-stream")
    private InputStream file;

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }
}
