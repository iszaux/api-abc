package com.roshka.utils;

import com.roshka.enums.ErrorMessage;

public class ABCApiException extends Exception {

    private String descripcion;
    private String codigo;
    private int status;

    public ABCApiException() {
        super();
    }

    public ABCApiException(String message) {
        super(message);
    }

    public ABCApiException(Throwable cause) {
        super(cause);
    }

    public ABCApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ABCApiException(ErrorMessage message) {
        this.descripcion = message.getError();
        this.codigo = message.getCodigo();
        this.status = message.getStatus();
    }

    public ABCApiException(ErrorMessage message, String infExtra) {
        this.descripcion = message.getError() + " " + infExtra;
        this.codigo = message.getCodigo();
        this.status = message.getStatus();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
