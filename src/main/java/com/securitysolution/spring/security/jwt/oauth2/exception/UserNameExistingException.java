package com.securitysolution.spring.security.jwt.oauth2.exception;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserNameExistingException extends RuntimeException {
    private  String entityType;  // Tipo de la entidad (CursosService, Tema, etc.)
    private  String operation; // Operación (Ejemplo: Save, update)
    private String username;


    public UserNameExistingException(String message) {
        super(message);
    }

    public UserNameExistingException( String username, String entityType, String operation) {
        this.username = username;
        this.entityType = entityType;
        this.operation = operation;
    }
}
