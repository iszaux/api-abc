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
import com.roshka.dto.ResponseErrorDto;
import com.roshka.enums.ErrorMessage;
import com.roshka.utils.ABCApiException;

@Path("/consulta")
public class NoticiaRest {

    @EJB
    NoticiaBean noticiaBean;

    @GET
    @Produces("application/json")
    public Response getNoticias(@QueryParam("q") String q) throws ABCApiException, IOException {

        try {

            List<NoticiaDto> listaNoticias = noticiaBean.getNoticias(q);
            return Response.status(200).entity(listaNoticias).build();

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
