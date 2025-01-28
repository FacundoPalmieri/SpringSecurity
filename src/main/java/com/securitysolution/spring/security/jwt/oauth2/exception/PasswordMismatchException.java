package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;

@Getter
public class PasswordMismatchException extends RuntimeException {

    private String userCreador;
    private String userNuevo;

    public PasswordMismatchException(String message, String userCreador, String userNuevo) {
        super(message);
        this.userCreador = userCreador;
        this.userNuevo = userNuevo;
    }

    public PasswordMismatchException(String userNuevo) {
        this.userNuevo = userNuevo;
    }
}

