package com.roshka.rest;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.roshka.beans.NoticiaBean;
import com.roshka.dto.NoticiaDto;
import com.roshka.dto.ResponseErrorDto;
import com.roshka.dto.ResponseListaNoticiasDto;
import com.roshka.enums.ErrorMessage;
import com.roshka.utils.ABCApiException;

@Path("/consulta")
public class NoticiaRest {

    @EJB
    NoticiaBean noticiaBean;

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML })
    public Response getNoticias(@QueryParam("q") String q, @HeaderParam("Accept") String headerParam) throws ABCApiException, IOException {

        try {
            ResponseListaNoticiasDto responseDto = new ResponseListaNoticiasDto();
            List<NoticiaDto> listaNoticias = noticiaBean.getNoticias(q);
            responseDto.setLista(listaNoticias);
            return Response.status(200).entity(responseDto).build();

        } catch (ABCApiException e) {
            ResponseErrorDto responseError = new ResponseErrorDto();
            responseError.setCodigo(e.getCodigo());
            responseError.setError(e.getDescripcion());

            return Response.status(e.getStatus()).entity(responseError).build();
        } catch (Exception e) {
            ResponseErrorDto responseError = new ResponseErrorDto();
            responseError.setCodigo(ErrorMessage.INTERNAL_ERROR.getCodigo());
            responseError.setError(ErrorMessage.INTERNAL_ERROR.getError());

            return Response.status(ErrorMessage.INTERNAL_ERROR.getStatus())
                    .entity(responseError).build();
        }

    }

}
