package com.roshka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionDto {

    private String basic;

    public String getBasic() {
        return basic;
    }

    public void setBasic(String value) {
        this.basic = value;
    }

}
