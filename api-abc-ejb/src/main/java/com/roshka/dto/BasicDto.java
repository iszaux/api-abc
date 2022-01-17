package com.roshka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicDto {
    private String url;

    public String getURL() {
        return url;
    }

    public void setURL(String value) {
        this.url = value;
    }

}
