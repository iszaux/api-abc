package com.roshka.beans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roshka.daos.TokenDao;
import com.roshka.dtos.DatumDto;
import com.roshka.dtos.NoticiaDto;
import com.roshka.dtos.ResponseDto;
import com.roshka.enums.ErrorMessage;
import com.roshka.models.Tokens;
import com.roshka.utils.ABCApiException;
import com.roshka.utils.DateHelper;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Stateless
public class NoticiaBean {
    final String pathPrincipal = "https://www.abc.com.py";

    @EJB
    TokenDao tokenDao;

    /**
     * Retorna la lista de noticias traidas desde la pagina de ABC
     * 
     * @param param el texto ingresado como filtro para la busqueda de las noticias
     * @return la lista de noticias
     * @throws ABCApiException
     * @throws IOException
     */
    public List<NoticiaDto> getNoticias(String qParam, Boolean fParam, String apiKey)
            throws ABCApiException, IOException {

        // se valida el apikey ingresado en el header
        validarToken(apiKey);

        // si el parametro esta vacio
        if (qParam.trim().equals(""))
            throw new ABCApiException(ErrorMessage.INVALID_PARAMS);

        disableSslVerification();
        // se obtiene el documento HTML
        Document doc = Jsoup.connect(pathPrincipal + "/buscar/" + qParam).get();
        Element listaNoticiasHtml = doc.getElementById("fusion-metadata");
        if (listaNoticiasHtml == null)
            throw new ABCApiException(ErrorMessage.INTERNAL_ERROR);

        // se obtiene un string con el texto donde se encuentran las noticias
        // No encontre una forma mas eficiente de extraer el json del script en el html
        String text = listaNoticiasHtml.data();
        int first = text.indexOf("globalContent="); // inicio del json
        text = text.substring(first);
        first = text.indexOf("{");
        int last = text.indexOf(";"); // ultimo caracter del json
        text = text.substring(first, last); // se obtiene el json en forma de string

        ObjectMapper om = new ObjectMapper();
        // se convierte el string en objeto
        ResponseDto responseJson = om.readValue(text, ResponseDto.class);
        // si la lista de noticias esta vacia se arroja una exepcion
        if (responseJson.getData().isEmpty())
            throw new ABCApiException(ErrorMessage.NOT_FOUND, qParam);

        return validateList(responseJson, fParam);
    }

    /**
     * Retorna la lista de Noticias recuperadas desde la web
     * 
     * @param responseDto es la respuesta de la web convertida en objeto
     * @throws ABCApiException
     * @return retorna la lista de Noticias en el formato requerido
     */
    public List<NoticiaDto> validateList(ResponseDto responseDto, Boolean fParam) throws ABCApiException {

        List<NoticiaDto> listaNoticias = new ArrayList<>();

        for (DatumDto dto : responseDto.getData()) {
            String urlFoto = dto.getPromoItems().getBasic().getURL();

            NoticiaDto noticia = new NoticiaDto();
            List<String> urlNoticias = dto.getWebsiteUrls();
            if (!urlNoticias.isEmpty())
                noticia.setEnlace(pathPrincipal + "" + urlNoticias.get(0));
            else
                noticia.setEnlace("No hay URL disponible");
            noticia.setTitulo(dto.getHeadlines().getBasic());
            noticia.setResumen(dto.getSubheadlines().getBasic());
            Date fecha = DateHelper.stringToDate(dto.getPublishDate());
            noticia.setFecha(DateHelper.getDateISO8601(fecha));
            noticia.setEnlaceFoto(dto.getPromoItems().getBasic().getURL());

            if (fParam) {
                if (!urlFoto.trim().equals("")) {
                    noticia.setContenidoFoto(getByteArrayFromImageURL(urlFoto));
                    noticia.setContentTypeFoto("image/jpeg;base64");
                }
            }

            listaNoticias.add(noticia);
        }

        return listaNoticias;

    }

    /**
     * Valida el apikey ingresado por el usuario en el header
     * 
     * @param apikey apikey ingresado por el usuario
     * @throws ABCApiException
     *
     */
    public void validarToken(String apikey) throws ABCApiException {
        // se verifica si en la base de datos existe el apikey
        Tokens token = tokenDao.finApiKey(apikey);

        if (token == null)
            throw new ABCApiException(ErrorMessage.UNAUTHORIZED);
    }

    /**
     * Codifica la imagen de una url en Base64
     * 
     * @param url url de la imagen
     * @throws ABCApiException
     * @return retorna un string de la imagen codificada
     */
    private String getByteArrayFromImageURL(String imageURL) {

        try {
            URL url = new java.net.URL(imageURL);
            InputStream is = url.openStream();
            byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            System.out.println("Error " + e.toString());
        }
        return null;
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
        } catch (

        NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
