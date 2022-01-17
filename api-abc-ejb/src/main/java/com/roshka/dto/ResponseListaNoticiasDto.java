package com.roshka.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "listaNoticias")
public class ResponseListaNoticiasDto {
    private List<NoticiaDto> lista;

    public List<NoticiaDto> getLista() {
        return lista;
    }

    public void setLista(List<NoticiaDto> lista) {
        this.lista = lista;
    }

    @Override
    public String toString() {
        return "ResponseListaNoticiasDto [lista=" + lista + "]";
    }

    
    
}
