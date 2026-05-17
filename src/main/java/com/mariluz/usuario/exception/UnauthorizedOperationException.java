package com.mariluz.usuario.exception;

// excepcion para manejo de permisos
public class UnauthorizedOperationException extends RuntimeException {

    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
