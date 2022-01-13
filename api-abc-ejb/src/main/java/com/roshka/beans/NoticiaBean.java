package com.roshka.beans;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import javax.ejb.Stateless;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roshka.dto.DatumDto;
import com.roshka.dto.NoticiaDto;
import com.roshka.dto.ResponseDto;
import com.roshka.enums.ErrorMessage;
import com.roshka.utils.ABCApiException;
import com.roshka.utils.DateHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Stateless
public class NoticiaBean {
    final String pathPrincipal = "https://www.abc.com.py";

    /**
     * Retorna la lista de noticias traidas desde la pagina de ABC
     * 
     * @param param el texto ingresado como filtro para la busqueda de las noticias
     * @return la lista de noticias
     * @throws ABCApiException
     * @throws IOException
     */
    public List<NoticiaDto> getNoticias(String param) throws ABCApiException, IOException {

        // si el parametro esta vacio
        if (param.trim().equals(""))
            throw new ABCApiException(ErrorMessage.INVALID_PARAMS);

        disableSslVerification();
        // se obtiene el documento HTML
        Document doc = Jsoup.connect(pathPrincipal + "/buscar/" + param).get();
        Element listaNoticiasHtml = doc.getElementById("fusion-metadata");
        if (listaNoticiasHtml == null)
            throw new ABCApiException(ErrorMessage.INTERNAL_ERROR);

        // se obtiene un string con el texto donde se encuentran las noticias
        String text = listaNoticiasHtml.data();
        int first = text.indexOf("globalContent="); // inicio del json
        System.out.println(" First: " + first);
        text = text.substring(first);
        first = text.indexOf("{");
        int last = text.indexOf(";"); // ultimo caracter del json
        System.out.println(" Last: " + last);
        text = text.substring(first, last); // se obtiene el json en forma de string

        ObjectMapper om = new ObjectMapper();
        // se convierte el string en objeto
        ResponseDto responseJson = om.readValue(text, ResponseDto.class);

        return validateList(responseJson);
    }

    /**
     * Retorna la lista de Noticias recuperadas desde la web
     * 
     * @param responseDto es la respuesta de la web convertida en objeto
     * @throws ABCApiException
     * @return retorna la lista de Noticias en el formato requerido
     */
    public List<NoticiaDto> validateList(ResponseDto responseDto) throws ABCApiException {
        if (responseDto.getData().isEmpty())
            throw new ABCApiException(ErrorMessage.NOT_FOUND);

        List<NoticiaDto> listaNoticias = new ArrayList<>();
        for (DatumDto dto : responseDto.getData()) {
            NoticiaDto noticia = new NoticiaDto();
            noticia.setEnlace(pathPrincipal + "" + dto.getWebsiteUrls().get(0));
            noticia.setTitulo(dto.getHeadlines().getBasic());
            noticia.setResumen(dto.getSubheadlines().getBasic());
            Date fecha = DateHelper.stringToDate(dto.getPublishDate());
            noticia.setFecha(dto.getPublishDate());
            noticia.setEnlaceFoto(dto.getPromoItems().getBasic().getURL());

            listaNoticias.add(noticia);
        }

        return listaNoticias;

    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }

            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
