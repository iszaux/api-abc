package com.roshka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseErrorDto {
    private String codigo;
    private String error;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String value) {
        this.codigo = value;
    }

    public String getError() {
        return error;
    }

    public void setError(String value) {
        this.error = value;
    }
}
