package com.roshka.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAPIDto extends ResponseErrorDto {
    private List<NoticiaDto> lista;

    public List<NoticiaDto> getLista() {
        return lista;
    }

    public void setLista(List<NoticiaDto> lista) {
        this.lista = lista;
    }

}
