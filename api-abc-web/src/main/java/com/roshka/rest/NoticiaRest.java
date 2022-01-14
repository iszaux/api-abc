package com.roshka.rest;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.roshka.beans.NoticiaBean;
import com.roshka.dtos.NoticiaDto;
import com.roshka.dtos.ResponseErrorDto;
import com.roshka.dtos.ResponseListaNoticiasDto;
import com.roshka.enums.ErrorMessage;
import com.roshka.utils.ABCApiException;

import io.swagger.v3.oas.annotations.Operation;



@Path("/consulta")
public class NoticiaRest {

    @EJB
    NoticiaBean noticiaBean;

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
    @Operation(summary = "Recupera una lista de noticias de ABC de acuerdo a un filtro de busqueda")
    public Response getNoticias(@QueryParam("q") String qParam, @HeaderParam("Accept") String headerParam,
            @DefaultValue("false") @QueryParam("f") Boolean fParam,
            @DefaultValue("") @HeaderParam("api-key") String apikeyHeader) throws ABCApiException, IOException {

        try {

            ResponseListaNoticiasDto responseDto = new ResponseListaNoticiasDto();
            List<NoticiaDto> listaNoticias = noticiaBean.getNoticias(qParam, fParam, apikeyHeader);
            responseDto.setLista(listaNoticias);
            return Response.status(Status.OK).entity(responseDto).build();

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
