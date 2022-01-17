package com.roshka.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "Response")
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

    @Override
    public String toString() {
        return "ResponseErrorDto [codigo=" + codigo + ", error=" + error + "]";
    }

}
