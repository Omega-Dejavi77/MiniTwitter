package com.example.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUploadProfilePhoto {
    @SerializedName("fieldname")
    @Expose
    private String fieldName;

    @SerializedName("originalname")
    @Expose
    private String originalName;

    @SerializedName("encoding")
    @Expose
    private String encoding;

    @SerializedName("mimetype")
    @Expose
    private String mimeType;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("filename")
    @Expose
    private String filename;

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("size")
    @Expose
    private Integer size;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
