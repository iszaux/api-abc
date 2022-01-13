package com.roshka.rest;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.roshka.beans.NoticiaBean;
import com.roshka.dto.NoticiaDto;
import com.roshka.dto.ResponseAPIDto;
import com.roshka.utils.ABCApiException;

@Path("/consulta")
public class NoticiaRest {

    @EJB
    NoticiaBean noticiaBean;

    @GET
    @Produces("application/json")
    public ResponseAPIDto getNoticias(@QueryParam("q") String q) throws ABCApiException, IOException {

        ResponseAPIDto response = new ResponseAPIDto();

        try {

            List<NoticiaDto> listaNoticias = noticiaBean.getNoticias(q);
            response.setLista(listaNoticias);

        } catch (ABCApiException e) {

            response.setCodigo(e.getCodigo());
            response.setError(e.getDescripcion());
            Response.status(e.getStatus());

            return response;
        }

        return response;
    }

}
