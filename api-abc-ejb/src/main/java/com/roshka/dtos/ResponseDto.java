package com.roshka.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto {
    private List<DatumDto> data;

    public List<DatumDto> getData() {
        return data;
    }

    public void setData(List<DatumDto> value) {
        this.data = value;
    }

    @Override
    public String toString() {
        return "ResponseDto [data=" + data + "]";
    }

}
