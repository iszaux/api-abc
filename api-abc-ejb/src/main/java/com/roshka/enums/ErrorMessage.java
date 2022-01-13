package com.roshka.enums;

public enum ErrorMessage {

    NOT_FOUND(404, "g267", "No se encuentran noticias para el texto: {TEXTO DE BÚSQUEDA}"),
    INVALID_PARAMS(400, "g268", "Parámetros inválidos"),
    INTERNAL_ERROR(500, "g100", "Error interno del servidor"),
    UNAUTHORIZED(403, "g103", "No Autorizado");

    private int status;
    private String codigo;
    private String error;

    private ErrorMessage(int status, String codigo, String error) {
        this.status = status;
        this.codigo = codigo;
        this.error = error;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
